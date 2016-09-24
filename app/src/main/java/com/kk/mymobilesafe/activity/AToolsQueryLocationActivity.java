package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.dao.QueryLocation;
import com.kk.mymobilesafe.signle.MySignal;

public class AToolsQueryLocationActivity extends Activity {
    EditText etInputPhone;
    Button btnQueryPhone;
    TextView tvShowLocation;
    Activity mActivity;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools_query_location);
        mActivity = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MySignal.ATool.QUERYRESULT) {
                    tvShowLocation.setText((String) msg.obj);
                }
            }
        };
        initUI();
        setlistener();
    }

    private void setlistener() {
        btnQueryPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = etInputPhone.getText().toString().trim();
                // 输入的号码为空时
                if (TextUtils.isEmpty(phonenum)) {
                    etInputPhone.setError("输入号码");
                    Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }
                new QueryLocation().query(mActivity, phonenum, mHandler);
            }
        });
        // 文本改变监视器
        etInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phonenum = etInputPhone.getText().toString().trim();
                QueryLocation.query(mActivity, phonenum, mHandler);
            }
        });

    }

    private void initUI() {
        etInputPhone = (EditText) findViewById(R.id.et_atool_inputPhone);
        btnQueryPhone = (Button) findViewById(R.id.btn_atool_queryPhone);
        tvShowLocation = (TextView) findViewById(R.id.tv_atool_queryResult);
    }
}
