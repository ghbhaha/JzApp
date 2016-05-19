package com.suda.jzapp.ui.activity.record;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExportRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView2(R.layout.activity_export_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        recordManager = new RecordManager(this);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startDate = calendar.getTime();
        stopDate = startDate;
        initWidget();
    }


    @Override
    protected void initWidget() {
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvStop = (TextView) findViewById(R.id.tv_stop);
    }

    public void selectStart(View view) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        final DatePickerDialog tpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        setStartDate = true;
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        startDate = calendar.getTime();
                        mTvStart.setText("开始日期:" + simpleDateFormat.format(startDate));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        tpd.setAccentColor(getResources().getColor(getMainTheme().getMainColorID()));
        tpd.show(getFragmentManager(), "Timepickerdialog");
        tpd.setMaxDate(Calendar.getInstance());
    }

    public void selectEnd(View view) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(stopDate);
        DatePickerDialog tpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        setStopDate = true;
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        stopDate = calendar.getTime();
                        mTvStop.setText("开始日期:" + simpleDateFormat.format(stopDate));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        tpd.setAccentColor(getResources().getColor(getMainTheme().getMainColorID()));
        tpd.show(getFragmentManager(), "Timepickerdialog");
        tpd.setMaxDate(Calendar.getInstance());
    }

    public void exportData(View view) {
        if (!setStartDate || !setStopDate) {
            Toast.makeText(ExportRecordActivity.this, "请选择开始结束时间", Toast.LENGTH_SHORT).show();
            return;
        }

        recordManager.exportToExcel(startDate.getTime(), stopDate.getTime(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_ERROR) {
                    Toast.makeText(ExportRecordActivity.this, "导出失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExportRecordActivity.this, "导出成功,文件保存在" + msg.obj.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private RecordManager recordManager;
    private Date startDate;
    private Date stopDate;
    private TextView mTvStart, mTvStop;
    private SimpleDateFormat simpleDateFormat;
    private boolean setStartDate, setStopDate = false;

}
