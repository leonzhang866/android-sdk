package com.yiche.ycanalytics;

public class YCErrorCode {
	 /**
     * 错误码标识
     */
    public static final int YC_Error = -1;

    /**
     * 正常码标识
     */
    public static final int YC_OK = 200;

    /**
     * 网络超时
     */
    public static final int YC_NET_TIME_OUT = 504;
    /**
     * 网络传输数据错误
     */
    public static final int YC_NET_DATA_ERROR = 1000;

    /**
     * 网络通用错误标识
     */
    public static final int YC_NET_GENER_ERROR = 1001;
    /**
     * 解析网络传输的json数据出错
     */
    public static final int YC_JSON_PARSER_ERROR = 1017;

}
