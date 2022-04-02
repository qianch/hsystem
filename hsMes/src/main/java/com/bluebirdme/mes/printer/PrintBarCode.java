package com.bluebirdme.mes.printer;

import com.bluebirdme.mes.core.spring.SpringContextHolder;
import com.bluebirdme.mes.store.service.IBoxBarcodeService;
import com.bluebirdme.mes.store.service.IPartBarcodeService;
import com.bluebirdme.mes.store.service.IRollBarcodeService;
import com.bluebirdme.mes.store.service.ITrayBarCodeService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PrintBarCode {
    private static PrintBarCode instance = new PrintBarCode();
    static IRollBarcodeService rollBarcodeService = SpringContextHolder.getBean(IRollBarcodeService.class);
    static ITrayBarCodeService trayBarcodeService = SpringContextHolder.getBean(ITrayBarCodeService.class);
    static IBoxBarcodeService boxBarcodeService = SpringContextHolder.getBean(IBoxBarcodeService.class);
    static IPartBarcodeService partBarcodeService = SpringContextHolder.getBean(IPartBarcodeService.class);

    public static PrintBarCode getInstance() {
        return instance;
    }

    private static final Map<String, Integer> barcodeCount = new HashMap();

    public static List<String> getBarCodeList(String type, String prefixPrint, int count) {

        type = type.toLowerCase();
        type = type.equals("roll_peibu") ? "roll" : type;

        synchronized (buildLock(type)) {
            List<String> list = new ArrayList<>();
            Calendar c = Calendar.getInstance();
            String year = "000" + (c.get(Calendar.YEAR) - 1999);
            String day = "00" + c.get(Calendar.DAY_OF_YEAR);
            year = year.substring(year.length() - 3);
            day = day.substring(day.length() - 3);
            String dateString = year + day;

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String barcodeDate = sdf.format(now);

            String likeString = "";
            List<Map<String, Object>> listmap=new ArrayList<>();
            switch (type) {
                case "roll":
                    likeString = "R" + prefixPrint + dateString + "%";
                    listmap = rollBarcodeService.findMaxRollBarCodeCount();
                    break;
                case "tray":
                    likeString = "T" + prefixPrint + dateString + "%";
                    listmap = trayBarcodeService.findMaxTrayBarCodeCount();
                    break;
                case "traypart":
                    likeString = "P" + prefixPrint + dateString + "%";
                    listmap = trayBarcodeService.findMaxTrayPartBarCodeCount();
                    break;
                case "box":
                    likeString = "B" + prefixPrint + dateString + "%";
                    listmap = boxBarcodeService.findMaxBoxBarCodeCount();
                    break;
                case "part":
                    likeString = "P" + prefixPrint + dateString + "%";
                    listmap = partBarcodeService.findMaxPartBarCodeCount();
                    break;
                default:
                    return null;
            }

            Integer   barCodeCount = Integer.parseInt(listmap.get(0).get("BARCODECOUNT").toString());

            if (barCodeCount >= (barcodeCount.get(barcodeDate + type) == null ? 0 : Integer.parseInt(barcodeCount.get(barcodeDate + type).toString()))) {
                barcodeCount.put(barcodeDate + type, barCodeCount);
            }

            int dc = barcodeCount.get(barcodeDate + type);
            String dcString;

            for (int a = 0; a < count; a++) {
                dc++;
                if (dc < 10) {
                    dcString = "0000" + dc;
                } else if (dc < 100 && dc >= 10) {
                    dcString = "000" + dc;
                } else if (dc < 1000 && dc >= 100) {
                    dcString = "00" + dc;
                } else if (dc < 10000 && dc >= 1000) {
                    dcString = "0" + dc;
                } else {
                    dcString = "" + dc;
                }
                String barcode = likeString.replaceAll("%", "") + dcString;
                list.add(barcode);
            }
            barcodeCount.put(barcodeDate + type, dc);
            return list;
        }
    }


    private static String buildLock(String str) {
        StringBuilder sb = new StringBuilder(str);
        String lock = sb.toString().intern();
        return lock;
    }
}
