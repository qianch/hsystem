package com.bluebirdme.mes.core.dev;

import com.bluebirdme.mes.core.annotation.Desc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class DevHelper {
    private static final Logger logger = LoggerFactory.getLogger(DevHelper.class);

    public DevHelper() {
    }

    public static <T> void genCSD(Class<T> entity, String author) throws Exception {
        Desc desc = entity.getAnnotation(Desc.class);
        if (desc == null) {
            throw new Exception("在类" + entity.getName() + "上找不到@Desc注解");
        } else {
            String table = desc.value();
            String clazz = entity.getName();
            String[] array = clazz.split("\\.");
            StringBuilder basePackage = new StringBuilder();
            for (int i = 0; i <= array.length - 3; ++i) {
                basePackage.append(i == 0 ? "" : ".").append(array[i]);
            }
            String controllerPackage = basePackage + ".controller";
            String entityName = array[array.length - 1];
            String fileBasePath = PathUtils.getClassDrivePath(entity).replace("entity/", "").replace("target/test-classes/", "src/main/java").replace("target/classes", "src/main/java");
            String controllerPath = fileBasePath + "controller/";
            String module = PathUtils.getClassDrivePath(entity).replace("/entity/", "");
            module = module.substring(module.lastIndexOf("/") + 1);
            Map<String, String> controllerData = new HashMap<>();
            controllerData.put("package", controllerPackage);
            controllerData.put("package2", entity.getName());
            controllerData.put("package3", basePackage + ".service");
            controllerData.put("entity", entityName);
            controllerData.put("entity2", StringUtils.firstCharToLowerCase(entityName));
            controllerData.put("date", (new Date()).toString());
            controllerData.put("author", author);
            controllerData.put("table", table);
            controllerData.put("module", module);
            (new File(controllerPath)).mkdirs();
            Utils.generateToFile("controller.ftl", controllerPath + entityName + "Controller.java", controllerData);
            logger.debug("[成功]" + controllerPath + entityName + "Controller.java");

            Map<String, String> serviceData = new HashMap<>();
            String servicePackage = basePackage + ".service";
            serviceData.put("package", servicePackage);
            serviceData.put("entity", entityName);
            serviceData.put("date", (new Date()).toString());
            serviceData.put("author", author);
            String servicePath = fileBasePath + "service/";
            (new File(servicePath)).mkdirs();
            Utils.generateToFile("service.ftl", servicePath + "I" + entityName + "Service.java", serviceData);
            logger.debug("[成功]" + servicePath + "I" + entityName + "Service.java");

            Map<String, String> serviceImplData = new HashMap<>();
            String serviceImplPath = fileBasePath + "service/impl/";
            String serviceImplPackage = basePackage + ".service.impl";
            serviceImplData.put("package", serviceImplPackage);
            serviceImplData.put("package2", servicePackage);
            serviceImplData.put("package3", basePackage + ".dao");
            serviceImplData.put("date", (new Date()).toString());
            serviceImplData.put("entity", entityName);
            serviceImplData.put("entity2", StringUtils.firstCharToLowerCase(entityName));
            serviceImplData.put("author", author);
            (new File(serviceImplPath)).mkdirs();
            Utils.generateToFile("serviceImpl.ftl", serviceImplPath + entityName + "ServiceImpl.java", serviceImplData);
            logger.debug("[成功]" + serviceImplPath + entityName + "ServiceImpl.java");

            String daoPath = fileBasePath + "dao/";
            Map<String, String> daoData = new HashMap<>();
            String daoPackage = basePackage + ".dao";
            daoData.put("package", daoPackage);
            daoData.put("date", (new Date()).toString());
            daoData.put("entity", entityName);
            daoData.put("author", author);
            (new File(daoPath)).mkdirs();
            Utils.generateToFile("dao.ftl", daoPath + "I" + entityName + "Dao.java", daoData);
            logger.debug("[成功]" + daoPath + "I" + entityName + "Dao.java");

            String daoImplPath = fileBasePath + "dao/impl/";
            Map<String, String> daoImplData = new HashMap<>();
            String daoImplPackage = basePackage + ".dao.impl";
            daoImplData.put("package", daoImplPackage);
            daoImplData.put("package2", daoPackage);
            daoImplData.put("date", (new Date()).toString());
            daoImplData.put("entity", entityName);
            daoImplData.put("author", author);
            daoImplData.put("entity2", StringUtils.firstCharToLowerCase(entityName));
            (new File(daoImplPath)).mkdirs();
            Utils.generateToFile("daoImpl.ftl", daoImplPath + entityName + "DaoImpl.java", daoImplData);
            logger.debug("[成功]" + daoImplPath + entityName + "DaoImpl.java");
        }
    }

    public static <T> void genJsJsp(Class<T> entity, String author) throws Exception {
        Desc desc = entity.getAnnotation(Desc.class);
        if (desc == null) {
            throw new Exception("在类" + entity.getName() + "上找不到@Desc注解");
        } else {
            String table = desc.value();
            Field[] fields = entity.getDeclaredFields();
            List<Map<String, String>> list = new ArrayList<>();
            for (Field f : fields) {
                Map<String, String> map = new HashMap<>(4);
                Desc d = f.getAnnotation(Desc.class);
                Column column = f.getAnnotation(Column.class);
                if (column != null) {
                    map.put("field", f.getName().toUpperCase());
                    map.put("field2", f.getName());
                    if (!column.nullable()) {
                        map.put("required", "true");
                    }
                    map.put("title", d.value());
                    list.add(map);
                }
            }
            Map<String, Object> data = new HashMap<>(5);
            data.put("table", table);
            data.put("entity", Utils.firstCharToLowerCase(entity.getSimpleName()));
            data.put("list", list);
            data.put("author", author);
            data.put("date", (new Date()).toString());
            String module = PathUtils.getClassDrivePath(entity).replace("/entity/", "");
            module = module.substring(module.lastIndexOf("/") + 1);
            String fileBasePath = PathUtils.getClassPath().substring(1).replace("target/classes/", "").replace("target/test-classes/", "") + "src/main/webapp/pages/" + module + "/";
            (new File(fileBasePath)).mkdirs();
            Utils.generateToFile("jsp.ftl", fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + ".jsp", data);
            logger.debug("[成功]" + fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + ".jsp");

            Utils.generateToFile("js.jsp.ftl", fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + ".js.jsp", data);
            logger.debug("[成功]" + fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + ".js.jsp");

            Utils.generateToFile("addOrEdit.jsp.ftl", fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + "AddOrEdit.jsp", data);
            logger.debug("[成功]" + fileBasePath + Utils.firstCharToLowerCase(entity.getSimpleName()) + "AddOrEdit.jsp");
        }
    }

    public static <T> void genSqlXml(Class<T> clazz, String author) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Desc desc = clazz.getAnnotation(Desc.class);
        if (desc == null) {
            throw new Exception("在类" + clazz.getName() + "上找不到@Desc注解");
        } else {
            Map<String, String> map = new HashMap<>(6);
            String Entity = clazz.getSimpleName();
            String entity = StringUtils.firstCharToLowerCase(Entity);
            String time = sdf.format(new Date());
            String name = desc.value();
            Table table = clazz.getAnnotation(Table.class);
            map.put("Entity", Entity);
            map.put("entity", entity);
            map.put("entity_table_name", table.name());
            map.put("time", time);
            map.put("author", author);
            map.put("name", name);
            String xml = PathUtils.getClassPath().substring(1).replace("target/classes/", "").replace("target/test-classes/", "") + "src/main/resources/SQLFiles/" + entity + ".xml";
            Utils.generateToFile("sql.ftl", xml, map);
        }
    }

    public static <T> void genAll(Class<T> entity, String author) throws Exception {
        genCSD(entity, author);
        genJsJsp(entity, author);
        genSqlXml(entity, author);
    }
}
