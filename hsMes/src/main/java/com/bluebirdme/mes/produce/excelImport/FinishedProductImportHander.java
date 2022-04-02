package com.bluebirdme.mes.produce.excelImport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IBCBomVersionService;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomService;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionService;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportHandler;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.exception.ExceptionParser;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.device.entity.WeightCarrier;
import com.bluebirdme.mes.device.service.IWeightCarrierService;
import com.bluebirdme.mes.produce.dao.IFinishedProductCategoryDao;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.service.IConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 成品信息导入
 *
 * @author ZhangHengdong
 */
public class FinishedProductImportHander extends ExcelImportHandler {
    private static Logger logger = LoggerFactory.getLogger(FinishedProductImportHander.class);

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
	
	/*@Override
	public ExcelImportMessage check(ExcelContent content) {
		//Excel校验信息
		List<String[]> data=content.getData();
		
		Map<String ,Object> map = new HashMap<String, Object>();
		
		String temp[] = null;
		
		try {
			for(int i=0;i<data.size();i++){
				temp = data.get(i);
				if(StringUtils.isBlank(temp[0])){//客户名称非空验证
					msg.addMessage(i+1, 1, "客户名称不能为空");
				} else {
					map.clear();
					map.put("CONSUMERNAME", temp[0]);
					if(!consumerService.isExist(Consumer.class, map)){
						msg.addMessage(i+1, 1, "客户名称："+temp[0]+"在响应的模块中不存在");
					}
				}
				if(StringUtils.isBlank(temp[1])){//客户代码非空验证
					msg.addMessage(i+1, 2, "客户代码不能为空");
				} else {//验证客户代码在响应的模块中已经存在
					map.clear();
					map.put("CONSUMERCODE", temp[1]);
					if(!consumerService.isExist(Consumer.class, map)){
						msg.addMessage(i+1, 2, "客户代码："+temp[1]+"在响应的模块中不存在");
					}
				}
				if(StringUtils.isBlank(temp[2])){//客户产品名称非空验证`
					msg.addMessage(i+1, 3, "客户产品名称不能为空");
				}
				
				if(StringUtils.isBlank(temp[12])){//产品类型非空验证
					msg.addMessage(i+1, 13, "产品类型不能为空");
				} else if("套材".equals(temp[12]) || "套材" == temp[12]){
					if(StringUtils.isBlank(temp[14])){
						TcBom tcBom=tcBomService.findOne(TcBom.class,"tcProcBomCode",temp[16]);
						if(tcBom !=null){
							if(StringUtils.isBlank(tcBom.getTcProcBomName())){
								msg.addMessage(i+1, 15, "工艺名称不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 15, "工艺名称不能为空");
						}
						
					} else {
						map.clear();
						map.put("TCPROCBOMNAME", temp[14]);
						if(!tcBomService.isExist(TcBom.class, map)){
							msg.addMessage(i+1, 15, "工艺名称："+temp[14]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[16])){
						msg.addMessage(i+1, 17, "工艺代码不能为空");
					} else {
						map.clear();
						map.put("TCPROCBOMCODE", temp[16]);
						if(!tcBomService.isExist(TcBom.class, map)){
							msg.addMessage(i+1, 17, "工艺代码："+temp[16]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[17])){
						TcBom tcBom=tcBomService.findOne(TcBom.class,"tcProcBomCode",temp[16]);
						if(tcBom != null){
							TcBomVersion tcBomVersion=tcBomService.findOne(TcBomVersion.class,"tcProcBomId",tcBom.getId());
							if(tcBomVersion == null){
								msg.addMessage(i+1, 18, "工艺版本不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 18, "工艺版本不能为空");
						}
						
					} else {
						map.clear();
						map.put("TCPROCBOMVERSIONCODE", temp[17]);
						if(!tcBomVersionService.isExist(TcBomVersion.class, map)){
							msg.addMessage(i+1, 18, "工艺版本："+temp[17]+"在响应的模块中不存在");
						}
					}
					
					if(StringUtils.isBlank(temp[18])){
						msg.addMessage(i+1, 19, "包装代码不能为空");
					} else {
						map.clear();
						map.put("PACKBOMCODE", temp[18]);
						if(!bcBomService.isExist(BcBom.class, map)){
							msg.addMessage(i+1, 19, "包装代码："+temp[18]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[19])){
						List<BcBom> bcBomList=tcBomService.find(BcBom.class,"packBomCode",temp[18]);
						if(bcBomList !=null && bcBomList.size()==1){
							BCBomVersion bcBomVersion=tcBomService.findOne(BCBomVersion.class,"packBomId",bcBomList.get(0).getId());
							if(bcBomVersion == null){
								msg.addMessage(i+1, 20, "包装版本不能根据包装代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 20, "包装版本不能为空");
						}
						
					} else {
						map.clear();
						map.put("PACKVERSION", temp[19]);
						if(!bcBomVersionService.isExist(BCBomVersion.class, map)){
							msg.addMessage(i+1, 20, "包装版本："+temp[19]+"在响应的模块中不存在");
						}
					}
				} else if("非套材".equals(temp[12]) || "非套材 " == temp[12]){
					if("全称 (单卷重量300KG以上)".equals(temp[26])){
						if(StringUtils.isBlank(temp[8])){//最大卷重非空验证
							msg.addMessage(i+1, 9, "称重规则为“全称 (单卷重量300KG以上)”时最大卷重不能为空");
						}
						if(StringUtils.isBlank(temp[9])){//最小卷重非空验证
							msg.addMessage(i+1, 10, "称重规则为“全称 (单卷重量300KG以上)”时最小卷重不能为空");
						}
					}else if("抽称 (单卷重量300KG以下)".equals(temp[26])){
						if(StringUtils.isBlank(temp[8])){//最大卷重非空验证
							msg.addMessage(i+1, 9, "称重规则为“抽称 (单卷重量300KG以下)”时最大卷重不能为空");
						}
						if(StringUtils.isBlank(temp[9])){//最小卷重非空验证
							msg.addMessage(i+1, 10, "称重规则为“抽称 (单卷重量300KG以下)”时最小卷重不能为空");
						}
					}else if("不称 (套裁)".equals(temp[26])){
						if(StringUtils.isBlank(temp[6])){
							msg.addMessage(i+1, 7, "称重规则为“不称 (套裁)”时卷重不能为空");
						}
					}
					
					if(StringUtils.isBlank(temp[14])){
						FtcBom ftcBom=tcBomService.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
						if(ftcBom !=null){
							if(StringUtils.isBlank(ftcBom.getFtcProcBomName())){
								msg.addMessage(i+1, 15, "工艺名称不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 15, "工艺名称不能为空");
						}
					} else {
						map.clear();
						map.put("FTCPROCBOMNAME", temp[14]);
						if(!ftcBomService.isExist(FtcBom.class, map)){
							msg.addMessage(i+1, 15, "工艺名称："+temp[14]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[16])){
						msg.addMessage(i+1, 17, "工艺代码不能为空");
					} else {
						map.clear();
						map.put("FTCPROCBOMCODE", temp[16]);
						if(!ftcBomService.isExist(FtcBom.class, map)){
							msg.addMessage(i+1, 17, "工艺代码："+temp[16]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[17])){
						FtcBom ftcBom=tcBomService.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
						if(ftcBom != null){
							FtcBomVersion ftcBomVersion=tcBomService.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
							if(ftcBomVersion == null){
								msg.addMessage(i+1, 18, "工艺版本不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 18, "工艺版本不能为空");
						}
					} else {
						map.clear();
						map.put("FTCPROCBOMVERSIONCODE", temp[17]);
						if(!ftcBomService.isExist(FtcBomVersion.class, map)){
							msg.addMessage(i+1, 18, "工艺版本："+temp[17]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[18])){
						msg.addMessage(i+1, 19, "包装代码不能为空");
					} else {
						map.clear();
						map.put("PACKBOMCODE", temp[18]);
						if(!bcBomService.isExist(BcBom.class, map)){
							msg.addMessage(i+1, 19, "包装代码："+temp[18]+"在响应的模块中不存在");
						}
					}
					
					if(StringUtils.isBlank(temp[20])){
						msg.addMessage(i+1, 21, "衬管编码不能为空");
					} else {
						map.clear();
						map.put("CARRIERCODE", temp[20]);
						if(!weightCarrierService.isExist(WeightCarrier.class, map)){
							msg.addMessage(i+1, 21, "衬管编码："+temp[20]+"在响应的模块中不存在");
						}
					}
				} else if("胚布".equals(temp[12]) || "胚布" == temp[12]){
					if(StringUtils.isBlank(temp[14])){
						FtcBom ftcBom=tcBomService.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
						if(ftcBom !=null){
							if(StringUtils.isBlank(ftcBom.getFtcProcBomName())){
								msg.addMessage(i+1, 15, "工艺名称不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 15, "工艺名称不能为空");
						}
						
					} else {
						map.clear();
						map.put("FTCPROCBOMNAME", temp[14]);
						if(!ftcBomService.isExist(FtcBom.class, map)){
							msg.addMessage(i+1, 15, "工艺名称："+temp[14]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[16])){
						msg.addMessage(i+1, 17, "工艺代码不能为空");
					} else {
						map.clear();
						map.put("FTCPROCBOMCODE", temp[16]);
						if(!ftcBomService.isExist(FtcBom.class, map)){
							msg.addMessage(i+1, 17, "工艺代码："+temp[16]+"在响应的模块中不存在");
						}
					}
					if(StringUtils.isBlank(temp[17])){
						FtcBom ftcBom=tcBomService.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
						if(ftcBom != null){
							FtcBomVersion ftcBomVersion=tcBomService.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
							if(ftcBomVersion == null){
								msg.addMessage(i+1, 18, "工艺版本不能根据工艺代码自动获取");
							}
						}else{
							msg.addMessage(i+1, 18, "工艺版本不能为空");
						}
						
					} else {
						map.clear();
						map.put("FTCPROCBOMVERSIONCODE", temp[17]);
						if(!ftcBomService.isExist(FtcBomVersion.class, map)){
							msg.addMessage(i+1, 18, "工艺版本："+temp[17]+"在响应的模块中不存在");
						}
					}
					if(!"".equals(temp[18]) && temp[18]!=null){
						msg.addMessage(i+1, 19, "胚布无包装");
					}
					if(!"".equals(temp[19]) && temp[19]!=null){
						msg.addMessage(i+1, 20, "胚布无包装");
					}
				}//胚布
				if(StringUtils.isBlank(temp[13])){//产品属性非空验证
					msg.addMessage(i+1, 14, "产品属性不能为空");
				}
				if(StringUtils.isBlank(temp[15])){//保质期非空验证
					msg.addMessage(i+1, 16, "保质期不能为空");
				}
				if(StringUtils.isBlank(temp[21])){//成品类别代码非空验证
					msg.addMessage(i+1, 22, "成品类别代码不能为空");
				} else {//验证成品类别代码在响应的模块中已经存在
					map.clear();
					map.put("CATEGORYCODE", temp[21]);
					if(!finishedProductCategoryService.isExist(FinishedProductCategory.class, map)){
						msg.addMessage(i+1, 22, "成品类别代码："+temp[21]+"在响应的模块中不存在");
					}
				}
				if(StringUtils.isBlank(temp[22])){//成品类别名称非空验证
					msg.addMessage(i+1, 23, "成品类别名称不能为空");
				} else {//验证成品类别名称在响应的模块中已经存在
					map.clear();
					map.put("CATEGORYNAME", temp[22]);
					if(!finishedProductCategoryService.isExist(FinishedProductCategory.class, map)){
						msg.addMessage(i+1, 23, "成品类别名称："+temp[22]+"在响应的模块中不存在");
					}
				}
				if(StringUtils.isBlank(temp[23])){//物料编码非空验证
					msg.addMessage(i+1, 24, "物料编码不能为空");
				} else {//物料编码非重验证
					map.clear();
					Consumer consumer = consumerService.findOne(Consumer.class,"consumerCode",temp[1]);
					map.put("MATERIELCODE", temp[23]);
					*//*if(consumer != null){
						map.put("PRODUCTCONSUMERID", consumer.getId());
					}*//*
					map.put("PRODUCTCONSUMERID", consumer.getId());
					if(finishedProductService.isExist(FinishedProduct.class, map, true)){
						msg.addMessage(i+1, 24, "物料编码重复："+temp[23]);
					}
				}
				if(StringUtils.isBlank(temp[24])){//产品规格非空验证
					msg.addMessage(i+1, 25, "产品规格不能为空");
				}
				if(StringUtils.isNotBlank(temp[25])){//包装要求不为空的情况下
					if(temp[25].length() > 500){//包装要求长度验证
						msg.addMessage(i+1, 26, "包装要求内容太长");
					}
				}
				if(StringUtils.isBlank(temp[26])){//称重规格非空验证
					msg.addMessage(i+1, 27, "称重规格不能为空");
				}
				if(StringUtils.isNotBlank(temp[28])){//工艺要求不为空的情况下
					if(temp[28].length() > 500){//工艺要求长度验证
						msg.addMessage(i+1, 29, "工艺要求内容太长");
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
			msg.addMessage(ExceptionParser.getMessage(e));
		}
		return msg;
	}*/

    @Override
    public ExcelImportMessage check(ExcelContent content) {
        //Excel校验信息
        List<String[]> data = content.getData();

        Map<String, Object> map = new HashMap<String, Object>();

        String temp[] = null;

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
                            ;
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
					/*if("全称 (单卷重量300KG以上)".equals(temp[28])){
						if(StringUtils.isBlank(temp[9])){//最大卷重非空验证
							msg.addMessage(i+1, 10, "称重规则为“全称 (单卷重量300KG以上)”时最大卷重不能为空");
						}
						if(StringUtils.isBlank(temp[10])){//最小卷重非空验证
							msg.addMessage(i+1, 11, "称重规则为“全称 (单卷重量300KG以上)”时最小卷重不能为空");
						}
					}else if("抽称 (单卷重量300KG以下)".equals(temp[28])){
						if(StringUtils.isBlank(temp[9])){//最大卷重非空验证
							msg.addMessage(i+1, 9, "称重规则为“抽称 (单卷重量300KG以下)”时最大卷重不能为空");
						}
						if(StringUtils.isBlank(temp[10])){//最小卷重非空验证
							msg.addMessage(i+1, 10, "称重规则为“抽称 (单卷重量300KG以下)”时最小卷重不能为空");
						}
					}else if("不称 (套裁)".equals(temp[28])){
						if(StringUtils.isBlank(temp[7])){
							msg.addMessage(i+1, 8, "称重规则为“不称 (套裁)”时卷重不能为空");
						}
					}*/
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
                        }/*else{
							WeightCarrier weightCarrier = weightCarrierService.findUniqueByMap(WeightCarrier.class, map);
							if (weightCarrier != null){
								if(StringUtils.isBlank(weightCarrier.getCarrierName())){
									msg.addMessage(i+1, 23, "衬管规格不能根据衬管编码自动获取");
								}else {
									msg.addMessage(i+1, 23, "衬管规格不能为空");
								}
							}
						}*/
                    }
                } else if ("胚布".equals(temp[13]) || "胚布" == temp[13]) {
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
                } else {//验证成品类别名称在响应的模块中已经存在
                    if (!finishedProductCategoryService.isExist(FinishedProductCategory.class, map)) {
                        map.clear();
                        map.put("CATEGORYNAME", temp[24]);
                        msg.addMessage(i + 1, 25, "成品类别名称：" + temp[24] + "在响应的模块中不存在");
                    }
                }

                if (StringUtils.isBlank(temp[25])) {//物料编码非空验证
                    msg.addMessage(i + 1, 26, "物料编码不能为空");
                } else {//物料编码非重验证
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
