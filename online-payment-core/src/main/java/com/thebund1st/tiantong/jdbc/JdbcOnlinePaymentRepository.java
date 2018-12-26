package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.time.LocalDateTime;


@RequiredArgsConstructor
public class JdbcOnlinePaymentRepository implements OnlinePaymentRepository {

    private static final String OP_COLUMNS = "ID, VERSION, AMOUNT, CORRELATION_KEY, CORRELATION_VALUE, STATUS, " +
            "METHOD, CREATED_AT, LAST_MODIFIED_AT";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(OnlinePayment model) {
        jdbcTemplate.update(insertOnlinePaymentSql(),
                model.getId().getValue(),
                model.getVersion(),
                model.getAmount(),
                model.getCorrelation().getKey(),
                model.getCorrelation().getValue(),
                model.getStatus().getValue(),
                model.getMethod().getValue(),
                model.getCreatedAt(),
                model.getLastModifiedAt()
        );
    }

    protected String insertOnlinePaymentSql() {
        return String.format("INSERT INTO ONLINE_PAYMENT(%s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", OP_COLUMNS);
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
                    op.setStatus(OnlinePayment.Status.of(rs.getInt("STATUS")));
                    op.setMethod(OnlinePayment.Method.of(rs.getString("METHOD")));
                    op.setCreatedAt(toDateTime(rs, "CREATED_AT"));
                    op.setLastModifiedAt(toDateTime(rs, "LAST_MODIFIED_AT"));
                    return op;
                });
    }

    private LocalDateTime toDateTime(ResultSet rs, String columnLabel) throws java.sql.SQLException {
        return rs.getObject(columnLabel, LocalDateTime.class);
    }
}
