/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单Controller
 * 
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */
@Controller
@RequestMapping("/salesOrder")
@Journal(name = "销售订单")
public class SalesOrderMirrorController extends BaseController {
@Resource
ISalesOrderService salesOrderService;

	@Journal(name = "根据订单号查询镜像bom数据")
	@RequestMapping(value = "salesOrderMirror", method = RequestMethod.GET)
	public String salesOrderMirror() {
		return "sales/salesOrderMirror";
	}

	@ResponseBody
	@Journal(name = "获取产品树状图列表信息")
	@RequestMapping("listsalesOrderDetail")
	public String listsalesOrderDetail(String status, String id, String vId, String data, Integer isNeedTop,String productIsTc) throws Exception {
		if (data == null) {
			data = "";
		}
		if (status != null) {
			if (status.indexOf(",") > 0) {
				String[] st = status.split(",");
				for (int i = 0; i < st.length; i++) {
					status = st[st.length - 1];
				}
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();

		JSONArray jarray = new JSONArray();
		String result = "";
		if (status == null) {
			JSONObject json = new JSONObject();
			json.put("text", "镜像bom数据");
			json.put("state", "closed");
			JSONObject j = new JSONObject();
			json.put("attributes", j.put("status", "0").put("vId", "0"));
			jarray.put(json);
			result = jarray.toString();
		} else if (status.equals("0") && data != "") {
			List<Map<String, Object>> list = salesOrderService.findSalesOrder(data);
			if (list.size() > 0) {
				JSONObject json = new JSONObject();
				json.put("text", "镜像bom数据");
				json.put("state", "closed");
				JSONObject j = new JSONObject();
				json.put("attributes", j.put("status", "0").put("vId", "0"));
				json.put("children", salesOrderService.findSalesOrder(data));
				jarray.put(json);
				result = jarray.toString();
			} else {
				JSONObject json = new JSONObject();
				json.put("text", "镜像bom数据");
				json.put("state", "closed");
				jarray.put(json);
				result = jarray.toString();
			}
		} else if (status.equals("1")) {
			SalesOrder so = salesOrderService.findById(SalesOrder.class,Long.parseLong(id));
			if(null != so){
				result = GsonTools.toJson(salesOrderService.findD(so.getId().toString()));
			}
		} else if (status.equals("2")) {
				result = GsonTools.toJson(salesOrderService.findBom(id));
		} else if (status.equals("3")) {
			result = GsonTools.toJson(salesOrderService.findV(id,vId));
		}  else if (status.equals("4")&& productIsTc.equals("1")) {
			result = GsonTools.toJson(salesOrderService.findP(id,vId));
		}
		return result;

	}
}
