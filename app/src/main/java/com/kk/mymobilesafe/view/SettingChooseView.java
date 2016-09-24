package com.kk.mymobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.mymobilesafe.R;

/**
 * 设置中心的自定义的 选择子菜单
 * Created by Administrator on 2016/9/24.
 */

public class SettingChooseView extends RelativeLayout {
    final String NAMESPACE = "http://schemas.android.com/apk/res/com.kk.mymobilesafe";
    TextView tvTitle, tvSubTitle;

    public SettingChooseView(Context context) {
        this(context, null);
    }

    public SettingChooseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_center_choose_menu, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_setting_choose_sub_menu_title);
        tvSubTitle = (TextView) view.findViewById(R.id.tv_setting_choose_sub_menu_subTitle);
        // 从XML文件获取  title 和 subTitle 的文字
        initAttrs(attrs);
    }

    // 设置UI 文字
    private void initUI(String title, String subTitle) {
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
    }

    private void initAttrs(AttributeSet attrs) {
        String title = attrs.getAttributeValue(NAMESPACE, "title");
        String subTitle = attrs.getAttributeValue(NAMESPACE, "subTitle");
        initUI(title, subTitle);
    }

    public void changeSubTitleText(String subTitle) {
        tvSubTitle.setText(subTitle);
    }


}
