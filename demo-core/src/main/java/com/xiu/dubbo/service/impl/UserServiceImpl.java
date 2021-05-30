package com.xiu.dubbo.service.impl;

import com.xiu.dubbo.service.DemoService;
import com.xiu.dubbo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName DemoService
 * @Desc DemoService接口实现类
 * @Author xieqx
 * @Date 2021/4/25 15:35
 **/
@Service("userService")
@com.alibaba.dubbo.config.annotation.Service()
public class UserServiceImpl implements UserService {

    @Override
    public String queryUserInfo() {
        return "hello user";
    }
}
