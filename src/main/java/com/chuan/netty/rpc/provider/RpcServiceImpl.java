package com.chuan.netty.rpc.provider;

import com.chuan.netty.rpc.api.IRpcService;

/**
 * @author: diaoche
 * @review:
 * @date: 2019/6/18 16:39
 */
public class RpcServiceImpl implements IRpcService {
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int sub(int a, int b) {
        return a-b;
    }

    @Override
    public int mult(int a, int b) {
        return a*b;
    }

    @Override
    public int div(int a, int b) {
        return a/b;
    }
}
