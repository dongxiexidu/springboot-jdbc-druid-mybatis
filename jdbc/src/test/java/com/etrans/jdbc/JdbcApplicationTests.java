package com.etrans.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
class JdbcApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws Exception {
        System.out.println("class======"+dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection======"+connection);
        connection.close();
    }

}
