package com.kk.mymobilesafe.signle;

/**
 *  为各种 Handler 写的各种信号 <br>
 * Created by Administrator on 2016/9/19.
 */
public class MySignal {
    /**
     *  更新下载的信号
     */
    public static class Download{
        public final static  int STARTING=0;
        public final static  int SUCCEED=1;
        public final static int FAILURE=2;
        public final static int LOADING=3;
    }

    /**
     * 安装新包信号
     */
    public static class  Installation{
        public final static int  YES=1000;
        public final static int NO=1001;
    }

    /**
     *  更新相关的信号
     */
    public static class Update{
        public final static int  YES=2000;
        public final static int NO=2001;
        public final static int HAS_NEW_VERSION_YES=2002;
        public final static int HAS_NEW_VERSION_NO=2003;
        public final static int HAS_NEW_VERSION_ERROR=2004;
    }
}
