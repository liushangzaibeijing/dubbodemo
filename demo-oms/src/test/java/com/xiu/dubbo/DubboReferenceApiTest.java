package com.xiu.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.model.ApplicationModel;
import com.alibaba.dubbo.config.model.ConsumerModel;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.StaticContext;
import com.alibaba.dubbo.rpc.cluster.directory.StaticDirectory;
import com.alibaba.dubbo.rpc.cluster.support.AvailableCluster;
import com.alibaba.dubbo.rpc.cluster.support.ClusterUtils;
import com.alibaba.dubbo.rpc.protocol.injvm.InjvmProtocol;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.dubbo.rpc.support.ProtocolUtils;
import com.xiu.dubbo.service.UserService;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.*;

import static com.alibaba.dubbo.common.utils.NetUtils.isInvalidLocalHost;

/**
 * @ClassName DubboReferenceApiTest
 * @Desc dubbo 服务消费者api形式进行服务消费
 * @Author xieqx
 * @Date 2021/5/21 9:17
 **/
public class DubboReferenceApiTest {



    @Test
    public void testAsync(){
        Properties properties = System.getProperties();
        Set<Object> objects = properties.keySet();

        for(Object key:objects){
            Object value = properties.get(key);
            System.out.println("key: "+key+", value: "+value);
        }
    }

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
    }


    private T createProxy(Map<String, String> map) {
        //判断是不是dubbo的本地调用
        URL tmpUrl = new URL("temp", "localhost", 0, map);
        final boolean isJvmRefer;
        if (isInjvm() == null) {
            if (url != null && url.length() > 0) { // if a url is specified, don't do local reference
                isJvmRefer = false;
            } else if (InjvmProtocol.getInjvmProtocol().isInjvmRefer(tmpUrl)) {
                // by default, reference local service if there is
                isJvmRefer = true;
            } else {
                isJvmRefer = false;
            }
        } else {
            isJvmRefer = isInjvm().booleanValue();
        }
        //如果是本地调用获取本地调用的invoker
        if (isJvmRefer) {
            URL url = new URL(Constants.LOCAL_PROTOCOL, NetUtils.LOCALHOST, 0, interfaceClass.getName()).addParameters(map);
            invoker = refprotocol.refer(interfaceClass, url);
            if (logger.isInfoEnabled()) {
                logger.info("Using injvm service " + interfaceClass.getName());
            }
        } else {
            //判断是不是点对点
            if (url != null && url.length() > 0) { // user specified URL, could be peer-to-peer address, or register center's address.
                String[] us = Constants.SEMICOLON_SPLIT_PATTERN.split(url);
                if (us != null && us.length > 0) {
                    for (String u : us) {
                        URL url = URL.valueOf(u);
                        if (url.getPath() == null || url.getPath().length() == 0) {
                            url = url.setPath(interfaceName);
                        }
                        if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                            urls.add(url.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                        } else {
                            urls.add(ClusterUtils.mergeUrl(url, map));
                        }
                    }
                }
            } else {
                //走注册中心
                //获取多个注册中心以及注册中心对应的监控中心 遍历将注册中心和监控中心添加到Dubbo URL 集合中 后面创建Invoker对象用
                //获取多个注册中心
                List<URL> us = loadRegistries(false);
                if (us != null && !us.isEmpty()) {
                    for (URL u : us) {
                        //获取注册中心对应的监控中心
                        URL monitorUrl = loadMonitor(u);
                        if (monitorUrl != null) {
                            map.put(Constants.MONITOR_KEY, URL.encode(monitorUrl.toFullString()));
                        }
                        //添加到DubboURL集合中 该Dubbo URL 是注册中心转换的url
                        urls.add(u.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                    }
                }
                if (urls == null || urls.isEmpty()) {
                    throw new IllegalStateException("No such any registry to reference " + interfaceName + " on the consumer " + NetUtils.getLocalHost() + " use dubbo version " + Version.getVersion() + ", please config <dubbo:registry address=\"...\" /> to your spring config.");
                }
            }
            //注册中心单个则使用该注册中心创建对应的Invoker
            if (urls.size() == 1) {
                invoker = refprotocol.refer(interfaceClass, urls.get(0));
            } else {
                //多个注册中心遍历处理 每一个注册中心创建一个Invoker添加到invokers集合中
                //最终选则最新的注册地址
                List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
                URL registryURL = null;
                for (URL url : urls) {
                    invokers.add(refprotocol.refer(interfaceClass, url));
                    if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                        registryURL = url; // use last registry url
                    }
                }
                if (registryURL != null) { // registry url is available
                    // use AvailableCluster only when register's cluster is available
                    URL u = registryURL.addParameter(Constants.CLUSTER_KEY, AvailableCluster.NAME);
                    invoker = cluster.join(new StaticDirectory(u, invokers));
                } else { // not a registry url
                    invoker = cluster.join(new StaticDirectory(invokers));
                }
            }
        }
       //省略日志打印
        //使用ProxyFacrory创建dubbo服务对应的代理对象
        return (T) proxyFactory.getProxy(invoker);
    }

}
