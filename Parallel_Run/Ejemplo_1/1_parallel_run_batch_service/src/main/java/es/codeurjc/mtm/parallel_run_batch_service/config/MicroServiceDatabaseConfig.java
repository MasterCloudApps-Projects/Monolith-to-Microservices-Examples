package es.codeurjc.mtm.parallel_run_batch_service.config;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "microEntityManagerFactory", transactionManagerRef = "microTransactionManager", basePackages = {
    "es.codeurjc.mtm.parallel_run_batch_service.repository.micro"})

public class MicroServiceDatabaseConfig {

  @Autowired
  private Environment env;

  @Bean
  public DataSource microDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("microservice.datasource.url")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("microservice.datasource.username")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("microservice.datasource.password")));
    dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("microservice.datasource.driver-class-name")));
    return dataSource;
  }
  @Bean
  public LocalContainerEntityManagerFactoryBean microEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(microDataSource());
    em.setPackagesToScan("es.codeurjc.mtm.parallel_run_batch_service.model");

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    properties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
    properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));

    em.setJpaPropertyMap(properties);

    return em;
  }
  @Bean
  public PlatformTransactionManager microTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(microEntityManagerFactory().getObject());

    return transactionManager;
  }

}
