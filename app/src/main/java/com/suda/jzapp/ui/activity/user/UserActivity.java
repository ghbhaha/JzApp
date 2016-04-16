package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.bumptech.glide.Glide;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.ImageUtil;
import com.suda.jzapp.util.QRCodeUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        userManager = new UserManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mHeadIcon = (CircleImageView) findViewById(R.id.profile_image);
        mTvUserName = (TextView) findViewById(R.id.userid);
        mTvUserCode = (TextView) findViewById(R.id.userCode);
        mTvEmail = (TextView) findViewById(R.id.email);
        imageViewQrCode = (ImageView) findViewById(R.id.qr_code_image);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.GONE);
        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                circleProgressBar.setVisibility(View.GONE);
                final User user = (User) msg.obj;
                if (user == null)
                    return;
                Glide.with(UserActivity.this).
                        load(user.getHeadImage())
                        .error(R.mipmap.suda).into(mHeadIcon);
                mTvUserName.setText(user.getUserName());
                mTvUserCode.setText("您是第" + user.getUserCode() + "位用户");
                mTvEmail.setText(MyAVUser.getCurrentUser().getEmail());
                userManager.queryUserLinkByUser(user.getUserName(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String code = "";
                        if (msg.obj == null) {
                            code = Constant.QR_MARK + user.getUserName();
                        } else {
                            code = Constant.QR_MARK_HAVE_LINK + user.getUserName();
                        }
                        imageViewQrCode.setImageBitmap(QRCodeUtil.createQRImage(code, imageViewQrCode.getLayoutParams().width, imageViewQrCode.getLayoutParams().width));
                    }
                });


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CROP_IMAGE:
                    updateIcon(data);
                    break;
                case REQUEST_SELECT_IMAGE:
                    if (data.getData() != null)
                        cropPhoto(data.getData());
                    break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.background).setBackgroundColor(mainColor);
    }

    public void showQcCode(View view) {
        Intent intent = new Intent(this, QrCodeActivity.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        userManager.logOut();
        setResult(RESULT_OK);
        this.finish();
    }

    public void setHeadIcon(View view) {
        Intent it = new Intent(Intent.ACTION_PICK, null);
        it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(it, REQUEST_SELECT_IMAGE);
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }

    public void updateIcon(Intent data) {
        Bundle extras = data.getExtras();
        final Bitmap mHeadBitMap = extras.getParcelable("data");
        circleProgressBar.setVisibility(View.VISIBLE);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);
        mHeadIcon.setVisibility(View.INVISIBLE);
        if (mHeadBitMap != null) {
            final AVFile f = new AVFile("icon", ImageUtil.Bitmap2Bytes(mHeadBitMap));
            userManager.updateHeadIcon(f, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constant.MSG_SUCCESS) {
                        userManager.getMe(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                User user = (User) msg.obj;
                                if (user == null)
                                    return;
                                mHeadIcon.setImageBitmap(mHeadBitMap);
                                mHeadIcon.setVisibility(View.VISIBLE);
                                circleProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });
        }
    }

    private static final int REQUEST_SELECT_IMAGE = 1;
    private static final int REQUEST_CROP_IMAGE = 2;

    private UserManager userManager;
    private CircleImageView mHeadIcon;
    private TextView mTvUserName, mTvUserCode, mTvEmail;
    private ImageView imageViewQrCode;
    private CircleProgressBar circleProgressBar;

}
