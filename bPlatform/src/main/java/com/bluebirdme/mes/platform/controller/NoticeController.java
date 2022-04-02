package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Notice;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.INoticeService;
import com.bluebirdme.mes.platform.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping({"/notice"})
public class NoticeController extends BaseController {
    @Resource
    INoticeService noticeService;
    @Resource
    IUserService userService;

    public NoticeController() {
    }

    @NoAuth
    @Journal(name = "访问系统公告模块")
    @RequestMapping
    public String index() {
        return "platform/notice";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取维系统公告数据")
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(this.noticeService.findPageInfo(filter, page));
    }

    @Journal(name = "添加系统公告")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Long userId) {
        User user = new User();
        if (userId == -1L) {
            user.setUserName("administrator");
        } else {
            user = this.userService.findById(User.class, userId);
        }

        return new ModelAndView("platform/noticeAddOrEdit", this.model.addAttribute("user", user));
    }

    @ResponseBody
    @Journal(name = "保存公告", logType = LogType.DB)
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    public String add(Notice notice) throws InterruptedException {
        this.noticeService.save(notice);
        return GsonTools.toJson(notice);
    }

    @Journal(name = "编辑系统公告")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Notice notice) {
        User user = new User();
        if (notice.getUserId() == -1L) {
            user.setUserName("administrator");
        } else {
            user = userService.findById(User.class, notice.getUserId());
        }

        notice = this.noticeService.findById(Notice.class, notice.getId());
        return new ModelAndView("platform/noticeAddOrEdit",
                model.addAttribute("user", user)
                        .addAttribute("notice", notice));
    }

    @ResponseBody
    @Journal(name = "编辑公告", logType = LogType.DB)
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Notice notice) {
        notice.setInputTime(new Date());
        noticeService.update(new Object[]{notice});
        return GsonTools.toJson(notice);
    }

    @ResponseBody
    @Journal(name = "删除系统", logType = LogType.DB)
    @RequestMapping(value = {"delete"}, method = {RequestMethod.POST})
    public String delete(String ids) {
        noticeService.delete(ids);
        return "{}";
    }

    @NoAuth
    @Journal(name = "打开系统公告")
    @RequestMapping(value = {"openview"}, method = {RequestMethod.GET})
    public ModelAndView read(Notice notice) {
        notice = noticeService.findById(Notice.class, notice.getId());
        User user = new User();
        if (notice.getUserId() == -1L) {
            user.setUserName("administrator");
        } else {
            user = userService.findById(User.class, notice.getUserId());
        }

        return new ModelAndView("platform/noticeView",
                model.addAttribute("notice", notice)
                        .addAttribute("user", user));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查询现在的系统公告数据")
    @RequestMapping({"findnotice"})
    public String findNotice() {
        return GsonTools.toJson(noticeService.findNotice());
    }
}
