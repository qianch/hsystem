package com.bluebirdme.mes.utils;

import com.bluebirdme.mes.cache.PrinterCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.RandomUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 打印工具类
 *
 * @author Goofy
 * @Date 2017年8月9日 下午9:28:43
 */
public class PrintUtils {
    private static Logger logger = LoggerFactory.getLogger(PrintUtils.class);

    /**
     * 同步方法，+文件锁锁，防止文件同时写入
     *
     * @param content
     * @param btw
     * @param printer
     * @throws Exception
     */
    public static synchronized void print(List<String> content, String btw, String printer) throws Exception {
        RandomAccessFile out = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            String file = PathUtils.getDrive() + "BtwFiles" + File.separator + "Data" + File.separator + RandomUtils.uuid() + ".txt";
            out = new RandomAccessFile(file, "rwd");
            channel = out.getChannel();
            //文件加锁，独占
            lock = channel.lock();
            //清空文件
            channel.truncate(0);
            //写入内容
            for (String str : content) {
                out.write(str.getBytes("UTF-8"));
                out.write("\n".getBytes("UTF-8"));
            }
            lock.release();
            channel.close();
            out.close();
            int i = printDb(printer, btw, file, 1);
            if (i == 20) {
                throw new Exception("找不到打印机");
            }
            if (i == 21) {
                throw new Exception("打印失败");
            }
        } catch (Exception e) {
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException e1) {
                    logger.error(e1.getLocalizedMessage(), e1);
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e1) {
                    logger.error(e1.getLocalizedMessage(), e1);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    logger.error(e1.getLocalizedMessage(), e1);
                }
            }
            if (e instanceof OverlappingFileLockException) {
                throw new Exception("打印机忙，请稍后");
            }
            throw e;
        }
    }

    /**
     * 10:成功，20:找不到打印机，21:打印失败
     *
     * @param printer
     * @param btwFileName
     * @param textFileName
     * @return
     */
    public static int printDb(String printer,String btwFileName,String textFileName,int count)
    {
        return print("db",printer,btwFileName,textFileName,count);
    }

    public static int printYu(String printer, String btwFileName, String textContent, int count) {

        return print("yu", printer, btwFileName, textContent, count);
    }

	public static int print(String textDBName ,String printer,String btwFileName,String textContent,int count) {
        try {
            int port= PrinterCache.getInstance().getPrinterList().get(printer).getPort()==null?7777:PrinterCache.getInstance().getPrinterList().get(printer).getPort();
            Socket sk = new Socket(InetAddress.getByName("127.0.0.1"), port);
            InputStream is = sk.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            OutputStream writer = sk.getOutputStream();

            String cmd = textDBName + "," + printer + "," + btwFileName + "," + count + "," + textContent;

            writer.write(cmd.getBytes(Charset.forName("UTF-8")));

            String data = reader.readLine();
            writer.close();
            reader.close();
            isr.close();
            is.close();
            sk.close();
            if (data.startsWith("10")) {
                return 10;
            } else if (data.startsWith("20")) {
                return 20;
            } else {
                return 21;
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return 21;
        }
    }

}
