package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResponse;
import com.thebund1st.tiantong.core.OnlinePaymentResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public class JdbcOnlinePaymentResponseRepository implements OnlinePaymentResponseRepository {

    private static final String COLUMNS = "ID, OP_ID, AMOUNT, CODE, TEXT, CREATED_AT";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(OnlinePaymentResponse model) {
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
    public OnlinePaymentResponse mustFindBy(OnlinePaymentResponse.Identifier id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ONLINE_PAYMENT_RESPONSE WHERE ID = ?",
                new Object[]{id.getValue()},
                (rs, rowNum) -> {
                    OnlinePaymentResponse model = new OnlinePaymentResponse();
                    model.setId(OnlinePaymentResponse.Identifier.of(rs.getString("ID")));
                    model.setOnlinePaymentId(OnlinePayment.Identifier.of(rs.getString("OP_ID")));
                    model.setAmount(rs.getDouble("AMOUNT"));
                    model.setCode(OnlinePaymentResponse.Code.of(rs.getInt("CODE")));
                    model.setText(rs.getString("TEXT"));
                    model.setCreatedAt(rs.getObject("CREATED_AT", LocalDateTime.class));
                    return model;
                });
    }

    protected String insertOnlinePaymentSql() {
        return String.format("INSERT INTO ONLINE_PAYMENT_RESPONSE(%s) VALUES (?, ?, ?, ?, ?, ?)", COLUMNS);
    }

}
