package com.bluebirdme.mes.serial.callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * 托利多—8142 型号地磅
 *
 * @author Goofy
 * @Date 2017年6月15日 下午3:41:36
 */
public class Toledo_8142_Callback extends AbstractReadCallback {
    @Override
    public void call(BufferedReader reader, InputStream is) {
        String result = "";
        try {
            result = reader.readLine();
            if (result.toUpperCase().indexOf("KG") != -1) {
                addResult(result.trim().split(" ")[0]);
            }
        } catch (IOException e) {
//			e.printStackTrace();
        }
    }
}
