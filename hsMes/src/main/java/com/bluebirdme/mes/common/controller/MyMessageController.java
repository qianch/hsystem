/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2015版权所有
 */
package com.bluebirdme.mes.common.controller;

import com.bluebirdme.mes.common.service.impl.MyMessageType;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.platform.controller.MessageController;
import com.bluebirdme.mes.platform.entity.Subscription;
import com.bluebirdme.mes.platform.service.IMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息处理器
 *
 * @author Goofy
 * @Date 2015年6月11日 下午3:06:51
 */
@Controller
@RequestMapping("/msg")
public class MyMessageController extends MessageController {
    @Resource
    IMessageService msgService;

    @NoAuth
    @Journal(name = "获取用户未订阅的内容")
    @ResponseBody
    @RequestMapping(value = "getunUserMessageType", method = RequestMethod.POST)
    public String getunUserMessageType() {
        Long userId = Long.parseLong(session.getAttribute(Constant.CURRENT_USER_ID).toString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<Subscription> subList = msgService.findListByMap(Subscription.class, map);
        String[] mtype = msgService.getAllType(new MyMessageType());
        List<HashMap<String, String>> li = new ArrayList<>();
        for (String messageType : mtype) {
            HashMap<String, String> returnMap = new HashMap<>();
            boolean isContinue = false;
            for (Subscription sub : subList) {
                if (sub.getMessageType().equals(messageType)) {
                    isContinue = true;
                }
            }
            if (isContinue) {
                continue;
            }
            returnMap.put("ID", messageType);
            returnMap.put("VALUE", messageType);
            li.add(returnMap);
        }
        return GsonTools.toJson(li);
    }
}
