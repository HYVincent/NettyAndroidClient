package com.shangyi.netty.module;

import java.io.Serializable;

/**
 * 消息基类
 * 必须实现序列，serialVersionUID 一定要有
 *
 * @author 徐飞
 * @version 2016/02/24 19:40
 */
public abstract class BaseMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private MsgType type;
    //必须唯一，否者会出现channel调用混乱
    private String phoneNum;

    //初始化客户端id
    public BaseMsg() {
        this.phoneNum = Constants.setPhoneNum();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }
}
