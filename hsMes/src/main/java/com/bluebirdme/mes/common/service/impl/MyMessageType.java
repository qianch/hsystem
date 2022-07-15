package com.bluebirdme.mes.common.service.impl;

import com.bluebirdme.mes.platform.entity.MessageType;

public class MyMessageType extends MessageType {
    public static final String PRODUCT_QUALITY = "质量审核";

    @Override
    public String[] getAllType() {
        return new String[]{ORDER_CLOSE, ORDER_AUDIT, ORDER_FINISH, ORDER_OUTDATE, BOM_AUDIT, PRODUCT_AUDIT, PRODUCE_PLAN_AUDIT, PRODUCE_PLAN_CLOSE, TURGBAG_PLAN_AUDIT, CUTDAILY_PLAN_AUDIT, CUTDAILY_PLAN_CLOSE, WEAVEDAILY_PLAN_AUDIT, WEAVEDAILY_PLAN_CLOSE, DELIVERY_PLAN_AUDIT,
                DELIVERY_PLAN_CLOSE, DELIVERY_PLAN_OUTDATE};
    }
}
