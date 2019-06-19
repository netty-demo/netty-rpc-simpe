package com.chuan.netty.rpc.consumer.proxy;

import com.chuan.netty.rpc.consumer.RpcProxyHandler;
import com.chuan.netty.rpc.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
 * @author: diaoche
 * @review:
 * @date: 2019/6/19 11:13
 */
public class RpcProxy {
    public static<T> T create(Class<?> clazz){
        //clazz传进来本身就是interface
        MethodProxy proxy = new MethodProxy(clazz);
        Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,proxy);
        return result;
    }

    private static class MethodProxy implements InvocationHandler{

        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(Object.class.equals(method.getDeclaringClass())){
                try{
                    return method.invoke(this,args);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                return rpcInvoke(proxy,method,args);
            }
            return null;
        }

        /**
         * 实现接口的核心方法
         * @param proxy
         * @param method
         * @param args
         * @return
         */
        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            //传输协议封装
            InvokerProtocol msg = new InvokerProtocol();
            msg.setClassName(this.clazz.getName());
            msg.setMethodName(method.getName());
            msg.setValues(args);
            msg.setParames(method.getParameterTypes());

            final RpcProxyHandler consumerHandler = new RpcProxyHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try{
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY,true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //自定义协议解码器
                                pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                                pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));

                                pipeline.addLast("encoder",new ObjectEncoder());
                                pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                                pipeline.addLast("handler",  consumerHandler);
                            }
                        });

                ChannelFuture future = b.connect("localhost",6666).sync();
                future.channel().writeAndFlush(msg).sync();
                future.channel().closeFuture().sync();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }


            return consumerHandler.getResponse();
        }
    }
}
