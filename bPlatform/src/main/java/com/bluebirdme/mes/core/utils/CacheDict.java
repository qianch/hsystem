package com.bluebirdme.mes.core.utils;

import com.bluebirdme.mes.core.spring.SpringContextHolder;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.service.IDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CacheDict {
    private static Logger log = LoggerFactory.getLogger(CacheDict.class);

    /**
     * 类型与字典值
     */
    private static final Map<String, Map<String, String>> dictMap = new HashMap<String, Map<String, String>>();

    public static Map<String, Map<String, String>> getInstance() throws SQLTemplateException {

        if (dictMap.size() > 0) {
            return dictMap;
        }
        //清空缓存数据
        dictMap.clear();

        IDictService dictService = SpringContextHolder.getBean(IDictService.class);
        List<Map<String, String>> dictList = dictService.queryDictAll();
        Set<String> codetypeSet = new TreeSet<String>();
        for (Map<String, String> map : dictList) {
            codetypeSet.add(map.get("ROOTCODE"));
        }

        for (String codetype : codetypeSet) {
            Map<String, String> codeValMap = new HashMap<String, String>();
            for (Map<String, String> map : dictList) {
                if (codetype.equals(map.get("ROOTCODE"))) {
                    codeValMap.put(map.get("CODE"), map.get("name_zh_CN".toUpperCase()));
                }
            }
            dictMap.put(codetype, codeValMap);
        }
        log.info("字典数据大小：  " + dictMap.size());
        return dictMap;
    }
}
