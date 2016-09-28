package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.bean.SMSBackupBean;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AToolsActivity extends ActionBarActivity {
    private static final String TAG = "main";
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
        // 归属地查询
        tvQueryLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AToolsQueryLocationActivity.class));
            }
        });
        //短信备份
        tvSMSbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backupSMS();
            }
        });
    }

    private void initUI() {
        tvQueryLocation = (TextView) findViewById(R.id.tv_atool_queryLocation);
        tvSMSbackup = (TextView) findViewById(R.id.tv_atool_SMSbackup);
        tvQueryPhone = (TextView) findViewById(R.id.tv_atool_query_phoneNum);
        tvAppLock = (TextView) findViewById(R.id.t_atool_applock);
    }

    // 备份短信
    private void backupSMS() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("备份短信");
        dialog.setMessage("备份短信中，请稍后.....");
        dialog.setIcon(R.mipmap.backup);
        dialog.setCancelable(true);
        dialog.show();
        readSMS(dialog);

    }

    private void readSMS(final ProgressDialog dialog) {
        new Thread(
                new Runnable() {
                    int index = 0;

                    @Override
                    public void run() {
                        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), new String[]{"address",
                                "date_sent", "body"}, null, null, null);
                        final ArrayList<SMSBackupBean> smsBeanList = new ArrayList<>();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMax(smsBeanList.size());
                            }
                        });
                        while (cursor.moveToNext()) {
                            SMSBackupBean bean = new SMSBackupBean();
                            bean.address = cursor.getString(0);
                            bean.date_sent = cursor.getString(1);
                            bean.body = cursor.getString(2);
                            smsBeanList.add(bean);
                        }

                        XmlSerializer xmlSerializer = Xml.newSerializer();
                        File backup = new File(Environment.getExternalStorageDirectory().getPath(), "smsBack.xml");

                        try {
                            OutputStream outputStream = new FileOutputStream(backup);
                            xmlSerializer.setOutput(outputStream, "utf-8");
                            xmlSerializer.startDocument("utf-8", true);
                            xmlSerializer.startTag(null, "SMSs");
                            for (SMSBackupBean bean : smsBeanList
                                    ) {
                                xmlSerializer.startTag(null, "sms");

                                xmlSerializer.startTag(null, "address");
                                xmlSerializer.text(bean.address);
                                xmlSerializer.endTag(null, "address");

                                xmlSerializer.startTag(null, "date_sent");
                                xmlSerializer.text(bean.date_sent);
                                xmlSerializer.endTag(null, "date_sent");

                                xmlSerializer.startTag(null, "body");
                                xmlSerializer.text(bean.body);
                                xmlSerializer.endTag(null, "body");
                                xmlSerializer.endTag(null, "sms");
                                SystemClock.sleep(200);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.setProgress(++index);
                                    }
                                });
                            }
                            xmlSerializer.endTag(null, "SMSs");
                            xmlSerializer.endDocument();
                            outputStream.flush();
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setMessage("备份完成\n 2秒后自动关闭此窗体");
                            }
                        });
                        SystemClock.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }
                }
        ).start();
    }


}
