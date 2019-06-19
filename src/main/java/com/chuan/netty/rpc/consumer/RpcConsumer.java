package com.chuan.netty.rpc.consumer;

import com.chuan.netty.rpc.api.IRpcHelloService;
import com.chuan.netty.rpc.api.IRpcService;
import com.chuan.netty.rpc.consumer.proxy.RpcProxy;

/**
 * @author: diaoche
 * @review:
 * @date: 2019/6/19 14:23
 */
public class RpcConsumer {
    public static void main(String[] args) {
        IRpcHelloService rpcHelloService = RpcProxy.create(IRpcHelloService.class);

        System.out.println(rpcHelloService.hello("chuan"));


        IRpcService iRpcService = RpcProxy.create(IRpcService.class);
        System.out.println(" 8 + 2 = " + iRpcService.add(8,2));
        System.out.println(" 8 - 2 = " + iRpcService.sub(8,2));
        System.out.println(" 8 * 2 = " + iRpcService.mult(8,2));
        System.out.println(" 8 / 2 = " + iRpcService.div(8,2));

    }
}
