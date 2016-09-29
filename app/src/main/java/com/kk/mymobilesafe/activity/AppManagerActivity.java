package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.bean.AppInfoBean;
import com.kk.mymobilesafe.utils.LogCatUtil;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kk.mymobilesafe.R.id.tv_appmanager_popupWindow_share;

public class AppManagerActivity extends ActionBarActivity {
    private static final int LOADINGFINISHED = 99;  // 加载完App
    private static final int CLASSFIYFINISHED = 100; // 分类完App
    public ProgressDialog mDialog;
    TextView tvInerStorage, tvExtraStorage;
    ListView lvAppList;
    TextView showAppTypeCount;
    ArrayList<AppInfoBean> appInfoBeanArrayList, sysAppList, userAppList;
    Handler mHandler;
    Activity mActivty;
    HashMap<View, Object> analyseCount = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        appInfoBeanArrayList = new ArrayList<>();
        sysAppList = new ArrayList<>();
        userAppList = new ArrayList<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOADINGFINISHED) {
                    classfiyAppinfoBean(appInfoBeanArrayList, sysAppList, userAppList);
                }
                if (CLASSFIYFINISHED == msg.what && mDialog != null) {
                    lvAppList.setAdapter(new AppListViewAdapter());
                    mDialog.dismiss();
                }
            }
        };
        mActivty = this;
        initUI();
        loadingAppsInfo(appInfoBeanArrayList);
        setListener();
    }

    private void setListener() {
        // 滚动监视器  用于改变 App 分类计数
        lvAppList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= userAppList.size() + 1) {
                    showAppTypeCount.setText("系统程序: " + sysAppList.size());
                    LogCatUtil.getSingleton().i("main", " firstVisibleItem userAppList.size() :" + firstVisibleItem + " " + userAppList.size());
                } else if (firstVisibleItem >= 1) {
                    showAppTypeCount.setText("用户程序: " + userAppList.size());
                }
            }
        });
        // 添加 卸载  启动  分享  pupopWindow
        lvAppList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 2 个 文本 和系统应用不能  弹出菜单
                if (position == 0 || position == userAppList.size() + 1) {
                    return false;
                } else {
                    showPopupWindow(view, position, id);
                }

                return false;
            }


        });
    }

    // 弹出 窗体  设置 窗体内控件的事件
    private void showPopupWindow(View view, int position, long id) {
        View contentView = View.inflate(mActivty, R.layout.layout_popup_window, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        setSubViewLinstener(contentView, popupWindow, position);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.alpha(Color.TRANSPARENT)));
        popupWindow.showAsDropDown(view, 300, -view.getHeight());
    }

    // 设置 popup菜单的事件
    private void setSubViewLinstener(View contentView, final PopupWindow popupWindow, final int position) {
        TextView uninstall = (TextView) contentView.findViewById(R.id.tv_appmanager_popupWindow_uninstall);
        TextView start = (TextView) contentView.findViewById(R.id.tv_appmanager_popupWindow_start);
        TextView share = (TextView) contentView.findViewById(tv_appmanager_popupWindow_share);
        // 卸载 APP
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // 系统的应用不能卸载
                if (position > userAppList.size() + 1) {
                    Toast.makeText(mActivty, "系统应用不能卸载", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pkgName = userAppList.get(position - 1).pkgName;
                uninstallApp(pkgName);
                Toast.makeText(mActivty, "uninstall:" + userAppList.get(position - 1).name, Toast.LENGTH_SHORT).show();
            }
        });
        // 启动 APP
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (position < userAppList.size() + 2) {
                    Toast.makeText(mActivty, "start " + userAppList.get(position - 1).name, Toast.LENGTH_SHORT).show();
                    String pkgName = userAppList.get(position - 1).pkgName;
                    startApp(pkgName);
                } else if (position >= userAppList.size() + 2) {
                    Toast.makeText(mActivty, "start " + sysAppList.get(position - userAppList.size() - 2).name, Toast.LENGTH_SHORT).show();
                    String pkgName = sysAppList.get(position - userAppList.size() - 2).pkgName;
                    startApp(pkgName);
                }

            }
        });
        // 分享 APP
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (position < userAppList.size() + 2) {
                    Toast.makeText(mActivty, "share " + userAppList.get(position - 1).name, Toast.LENGTH_SHORT).show();
                    shareApp(userAppList.get(position - 1).name);
                } else if (position >= userAppList.size() + 2) {
                    Toast.makeText(mActivty, "share " + sysAppList.get(position - userAppList.size() - 2).name, Toast.LENGTH_SHORT).show();
                    shareApp(sysAppList.get(position - userAppList.size() - 2).name);
                }
            }
        });
    }

    private void uninstallApp(String pkgName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + pkgName));
        startActivity(intent);
    }

    private void startApp(String pkgName) {
        PackageManager pm = getPackageManager();
        Intent launchIntentForPackage = pm.getLaunchIntentForPackage(pkgName);
        startActivity(launchIntentForPackage);
    }

    private void shareApp(String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "分享一个应用,应用名称为" + appName);
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * 分类APP
     *
     * @param allAppList  所有APP集合
     * @param sysAppList  系统APP集合
     * @param userAppList 用户App集合
     */
    private void classfiyAppinfoBean(ArrayList<AppInfoBean> allAppList,
                                     ArrayList<AppInfoBean> sysAppList,
                                     ArrayList<AppInfoBean> userAppList) {
        for (AppInfoBean bean : allAppList
                ) {
            if (bean.isUserApp) {
                userAppList.add(bean);
            } else {
                sysAppList.add(bean);
            }
        }
        Message msg = Message.obtain();
        msg.what = CLASSFIYFINISHED;
        mHandler.sendMessage(msg);

    }


    /**
     * @param appInfoBeanList 保存结果集
     */
    private void loadingAppsInfo(final ArrayList<AppInfoBean> appInfoBeanList) {
        if (appInfoBeanList == null)
            throw new InvalidParameterException(" appInfoBeanList is null");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_CONFIGURATIONS);
                for (PackageInfo pg : packages
                        ) {
                    AppInfoBean bean = new AppInfoBean();
                    bean.pkgName = pg.applicationInfo.packageName;
                    bean.name = pg.applicationInfo.loadLabel(pm).toString();
                    bean.icon = pg.applicationInfo.loadIcon(pm);
                    if ((pg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                        bean.isUserApp = false;
                    } else {
                        bean.isUserApp = true;
                    }
                    if ((pg.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                        bean.isRom = false;
                    } else {
                        bean.isRom = true;
                    }
                    appInfoBeanList.add(bean);
                }
                Message msg = Message.obtain();
                msg.what = LOADINGFINISHED;
                mHandler.sendMessage(msg);
            }
        }).start();


    }

    private void initUI() {
        tvInerStorage = (TextView) findViewById(R.id.tv_appManager_interStorage_available);
        String innerfreesize = Formatter.formatFileSize(this, Environment.getDataDirectory().getFreeSpace());
        tvInerStorage.setText(tvInerStorage.getText().toString() + innerfreesize);
        tvExtraStorage = (TextView) findViewById(R.id.tv_appManager_extralStorage_available);
        String extrafreesize = Formatter.formatFileSize(this, Environment.getExternalStorageDirectory().getFreeSpace());
        tvExtraStorage.setText(tvExtraStorage.getText().toString() + extrafreesize);
        lvAppList = (ListView) findViewById(R.id.lv_appManager_applistview);
        showAppTypeCount = (TextView) findViewById(R.id.tv_appmanager_showAppTypeCount);
        showProgressDialog();
    }

    /**
     * 手机慢 显示加载框
     */
    private void showProgressDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在加载");
        mDialog.setMessage("正在加载App信息.....");
        mDialog.show();
    }

    /**
     * ListView 适配器
     */
    class AppListViewAdapter extends BaseAdapter {
        // 返回View条目类型
        @Override
        public int getItemViewType(int position) {
            // +1 有一个文本的条目
            if (position == 0 || position == userAppList.size() + 1)
                return 1; // 文字型 条目

            return 0;// 原本条目
        }

        // 返回View类型的数目
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            // 有2个 文本的条形 条目
            return sysAppList.size() + userAppList.size() + 2;
        }

        @Override
        public AppInfoBean getItem(int position) {
            if (position == 0 || position == userAppList.size() + 1) {
                return null;
            }
            if (position < userAppList.size() + 1) {
                // 返回用户App
                return userAppList.get(position - 1);
            }
            // 返回系统应用
            return sysAppList.get(position - userAppList.size() - 2);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView view = null; // 0. 但返回的 应用类型条目
            //1.判断类型
            int type = getItemViewType(position);
            if (type == 0) {
                // 2.返回原本的条目
                Holder holder;
                if (convertView == null) {
                    convertView = View.inflate(mActivty, R.layout.appinfo_listview_item, null);
                    holder = new Holder();
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_appManager_listItem_icon);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_appManager_listItem_title);
                    holder.subTitle = (TextView) convertView.findViewById(R.id.tv_appManager_listItem_subtitle);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                holder.icon.setImageDrawable(getItem(position).icon);
                holder.title.setText(getItem(position).name);
                holder.subTitle.setText(textTag(getItem(position).isRom) + " " + textSysOrUser(getItem(position).isUserApp));
                return convertView;
            } else if (type == 1) {
                HolderText holderText;
                // 3. 返回 文本条 条目
                if (convertView == null) {
                    convertView = View.inflate(mActivty, R.layout.layout_textview, null);
                    holderText = new HolderText();
                    holderText.textView = (TextView) convertView.findViewById(R.id.tv_appmanager_showAppCount);
                    convertView.setTag(holderText);
                } else {
                    holderText = (HolderText) convertView.getTag();
                }

                if (position == 0) {
                    holderText.textView.setText(textSysOrUser(getItem(position + 1).isUserApp) + "共：" + userAppList.size());
                }
                if (position == userAppList.size() + 1) {
                    holderText.textView.setText(textSysOrUser(getItem(position + 1).isUserApp) + "共：" + sysAppList.size());
                }
            }
            analyseCount.put(view, "");
            LogCatUtil.getSingleton().i("main", " 未优化的文本条目数：" + analyseCount.size());
            return convertView;
        }

        String textTag(Boolean isRom) {
            return isRom ? "内置存储" : "外部存储";
        }

        String textSysOrUser(Boolean isUserApp) {
            return isUserApp ? "用户程序" : "系统程序";
        }

        private class Holder {
            ImageView icon;
            TextView title, subTitle;
        }

        private class HolderText {
            TextView textView;
        }

    }
}
