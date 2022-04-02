package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 通过订单Id查询订单完成进度，如果全部完成删除该订单关联的镜像数据
 *
 * @author zhanwenhu
 * @Date 2020年06月04日 下午19:38:32
 */
@Component
public class SalesOrdersBOMMirrorTask {
    private static Logger log = LoggerFactory.getLogger(SalesOrdersBOMMirrorTask.class);
    @Resource
    ISalesOrderService salesOrderService;

    // @Scheduled(cron = "0 0 0 1 * ?")
// @Scheduled(cron ="0 */1 * * * ?")
    public synchronized void deleteOrderMirror() {
        try {
            List<Map<String, Object>> orderList = salesOrderService.findOrder();
            for (Map<String, Object> fpm : orderList) {
                List<Map<String, Object>> list = salesOrderService.findDetailByCodes(fpm.get("SALESORDERCODE").toString());
                Map<String, Long> bomPartCount = null;
                List<Map<String, Object>> orderPartCounts = null;
                Map<String, Long> orderPartCount = null;
                int suitCount = Integer.MAX_VALUE;
                BigDecimal b1;
                BigDecimal b2;
                Long bomCount;
                Long producedCount;
                int tcOrderCount = 0;
                int ftcOrderCount = 0;
                int productCount = 0;
                Iterator<Map.Entry<String, Long>> it;
                Map.Entry<String, Long> ent;
                for (Map<String, Object> sod : list) {
                    String str = sod.get("PRODUCTCOUNT").toString().substring(0, sod.get("PRODUCTCOUNT").toString().indexOf("."));
                    String str1 = sod.get("RC").toString();
                    productCount += Integer.parseInt(str);
                    if (MapUtils.getAsInt(sod, "PRODUCTISTC") == 1) {
                        //BOM部件和数量
                        bomPartCount = getPartCount(MapUtils.getAsLong(sod, "ID"));
                        //订单部件数量
                        orderPartCounts = salesOrderService.getOrderPartCount(MapUtils.getAsLong(sod, "ID"));
                        //订单部件数量MAP
                        orderPartCount = new HashMap<String, Long>();
                        for (Map<String, Object> m : orderPartCounts) {
                            orderPartCount.put(MapUtils.getAsString(m, "PARTNAME"), MapUtils.getAsLong(m, "COUNT"));
                        }
                        it = bomPartCount.entrySet().iterator();
                        while (it.hasNext()) {
                            ent = it.next();
                            bomCount = (Long) ent.getValue();
                            producedCount = orderPartCount.get(ent.getKey());
                            if (producedCount == null || producedCount == 0L) {
                                suitCount = 0;
                                break;
                            } else {
                                b1 = new BigDecimal(bomCount);
                                b2 = new BigDecimal(producedCount);
                                if (b2.divide(b1, 0, BigDecimal.ROUND_DOWN).intValue() < suitCount) {
                                    suitCount = b2.divide(b1, 0, BigDecimal.ROUND_DOWN).intValue();
                                }
                            }
                        }
                        if (suitCount < Integer.parseInt(str)) continue;
                        tcOrderCount += suitCount;
                    } else {
                        if (Integer.parseInt(str) < productCount) continue;
                        ftcOrderCount += Integer.parseInt(str1);
                    }
                }
//    if (tcOrderCount > 0 && (tcOrderCount >= productCount)) {
//     salesOrderService.delete(FinishedProductMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(TcBomVersionPartsMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(TcBomVersionPartsFinishedWeightEmbryoClothMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(TcBomVersionMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(TcBomVersionPartsDetailMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(TcBomMirror.class, fpm.get("salesOrderId"));
//    }else if(ftcOrderCount > 0 && (ftcOrderCount >= productCount)){
//     salesOrderService.delete(FinishedProductMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(FtcBomDetailMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(FtcBomVersionMirror.class, fpm.get("salesOrderId"));
//     salesOrderService.delete(FtcBomMirror.class, fpm.get("salesOrderId"));
//    }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public Map<String, Long> getPartCount(Long salesOrderDetailId) {

        Map<String, Object> param = new HashMap<String, Object>();
        List<SalesOrderDetailPartsCount> parts = null;
        param.clear();
        param.put("salesOrderDetailId", salesOrderDetailId);

        parts = salesOrderService.findListByMap(SalesOrderDetailPartsCount.class, param);

        Map<String, Long> ret = new HashMap<String, Long>();

        for (SalesOrderDetailPartsCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }

        return ret;
    }
}
