package com.shangyi.netty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shangyi.netty.module.PushMsg;
import java.util.List;

/**
 * @name NettyAndroidClient
 * @class name：com.shangyi.netty
 * @class describe
 * @anthor Vincent QQ:1032006226
 * @time 2016/10/28 14:24
 * @change
 * @chang time
 * @class describe
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private Context mContext;
    private List<PushMsg> msgData;

    public void setMsgData(List<PushMsg> msg){
        this.msgData=msg;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent!=null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.msg_item_list,null);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PushMsg msg=msgData.get(position);
        holder.tvMsg.setText(msg.getTitle()+":"+msg.getContent());
        Log.e("title",msg.getTitle());
        Log.e("content",msg.getContent());
    }

    @Override
    public int getItemCount() {
        return (msgData!=null&&msgData.isEmpty())?msgData.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMsg;
        public ViewHolder(View itemView) {
            super(itemView);
            tvMsg=(TextView)itemView.findViewById(R.id.tv_msg);
        }
    }
}
