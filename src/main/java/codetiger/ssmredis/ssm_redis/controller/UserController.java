package codetiger.ssmredis.ssm_redis.controller;

import codetiger.ssmredis.ssm_redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: ssm-redis
 * @description:
 * @author: Mr.Nie
 * @create: 2020-12-31 18:48
 **/

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/user/selectAll")
    @ResponseBody
    public String seltctAll(){
        return userService.selectAll().toString();
    }

}
