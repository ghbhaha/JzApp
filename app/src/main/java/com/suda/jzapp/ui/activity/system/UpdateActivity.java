package com.suda.jzapp.ui.activity.system;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.system.AVUpdateCheck;
import com.suda.jzapp.misc.IntentConstant;

public class UpdateActivity extends Activity {


    private AVUpdateCheck mAvUpdateCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mAvUpdateCheck = (AVUpdateCheck) getIntent().getParcelableExtra(IntentConstant.UPDATE_CHECK);
        ((TextView) findViewById(R.id.version_info)).setText(mAvUpdateCheck.getUpdateInfo());
        ((TextView) findViewById(R.id.version)).setText("V" + mAvUpdateCheck.getVersion());
    }

    public void close(View view) {
        finish();
    }

    public void download(View view) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(mAvUpdateCheck.getLink()));
        intent.setAction(Intent.ACTION_VIEW);
        this.startActivity(intent);
        finish();
    }
}
