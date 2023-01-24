package com.example.boardservice.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages="com.example.boardservice.dao")
public class MybatisConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactory=new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(datasource);
        sqlSessionFactory.setTypeAliasesPackage("com.example.boardservice.domain");
        sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}