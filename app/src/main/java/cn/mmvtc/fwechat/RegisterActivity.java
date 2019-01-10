package cn.mmvtc.fwechat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_name,register_user, register_password;
    private String name,user, password, register_url;

    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                String RegisterMessage = (String) msg.obj;
                Log.i("获取的返回信息", RegisterMessage);
                final user_register user_register = new Gson().fromJson(RegisterMessage, user_register.class);
                /***
                 * 在此处可以通过获取到的Msg值来判断
                 * 给出用户提示注册成功 与否，以及判断是否用户名已经存在
                 */
                if (user_register.getStatus().equals("注册成功")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    ToastUtil.showMessage(RegisterActivity.this,user_register.getStatus());
                    intent.putExtra("user", user);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else{
                    ToastUtil.showMessage(RegisterActivity.this,user_register.getStatus());
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_name = (EditText) findViewById(R.id.register_name);
        register_user = (EditText) findViewById(R.id.register_user);
        register_password = (EditText) findViewById(R.id.register_password);
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = register_name.getText().toString().trim();
                user = register_user.getText().toString().trim();
                password = register_password.getText().toString().trim();
                register_url = "http://123.207.85.214/chat/register.php";
                postRequest(register_url,name,user,password);
            }
        });
    }
    /**
     * post请求后台
     * @param user
     * @param password
     */
    private void postRequest(String url,String name,String user, String password) {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("user", user)
                .add("password", password)
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
                        mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}