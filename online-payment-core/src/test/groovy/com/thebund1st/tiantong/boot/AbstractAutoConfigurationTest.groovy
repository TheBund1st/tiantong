package com.thebund1st.tiantong.boot

import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger

class AbstractAutoConfigurationTest extends Specification {
    protected final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OnlinePaymentAutoConfiguration.class))
            .withUserConfiguration(JdbcTemplateConfiguration)
            .withUserConfiguration(JacksonAutoConfiguration)

    @Configuration
    static class JdbcTemplateConfiguration {

        @Bean
        JdbcTemplate jdbcTemplate() {
            def template = new JdbcTemplate()
            template.setDataSource(new DataSource() {
                @Override
                Connection getConnection() throws SQLException {
                    return null
                }

                @Override
                Connection getConnection(String username, String password) throws SQLException {
                    return null
                }

                @Override
                def <T> T unwrap(Class<T> iface) throws SQLException {
                    return null
                }

                @Override
                boolean isWrapperFor(Class<?> iface) throws SQLException {
                    return false
                }

                @Override
                PrintWriter getLogWriter() throws SQLException {
                    return null
                }

                @Override
                void setLogWriter(PrintWriter out) throws SQLException {

                }

                @Override
                void setLoginTimeout(int seconds) throws SQLException {

                }

                @Override
                int getLoginTimeout() throws SQLException {
                    return 0
                }

                @Override
                Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return null
                }
            })
            return template
        }
    }
}
