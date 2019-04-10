package com.thebund1st.tiantong.boot.jdbc;

import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentResultNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfiguration {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcOnlinePaymentRepository jdbcOnlinePaymentRepository() {
        return new JdbcOnlinePaymentRepository(jdbcTemplate);
    }

    @Bean
    public JdbcOnlinePaymentResultNotificationRepository jdbcOnlinePaymentResponseRepository() {
        return new JdbcOnlinePaymentResultNotificationRepository(jdbcTemplate);
    }
}
