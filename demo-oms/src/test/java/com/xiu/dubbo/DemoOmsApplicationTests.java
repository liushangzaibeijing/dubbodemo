package com.xiu.dubbo;

import com.alibaba.dubbo.config.AbstractConfig;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.alibaba.dubbo.config.spring.util.PropertySourcesUtils.getSubProperties;

@SpringBootTest
class DemoOmsApplicationTests {

    @Test
    void contextLoads() {

    }
}
