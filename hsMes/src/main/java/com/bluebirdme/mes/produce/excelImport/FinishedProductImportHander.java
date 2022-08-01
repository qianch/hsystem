package com.bluebirdme.mes.produce.excelImport;

import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.*;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportHandler;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.exception.ExceptionParser;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.device.entity.WeightCarrier;
import com.bluebirdme.mes.device.service.IWeightCarrierService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.service.IConsumerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成品信息导入
 *
 * @author ZhangHengdong
 */
public class FinishedProductImportHander extends ExcelImportHandler {
    private static final Logger logger = LoggerFactory.getLogger(FinishedProductImportHander.class);

    IFinishedProductService finishedProductService = (IFinishedProductService) SpringCtx.getBean("finishedProductServiceImpl");

    IConsumerService consumerService = (IConsumerService) SpringCtx.getBean("consumerServiceImpl");

    IFinishedProductCategoryService finishedProductCategoryService = (IFinishedProductCategoryService) SpringCtx.getBean("finishedProductCategoryServiceImpl");

    ITcBomService tcBomService = (ITcBomService) SpringCtx.getBean("tcBomServiceImpl");

    ITcBomVersionService tcBomVersionService = (ITcBomVersionService) SpringCtx.getBean("tcBomVersionServiceImpl");

    IFtcBomService ftcBomService = (IFtcBomService) SpringCtx.getBean("ftcBomServiceImpl");

    IBcBomService bcBomService = (IBcBomService) SpringCtx.getBean("bcBomServiceImpl");

    IBCBomVersionService bcBomVersionService = (IBCBomVersionService) SpringCtx.getBean("bCBomVersionServiceImpl");

    IWeightCarrierService weightCarrierService = (IWeightCarrierService) SpringCtx.getBean("weightCarrierServiceImpl");

    ExcelImportMessage msg = new ExcelImportMessage();

    @Override
    public ExcelImportMessage check(ExcelContent content) {
        //Excel校验信息
        List<String[]> data = content.getData();
        Map<String, Object> map = new HashMap<>();
        String[] temp;

        try {
            for (int i = 0; i < data.size(); i++) {
                temp = data.get(i);
                if (StringUtils.isBlank(temp[1])) {//客户代码非空验证
                    msg.addMessage(i + 1, 2, "客户代码不能为空");
                } else {//验证客户代码在响应的模块中已经存在
                    map.clear();
                    map.put("CONSUMERCODE", temp[1]);
                    if (!consumerService.isExist(Consumer.class, map)) {
                        msg.addMessage(i + 1, 2, "客户代码：" + temp[1] + "在响应的模块中不存在");
                    } else {
                        Consumer consumer = consumerService.findUniqueByMap(Consumer.class, map);
                        if (consumer != null) {
                            if (StringUtils.isBlank(consumer.getConsumerName())) {
                                msg.addMessage(i + 1, 1, "客户名称不能根据客户代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 1, "客户名称不能为空");
                        }
                    }
                }
                if (StringUtils.isBlank(temp[2])) {//客户产品名称非空验证`
                    msg.addMessage(i + 1, 3, "客户产品名称不能为空");
                }
                if (StringUtils.isBlank(temp[6])) {//客户产品名称非空验证`
                    msg.addMessage(i + 1, 7, "预留米长不能为空");
                }
                if (StringUtils.isBlank(temp[13])) {//产品类型非空验证
                    msg.addMessage(i + 1, 14, "产品类型不能为空");
                } else if ("套材".equals(temp[13]) || "套材" == temp[13]) {
                    if (StringUtils.isBlank(temp[15])) {
                        TcBom tcBom = tcBomService.findOne(TcBom.class, "tcProcBomCode", temp[17]);
                        if (tcBom != null) {
                            if (StringUtils.isBlank(tcBom.getTcProcBomName())) {
                                msg.addMessage(i + 1, 16, "工艺名称不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 16, "工艺代码不正确，未找到套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("TCPROCBOMNAME", temp[15]);
                        if (!tcBomService.isExist(TcBom.class, map)) {
                            msg.addMessage(i + 1, 16, "工艺名称：" + temp[15] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[17])) {
                        msg.addMessage(i + 1, 18, "工艺代码不能为空");
                    } else {
                        map.clear();
                        map.put("TCPROCBOMCODE", temp[17]);
                        if (!tcBomService.isExist(TcBom.class, map)) {
                            msg.addMessage(i + 1, 18, "工艺代码：" + temp[17] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[18])) {
                        TcBom tcBom = tcBomService.findOne(TcBom.class, "tcProcBomCode", temp[17]);
                        if (tcBom != null) {
                            TcBomVersion tcBomVersion = tcBomService.findOne(TcBomVersion.class, "tcProcBomId", tcBom.getId());
                            if (tcBomVersion == null) {
                                msg.addMessage(i + 1, 19, "工艺版本不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 19, "工艺代码不正确，未找到套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("TCPROCBOMVERSIONCODE", temp[18]);
                        if (!tcBomVersionService.isExist(TcBomVersion.class, map)) {
                            msg.addMessage(i + 1, 19, "工艺版本：" + temp[18] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[19])) {
                        msg.addMessage(i + 1, 20, "包装代码不能为空");
                    } else {
                        map.clear();
                        map.put("PACKBOMCODE", temp[19]);
                        if (!bcBomService.isExist(BcBom.class, map)) {
                            msg.addMessage(i + 1, 20, "包装代码：" + temp[19] + "在响应的模块中不存在");
                        }
                    }
                    if (StringUtils.isBlank(temp[20])) {
                        List<BcBom> bcBomList = tcBomService.find(BcBom.class, "packBomCode", temp[19]);
                        if (bcBomList != null && bcBomList.size() == 1) {
                            BCBomVersion bcBomVersion = tcBomService.findOne(BCBomVersion.class, "packBomId", bcBomList.get(0).getId());
                            if (bcBomVersion == null) {
                                msg.addMessage(i + 1, 21, "包装版本不能根据包装代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 21, "包装版本不能为空");
                        }
                    } else {
                        map.clear();
                        map.put("PACKVERSION", temp[20]);
                        if (!bcBomVersionService.isExist(BCBomVersion.class, map)) {
                            msg.addMessage(i + 1, 21, "包装版本：" + temp[20] + "在响应的模块中不存在");
                        }
                    }
                } else if ("非套材".equals(temp[13]) || "非套材 " == temp[13]) {
                    if (StringUtils.isBlank(temp[7])) {
                        msg.addMessage(i + 1, 8, "卷重不能为空");
                    }
                    if (StringUtils.isBlank(temp[15])) {
                        FtcBom ftcBom = tcBomService.findOne(FtcBom.class, "ftcProcBomCode", temp[17]);
                        if (ftcBom != null) {
                            if (StringUtils.isBlank(ftcBom.getFtcProcBomName())) {
                                msg.addMessage(i + 1, 16, "工艺名称不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 16, "工艺代码不正确，找不到非套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMNAME", temp[15]);
                        if (!ftcBomService.isExist(FtcBom.class, map)) {
                            msg.addMessage(i + 1, 16, "工艺名称：" + temp[15] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[17])) {
                        msg.addMessage(i + 1, 18, "工艺代码不能为空");
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMCODE", temp[17]);
                        if (!ftcBomService.isExist(FtcBom.class, map)) {
                            msg.addMessage(i + 1, 18, "工艺代码：" + temp[17] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[18])) {
                        FtcBom ftcBom = tcBomService.findOne(FtcBom.class, "ftcProcBomCode", temp[17]);
                        if (ftcBom != null) {
                            FtcBomVersion ftcBomVersion = tcBomService.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
                            if (ftcBomVersion == null) {
                                msg.addMessage(i + 1, 19, "工艺版本不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 19, "工艺代码不正确，未找到非套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMVERSIONCODE", temp[18]);
                        if (!ftcBomService.isExist(FtcBomVersion.class, map)) {
                            msg.addMessage(i + 1, 19, "工艺版本：" + temp[18] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[19])) {
                        msg.addMessage(i + 1, 20, "包装代码不能为空");
                    } else {
                        map.clear();
                        map.put("PACKBOMCODE", temp[19]);
                        if (!bcBomService.isExist(BcBom.class, map)) {
                            msg.addMessage(i + 1, 20, "包装代码：" + temp[19] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[21])) {
                        msg.addMessage(i + 1, 22, "衬管编码不能为空");
                    } else {
                        map.clear();
                        map.put("CARRIERCODE", temp[21]);
                        if (!weightCarrierService.isExist(WeightCarrier.class, map)) {
                            msg.addMessage(i + 1, 22, "衬管编码：" + temp[21] + "在响应的模块中不存在");
                        }
                    }
                } else if ("胚布".equals(temp[13])) {
                    if (StringUtils.isBlank(temp[15])) {
                        FtcBom ftcBom = tcBomService.findOne(FtcBom.class, "ftcProcBomCode", temp[17]);
                        if (ftcBom != null) {
                            if (StringUtils.isBlank(ftcBom.getFtcProcBomName())) {
                                msg.addMessage(i + 1, 16, "工艺名称不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 16, "工艺代码不正确，未找到非套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMNAME", temp[15]);
                        if (!ftcBomService.isExist(FtcBom.class, map)) {
                            msg.addMessage(i + 1, 16, "工艺名称：" + temp[15] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[17])) {
                        msg.addMessage(i + 1, 18, "工艺代码不能为空");
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMCODE", temp[17]);
                        if (!ftcBomService.isExist(FtcBom.class, map)) {
                            msg.addMessage(i + 1, 18, "工艺代码：" + temp[17] + "在响应的模块中不存在");
                        }
                    }

                    if (StringUtils.isBlank(temp[18])) {
                        FtcBom ftcBom = tcBomService.findOne(FtcBom.class, "ftcProcBomCode", temp[17]);
                        if (ftcBom != null) {
                            FtcBomVersion ftcBomVersion = tcBomService.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
                            if (ftcBomVersion == null) {
                                msg.addMessage(i + 1, 19, "工艺版本不能根据工艺代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 19, "工艺代码不正确，未找到非套材BOM");
                        }
                    } else {
                        map.clear();
                        map.put("FTCPROCBOMVERSIONCODE", temp[18]);
                        if (!ftcBomService.isExist(FtcBomVersion.class, map)) {
                            msg.addMessage(i + 1, 19, "工艺版本：" + temp[18] + "在响应的模块中不存在");
                        }
                    }
                    if (!"".equals(temp[19]) && temp[19] != null) {
                        msg.addMessage(i + 1, 20, "胚布无包装");
                    }
                    if (!"".equals(temp[20]) && temp[20] != null) {
                        msg.addMessage(i + 1, 21, "胚布无包装");
                    }
                }
                //胚布
                if (StringUtils.isBlank(temp[14])) {//产品属性非空验证
                    msg.addMessage(i + 1, 15, "产品属性不能为空");
                }
                if (StringUtils.isBlank(temp[16])) {//保质期非空验证
                    msg.addMessage(i + 1, 17, "保质期不能为空");
                }

                if (StringUtils.isBlank(temp[23])) {//成品类别代码非空验证
                    msg.addMessage(i + 1, 24, "成品类别代码不能为空");
                } else {//验证成品类别代码在响应的模块中已经存在
                    map.clear();
                    map.put("CATEGORYCODE", temp[23]);
                    if (!finishedProductCategoryService.isExist(FinishedProductCategory.class, map)) {
                        msg.addMessage(i + 1, 24, "成品类别代码：" + temp[23] + "在响应的模块中不存在");
                    }
                }

                if (StringUtils.isBlank(temp[24])) {//成品类别名称非空验证
                    map.clear();
                    if (!StringUtils.isBlank(temp[23])) {
                        map.put("CATEGORYCODE", temp[23]);
                        FinishedProductCategory finishedProductCategory = finishedProductCategoryService.findUniqueByMap(FinishedProductCategory.class, map);
                        if (finishedProductCategory != null) {
                            if (StringUtils.isBlank(finishedProductCategory.getCategoryName())) {
                                msg.addMessage(i + 1, 25, "成品类别名称不能根据成品类别代码自动获取");
                            }
                        } else {
                            msg.addMessage(i + 1, 25, "成品类别代码在库中");
                        }
                    }
                } else {
                    //验证成品类别名称在响应的模块中已经存在
                    if (!finishedProductCategoryService.isExist(FinishedProductCategory.class, map)) {
                        map.clear();
                        map.put("CATEGORYNAME", temp[24]);
                        msg.addMessage(i + 1, 25, "成品类别名称：" + temp[24] + "在响应的模块中不存在");
                    }
                }

                if (StringUtils.isBlank(temp[25])) {//物料编码非空验证
                    msg.addMessage(i + 1, 26, "物料编码不能为空");
                } else {
                    //物料编码非重验证
                    map.clear();
                    Consumer consumer = consumerService.findOne(Consumer.class, "consumerCode", temp[1]);
                    map.put("MATERIELCODE", temp[25]);
                    map.put("PRODUCTCONSUMERID", consumer.getId());
                    if (finishedProductService.isExist(FinishedProduct.class, map, true)) {
                        msg.addMessage(i + 1, 26, "物料编码重复：" + temp[25]);
                    }
                }

                if (StringUtils.isNotBlank(temp[27])) {//包装要求不为空的情况下
                    if (temp[27].length() > 500) {//包装要求长度验证
                        msg.addMessage(i + 1, 28, "包装要求内容太长");
                    }
                }
                if (StringUtils.isBlank(temp[28])) {//称重规格非空验证
                    msg.addMessage(i + 1, 29, "称重规格不能为空");
                }
                if (StringUtils.isNotBlank(temp[30])) {//工艺要求不为空的情况下
                    if (temp[30].length() > 500) {//工艺要求长度验证
                        msg.addMessage(i + 1, 31, "工艺要求内容太长");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            msg.addMessage(ExceptionParser.getMessage(e));
        }
        return msg;
    }

    @Override
    public ExcelImportMessage toDB(ExcelContent content) {
        try {
            msg = finishedProductService.saveFinishedProductFromExcel(content);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            msg.addMessage(ExceptionParser.getMessage(e));
        }
        return msg;
    }
}
