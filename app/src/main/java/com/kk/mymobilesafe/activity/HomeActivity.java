package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.adapter.NineGridsAdapter;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.listener.GridItemListener;

public class HomeActivity extends Activity {
    private static final String TAG = "main";
    GridView mGridView;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivity=this;
        initUI();
        initGridDate();

//        BlackNumDao dao = new BlackNumDao(this);
//        BlackNumBean bean=new BlackNumBean();
//        dao.initData();
//        Cursor cursor = dao.queryAll();
//        while (cursor.moveToNext()){
//            bean._id=cursor.getInt(0);
//            bean.phone=cursor.getString(1);
//            bean.blockType=cursor.getInt(2);
//            LogCatUtil.getSingleton().i(TAG,bean.toString());
//        }
    }

    private void initGridDate() {

        mGridView.setAdapter(new NineGridsAdapter(Constant.GridData.icons,Constant.GridData.descs,mActivity));
        mGridView.setOnItemClickListener(new GridItemListener(mActivity));


    }

    private void initUI() {
        mGridView= (GridView) findViewById(R.id.gl_nineGrid);
    }


}
