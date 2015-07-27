
package com.xgsdk.client.entity;

public interface XGErrorCode {
    public static final int SDK_CLIENT_INIT_FAILED = 1000;// 客户端初始化失败

    // 调用 SDK server参数不正确
    public static final int SDK_SERVER_PARAM_INVALID = 2000;// 参数异常
    public static final int SDK_SERVER_CONN_FAILED = 2001;// 连接异常
    public static final int SDK_SERVER_RESPONSE_ERROR = 2002;// SDK server返回失败

    public static final int BUSINESS_ERROR = 3000;// 业务异常

    public static final int CHANNEL_ERROR = 3000;// 渠道异常

    public static final int OTHER_ERROR = 9999;

}
