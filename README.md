# springboot-jdbc-druid-mybatis
 jdbc-druid-mybatis

### 1.jdbc
```java
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
```

### 2.durid
```java
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
```

## 3.mybatis

### 3.1 mapper接口
```java
/**
 * @Repository和@Controller、@Service、@Component的作用差不多，都是把对象交给spring管理。
 * @Repository用在持久层的接口上，这个注解是将接口的一个实现类交给spring管理。
 */

@Mapper // 表示这个是mybatis的mapper类 dao
@Repository // 表示被Spring整合 @Repository用在持久层的接口上 该注解的作用是将类识别为Bean 存储库
public interface UserMapper {

    List<User> queryUserList();
    User queryUserById(int id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(int id);
    // 查询结果总数
    int queryForUserCount();
}
```
### 3.2 Controller
```java
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
```
### 3.3 配置xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">


<mapper namespace="com.etrans.jdbc.mapper.UserMapper">
    <!-- 查询所有 -->
    <select id="queryUserList" resultType="User">
        SELECT id, username, password FROM t_user
    </select>

    <!-- 查询单个 resultType不区分大小写 -->
    <select id="queryUserById" parameterType="java.lang.Integer" resultType="User">
        SELECT id, username, password FROM t_user WHERE id = #{id}
    </select>

    <!-- 更改操作 -->
    <update id="updateUser" parameterType="com.etrans.jdbc.pojo.User">
        UPDATE t_user SET username = #{username}, password = ${password} WHERE id = #{id}
    </update>

    <!-- 删除操作 -->
    <delete id="deleteUser">
        DELETE FROM t_user WHERE id = #{id}
    </delete>


    <!-- 参数类型 parameterTypekey可以省略 -->

    <!-- 保存操作 -->
    <insert id="addUser" parameterType="User" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO t_user (username, password) VALUES (#{username}, #{password})
    </insert>

    <!-- 查询结果总数 resultType 不能省略-->
    <select id="queryForUserCount" resultType="java.lang.Integer">
        SELECT COUNT(id) FROM t_user
    </select>

</mapper>
```
### 3.4 配置application.yaml
```yaml
spring:
  datasource:
    username: root
    password: 1234
    url: jdbc:mysql://localhost:3306/jdbcDemo?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    type: com.alibaba.druid.pool.DruidDataSource
    filters: stat,wall,log4j
    driver-class-name: com.mysql.cj.jdbc.Driver
    #druid:
      #filters: stat,wall,log4j

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.etrans.jdbc.pojo
```

## pom依赖
```xml
<dependencies>
        
       <dependency>
           <groupId>org.mybatis.spring.boot</groupId>
           <artifactId>mybatis-spring-boot-starter</artifactId>
           <version>2.1.3</version>
       </dependency>
       
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>druid-spring-boot-starter</artifactId>
           <version>1.1.23</version>
       </dependency>

       <dependency>
           <groupId>log4j</groupId>
           <artifactId>log4j</artifactId>
           <version>1.2.17</version>
       </dependency>

       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-jdbc</artifactId>
       </dependency>

       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <scope>runtime</scope>
       </dependency>
       
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <optional>true</optional>
       </dependency>
       
</dependencies>
```
