package config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@PropertySource("classpath:/db.properties")
@EnableJpaRepositories(basePackages={ "domain"})    //JPA Repository Class Scanning
@EnableTransactionManagement//Enable Transaction Annotation Setting. It is same with <tx:annotation-driven/>
@Configuration
public class DbConfig {

	@Value("${database.driverClassName}")
	private String driverClassName;
	
	@Value("${database.url}")
	private String url;

	@Value("${database.username}")
	private String username;

	@Value("${database.password}")
	private String password;
	
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddl;

	@Value("${hibernate.format_sql}")
	private String formatSql;
	
	@Value("${hibernate.ejb.naming_strategy}")
	private String namingStrategy;
	
	@Value("${hibernate.show_sql}")
	private String showSql;

	public Properties getJpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
		properties.put("hibernate.format_sql", formatSql);
		properties.put("hibernate.ejb.naming_strategy", namingStrategy);
		properties.put("hibernate.show_sql", showSql);
		return properties;
	}

	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("domain");
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

		entityManagerFactoryBean.setJpaProperties(getJpaProperties());
		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(
				entityManagerFactory().getObject()
		);

		return transactionManager;
	}
}
