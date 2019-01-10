package cn.mmvtc.fwechat;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private String login_user,login_name,encryption_user,login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRgTab = (RadioGroup) findViewById(R.id.rg_main);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        changeFragment(ChatFragment.class.getName());
                        break;
                    case R.id.rb_phone_book:
                        changeFragment(PhoneBookFragment.class.getName());
                        break;
                    case R.id.rb_find:
                        changeFragment(FindFragment.class.getName());
                        break;
                    case R.id.rb_me:
                        changeFragment(MeFragment.class.getName());
                        break;
                }
            }
        });
        if(savedInstanceState == null){
            changeFragment(ChatFragment.class.getName());
        }
    }

    /**
     * show target fragment
     *
     * @param tag
     */
    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(ChatFragment.class.getName())) {
                Intent visit_intent = getIntent();
                encryption_user = visit_intent.getStringExtra("encryption_user");
                login_user = visit_intent.getStringExtra("login_user");
                login_password = visit_intent.getStringExtra("login_password");
                fragment = ChatFragment.newInstance(encryption_user,login_user,login_password);
            } else if (tag.equals(PhoneBookFragment.class.getName())) {
                fragment = PhoneBookFragment.newInstance();
            } else if (tag.equals(FindFragment.class.getName())) {
                fragment = FindFragment.newInstance();
            } else if (tag.equals(MeFragment.class.getName())) {
                Intent me_intent = getIntent();
                login_name = me_intent.getStringExtra("login_name");
                login_user = me_intent.getStringExtra("login_user");
                fragment = MeFragment.newInstance(login_name,login_user);
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }

    /**
     * hide all fragment
     */
    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }
}
