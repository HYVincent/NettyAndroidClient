package com.shangyi.netty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shangyi.netty.module.PushMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐飞
 * @version 2016/02/24 09:49
 */
public class MainActivity extends Activity {

    private Button btnSendMsg;
    private EditText etContent;
//    private TextView tvServiceMsg;
    private RecyclerView rlvMsg;
    private List<PushMsg> msgData;
    private MsgAdapter adapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgData=new ArrayList<>();
        adapter=new MsgAdapter();
        setTitle(getIntent().getStringExtra("hi"));
        Button btnStart = (Button) findViewById(R.id.btn1);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushClient.start();
            }
        });

        Button btnClose = (Button) findViewById(R.id.btn2);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushClient.close();
            }
        });

        Button btnCheck = (Button) findViewById(R.id.btn3);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushClient.isOpen();
            }
        });
        etContent=(EditText)findViewById(R.id.et_input_content);
        btnSendMsg=(Button)findViewById(R.id.btn_send_message);

        rlvMsg=(RecyclerView)findViewById(R.id.rlv_msg);
      /*  rlvMsg.setItemAnimator(new DefaultItemAnimator());//默认动画效果
        rlvMsg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//设置布局管理器，第三个参数为是否逆向布局
        rlvMsg.setLayoutMode(0);
        rlvMsg.setHasFixedSize(true);*/
        rlvMsg.setItemAnimator(new DefaultItemAnimator());
        rlvMsg.setHasFixedSize(true);
        rlvMsg.setLayoutManager(new LinearLayoutManager(this));
        rlvMsg.setAdapter(adapter);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("发送给服务器的消息",etContent.getText().toString());
                if(PushClient.isOpen()){
                    PushClient.sendMsg(etContent.getText().toString());
                    PushMsg pushMsg=new PushMsg();
                    pushMsg.setTitle("12345");
                    pushMsg.setContent(etContent.getText().toString());
                    msgData.add(pushMsg);
                    refreshMsg(msgData);
                }else {
                    Log.d("消息提示","未连接");
                }
            }
        });
        Intent intent=getIntent();
        PushMsg pushMsg=new PushMsg();
        pushMsg.setTitle(intent.getStringExtra("title"));
        pushMsg.setContent(intent.getStringExtra("content"));
        msgData.add(pushMsg);
        refreshMsg(msgData);
        /*tvServiceMsg=(TextView)findViewById(R.id.tv_from_service_msg);
        Intent intent=getIntent();
        tvServiceMsg.setText(intent.getStringExtra("title")+":"+intent.getStringExtra("content"));
        *//*if(intent.getStringExtra("content").equals("成功登陆服务器")){
            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
        }*/
    }
    //刷新数据
    public void refreshMsg(List<PushMsg> msgData){
        adapter.setMsgData(msgData);
        adapter.notifyDataSetChanged();
    }

}
