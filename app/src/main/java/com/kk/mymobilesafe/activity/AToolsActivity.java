package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.utils.CopyFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AToolsActivity extends ActionBarActivity {
    TextView tvQueryLocation, tvSMSbackup, tvQueryPhone, tvAppLock;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        mActivity = this;
        initPhoneDB();
        initUI();
        setListener();
    }

    private void initPhoneDB() {
        AssetManager assetManager = getAssets();
        File file = new File(getFilesDir(), "address.db");
        if (file.exists()) {
            return;
        }
        try {
            InputStream inputStream = assetManager.open("address.db");
            FileOutputStream fileOutputStream = openFileOutput("address.db", Context.MODE_PRIVATE);
            boolean copyResult = CopyFile.copy(inputStream, fileOutputStream);
            if (copyResult) {
                Toast.makeText(this, "初始化地址数据库成功", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        tvQueryLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AToolsQueryLocationActivity.class));
            }
        });
    }

    private void initUI() {
        tvQueryLocation = (TextView) findViewById(R.id.tv_atool_queryLocation);
        tvSMSbackup = (TextView) findViewById(R.id.tv_atool_SMSbackup);
        tvQueryPhone = (TextView) findViewById(R.id.tv_atool_query_phoneNum);
        tvAppLock = (TextView) findViewById(R.id.t_atool_applock);
    }
}
