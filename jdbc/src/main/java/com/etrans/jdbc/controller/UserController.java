package com.etrans.jdbc.controller;

import com.etrans.jdbc.mapper.UserMapper;
import com.etrans.jdbc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/mybatis")
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/queryUserList")
    public List<User> queryUserList() {
        List<User> users = userMapper.queryUserList();
        return users;
    }

    @GetMapping("/addUser")
    public String addUser() {
        User user = new User("ldx","123");
        userMapper.addUser(user);
        return "addUser-ok";
    }

    @GetMapping("/updateUser")
    public String updateUser() {
        User user = userMapper.queryUserById(1);
        user.setUsername("东邪西毒");
        userMapper.updateUser(user);
        return "updateUser-ok";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userMapper.deleteUser(id);
        return "deleteUser-ok";
    }

    @GetMapping("/queryUserById/{id}")
    public User queryUserById(@PathVariable("id") int id) {
        User user = userMapper.queryUserById(id);
        return user;
    }

    @GetMapping("/queryForUserCount")
    public Integer queryForUserCount() {
        Integer count = userMapper.queryForUserCount();
        return count;
    }

}
