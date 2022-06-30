package com.bluebirdme.mes.resources;

import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.PropertiesUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * 属性
 *
 * @author Goofy
 * @Date 2017年6月15日 下午4:52:06
 */
public class Prop {
    Properties ps = new Properties();
    /**
     * 端口号
     */
    public static final String KEY_PORT = "PORT";
    /**
     * 波特率
     */
    public static final String KEY_BAUD_RATE = "BAUD_RATE";
    /**
     * 服务器地址
     */
    public static final String KEY_SERVER_ADDR = "SERVER_ADDR";
    /**
     * 设备型号
     */
    public static final String KEY_DEVICE_CALLBACK = "DEVICE_CALLBACK";
    /**
     * 校验位
     */
    public static final String KEY_PARITY_BIT = "PARITY_BIT";
    /**
     * 发送的命令
     */
    public static final String KEY_COMMAND = "COMMAND";
    /**
     * 数据位
     */
    public static final String KEY_DATA_BIT = "DATA_BIT";
    /**
     * 停止位
     */
    public static final String KEY_STOP_BIT = "STOP_BIT";

    public static final String KEY_FTP_HOST = "FTP.HOST";

    public static final String KEY_FTP_PORT = "FTP.PORT";

    public static final String KEY_FTP_USER = "FTP.USER";

    public Prop() {
        try {
            File configFile = new File(this.getClass().getResource("/").getPath() + File.separator + "config.properties");
            if (!configFile.exists()) {
                Object[] options = {"确认"};
                JOptionPane.showOptionDialog(null, "找不到配置文件或者无权限访问,请打开配置文件查看详情,路径:C:\\config.properties", "提示", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                FileUtils.writeToFile("#COM PORT PORT=COM3 BAUD_RATE=9600 SERVER_ADDR=http\\://10.10.1.9/mes #TOLED_8142,TOLED_245,LANJIAN_OCS_SZ_2 DEVICE_TYPE=TOLED_245 #NONE:0,ODD:1,EVEN:2,MARK:3,SPACE:4 PARITY_BIT=0 COMMAND=P #5,6,7,8 DATA_BIT=8 #1:1,2:2,1.5:3 STOP_BIT=1", "C:\\config.properties", true, true);
                System.exit(0);
            }
            ps = PropertiesUtils.readProperties(configFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProp(String key) {
        return ps.getProperty(key);
    }
}
