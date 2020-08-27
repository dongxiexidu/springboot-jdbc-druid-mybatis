package com.etrans.jdbc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private Integer id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;
}
