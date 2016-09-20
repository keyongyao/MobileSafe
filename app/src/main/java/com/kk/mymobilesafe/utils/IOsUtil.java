package com.kk.mymobilesafe.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/19.
 */
public class IOsUtil {
    /**
     *
     * @param ios 不定数可以关闭的输入输出流
     */
    public static void closeALL(Closeable ... ios){
        for (Closeable io:ios
             ) {
            if (null!=io){
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
