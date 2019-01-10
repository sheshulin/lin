package cn.mmvtc.fwechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    ArrayList<ChatInfo> ChatList;

    ChatAdapter(Context context, ArrayList<ChatInfo> ChatList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.ChatList = ChatList;
    }


    @Override
    public int getCount() {
        return ChatList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        private TextView name;
        private TextView chat;
        private TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.chat_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.chat = (TextView) convertView.findViewById(R.id.tv_chat);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(ChatList.get(position).getName());
        viewHolder.chat.setText(ChatList.get(position).getChat());
        viewHolder.time.setText(ChatList.get(position).getTime());
        return convertView;
    }
}
