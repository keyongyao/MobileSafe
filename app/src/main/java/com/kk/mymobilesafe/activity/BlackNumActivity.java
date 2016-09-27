package com.kk.mymobilesafe.activity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.adapter.ListViewBlacknumAdapter;
import com.kk.mymobilesafe.bean.BlackNumBean;
import com.kk.mymobilesafe.dao.BlackNumDao;
import com.kk.mymobilesafe.utils.LogCatUtil;

import java.util.ArrayList;

public class BlackNumActivity extends ActionBarActivity {
    private static final String TAG = "main";
    /**
     * 添加黑名单按钮
     */
    Button btnAddBlackNum;
    /**
     * 显示黑名单的ListView
     */
    ListView lvShowBlackNum;
    ArrayList<BlackNumBean> blackNumBeanList = new ArrayList<>();
    BlackNumBean beanForAdd;
    ListViewBlacknumAdapter listViewBlacknumAdapter;
    boolean isLodingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_num);
        initUI();
        // initData();
        pagedData();
        setListener();

    }

    private void pagedData() {
        isLodingData = true;
        // 每次 取出数据的集合
        ArrayList<BlackNumBean> addBeanList = new ArrayList<>();
        // 每次取25 条数据
        Cursor cursor = new BlackNumDao(getApplicationContext()).paged(blackNumBeanList.size());
        while (cursor.moveToNext()) {
            BlackNumBean bean = new BlackNumBean();
            bean._id = cursor.getInt(0);
            bean.phone = cursor.getString(1);
            bean.blockType = cursor.getInt(2);
            addBeanList.add(bean);
        }
        // 往最终的集合 放数据
        blackNumBeanList.addAll(addBeanList);
        if (listViewBlacknumAdapter != null)
            listViewBlacknumAdapter.notifyDataSetChanged();

        isLodingData = false;
    }

    // 初始化 黑名单数据
    private void initData() {
        BlackNumDao dao = new BlackNumDao(getApplicationContext());
        dao.initData();
        Cursor cursor = dao.queryAll();
        while (cursor.moveToNext()) {
            BlackNumBean bean = new BlackNumBean();
            bean._id = cursor.getInt(0);
            bean.phone = cursor.getString(1);
            bean.blockType = cursor.getInt(2);
            blackNumBeanList.add(bean);
        }
    }


    private void setListener() {
        // 添加黑名单按钮
        btnAddBlackNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beanForAdd = new BlackNumBean();
                showAddDialog();
            }
        });
        // ListView 适配器
        lvShowBlackNum.setAdapter((listViewBlacknumAdapter = new ListViewBlacknumAdapter(blackNumBeanList, this)));
        // listView 滚动监视器
        lvShowBlackNum.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL: {
                        LogCatUtil.getSingleton().i(TAG, "SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    }
                    case SCROLL_STATE_IDLE: {
                        if (lvShowBlackNum.getLastVisiblePosition() == blackNumBeanList.size() - 1 && !isLodingData) {
                            // 分页加载数据
                            pagedData();
                        }
                        break;
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LogCatUtil.getSingleton().i(TAG, firstVisibleItem + "  " + visibleItemCount + "  " + totalItemCount);
            }
        });
    }

    /**
     * 添加黑名单对话框
     */
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog addDialog = builder.create();
        View dialogInflate = View.inflate(this, R.layout.dialog_add_blacknum, null);
        addDialog.setView(dialogInflate);
        addDialog.show();
        // 初始化对话框中的控件
        final EditText etInputNum = (EditText) dialogInflate.findViewById(R.id.et_blacknum_inputnum);
        RadioGroup rgblockType = (RadioGroup) dialogInflate.findViewById(R.id.rg_blacknum_type);
        Button btnOK = (Button) dialogInflate.findViewById(R.id.btn_blacknum_addDialog_OK);
        Button btnCancel = (Button) dialogInflate.findViewById(R.id.btn_blacknum_addDialog_cancel);

        // 为控件设置事件监视器
        rgblockType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blacknum_type_msg: {
                        beanForAdd.blockType = 1;
                        break;
                    }
                    case R.id.rb_blacknum_type_phone: {
                        beanForAdd.blockType = 2;

                        break;
                    }
                    case R.id.rb_blacknum_type_both: {
                        beanForAdd.blockType = 3;
                        break;
                    }
                }
            }
        });
        // 确定添加按钮
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etInputNum.getText().toString().trim();
                beanForAdd.phone = phone;
                blackNumBeanList.add(0, beanForAdd);
                Toast.makeText(BlackNumActivity.this, "" + beanForAdd.toString(), Toast.LENGTH_SHORT).show();
                new BlackNumDao(getApplicationContext()).insert(beanForAdd);
                listViewBlacknumAdapter.notifyDataSetChanged();
                addDialog.dismiss();
            }
        });
        // 取消添加按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
    }

    private void initUI() {
        btnAddBlackNum = (Button) findViewById(R.id.btn_blcknum_add);
        lvShowBlackNum = (ListView) findViewById(R.id.lv_blacknum_show);
    }
}
