package com.bluebirdme.mes.serial.callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * 蓝剑牌吊秤
 *
 * @author Goofy
 * @Date 2017年6月15日 下午3:45:29
 */
public class LanJian_OCS_SZ_2_Callback extends AbstractReadCallback {
    @Override
    public void call(BufferedReader reader, InputStream is) {
        try {
            char[] buff = new char[1024];
            reader.read(buff);
            String c = new String(buff).trim();
            if (c.indexOf("=") > -1) {
                if (sb.indexOf("=") > -1) {
                    sb.append(c, 0, c.indexOf("="));
                    addResult(sb.reverse().toString().replaceAll("=|-", ""));
                    sb = new StringBuffer();
                } else {
                    sb.append(c.substring(c.indexOf("=")));
                }
            } else {
                if (sb.indexOf("=") > -1) {
                    sb.append(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
