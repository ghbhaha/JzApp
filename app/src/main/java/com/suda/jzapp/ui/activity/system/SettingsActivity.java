package com.suda.jzapp.ui.activity.system;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTheme(ThemeUtil.getAppStyleId(this));


        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment(this);
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsFragment.changeGestureCheckStatus();

    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        public SettingsFragment(Context context) {
            this.context = context;
        }

        public SettingsFragment() {

        }

        private Context context;
        private CheckBoxPreference mGestureLockCheck;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            mGestureLockCheck = (CheckBoxPreference) findPreference(GESTURE_LOCK);
            mGestureLockCheck.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mGestureLockCheck) {
                if (mGestureLockCheck.isChecked()) {
                    SPUtils.put(context, Constant.SP_GESTURE, "");
                    mGestureLockCheck.setChecked(false);
                } else {
                    if (MyAVUser.getCurrentUser() != null) {
                        Intent intent = new Intent(context, GestureLockActivity.class);
                        intent.putExtra(IntentConstant.SETTING_MODE, true);
                        startActivity(intent);
                    } else {
                        SnackBarUtil.showSnackInfo(getView(), context, "请先登录账户");
                    }
                }
            }
            return false;
        }

        public void changeGestureCheckStatus() {
            if (mGestureLockCheck != null) {
                if (!TextUtils.isEmpty((String) SPUtils.get(context, Constant.SP_GESTURE, ""))) {
                    mGestureLockCheck.setChecked(true);
                }
            }
        }
    }

    public static final String GESTURE_LOCK = "gesture_lock";

    private SettingsFragment mSettingsFragment;
}
