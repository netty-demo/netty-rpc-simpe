package com.chuan.netty.rpc.provider;

import com.chuan.netty.rpc.api.IRpcHelloService;

/**
 * @author: diaoche
 * @review:
 * @date: 2019/6/18 16:38
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name +" !" ;
    }
}
