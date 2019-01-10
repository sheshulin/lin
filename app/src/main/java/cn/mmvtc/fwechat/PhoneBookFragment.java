package cn.mmvtc.fwechat;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneBookFragment extends Fragment {

    final OkHttpClient client = new OkHttpClient();
    //查询结果集合
    ArrayList<ContactsInfo> UserList;
    private ListView Contacts_ListView;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 3:
                    String result = msg.obj.toString();
                    Uer(result);
                    Contacts_ListView.setAdapter(new ContactsAdapter(getActivity(),UserList));
                    break;
            }

        }

    };

    public void get_Y(String url, final Handler handler) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                if (arg1.isSuccessful()) {
                    String returns = arg1.body().string();
                    Message ContactsInfo_msg = new Message();
                    ContactsInfo_msg.what = 3;
                    ContactsInfo_msg.obj = returns;
                    handler.sendMessage(ContactsInfo_msg);
                } else {
                    ToastUtil.showMessage(getActivity(),"请求成功,没有数据");
                }

            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                ToastUtil.showMessage(getActivity(),"请求失败");
            }
        });
    }

    public PhoneBookFragment() {

    }

    public static PhoneBookFragment newInstance() {
        PhoneBookFragment fragment = new PhoneBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_book, container, false);
        get_Y("http://123.207.85.214/chat/member.php", handler);
        Contacts_ListView = (ListView) view.findViewById(R.id.Contacts_ListView);


        return view;
    }
//查询通讯录的人员
    private void Uer(String result) {
        try {
            JSONArray UserArray = new JSONArray(result);
            UserList = new ArrayList<ContactsInfo>();
            for (int i = 0; i < UserArray.length(); i++) {
                JSONObject futureJson = UserArray.getJSONObject(i);
                String name = futureJson.getString("name");
                String user = futureJson.getString("user");
                ContactsInfo contactsInfo = new ContactsInfo(name, user);
                UserList.add(contactsInfo);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
