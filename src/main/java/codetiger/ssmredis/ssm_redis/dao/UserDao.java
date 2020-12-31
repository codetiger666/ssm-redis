package codetiger.ssmredis.ssm_redis.dao;

import codetiger.ssmredis.ssm_redis.entily.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: ssm-redis
 * @description:
 * @author: Mr.Nie
 * @create: 2020-12-31 18:45
 **/

@Repository
public interface UserDao {
    List<User> selectAll();
}
