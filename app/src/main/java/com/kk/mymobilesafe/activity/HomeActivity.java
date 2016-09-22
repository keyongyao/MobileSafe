package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.adapter.NineGridsAdapter;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.listener.GridItemListener;

public class HomeActivity extends AppCompatActivity {
    GridView mGridView;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivity=this;
        initUI();
        initGridDate();
    }

    private void initGridDate() {

        mGridView.setAdapter(new NineGridsAdapter(Constant.GridData.icons,Constant.GridData.descs,mActivity));
        mGridView.setOnItemClickListener(new GridItemListener(mActivity));


    }

    private void initUI() {
        mGridView= (GridView) findViewById(R.id.gl_nineGrid);
    }


}
