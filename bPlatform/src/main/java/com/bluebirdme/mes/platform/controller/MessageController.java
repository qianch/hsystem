package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Message;
import com.bluebirdme.mes.platform.entity.Subscription;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IMessageService;
import com.bluebirdme.mes.platform.service.IUserService;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class MessageController extends BaseController {
    @Resource
    IMessageService msgService;
    @Resource
    IUserService userService;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public MessageController() {
    }

    @Journal(name = "访问消息模块")
    @RequestMapping
    public String index() {
        return "platform/message";
    }

    @NoAuth
    @Journal(name = "查询全部消息列表")
    @ResponseBody
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        if (!filter.getFilter().containsKey("messageType")) {
            filter.set("messageType", "in:" + getUserMTypes(userId));
        }

        filter.set("toUser", session.getAttribute("userId").toString());
        return (new GsonBuilder()).setDateFormat(DATE_FORMAT).create().toJson(msgService.findPageInfo(filter, page));
    }

    @NoAuth
    @Journal(name = "查询未读消息列表")
    @ResponseBody
    @RequestMapping({"unread"})
    public String unread(Filter filter, Page page) throws Exception {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        if (!filter.getFilter().containsKey("messageType")) {
            filter.set("messageType", "in:" + getUserMTypes(userId));
        }

        filter.set("toUser", session.getAttribute("userId").toString());
        return (new GsonBuilder()).setDateFormat(DATE_FORMAT).create().toJson(msgService.findUnreadMessage(filter, page));
    }

    @NoAuth
    @Journal(name = "查询已读消息列表")
    @ResponseBody
    @RequestMapping({"readed"})
    public String readed(Filter filter, Page page) throws Exception {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        if (!filter.getFilter().containsKey("messageType")) {
            filter.set("messageType", "in:" + getUserMTypes(userId));
        }

        filter.set("toUser", session.getAttribute("userId").toString());
        return (new GsonBuilder()).setDateFormat(DATE_FORMAT).create().toJson(msgService.findReadedMessage(filter, page));
    }

    @NoAuth
    @Journal(name = "查看消息内容")
    @RequestMapping({"view"})
    public ModelAndView view(Message msg) {
        msg = msgService.findById(Message.class, msg.getId());
        User u = userService.findById(User.class, msg.getFromUser());
        return new ModelAndView("platform/messageView",
                model.addAttribute("msg", msg)
                        .addAttribute("user", u));
    }

    @Journal(name = "进入发送消息页面")
    @RequestMapping(value = {"send"}, method = {RequestMethod.GET})
    public ModelAndView _send(Message msg) {
        msg.setAttachment(UUID.randomUUID().toString());
        msg.setFromUser((Long) session.getAttribute("userId"));
        msg.setCreateTime(new Date());
        msg.setUuid(UUID.randomUUID().toString());
        return new ModelAndView("platform/messageAddOrEdit", model.addAttribute("msg", msg));
    }

    @ResponseBody
    @RequestMapping(value = {"send"}, method = {RequestMethod.POST})
    public String send(Message msg, @RequestParam(required = true) Integer toType, @RequestBody List<Map<Integer, Long>> sendto) throws IOException {
        if (toType == -1) {
            this.broadcast(msg);
        } else {
            this.sendto(msg, sendto);
        }
        return "{}";
    }

    @Journal(name = "发送系统广播")
    public void broadcast(Message message) {
        message.setToUser(-1L);
        msgService.save(message);
    }

    @Journal(name = "发送到指定的人")
    public void sendto(Message message, @RequestBody List<Map<Integer, Long>> sendto) {
        message.setCreateTime(new Date());
        this.msgService.save(message);
    }

    @Journal(name = "进入设置订阅页面")
    @RequestMapping(value = {"subMessage"}, method = {RequestMethod.GET})
    public ModelAndView subMessage() {
        return new ModelAndView("platform/messageAddOrEdit",
                this.model.addAttribute("addOrDelete", "subMessage")
                        .addAttribute("mTypeUrl", "getunUserMessageType"));
    }

    @Journal(name = "进入取消订阅页面")
    @RequestMapping(value = {"unsubMessage"}, method = {RequestMethod.GET})
    public ModelAndView unsubMessage() {
        return new ModelAndView("platform/messageAddOrEdit",
                model.addAttribute("addOrDelete", "unsubMessage")
                        .addAttribute("mTypeUrl", "getUserMessageType"));
    }

    @NoAuth
    @Journal(name = "设置订阅的消息类型")
    @ResponseBody
    @RequestMapping(value = {"subMessage"}, method = {RequestMethod.POST})
    public String subMessage(String messageTypes) throws Exception {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        msgService.subMessage(messageTypes, userId);
        return ajaxSuccess();
    }

    @NoAuth
    @Journal(name = "取消订阅的消息类型")
    @ResponseBody
    @RequestMapping(value = {"unsubMessage"}, method = {RequestMethod.POST})
    public String unsubMessage(String messageTypes) throws Exception {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        msgService.unsubMessage(messageTypes, userId);
        return ajaxSuccess();
    }

    @NoAuth
    @Journal(name = "设置已读的消息")
    @ResponseBody
    @RequestMapping(value = {"readMessage"}, method = {RequestMethod.POST})
    public String readMessage(String ids) {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        msgService.readMessage(ids, userId);
        return ajaxSuccess();
    }

    @NoAuth
    @Journal(name = "删除消息")
    @ResponseBody
    @RequestMapping(value = {"deleteMessage"}, method = {RequestMethod.POST})
    public String deleteMessage(String messageIds) {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        try {
            msgService.deleteMessage(messageIds, userId);
        } catch (Exception var4) {
            return ajaxError("删除失败");
        }
        return ajaxSuccess();
    }

    @NoAuth
    @Journal(name = "获取用户订阅的内容")
    @ResponseBody
    @RequestMapping(value = {"getUserMessageType"}, method = {RequestMethod.POST})
    public String getUserMessageType() {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        HashMap<String, Object> map = new HashMap();
        map.put("userId", userId);
        List<Subscription> subList = msgService.findListByMap(Subscription.class, map);
        if (subList.size() == 0) {
            return ajaxError("没有订阅的内容，请先订阅");
        } else {
            List<HashMap<String, String>> li = new ArrayList();
            Iterator iterator = subList.iterator();
            while (iterator.hasNext()) {
                Subscription sub = (Subscription) iterator.next();
                HashMap<String, String> returnMap = new HashMap();
                returnMap.put("ID", sub.getMessageType());
                returnMap.put("VALUE", sub.getMessageType());
                li.add(returnMap);
            }
            return GsonTools.toJson(li);
        }
    }

    private String getUserMTypes(Long userId) {
        HashMap<String, Object> map = new HashMap();
        map.put("userId", userId);
        List<Subscription> subList = msgService.findListByMap(Subscription.class, map);
        String message = "";
        Iterator iterator = subList.iterator();
        while (iterator.hasNext()) {
            Subscription sub = (Subscription) iterator.next();
            if (message.length() == 0) {
                message = message + sub.getMessageType();
            } else {
                message = message + "," + sub.getMessageType();
            }
        }
        return message;
    }
}
