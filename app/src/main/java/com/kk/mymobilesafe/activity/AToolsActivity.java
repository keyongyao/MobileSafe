package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.kk.mymobilesafe.R;

public class AToolsActivity extends ActionBarActivity {
    TextView tvQueryLocation, tvSMSbackup, tvQueryPhone, tvAppLock;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        mActivity = this;
        initUI();
        setListener();
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
