package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public class JdbcOnlinePaymentResultNotificationRepository implements OnlinePaymentResultNotificationRepository {

    private static final String COLUMNS = "ID, OP_ID, AMOUNT, CODE, TEXT, CREATED_AT";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(OnlinePaymentResultNotification model) {
        jdbcTemplate.update(insertOnlinePaymentSql(),
                model.getId().getValue(),
                model.getOnlinePaymentId().getValue(),
                model.getAmount(),
                model.getCode().getValue(),
                model.getText(),
                model.getCreatedAt()
        );
    }

    @Override
    public OnlinePaymentResultNotification mustFindBy(OnlinePaymentResultNotification.Identifier id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ONLINE_PAYMENT_RESULT_NOTIFICATION WHERE ID = ?",
                new Object[]{id.getValue()},
                (rs, rowNum) -> {
                    OnlinePaymentResultNotification model = new OnlinePaymentResultNotification();
                    model.setId(OnlinePaymentResultNotification.Identifier.of(rs.getString("ID")));
                    model.setOnlinePaymentId(OnlinePayment.Identifier.of(rs.getString("OP_ID")));
                    model.setAmount(rs.getDouble("AMOUNT"));
                    model.setCode(OnlinePaymentResultNotification.Code.of(rs.getInt("CODE")));
                    model.setText(rs.getString("TEXT"));
                    model.setCreatedAt(rs.getObject("CREATED_AT", LocalDateTime.class));
                    return model;
                });
    }

    protected String insertOnlinePaymentSql() {
        return String.format("INSERT INTO ONLINE_PAYMENT_RESULT_NOTIFICATION(%s) VALUES (?, ?, ?, ?, ?, ?)", COLUMNS);
    }

}
