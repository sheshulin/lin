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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText login_user,login_password;
    private String user, password, url,register_user,register_password;
    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                final user_login user_login = new Gson().fromJson(ReturnMessage, user_login.class);
                /***
                 * 在此处可以通过获取到的Msg值来判断
                 * 给出用户提示注册成功 与否，以及判断是否用户名已经存在
                 */
                Log.i("TAG", "handleMessage: "+user_login.getStatus());
                if (user_login.getStatus().equals("登陆成功")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    ToastUtil.showMessage(LoginActivity.this,"欢迎回来!尊敬的"+user_login.getName());
                    intent.putExtra("login_name",user_login.getName());
                    intent.putExtra("encryption_user",user_login.getUser());
                    intent.putExtra("login_user", user);
                    intent.putExtra("login_password", password);
                    startActivity(intent);
                }else{
                    ToastUtil.showMessage(LoginActivity.this,user_login.getStatus());
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_user = (EditText) findViewById(R.id.login_user);
        login_password = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        Intent intent = getIntent();
        register_user = intent.getStringExtra("user");
        register_password = intent.getStringExtra("password");
        login_user.setText(register_user);
        login_password.setText(register_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                user = login_user.getText().toString().trim();
                password = login_password.getText().toString().trim();
                url = "http://123.207.85.214/chat/login.php";
                //通过okhttp发起post请求
                postRequest(url, user, password);
                break;
            case R.id.btn_register:
                Intent register_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register_intent);
                break;
        }
    }

    /**
     * post请求后台
     *
     * @param user
     * @param password
     */
    private void postRequest(String url, String user, String password) {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
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
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
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


