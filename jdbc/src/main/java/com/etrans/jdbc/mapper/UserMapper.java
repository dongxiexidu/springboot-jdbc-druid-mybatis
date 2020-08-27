package com.etrans.jdbc.mapper;

import com.etrans.jdbc.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

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
