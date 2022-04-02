/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.siemens.bom.dao.IFragmentDao;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.siemens.bom.entity.Grid;
import com.bluebirdme.mes.siemens.bom.service.IFragmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author 高飞
 * @Date 2017-7-19 16:13:04
 */
@Service
@AnyExceptionRollback
public class FragmentServiceImpl extends BaseServiceImpl implements IFragmentService {
    @Resource
    IFragmentDao fragmentDao;

    @Override
    protected IBaseDao getBaseDao() {
        return fragmentDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return fragmentDao.findPageInfo(filter, page);
    }

    public List<Fragment> fragmentList(Long tcBomId) {
        return fragmentDao.fragmentList(tcBomId);
    }

    public void saveFragmentList(Grid<Fragment> grid) {
        saveList(grid.getInserted());
        for (Fragment fg : grid.getDeleted()) {
            fg.setIsDeleted(1);
            update(fg);
        }

        for (Fragment fg : grid.getUpdated()) {
            update(fg);
        }
    }

    /**
     * 判断字符串是不是数字
     *
     * @param str
     * @return
     */
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 西门子裁片Excel导入保存
     */
    @Override
    public ExcelImportMessage fragmentImport(Long fileId, Long tcBomId) throws Exception {
        ExcelImportMessage msg = new ExcelImportMessage();// 存放错误提示信息
        if (fileId != null) {
            Map<String, Object> map = new HashMap();
            map.put("tcBomId", tcBomId);
            Attachment att = fragmentDao.findById(Attachment.class, fileId);
            if (att != null) {
                String filePath = att.getFilePath();
                List<Fragment> fragmentList = new ArrayList();
                // 读取文件
                InputStream is = new FileInputStream(filePath);
                Workbook wb = WorkbookFactory.create(is);
                Sheet sheet = wb.getSheetAt(1); // 获取第2个sheet
                int fa = -1;// 成品信息 标题所在的行
                int fb = -1;// 图纸文件信息 标题所在的行
                Map<String, Integer> titleIndexMap = new LinkedHashMap<>();
                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Cell cellt = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (StringUtils.equals(cellt.toString(), "成品信息")) {
                            fa = i;
                        }
                        if (StringUtils.equals(cellt.toString(), "图纸文件信息")) {
                            fb = i;
                        }
                        if (fa + 2 == i && fa != -1) {// 标题
                            // 标题
                            for (int j = 0; j < row.getLastCellNum(); j++) {
                                if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "子物料号")) {
                                    titleIndexMap.put("fragmentCode", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "坯布规格")) {
                                    titleIndexMap.put("farbicModel", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "产品名称/层号")) {
                                    titleIndexMap.put("fragmentName", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "重量/kg")) {
                                    titleIndexMap.put("fragmentWeight", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "卷/套")) {
                                    titleIndexMap.put("fragmentCountPerDrawings", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "门幅/mm")) {
                                    titleIndexMap.put("fragmentWidth", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "米长/m")) {
                                    titleIndexMap.put("fragmentLength", j);
                                } else if (StringUtils.startsWith(row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "备注")) {
                                    titleIndexMap.put("fragmentMemo", j);
                                }
                            }

                            // 检查标题
                            if (!titleIndexMap.containsKey("fragmentCode")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"子物料号\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("farbicModel")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"坯布规格\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentName")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"产品名称/层号\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentWeight")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"重量/kg\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentCountPerDrawings")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"卷/套\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentWidth")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"门幅/mm\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentLength")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"米长/m\"标题不存在");
                            }
                            if (!titleIndexMap.containsKey("fragmentMemo")) {
                                msg.addMessage("第 " + (i + 1) + "行 \"备注\"标题不存在");
                            }
                        } else if (fa != -1 && fa + 2 < i && fb == -1) {
                            if (StringUtils.isNotBlank(row.getCell(0,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                    || StringUtils.isNotBlank(row.getCell(2,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(3,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(4,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(5,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(7,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(8,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())
                                    || StringUtils.isNotBlank(row.getCell(9,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                    .toString())) {// 判断有效行内内容不为空
                                Fragment importFragment = new Fragment();
                                for (String key : titleIndexMap.keySet()) {
                                    int index = titleIndexMap.get(key);
                                    Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                    String value = cell.toString();
                                    if (StringUtils.equals(key, "fragmentCode")) {// 子物料号
                                        if (StringUtils.isBlank(value)) {
                                            msg.addMessage(i + 1, index + 1, "子物料号不能为空");
                                        } else {
                                            importFragment.setFragmentCode(value);
                                        }
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentName")) {// 产品名称/层号
                                        if (StringUtils.isBlank(value)) {
                                            msg.addMessage(i + 1, index + 1, "产品名称/层号不能为空!");
                                        } else {
                                            importFragment.setFragmentName(value);
                                        }
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentWeight")) {// 重量
                                        if (StringUtils.isBlank(value)) {
                                            msg.addMessage(i + 1, index + 1, "重量不能为空!");
                                        } else {//重量不为空时，验证是否是数字类型
                                            if (isNumber(value)) {
                                                importFragment.setFragmentWeight(Double.parseDouble(value));
                                            } else {
                                                msg.addMessage(i + 1, index + 1, "重量应为数字类型!");
                                            }
                                        }
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentCountPerDrawings")) {// 卷/套
                                        if (StringUtils.isBlank(value)) {
                                            msg.addMessage(i + 1, index + 1, "卷/套不能为空");
                                        } else {
                                            if (isNumber(value)) {// 判断"卷/套"是否为数字类型
                                                importFragment.setFragmentCountPerDrawings(Double.valueOf(value).intValue());
                                            } else {
                                                msg.addMessage(i + 1, index + 1, "卷/套应为数字类型");
                                            }
                                        }
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentLength")) {// 米长
                                        importFragment.setFragmentLength(value);
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentWidth")) {// 门幅
                                        String fragmentWidth = value;
                                        if (fragmentWidth.indexOf(".") != -1) {
                                            importFragment.setFragmentWidth(fragmentWidth.substring(0, fragmentWidth.indexOf(".")));
                                        } else {
                                            importFragment.setFragmentWidth(fragmentWidth);
                                        }
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "farbicModel")) {// 胚布规格
                                        importFragment.setFarbicModel(value);
                                        continue;
                                    }
                                    if (StringUtils.equals(key, "fragmentMemo")) {// 备注
                                        importFragment.setFragmentMemo(value);
                                        continue;
                                    }
                                }
                                if (!msg.hasError()) {
                                    importFragment.setIsDeleted(0);
                                    importFragment.setTcBomId(tcBomId);
                                    fragmentList.add(importFragment);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (!msg.hasError()) {
                    List<Fragment> fragments = fragmentDao.findListByMap(Fragment.class, map);
                    for (Fragment fragment : fragments) {// 保存之前，删除之前的数据
                        fragmentDao.delete(fragment);
                    }
                    fragmentDao.saveList(fragmentList);// 保存导入数据
                }
            } else {
                msg.addMessage("请重新选择导入文件!");
            }
        } else {
            msg.addMessage("请选择导入文件!");
        }
        return msg;
    }

    /**
     * 裁剪图纸BOM Excel 导入保存
     */
    @Override
    public ExcelImportMessage drawingsImport(Long fileId, Long tcBomId, String partId) throws Exception {
        ExcelImportMessage msg = new ExcelImportMessage();// 存放错误提示信息
        if (fileId != null) {
            Map<String, Object> tcBomIdMap = new HashMap();
            tcBomIdMap.put("tcBomId", tcBomId);
            Attachment att = fragmentDao.findById(Attachment.class, fileId);
            if (att != null) {
                String filePath = att.getFilePath();
                List<Drawings> drawingsList = new ArrayList();
                // 读取文件
                InputStream is = new FileInputStream(filePath);
                Workbook wb = WorkbookFactory.create(is);
                Sheet sheet = wb.getSheetAt(2);// 获取已导入Excel的第3个sheet
                Map<String, Integer> titleIndexMap = new LinkedHashMap<>();
                for (int k = 0; k <= sheet.getLastRowNum(); k++) {
                    Drawings drawings = new Drawings();
                    Row row2 = sheet.getRow(k);
                    if (row2 != null) {
                        if (StringUtils.isNotBlank(row2.getCell(0,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                | StringUtils.isNotBlank(row2.getCell(1,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(2,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(3,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(4,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(5,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(6,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(7,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(8,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(9,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(10,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(11,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())
                                || StringUtils.isNotBlank(row2.getCell(12,
                                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString())) {// 判断有效行内内容不为空时
                            if (k == 0) {// Excle标题
                                for (int j = 0; j < row2.getLastCellNum(); j++) {
                                    if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "小部件编码")) {
                                        titleIndexMap.put("fragmentCode", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "小部件名称")) {
                                        titleIndexMap.put("fragmentName", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "数量")) {
                                        titleIndexMap.put("fragmentCountPerDrawings", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "图号")) {
                                        titleIndexMap.put("fragmentDrawingNo", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "图纸版本")) {
                                        titleIndexMap.put("fragmentDrawingVer", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "图内套数")) {
                                        titleIndexMap.put("suitCountPerDrawings", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "出图顺序")) {
                                        titleIndexMap.put("printSort", j);
                                    } else if (StringUtils.startsWith(row2.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(), "部件名称")) {
                                        titleIndexMap.put("partName", j);
                                    }
                                }

                                // 检查标题
                                if (!titleIndexMap.containsKey("fragmentCode")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"小部件编码\"标题不存在");
                                }
                                if (!titleIndexMap.containsKey("fragmentName")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"小部件名称\"标题不存在");
                                }

                                if (!titleIndexMap.containsKey("fragmentCountPerDrawings")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"数量\"标题不存在");
                                }

                                if (!titleIndexMap.containsKey("fragmentDrawingNo")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"图号\"标题不存在");
                                }
                                if (!titleIndexMap.containsKey("fragmentDrawingVer")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"图纸版本\"标题不存在");
                                }
                                if (!titleIndexMap.containsKey("suitCountPerDrawings")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"图内套数\"标题不存在");
                                }
                                if (!titleIndexMap.containsKey("printSort")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"出图顺序\"标题不存在");
                                }
                                if (!titleIndexMap.containsKey("partName")) {
                                    msg.addMessage("第 " + (k + 1) + "行 \"部件名称\"标题不存在");
                                }
                            } else {// Excle内容
                                if (titleIndexMap.size() == 0) {
                                    msg.addMessage("请将图纸BOM放在第三个sheet页");
                                    break;
                                }
                                int index1 = titleIndexMap.get("fragmentCode");
                                Cell cell1 = row2.getCell(index1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                String value1 = cell1.toString();
                                if (StringUtils.isBlank(value1)) {// 小部件编码为空时
                                    msg.addMessage(k + 1, index1 + 1, "小部件编码不能为空");
                                } else {// 小部件编码不为空时，判断小部件编码是否存在裁片中
                                    List<Map<String, Object>> fragments = fragmentDao.findFragmentBytcBomIdAndfragmentCode(tcBomId, value1);
                                    if (fragments.size() == 0) {// 该小部件编码不存在裁片中
                                        msg.addMessage(k + 1, 1, "小部件编码(" + value1 + ")在裁片中不存在!");
                                    } else {// 该小部件编码存在裁片中时
                                        Map<String, Object> fragment = fragments.get(0);
                                        for (String key1 : titleIndexMap.keySet()) {
                                            int index2 = titleIndexMap.get(key1);
                                            Cell cell2 = row2.getCell(index2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                            String value2 = cell2.toString();
                                            if (StringUtils.equals(key1, "fragmentName")) {// 小部件名称
                                                if (StringUtils.isBlank(value1)) {
                                                    msg.addMessage(k + 1, index2 + 1, "小部件名称不能为空!");
                                                } else {
                                                    if ((value2.toLowerCase()).equals(fragment.get("FRAGMENTNAME").toString().toLowerCase())) {
                                                        drawings.setFragmentName(value2);
                                                    } else {
                                                        msg.addMessage(k + 1, index2 + 1, "和裁片中小部件名称不相同!");
                                                    }
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "fragmentCountPerDrawings")) {// 数量
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "数量不能为空!");
                                                } else {
                                                    if (isNumber(value2)) {// 验证数量是否为数字类型
                                                        drawings.setFragmentCountPerDrawings(Double.valueOf(value2).intValue());
                                                    } else {
                                                        msg.addMessage(k + 1, index2 + 1, "数量应为数字类型!");
                                                    }
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "fragmentDrawingNo")) {// 图号
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "图号不能为空");
                                                } else {
                                                    drawings.setFragmentDrawingNo(value2);
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "partName")) {// 部件名称
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "部件名称不能为空");
                                                } else {
                                                    drawings.setPartName(value2);
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "fragmentDrawingVer")) {// 图纸版本
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "图纸版本不能为空");
                                                } else {
                                                    drawings.setFragmentDrawingVer(value2);
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "suitCountPerDrawings")) {// 图内套数
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "图内套数不能为空");
                                                } else {
                                                    if (isNumber(value2)) {
                                                        drawings.setSuitCountPerDrawings(Double.valueOf(value2).intValue());
                                                    } else {
                                                        msg.addMessage(k + 1, index2 + 1, "图内套数应为数字类型!");
                                                    }
                                                }
                                                continue;
                                            }

                                            if (StringUtils.equals(key1, "printSort")) {// 出图顺序
                                                if (StringUtils.isBlank(value2)) {
                                                    msg.addMessage(k + 1, index2 + 1, "出图顺序不能为空");
                                                } else {
                                                    if (isNumber(value2)) {
                                                        drawings.setPrintSort(Double.valueOf(value2).intValue());
                                                    } else {
                                                        msg.addMessage(k + 1, index2 + 1, "出图顺序应为数字类型!");
                                                    }
                                                }
                                                continue;
                                            }
                                        }

                                        if (!msg.hasError()) {// 如果没有错误信息时，添加一条记录
                                            drawings.setIsDeleted(0);
                                            drawings.setFragmentCode(value1);// 裁片编码
                                            drawings.setFarbicModel(fragment.get("FARBICMODEL").toString());//胚布规格
                                            drawings.setFragmentWeight(Double.parseDouble(fragment.get("FRAGMENTWEIGHT").toString()));//重量
                                            drawings.setFragmentLength(fragment.get("FRAGMENTLENGTH").toString());//长度
                                            drawings.setFragmentWidth(fragment.get("FRAGMENTWIDTH").toString());//宽度
                                            drawings.setFragmentMemo(fragment.get("FRAGMENTMEMO").toString());//备注
                                            drawings.setFragmentId(fragment.get("ID").toString());// 裁片ID
                                            drawings.setPartId(Long.parseLong(partId));// 部件Id
                                            drawings.setTcBomId(tcBomId);
                                            drawingsList.add(drawings);
                                        }
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                if (!msg.hasError()) {
                    tcBomIdMap.put("partId", Long.parseLong(partId));
                    List<Drawings> drawingsList1 = fragmentDao.findListByMap(Drawings.class, tcBomIdMap);
                    for (Drawings drawings : drawingsList1) {// 保存之前，删除之前的数据
                        fragmentDao.delete(drawings);
                    }
                    fragmentDao.saveList(drawingsList);// 保存导入数据
                }
            } else {
                msg.addMessage("请重新选择导入文件!");
            }
        } else {
            msg.addMessage("请选择导入文件!");
        }
        return msg;
    }
}
