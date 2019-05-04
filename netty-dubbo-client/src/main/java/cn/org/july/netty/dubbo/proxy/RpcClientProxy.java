package cn.org.july.netty.dubbo.proxy;

import cn.org.july.netty.dubbo.api.RpcRequest;
import cn.org.july.netty.dubbo.registry.IServiceDiscover;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User:  wanghongjie
 * Date:  2019/5/3 - 23:08
 * <p>
 * Description:
 */
public class RpcClientProxy {
    private IServiceDiscover serviceDiscover;

    public RpcClientProxy(IServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public <T> T create(final Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    //封装RpcRequest请求对象，然后通过netty发送给服务等
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest rpcRequest = new RpcRequest();
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setTypes(method.getParameterTypes());
                        rpcRequest.setParams(args);
                        //服务发现，zk进行通讯
                        String serviceName = interfaceClass.getName();
                        //获取服务实现url地址
                        String serviceAddress = serviceDiscover.discover(serviceName);
                        //解析ip和port
                        System.out.println("服务端实现地址：" + serviceAddress);
                        String[] arrs = serviceAddress.split(":");
                        String host = arrs[0];
                        int port = Integer.parseInt(arrs[1]);
                        System.out.println("服务实现ip:" + host);
                        System.out.println("服务实现port:" + port);
                        final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
                        //通过netty方式进行连接发送数据
                        EventLoopGroup group = new NioEventLoopGroup();
                        try {
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                                    .handler(new ChannelInitializer<SocketChannel>() {
                                        @Override
                                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                                            channelPipeline.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                                            channelPipeline.addLast(new ObjectEncoder());
                                            //netty实现代码
                                            channelPipeline.addLast(rpcProxyHandler);
                                        }
                                    });
                            ChannelFuture future = bootstrap.connect(host, port).sync();
                            //将封装好的对象写入
                            future.channel().writeAndFlush(rpcRequest);
                            future.channel().closeFuture().sync();
                        } catch (Exception e) {

                        } finally {
                            group.shutdownGracefully();
                        }
                        return rpcProxyHandler.getResponse();
                    }
                });
    }
}