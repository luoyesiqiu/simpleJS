package com.luoye.simplejs.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyw on 2017/10/28.
 */
public class Utils {

    public static   String readDemoFile(Context context)
    {
        StringBuilder sb=new StringBuilder();
        InputStream inputStream=null;
        try {
            inputStream= context.getAssets().open("code.js");
            byte[] buf=new byte[1024];
            int len=0;
            while((len=inputStream.read(buf))!=-1)
            {
                sb.append(new String(buf,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream!=null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return  sb.toString();
    }
}
