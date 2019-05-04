package cn.org.july.netty.dubbo.service;

import cn.org.july.netty.dubbo.annotation.RpcAnnotation;
import cn.org.july.netty.dubbo.api.IService;

/**
 * @author july_whj
 */
@RpcAnnotation(IService.class)
public class ServiceImpl implements IService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public String sayHello(String msg) {
        System.out.println("rpc say :" + msg);
        return "rpc say: " + msg;
    }
}
