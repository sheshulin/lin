package cn.mmvtc.fwechat;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static Toast mtoast;

    public static void showMessage(Context context, String message) {
        if (mtoast == null){
            mtoast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        }else {
            mtoast.setText(message);
        }
        mtoast.show();
    }
}
