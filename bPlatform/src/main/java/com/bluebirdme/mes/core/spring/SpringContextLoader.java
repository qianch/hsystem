package com.bluebirdme.mes.core.spring;

import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.properties.SystemProperties;
import com.bluebirdme.mes.core.sql.DBType;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.ZipUtils;
import org.xdemo.superutil.j2se.filewatch.FileActionCallback;
import org.xdemo.superutil.j2se.filewatch.WatchDir;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class SpringContextLoader extends ContextLoaderListener {
    private static final Logger logger = LoggerFactory.getLogger(SpringContextLoader.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            initPlatform();
            File file = new File(PathUtils.getClassPath());
            String root = file.getParentFile().getParent();
            File libFile = new File(file.getParent() + File.separator + "lib");
            if (libFile.isDirectory()) {
                File[] files = libFile.listFiles();
                for (File jarFile : files) {
                    if (jarFile.getName().startsWith("hsmes")) {
                        logger.info("正在解压部署资源...");
                        String tempDir = root + File.separator + "temp";
                        ZipUtils.unZip(jarFile.getAbsolutePath(), tempDir);
                        logger.info("解压完毕，正在部署资源文件...");
                        //SQL 文件
                        String classDir = root + File.separator + "WEB-INF" + File.separator + "classes";
                        String tempSqlDir = tempDir + File.separator + "SQLFiles";
                        String targetSqlDir = classDir + File.separator + "SQLFiles";
                        FileUtils.copyDir(tempSqlDir, targetSqlDir);

                        //template文件
                        String tempTemplateDir = tempDir + File.separator + "template";
                        String targetTemplateDir = classDir + File.separator + "template";
                        FileUtils.copyDir(tempTemplateDir, targetTemplateDir);

                        //BtwFiles文件
                        String tempBtwDir = tempDir + File.separator + "BtwFiles";
                        String targetBtwDir = classDir + File.separator + "BtwFiles";
                        FileUtils.copyDir(tempBtwDir, targetBtwDir);

                        File temp = new File(tempDir);
                        if (temp.exists()) {
                            FileUtils.deleteByDir(temp);
                        }
                    }
                }
            }

            logger.info("资源部署完毕");
            logger.info("系统启动中...");
            Thread.sleep(1000);
            String path = PathUtils.getClassPath() + File.separator + "SQLFiles" + File.separator + new SystemProperties().getAsString("dbType");
            loadSql(path);
            sqlListener(path);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            StartException.show(e);
        }
        super.contextInitialized(event);
    }

    /**
     * 加载SQL语句的模板
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void loadSql(String path) throws Exception {
        List<File> fileList = FileUtils.getFiles(path, new ArrayList(), true);
        File[] files = ListUtils.asArray(fileList, new File[fileList.size()]);
        if (files == null || files.length == 0) {
            throw new SQLTemplateException("找不到SQL的模板文件");
        }
        Map<String, String> sql = new HashMap();
        SAXReader reader = new SAXReader(true);
        Document doc;
        List<Element> eList;
        Element root;
        for (File file : files) {
            logger.info(" [Loading " + file.getName() + " ]");
            doc = reader.read(file);
            root = doc.getRootElement();
            if (root == null) {
                continue;
            }
            eList = root.elements("sql");
            for (Element e : eList) {
                if (sql.containsKey(e.attribute("id").getText())) {
                    throw new SQLTemplateException("重复的SQL ID定义:" + e.attribute("id").getText() + "，文件:" + file.getName());
                }
                sql.put(e.attribute("id").getText(), e.getTextTrim());
            }
        }
        SQL.setSql(sql);
    }

    public void sqlListener(final String path) {
        // 自动重载SQL内容
        final File file = new File(path);
        logger.info("Watching SQL files:" + file.getAbsolutePath());
        new Thread(() -> {
            try {
                new WatchDir(file, true, new FileActionCallback() {
                    @Override
                    public void create(File file1) {
                        try {
                            loadSql(path);
                        } catch (Exception e) {
                            logger.error(e.getLocalizedMessage(), e);
                        }
                    }

                    @Override
                    public void delete(File file1) {
                        try {
                            loadSql(path);
                        } catch (Exception e) {
                            logger.error(e.getLocalizedMessage(), e);
                        }
                    }

                    @Override
                    public void modify(File file1) {
                        try {
                            loadSql(path);
                        } catch (Exception e) {
                            logger.error(e.getLocalizedMessage(), e);
                        }
                    }
                });
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }).start();
    }

    /**
     * 平台初始化需要做的事情
     *
     * @throws IOException
     */
    public void initPlatform() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties");
        Properties properties = new Properties();
        properties.load(is);
        String user_log = properties.getProperty("user_log");
        String exception_log = properties.getProperty("exception_log");
        String debug = properties.getProperty("debug");
        String uploadPath = properties.getProperty("upload_path");
        String db_type = properties.getProperty("dbType");
        String runType = properties.getProperty("runType");
        RuntimeVariable.RUNTYPE = runType == null ? 0 : Integer.parseInt(runType);
        RuntimeVariable.USER_LOG = user_log == null ? false : (user_log.equalsIgnoreCase("false") ? false : true);
        RuntimeVariable.EXCEPTION_LOG = exception_log == null ? false : (exception_log.equalsIgnoreCase("false") ? false : true);
        RuntimeVariable.DEBUG = debug == null ? false : (debug.equalsIgnoreCase("false") ? false : true);
        RuntimeVariable.UPLOAD_PATH = uploadPath;
        RuntimeVariable.DBTYPE = (db_type == null ? DBType.MYSQL : DBType.getType(db_type));

        if (RuntimeVariable.UPLOAD_PATH != null) {
            File file = new File(RuntimeVariable.UPLOAD_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}
