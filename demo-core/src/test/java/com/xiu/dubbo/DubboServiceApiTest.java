package com.xiu.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.ClassHelper;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.invoker.DelegateProviderMetaDataInvoker;
import com.alibaba.dubbo.config.model.ApplicationModel;
import com.alibaba.dubbo.config.model.ProviderModel;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.ConfiguratorFactory;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.dubbo.rpc.support.ProtocolUtils;
import com.xiu.dubbo.service.UserService;
import com.xiu.dubbo.service.impl.UserServiceImpl;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName DubboServiceApiTest
 * @Desc 使用api编码的形式进行dubbo服务暴露
 * @Author xieqx
 * @Date 2021/5/23 9:08
 **/
public class DubboServiceApiTest {


    @Test
    /**
     * 使用api编码的形式进行dubbo服务暴露
     */
    public void exportService(){
        //模拟spring服务实现（此处不使用spring环境 读者可以自行使用）
        UserService demoService = new UserServiceImpl();

        //1、创建应用信息（服务提供者和服务消费者均需要，以便用于计算应用之间的依赖关系）
        ApplicationConfig appliction = new ApplicationConfig("demo-core");

        //2、创建注册中心（服务提供者和服务消费者均需要，以便用于服务注册和服务发现）
        //dubbo支持的注册中心：①simple、②Multicast、③zookeeper、④ redis
        //这里采用zookeeper（常用也是dubbo推荐使用的）
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://182.92.189.235:2181");

        //3、创建服务暴露后对应的协议信息，该设置约定了消费者使用哪种协议来请求服务提供者
        //dubbo消费协议如下：   ①dubbo://、②hessian://、③rmi://、 ④http://、⑤webservice://、⑦memcached://、⑧redis://
        ProtocolConfig protocol = new ProtocolConfig();
        //使用dubbo协议
        protocol.setName("dubbo");
        protocol.setPort(28830);

        //4、该类为服务消费者的全局配置（比如是否延迟暴露，是否暴露服务等）
        ProviderConfig provider = new ProviderConfig();
        //是否暴露服务
        provider.setExport(true);
        //延迟一秒发布服务
        provider.setDelay(1000);

        //5、服务提供者 会配置应用、注册、协议、提供者等信息
        ServiceConfig<UserService> serviceService = new ServiceConfig<>();

        //设置应用信息
        serviceService.setApplication(appliction);
        //设置注册信息
        serviceService.setRegistry(registry);
        //设置相关协议
        serviceService.setProtocol(protocol);
        //设置全局配置信息
        serviceService.setProvider(provider);
        //设置接口信息
        serviceService.setInterface(UserService.class);
        serviceService.setRef(demoService);

        //6、服务暴露 （最终上面设置的相关信息转换为Dubbo URL的形式暴露出去）
        serviceService.export();
        while (true){
        }
    }
}
