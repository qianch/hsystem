package com.bluebirdme.mes.core.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.*;

import java.io.IOException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class StartException {
    private static Logger log = LoggerFactory.getLogger(StartException.class);

    public StartException() {
    }

    public static void show(Exception exception) {
        try {
            if (SystemUtils.isWindows()) {
                log.error("系统初始化异常,即将将关闭，请联系维护人员...", exception);
                String logFile = PathUtils.getDrive() + DateUtils.getCurrentDate("yyyy年MM月dd日HH时mm分ss秒") + ".txt";
                FileUtils.writeToFile("错误原因:" + exception.getMessage(), logFile, true, true);
                StackTraceElement[] elements = exception.getStackTrace();
                int length = elements.length;

                for (int i = 0; i < length; ++i) {
                    StackTraceElement element = elements[i];
                    FileUtils.writeToFile((element.getClassName().startsWith("com.bluebirdme.mes") ? ">>>>>>>>>>>>>" : "") + element.getClassName() + "->" + element.getMethodName() + "()->" + element.getLineNumber() + "行", logFile, true, true);
                }
                RunTimeUtils.run("notepad " + logFile);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        System.exit(1);
    }
}
