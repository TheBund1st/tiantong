package com.thebund1st.tiantong.jdbc;

import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;


@RequiredArgsConstructor
public class JdbcOnlineRefundRepository implements OnlineRefundRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(OnlineRefund model) {

    }


}
