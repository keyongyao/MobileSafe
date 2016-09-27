package com.kk.mymobilesafe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 复制小工具类
 * Created by Administrator on 2016/9/23.
 */

public class CopyFileUtil {
    /**
     * @param inputStream  文件输入流
     * @param outputStream 文件输出流
     * @return true 成功复制  false 复制失败
     */
    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            if (inputStream == null || outputStream == null) {
                throw new IOException(" 输入流或输出流为空");
            }
            int len = 0;
            byte[] flush = new byte[2048];
            while (-1 != (len = inputStream.read(flush))) {
                outputStream.write(flush, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOsUtil.closeALL(inputStream, outputStream);
        }

        return true;
    }
}
