package org.project.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.project.shop.auth.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator();
    }
}
