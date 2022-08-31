package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
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
    private static final Logger log = LoggerFactory.getLogger(SalesOrdersBOMMirrorTask.class);
    @Resource
    ISalesOrderService salesOrderService;

    public Map<String, Long> getPartCount(Long salesOrderDetailId) {
        Map<String, Object> param = new HashMap<>();
        List<SalesOrderDetailPartsCount> parts;
        param.put("salesOrderDetailId", salesOrderDetailId);
        parts = salesOrderService.findListByMap(SalesOrderDetailPartsCount.class, param);
        Map<String, Long> ret = new HashMap<>();
        for (SalesOrderDetailPartsCount part : parts) {
            ret.put(part.getPartName(), part.getPartBomCount().longValue());
        }
        return ret;
    }
}
