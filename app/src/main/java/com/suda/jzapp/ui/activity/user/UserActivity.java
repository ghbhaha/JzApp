package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.soundcloud.android.crop.Crop;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.BuildConfig;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.SyncManager;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.ImageUtil;
import com.suda.jzapp.util.QRCodeUtil;
import com.suda.jzapp.util.SnackBarUtil;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        userManager = new UserManager(this);
        recordManager = new RecordManager(this);
        syncManager = new SyncManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mHeadIcon = (CircleImageView) findViewById(R.id.profile_image);
        mTvUserName = (TextView) findViewById(R.id.userid);
        mTvUserCode = (TextView) findViewById(R.id.userCode);
        mTvEmail = (TextView) findViewById(R.id.email);
        imageViewQrCode = (ImageView) findViewById(R.id.qr_code_image);
        mTvRecordDate = (TextView) findViewById(R.id.record_date_count);
        if (BuildConfig.USERLINK)
            findViewById(R.id.user_code_ll).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.user_code_ll).setVisibility(View.GONE);
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
                Glide.with(UserActivity.this.getApplicationContext()).
                        load(user.getHeadImage())
                        .error(R.mipmap.suda).into(mHeadIcon);
                mTvUserName.setText(user.getUserName());
                mTvUserCode.setText("您是第" + user.getUserCode() + "位用户");
                mTvRecordDate.setText(recordManager.getWidgetRecordDayCount());
                mTvEmail.setText(MyAVUser.getCurrentUser().getEmail());
                imageViewQrCode.setImageBitmap(QRCodeUtil.createQRImage(Constant.QR_MARK + user.getUserName(), imageViewQrCode.getLayoutParams().width, imageViewQrCode.getLayoutParams().width));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CROP_IMAGE:
                    updateIcon();
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
        int notBackData = syncManager.getNotBackDataCount();
        if (notBackData == 0) {
            userManager.logOut();
            setResult(RESULT_OK);
            finish();
            return;
        }
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("登出提示");
        materialDialog.setMessage("检测到您有" + notBackData + "条数据未同步，建议您同步后再退出");
        materialDialog.setNegativeButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.logOut();
                setResult(RESULT_OK);
                finish();
            }
        });
        materialDialog.setPositiveButton("暂不退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.show();

    }

    public void setHeadIcon(View view) {
        Intent it = new Intent(Intent.ACTION_PICK, null);
        it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(it, REQUEST_SELECT_IMAGE);
    }

    public void cropPhoto(Uri uri) {
        Crop.of(uri, Uri.fromFile(ImageUtil.getPathByImageName(this, HEAD))).asSquare().withAspect(150, 150)
                .start(this, REQUEST_CROP_IMAGE);
    }

    public void updateIcon() {
        final File file = ImageUtil.getPathByImageName(this, HEAD);
        if (!file.exists()) {
            SnackBarUtil.showSnackInfo(mHeadIcon, this, "获取图片失败");
            return;
        }
        final Bitmap mHeadBitMap = BitmapFactory.decodeFile(file.getAbsolutePath());
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
                                file.delete();
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

    private SyncManager syncManager;
    private RecordManager recordManager;
    private UserManager userManager;
    private CircleImageView mHeadIcon;
    private TextView mTvUserName, mTvUserCode, mTvEmail, mTvRecordDate;
    private ImageView imageViewQrCode;
    private CircleProgressBar circleProgressBar;
    private static final String HEAD = "head";

}
