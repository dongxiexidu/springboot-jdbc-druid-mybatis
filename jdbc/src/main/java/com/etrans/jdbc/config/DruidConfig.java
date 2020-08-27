package com.etrans.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    // http://localhost:8080/druid/sql.html
    // 后台监控: web.xml
    // 因为springboot内置了servlet容器,所以没有web.xml, 替代方法: ServletRegistrationBean
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        HashMap<String, String> initParameters = new HashMap<>();
        // key是固定的(loginUsername,loginPassword)
        initParameters.put("loginUsername", "root");
        initParameters.put("loginPassword", "1234");
        // 允许谁可以访问
        initParameters.put("allow", "");
        // 禁止谁能访问
        //initParameters.put("kuang", "192.168.11.12");

        bean.setInitParameters(initParameters);
        return bean;
    }

}
