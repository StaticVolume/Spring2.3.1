package spring.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("spring")
@PropertySource("classpath:hibernate.properties")
public class Appconf implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    /**в environment находится информация из  hibernate properties*/
    private final Environment environment;

    @Autowired // не обязательно
    public Appconf(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }


    /**Конфигурация необходимая для обращения к html страницам (allUsers, index и т.д.) не использую полный путь*/
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**Конфигурация необходимая для работы thymeleaf */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    /**Конфигурация необходимая для работы thymeleaf */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    /**Конфигурируем поделючение к к базе данных*/
@Bean
public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("hibernate.driver_class"));
    dataSource.setUrl(environment.getRequiredProperty("hibernate.connection.url"));
    dataSource.setUsername(environment.getRequiredProperty("hibernate.connection.username"));
    dataSource.setPassword(environment.getRequiredProperty("hibernate.connection.password"));

    return dataSource;
}
    /**Конфигурируем сам hibernate, это лично его параметры*/
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.current_session_context_class", environment.getRequiredProperty("hibernate.current_session_context_class"));
        return properties;
    }

    /**Для того, чтобы адекватно работал EntityManager , необходимо сконфигурировать LocalContainerEntityManagerFactoryBean */
    @Bean
    public LocalContainerEntityManagerFactoryBean EntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean EntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean ();
        EntityManagerFactoryBean.setDataSource(dataSource());
        EntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        EntityManagerFactoryBean.setJpaProperties(hibernateProperties());
        EntityManagerFactoryBean.setPackagesToScan("spring.model");
        return EntityManagerFactoryBean;
    }

    /**Инициализируем EntityManager*/
    @Bean
    public EntityManager entityManager(){
        EntityManager entityManager = EntityManagerFactoryBean().getNativeEntityManagerFactory().createEntityManager();
        return  entityManager;
    }

    /**Для того, чтобы корректно работала функция, помеченная @Transactional*/

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(EntityManagerFactory entityManagerFactory) {
            JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
        return transactionManager;
    }


}
