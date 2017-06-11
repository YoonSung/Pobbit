package config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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

	@Autowired
	Environment environment;

	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();

		String driverClassName = environment.getProperty("database.driverClassName");
		String url = environment.getProperty("database.url");
		String username = environment.getProperty("database.username");
		String password = environment.getProperty("database.password");

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

		String jpaDialect = "hibernate.dialect";
		String jpaFormatSql = "hibernate.format_sql";
		String jpaNamingStrategy = "hibernate.ejb.naming_strategy";
		String jpaShowSql = "hibernate.show_sql";
		String jpaOperationMode="hibernate.hbm2ddl.auto";

		Properties jpaProperties = new Properties();
		jpaProperties.put(jpaDialect, environment.getProperty(jpaDialect));
		jpaProperties.put(jpaFormatSql, environment.getProperty(jpaFormatSql));
		jpaProperties.put(jpaNamingStrategy, environment.getProperty(jpaNamingStrategy));
		jpaProperties.put(jpaShowSql, environment.getProperty(jpaShowSql));
		jpaProperties.put(jpaOperationMode, environment.getProperty(jpaOperationMode));

		/*
		jpaProperties.put("hibernate.cache.provider_class", "org.hibernate.cache.SingletonEhCacheProvider");
		jpaProperties.put("hibernate.cache.use_second_level_cache", "false");
		jpaProperties.put("hibernate.cache.use_query_cache", "false");
		*/

		//jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");

		entityManagerFactoryBean.setJpaProperties(jpaProperties);
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
