package cn.org.july.netty.dubbo.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    @Override
    public String select(List<String> repos) {
        int len = repos.size();
        if (len == 0)
            throw new RuntimeException("未发现注册的服务。");
        Random random = new Random();
        return repos.get(random.nextInt(len));
    }
}
