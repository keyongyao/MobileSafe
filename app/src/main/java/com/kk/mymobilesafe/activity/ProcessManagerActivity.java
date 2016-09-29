package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.bean.ProcessInfoBean;
import com.kk.mymobilesafe.utils.ProcessUtil;

import java.util.ArrayList;

public class ProcessManagerActivity extends Activity {
    private static final int LOADINGDATAOK = 22;
    TextView tvAllProcessCount, tvMemoryStatus, tvClassfiyProcessCount;
    ListView lvProcess;
    Button btnAll, btnAllIinverse, btnOneKeyClean, btnSetting;
    ProgressDialog mProgressDialog;
    Handler mHandler;
    Activity mActivity;
    volatile ArrayList<ProcessInfoBean> mUserProcessList, mSysProcessList;
    ProcessListAdapter mProcessListAdapter;
    ProcessUtil mProcessUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        mActivity = this;
        mProcessUtil = new ProcessUtil(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOADINGDATAOK) {
                    lvProcess.setAdapter(mProcessListAdapter);
                    mProgressDialog.dismiss();
                }
            }
        };
        initUI();
        initData();
        mProcessListAdapter = new ProcessListAdapter();
        setListener();
    }

    // 进程列表 滚动 事件 监视器
    private void setListener() {
        lvProcess.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem <= mUserProcessList.size()) {
                    tvClassfiyProcessCount.setText("用户进程：" + mUserProcessList.size());
                } else if (firstVisibleItem == mUserProcessList.size() + 1) {
                    tvClassfiyProcessCount.setText("系统进程：" + mSysProcessList.size());
                }
            }
        });
        // 全选按钮
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfoBean pi : mUserProcessList) {
                    pi.isCheck = true;
                }
                for (ProcessInfoBean pi : mSysProcessList) {
                    pi.isCheck = true;
                }
                mProcessListAdapter.notifyDataSetChanged();
            }
        });
        //反选按钮
        btnAllIinverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfoBean pi : mUserProcessList) {
                    pi.isCheck = !pi.isCheck;
                }
                for (ProcessInfoBean pi : mSysProcessList) {
                    pi.isCheck = !pi.isCheck;
                }
                mProcessListAdapter.notifyDataSetChanged();
            }
        });
        btnOneKeyClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMem = mProcessUtil.getAvailableMemory();
                ProcessUtil.killProcess(mActivity, mUserProcessList);
                ProcessUtil.killProcess(mActivity, mSysProcessList);
                long endMem = mProcessUtil.getAvailableMemory();
                mProgressDialog.show();


                initData();
                mProcessListAdapter.notifyDataSetChanged();
                Toast.makeText(mActivity, "释放内存:" + Formatter.formatFileSize(mActivity, endMem - startMem), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initData() {
        initStatus();

        mUserProcessList = new ArrayList<>();
        mSysProcessList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mProcessUtil.loadClassfiyProcessInfoList(mUserProcessList, mSysProcessList);
                Message msg = Message.obtain();
                msg.what = LOADINGDATAOK;
                mHandler.sendMessage(msg);
            }
        }).start();


    }

    // 显示 进程数 和 内存状态
    private void initStatus() {
        int allProcessCount = mProcessUtil.getAllProcessCount();
        tvAllProcessCount.setText("进程总数：" + allProcessCount);

        String availableMem = Formatter.formatFileSize(this, mProcessUtil.getAvailableMemory());
        String totalMem = Formatter.formatFileSize(this, mProcessUtil.getTotalMemory());
        tvMemoryStatus.setText(String.format("剩余/总共：%s/%s", availableMem, totalMem));
    }

    // 找出这些控件
    private void initUI() {
        lvProcess = (ListView) findViewById(R.id.lv_processManager_processlistview);
        tvAllProcessCount = (TextView) findViewById(R.id.tv_processManager_allprocessCount);
        tvMemoryStatus = (TextView) findViewById(R.id.tv_processManager_memorystatus);
        tvClassfiyProcessCount = (TextView) findViewById(R.id.tv_processManager_showClassfiyProcessCount);
        btnAll = (Button) findViewById(R.id.btn_processManager_all);
        btnAllIinverse = (Button) findViewById(R.id.btn_processManager_allInverse);
        btnOneKeyClean = (Button) findViewById(R.id.btn_processManager_oneKeyClean);
        btnSetting = (Button) findViewById(R.id.btn_processManager_setting);

        // 显示进度对话框  收集手机进程信息
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("加载进程");
        mProgressDialog.setMessage("正在加载进程信息中......");
        mProgressDialog.show();
    }

    class ProcessListAdapter extends BaseAdapter {


        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mUserProcessList.size() + 1) {
                return 1; // 文本类型
            }
            return 0; // 正常 item
        }

        @Override
        public int getViewTypeCount() {
            // 2 种类型的 View
            return 2;
        }

        @Override
        public int getCount() {
            return mUserProcessList.size() + mSysProcessList.size() + 2;
        }

        @Override
        public ProcessInfoBean getItem(int position) {
            if (position == 0 || position == mUserProcessList.size() + 1) {
                return null;
            }
            if (position < mUserProcessList.size() + 1) {
                return mUserProcessList.get(position - 1);
            }
            return mSysProcessList.get(position - mUserProcessList.size() - 2);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == 0) {
                final Holder holder;
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.processinfo_listview_item, null);
                    holder = new Holder();
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_processManager_listItem_icon);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_processManager_listItem_title);
                    holder.subTitle = (TextView) convertView.findViewById(R.id.tv_processManager_listItem_subtitle);
                    holder.cbIsCheck = (CheckBox) convertView.findViewById(R.id.cb_processmanager_listItem_checkbox);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                holder.icon.setImageDrawable(getItem(position).icon);
                holder.title.setText(getItem(position).name);
                holder.subTitle.setText("内存占用：" + Formatter.formatFileSize(mActivity, getItem(position).memSize));
                // 全选按钮
                holder.cbIsCheck.setChecked(getItem(position).isCheck);
                holder.cbIsCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.cbIsCheck.isChecked()) {
                            getItem(position).isCheck = true;
                        } else if (!holder.cbIsCheck.isChecked()) {
                            getItem(position).isCheck = false;
                        }
                    }
                });
                return convertView;
            }
            // 剩下的情况是特殊的 文本
            HolderText holderText;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.layout_textview_process, null);
                holderText = new HolderText();
                holderText.showClassfiyProcessCount = (TextView) convertView.findViewById(R.id.tv_processManager_showprocessCount);
                convertView.setTag(holderText);
            } else {
                holderText = (HolderText) convertView.getTag();
            }
            if (position == 0) {
                holderText.showClassfiyProcessCount.setText("用户进程：" + mUserProcessList.size());
            }
            holderText.showClassfiyProcessCount.setText("系统进程：" + mSysProcessList.size());

            return convertView;
        }

        private class Holder {
            ImageView icon;
            TextView title, subTitle;
            CheckBox cbIsCheck;
        }

        private class HolderText {
            TextView showClassfiyProcessCount;
        }
    }
}
