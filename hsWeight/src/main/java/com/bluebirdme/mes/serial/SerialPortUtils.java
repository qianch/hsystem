package com.bluebirdme.mes.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.resources.Prop;
import com.bluebirdme.mes.resources.Resources;
import com.bluebirdme.mes.serial.callback.AbstractReadCallback;
import com.bluebirdme.mes.serial.callback.LanJian_OCS_SZ_2_Callback;
import com.bluebirdme.mes.serial.callback.Toledo_245_Callback;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年6月15日 上午9:47:57
 */
public class SerialPortUtils implements SerialPortEventListener {
    Resources res = new Resources();
    Prop prop = new Prop();
    CommPortIdentifier portId;
    private SerialPort serialPort;
    private BufferedReader reader;
    private PrintWriter writer;

    AbstractReadCallback callback;

    private InputStream is;

    /**
     * 打开串口
     *
     * @return
     */
    public SerialPortUtils open() {
        try {
            portId = CommPortIdentifier.getPortIdentifier(prop.getProp(Prop.KEY_PORT));
            serialPort = (SerialPort) portId.open("PORTID", 2000);
            // 波特率
            serialPort.setSerialPortParams(Integer.parseInt(prop.getProp(Prop.KEY_BAUD_RATE)),
                    // 数据位数
                    Integer.parseInt(prop.getProp(Prop.KEY_DATA_BIT)),
                    // 停止位
                    Integer.parseInt(prop.getProp(Prop.KEY_STOP_BIT)),
                    // 校验
                    Integer.parseInt(prop.getProp(Prop.KEY_PARITY_BIT)));
        } catch (Exception e) {
            e.printStackTrace();
            res.error("串口打开失败");
            System.exit(0);
        }
        return this;
    }

    /**
     * 读取串口数据，实际上是通过{@link #serialEvent(SerialPortEvent)}事件监听来实现的
     *
     * @param callback
     * @return
     */
    public SerialPortUtils read(AbstractReadCallback callback) {
        try {
            this.callback = callback;
            reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream(), "GBK"));
            is = new BufferedInputStream(serialPort.getInputStream());
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            /*
             * 如果需要发送命令，那么就要先写入才有数据输出
             */
            if (!StringUtils.isBlank(prop.getProp(Prop.KEY_COMMAND))) {
                write();
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        write();
                    }
                }, 0, 1000);
            }
            return this;
        } catch (UnsupportedEncodingException | TooManyListenersException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 写入命令，命令在配置文件中
     */
    public void write() {
        try {
            writer = new PrintWriter(new OutputStreamWriter(serialPort.getOutputStream(), "UTF-8"), true);
            writer.println(prop.getProp(Prop.KEY_COMMAND));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                res.error("通信中断，请确认连接完好，并重启软件,软件将自动退出!");
                System.exit(0);
                break;
            case SerialPortEvent.OE:
                res.error("溢位错误,请检查地磅参数，并重启软件,软件将自动退出!");
                System.exit(0);
                break;
            case SerialPortEvent.FE:
                res.error("帧错误,请检查地磅参数，并重启软件,软件将自动退出!");
                System.exit(0);
                break;
            case SerialPortEvent.PE:
                res.error("奇偶校验错误,请检查地磅参数，并重启软件,软件将自动退出!");
                System.exit(0);
                break;
            case SerialPortEvent.CD:
                break;
            case SerialPortEvent.CTS:
                break;
            case SerialPortEvent.DSR:
                break;
            case SerialPortEvent.RI:
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                synchronized (reader) {
                    //数据到达，回调
                    callback.call(reader, is);
                }
            default:
                break;
        }
    }

    public static void main(String[] args) {
        AbstractReadCallback callback = new LanJian_OCS_SZ_2_Callback();
        new SerialPortUtils().open().read(callback);
        while (true) {
            System.out.println(callback.getResult());
        }
    }
}
