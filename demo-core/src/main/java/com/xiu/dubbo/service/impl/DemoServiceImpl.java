package com.xiu.dubbo.service.impl;

import com.xiu.dubbo.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * @ClassName DemoService
 * @Desc DemoService接口实现类
 * @Author xieqx
 * @Date 2021/4/25 15:35
 **/
@Service("demoService")
@com.alibaba.dubbo.config.annotation.Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String queryDemoInfo() {
        return "hello dubbo";
    }
}
