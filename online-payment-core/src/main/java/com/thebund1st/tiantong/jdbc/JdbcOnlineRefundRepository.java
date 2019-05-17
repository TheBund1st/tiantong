package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@RequiredArgsConstructor
public class JdbcOnlineRefundRepository implements OnlineRefundRepository {

    private static final String OR_COLUMNS = "ID, VERSION, AMOUNT, OP_ID, OP_AMOUNT, CORRELATION_KEY, CORRELATION_VALUE, " +
            "STATUS, METHOD, CREATED_AT, LAST_MODIFIED_AT";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(OnlineRefund model) {
        jdbcTemplate.update(insertSql(),
                model.getId().getValue(),
                model.getVersion(),
                model.getAmount(),
                model.getOnlinePaymentId().getValue(),
                model.getOnlinePaymentAmount(),
                model.getCorrelation().getKey(),
                model.getCorrelation().getValue(),
                model.getStatus().getValue(),
                model.getMethod().getValue(),
                Timestamp.valueOf(model.getCreatedAt()),
                Timestamp.valueOf(model.getLastModifiedAt())
        );
    }

    protected String insertSql() {
        return String.format("INSERT INTO ONLINE_REFUND(%s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", OR_COLUMNS);
    }

    @Override
    public OnlineRefund mustFindBy(OnlineRefund.Identifier id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ONLINE_REFUND WHERE ID = ?",
                new Object[]{id.getValue()},
                (rs, rowNum) -> {
                    OnlineRefund or = new OnlineRefund();
                    or.setId(OnlineRefund.Identifier.of(rs.getString("ID")));
                    or.setVersion(rs.getInt("VERSION"));
                    or.setAmount(rs.getDouble("AMOUNT"));
                    or.setOnlinePaymentId(OnlinePayment.Identifier.of(rs.getString("OP_ID")));
                    or.setOnlinePaymentAmount(rs.getDouble("OP_AMOUNT"));
                    or.setCorrelation(OnlinePayment.Correlation.of(rs.getString("CORRELATION_KEY"),
                            rs.getString("CORRELATION_VALUE")));
                    or.setStatus(OnlineRefund.Status.of(rs.getInt("STATUS")));
                    or.setMethod(Method.of(rs.getString("METHOD")));
                    or.setCreatedAt(toDateTime(rs, "CREATED_AT"));
                    or.setLastModifiedAt(toDateTime(rs, "LAST_MODIFIED_AT"));
                    return or;
                });
    }

    private LocalDateTime toDateTime(ResultSet rs, String columnLabel) throws java.sql.SQLException {
        return rs.getTimestamp(columnLabel).toLocalDateTime();
    }

}
