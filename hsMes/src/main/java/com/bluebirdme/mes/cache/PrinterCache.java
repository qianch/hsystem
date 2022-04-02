package com.bluebirdme.mes.cache;

import com.bluebirdme.mes.core.spring.SpringContextHolder;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.service.IPrinterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrinterCache {
    private static PrinterCache instance = new PrinterCache();

    public static PrinterCache getInstance() {
        return instance;
    }

    private static final Map<String, Printer> printerMap = new HashMap<String, Printer>();

    public static Map<String, Printer> getPrinterList() {

        if (printerMap.size() > 0) {
            return printerMap;
        }

        //清空缓存数据
        printerMap.clear();

        IPrinterService printerService = SpringContextHolder.getBean(IPrinterService.class);
        List<Printer> listPrinter = printerService.findAll(Printer.class);
        for (Printer entity : listPrinter) {

            printerMap.put(entity.getPrinterName(), entity);
        }

        return printerMap;
    }


}
