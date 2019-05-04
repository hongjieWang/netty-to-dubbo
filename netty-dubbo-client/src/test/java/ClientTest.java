import cn.org.july.netty.dubbo.api.IService;
import cn.org.july.netty.dubbo.proxy.RpcClientProxy;
import cn.org.july.netty.dubbo.registry.IServiceDiscover;
import cn.org.july.netty.dubbo.registry.ServiceDiscoverImpl;

/**
 * Created with IntelliJ IDEA.
 * User:  wanghongjie
 * Date:  2019/5/3 - 23:06
 * <p>
 * Description:
 */
public class ClientTest {
    public static void main(String[] args) {
        IServiceDiscover serviceDiscover = new ServiceDiscoverImpl();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscover);
        IService iService = rpcClientProxy.create(IService.class);
        System.out.println(iService.sayHello("netty-to-dubbo"));
        System.out.println(iService.sayHello("你好"));
        System.out.println(iService.sayHello("成功咯，很高兴"));
        System.out.println(iService.add(10, 4));
    }
}