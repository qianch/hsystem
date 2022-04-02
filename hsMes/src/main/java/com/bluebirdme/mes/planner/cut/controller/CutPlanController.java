/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomVersionMirror;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ObjectUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanUsers;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.PlanUsers;
import com.bluebirdme.mes.planner.cut.service.ICutDailyPlanDetailService;
import com.bluebirdme.mes.planner.cut.service.ICutPlanService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.utils.MapUtils;

/**
 * 裁剪计划Controller
 *
 * @author 宋黎明
 * @Date 2016-10-18 13:35:16
 */
@Controller
@RequestMapping("planner/cutPlan")
@Journal(name = "裁剪计划")
public class CutPlanController extends BaseController {
    // 裁剪计划页面
    final String index = "planner/cut/cutPlan";
    final String addOrEdit = "planner/cut/cutPlanAddOrEdit";
    final String addOrEditOrder = "planner/cut/salesOrderAddOrEdit";
    final String selectPage = "planner/cut/product";
    final String copyProduct = "planner/cut/finishProductAddOrEdit";
    @Resource
    ISalesOrderService salesOrderService;
    @Resource
    ICutPlanService cutPlanService;
    @Resource
    ITcBomService tcBomService;
    @Resource
    IBomService bomService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取裁剪计划列表信息")
    @RequestMapping("list")
    public String getCutPlanByPlanCode(Filter filter, String planCode) throws Exception {
        List<Map<String, Object>> findCutPlan = cutPlanService.findCutPlan(planCode);
        return GsonTools.toJson(findCutPlan);
    }

    @Journal(name = "选择胚布页面 ")
    @RequestMapping(method = RequestMethod.GET, value = "selectProductPage")
    public String selectProductPage(String singleSelect, String showCode, Integer isShow, Long finalConsumerId) throws Exception {
        model.addAttribute("singleSelect", singleSelect).addAttribute("showCode", showCode).addAttribute("finalConsumerId", finalConsumerId);
        if (isShow != null) {
            model.addAttribute("isShow", isShow);
        }
        return selectPage;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "根据生产计划明细ID查询人员")
    @RequestMapping("userList")
    public String getCutPlan(String id) throws Exception {
        return GsonTools.toJson(cutPlanService.userListById(id));
    }

    @Journal(name = "编辑裁剪计划页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(String partsId, String cutPlanId, String userIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        CutPlan cutPlan = cutPlanService.findById(CutPlan.class, Long.valueOf(cutPlanId));
        TcBomVersionParts tcVersionParts = cutPlanService.findById(TcBomVersionParts.class, Long.valueOf(partsId));
        String[] ids = userIds.split(",");
        StringBuffer sb = new StringBuffer();
        if (userIds == "") {
            return new ModelAndView(addOrEdit, model.addAttribute("cutPlan", cutPlan).addAttribute("tcVersionParts", tcVersionParts).addAttribute("userName", ""));
        } else {
            for (int i = 0; i < ids.length; i++) {
                map.put("id", Long.valueOf(ids[i]));
                List<User> userList = cutPlanService.findListByMap(User.class, map);
                for (int j = 0; j < userList.size(); j++) {
                    sb.append((i == 0 ? "" : ",") + userList.get(j).getUserName());
                }
            }
            return new ModelAndView(addOrEdit, model.addAttribute("tcVersionParts", tcVersionParts).addAttribute("cutPlan", cutPlan).addAttribute("userName", sb.toString()).addAttribute("userIds", userIds));
        }
    }

    @ResponseBody
    @Journal(name = "编辑裁剪计划", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(CutPlan cutPlan, Long[] userIds, String tcBomPartId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tcBomPartId", Long.valueOf(tcBomPartId));
        List<PlanUsers> list = cutPlanService.findListByMap(PlanUsers.class, map);
        if (list.size() == 0) {
            cutPlanService.save(cutPlan, userIds, tcBomPartId);
        } else {
            cutPlanService.updateUser(cutPlan, userIds, tcBomPartId);
        }
        return GsonTools.toJson(cutPlan);
    }

    @ResponseBody
    @Journal(name = "排序字段", logType = LogType.DB)
    @RequestMapping(value = "sortEdit", method = RequestMethod.POST)
    public String sortEdit(Integer index, Long id) throws Exception {
        CutPlan cutPlan = cutPlanService.findById(CutPlan.class, id);
        cutPlan.setSort(index.longValue());
        cutPlanService.update(cutPlan);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除裁剪计划", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        cutPlanService.delete(CutPlan.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "加载生产计划单号combobox", logType = LogType.DB)
    @RequestMapping(value = "planCodeCombobox", method = RequestMethod.POST)
    public String planCodeCombobox() throws Exception {
        return GsonTools.toJson(cutPlanService.planCodeCombobox());
    }

    /**
     * EASYUI组件DataList
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产计划列表信息")
    @RequestMapping("datalist")
    public String datalist(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(cutPlanService.dataList(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部件信息")
    @RequestMapping("findParts")
    public String findParts(Long id) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("cutPlanDailyPlanId", id);
        List<CutDailyPlanUsers> li = cutPlanService.findListByMap(CutDailyPlanUsers.class, map);
        Map<String, Object> ret = null;
        List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();
		
		/*for (CutDailyPlanUsers cpu : li) {
			ret = new HashMap<String, Object>();
			ret.put("id", cpu.getId());
			ret.put("text", cpu.getPartName());
			User user = cutPlanService.findById(User.class, cpu.getUserId());
			ret.put("userName", user.getUserName() + "(" + cpu.getCount() + ")");
			listMap1.add(ret);
		}*/

        Map<String, Object> ret2 = new HashMap<String, Object>();
        for (CutDailyPlanUsers cpu2 : li) {
            User user = cutPlanService.findById(User.class, cpu2.getUserId());
            if (ret2.containsKey(cpu2.getPartName())) {
                ret2.put(cpu2.getPartName(), ret2.get(cpu2.getPartName()) + " " + user.getUserName() + "(" + cpu2.getCount() + ")");
            } else {
                ret2.put(cpu2.getPartName(), user.getUserName() + "(" + cpu2.getCount() + ")");
            }
        }
        Iterator<String> it = ret2.keySet().iterator();
        while (it.hasNext()) {
            ret = new HashMap<String, Object>();
            String key = (String) it.next();
            ret.put("text", key);
            ret.put("userName", ret2.get(key));
            listMap1.add(ret);
        }
        return GsonTools.toJson(listMap1);
    }

	/*@NoAuth
	@ResponseBody
	@Journal(name = "获取部件信息")
	@RequestMapping("findParts2")
	public String findParts(Long cutPlanId, Long cutDailyPlanId) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cutPlanDailyPlanId", cutDailyPlanId);
		map.put("cutPlanId", cutPlanId);
		List<CutDailyPlanUsers> li = cutPlanService.findListByMap(CutDailyPlanUsers.class, map);
		Map<String, Integer> ret = new HashMap<String, Integer>();
		List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();

		for (CutDailyPlanUsers cpu : li) {
			if (!ret.containsKey(cpu.getPartName())) {
				ret.put(cpu.getPartName(), cpu.getCount());
			} else {
				int count = ret.get(cpu.getPartName());
				ret.put(cpu.getPartName(), cpu.getCount() + count);
			}
		}
		Map<String, Object> resultMap = null;
		int a = 0;
		for (String key : ret.keySet()) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("id", a);
			resultMap.put("partName", key);
			resultMap.put("printCount", ret.get(key));
			listMap1.add(resultMap);
			a++;
		}
		return GsonTools.toJson(listMap1);
	}*/

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部件信息")
    @RequestMapping("findParts2")
    public String findParts(Long cutPlanId, Long cutDailyPlanId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("cutPlanDailyPlanId", cutDailyPlanId);
        map.put("cutPlanId", cutPlanId);
        List<CutDailyPlanUsers> li = cutPlanService.findListByMap(CutDailyPlanUsers.class, map);
        HashMap<String, Long> parN = new HashMap<String, Long>();
        for (CutDailyPlanUsers cpu : li) {
            parN.put(cpu.getPartName(), cpu.getPartId());
        }
        List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        for (String partName : parN.keySet()) {
            resultMap = new HashMap<String, Object>();
            resultMap.put("text", partName);
            resultMap.put("value", parN.get(partName));
            listMap1.add(resultMap);
        }
        String json = GsonTools.toJson(listMap1);
        return json;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部件信息")
    @RequestMapping("findParts3")
    public String findParts3(Long orderId, Long planId) throws Exception {

        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderDetailId", orderId);
        List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
        for (SalesOrderDetailPartsCount p : list) {
            Map<String, Object> unit = new HashMap<>();

            unit.put("text", p.getPartName());
            unit.put("value", p.getPartId());
            ret.add(unit);
        }
        return GsonTools.toJson(ret);
    }

    @ResponseBody
    @Journal(name = "裁剪计划标记已完成", logType = LogType.DB)
    @RequestMapping(value = "isFinish", method = RequestMethod.POST)
    @Valid
    public String finish(String ids) throws Exception {
        cutPlanService.updateState(ids);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "添加裁剪订单页面")
    @RequestMapping(value = "add")
    public ModelAndView _add(SalesOrder salesOrder, Long cutPlanId, Filter filter) throws Exception {
        CutPlan plan = salesOrderService.findById(CutPlan.class, cutPlanId);
        filter.set("salesOrderSubCode", "like:" + plan.getSalesOrderCode());
        Map<String, Object> findPageInfo = salesOrderService.findPageInfo(filter, new Page());
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageInfo.get("rows");
        filter = new Filter();
        String salesOrderSubCode = list.get(0).get("ID").toString();
        HashMap<String, Object> map11 = new HashMap<String, Object>();
        map11.put("salesOrderId", Long.parseLong(salesOrderSubCode));
        List<SalesOrderDetail> details2 = salesOrderService.findListByMap(SalesOrderDetail.class, map11);
//		SalesOrderDetail sod=details2.get(0);
        String yx = plan.getFromTcName();
        if (plan.getWeaveSalesOrderId() != null) {
            salesOrder = salesOrderService.findById(SalesOrder.class, plan.getWeaveSalesOrderId());
            List<SalesOrderDetail> de = salesOrderService.getDetails(plan.getWeaveSalesOrderId());
            List<Map<Object, Object>> details = new ArrayList<Map<Object, Object>>();
            for (SalesOrderDetail sd : de) {
//				map11.clear();
//				map11.put("tcFinishedProductId", sd.getProductId());
                Map<Object, Object> m = MapUtils.entityToMap(sd);
                //List<TcBomVersionPartsDetail> findListByMap = salesOrderService.findListByMap(TcBomVersionPartsDetail.class, map11);
				/*for (int i = 0; i < findListByMap.size(); i++) {
					m.put("levelno", findListByMap.get(i).getLevelNo());//层数
					m.put("rollno", findListByMap.get(i).getRollNo());//卷号
					m.put("drawingno", findListByMap.get(i).getDrawingNo());//图号
				}*/
                details.add(m);
            }
            return new ModelAndView(addOrEditOrder, model.addAttribute("salesOrder", salesOrder).addAttribute("details", GsonTools.toJson(details)).addAttribute("consumerName", "裁剪车间").addAttribute("cutPlanId", cutPlanId).addAttribute("finalConsumerId", plan.getConsumerId()).addAttribute("code", plan.getSalesOrderCode()));

        } else {
            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("consumerCode", "CJ000");
            Consumer c = salesOrderService.findUniqueByMap(Consumer.class, map1);
            salesOrder.setSalesOrderConsumerId(c.getId());
            salesOrder.setSalesOrderIsExport(-1);
            salesOrder.setSalesOrderDate(new Date());
            salesOrder.setAuditState(0);
            // 根据裁剪计划明细编辑

            // Consumer c=salesOrderService.findById(Consumer.class,
            // plan.getConsumerId());
            salesOrder.setSalesOrderMemo("来自裁剪计划：" + plan.getPlanCode() + ";订单号为:" + plan.getSalesOrderCode() + ";叶型为：" + yx);
            List<SalesOrderDetail> de = getFabs(plan);

            List<Map<Object, Object>> details = new ArrayList<Map<Object, Object>>();
            for (SalesOrderDetail sd : de) {
				/*map11.clear();
				map11.put("tcFinishedProductId", sd.getProductId());*/
                Map<Object, Object> m = MapUtils.entityToMap(sd);
				/*List<TcBomVersionPartsDetail> findListByMap = salesOrderService.findListByMap(TcBomVersionPartsDetail.class, map11);
				for (int i = 0; i < findListByMap.size(); i++) {
					m.put("levelno", findListByMap.get(i).getLevelNo());//层数
					m.put("rollno", findListByMap.get(i).getRollNo());//卷号
					m.put("drawingno", findListByMap.get(i).getDrawingNo());//图号
				}*/
                details.add(MapUtils.entityToMap(sd));
            }
            return new ModelAndView(addOrEditOrder, model
                    .addAttribute("salesOrder", salesOrder)
                    .addAttribute("details", GsonTools.toJson(details))
                    .addAttribute("consumerName", "裁剪车间")
                    .addAttribute("cutPlanId", cutPlanId)
                    .addAttribute("finalConsumerId", plan.getConsumerId())
                    .addAttribute("code", plan.getSalesOrderCode())
                    .addAttribute("fromProducePlancode", plan.getPlanCode())


            );
        }
    }

    // 根据tcBomVersionId获取需要生产的胚布的id和数量
    public List<SalesOrderDetail> getFabs(CutPlan plan) {
        HashMap<String, Object> parm = new HashMap<String, Object>();
        parm.put("productBatchCode", plan.getBatchCode());
        parm.put("closed", null);
        parm.put("salesOrderSubCode", "cj-" + plan.getSalesOrderCode());
        List<SalesOrderDetailTemp> sodList = bomService.findListByMap(SalesOrderDetailTemp.class, parm);
        //查找套材镜像成品信息
        Map<String,Object> planMap = new HashMap<>();
        planMap.put("productId",plan.getProductId());
        planMap.put("salesOrderDetailId",plan.getFromSalesOrderDetailId());
        List<FinishedProductMirror> finishedProductMirrors = salesOrderService.findListByMap(FinishedProductMirror.class, planMap);
        List<SalesOrderDetail> list = new ArrayList<SalesOrderDetail>();
        if (sodList.size() > 0) {
            SalesOrderDetail sd = null;
            for (SalesOrderDetailTemp temp : sodList) {
                sd = new SalesOrderDetail();
                sd.setProductId(temp.getProductId());
                sd.setSalesOrderSubCode(temp.getSalesOrderSubCode());
                sd.setProductBatchCode(temp.getProductBatchCode());
                sd.setConsumerProductName(temp.getConsumerProductName());
                sd.setFactoryProductName(temp.getFactoryProductName());
                sd.setProductWidth(temp.getProductWidth());
                sd.setProductRollLength(temp.getProductRollLength());
                sd.setProductRollWeight(temp.getProductRollWeight());
                sd.setProductProcessCode(temp.getProductProcessCode());
                sd.setProductProcessBomVersion(temp.getProductProcessBomVersion());
                sd.setProductPackagingCode(temp.getProductPackagingCode());
                sd.setProductPackageVersion(temp.getProductPackageVersion());
                sd.setProductRollCode(temp.getProductRollCode());
                sd.setProductBoxCode(temp.getProductBoxCode());
                sd.setProductTrayCode(temp.getProductTrayCode());
                sd.setProductModel(temp.getProductModel());
                sd.setProductCount(temp.getProductCount());
                sd.setProductIsTc(temp.getProductIsTc());
                sd.setProcBomId(temp.getProcBomId());
                sd.setPackBomId(temp.getPackBomId());
                /*新增图号，卷号，层号,部件ID*/
                sd.setLevelNo(temp.getLevelNo());
                sd.setDrawNo(temp.getDrawNo());
                sd.setRollNo(temp.getRollNo());
                sd.setPartId(temp.getPartId());
                sd.setSorting(temp.getSorting());
                sd.setPartName(temp.getPartName());
                sd.setBladeProfile(finishedProductMirrors.get(0).getConsumerProductName());
                if (null != temp.getMirrorPartId()) {
                    sd.setMirrorPartId(temp.getMirrorPartId());
                }
                if (null != temp.getMirrorFtcProcBomId()) {
                    sd.setMirrorProcBomVersionId(temp.getMirrorFtcProcBomId());
                }
                list.add(sd);
            }
        } else {
            HashMap<String, Object> part = new HashMap<String, Object>();
            part.put("tcProcBomVersoinId", plan.getProcBomId());
            //套材部件列表
            List<TcBomVersionParts> partList = bomService.findListByMap(TcBomVersionParts.class, part);
            //套材部件明细
            List<TcBomVersionPartsDetail> tcBomList = new ArrayList<TcBomVersionPartsDetail>();
            for (TcBomVersionParts p : partList) {
                if (p.getTcProcBomVersionPartsType().equals("成品胚布")) continue;
                part.clear();
                part.put("tcProcBomPartsId", p.getId());
                tcBomList.addAll(bomService.findListByMap(TcBomVersionPartsDetail.class, part));
            }
            if (tcBomList.size() == 0) {
                return list;
            }

            ProducePlanDetail ppd = bomService.findById(ProducePlanDetail.class, plan.getProducePlanDetailId());
            part.clear();
            part.put("planDetailId", ppd.getId());
            List<ProducePlanDetailPartCount> ppdpcList = bomService.findListByMap(ProducePlanDetailPartCount.class, part);

            SalesOrderDetail sd = null;
            TcBomVersionParts partInfo = null;

            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = tcBomList.size() - 1; i >= 0; i--) {
                double count = 0;
                for (ProducePlanDetailPartCount ppdpc : ppdpcList) {
                    if (ppdpc.getPartId().equals(tcBomList.get(i).getTcProcBomPartsId())) {
                        count = ppdpc.getPlanPartCount();
                    }
                }
                FinishedProduct fp = bomService.findById(FinishedProduct.class, tcBomList.get(i).getTcFinishedProductId());

                if (count * tcBomList.get(i).getTcProcBomFabricCount() > 0) {
                    sd = new SalesOrderDetail();
                    sd.setProductId(fp.getId());
                    sd.setSalesOrderSubCode("cj-" + plan.getSalesOrderCode());
                    sd.setProductBatchCode(plan.getBatchCode());
                    sd.setConsumerProductName(fp.getConsumerProductName());
                    sd.setFactoryProductName(fp.getFactoryProductName());
                    sd.setProductWidth(fp.getProductWidth());
                    sd.setProductRollLength(fp.getProductRollLength());
                    sd.setProductRollWeight(fp.getProductRollWeight());
                    sd.setProductProcessCode(fp.getProductProcessCode());
                    sd.setProductProcessBomVersion(fp.getProductProcessBomVersion());
                    sd.setProductPackagingCode(fp.getProductPackagingCode());
                    sd.setProductPackageVersion(fp.getProductPackageVersion());
                    sd.setProductRollCode(fp.getProductRollCode());
                    sd.setProductBoxCode(fp.getProductBoxCode());
                    sd.setProductTrayCode(fp.getProductTrayCode());
                    sd.setProductModel(fp.getProductModel());
                    sd.setProductCount(count * tcBomList.get(i).getTcProcBomFabricCount());
                    sd.setProductIsTc(fp.getProductIsTc());
                    sd.setProcBomId(fp.getProcBomId());
                    sd.setPackBomId(fp.getPackBomId());
                    /*新增图号，卷号，层号,部件ID*/
                    sd.setLevelNo(tcBomList.get(i).getLevelNo());
                    sd.setDrawNo(tcBomList.get(i).getDrawingNo());
                    sd.setRollNo(tcBomList.get(i).getRollNo());
                    sd.setPartId(tcBomList.get(i).getTcProcBomPartsId());
                    sd.setSorting(tcBomList.get(i).getSorting());
                    partInfo = bomService.findById(TcBomVersionParts.class, tcBomList.get(i).getTcProcBomPartsId());
                    sd.setPartName(partInfo.getTcProcBomVersionPartsName());
                    list.add(sd);
                }
            }
        }
        return list;
    }

    @ResponseBody
    @Journal(name = "保存销售订单", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody SalesOrder salesOrder) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("salesOrderCode", salesOrder.getSalesOrderCode());

        if (salesOrderService.isExist(SalesOrder.class, param, true)) {
            return ajaxError("订单编号重复");
        }

        if (salesOrder.getAuditState() != null) {
            if (salesOrder.getAuditState() > 0) {
                return ajaxError("已审核和审核中的订单不能修改");
            }
        }

        CutPlan cutPlan = salesOrderService.findById(CutPlan.class, salesOrder.getCutPlanId());
        if (cutPlan.getIsCreatWeave() == 1) {
            return ajaxError("已生成胚布计划的订单不能修改");
        }
        //原订单
        SalesOrderDetail originOrder = salesOrderService.findById(SalesOrderDetail.class, cutPlan.getFromSalesOrderDetailId());
        // 校验产品对应的BOM是否可用
        List<SalesOrderDetail> ps = salesOrder.getDetails();
        FtcBomVersion fbv = null;
        FtcBomVersionMirror fbvm = null;
        for (SalesOrderDetail p : ps) {
            //设置原订单
            p.setSalesOrderSubCodePrint(originOrder.getSalesOrderSubCodePrint());
            if (p.getProcBomId() == null) {
                return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺BOM错误，不能下单,请联系工艺部");
            }
            if (null != p.getMirrorProcBomVersionId()) {
                fbvm = salesOrderService.findById(FtcBomVersionMirror.class, p.getMirrorProcBomVersionId());
            }
            if (null == fbvm) {
                fbv = salesOrderService.findById(FtcBomVersion.class, p.getProcBomId());
                if (fbv != null && fbv.getAuditState() != AuditConstant.RS.PASS) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺BOM尚未审核通过或变更中，不能下单");
                }
            }
            param.clear();
            param.put("salesOrderSubCode", p.getSalesOrderSubCode());
            param.put("closed", null);
            if (salesOrderService.has(SalesOrderDetail.class, param)) {
                return ajaxError("销售订单号重复:" + p.getSalesOrderSubCode());
            }
            param.put("closed", 0);
            if (salesOrderService.has(SalesOrderDetail.class, param)) {
                return ajaxError("销售订单号重复:" + p.getSalesOrderSubCode());
            }
        }
        if (salesOrder.getId() == null) {
            salesOrderService.save(salesOrder);
        } else {
            salesOrderService.edit(salesOrder);
        }

        if (cutPlan.getIsCreatWeave() == 0) {
            cutPlan.setIsCreatWeave(1);
        }/* else {
            cutPlan.setIsCreatWeave(0);
        }*/

        cutPlan.setWeaveSalesOrderId(salesOrder.getId());
        salesOrderService.update(cutPlan);
        return GsonTools.toJson("保存成功");
    }

    @Journal(name = "复制产品信息页面")
    @RequestMapping(value = "copyProduct", method = RequestMethod.GET)
    public ModelAndView copyProduct(Long productId, Long finalConsumerId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // 获取复制产品对象的id，查找原产品的信息
        FinishedProduct fp = salesOrderService.findById(FinishedProduct.class, productId);
        FinishedProduct copyInfo = new FinishedProduct();
        ObjectUtils.clone(fp, copyInfo);
        Consumer consumer = salesOrderService.findById(Consumer.class, finalConsumerId);
        // 复制原产品的信息生成新对象，将新对象的卷长设置为空
        copyInfo.setId(null);
        copyInfo.setProductRollLength(null);
        return new ModelAndView(copyProduct, model.addAttribute("finishProduct", copyInfo).addAttribute("consumer", consumer));
    }
}