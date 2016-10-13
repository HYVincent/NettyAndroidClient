package com.shangyi.netty.module;

/**
 * 消息基本信息
 *
 * @author 徐飞
 * @version 2016/02/24 19:40
 */
public class Constants {
    private static String phoneNum;

    public static String setPhoneNum() {
        return phoneNum;
    }

    public static void setPhoneNum(String phoneNum) {
        Constants.phoneNum = phoneNum;
    }
}
