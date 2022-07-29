/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.controller;

import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.service.IConsumerService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 成品信息Controller
 *
 * @author 宋黎明
 * @Date 2016-9-30 10:49:33
 */
@Controller
@RequestMapping("/finishProduct")
@Journal(name = "成品信息")
public class FinishedProductController extends BaseController {
    // 成品信息页面
    final String index = "produce/finishProduct/finishProduct";
    final String addOrEdit = "produce/finishProduct/finishProductAddOrEdit";
    // 选择客户信息窗口
    final String chooseConsumer = "produce/finishProduct/chooseConsumer";
    // 选择套材BOM窗口
    final String chooseTcBom = "produce/finishProduct/chooseTcBom";
    // 选择非套材BOM窗口
    final String chooseFtcBom = "produce/finishProduct/chooseFtcBom";
    // 选择成品类别窗口
    final String selectorProductCategory = "selector/selectorProductcategory";
    // 选择衬衬管编码窗口
    final String selectorCarrierCode = "selector/selectorCarrierCode";
    // 选择包装BOM窗口
    final String choosePackingBom = "produce/finishProduct/choosePackingBom";
    //查看成品信息窗口
    final String checkProduct = "produce/finishProduct/checkProcuct";

    //查看叶型信息
    final String viewYxInfo = "produce/finishProduct/checkYxInfo";

    final String audit = "produce/finishProduct/finishProductAudit";

    @Resource
    IFinishedProductService finishProductService;
    @Resource
    IConsumerService consumerService;
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IFtcBcBomService ftcBcBomService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    /**
     * 选择成品类别页面
     *
     * @return
     */
    @Journal(name = "选择成品类别页面")
    @RequestMapping(value = "selectorProductCategory", method = RequestMethod.GET)
    public String selectorProductCategory() {
        return selectorProductCategory;
    }


    /**
     * 选择衬管编码页面
     */
    @Journal(name = "选择衬管编码页面")
    @RequestMapping(value = "selectorCarrierCode", method = RequestMethod.GET)
    public String selectorCarrierCode() {
        return selectorCarrierCode;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品信息列表信息")
    @RequestMapping("list")
    public String getFinishProduct(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = finishProductService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.0");
        if (list.size() > 0) {
            boolean isChange = false;
            for (Map<String, Object> stringObjectMap : list) {
                FinishedProduct finishedProduct = finishProductService.findById(FinishedProduct.class, Long.parseLong(stringObjectMap.get("ID").toString()));
                //BOM工艺名称改变时产品工艺代码和名称也会随之改变
                if (stringObjectMap.get("PRODUCTPROCESSCODE") != null) {
                    String productProcessCode = stringObjectMap.get("PRODUCTPROCESSCODE").toString();
                    String productProcessCodes[] = productProcessCode.split("/");
                    if (productProcessCodes.length == 2) {
                        if (!"HS".equals(productProcessCodes[0]) && productProcessCodes[0] != null && productProcessCodes[1] != null) {
                            finishedProduct.setProductProcessCode(productProcessCodes[1]);
                            finishedProduct.setProductProcessName(productProcessCodes[0]);
                            finishProductService.update(finishedProduct);
                            isChange = true;
                        }
                    } else if (productProcessCodes.length == 3) {
                        finishedProduct.setProductProcessCode(productProcessCodes[1] + "/" + productProcessCodes[2]);
                        finishedProduct.setProductProcessName(productProcessCodes[0]);
                        finishProductService.update(finishedProduct);
                        isChange = true;
                    }
                }

                Object weight = stringObjectMap.get("PRODUCTROLLWEIGHT");
                if (weight != null) {
                    weight = df.format(weight);
                    stringObjectMap.put("PRODUCTROLLWEIGHT", weight);
                }
                // 产品的总克重
                String procBomId = stringObjectMap.get("PROCBOMID").toString();
                int total_ftcBomDetailWeightPerSquareMetre = finishProductService.queryProcBomDetail(procBomId);
                stringObjectMap.put("PRODUCTRTOTALWEIGHT", total_ftcBomDetailWeightPerSquareMetre);
            }
            if (isChange) {
                findPageInfo = finishProductService.findPageInfo(filter, page);
            }
        }
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品信息列表信息")
    @RequestMapping("productMirrorList")
    public String getFinishProductMirror(Filter filter, Page page) {
        Map<String, Object> findPageInfo = finishProductService.findPageInfo1(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品信息列表信息")
    @RequestMapping("listAll")
    public String getAllFinishProduct(Filter filter, Page page) throws Exception {
        page.setAll(1);
        Map<String, Object> findPageInfo = finishProductService.findPageInfo(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.0");
        if (list.size() > 0) {
            list.removeIf(x -> "-1".equals(x.get("BCBOMCANCELED") + "") || "-1".equals(x.get("FTCBCBOMCANCELED") + ""));
            for (Map<String, Object> stringObjectMap : list) {
                Object weight = stringObjectMap.get("PRODUCTROLLWEIGHT");
                if (weight != null) {
                    weight = df.format(weight);
                    stringObjectMap.put("PRODUCTROLLWEIGHT", weight);
                }
            }
        }
        return GsonTools.toJson(findPageInfo);
    }

    List<FtcBcBom> findPackType() {
        Map<String, Object> map = new HashMap<>();
        map.put("level", 2);
        return ftcBcBomService.findListByMap(FtcBcBom.class, map);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品信息列表信息")
    @RequestMapping("listDelivery")
    public String listDelivery(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(finishProductService.findPageInfoDelivery(filter, page));
    }

    @Journal(name = "添加成品信息页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(FinishedProduct finishProduct, FinishedProductCategory productCategory) {
        return new ModelAndView(addOrEdit, model.addAttribute("codes", GsonTools.toJson(findPackType())).addAttribute("finishProduct", finishProduct).addAttribute("productCategory", productCategory));
    }

    @ResponseBody
    @Journal(name = "保存成品信息", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(FinishedProduct finishProduct, String productCategoryID) throws Exception {
        int bh = finishProductService.querySlinfo(finishProduct.getMaterielCode());
        if (bh > 0) {
            return ajaxError("已存在相同物料编号，请重新获取物料编号！");
        }

        finishProduct.setCreater(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        finishProduct.setCreateTime(new Date());
        if (finishProduct.getProductRollWeight() != null) {
            Double w = finishProduct.getProductRollWeight();
            finishProduct.setProductRollWeight(Double.parseDouble(String.format("%.1f", w)));//四舍五入，保留一位小数点
        }
        finishProduct.setFpcid(Long.parseLong(productCategoryID));
        //新添加的成品信息与已保存的且非作废的产品信息进行比对，判断是否重复
        //胚布
        List<FinishedProduct> finishedProducts = finishProductService.findAll(FinishedProduct.class);
        for (FinishedProduct oldFinishedProduct : finishedProducts) {
            if (oldFinishedProduct.getObsolete() == null || oldFinishedProduct.getObsolete() == 0) {
                //胚布
                if (finishProduct.getProductIsTc() == -1) {
                    if ((finishProduct.getProductConsumerId() == oldFinishedProduct.getProductConsumerId()) && finishProduct.getMaterielCode().equals(oldFinishedProduct.getMaterielCode())) {
                        return ajaxError("已存在相同的产品信息！");
                    }
                } else if (finishProduct.getProductIsTc() == 1) {//套材
                    if ((finishProduct.getProductConsumerId().equals(oldFinishedProduct.getProductConsumerId())) && (finishProduct.getConsumerProductName().equals(oldFinishedProduct.getConsumerProductName()))
                            && (finishProduct.getProductIsTc().equals(oldFinishedProduct.getProductIsTc())) && (finishProduct.getIsCommon().equals(oldFinishedProduct.getIsCommon()))
                            && (finishProduct.getProductProcessCode().equals(oldFinishedProduct.getProductProcessCode())) && (finishProduct.getProductProcessName().equals(oldFinishedProduct.getProductProcessName()))
                            && (finishProduct.getProductShelfLife().equals(oldFinishedProduct.getProductShelfLife())) && (finishProduct.getProductPackagingCode().equals(oldFinishedProduct.getProductPackagingCode()))
                            && (Long.parseLong(productCategoryID) == oldFinishedProduct.getFpcid()) && (finishProduct.getMaterielCode().equals(oldFinishedProduct.getMaterielCode()))
                            && (finishProduct.getProductModel().equals(oldFinishedProduct.getProductModel())) && (finishProduct.getProductWeigh().equals(oldFinishedProduct.getProductWeigh()))) {
                        return ajaxError("已存在相同的产品信息！");
                    }
                } else if (finishProduct.getProductIsTc() == 2) {//非套材
                    if ((finishProduct.getProductConsumerId() == oldFinishedProduct.getProductConsumerId()) && finishProduct.getConsumerProductName().equals(oldFinishedProduct.getConsumerProductName())
                            && finishProduct.getProductIsTc().equals(oldFinishedProduct.getProductIsTc()) && finishProduct.getIsCommon().equals(oldFinishedProduct.getIsCommon())
                            && finishProduct.getProductProcessCode().equals(oldFinishedProduct.getProductProcessCode()) && finishProduct.getProductProcessName().equals(oldFinishedProduct.getProductProcessName())
                            && finishProduct.getProductShelfLife().equals(oldFinishedProduct.getProductShelfLife()) && finishProduct.getProductPackagingCode().equals(oldFinishedProduct.getProductPackagingCode())
                            && (Long.parseLong(productCategoryID) == oldFinishedProduct.getFpcid()) && finishProduct.getMaterielCode().equals(oldFinishedProduct.getMaterielCode())
                            && finishProduct.getProductModel().equals(oldFinishedProduct.getProductModel()) && finishProduct.getProductWeigh().equals(oldFinishedProduct.getProductWeigh())
                            && (finishProduct.getProductWidth() != null ? finishProduct.getProductWidth() : "").equals(oldFinishedProduct.getProductWidth() != null ? oldFinishedProduct.getProductWidth() : "") && (finishProduct.getProductRollLength() != null ? finishProduct.getProductRollLength() : "").equals(oldFinishedProduct.getProductRollLength() != null ? oldFinishedProduct.getProductRollLength() : "")
                            && (finishProduct.getMaxWeight() != null ? finishProduct.getMaxWeight() : "").equals(oldFinishedProduct.getMaxWeight() != null ? oldFinishedProduct.getMaxWeight() : "") && (finishProduct.getMinWeight() != null ? finishProduct.getMinWeight() : "").equals(oldFinishedProduct.getMinWeight() != null ? oldFinishedProduct.getMinWeight() : "")
                            && (finishProduct.getProductRollWeight() != null ? finishProduct.getProductRollWeight() : "").equals(oldFinishedProduct.getProductRollWeight() != null ? oldFinishedProduct.getProductRollWeight() : "") && finishProduct.getCarrierCode().equals(oldFinishedProduct.getCarrierCode())) {
                        return ajaxError("已存在相同的产品信息！");
                    }
                }
            }
        }

        double productWeight;
        if (finishProduct.getProductRollWeight() == null) {
            List<FtcBomDetail> bomDetails;
            Map<String, Object> map = new HashMap<>();
            map.put("ftcBomVersionId", finishProduct.getProcBomId());
            bomDetails = finishProductService.findListByMap(FtcBomDetail.class, map);
            double bomWeight = 0D;
            for (FtcBomDetail d : bomDetails) {
                bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
            }
            //米长*门幅*单位面积克重
            double width = finishProduct.getProductWidth() != null ? finishProduct.getProductWidth() : 0D;
            double length = finishProduct.getProductRollLength() != null ? finishProduct.getProductRollLength() : 0D;
            productWeight = MathUtils.add(bomWeight * width * length, 0D, 2);
            productWeight = MathUtils.div(productWeight, 1000000, 2);
            if (finishProduct.getMaxWeight() == null) {
                finishProduct.setMaxWeight(MathUtils.add(productWeight * 1.03, 0D, 2));
            }
            if (finishProduct.getMinWeight() == null) {
                finishProduct.setMinWeight(MathUtils.add(productWeight * 0.97, 0D, 2));
            }
        }
        finishProduct.setAuditState(0);
        finishProduct.setAuditChange(1);
        finishProduct.setObsolete(null);
        finishProductService.save(finishProduct);
        return GsonTools.toJson(finishProduct);
    }

    @Journal(name = "编辑成品信息页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(FinishedProduct finishProduct, Boolean copy, FinishedProductCategory productCategory) throws Exception {
        finishProduct = finishProductService.findById(FinishedProduct.class, finishProduct.getId());
        //根据衬管编码查找衬管名称
        String zgmc = finishProductService.getzgmcbycode(finishProduct.getCarrierCode());
        Consumer consumer = consumerService.findById(Consumer.class, finishProduct.getProductConsumerId());
        if (finishProduct.getIsShow() == null) {
            if (copy != null && copy) {
                finishProduct.setIsShow(1);
            } else {
                finishProduct.setIsShow(0);
            }
        }
        double productWeight = 0D;
        double bomWeight = 0D;
        if (finishProduct.getProductRollWeight() == null) {
            List<FtcBomDetail> bomDetails;
            Map<String, Object> map = new HashMap<>();
            map.put("ftcBomVersionId", finishProduct.getProcBomId());
            bomDetails = finishProductService.findListByMap(FtcBomDetail.class, map);
            for (FtcBomDetail d : bomDetails) {
                bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
            }
            //米长*门幅*单位面积克重
            double width = finishProduct.getProductWidth() != null ? finishProduct.getProductWidth() : 0D;
            double length = finishProduct.getProductRollLength() != null ? finishProduct.getProductRollLength() : 0D;
            productWeight = MathUtils.add(bomWeight * width * length, 0D, 2);
            productWeight = MathUtils.div(productWeight, 1000000, 2);
            if (finishProduct.getMaxWeight() == null) {
                finishProduct.setMaxWeight(MathUtils.add(productWeight * 1.03, 0D, 2));
            }
            if (finishProduct.getMinWeight() == null) {
                finishProduct.setMinWeight(MathUtils.add(productWeight * 0.97, 0D, 2));
            }
        }
        //成品类别信息
        productCategory = finishProduct.getFpcid() == null ? null : finishProductService.findById(FinishedProductCategory.class, finishProduct.getFpcid());
        return new ModelAndView(addOrEdit, model.addAttribute("finishProduct", finishProduct).addAttribute("codes", GsonTools.toJson(findPackType())).addAttribute("consumer", consumer).addAttribute("productCategory", productCategory).addAttribute("productWeight", productWeight).addAttribute("bomWeight", bomWeight).addAttribute("zgmc", zgmc));
    }

    @ResponseBody
    @Journal(name = "编辑成品信息", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(FinishedProduct finishProduct, String productCategoryID) throws Exception {
        finishProduct.setModifyTime(new Date());
        finishProduct.setModifyUser(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        Long id = finishProduct.getId();
        finishProduct.setId(null);
        if (finishProduct.getProductRollWeight() != null) {
            Double w = finishProduct.getProductRollWeight();
            //四舍五入，保留一位小数点
            finishProduct.setProductRollWeight(Double.parseDouble(String.format("%.1f", w)));
        }
        finishProduct.setFpcid(Long.parseLong(productCategoryID));
        //成品信息与已保存的且非作废的产品信息进行比对，判断是否重复
        List<Map<String, Object>> finishedProducts = finishProductService.findAllFinishProduct();
        for (Map<String, Object> oldFinishedProduct : finishedProducts) {
            if (finishProduct.getProductIsTc() == -1) { //胚布
                if ((Objects.equals(finishProduct.getProductConsumerId(), Long.valueOf(oldFinishedProduct.get("PRODUCTCONSUMERID").toString()))) && finishProduct.getMaterielCode().equals(oldFinishedProduct.get("MATERIELCODE")) && (!String.valueOf(id).equals(oldFinishedProduct.get("ID").toString()))) {
                    return ajaxError("已存在相同的产品信息！");
                }
            } else if (finishProduct.getProductIsTc() == 1) {//套材
                if ((String.valueOf(finishProduct.getProductConsumerId()).equals(oldFinishedProduct.get("PRODUCTCONSUMERID").toString())) && (finishProduct.getConsumerProductName().equals(oldFinishedProduct.get("CONSUMERPRODUCTNAME")))
                        && (finishProduct.getProductIsTc().equals(oldFinishedProduct.get("PRODUCTISTC"))) && (finishProduct.getIsCommon().equals(oldFinishedProduct.get("ISCOMMON")))
                        && (finishProduct.getProductProcessCode().equals(oldFinishedProduct.get("PRODUCTPROCESSCODE"))) && (finishProduct.getProductProcessName().equals(oldFinishedProduct.get("PRODUCTPROCESSNAME")))
                        && (finishProduct.getProductShelfLife().equals(oldFinishedProduct.get("PRODUCTSHELFLIFE"))) && (finishProduct.getProductPackagingCode().equals(oldFinishedProduct.get("PRODUCTPACKAGINGCODE")))
                        && (Long.parseLong(productCategoryID) == Long.parseLong(oldFinishedProduct.get("FPCID").toString())) && (finishProduct.getMaterielCode().equals(oldFinishedProduct.get("MATERIELCODE")))
                        && (finishProduct.getProductModel().equals(oldFinishedProduct.get("PRODUCTMODEL"))) && (finishProduct.getProductWeigh().equals(oldFinishedProduct.get("PRODUCTWEIGH"))) && (!String.valueOf(id).equals(oldFinishedProduct.get("ID").toString()))) {
                    return ajaxError("已存在相同的产品信息！");
                }
            } else if (finishProduct.getProductIsTc() == 2) {//非套材
                if ((Objects.equals(finishProduct.getProductConsumerId(), Long.valueOf(oldFinishedProduct.get("PRODUCTCONSUMERID").toString()))) && finishProduct.getConsumerProductName().equals(oldFinishedProduct.get("CONSUMERPRODUCTNAME"))
                        && finishProduct.getProductIsTc().equals(oldFinishedProduct.get("PRODUCTISTC")) && finishProduct.getIsCommon().equals(oldFinishedProduct.get("ISCOMMON"))
                        && finishProduct.getProductProcessCode().equals(oldFinishedProduct.get("PRODUCTPROCESSCODE")) && finishProduct.getProductProcessName().equals(oldFinishedProduct.get("PRODUCTPROCESSNAME"))
                        && finishProduct.getProductShelfLife().equals(oldFinishedProduct.get("PRODUCTSHELFLIFE")) && finishProduct.getProductPackagingCode().equals(oldFinishedProduct.get("PRODUCTPACKAGINGCODE"))
                        && (Long.parseLong(productCategoryID) == Long.parseLong(oldFinishedProduct.get("FPCID").toString())) && finishProduct.getMaterielCode().equals(oldFinishedProduct.get("MATERIELCODE"))
                        && finishProduct.getProductModel().equals(oldFinishedProduct.get("PRODUCTMODEL")) && finishProduct.getProductWeigh().equals(oldFinishedProduct.get("PRODUCTWEIGH"))
                        && (finishProduct.getProductWidth() != null ? finishProduct.getProductWidth() : "").equals(oldFinishedProduct.get("PRODUCTWIDTH") != null ? oldFinishedProduct.get("PRODUCTWIDTH") : "") && (finishProduct.getProductRollLength() != null ? finishProduct.getProductRollLength() : "").equals(oldFinishedProduct.get("PRODUCTROLLLENGTH") != null ? oldFinishedProduct.get("PRODUCTROLLLENGTH") : "")
                        && (finishProduct.getMaxWeight() != null ? finishProduct.getMaxWeight() : "").equals(oldFinishedProduct.get("MAXWEIGHT") != null ? oldFinishedProduct.get("MAXWEIGHT") : "") && (finishProduct.getMinWeight() != null ? finishProduct.getMinWeight() : "").equals(oldFinishedProduct.get("MINWEIGHT") != null ? oldFinishedProduct.get("MINWEIGHT") : "")
                        && (finishProduct.getProductRollWeight() != null ? finishProduct.getProductRollWeight() : "").equals(oldFinishedProduct.get("PRODUCTROLLWEIGHT") != null ? oldFinishedProduct.get("PRODUCTROLLWEIGHT") : "") && finishProduct.getCarrierCode().equals(oldFinishedProduct.get("CARRIERCODE")) && (!String.valueOf(id).equals(oldFinishedProduct.get("ID").toString()))) {
                    return ajaxError("已存在相同的产品信息！");
                }
            }
        }

        finishProduct.setId(id);
        finishProduct.setAuditState(0);
        finishProductService.update2(finishProduct);
        return GsonTools.toJson(finishProduct);
    }

    @ResponseBody
    @Journal(name = "删除成品信息", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        finishProductService.delete(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "恢复成品信息", logType = LogType.DB)
    @RequestMapping(value = "resumeFinishProduct", method = RequestMethod.POST)
    public String resumeFinishProduct(String id) throws Exception {
        finishProductService.resumeFinishProduct(Long.valueOf(id));
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "添加选择客户信息页面")
    @RequestMapping(value = "chooseconsumer", method = RequestMethod.GET)
    public String chooseConsumer() {
        return chooseConsumer;
    }

    @Journal(name = "添加选择套材/非套材BOM页面")
    @RequestMapping(value = "chooseBom", method = RequestMethod.GET)
    public String chooseBom(String productIsTc) {
        if (productIsTc.equals("1")) {
            return chooseTcBom;
        } else {
            return chooseFtcBom;
        }
    }

    @Journal(name = "添加包装BOM页面")
    @RequestMapping(value = "choosePackingBom", method = RequestMethod.GET)
    public String choosePackingBom() {
        return choosePackingBom;
    }

    @ResponseBody
    @Journal(name = "套材BOM树列表")
    @RequestMapping(value = "listTcBom")
    public String listTcBom(String data) throws SQLTemplateException {
        return GsonTools.toJson(finishProductService.tcTree(data));
    }

    @ResponseBody
    @Journal(name = "非套材BOM树列表")
    @RequestMapping(value = "listFtcBom")
    public String listFtcBom(String data) throws SQLTemplateException {
        return GsonTools.toJson(finishProductService.ftcTree(data));
    }

    @ResponseBody
    @Journal(name = "包装BOM树列表")
    @RequestMapping(value = "listBcBom")
    public String listBcBom(String data) throws SQLTemplateException {
        return GsonTools.toJson(finishProductService.bcTree(data));
    }

    @ResponseBody
    @Journal(name = "包装BOM树列表")
    @RequestMapping(value = "completeBomId")
    public String completeBomId() throws SQLTemplateException, IOException {
        finishProductService.completeBomId();
        System.out.println("完成");
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "成品作废", logType = LogType.DB)
    @RequestMapping(value = "updateS", method = RequestMethod.POST)
    public String update(Long id) throws Exception {
        finishProductService.updates(id);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查看产品信息页面")
    @RequestMapping(value = "checkProduct", method = RequestMethod.GET)
    public ModelAndView check(FinishedProduct finishProduct, Boolean copy) throws SQLTemplateException {
        finishProduct = finishProductService.findById(FinishedProduct.class, finishProduct.getId());
        //根据衬管编码查找衬管名称
        String zgmc = finishProductService.getzgmcbycode(finishProduct.getCarrierCode());
        Consumer consumer = consumerService.findById(Consumer.class, finishProduct.getProductConsumerId());
        if (finishProduct.getIsShow() == null) {
            if (copy != null && copy) {
                finishProduct.setIsShow(1);
            } else {
                finishProduct.setIsShow(0);
            }
        }
        //成品类别信息
        FinishedProductCategory productCategory = finishProduct.getFpcid() == null ? null : finishProductService.findById(FinishedProductCategory.class, finishProduct.getFpcid());
        return new ModelAndView(checkProduct, model.addAttribute("finishProduct", finishProduct).addAttribute("consumer", consumer).addAttribute("productCategory", productCategory).addAttribute("zgmc", zgmc));
    }

    @ResponseBody
    @Journal(name = "查看镜像产品信息页面")
    @RequestMapping(value = "checkProductMirror", method = RequestMethod.GET)
    public ModelAndView checkProductMirror(FinishedProductMirror finishProductMirror, Boolean copy) throws SQLTemplateException {
        finishProductMirror = finishProductService.findById(FinishedProductMirror.class, finishProductMirror.getId());
        //根据衬管编码查找衬管名称
        String zgmc = finishProductService.getzgmcbycode(finishProductMirror.getCarrierCode());
        Consumer consumer = consumerService.findById(Consumer.class, finishProductMirror.getProductConsumerId());
        if (finishProductMirror.getIsShow() == null) {
            if (copy != null && copy) {
                finishProductMirror.setIsShow(1);
            } else {
                finishProductMirror.setIsShow(0);
            }
        }
        //成品类别信息
        FinishedProductCategory productCategory = finishProductMirror.getFpcid() == null ? null : finishProductService.findById(FinishedProductCategory.class, finishProductMirror.getFpcid());
        return new ModelAndView(checkProduct, model.addAttribute("finishProduct", finishProductMirror).addAttribute("consumer", consumer).addAttribute("productCategory", productCategory).addAttribute("zgmc", zgmc));
    }

    @ResponseBody
    @Journal(name = "产品提交审核", logType = LogType.DB)
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    public String _commitAudit(Long id, String name) throws Exception {
        FinishedProduct finishedProduct = finishProductService.findById(FinishedProduct.class, id);
        if (finishedProduct.getIsCommon() == 1) {
            if (finishedProduct.getProductIsTc() == 1) {
                auditInstanceService.submitAudit(name, "CPTC", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            } else if (finishedProduct.getProductIsTc() == 2) {
                auditInstanceService.submitAudit(name, "CPFC", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            } else if (finishedProduct.getProductIsTc() == -1) {
                auditInstanceService.submitAudit(name, "CPPC", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            }
        } else {
            if (finishedProduct.getProductIsTc() == 1) {
                auditInstanceService.submitAudit(name, "CPTS", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            } else if (finishedProduct.getProductIsTc() == 2) {
                auditInstanceService.submitAudit(name, "CPFS", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            } else if (finishedProduct.getProductIsTc() == -1) {
                auditInstanceService.submitAudit(name, "CPPS", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "finishProduct/audit?id=" + id, Long.valueOf(id), FinishedProduct.class);
            }
        }
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "产品审核页面")
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView audit(String id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLTemplateException {
        FinishedProduct finishedProduct = finishProductService.findById(FinishedProduct.class, Long.valueOf(id));
        //根据衬管编码查找衬管名称
        String zgmc = finishProductService.getzgmcbycode(finishedProduct.getCarrierCode());
        FinishedProductCategory productCategory = finishedProduct.getFpcid() == null ? null : finishProductService.findById(FinishedProductCategory.class, finishedProduct.getFpcid());
        Consumer consumer = finishProductService.findById(Consumer.class, finishedProduct.getProductConsumerId());
        return new ModelAndView(audit, model.addAttribute("finishProduct", finishedProduct).addAttribute("consumer", consumer).addAttribute("productCategory", productCategory).addAttribute("zgmc", zgmc));
    }

    /**
     * 成品信息模板下载
     *
     * @throws Exception
     */
    @Journal(name = "成品信息模板下载")
    @ResponseBody
    @RequestMapping(value = "tempExport", method = RequestMethod.GET)
    public void tempExport() throws Exception {
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/finishedProductTemp.xlsx");
        Workbook wb = new XSSFWorkbook(is);
        HttpUtils.download(response, wb, "成品信息导入模板");
        is.close();
    }

    /**
     * 获取物料编号
     *
     * @throws Exception
     */
    @ResponseBody
    @Journal(name = "获取物料编号", logType = LogType.DB)
    @RequestMapping(value = "hqwlbm", method = RequestMethod.POST)
    public String hqwlbm(String lbval, String cpgg, String mf, String jc, String jz) throws Exception {
        //跟进规格查询数据
        List<Map<String, Object>> gglist = finishProductService.queryGGinfo(lbval, cpgg);
        //规格流水号
        String cpgglsh = "";
        //门幅流水号
        String mflsh = "";
        //卷长流水号
        String jclsh = "";
        if (gglist.size() == 0) {
            //重新生成流水号
            int ggxhs = finishProductService.queryCpggInfo(lbval) + 1;
            if (ggxhs < 10) {
                cpgglsh = "000" + ggxhs;
            } else if (ggxhs < 100) {
                cpgglsh = "00" + ggxhs;
            } else if (ggxhs < 1000) {
                cpgglsh = "0" + ggxhs;
            } else {
                cpgglsh = ggxhs + "";
            }
        } else {
            //取原来的流水号
            String[] lshs = gglist.get(0).get("MATERIELCODE").toString().split("\\.");
            for (String lsh : lshs) {
                if (lsh.length() == 4) {
                    cpgglsh = lsh;
                    break;
                }
            }
        }

        String wlbh = lbval + "." + cpgglsh;
        //查询门幅信息
        List<Map<String, Object>> mflist = finishProductService.queryMfinfo(wlbh, mf);
        if (mflist.size() == 0) {
            //新流水号
            int mflshs = finishProductService.queryMfnewcode(wlbh) + 1;
            if (mflshs < 10) {
                mflsh = "0" + mflshs;
            } else {
                mflsh = mflshs + "";
            }
        } else {
            //取原来的流水号
            String[] mflshs = mflist.get(0).get("MATERIELCODE").toString().split("\\.");
            //数组倒数第二个元素
            mflsh = mflshs[mflshs.length - 2];
        }
        wlbh = wlbh + "." + mflsh;
        //如jz有，jc没有
        if (!"".equals(jz) && "".equals(jc)) {
            //查询卷重信息
            List<Map<String, Object>> jzlist = finishProductService.queryJZinfo(wlbh, jz);
            if (jzlist.size() == 0) {
                //取新的流水号
                int jzlshs = finishProductService.queryJznewcode(wlbh) + 1;
                if (jzlshs < 10) {
                    jclsh = "00" + jzlshs;
                } else if (jzlshs < 100) {
                    jclsh = "0" + jzlshs;
                } else {
                    jclsh = jzlshs + "";
                }
            } else {
                //取原来的流水号
                String[] jzlshs = jzlist.get(0).get("MATERIELCODE").toString().split("\\.");
                //数组倒数第二个元素
                jclsh = jzlshs[jzlshs.length - 1];
            }
        }

        //jz无，jc有
        if ("".equals(jz) && !"".equals(jc)) {
            //查询卷长信息
            List<Map<String, Object>> jclist = finishProductService.queryJCinfo(wlbh, jc);
            if (jclist.size() == 0) {
                //取新的流水号
                int jzlshs = finishProductService.queryJcnewcode(wlbh) + 1;
                if (jzlshs < 10) {
                    jclsh = "00" + jzlshs;
                } else if (jzlshs < 100) {
                    jclsh = "0" + jzlshs;
                } else {
                    jclsh = jzlshs + "";
                }
            } else {
                //取原来的流水号
                String[] jzlshs = jclist.get(0).get("MATERIELCODE").toString().split("\\.");
                //数组倒数第二个元素
                jclsh = jzlshs[jzlshs.length - 1];
            }
        }

        //jz jc都有
        if (!"".equals(jz) && !"".equals(jc)) {
            //查询卷长和jc信息
            List<Map<String, Object>> jzclist = finishProductService.queryJZCinfo(wlbh, jz, jc);
            if (jzclist.size() == 0) {
                //取新的流水号
                int jzlshs = finishProductService.queryJzcnewcode(wlbh) + 1;
                if (jzlshs < 10) {
                    jclsh = "00" + jzlshs;
                } else if (jzlshs < 100) {
                    jclsh = "0" + jzlshs;
                } else {
                    jclsh = jzlshs + "";
                }
            } else {
                //取原来的流水号
                String[] jzlshs = jzclist.get(0).get("MATERIELCODE").toString().split("\\.");
                //数组倒数第二个元素
                jclsh = jzlshs[jzlshs.length - 1];
            }
        }
        wlbh = wlbh + "." + jclsh;
        return GsonTools.toJson(wlbh);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查看叶型信息")
    @RequestMapping("viewYxInfo")
    public ModelAndView getYxInfo(String id) throws Exception {
        return new ModelAndView(viewYxInfo, model.addAttribute("yxdates", GsonTools.toJson(finishProductService.checkYxInfo(id))));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查看叶型信息")
    @RequestMapping("queryYxInfo")
    public ModelAndView queryYxInfo(long id) throws Exception {
        return new ModelAndView(viewYxInfo, model.addAttribute("yxdates", GsonTools.toJson(finishProductService.queryYxInfo(id))));
    }

    @ResponseBody
    @Journal(name = "计算工艺BOM中的单位面积质量", logType = LogType.CONSOLE)
    @RequestMapping(value = "calcWeight", method = RequestMethod.POST)
    @Valid
    public String calcWeight(String procBomId) {
        double bomWeight = 0d;
        List<FtcBomDetail> bomDetails;
        Map<String, Object> map = new HashMap<>();
        map.put("ftcBomVersionId", Long.parseLong(procBomId));
        bomDetails = finishProductService.findListByMap(FtcBomDetail.class, map);
        for (FtcBomDetail d : bomDetails) {
            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
        }
        return GsonTools.toJson(bomWeight);
    }
}
