import cn.org.july.netty.dubbo.registry.IRegisterCenter;
import cn.org.july.netty.dubbo.registry.RegisterCenterImpl;

import java.io.IOException;

public class RegTest {
    public static void main(String[] args) throws IOException {
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        registerCenter.registry("cn.org.july.test", "127.0.0.1:9090");
        System.in.read();
    }
}
