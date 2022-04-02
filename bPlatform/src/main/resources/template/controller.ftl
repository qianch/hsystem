/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package ${package};

import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bluebirdme.mes.core.annotation.Journal;




import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.core.constant.Constant;
import ${package2};
import ${package3}.I${entity}Service;
import com.google.gson.GsonBuilder;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

/**
 * ${table}Controller
 * @author ${author}
 * @Date ${date}
 */
@Controller
@RequestMapping("/${entity2}")
@Journal(name="${table}")
public class ${entity}Controller extends BaseController {

	// ${table}页面
	final String index = "${module}/${entity2}";
	final String addOrEdit="${module}/${entity2}AddOrEdit";

	@Resource I${entity}Service ${entity2}Service;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取${table}列表信息")
	@RequestMapping("list")
	public String get${entity}(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(${entity2}Service.findPageInfo(filter, page));
	}
	

	@Journal(name="添加${table}页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(${entity} ${entity2}){
		return new ModelAndView(addOrEdit,model.addAttribute("${entity2}", ${entity2}));
	}
	
	@ResponseBody
	@Journal(name="保存${table}",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(${entity} ${entity2}) throws Exception{
		${entity2}Service.save(${entity2});
		return GsonTools.toJson(${entity2});
	}
	
	@Journal(name="编辑${table}页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(${entity} ${entity2}){
		${entity2}=${entity2}Service.findById(${entity}.class, ${entity2}.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("${entity2}", ${entity2}));
	}
	
	@ResponseBody
	@Journal(name="编辑${table}",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(${entity} ${entity2}) throws Exception{
		${entity2}Service.update(${entity2});
		return GsonTools.toJson(${entity2});
	}

	@ResponseBody
	@Journal(name="删除${table}",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		${entity2}Service.delete(${entity}.class,ids);
		return Constant.AJAX_SUCCESS;
	}

}