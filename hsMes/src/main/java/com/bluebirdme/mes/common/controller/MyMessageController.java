/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2015版权所有
 */
package com.bluebirdme.mes.common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.common.service.impl.MyMessageType;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.platform.controller.MessageController;
import com.bluebirdme.mes.platform.entity.Message;
import com.bluebirdme.mes.platform.entity.MessageStatus;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.platform.entity.Subscription;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IMessageService;
import com.bluebirdme.mes.platform.service.IUserService;
import com.google.gson.GsonBuilder;

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
	@Resource
	IUserService userService;
	// @Resource PlatformWebSocketHandler handler;

	String dateFormat = "yyyy-MM-dd HH:mm:ss";

	@NoAuth
	@Journal(name = "获取用户未订阅的内容")
	@ResponseBody
	@RequestMapping(value = "getunUserMessageType", method = RequestMethod.POST)
	public String getunUserMessageType() throws Exception {
		Long userId = Long.parseLong(session.getAttribute(Constant.CURRENT_USER_ID).toString());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		List<Subscription> subList = msgService.findListByMap(Subscription.class, map);
		// if (subList.size() == 0) {
		// return ajaxError("没有订阅的内容，请先订阅");
		// }
		String[] mtype = msgService.getAllType(new MyMessageType() );
		List<HashMap<String, String>> li = new ArrayList<HashMap<String, String>>();
		for (String messageType : mtype) {
			HashMap<String, String> returnMap = new HashMap<String, String>();
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
