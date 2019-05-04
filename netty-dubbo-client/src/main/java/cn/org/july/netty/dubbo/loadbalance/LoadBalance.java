package cn.org.july.netty.dubbo.loadbalance;

import java.util.List;

public interface LoadBalance {
    String select(List<String> repos);
}
