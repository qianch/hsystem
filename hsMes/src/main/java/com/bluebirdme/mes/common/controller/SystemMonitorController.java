/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.common.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.utils.DiskDriver;
import com.bluebirdme.mes.utils.SystemInfo;

/**
 * 
 * @author Goofy
 * @Date 2018年4月10日 下午2:01:36
 */
@Controller
@RequestMapping("/sys")
public class SystemMonitorController extends BaseController {

	@NoLogin
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView status() throws Exception {
		String dir= PathUtils.getDrive();
		SystemInfo.getInstance().log(dir);
		String content=org.apache.commons.io.FileUtils.readFileToString(new File(dir+"系统状态.txt"),"UTF-8");
		return new ModelAndView("sys").addObject("content", content.replace("\n", "<br>"));
	}

}
