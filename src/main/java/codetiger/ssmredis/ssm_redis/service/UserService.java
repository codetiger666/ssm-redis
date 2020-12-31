package codetiger.ssmredis.ssm_redis.service;

import codetiger.ssmredis.ssm_redis.dao.UserDao;
import codetiger.ssmredis.ssm_redis.entily.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: ssm-redis
 * @description:
 * @author: Mr.Nie
 * @create: 2020-12-31 18:45
 **/

@Service
public class UserService {
    @Autowired
    UserDao userDao;


    @Cacheable(value = "user", key = "'test'")
    public List<User> selectAll(){
        return userDao.selectAll();
    }

}
