package es.codeurjc.mtm.parallel_run_notification_ms.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Primary;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "monolithEntityManagerFactory", transactionManagerRef = "monolithTransactionManager", basePackages = {"es.codeurjc.mtm.parallel_run_notification_ms.repository.mono"})

public class MonolithDatabaseConfig {

  @Autowired
  private Environment env;

  public MonolithDatabaseConfig() {
    super();
  }
  @Bean
  public DataSource monolithDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("mono.datasource.url")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("mono.datasource.username")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("mono.datasource.password")));
    dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("mono.datasource.driver-class-name")));
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean monolithEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(monolithDataSource());
    em.setPackagesToScan("es.codeurjc.mtm.parallel_run_notification_ms.model");

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("mono.jpa.hibernate.ddl-auto"));
    properties.put("hibernate.show-sql", env.getProperty("mono.jpa.show-sql"));
    properties.put("hibernate.dialect", env.getProperty("mono.jpa.database-platform"));

    em.setJpaPropertyMap(properties);

    return em;
  }
  @Bean
  public PlatformTransactionManager monolithTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(monolithEntityManagerFactory().getObject());

    return transactionManager;
  }

}
