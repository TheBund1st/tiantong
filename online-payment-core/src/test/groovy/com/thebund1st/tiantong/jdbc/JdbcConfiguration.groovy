package com.thebund1st.tiantong.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class JdbcConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Bean
    JdbcOnlinePaymentRepository jdbcOnlinePaymentRepository() {
        return new JdbcOnlinePaymentRepository(jdbcTemplate)
    }
}
