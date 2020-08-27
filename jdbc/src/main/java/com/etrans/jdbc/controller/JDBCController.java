package com.etrans.jdbc.controller;

import com.etrans.jdbc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/jdbc")
@RestController // 导入web才能用,当前Controller只能返回字符串
public class JDBCController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/userList")
    public List<Map<String, Object>> userList() {
        String sql = "select * from t_user";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @GetMapping("/queryUserById/{id}")
    public Map<String, Object> queryUserById(@PathVariable("id") int id) {
        String sql = "select * from t_user where id = " + id;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list.size() == 1 ? list.get(0) : null;
    }


    @GetMapping("/addUser")
    public String addUser() {
        String sql = "insert into jdbcDemo.t_user (username,password) values ('ldx',12345)";
        jdbcTemplate.update(sql);
        return "addUser-ok";
    }

    // 尽可能不要拼接SQL
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        String sql = "delete from jdbcDemo.t_user where id = ?";
        jdbcTemplate.update(sql, id);
        return "deleteUser-ok";
    }

    // 注解中用{}来表明它的变量部分
    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") int id) {
        String sql = "update jdbcDemo.t_user set username = ?, password = ? where id = " + id;
        Object[] object = new Object[2];
        object[0] = "dongxi";
        object[1] = "6666";
        jdbcTemplate.update(sql, object);
        return "updateUser-ok";
    }

}
