package com.bluebirdme.mes.core.excel;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class ExcelImportMessage {
    List<String> rs = new ArrayList();

    public ExcelImportMessage() {
    }

    public ExcelImportMessage addMessage(int rowIndex, int columnIndex, String message) {
        rs.add("第 " + rowIndex + " 行 第 " + columnIndex + " 列" + message);
        return this;
    }

    public ExcelImportMessage addMessage(String message) {
        rs.add(message);
        return this;
    }

    public String getMessage() {
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = this.rs.iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            buffer.append(s + "<br>");
        }
        return buffer.toString();
    }

    public boolean hasError() {
        return this.rs.size() != 0;
    }
}
