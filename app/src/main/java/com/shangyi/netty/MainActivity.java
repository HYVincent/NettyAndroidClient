package com.shangyi.netty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 徐飞
 * @version 2016/02/24 09:49
 */
public class MainActivity extends Activity {

    private Button btnSendMsg;
    private EditText etContent;
    private TextView tvServiceMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("发送给服务器的消息",etContent.getText().toString());
                PushClient.sendMsg(etContent.getText().toString());
            }
        });
        tvServiceMsg=(TextView)findViewById(R.id.tv_from_service_msg);
        Intent intent=getIntent();
        tvServiceMsg.setText(intent.getStringExtra("title")+":"+intent.getStringExtra("content"));

    }


}
