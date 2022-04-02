/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;

import com.aspose.cells.Worksheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.dao.IFtcBomDao;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.BomFile;
import com.bluebirdme.mes.baseInfo.entity.BomFilePdf;
import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

/**
 * @author 宋黎明
 * @Date 2016-10-8 13:36:52
 */
@Service
@AnyExceptionRollback
public class FtcBomServiceImpl extends BaseServiceImpl implements IFtcBomService {
    private static Logger logger = LoggerFactory.getLogger(FtcBomServiceImpl.class);
    @Resource
    IFtcBomDao fTc_BomDao;

    @Override
    protected IBaseDao getBaseDao() {
        return fTc_BomDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return fTc_BomDao.findPageInfo(filter, page);
    }

    // 删除工艺BOM及它的所有节点
    public void delete(String ids) {
        fTc_BomDao.delete(ids);
    }

    // 删除BOM明细
    public void deleteDetail(String ids) {
        fTc_BomDao.deleteDetail(ids);
    }

    // 删除BOM版本及它的明细
    public void deleteBomVersion(String ids) {
        fTc_BomDao.deleteBomVersion(ids);
    }

    // 获取工艺BOM数据并封装树数据
    public List<Map<String, Object>> getFtcBomJson(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJson(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }
        Map<String, Object> ret = null;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        // Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> map : listMap) {
            // System.out.println(MapUtils.getAsLong(map, "ID"));
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            /*
             * map1.put("ftcProcBomId", MapUtils.getAsLong(map, "ID"));
             * List<FtcBomVersion>
             * ftcBomVerson1=fTc_BomDao.findListByMap(FtcBomVersion.class,map1);
             * if(ftcBomVerson1.isEmpty()){ ret.put("state", ""); }else{
             * ret.put("state", "closed"); }
             */
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 获取工艺BOM数据并封装树数据
    public List<Map<String, Object>> getFtcBomJsonTest(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJsonTest(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }
        Map<String, Object> ret = null;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        // Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> map : listMap) {
            // System.out.println(MapUtils.getAsLong(map, "ID"));
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            /*
             * map1.put("ftcProcBomId", MapUtils.getAsLong(map, "ID"));
             * List<FtcBomVersion>
             * ftcBomVerson1=fTc_BomDao.findListByMap(FtcBomVersion.class,map1);
             * if(ftcBomVerson1.isEmpty()){ ret.put("state", ""); }else{
             * ret.put("state", "closed"); }
             */
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 获取BOM版本数据并封装树数据
    public List<Map<String, Object>> getFtcBomByVersionJson(String id) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            listMap = fTc_BomDao.getFtcBomByVersionJson(id);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }
        Map<String, Object> ret = null;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            // String s="";
            // if(MapUtils.getAsInt(map, "FTCPROCBOMVERSIONDEFAULT")==1){
            // s="[默认-";
            // }else{
            // s+="[";
            // }
            // if(MapUtils.getAsInt(map, "FTCPROCBOMVERSIONENABLED")==1){
            // s+="启用]";
            // }else if(MapUtils.getAsInt(map, "FTCPROCBOMVERSIONENABLED")==0){
            // s+="改版]";
            // }else{
            // s+="停用]";
            // }
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomVersionCode".toUpperCase()));
            // ret.put("state", "closed");
            map.put("nodeType", "version");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    // 复制BOM版本
    @Override
    public void toCompliteCopy(String id) throws Exception {
        // 根据id查询需要复制的版本对象
        FtcBomVersion ftcBomVersion = findById(FtcBomVersion.class, Long.parseLong(id));
        // 根据字段查询对象的集合
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ftcBomVersionId", ftcBomVersion.getId());
        List<FtcBomDetail> li = fTc_BomDao.findListByMap(FtcBomDetail.class, map);
        ArrayList<FtcBomDetail> saveLi = new ArrayList<FtcBomDetail>();
        // 保存新的version对象
        FtcBomVersion newVersion = new FtcBomVersion();
        ObjectUtils.clone(ftcBomVersion, newVersion);
        newVersion.setFtcProcBomVersionCode(newVersion.getFtcProcBomVersionCode() + "(复制)");
        newVersion.setFtcConsumerVersionCode(newVersion.getFtcConsumerVersionCode() + "(复制)");
        newVersion.setFtcProcBomVersionDefault(-1);
        // 复制版本时，将审核状态都改为未审核
        newVersion.setAuditState(AuditConstant.RS.SUBMIT);
        fTc_BomDao.save(newVersion);
        for (FtcBomDetail d : li) {
            FtcBomDetail saveDetail = new FtcBomDetail();
            ObjectUtils.clone(d, saveDetail);
            saveDetail.setFtcBomVersionId(newVersion.getId());
            saveLi.add(saveDetail);
        }
        fTc_BomDao.save(saveLi.toArray(new FtcBomDetail[]{}));
    }

    /**
     * 批量更新非套材的bom信息
     *
     * @param
     */
    public void updateFtcInfos(int value) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("productIsTc", value);
        List<FinishedProduct> fpList = findListByMap(FinishedProduct.class, map);
        for (FinishedProduct fp : fpList) {
            Long procBomId = fp.getProcBomId();
            Long packBomId = fp.getPackBomId();
            if (procBomId != null) {
                FtcBomVersion fv = findById(FtcBomVersion.class, procBomId);
                if (fv == null) {
                    fp.setProductProcessCode(null);
                    fp.setProductProcessBomVersion(null);
                    fp.setProductConsumerBomVersion(null);
                } else {
                    FtcBom fb = findById(FtcBom.class, fv.getFtcProcBomId());
                    fp.setProductProcessCode(fb.getFtcProcBomName() + "/" + fb.getFtcProcBomCode());
                    fp.setProductProcessBomVersion(fv.getFtcProcBomVersionCode());
                    fp.setProductConsumerBomVersion(fv.getFtcConsumerVersionCode());
                }
            } else {
                fp.setProductProcessCode(null);
                fp.setProductProcessBomVersion(null);
                fp.setProductConsumerBomVersion(null);
            }
            if (packBomId != null) {
                BCBomVersion bv = findById(BCBomVersion.class, packBomId);
                if (bv == null) {
                    continue;
                }
                BcBom bb = findById(BcBom.class, bv.getPackBomId());
                fp.setProductPackagingCode(bb.getPackBomName() + "/" + bb.getPackBomCode());
                fp.setProductPackageVersion(bv.getPackVersion());
            }
            update(fp);
        }
    }

    @Override
    public List<Map<String, Object>> getFtcBomJsonTest1(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = fTc_BomDao.getFtcBomJsonTest1(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }
        Map<String, Object> ret = null;
        List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
        // Map<String, Object> map1=new HashMap<>();
        for (Map<String, Object> map : listMap) {
            // System.out.println(MapUtils.getAsLong(map, "ID"));
            ret = new HashMap<String, Object>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "ftcProcBomName".toUpperCase()) + "/" + MapUtils.getAsString(map, "ftcProcBomCode".toUpperCase()));
            /*
             * map1.put("ftcProcBomId", MapUtils.getAsLong(map, "ID"));
             * List<FtcBomVersion>
             * ftcBomVerson1=fTc_BomDao.findListByMap(FtcBomVersion.class,map1);
             * if(ftcBomVerson1.isEmpty()){ ret.put("state", ""); }else{
             * ret.put("state", "closed"); }
             */
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public ExcelImportMessage doAddFtcBom(FtcBom ftcBom, FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        fTc_BomDao.save(ftcBom);
        ExcelImportMessage eim = new ExcelImportMessage();//存放错误消息
        if (ftcBom.getId() != null) {
            String fpbvc = ftcBomVersion.getFtcProcBomVersionCode();
            if (fpbvc != null && !fpbvc.equals("")) {
                //添加版本号
                ftcBomVersion.setFtcProcBomId(ftcBom.getId());
                ftcBomVersion.setFtcProcBomVersionDefault(1);
                ftcBomVersion.setFtcProcBomVersionEnabled(1);
                ftcBomVersion.setAuditState(AuditConstant.RS.SUBMIT);
                if (fileId != null){
                    ftcBomVersion.setAttachmentId(fileId.intValue());
                }
                fTc_BomDao.save(ftcBomVersion);

                //判断有Excel文件要解析？
                if (fileId != null) {
                    Attachment att = fTc_BomDao.findById(Attachment.class, fileId);
//					String filePath=att.getFilePath();
//					// 读取文件
//					InputStream is = new FileInputStream(filePath);
//					Workbook wb = WorkbookFactory.create(is);
//					Sheet sheet = wb.getSheetAt(1);		//获取第2个sheet
//					List<ConstructionParameter> data=new ArrayList<>();
//					int fa=-1;		//"结构参数"所在的行号
//					int fb=-1;		//"缝编针数"所在的行号
//					Map<String,Integer> titleIndexMap=new LinkedHashMap<>();
//					for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//						Row row =sheet.getRow(i);
//						if(fa == -1 && row != null && StringUtils.startsWith(row.getCell(0,Row.CREATE_NULL_AS_BLANK).toString(),"结构参数")){
//							fa=i;
//							continue;
//						}
//						if(fa != -1 && fa<i && fb == -1){
//							if(row!=null && StringUtils.startsWith(row.getCell(0,Row.CREATE_NULL_AS_BLANK).toString(),"缝编针数")){
//								fb=i;
//								continue;
//							}
//							if(row != null){
//								if(fa+1 == i){//标题
//									for (int j = 0; j < row.getLastCellNum(); j++) {
//										if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"织物结构")){
//											titleIndexMap.put("Product_feature", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"原料规格")){
//											titleIndexMap.put("raw_material", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"物料代码")){
//											titleIndexMap.put("Item_number", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"单位面积质量")){
//											titleIndexMap.put("Nominal_area_weight", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"钢筘规格")){
//											titleIndexMap.put("Reed", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"导纱针规格")){
//											titleIndexMap.put("Guide_needle", j);
//										}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"备注")){
//											titleIndexMap.put("Remark", j);
//										}
//									}
//									//检查标题
//									if(!titleIndexMap.containsKey("Product_feature")){
//										eim.addMessage("第 "+i+"行 \"织物结构\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("raw_material")){
//										eim.addMessage("第 "+i+"行 \"原料规格\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("Item_number")){
//										eim.addMessage("第 "+i+"行 \"物料代码\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("Nominal_area_weight")){
//										eim.addMessage("第 "+i+"行 \"单位面积质量\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("Reed")){
//										eim.addMessage("第 "+i+"行 \"钢筘规格\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("Guide_needle")){
//										eim.addMessage("第 "+i+"行 \"导纱针规格\"标题不存在");
//									}
//									if(!titleIndexMap.containsKey("Remark")){
//										eim.addMessage("第 "+i+"行 \"备注\"标题不存在");
//									}
//									
//								}else if(fa+2 <=i){//数据 start
//									if(fa+2 ==i){
//										titleIndexMap.put("baseIndex",i+1);
//									}
//									boolean rowNotBlank=false;
//									for(Integer index:titleIndexMap.values()){
//										Cell cell=row.getCell(index,Row.CREATE_NULL_AS_BLANK);
//										if(StringUtils.isNotBlank(cell.toString())){
//											rowNotBlank=true;
//										}
//									}
//									if(rowNotBlank){
//										ConstructionParameter cp=new ConstructionParameter();
//										for(String key:titleIndexMap.keySet()){
//											int index=titleIndexMap.get(key);
//											Cell cell=row.getCell(index,Row.CREATE_NULL_AS_BLANK);
//											String value=cell.toString();
//											if(StringUtils.equals(key,"Product_feature")){//织物结构
//												if(StringUtils.isBlank(value)){
//													eim.addMessage(i+1,index+1,"织物结构不能为空");
//												}else{
//													cp.setProductFeature(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "raw_material")){//原料规格
//												if(StringUtils.isBlank(value)){
//													eim.addMessage(i+1,index+1,"原料规格不能为空");
//												}else{
//													cp.setRawMaterial(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "Item_number")){//物料代码
//												if(StringUtils.isBlank(value)){
//													eim.addMessage(i+1,index+1,"物料代码不能为空");
//												}else{
//													cp.setItemNumber(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "Nominal_area_weight")){//单位面积质量
//												if(StringUtils.isBlank(value)){
//													eim.addMessage(i+1,index+1,"单位面积质量不能为空");
//												}else if(!this.isNumber(value)){
//													eim.addMessage(i+1,index+1,"单位面积质量只能是数字");
//												}else{
//													cp.setNominalAreaWeight(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "Reed")){//钢筘规格
//												if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//													cp.setReed("");
//												}else{
//													cp.setReed(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "Guide_needle")){//导纱针规格
//												if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//													cp.setGuideNeedle("");
//												}else{
//													cp.setGuideNeedle(value);
//												}
//												continue;
//											}
//											
//											if(StringUtils.equals(key, "Remark")){//备注
//												if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//													cp.setRemark("");
//												}else{
//													cp.setRemark(value);
//												}
//											}
//											
//										}//end for key
//										data.add(cp);
//									}
//								}//数据 end
//							}
//
//						}
//						
//					}//end for
//					if(fa==-1){
//						eim.addMessage("第二个工作表\"结构参数\"标题不存在");
//					}
//					
//					if(fb==-1){
//						eim.addMessage("第二个工作表\"缝编针数\"不存在");
//					}
//					
//					List<Material> plist=fTc_BomDao.findAll(Material.class);
//					
//					//验证合法性
//					for(int i=0;i<data.size();i++){
//						String name=data.get(i).getProductFeature();	//原料名称
//						String model=data.get(i).getRawMaterial();		//原料规格
//						String itemNumber=data.get(i).getItemNumber();	//物料代码
//						String weightPerSquareMetre=data.get(i).getNominalAreaWeight();	//单位面积质量
//						if(StringUtils.isNotBlank(model) && StringUtils.isNotBlank(itemNumber)){
//							boolean findModel=false;	//ture为原料信息中有该信息
//							for(Material m:plist){
//								//TODO 原料信息修改
//								if(model.equals(m.getMaterialModel())&&itemNumber.equals(null)){
//									findModel=true;
//									break;
//								}
//							}
//							if(!findModel){
//								eim.addMessage("第 "+(titleIndexMap.get("baseIndex")+i)+"行:原料规格("+model+")或物料代码("+itemNumber+")在原料信息中不存在");
//							}
//						}
//						
//					}
//					if(!eim.hasError()){
//						//把数据放入非套材BOM明细列表
////						List<FtcBomDetail> detailList=new ArrayList<>();
//						for(ConstructionParameter cp : data){
//							FtcBomDetail detail=new FtcBomDetail();
//							detail.setFtcBomVersionId(ftcBomVersion.getId());		//非套材BOM版本信息ID
//							detail.setFtcBomDetailName(cp.getProductFeature());		//原料名称
//							detail.setFtcBomDetailModel(cp.getRawMaterial());		//原料规格
//							detail.setFtcBomDetailItemNumber(cp.getItemNumber());	//物料代码
//							detail.setFtcBomDetailWeightPerSquareMetre(Double.valueOf(cp.getNominalAreaWeight()));//单位面积质量
//							detail.setFtcBomDetailReed(cp.getReed());				//钢筘规格
//							detail.setFtcBomDetailGuideNeedle(cp.getGuideNeedle());	//导纱针规格
//							detail.setFtcBomDetailRemark(cp.getRemark());			//备注
////							detailList.add(detail);
//							fTc_BomDao.save(detail);
//						}
////						fTc_BomDao.saveList(detailList);
//					}else{
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事物
//					}
                    savePdfFile(ftcBomVersion, att, eim);
                }
            }
        }
        return eim;
    }

    @Override
    public ExcelImportMessage doUpdateFtcBomVersion(FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        fTc_BomDao.update2(ftcBomVersion);
        ExcelImportMessage eim = new ExcelImportMessage();//存放错误消息
        //判断有Excel文件要解析？
        if (fileId != null) {
            Attachment att = fTc_BomDao.findById(Attachment.class, fileId);
//			String filePath=att.getFilePath();
//			// 读取文件
//			InputStream is = new FileInputStream(filePath);
//			Workbook wb = WorkbookFactory.create(is);
//			Sheet sheet = wb.getSheetAt(1);		//获取第2个sheet
//			List<ConstructionParameter> data=new ArrayList<>();
//			int fa=-1;		//"结构参数"所在的行号
//			int fb=-1;		//"缝编针数"所在的行号
//			Map<String,Integer> titleIndexMap=new LinkedHashMap<>();
//			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//				Row row =sheet.getRow(i);
//				if(fa == -1 && row != null && StringUtils.startsWith(row.getCell(0,Row.CREATE_NULL_AS_BLANK).toString(),"结构参数")){
//					fa=i;
//					continue;
//				}
//				if(fa != -1 && fa<i && fb == -1){
//					if(row!=null && StringUtils.startsWith(row.getCell(0,Row.CREATE_NULL_AS_BLANK).toString(),"缝编针数")){
//						fb=i;
//						continue;
//					}
//					if(row != null){
//						if(fa+1 == i){//标题
//							for (int j = 0; j < row.getLastCellNum(); j++) {
//								if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"织物结构")){
//									titleIndexMap.put("Product_feature", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"原料规格")){
//									titleIndexMap.put("raw_material", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"物料代码")){
//									titleIndexMap.put("Item_number", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"单位面积质量")){
//									titleIndexMap.put("Nominal_area_weight", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"钢筘规格")){
//									titleIndexMap.put("Reed", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"导纱针规格")){
//									titleIndexMap.put("Guide_needle", j);
//								}else if(StringUtils.startsWith(row.getCell(j,Row.CREATE_NULL_AS_BLANK).toString(),"备注")){
//									titleIndexMap.put("Remark", j);
//								}
//							}
//							//检查标题
//							if(!titleIndexMap.containsKey("Product_feature")){
//								eim.addMessage("第 "+i+"行 \"织物结构\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("raw_material")){
//								eim.addMessage("第 "+i+"行 \"原料规格\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("Item_number")){
//								eim.addMessage("第 "+i+"行 \"物料代码\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("Nominal_area_weight")){
//								eim.addMessage("第 "+i+"行 \"单位面积质量\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("Reed")){
//								eim.addMessage("第 "+i+"行 \"钢筘规格\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("Guide_needle")){
//								eim.addMessage("第 "+i+"行 \"导纱针规格\"标题不存在");
//							}
//							if(!titleIndexMap.containsKey("Remark")){
//								eim.addMessage("第 "+i+"行 \"备注\"标题不存在");
//							}
//							
//						}else if(fa+2 <=i){//数据 start
//							if(fa+2 ==i){
//								titleIndexMap.put("baseIndex",i+1);
//							}
//							boolean rowNotBlank=false;
//							for(Integer index:titleIndexMap.values()){
//								Cell cell=row.getCell(index,Row.CREATE_NULL_AS_BLANK);
//								if(StringUtils.isNotBlank(cell.toString())){
//									rowNotBlank=true;
//								}
//							}
//							if(rowNotBlank){
//								ConstructionParameter cp=new ConstructionParameter();
//								for(String key:titleIndexMap.keySet()){
//									int index=titleIndexMap.get(key);
//									Cell cell=row.getCell(index,Row.CREATE_NULL_AS_BLANK);
//									String value=cell.toString();
//									if(StringUtils.equals(key,"Product_feature")){//织物结构
//										if(StringUtils.isBlank(value)){
//											eim.addMessage(i+1,index+1,"织物结构不能为空");
//										}else{
//											cp.setProductFeature(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "raw_material")){//原料规格
//										if(StringUtils.isBlank(value)){
//											eim.addMessage(i+1,index+1,"原料规格不能为空");
//										}else{
//											cp.setRawMaterial(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "Item_number")){//物料代码
//										if(StringUtils.isBlank(value)){
//											eim.addMessage(i+1,index+1,"物料代码不能为空");
//										}else{
//											cp.setItemNumber(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "Nominal_area_weight")){//单位面积质量
//										if(StringUtils.isBlank(value)){
//											eim.addMessage(i+1,index+1,"单位面积质量不能为空");
//										}else if(!this.isNumber(value)){
//											eim.addMessage(i+1,index+1,"单位面积质量只能是数字");
//										}else{
//											cp.setNominalAreaWeight(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "Reed")){//钢筘规格
//										if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//											cp.setReed("");
//										}else{
//											cp.setReed(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "Guide_needle")){//导纱针规格
//										if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//											cp.setGuideNeedle("");
//										}else{
//											cp.setGuideNeedle(value);
//										}
//										continue;
//									}
//									
//									if(StringUtils.equals(key, "Remark")){//备注
//										if(StringUtils.isBlank(value) || StringUtils.equals("-",value.trim()) || StringUtils.equals("/",value.trim())){
//											cp.setRemark("");
//										}else{
//											cp.setRemark(value);
//										}
//									}
//									
//								}//end for key
//								data.add(cp);
//							}
//						}//数据 end
//					}
//
//				}
//				
//			}//end for
//			if(fa==-1){
//				eim.addMessage("第二个工作表\"结构参数\"标题不存在");
//			}
//			
//			if(fb==-1){
//				eim.addMessage("第二个工作表\"缝编针数\"不存在");
//			}
//			
//			List<Material> plist=fTc_BomDao.findAll(Material.class);
//			
//			//验证合法性
//			for(int i=0;i<data.size();i++){
//				String name=data.get(i).getProductFeature();	//原料名称
//				String model=data.get(i).getRawMaterial();		//原料规格
//				String itemNumber=data.get(i).getItemNumber();	//物料代码
//				String weightPerSquareMetre=data.get(i).getNominalAreaWeight();	//单位面积质量
//				if(StringUtils.isNotBlank(model) && StringUtils.isNotBlank(itemNumber)){
//					boolean findModel=false;	//ture为原料信息中有该信息
//					for(Material m:plist){
//						//TODO 原料信息修改
//						if(model.equals(m.getMaterialModel())&&itemNumber.equals(null)){
//							findModel=true;
//							break;
//						}
//					}
//					if(!findModel){
//						eim.addMessage("第 "+(titleIndexMap.get("baseIndex")+i)+"行:原料规格("+model+")或物料代码("+itemNumber+")在原料信息中不存在");
//					}
//				}
//				
//			}
//			if(!eim.hasError()){
//				//删除以前的非套材BOM明细
//				Map<String,Object> map=new HashMap<>();
//				map.put("ftcBomVersionId", ftcBomVersion.getId());
//				fTc_BomDao.delete(FtcBomDetail.class, map);
//				
//				//把数据放入非套材BOM明细列表
////				List<FtcBomDetail> detailList=new ArrayList<>();
//				for(ConstructionParameter cp : data){
//					FtcBomDetail detail=new FtcBomDetail();
//					detail.setFtcBomVersionId(ftcBomVersion.getId());		//非套材BOM版本信息ID
//					detail.setFtcBomDetailName(cp.getProductFeature());		//原料名称
//					detail.setFtcBomDetailModel(cp.getRawMaterial());		//原料规格
//					detail.setFtcBomDetailItemNumber(cp.getItemNumber());	//物料代码
//					detail.setFtcBomDetailWeightPerSquareMetre(Double.valueOf(cp.getNominalAreaWeight()));//单位面积质量
//					detail.setFtcBomDetailReed(cp.getReed());				//钢筘规格
//					detail.setFtcBomDetailGuideNeedle(cp.getGuideNeedle());	//导纱针规格
//					detail.setFtcBomDetailRemark(cp.getRemark());			//备注
////							detailList.add(detail);
//					fTc_BomDao.save(detail);
//				}
////						fTc_BomDao.saveList(detailList);
//			}else{
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	//手动回滚事物
//			}
            savePdfFile(ftcBomVersion, att, eim);
        }
        return eim;
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) {
        return fTc_BomDao.findPageInfo1(filter, page);
    }

    public void savePdfFile(FtcBomVersion ftcBomVersion, Attachment att, ExcelImportMessage eim) throws Exception {
        FtcBom ftcBom = fTc_BomDao.findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
        String msg = "";
        List<String> paths = new ArrayList<>();
        try {
            com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
            for (int i = 1; i < workbook.getWorksheets().getCount(); i++) {
                Worksheet ws = workbook.getWorksheets().get(i);
                if (ws.isVisible()) {
                    ws.setVisible(false);
                }
            }
            File file = new File(att.getFilePath());
            for (int j = 0; j < workbook.getWorksheets().getCount(); j++) {
                Worksheet ws = workbook.getWorksheets().get(j);
                String path = file.getParent() + File.separator + ftcBom.getFtcProcBomCode() + "_" + ftcBomVersion.getFtcProcBomVersionCode() + "_" + ws.getName() + "(" + ftcBomVersion.getFtcProcBomVersionCode() + ").pdf";
                workbook.save(path);
                path = path.substring(path.lastIndexOf("upload"));
                path = path.replace("upload","\\upload\\");
                paths.add(path);
                if (j < workbook.getWorksheets().getCount() - 1) {
                    workbook.getWorksheets().get(j + 1).setVisible(true);
                    workbook.getWorksheets().get(j).setVisible(false);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
            msg = "ERROR";
        }

        if (msg.contains("ERROR")) {
            eim.addMessage("pdf转换失败");
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("bomName", ftcBom.getFtcProcBomName());
            param.put("processBomCode", ftcBom.getFtcProcBomCode());
            param.put("processBomVersion", ftcBomVersion.getFtcProcBomVersionCode());
            List<BomFile> list = fTc_BomDao.findListByMap(BomFile.class, param);
            BomFile bomFile;
            if (list.isEmpty()) {
                bomFile = new BomFile();
                bomFile.setBomName(ftcBom.getFtcProcBomName());
                bomFile.setProcessBomCode(ftcBom.getFtcProcBomCode());
                bomFile.setProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());
                bomFile.setFileName(att.getFileName());
                bomFile.setFilePath(att.getFilePath());
                bomFile.setUploadTime(new Date());
                fTc_BomDao.save(bomFile);
            } else {
                bomFile = list.get(0);
                bomFile.setBomName(ftcBom.getFtcProcBomName());
                bomFile.setProcessBomCode(ftcBom.getFtcProcBomCode());
                bomFile.setProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());
                bomFile.setFileName(att.getFileName());
                bomFile.setFilePath(att.getFilePath());
                bomFile.setUploadTime(new Date());
                fTc_BomDao.update(bomFile);
            }
            param.clear();
            param.put("bomFileId", bomFile.getId());
            fTc_BomDao.delete(BomFilePdf.class, param);

            com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(att.getFilePath());
            for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                if(workbook.getWorksheets().get(i).isVisible()){
                    BomFilePdf bomFilePdf = new BomFilePdf();
                    bomFilePdf.setBomFileId(bomFile.getId());
                    bomFilePdf.setPDFfilePath(paths.get(i));
                    fTc_BomDao.save(bomFilePdf);
                }
            }
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
     * 结构参数
     */
    private class ConstructionParameter {

        /**
         * 织物结构
         */
        private String productFeature;

        /**
         * 原料规格
         */
        private String rawMaterial;

        /**
         * 物料代码
         */
        private String itemNumber;

        /**
         * 单位面积质量
         */
        private String nominalAreaWeight;

        /**
         * 钢筘规格
         */
        private String reed;

        /**
         * 导纱针规格
         */
        private String guideNeedle;

        /**
         * 备注
         */
        private String remark;

        public String getProductFeature() {
            return productFeature;
        }

        public void setProductFeature(String productFeature) {
            this.productFeature = productFeature;
        }

        public String getRawMaterial() {
            return rawMaterial;
        }

        public void setRawMaterial(String rawMaterial) {
            this.rawMaterial = rawMaterial;
        }

        public String getItemNumber() {
            return itemNumber;
        }

        public void setItemNumber(String itemNumber) {
            this.itemNumber = itemNumber;
        }

        public String getNominalAreaWeight() {
            return nominalAreaWeight;
        }

        public void setNominalAreaWeight(String nominalAreaWeight) {
            this.nominalAreaWeight = nominalAreaWeight;
        }

        public String getReed() {
            return reed;
        }

        public void setReed(String reed) {
            this.reed = reed;
        }

        public String getGuideNeedle() {
            return guideNeedle;
        }

        public void setGuideNeedle(String guideNeedle) {
            this.guideNeedle = guideNeedle;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
