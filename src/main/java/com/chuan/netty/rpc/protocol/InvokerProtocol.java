package com.chuan.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义传输协议
 * @author: diaoche
 * @review:
 * @date: 2019/6/18 16:35
 */
@Data
public class InvokerProtocol implements Serializable {

    private String className;//类名
    private String methodName;//函数名称
    private Class<?>[] parames;// 参数类型
    private Object[] values;//参数  列表
}
