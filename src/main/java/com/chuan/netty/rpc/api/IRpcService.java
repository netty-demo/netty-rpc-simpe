package com.chuan.netty.rpc.api;

/**
 * @author: diaoche
 * @review:
 * @date: 2019/6/18 16:33
 */
public interface IRpcService {

    public int add(int a,int b);

    public int sub(int a,int b);

    public int mult(int a,int b);

    public int div(int a,int b);
}
