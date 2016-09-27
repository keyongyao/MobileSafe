package com.kk.mymobilesafe.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.activity.PhoneGuard1stActivity;
import com.kk.mymobilesafe.activity.PhoneGuardOverViewActivity;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

/**
 * 手机防盗模块 的设置密码对话框
 * Created by Administrator on 2016/9/21.
 */
public class PhoneGuardPwdDialog {
    Activity mActivity;
    EditText etFirstPWd, etComfirPWd;
    Button btnSure, btnCancel;

    public PhoneGuardPwdDialog(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void inputPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final AlertDialog dialog = builder.create();
        View inflate = View.inflate(mActivity, R.layout.inputpwd, null);
        dialog.setView(inflate);
        // TODO: 2016/9/21   MD5 Miami 
        dialog.show();
        btnSure = (Button) dialog.findViewById(R.id.btn_sure);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstPWd = (EditText) dialog.findViewById(R.id.et_pwd);
                etComfirPWd = (EditText) dialog.findViewById(R.id.et_comfirpwd);


                String pwd1 = etFirstPWd.getText().toString().trim();
                String pwd2 = etComfirPWd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd1)) {
                    etFirstPWd.setError("输入密码");
                } else if (TextUtils.isEmpty(pwd2)) {
                    etComfirPWd.setError("输入密码");
                } else if (pwd1.equals(pwd2)) {
                    if (SharedPreferenceUtil.putString(mActivity, Constant.PhoneGuard.GUARDPWD, pwd1)) {
                        Toast.makeText(mActivity, "保存密码成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mActivity.getApplicationContext(), PhoneGuard1stActivity.class);
                        mActivity.startActivity(intent);
                        dialog.dismiss();
                    }
                } else {
                    etFirstPWd.setError("输入密码不一致");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "取消密码设置", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void checkPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final AlertDialog dialog = builder.create();
        View inflate = View.inflate(mActivity, R.layout.checkpwd, null);
        dialog.setView(inflate);
        dialog.show();

        btnSure = (Button) dialog.findViewById(R.id.btn_sure);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstPWd = (EditText) dialog.findViewById(R.id.et_pwd);
                String pwd = etFirstPWd.getText().toString().trim();
                String pwd2 = SharedPreferenceUtil.getString(mActivity, Constant.PhoneGuard.GUARDPWD);
                if (pwd.equals(pwd2)) {
                    Toast.makeText(mActivity, "密码正确，解锁成功", Toast.LENGTH_SHORT).show();
                    // 如果用户把所有设置都配置了 进入 手机防盗总览页面
                        Intent intent = new Intent(mActivity.getApplicationContext(), PhoneGuardOverViewActivity.class);
                        mActivity.startActivity(intent);
                        dialog.dismiss();


                } else if (!pwd.equals(pwd2)) {
                    etFirstPWd.setError("密码错误");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }
}
