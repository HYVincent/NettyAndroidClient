package com.shangyi.netty;


import android.content.Intent;


import com.shangyi.netty.module.BaseMsg;
import com.shangyi.netty.module.LoginMsg;
import com.shangyi.netty.module.PingMsg;
import com.shangyi.netty.module.PushMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 徐飞
 * @version 2016/02/25 14:11
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {
    //设置心跳时间  开始
    public static final int MIN_CLICK_DELAY_TIME = 1000 * 3;
    private long lastClickTime = 0;
    //设置心跳时间   结束

    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = System.currentTimeMillis();
                        PingMsg pingMsg = new PingMsg();
                        ctx.writeAndFlush(pingMsg);
                        System.out.println("send ping to server----------");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //这里是断线要进行的操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("重连了。---------");
        NettyClientBootstrap bootstrap = PushClient.getBootstrap();
        bootstrap.startNetty();
    }

    //这里是出现异常的话要进行的操作
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("出现异常了。。。。。。。。。。。。。");
        cause.printStackTrace();
    }

    //这里是接受服务端发送过来的消息
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        switch (baseMsg.getType()) {
            case LOGIN:
                //向服务器发起登录
                LoginMsg loginMsg = new LoginMsg();
                loginMsg.setPassword("yao");
                loginMsg.setUserName("robin");
                channelHandlerContext.writeAndFlush(loginMsg);
                break;
            case PING:
                System.out.println("receive ping from server----------");
                break;
            case PUSH:
                PushMsg pushMsg = (PushMsg) baseMsg;
                Intent intent = new Intent(MainApplication.getAppContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("content", pushMsg.getContent());
                intent.putExtra("title",pushMsg.getTitle());
                MainApplication.getAppContext().startActivity(intent);
                System.out.println("服务器回应："+pushMsg.getTitle() + " , " +pushMsg.getContent());
                break;
            default:
                System.out.println("default..");
                break;
        }
        ReferenceCountUtil.release(baseMsg);
    }
}
