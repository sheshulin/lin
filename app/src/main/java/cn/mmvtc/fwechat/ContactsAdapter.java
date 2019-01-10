package cn.mmvtc.fwechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    ArrayList<ContactsInfo> UserList;

    ContactsAdapter(Context context, ArrayList<ContactsInfo> UserList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.UserList = UserList;
    }


    @Override
    public int getCount() {
        return UserList .size();
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
        private TextView user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.contacts_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.user = (TextView) convertView.findViewById(R.id.tv_user);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(UserList.get(position).getName());
        viewHolder.user.setText(UserList.get(position).getUser());
        return convertView;
    }
}
