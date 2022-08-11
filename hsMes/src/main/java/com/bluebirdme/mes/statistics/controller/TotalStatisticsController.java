/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合统计Controller
 *
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */
@Controller
@RequestMapping("/totalStatistics")
@Journal(name = "综合统计")
public class TotalStatisticsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TotalStatisticsController.class);
    /**
     * 综合统计页面
     */
    final String index = "statistics/totalStatistics";
    final String addOrEdit = "statistics/totalStatisticsAddOrEdit";
    final String dailyIndex = "statistics/dailyStatistics";
    final String lockPage = "statistics/totalStatisticsLock";
    final String judgepage = "statistics/totalStatisticsQualityGrade";
    final String changeBarcodePage = "statistics/produceInfo";
    @Resource
    ITotalStatisticsService totalStatisticsService;
    @Resource
    IMobileService mService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @Journal(name = "日生产首页")
    @RequestMapping(value = "daily", method = RequestMethod.GET)
    public String dailyIndex() {
        return dailyIndex;
    }

    @Journal(name = "修改条码重量页面")
    @RequestMapping(value = "changeBarcodePage", method = RequestMethod.GET)
    public String changeBarcodePage() {
        return changeBarcodePage;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取综合统计列表信息")
    @RequestMapping("list")
    public String getTotalStatistics(Filter filter, Page page, String searchType) throws Exception {
        if (searchType != null) {
            if ((searchType.equals("roll")) && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByRoll(filter, page));
            } else if (searchType.equals("box") && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByBox(filter, page));
            } else if (searchType.equals("tray") && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByTray(filter, page));
            } else if (searchType.equals("part") && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByPart(filter, page));
            } else if (filter.get("rollBarcode") != null && (filter.get("rollBarcode").startsWith("like:R") || filter.get("rollBarcode").startsWith("P"))) {
                return GsonTools.toJson(totalStatisticsService.findPageInfo(filter, page));
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:B")) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByBox(filter, page));
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:T")) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByTray(filter, page));
            }
        }
        return GsonTools.toJson(totalStatisticsService.findPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取综合统计列表信息")
    @RequestMapping("list1")
    public String getTotalStatistics1(Filter filter, Page page, String searchType) throws Exception {
        if (searchType != null) {
            if ((searchType.equals("roll")) && filter.get("rollBarcode") == null) {
                Map<String, Object> findPageInfoByRoll = totalStatisticsService.findPageInfoByRoll(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfoByRoll.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        Object rollbarcode = row.get("ROLLBARCODE");
                        row.put("ROLLCOUNT", 1);
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
                return GsonTools.toJson(findPageInfoByRoll);
            } else if (searchType.equals("box") && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByBox(filter, page));
            } else if (searchType.equals("tray") && filter.get("rollBarcode") == null) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByTray(filter, page));
            } else if (searchType.equals("part") && filter.get("rollBarcode") == null) {
                Map<String, Object> findPageInfoByPart = totalStatisticsService.findPageInfoByPart(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfoByPart.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        Object rollbarcode = row.get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
                return GsonTools.toJson(findPageInfoByPart);
            } else if (filter.get("rollBarcode") != null && (filter.get("rollBarcode").startsWith("like:R") || filter.get("rollBarcode").startsWith("like:P"))) {
                Map<String, Object> findPageInfo = totalStatisticsService.findPageInfo(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        row.put("rollCount", 1);
                        Object rollbarcode = row.get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
                return GsonTools.toJson(findPageInfo);
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:B")) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByBox(filter, page));
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:T")) {
                return GsonTools.toJson(totalStatisticsService.findPageInfoByTray(filter, page));
            }
        }
        Map<String, Object> findPageInfo = totalStatisticsService.findPageInfo(filter, page);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        if (rows.size() > 0) {
            for (Map<String, Object> row : rows) {
                Object rollbarcode = row.get("ROLLBARCODE");
                if (rollbarcode.toString().indexOf("T") != 0 && rollbarcode.toString().indexOf("B") != 0 && rollbarcode.toString().indexOf("P") != 0) {
                    row.put("ROLLCOUNT", 1);
                }
                if (rollbarcode.toString().indexOf("T") != 0 && rollbarcode.toString().indexOf("B") != 0) {
                    // 查询该条码是否成托
                    String topBarcode = TopBarcode(rollbarcode.toString());
                    if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                        row.put("ISPACKED", 1);
                    } else {
                        row.put("ISPACKED", 0);
                    }
                }
            }
        }
        return GsonTools.toJson(findPageInfo);
    }

    @Journal(name = "添加综合统计页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(TotalStatistics totalStatistics) {
        return new ModelAndView(addOrEdit, model.addAttribute("totalStatistics", totalStatistics));
    }

    @ResponseBody
    @Journal(name = "保存综合统计", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(TotalStatistics totalStatistics) throws Exception {
        totalStatisticsService.save(totalStatistics);
        return GsonTools.toJson(totalStatistics);
    }

    @Journal(name = "编辑综合统计页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(TotalStatistics totalStatistics) {
        totalStatistics = totalStatisticsService.findById(TotalStatistics.class, totalStatistics.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("totalStatistics", totalStatistics));
    }

    @ResponseBody
    @Journal(name = "编辑综合统计", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(TotalStatistics totalStatistics) throws Exception {
        totalStatisticsService.update(totalStatistics);
        return GsonTools.toJson(totalStatistics);
    }

    @ResponseBody
    @Journal(name = "删除综合统计", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        totalStatisticsService.delete(TotalStatistics.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    // 拦截请求存储追溯类型9的投诉信息
    @ResponseBody
    @Journal(name = "冻结产品", logType = LogType.DB)
    @RequestMapping(value = "lock", method = RequestMethod.POST)
    public String lock(String ids, String complaintCode, String reasons) {
        List<TotalStatistics> list = totalStatisticsService.getByIds(ids);
        if (list.size() > 0) {
            for (TotalStatistics totalStatistics : list) {
                totalStatistics.setIsNameLock(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
            }
        }
        totalStatisticsService.saveLockState(list, complaintCode, reasons);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "解冻产品", logType = LogType.DB)
    @RequestMapping(value = "unLock", method = RequestMethod.POST)
    public String unLock(String ids, String complaintCode, String reasons) {
        List<TotalStatistics> list = totalStatisticsService.getByIds(ids);
        totalStatisticsService.saveUnLockState(list, complaintCode, reasons);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "添加综合统计页面")
    @RequestMapping(value = "lockpage", method = RequestMethod.GET)
    public ModelAndView lockpage(String ids, String type) {
        return new ModelAndView(lockPage, model.addAttribute("ids", ids).addAttribute("type", type));
    }

    @Journal(name = "质量判级页面")
    @RequestMapping(value = "judgepage", method = RequestMethod.GET)
    public ModelAndView judgepage(String ids, String type) {
        return new ModelAndView(judgepage, model.addAttribute("ids", ids).addAttribute("type", type));
    }

    @ResponseBody
    @Journal(name = "获取产品树状图列表信息")
    @RequestMapping("listProduct")
    public String listProduct(String barcode) {
        String result = "";
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        HashMap<String, Object> partMap = new HashMap<>();
        partMap.put("partBarcode", barcode);
        // 查询出对应的箱条码信息，如果没有，查询托条码信息
        BoxRoll part = totalStatisticsService.findUniqueByMap(BoxRoll.class, partMap);
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("rollBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("P") && part != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            // 根据关系查询出顶级节点
            map.clear();
            map.put("boxBarcode", part.getBoxBarcode());
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            } else {
                topBarcode = part.getBoxBarcode();
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }

        // 如果顶级条码号B开头，查询卷箱关系补充卷条码在树状结构
        if (topBarcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", topBarcode);
            List<BoxRoll> boxrollList = totalStatisticsService.findListByMap(BoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<>();
            for (BoxRoll br : boxrollList) {
                ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                pdInfo.setId(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                pdInfo.setText(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                pdInfo.setState("closed");
                childrenList.add(pdInfo);
            }
            pdInfoStruct.setChildren(childrenList);
            result = "[" + GsonTools.toJson(pdInfoStruct) + "]";
        }
        // 如果顶级条码号T开头，查询托箱卷关系，补充箱条码和卷条码和箱条码所包含的卷条码信息，组织树状结构
        else if (topBarcode.startsWith("T") || topBarcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("trayBarcode", topBarcode);
            List<TrayBoxRoll> tbrList = totalStatisticsService.findListByMap(TrayBoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<>();
            for (TrayBoxRoll tbr : tbrList) {
                if (tbr.getRollBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getRollBarcode());
                    pdInfo.setText(tbr.getRollBarcode());
                    pdInfo.setState("closed");
                    childrenList.add(pdInfo);
                }
                if (tbr.getPartBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getPartBarcode());
                    pdInfo.setText(tbr.getPartBarcode());
                    pdInfo.setState("closed");
                    childrenList.add(pdInfo);
                }
                if (tbr.getBoxBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getBoxBarcode());
                    pdInfo.setText(tbr.getBoxBarcode());
                    pdInfo.setState("closed");
                    List<ProductInfoTreeStruct> boxchildrenList = new ArrayList<>();
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brlist = totalStatisticsService.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brlist) {
                        ProductInfoTreeStruct boxPdInfo = new ProductInfoTreeStruct();
                        boxPdInfo.setId(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                        boxPdInfo.setText(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                        boxPdInfo.setState("closed");
                        boxchildrenList.add(boxPdInfo);
                    }
                    pdInfo.setChildren(boxchildrenList);
                    childrenList.add(pdInfo);
                }
            }
            pdInfoStruct.setChildren(childrenList);
            result = "[" + GsonTools.toJson(pdInfoStruct) + "]";
        }
        // 如果顶级条码以R/P开头，表示没有父级条码，结构为其本身
        else {
            ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
            pdInfo.setId(barcode);
            pdInfo.setText(barcode);
            pdInfo.setState("closed");
            result = "[" + GsonTools.toJson(pdInfo) + "]";
        }
        result = result.toLowerCase();
        return result;
    }

    @ResponseBody
    @Journal(name = "获取卷条码信息")
    @RequestMapping(value = "productInfo", method = RequestMethod.POST)
    public String productInfo(String barcode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rollBarcode", barcode);
        TotalStatistics ts = totalStatisticsService.findUniqueByMap(TotalStatistics.class, map);
        return GsonTools.toJson(ts);
    }

    @ResponseBody
    @Journal(name = "修改卷条码重量", logType = LogType.DB)
    @RequestMapping(value = "changeInfo", method = RequestMethod.POST)
    public String changeInfo(String barcode, String parentBarocde, String topBarcode, Double newWeight) throws Exception {
        if (barcode.startsWith("T") || barcode.startsWith("B") || barcode.startsWith("PHS")) {
            return ajaxError("只能修改卷重,部件重量");
        }
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", barcode);
            Roll roll = totalStatisticsService.findUniqueByMap(Roll.class, map);
            if (roll.getRollWeight() == null) {
                return ajaxError("没有称重，请先称重！");
            }
        }
        totalStatisticsService.changeInfo(barcode, parentBarocde, topBarcode, newWeight);
        return GsonTools.toJson("修改成功");
    }

    @NoLogin
    @RequestMapping(value = "up111", method = RequestMethod.GET)
    public String updateTotalStaticsModel() {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("productModel", null);
        List<Tray> li = totalStatisticsService.findListByMap(Tray.class, map1);
        for (Tray ty : li) {
            if (ty.getProductModel() != null) {
                continue;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("trayBarcode", ty.getTrayBarcode());
            List<TrayBoxRoll> tbrList = totalStatisticsService.findListByMap(TrayBoxRoll.class, map);
            if (tbrList.size() > 0) {
                TrayBoxRoll tbr = tbrList.get(0);
                String b = "";
                if (tbr.getRollBarcode() != null) {
                    b = tbr.getRollBarcode();
                } else if (tbr.getBoxBarcode() != null) {
                    b = tbr.getBoxBarcode();
                } else {
                    b = tbr.getPartBarcode();
                }

                map.clear();
                map.put("rollBarcode", b);
                TotalStatistics ts = totalStatisticsService.findUniqueByMap(TotalStatistics.class, map);
                ty.setProductModel(ts.getProductModel());
                totalStatisticsService.update(ty);
                map.clear();
                map.put("rollBarcode", ty.getTrayBarcode());
                TotalStatistics ts1 = null;
                try {
                    ts1 = totalStatisticsService.findUniqueByMap(TotalStatistics.class, map);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage() + ty.getTrayBarcode(), e);
                }
                ts1.setProductModel(ts.getProductModel());
                totalStatisticsService.update(ts1);
            }
        }
        return GsonTools.toJson("修改成功");
    }

    @ResponseBody
    @Journal(name = "质量判级", logType = LogType.DB)
    @RequestMapping(value = "judge", method = RequestMethod.POST)
    public String judge(String ids, String qualityGrade) throws Exception {
        totalStatisticsService.judge(ids, qualityGrade);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "修改米长", logType = LogType.DB)
    @RequestMapping(value = "changeMeterInfo", method = RequestMethod.POST)
    public String changeMeterInfo(String rollbarcode, Double meter) {
        Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, rollbarcode);
        roll.setRollRealLength(meter);
        mService.update(roll);
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "修改备注")
    @RequestMapping(value = "changeMemoInfo", method = RequestMethod.POST)
    public String changeMemoInfo(String barcode, String memo) {
        if (barcode.startsWith("R") || barcode.startsWith("P")) {
            Roll roll = mService.findBarCodeReg(BarCodeRegType.ROLL, barcode);
            roll.setMemo(memo);
            mService.update(roll);
        } else if (barcode.startsWith("B")) {
            Box box = mService.findBarCodeReg(BarCodeRegType.BOX, barcode);
            box.setMemo(memo);
            mService.update(box);
        } else if (barcode.startsWith("T")) {
            Tray tray = mService.findBarCodeReg(BarCodeRegType.TRAY, barcode);
            tray.setMemo(memo);
            mService.update(tray);
        }
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "查询最顶级的条码")
    @RequestMapping("TopBarcode")
    public String TopBarcode(String barcode) {
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("rollBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("partBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }
        return topBarcode;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "导出生产统计Excel明细2", logType = LogType.DB)
    @RequestMapping(value = "exportDailyStatistics")
    public void exportDailyStatistics(Filter filter, String searchType) throws Exception {
        SXSSFWorkbook wb = totalStatisticsService.exportDailyStatistics(filter, searchType);
        HttpUtils.download(response, wb, "生产统计表");
    }

    @NoAuth
    @Journal(name = "导出生产统计Excel明细")
    @ResponseBody
    @RequestMapping(value = "export1")
    public void export1(String rollBarcode, String searchType, String CONSUMERNAME, String name, String batchCode, String productModel, String rollqualitygradecode, String deviceCode, String salesOrderCode, String state, String start, String end, String loginName, String producePlanCode, String isAbandon, String isOpened) throws Exception {
        Page page = new Page();
        page.setAll(1);
        page.setRows(999);
        Filter filter = new Filter();
        HashMap<String, String> filterMap = new HashMap<>();
        if (rollBarcode != null) {
            filterMap.put("rollBarcode", "like:" + rollBarcode);
        }

        if (searchType != null) {
            filterMap.put("searchType", searchType);
        }

        if (CONSUMERNAME != null) {
            filterMap.put("CONSUMERNAME", "like:" + CONSUMERNAME);
        }

        if (name != null) {
            filterMap.put("name", name);
        }

        if (batchCode != null) {
            filterMap.put("batchCode", "like:" + batchCode);
        }

        if (productModel != null) {
            filterMap.put("productModel", "like:" + productModel);
        }

        if (rollqualitygradecode != null) {
            filterMap.put("rollqualitygradecode", "like:" + rollqualitygradecode);
        }

        if (deviceCode != null) {
            filterMap.put("deviceCode", deviceCode);
        }

        if (salesOrderCode != null) {
            filterMap.put("salesOrderCode", "like:" + salesOrderCode);
        }

        if (state != null) {
            filterMap.put("state", state);
        }

        if (start != null) {
            filterMap.put("start", start);
        }

        if (end != null) {
            filterMap.put("end", end);
        }

        if (loginName != null) {
            filterMap.put("loginName", loginName);
        }

        if (producePlanCode != null) {
            filterMap.put("producePlanCode", "like:" + producePlanCode);
        }

        if (isAbandon != null) {
            filterMap.put("isAbandon", isAbandon);
        }

        if (isOpened != null) {
            filterMap.put("isOpened", isOpened);
        }

        filter.setFilter(filterMap);
        Map<String, Object> findPageInfo = new HashMap<>();
        if (filter.get("searchType") != null) {
            if (filter.get("searchType").equals("roll") && filter.get("rollBarcode") == null) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByRoll(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        Object rollbarcode = row.get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
            } else if (filter.get("searchType").equals("box") && filter.get("rollBarcode") == null) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByBox(filter, page);
            } else if (filter.get("searchType").equals("tray") && filter.get("rollBarcode") == null) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByTray(filter, page);
            } else if (filter.get("searchType").equals("part") && filter.get("rollBarcode") == null) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByPart(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (int i = 0; i < rows.size(); i++) {
                        Object rollbarcode = rows.get(i).get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0) {
                                rows.get(i).put("ISPACKED", 1);
                            } else {
                                rows.get(i).put("ISPACKED", 0);
                            }
                        }
                    }
                }
            } else if (filter.get("rollBarcode") != null && (filter.get("rollBarcode").startsWith("like:R") || filter.get("rollBarcode").startsWith("like:P"))) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfo(filter, page);
                List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
                if (rows.size() > 0) {
                    for (Map<String, Object> row : rows) {
                        Object rollbarcode = row.get("ROLLBARCODE");
                        if (rollbarcode != null) {
                            // 查询该条码是否成托
                            String topBarcode = TopBarcode(rollbarcode.toString());
                            if (topBarcode.indexOf("T") == 0) {
                                row.put("ISPACKED", 1);
                            } else {
                                row.put("ISPACKED", 0);
                            }
                        }
                    }
                }
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:B")) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByBox(filter, page);
            } else if (filter.get("rollBarcode") != null && filter.get("rollBarcode").startsWith("like:T")) {
                filter.set("searchType", null);
                filter.clear();
                findPageInfo = totalStatisticsService.findPageInfoByTray(filter, page);
            }
        } else {
            filter.clear();
            findPageInfo = totalStatisticsService.findPageInfo(filter, page);
        }

        List<Map<String, Object>> rows = (List) findPageInfo.get("rows");
        if (rows.size() > 0) {
            for (Map<String, Object> row : rows) {
                Object rollbarcode = row.get("ROLLBARCODE");
                if (rollbarcode != null) {
                    if (rollbarcode.toString().indexOf("T") != 0 && rollbarcode.toString().indexOf("B") != 0) {
                        // 查询该条码是否成托
                        String topBarcode = TopBarcode(rollbarcode.toString());
                        if (topBarcode.indexOf("T") == 0 || topBarcode.indexOf("P") == 0) {
                            row.put("ISPACKED", 1);
                        } else {
                            row.put("ISPACKED", 0);
                        }
                    }
                }
            }
        }
        String templateName = "生产统计表";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Integer xx = null;
        Row row;
        Cell cell;
        String[] columnName = new String[]{"条码号", "条码类型", "计划单号", "订单号", "客户名称", "产品规格", "批次号", "质量等级", "机台号", "车间", "卷重（kg）", "门幅（mm）",
                "称重重量（kg）", "卷长（m）", "实际卷长（m）", "卷数", "生产时间", "操作人", "库存状态",
                "状态", "是否作废", "是否打包", "是否拆包", "备注"};
        int r = 0;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 24; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 23));
        r++;
        row = sheet.createRow(r);
        row.createCell(0);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 25 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 10 * 256);
        sheet.setColumnWidth(13, 10 * 256);
        sheet.setColumnWidth(14, 10 * 256);
        sheet.setColumnWidth(15, 10 * 256);
        sheet.setColumnWidth(16, 10 * 256);
        sheet.setColumnWidth(17, 10 * 256);
        sheet.setColumnWidth(18, 10 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 10 * 256);
        sheet.setColumnWidth(21, 10 * 256);
        sheet.setColumnWidth(22, 10 * 256);
        sheet.setColumnWidth(23, 10 * 256);
        row = sheet.createRow(r);
        row.createCell(0);
        for (Map<String, Object> stringObjectMap : rows) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(stringObjectMap.get("ROLLBARCODE").toString());
                        break;
                    case 1:
                        if (stringObjectMap.get("BARCODETYPE").equals("tray")) {
                            cell.setCellValue("托条码");
                        } else if (stringObjectMap.get("BARCODETYPE").equals("box")) {
                            cell.setCellValue("箱条码");
                        } else {
                            cell.setCellValue("卷条码");
                        }
                        break;
                    case 2:
                        if (stringObjectMap.get("PRODUCEPLANCODE") != null) {
                            cell.setCellValue(stringObjectMap.get("PRODUCEPLANCODE")
                                    .toString());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 3:
                        cell.setCellValue(stringObjectMap.get("SALESORDERCODE").toString());
                        break;
                    case 4:
                        cell.setCellValue(stringObjectMap.get("CONSUMERNAME").toString());
                        break;
                    case 5:
                        if (stringObjectMap.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(stringObjectMap.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 6:
                        cell.setCellValue(stringObjectMap.get("BATCHCODE").toString());
                        break;
                    case 7:
                        cell.setCellValue(stringObjectMap.get("ROLLQUALITYGRADECODE").toString());
                        break;
                    case 8:
                        if (stringObjectMap.get("DEVICECODE") != null) {
                            cell.setCellValue(stringObjectMap.get("DEVICECODE").toString());
                        }
                        break;
                    case 9:
                        cell.setCellValue(stringObjectMap.get("NAME").toString());
                        break;
                    case 10:
                        if (stringObjectMap.get("ROLLWEIGHT") != null) {
                            cell.setCellValue((Double) stringObjectMap.get("ROLLWEIGHT"));
                        }
                        break;
                    case 11:
                        if (stringObjectMap.get("PRODUCTWIDTH") != null) {
                            cell.setCellValue((Double) stringObjectMap.get("PRODUCTWIDTH"));
                        }
                        break;
                    case 12:
                        if (stringObjectMap.get("PRODUCTWEIGHT") != null) {
                            cell.setCellValue((Double) stringObjectMap.get("PRODUCTWEIGHT"));
                        }
                        break;
                    case 13:
                        if (stringObjectMap.get("PRODUCTLENGTH") != null) {
                            cell.setCellValue((Double) stringObjectMap.get("PRODUCTLENGTH"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 14:
                        if (stringObjectMap.get("ROLLREALLENGTH") != null) {
                            cell.setCellValue((Double) stringObjectMap.get("ROLLREALLENGTH"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 15:
                        if (stringObjectMap.get("ROLLBARCODE").toString().startsWith("R")) {
                            cell.setCellValue(1);
                        }
                        if (stringObjectMap.get("ROLLCOUNT") != null) {
                            cell.setCellValue(stringObjectMap.get("ROLLCOUNT").toString());
                        }
                        break;
                    case 16:
                        if (stringObjectMap.get("ROLLOUTPUTTIME") != null) {
                            cell.setCellValue(stringObjectMap.get("ROLLOUTPUTTIME").toString());
                        }
                        break;
                    case 17:
                        if (stringObjectMap.get("LOGINNAME") != null) {
                            cell.setCellValue(stringObjectMap.get("LOGINNAME").toString());
                        }
                        break;
                    case 18:
                        if (stringObjectMap.get("STATE") == null || xx.parseInt(stringObjectMap.get("STATE").toString()) == 0) {
                            cell.setCellValue("未入库");
                            break;
                        }
                        if (xx.parseInt(stringObjectMap.get("STATE").toString()) == 1) {
                            cell.setCellValue("在库");
                        } else if (xx.parseInt(stringObjectMap.get("STATE").toString()) == -1) {
                            cell.setCellValue("出库");
                        }
                        break;
                    case 19:
                        if (stringObjectMap.get("ISLOCKED") == "1") {
                            cell.setCellValue("冻结");
                        } else {
                            cell.setCellValue("正常");
                        }
                        break;
                    case 20:
                        if (stringObjectMap.get("ISABANDON") == null) {
                            cell.setCellValue("正常");
                        } else if (xx.parseInt(stringObjectMap.get("ISABANDON").toString()) == 0) {
                            cell.setCellValue("正常");
                        } else if (xx.parseInt(stringObjectMap.get("ISABANDON").toString()) == 1) {
                            cell.setCellValue("已作废");
                        }
                        break;
                    case 21:
                        if (stringObjectMap.get("ISPACKED") != null) {
                            if (xx.parseInt(stringObjectMap.get("ISPACKED").toString()) == 1) {
                                cell.setCellValue("已打包");
                            } else if (xx.parseInt(stringObjectMap.get("ISPACKED").toString()) == 0) {
                                cell.setCellValue("未打包");
                            }
                        }
                        break;
                    case 22:
                        if (stringObjectMap.get("ROLLBARCODE").toString().indexOf("T") == 0 || stringObjectMap.get("ROLLBARCODE").toString().indexOf("P") == 0) {
                            if (stringObjectMap.get("ISOPENED") == null) {
                                cell.setCellValue("正常");
                            } else if (xx.parseInt(stringObjectMap.get("ISOPENED").toString()) == 0) {
                                cell.setCellValue("正常");
                            } else if (xx.parseInt(stringObjectMap.get("ISOPENED").toString()) == 1) {
                                cell.setCellValue("已拆包");
                            }
                        }
                        break;
                    case 23:
                        if (stringObjectMap.get("MEMO") != null) {
                            cell.setCellValue(stringObjectMap.get("MEMO").toString());
                        }
                        break;
                }
            }
            r++;
        }
        row = sheet.createRow(r);
        row.createCell(0);
        HttpUtils.download(response, wb, templateName);
    }

    /**
     * 托盒卷关系导出到Excel
     */
    @Journal(name = "Excel导出")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(String barcode) throws Exception {
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("rollBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(
                        TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            if (br != null) {
                map.clear();
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.clear();
                map.put("partBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }
        ProductInfoTreeStruct result = null;
        // 如果顶级条码号B开头，查询卷箱关系补充卷条码在树状结构
        if (topBarcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boxBarcode", topBarcode);
            List<BoxRoll> boxrollList = totalStatisticsService.findListByMap(BoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<>();
            for (BoxRoll br : boxrollList) {
                ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                pdInfo.setId(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                pdInfo.setText(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                pdInfo.setState("closed");
                childrenList.add(pdInfo);
            }
            pdInfoStruct.setChildren(childrenList);
            result = pdInfoStruct;
        }
        // 如果顶级条码号T开头，查询托箱卷关系，补充箱条码和卷条码和箱条码所包含的卷条码信息，组织树状结构
        else if (topBarcode.startsWith("T")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("trayBarcode", topBarcode);
            List<TrayBoxRoll> tbrList = totalStatisticsService.findListByMap(TrayBoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<>();
            for (TrayBoxRoll tbr : tbrList) {
                if (tbr.getRollBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getRollBarcode());
                    pdInfo.setText(tbr.getRollBarcode());
                    pdInfo.setState("closed");
                    childrenList.add(pdInfo);
                }
                if (tbr.getPartBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getPartBarcode());
                    pdInfo.setText(tbr.getPartBarcode());
                    pdInfo.setState("closed");
                    childrenList.add(pdInfo);
                }
                if (tbr.getBoxBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getBoxBarcode());
                    pdInfo.setText(tbr.getBoxBarcode());
                    pdInfo.setState("closed");
                    List<ProductInfoTreeStruct> boxchildrenList = new ArrayList<ProductInfoTreeStruct>();
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brlist = totalStatisticsService.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brlist) {
                        ProductInfoTreeStruct boxPdInfo = new ProductInfoTreeStruct();
                        boxPdInfo.setId(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                        boxPdInfo.setText(br.getRollBarcode() != null ? br.getRollBarcode() : br.getPartBarcode());
                        boxPdInfo.setState("closed");
                        boxchildrenList.add(boxPdInfo);
                    }
                    pdInfo.setChildren(boxchildrenList);
                    childrenList.add(pdInfo);
                }
            }
            pdInfoStruct.setChildren(childrenList);
            result = pdInfoStruct;
        }
        // 如果顶级条码以R/P开头，表示没有父级条码，结构为其本身
        else {
            ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
            pdInfo.setId(barcode);
            pdInfo.setText(barcode);
            pdInfo.setState("closed");
            result = pdInfo;
        }

        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/trayBoxRoll.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        int i = 1;

        String first = result.getText();
        if (first.startsWith("T")) {//托条码
            List<ProductInfoTreeStruct> p1s = result.getChildren();
            if (p1s != null) {
                for (ProductInfoTreeStruct p1 : p1s) {
                    String box = p1.getText();
                    List<ProductInfoTreeStruct> p2s = p1.getChildren();
                    if (p2s != null) {//卷-->箱-->托
                        for (ProductInfoTreeStruct p2 : p2s) {
                            String roll = p2.getText();
                            row = sheet.createRow(i++);
                            Cell cell1 = row.createCell(0);//第一列
                            Cell cell2 = row.createCell(1);//第二列
                            Cell cell3 = row.createCell(2);//第三列
                            cell1.setCellValue(first);
                            cell2.setCellValue(box);
                            cell3.setCellValue(roll);
                        }
                    } else {//卷-->托
                        row = sheet.createRow(i++);
                        Cell cell1 = row.createCell(0);//第一列
                        Cell cell2 = row.createCell(2);//第三列
                        cell1.setCellValue(first);
                        cell2.setCellValue(box);
                    }
                }
            }
        } else if (first.startsWith("B")) {//箱条码
            List<ProductInfoTreeStruct> p1s = result.getChildren();
            if (p1s != null) {//卷-->箱
                for (ProductInfoTreeStruct p1 : p1s) {
                    String second = p1.getText();
                    row = sheet.createRow(i++);
                    Cell cell1 = row.createCell(1);//第二列
                    Cell cell2 = row.createCell(2);//第三列
                    cell1.setCellValue(first);
                    cell2.setCellValue(second);
                }
            }
        } else if (first.startsWith("R")) {//卷条码
            row = sheet.createRow(i++);
            Cell cell1 = row.createCell(2);//第三列
            cell1.setCellValue(first);
        }
        HttpUtils.download(response, wb, "托合卷关系");
        is.close();
    }
}
