package com.qoobico.remind.server.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.qoobico.remind.server.repository")
@EnableTransactionManagement
@ComponentScan("com.qoobico.remind.server")
@PropertySource("classpath:db.properties")
public class DatabaseConfig {

    @Resource
    private Environment env;

    // show db tables as classes
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        // using JPA adapter Hibernate
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaVendorAdapter(getHibernateProperties());

        return em;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        // load hibernate configs
        InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can`t find file hibernate.properties");
        }

        return properties;
    }

    // for connection to db
    @Bean
    public DataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getRequiredProperty("db.url"));
        ds.setDriverClassName(env.getRequiredProperty("db.driver"));
        ds.setUsername(env.getRequiredProperty("db.username"));
        ds.setPassword(env.getRequiredProperty("db.password"));

        return ds;
    }
}
