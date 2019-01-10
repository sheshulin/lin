package cn.mmvtc.fwechat;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatFragment extends Fragment {

    final OkHttpClient client = new OkHttpClient();
    //发送消息返回数组
    ArrayList<ChatInfo> ChatList;
    //浏览聊天室的内容
    ArrayList<VisitInfo> VisitList;
    private ListView Chat_ListView;
    private EditText ed_message;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                                case 3:
                                    String Visit_result = msg.obj.toString();
                                    Visit(Visit_result);
                                    Chat_ListView.setAdapter(new VisitAdapter(getActivity(),VisitList));
                                    break;
                case 4:
                    String result = msg.obj.toString();
                    Chat(result);
                    Chat_ListView.setAdapter(new ChatAdapter(getActivity(), ChatList));
                    break;
            }

        }

    };

    //发送聊天信息的okhttp-post
    public void post_Form(String url, String user, String password, String chat, final Handler handler) {
        FormBody requestBody = new FormBody.Builder()
                .add("user", user)
                .add("password", password)
                .add("chat", chat)
                .build();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json; charset=UTF-8").post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                if (arg1.isSuccessful()) {
                    String returns = arg1.body().string();
                    Message msg3 = new Message();
                    msg3.what = 4;
                    msg3.obj = returns;
                    handler.sendMessage(msg3);
                } else {
                ToastUtil.showMessage(getActivity(),"发送成功，返回失败");
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                ToastUtil.showMessage(getActivity(),"发送失败");
            }
        });
    }

        //浏览聊天信息的okhttp-post
    private void postRequest(String url, String user) {
            //建立请求表单，添加上传服务器的参数
            RequestBody formBody = new FormBody.Builder()
                    .add("user", user)
                    .build();
            //发起请求
            final Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            //新建一个线程，用于得到服务器响应的参数
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = null;
                    try {
                        //回调
                        response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                            handler.obtainMessage(3, response.body().string()).sendToTarget();
                        } else {
                            throw new IOException("Unexpected code:" + response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    public ChatFragment() {

    }
//Activityx向Fragment传递值
    public static ChatFragment newInstance(String encryption_user, String login_user, String login_password) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("encryption_user", encryption_user);
        bundle.putString("login_user", login_user);
        bundle.putString("login_password", login_password);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        Chat_ListView = (ListView) view.findViewById(R.id.Chat_ListView);
        String login_user = "1";
        postRequest("http://123.207.85.214/chat/chat1.php",login_user);
        ed_message = (EditText) view.findViewById(R.id.ed_message);
        view.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null) {
                    String message = ed_message.getText().toString().trim();
                    String encryption_user = getArguments().getString("encryption_user");
                    String login_password = getArguments().getString("login_password");
                    String url = "http://123.207.85.214/chat/chat1.php";
                    //通过okhttp发起post请求
                    post_Form(url, encryption_user, login_password, message, handler);
                    ToastUtil.showMessage(getActivity(),"已发送");
                }
            }
        });


        return view;
    }
//发送聊天
    private void Chat(String result) {
        try {
            JSONArray ChatArray = new JSONArray(result);
            ChatList = new ArrayList<ChatInfo>();
            for (int i = ChatArray.length()-1; i >= 0; i--) {
                JSONObject ChatJson = ChatArray.getJSONObject(i);
                String name = ChatJson.getString("name");
                String chat = ChatJson.getString("chat");
                String time = ChatJson.getString("time");
                ChatInfo ChatInfo = new ChatInfo(name, chat, time);
                ChatList.add(ChatInfo);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//获取聊天室内容
        private void Visit(String result) {
            try {
                JSONArray VisitArray = new JSONArray(result);
                VisitList = new ArrayList<VisitInfo>();
                //倒序插入
                for (int i = VisitArray.length()-1; i >=0; i--) {
                    JSONObject VisitJson = VisitArray.getJSONObject(i);
                    String Visit_name = VisitJson.getString("name");
                    String Visit_chat = VisitJson.getString("chat");
                    String Visit_time = VisitJson.getString("time");
                    VisitInfo Visit_Info = new VisitInfo(Visit_name, Visit_chat, Visit_time);
                    VisitList.add(Visit_Info);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

}
