package com.bluebirdme.mes.printer.entity;

import com.bluebirdme.mes.core.annotation.Desc;

@Desc("打印信息记录")
public class BarCodePrintRecord {

    public BarCodePrintRecord(String KEY, String VALUE)
    {
        this.KEY=KEY;
        this.VALUE=VALUE;
    }

    @Desc("打印属性")
    private String KEY;

    @Desc("打印内容")
    private String VALUE;

    public String getKey() {
        return KEY;
    }
    public void setKey(String key) {
        this.KEY = KEY;
    }

    public String getValue() {
        return VALUE;
    }
    public void setValue(String value) {
        this.VALUE = VALUE;
    }
}
