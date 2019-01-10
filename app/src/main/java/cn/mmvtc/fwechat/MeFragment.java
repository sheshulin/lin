package cn.mmvtc.fwechat;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 简书ID：天哥在奔跑
 * 原创Android教程：http://www.jianshu.com/p/9618c038135f
 * 教程答疑专用QQ群：667833258
 */
public class MeFragment extends Fragment {
    private TextView me_name, me_user;
    private String login_user, login_name;

    public MeFragment() {

    }

    public static MeFragment newInstance(String me_name, String me_user) {
        MeFragment mefragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("me_name", me_name);
        bundle.putString("me_user", me_user);
        mefragment.setArguments(bundle);
        return mefragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me_name = (TextView) view.findViewById(R.id.me_name);
        me_user = (TextView) view.findViewById(R.id.me_user);

        if (getArguments()!=null){
            me_name.setText(getArguments().getString("me_name"));
            me_user.setText("微信号:" + getArguments().getString("me_user"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        Intent me_intent = getIntent();
//        login_name = me_intent.getStringExtra("login_name");
//        login_user = me_intent.getStringExtra("login_user");
//        Log.d("TAG", "onCheckedChanged: "+login_name);
//        Log.d("TAG", "onCheckedChanged: "+login_user);
    }
}
