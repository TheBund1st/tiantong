package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobStore;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.core.payable.Payable;
import com.thebund1st.tiantong.core.payee.Payee;
import com.thebund1st.tiantong.json.deserializers.ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher;
import com.thebund1st.tiantong.json.serializers.ProviderSpecificOnlinePaymentRequestJsonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING;


@RequiredArgsConstructor
public class JdbcOnlinePaymentRepository implements
        OnlinePaymentRepository,
        OnlinePaymentResultSynchronizationJobStore {

    private static final String OP_COLUMNS = "ID, VERSION, AMOUNT, CORRELATION_KEY, CORRELATION_VALUE, STATUS, " +
            "PAYEE_CONTEXT, PAYEE_OBJ_ID, METHOD, SUBJECT, BODY, PROVIDER_SPECIFIC_INFO, " +
            "CREATED_AT, LAST_MODIFIED_AT, EXPIRES_AT";

    private final JdbcTemplate jdbcTemplate;

    private final ProviderSpecificOnlinePaymentRequestJsonSerializer providerSpecificOnlinePaymentRequestJsonSerializer;

    private final ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher providerSpecificOnlinePaymentRequestJsonDeserializer;

    @Override
    public void save(OnlinePayment model) {
        jdbcTemplate.update(insertOnlinePaymentSql(),
                model.getId().getValue(),
                model.getVersion(),
                model.getAmount(),
                model.getPayable().getContext(),
                model.getPayable().getObjectId(),
                model.getStatus().getValue(),
                model.getPayee().getContext(),
                model.getPayee().getObjectId(),
                model.getMethod().getValue(),
                model.getSubject(),
                model.getBody(),
                providerSpecificOnlinePaymentRequestJsonSerializer
                        .serialize(model.getProviderSpecificOnlinePaymentRequest()),
                Timestamp.valueOf(model.getCreatedAt()),
                Timestamp.valueOf(model.getLastModifiedAt()),
                Timestamp.valueOf(model.getExpiresAt())
        );
    }

    protected String insertOnlinePaymentSql() {
        return String.format("INSERT INTO ONLINE_PAYMENT(%s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                OP_COLUMNS);
    }

    @Override
    public OnlinePayment mustFindBy(OnlinePayment.Identifier id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ONLINE_PAYMENT WHERE ID = ?",
                new Object[]{id.getValue()},
                (rs, rowNum) -> {
                    OnlinePayment op = new OnlinePayment();
                    op.setId(OnlinePayment.Identifier.of(rs.getString("ID")));
                    op.setVersion(rs.getInt("VERSION"));
                    op.setAmount(rs.getDouble("AMOUNT"));
                    op.setCorrelation(OnlinePayment.Correlation.of(rs.getString("CORRELATION_KEY"),
                            rs.getString("CORRELATION_VALUE")));
                    op.setPayable(Payable.of(rs.getString("CORRELATION_KEY"),
                            rs.getString("CORRELATION_VALUE")));
                    op.setPayee(Payee.of(rs.getString("PAYEE_CONTEXT"),
                            rs.getString("PAYEE_OBJ_ID")));
                    op.setStatus(OnlinePayment.Status.of(rs.getInt("STATUS")));
                    op.setMethod(Method.of(rs.getString("METHOD")));
                    op.setSubject(rs.getString("SUBJECT"));
                    op.setBody(rs.getString("BODY"));
                    op.setProviderSpecificInfo(rs.getString("PROVIDER_SPECIFIC_INFO"));
                    op.setProviderSpecificOnlinePaymentRequest(providerSpecificOnlinePaymentRequestJsonDeserializer
                            .deserialize(rs.getString("METHOD"),
                                    rs.getString("PROVIDER_SPECIFIC_INFO")));
                    op.setCreatedAt(toDateTime(rs, "CREATED_AT"));
                    op.setLastModifiedAt(toDateTime(rs, "LAST_MODIFIED_AT"));
                    op.setExpiresAt(toDateTime(rs, "EXPIRES_AT"));
                    return op;
                });
    }

    @Override
    public void update(OnlinePayment onlinePayment) {
        String onlinePaymentId = onlinePayment.getId().getValue();
        int onlinePaymentVersion = onlinePayment.getVersion();
        int rowUpdated = jdbcTemplate.update("UPDATE ONLINE_PAYMENT SET VERSION = VERSION + 1, " +
                        "STATUS = ?, " +
                        "LAST_MODIFIED_AT = ? " +
                        "WHERE ID = ? " +
                        "AND VERSION = ?",
                onlinePayment.getStatus().getValue(),
                Timestamp.valueOf(onlinePayment.getLastModifiedAt()),
                onlinePaymentId,
                onlinePaymentVersion
        );
        if (rowUpdated != 1) {
            throw new OptimisticLockingFailureException(String.format("Conflict when updating Online Payment [%s][%d]",
                    onlinePaymentId, onlinePaymentVersion));
        }
    }

    private LocalDateTime toDateTime(ResultSet rs, String columnLabel) throws java.sql.SQLException {
        return rs.getTimestamp(columnLabel).toLocalDateTime();
    }

    @Override
    public Page<OnlinePaymentResultSynchronizationJob> find(LocalDateTime localDateTime, Pageable pageable) {
        //FIXME extract where clause
        @SuppressWarnings("ConstantConditions")
        long totalElements = jdbcTemplate.queryForObject("SELECT COUNT(ID) AS TOTAL_ELEMENTS FROM ONLINE_PAYMENT " +
                        "WHERE STATUS = ? " +
                        "AND CREATED_AT <= ? ",
                new Object[]{
                        PENDING.getValue(),
                        Timestamp.valueOf(localDateTime)
                },
                (rs, rowNum) -> rs.getLong("TOTAL_ELEMENTS"));
        List<OnlinePaymentResultSynchronizationJob> content = jdbcTemplate.query("SELECT * FROM ONLINE_PAYMENT " +
                        "WHERE STATUS = ? " +
                        "AND CREATED_AT <= ? " +
                        "ORDER BY CREATED_AT ASC " +
                        "LIMIT ?, ?",
                new Object[]{
                        PENDING.getValue(),
                        Timestamp.valueOf(localDateTime),
                        pageable.getOffset(),
                        pageable.getPageSize()
                },
                (rs, rowNum) -> {
                    OnlinePaymentResultSynchronizationJob job = new OnlinePaymentResultSynchronizationJob();
                    job.setOnlinePaymentId(OnlinePayment.Identifier.of(rs.getString("ID")));
                    return job;
                });
        return new PageImpl<>(content, pageable, totalElements);
    }
}
