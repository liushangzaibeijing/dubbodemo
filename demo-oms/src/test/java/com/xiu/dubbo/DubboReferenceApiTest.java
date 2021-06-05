package com.xiu.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.bytecode.ClassGenerator;
import com.alibaba.dubbo.common.bytecode.NoSuchMethodException;
import com.alibaba.dubbo.common.bytecode.NoSuchPropertyException;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.*;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.invoker.DelegateProviderMetaDataInvoker;
import com.alibaba.dubbo.config.model.ApplicationModel;
import com.alibaba.dubbo.config.model.ConsumerModel;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Transporter;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.remoting.exchange.Exchangers;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.StaticContext;
import com.alibaba.dubbo.rpc.cluster.directory.StaticDirectory;
import com.alibaba.dubbo.rpc.cluster.support.AvailableCluster;
import com.alibaba.dubbo.rpc.cluster.support.ClusterUtils;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboCodec;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboExporter;
import com.alibaba.dubbo.rpc.protocol.injvm.InjvmProtocol;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.dubbo.rpc.support.ProtocolUtils;
import com.xiu.dubbo.service.UserService;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

import static com.alibaba.dubbo.common.utils.NetUtils.isInvalidLocalHost;

/**
 * @ClassName DubboReferenceApiTest
 * @Desc dubbo 服务消费者api形式进行服务消费
 * @Author xieqx
 * @Date 2021/5/21 9:17
 **/
public class DubboReferenceApiTest {
    private static AtomicLong WRAPPER_CLASS_COUNTER = new AtomicLong(0);
    /**
     * dubbo 服务消费者api形式进行服务消费
     */
    @Test
    public void consumerDubboService(){
        //声明应用 dubbo生态质检服务调用是基于应用的
        ApplicationConfig application = new ApplicationConfig("dubbo-refrence");
        //涉及注册中心
        RegistryConfig registryCenter = new RegistryConfig();
        registryCenter.setAddress("zookeeper://182.92.189.235:2181");

        //消费者消费
        //设置消费者全局配置
        ConsumerConfig consumerConfig = new ConsumerConfig();
        //设置默认的超时时间
        consumerConfig.setTimeout(1000*5);
        ReferenceConfig<UserService> userConfigReference = new ReferenceConfig<>();
        userConfigReference.setApplication(application);
//        List<RegistryConfig> registryConfigs = new ArrayList<>();
//        registryConfigs.add(registryCenter);
        userConfigReference.setRegistry(registryCenter);
        userConfigReference.setInterface(UserService.class);
        //dubbo直连
        //userConfigReference.setUrl("dubbo://xxx.xxx.xx:22890");
        //设置methodConfig 方法级别的dubbo参数包配置 比如方法名必填、重试次数、超时时间、负载均衡策略
        MethodConfig methodConfig = new MethodConfig();
        //方法名必填
        methodConfig.setName("queryUserInfo");
        //超时时间
        methodConfig.setTimeout(1000 * 5);
        //重试次数
        methodConfig.setRetries(3);
        //获取服务（并非真实的对象而是代理对象）
        UserService userService = userConfigReference.get();
        //调用对象方法
        String info = userService.queryUserInfo();
        System.out.println(info);
//        JavassistProxyFactory
    }


  //对于单个注册中心则直接调用protocol.refer()获取远程调用对象Invoker
  if (urls.size() == 1) {
    invoker = refprotocol.refer(interfaceClass, urls.get(0));
  } else {
    List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
    URL registryURL = null;
    //对于多注册中心则多次调用protocol.refer()获取远程调用对象Invoker
    for (URL url : urls) {
        invokers.add(refprotocol.refer(interfaceClass, url));
        //此处对于多注册中心 则获取最新的注册中心url 从该注册中心上获取对应的服务信息
        if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
            registryURL = url; // use last registry url
        }
    }
    //
    if (registryURL != null) {
        //从注册中心中获取多
        URL u = registryURL.addParameter(Constants.CLUSTER_KEY, AvailableCluster.NAME);
        invoker = cluster.join(new StaticDirectory(u, invokers));
    } else { // not a registry url
        invoker = cluster.join(new StaticDirectory(invokers));
    }
}



}
