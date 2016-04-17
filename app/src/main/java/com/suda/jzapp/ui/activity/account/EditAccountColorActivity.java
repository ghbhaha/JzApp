package com.suda.jzapp.ui.activity.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.view.MyRoundColorView;

public class EditAccountColorActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_color_activity);
        mAccountID = getIntent().getLongExtra(IntentConstant.ACCOUNT_ID, 0);
        accountManager = new AccountManager(this);
    }

    public void switchColor(View view) {
        MyRoundColorView myRoundColorView = (MyRoundColorView) view;
        final Intent intent = new Intent();
        intent.putExtra("color", myRoundColorView.getRoundColor());

        if (mAccountID>0){
            accountManager.updateAccountColor(mAccountID, myRoundColorView.getRoundColor() + "", new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else {
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    private long mAccountID;
    private AccountManager accountManager;
}
