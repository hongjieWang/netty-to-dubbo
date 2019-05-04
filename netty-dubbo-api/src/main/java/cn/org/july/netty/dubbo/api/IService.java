package cn.org.july.netty.dubbo.api;

/**
 * @author july_whj
 */
public interface IService {
    /**
     * 计算加法
     */
    int add(int a, int b);

    /**
     * @param msg
     */
    String sayHello(String msg);

}
