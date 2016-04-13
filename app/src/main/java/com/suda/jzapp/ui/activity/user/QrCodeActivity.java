package com.suda.jzapp.ui.activity.user;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.util.QRCodeUtil;

public class QrCodeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        imageQrCode = (ImageView) findViewById(R.id.qr_code_image);
        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                User user = (User) msg.obj;
                if (user == null)
                    return;
                imageQrCode.setImageBitmap(QRCodeUtil.createQRImage(user.getUserId(), imageQrCode.getLayoutParams().width, imageQrCode.getLayoutParams().width));
            }
        });

    }


    private ImageView imageQrCode;
    private UserManager userManager;
}
