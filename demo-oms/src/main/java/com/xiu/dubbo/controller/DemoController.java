package com.xiu.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiu.dubbo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName DemoController
 * @Desc TODO
 * @Author xieqx
 * @Date 2021/4/25 15:59
 **/
@RestController("demoController")
@RequestMapping("/demo")
public class DemoController {

    @Reference
    private DemoService demoService;

    @RequestMapping(value = "queryDemo", method = RequestMethod.POST)
    public String queryDemoInfo(){
       return  demoService.queryDemoInfo();
    }

}
