import cn.org.july.netty.dubbo.api.IService;
import cn.org.july.netty.dubbo.registry.IRegisterCenter;
import cn.org.july.netty.dubbo.registry.RegisterCenterImpl;
import cn.org.july.netty.dubbo.rpc.NettyRpcServer;
import cn.org.july.netty.dubbo.service.ServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User:  wanghongjie
 * Date:  2019/5/3 - 23:03
 * <p>
 * Description:
 */
public class ServerTest {
    public static void main(String[] args) {
        IService service = new ServiceImpl();
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        NettyRpcServer rpcServer = new NettyRpcServer(registerCenter, "127.0.0.1:8080");
        rpcServer.bind(service);
        rpcServer.publisher();
    }
}