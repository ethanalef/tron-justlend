package org.tron.justlend.justlendapiserver.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.tron.justlend.justlendapiserver.core.ProjectConstant;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(
  basePackages = ProjectConstant.MAPPER_PACKAGE,
  sqlSessionFactoryRef = "sqlSessionFactory"
)
public class DataSourceConfig {
  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "dataSourceTransactionManager")
  @Primary
  public DataSourceTransactionManager dataSourceTransactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean(name = "sqlSessionFactory")
  @Primary
  public SqlSessionFactory sqlSessionFactory(
    @Qualifier("dataSource") DataSource dataSource)
    throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                                        .getResources(ProjectConstant.MAPPER_LOCATION));
    setPlugin(sessionFactory, dataSource);
    return sessionFactory.getObject();
  }

  private void setPlugin(SqlSessionFactoryBean factory, DataSource dataSource) {
    factory.setDataSource(dataSource);
    factory.setTypeAliasesPackage(ProjectConstant.MODEL_PACKAGE);

    Properties properties = new Properties();
    properties.setProperty("pageSizeZero", "true");
    properties.setProperty("reasonable", "true");
    properties.setProperty("supportMethodsArguments", "true");

    PageInterceptor pageInterceptor = new PageInterceptor();
    pageInterceptor.setProperties(properties);
    factory.setPlugins(pageInterceptor);
  }
}

