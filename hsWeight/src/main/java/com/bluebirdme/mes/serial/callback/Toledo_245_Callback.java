package com.bluebirdme.mes.serial.callback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * 托利多—245 型号地磅
 *
 * @author Goofy
 * @Date 2017年6月15日 下午3:41:36
 */
public class Toledo_245_Callback extends AbstractReadCallback {
    @Override
    public void call(BufferedReader reader, InputStream is) {
        try {
            String result = reader.readLine();
            result = new String(result.getBytes("GBK"), "GBK");
            if (result.indexOf("净重") > -1) {
                addResult(result.toUpperCase().replaceAll("净重|KG| ", "").trim());
            }
        } catch (Exception e) {
        }
    }
}
