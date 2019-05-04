package cn.org.july.netty.dubbo.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author july_whj
 */
public class RegisterCenterImpl implements IRegisterCenter {

    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.addr).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();
    }

    @Override
    public void registry(String serverName, String serviceAddress) {
        String serverPath = ZkConfig.ZK_REGISTER_PATH.concat("/").concat(serverName);
        try {
            if (curatorFramework.checkExists().forPath(serverPath) == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(serverPath, "0".getBytes());
            }
            String addr = serverPath.concat("/").concat(serviceAddress);
            String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addr, "0".getBytes());
            System.out.println("服务注册成功，" + rsNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
