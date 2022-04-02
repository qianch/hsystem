/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;



/**
 * 包装任务
 * @author Goofy
 * @Date 2017年12月7日 下午6:38:23
 */
@RestController
@RequestMapping("/packtask")
public class PackTaskController extends BaseController {
	
	@Resource IPackTaskService service;
	
	@Journal(name="保存或者更新包装任务")
	@RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
	public String saveOrUpdate(@RequestBody List<PackTask> tasks){
		
		if(tasks.get(0).getPpdId()!=null){
			ProducePlanDetail ppd=service.findById(ProducePlanDetail.class,tasks.get(0).getPpdId());
			ProducePlan pp=service.findById(ProducePlan.class, ppd.getProducePlanId());
			if(pp.getAuditState()>0){
				return ajaxError("当前生产任务单状态无法更改包装任务");
			}
		}else{
			SalesOrderDetail sod=service.findById(SalesOrderDetail.class, tasks.get(0).getSodId());
			SalesOrder so=service.findById(SalesOrder.class, sod.getSalesOrderId());
			
			if(so.getAuditState()>0){
				return ajaxError("当前订单无法更改包装任务");
			}
		}
		
		service.saveOrUpdate(tasks);
		return ajaxSuccess();
	}
	
	@Journal(name="查询订单包装任务")
	@RequestMapping("/order/{id}")
	public String orderTask(@PathVariable("id")Long id){
		return _task("sodId",id);
	}
	
	@Journal(name="查询生产任务单包装任务")
	@RequestMapping("/produce/{id}")
	public String produceTask(@PathVariable("id")Long id){
		return GsonTools.toJson(service.findProduceTask(id));
	}
	
	@Journal(name="查询产品的包装方式")
	@RequestMapping("/product/{id}")
	public String productTask(@PathVariable("id")Long productId){
		return GsonTools.toJson(service.findProductTask(productId));
	}
	
	/**
	 * 获取订单或者生产任务单的包装任务
	 * @param column
	 * @param id
	 * @return
	 */
	public String _task(String column,Long id){
		Map<String, Object> param=new HashMap<String,Object>();
		param.put(column, id);
		return GsonTools.toJson(service.findListByMap(PackTask.class,param));
	}
}
