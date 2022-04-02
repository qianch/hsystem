/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.bluebirdme.mes.core.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.MapUtils;



import com.bluebirdme.mes.baseInfo.dao.IFtcBomDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionDao;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.produce.dao.IFinishedProductCategoryDao;
import com.bluebirdme.mes.produce.dao.IFinishedProductDao;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.dao.IConsumerDao;
import com.bluebirdme.mes.sales.entity.Consumer;

/**
 *
 * @author 宋黎明
 * @Date 2016-9-30 10:49:34
 */
@Service
@AnyExceptionRollback
public class FinishedProductServiceImpl extends BaseServiceImpl implements IFinishedProductService {

	@Resource
	IFinishedProductDao finishProductDao;

	@Resource
	IConsumerDao consumerDao;

	@Resource
	IFinishedProductCategoryDao finishedProductCategoryDao;

	@Autowired
	public HttpSession session;


	@Override
	protected IBaseDao getBaseDao() {
		return finishProductDao;
	}

	Map<String, Object> map =null;

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return finishProductDao.findPageInfo(filter, page);
	}
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
		return finishProductDao.findPageInfo2(filter, page);
	}

	public void delete(String ids) {
		finishProductDao.delete(ids);
	}

	public List<Map<String, Object>> tcTree(String data) throws SQLTemplateException {
		List<Map<String, Object>> listMap = finishProductDao.findTcBom(data);
		Map<String, Object> ret = null;
		List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			map.put("state", "1");
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsString(map, "ID"));
			ret.put("text", MapUtils.getAsString(map, "TCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "TCPROCBOMCODE"));
			ret.put("attributes", map);
			ret.put("children", children(MapUtils.getAsLong(map, "ID")));
			listMap1.add(ret);

		}

		return listMap1;
	}

	private List<Map<String, Object>> children(Long id) {
		List<Map<String, Object>> listMap2 = new ArrayList<Map<String, Object>>();
		Map<String, Object> ret = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<TcBomVersion> list = finishProductDao.findTcV(id);
		for (TcBomVersion tc : list) {
			map.put("state", "2");
			ret = new HashMap<String, Object>();
			ret.put("id", tc.getId());
			ret.put("text", tc.getTcProcBomVersionCode());
			ret.put("attributes", map);
			listMap2.add(ret);
		}
		return listMap2;
	}

	public List<Map<String, Object>> ftcTree(String data) throws SQLTemplateException {
		List<Map<String, Object>> listMap = finishProductDao.findFtcBom(data);
		Map<String, Object> ret = null;
		List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			map.put("state", "1");
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsString(map, "ID"));
			ret.put("text", MapUtils.getAsString(map, "FTCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "FTCPROCBOMCODE"));
			ret.put("attributes", map);
			ret.put("children", ftcChildren(MapUtils.getAsLong(map, "ID")));
			listMap1.add(ret);
		}
		return listMap1;

	}

	private List<Map<String, Object>> ftcChildren(Long id) {
		List<Map<String, Object>> listMap2 = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> ret = null;
		List<FtcBomVersion> list = finishProductDao.findFtcV(id);
		for (FtcBomVersion ftc : list) {
			map.put("state", "2");
			ret = new HashMap<String, Object>();
			ret.put("id", ftc.getId());
			ret.put("text", ftc.getFtcProcBomVersionCode());
			ret.put("attributes", map);
			listMap2.add(ret);
		}
		return listMap2;
	}

	public List<Map<String, Object>> bcTree(String data) throws SQLTemplateException {
		List<Map<String, Object>> listMap = finishProductDao.findBcBom(data);
		Map<String, Object> ret = null;
		List<Map<String, Object>> listMap1 = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			map.put("state", "1");
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsString(map, "ID"));
			ret.put("text", MapUtils.getAsString(map, "PACKBOMGENERICNAME") + "/" + MapUtils.getAsString(map, "PACKBOMCODE"));
			ret.put("attributes", map);
			ret.put("children", bcChildren(MapUtils.getAsLong(map, "ID")));

			listMap1.add(ret);

		}

		return listMap1;
	}

	private List<Map<String, Object>> bcChildren(Long id) {
		List<Map<String, Object>> listMap2 = new ArrayList<Map<String, Object>>();
		List<BCBomVersion> list = finishProductDao.findBcV(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> ret = null;
		for (BCBomVersion bc : list) {
			map.put("state", "2");
			ret = new HashMap<String, Object>();
			ret.put("id", bc.getId());
			ret.put("text", bc.getPackVersion());
			ret.put("attributes", map);
			listMap2.add(ret);
		}
		return listMap2;
	}

	public void completeBomId() throws SQLTemplateException, IOException {
		List<BcBom> bcBoms=findAll(BcBom.class);
		for(BcBom bb:bcBoms){
			if(bb.getPackBomCode()==null){
				continue;
			}
			String bcBomCode=bb.getPackBomCode().split("//")[0];
			bb.setPackBomCode(bcBomCode);
			update(bb);
		}
		List<FinishedProduct> productList = findAll(FinishedProduct.class);
		StringBuilder sb = new StringBuilder();
		sb.append("产品ID\t厂内名称\t产品型号\t工艺代码\t工艺版本\t客户版本\t包装代码\t包装版本\t是否套材\r\n");
		for (FinishedProduct fp : productList) {
			if (fp.getProductIsTc() == 1) {// 套材为工艺版本必填
				boolean isSave = false;
				String procBomCode = fp.getProductProcessCode();
				String procVersion = fp.getProductProcessBomVersion();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tcProcBomCode", procBomCode);
				List<TcBom> bomList = findListByMap(TcBom.class, map);
				if (bomList.size() == 1) {
					map.clear();
					map.put("tcProcBomId", bomList.get(0).getId());
					List<TcBomVersion> versionList = findListByMap(TcBomVersion.class, map);
					if (versionList.size() ==1) {
						fp.setProductProcessCode(bomList.get(0).getTcProcBomCode()+"//"+bomList.get(0).getTcProcBomName());
						fp.setProductProcessBomVersion(versionList.get(0).getTcProcBomVersionCode());
						fp.setProductConsumerBomVersion(versionList.get(0).getTcConsumerVersionCode());
						fp.setProcBomId(versionList.get(0).getId());
					} else if (versionList.size() > 1) {
						for(TcBomVersion fb:versionList){
							if(fb.getAuditState()>0){
							fp.setProductProcessCode(bomList.get(0).getTcProcBomCode()+"//"+bomList.get(0).getTcProcBomName());
							fp.setProductProcessBomVersion(fb.getTcProcBomVersionCode());
							fp.setProductConsumerBomVersion(fb.getTcConsumerVersionCode());
							fp.setProcBomId(fb.getId());
							}
						}
						isSave = true;
					} else {

						System.out.println("套材ID：" + fp.getId() + "工艺：" + procBomCode + "/" + procVersion);
					}
				} else if (bomList.size() > 1) {
					isSave = true;
				} else {
					isSave = true;
					System.out.println("套材ID：" + fp.getId() + "工艺：" + procBomCode + "/" + procVersion);
				}
				if (isSave)
					sb.append(fp.getId() + "\t" + fp.getFactoryProductName() + "\t" + fp.getProductModel() + "\t" + fp.getProductProcessCode() + "\t" + fp.getProductProcessBomVersion() + "\t " + fp.getProductPackagingCode() + "\t " + fp.getProductPackageVersion() + "\t套材\r\n");

			} else {// 非套材为包装和工艺版本都必填
				boolean isSave = false;
				String procBomCode = fp.getProductProcessCode();
				String procVersion = fp.getProductProcessBomVersion();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ftcProcBomCode", procBomCode);
				List<FtcBom> bomList = findListByMap(FtcBom.class, map);
				if (bomList.size() == 1) {
					map.clear();
					map.put("ftcProcBomId", bomList.get(0).getId());

					List<FtcBomVersion> versionList = findListByMap(FtcBomVersion.class, map);
					if (versionList.size() == 1) {
						fp.setProductProcessCode(bomList.get(0).getFtcProcBomCode()+"//"+bomList.get(0).getFtcProcBomName());
						fp.setProductProcessBomVersion(versionList.get(0).getFtcProcBomVersionCode());
						fp.setProductConsumerBomVersion(versionList.get(0).getFtcConsumerVersionCode());
						fp.setProcBomId(versionList.get(0).getId());
					} else if (versionList.size() > 1) {
						for(FtcBomVersion fb:versionList){
							if(fb.getAuditState()>0){
								fp.setProductProcessCode(bomList.get(0).getFtcProcBomCode()+"//"+bomList.get(0).getFtcProcBomName());
								fp.setProductProcessBomVersion(fb.getFtcProcBomVersionCode());
								fp.setProductConsumerBomVersion(fb.getFtcConsumerVersionCode());
								fp.setProcBomId(fb.getId());
							}

						}
						isSave = true;
					} else {
						isSave = true;
						System.out.println("非套材ID：" + fp.getId() + "    工艺BOM未写入" + procBomCode + "/" + procVersion);
					}
				}
				else if (bomList.size() > 1) {
					isSave = true;
				} else {
					isSave = true;
					System.out.println("非套材ID：" + fp.getId() + "    工艺BOM未写入" + procBomCode + "/" + procVersion);
				}
				String packBomCode = fp.getProductPackagingCode();
				String packVersion = fp.getProductPackageVersion();
				map.clear();
				map.put("packBomCode", packBomCode);
				List<BcBom> bcBomList = findListByMap(BcBom.class, map);
				if (bcBomList.size() == 1) {
					map.clear();
					map.put("packIsDefault", 1);
					map.put("packBomId", bcBomList.get(0).getId());
					List<BCBomVersion> versionList = findListByMap(BCBomVersion.class, map);
					if (versionList.size() == 1) {
						fp.setProductPackagingCode( bcBomList.get(0).getPackBomCode()+ "//"+ bcBomList.get(0).getPackBomName());
						fp.setProductPackageVersion(versionList.get(0).getPackVersion());
						fp.setPackBomId(versionList.get(0).getId());
					}
					else if (versionList.size() > 1) {
						for(BCBomVersion bv:versionList){
							fp.setProductPackagingCode( bcBomList.get(0).getPackBomCode()+ "//"+ bcBomList.get(0).getPackBomName());
							fp.setProductPackageVersion(bv.getPackVersion());
							fp.setPackBomId(bv.getId());
						}
						isSave = true;
					} else {
						isSave = true;
						System.out.println("非套材ID：" + fp.getId() + "    包装BOM未写入:" + packBomCode + "/" + packVersion);
					}
				}
				if (bcBomList.size() > 1) {
					map.clear();
					map.put("packIsDefault", 1);
					map.put("packBomId", bcBomList.get(0).getId());
					List<BCBomVersion> versionList = findListByMap(BCBomVersion.class, map);
					if (versionList.size() == 1) {
						fp.setProductPackagingCode( bcBomList.get(0).getPackBomCode()+ "//"+ bcBomList.get(0).getPackBomName());
						fp.setProductPackageVersion(versionList.get(0).getPackVersion());
						fp.setPackBomId(versionList.get(0).getId());
					}
					else if (versionList.size() > 1) {
						for(BCBomVersion bv:versionList){
							fp.setProductPackagingCode( bcBomList.get(0).getPackBomCode()+ "//"+ bcBomList.get(0).getPackBomName());
							fp.setProductPackageVersion(bv.getPackVersion());
							fp.setPackBomId(bv.getId());
						}
						isSave = true;
					} else {
						isSave = true;
						System.out.println("非套材ID：" + fp.getId() + "    包装BOM未写入:" + packBomCode + "/" + packVersion);
					}
					isSave = true;
				} else {
					isSave = true;
					System.out.println("非套材ID：" + fp.getId() + "    包装BOM未写入:" + packBomCode + "/" + packVersion);
				}
				if (isSave)
					sb.append(fp.getId() + "\t" + fp.getFactoryProductName() + "\t" + fp.getProductModel() + "\t" + fp.getProductProcessCode() + "\t" + fp.getProductProcessBomVersion() + "\t" + fp.getProductPackagingCode() + "\t" + fp.getProductPackageVersion() + "\t \r\n");

			}
//			update(fp);
		}
		FileUtils.writeToFile(sb.toString(), "D:\\错误的产品BOM.txt", true, true, Charset.forName("UTF-8"));
	}

	@Override
	public void updates(Long id) {
		finishProductDao.updates(id);
	}

	@Override
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception {
		return finishProductDao.findPageInfoDelivery(filter, page);
	}

	/**
	 * 产成品汇总（按成品类别）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> productsSummary(Filter filter, Page page) throws Exception {
		// TODO Auto-generated method stub
//		Map<String, Object> map1 = finishProductDao.productsSummary(filter, page);
//		List<Object> list = (List<Object>) map1.get("rows");
//		List<Object> list2 = new ArrayList<Object>();//存放页面显示的数据
//		for(int i = 0;i < list.size();i++){
//			Map<String, Object> map2 = (Map<String, Object>) list.get(i);//取到每一个rows
//			map = new HashMap<String, Object>();
//
//			Double startWeight = (Double) map2.get("STARTWEIGHT");
//			Double inWeight = (Double) map2.get("INWEIGHT");
//			Double outWeight = (Double) map2.get("OUTWEIGHTS");
//			Double useNum = (Double) map2.get("USESUM");
//
//			//成品大类
//			map.put("CATEGORYNAME", map2.get("CATEGORYNAME"));
//			//成品类别代码
//			map.put("CATEGORYCODE", map2.get("CATEGORYCODE"));
//			//月初库存数量
//			map.put("STARTWEIGHT", startWeight == null ? 0 :startWeight);
//			//当月入库数量
//			map.put("INWEIGHT", inWeight == null ? 0 :inWeight);
//			//当月发出数量
//			map.put("OUTWEIGHTS", outWeight == null ? 0 : outWeight);
//			//当月领用数量
//			map.put("USENUM", useNum == null ? 0 : useNum);
//			//月末累计数量
//
//			if(startWeight == null)startWeight = 0.0;
//			if(inWeight == null)inWeight = 0.0;
//			if(outWeight == null)outWeight = 0.0;
//			if(useNum == null)useNum = 0.0;
//			map.put("ATUM", startWeight+inWeight-useNum-outWeight);
//			list2.add(map);
//		}
//		map = new HashMap<String, Object>();
//		map.put("total", map1.get("total"));
//		map.put("rows", list2);
		return finishProductDao.productsSummary(filter, page);
	}


	/**
	 * 产成品汇总（按厂内名称）
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> productsFactorySummary(Filter filter, Page page) throws Exception {
		// TODO Auto-generated method stub
		return finishProductDao.productsFactorySummary(filter, page);
	}

	/**
	 * 产成品汇总（订单号、批次号、厂内名称）
	 */
	@Override
	public Map<String, Object> productsSundrySummary(Filter filter, Page page) throws Exception {
		// TODO Auto-generated method stub
		return finishProductDao.productsSundrySummary(filter, page);
	}

	@Override
	public Map<String, Object> productsCustomerStockSummary(Filter filter,Page page) throws Exception {
		// TODO Auto-generated method stub
		return finishProductDao.productsCustomerStockSummary(filter, page);
	}

	/**
	 * Excel导入保存
	 */
/*	@Override
	public ExcelImportMessage saveFinishedProductFromExcel(ExcelContent content)
			throws NoSuchAlgorithmException {
		ExcelImportMessage msg=new ExcelImportMessage();
		List<String[]> data=content.getData();

		FinishedProduct finishedProduct = null;

		String[] temp=null;

		for(int i=0;i < data.size();i++){
			finishedProduct = new FinishedProduct();
			temp = data.get(i);
			Consumer consumer = consumerDao.findOne(Consumer.class,"consumerCode",temp[1]);
			finishedProduct.setProductConsumerId(consumer.getId());//客户ID
			finishedProduct.setConsumerProductName(temp[2]);//客户产品名称


			if(temp[3]!=null &&!"".equals(temp[3])){
				finishedProduct.setFactoryProductName(temp[3]);//厂内名称
			}else{//根据规则自动生成：规格-门幅mm-卷长m/卷重kg
				String productModel=temp[24];		//规格
				String productWidth=temp[4];		//门幅
				String productRollLength=temp[5];	//卷长
				String productRollWeight=temp[6];	//卷重

				String factoryProductName="";
				if (productWidth != null && !productWidth.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productWidth + "mm";
					} else {
						factoryProductName += "-" + productWidth
								+ "mm";
					}

				}
				if (productRollLength != null && !productRollLength.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productRollLength + "mm";
					} else {
						factoryProductName += "-" + productRollLength + "m";
					}
				}
				if (productRollWeight != null && !productRollWeight.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productRollWeight+ "kg";
					} else if (productRollLength != null && !factoryProductName.equals("")) {
						factoryProductName += "/" + productRollWeight + "kg";
					} else if (!"".equals(factoryProductName) && ( productRollLength == null ||productRollLength.equals(""))) {
						factoryProductName += "-" + productRollWeight + "kg";
					}
				}
				if (productModel != null && !productModel.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productModel;
					} else {
						factoryProductName = productModel + "-" + factoryProductName;
					}
				}
				finishedProduct.setFactoryProductName(factoryProductName);
			}

			if(!"".equals(temp[4]) && temp[4]!=null){
				if(isDouble(temp[4])){
					double productWidth = Double.parseDouble(temp[4]);
					finishedProduct.setProductWidth(productWidth);//门幅
				}
			}
			if(!"".equals(temp[5]) && temp[5]!=null){
				if(isDouble(temp[5])){
					double productRollLength = Double.parseDouble(temp[5]);
					finishedProduct.setProductRollLength(productRollLength);//卷长
				}
			}
			if(!"".equals(temp[6]) && temp[6]!=null){
				if(isDouble(temp[6])){
					double productRollWeight = Double.parseDouble(temp[6]);
					finishedProduct.setProductRollWeight(productRollWeight);//卷重
				}
			}
			finishedProduct.setProductTrayCode(temp[7]);//托唛头
			if(!"".equals(temp[8]) && temp[8]!=null){
				if(isDouble(temp[8])){
					double maxWeight = Double.parseDouble(temp[8]);
					finishedProduct.setMaxWeight(maxWeight);//最大卷重
				}
			}
			if(!"".equals(temp[9]) && temp[9]!=null){
				if(isDouble(temp[9])){
					double minWeight = Double.parseDouble(temp[9]);
					finishedProduct.setMinWeight(minWeight);//最小卷重
				}
			}
			finishedProduct.setProductRollCode(temp[10]);//卷标签
			finishedProduct.setProductBoxCode(temp[11]);//箱唛头
			*//*
			 * 产品类型
			 *//*
			if("套材".equals(temp[12])){
				finishedProduct.setProductIsTc(1);
			}else if("非套材".equals(temp[12])){
				finishedProduct.setProductIsTc(2);
			}else if("胚布".equals(temp[12])){
				finishedProduct.setProductIsTc(-1);
			}
			*//*
			 * 产品属性
			 *//*
			if("常规".equals(temp[13])){
				finishedProduct.setIsCommon(1);
			}else if("试样".equals(temp[13])){
				finishedProduct.setIsCommon(0);
			}else{
				finishedProduct.setIsCommon(null);
			}
			if(temp[14]!=null && !"".equals(temp[14])){
				finishedProduct.setProductProcessName(temp[14]);//工艺名称
				if("套材".equals(temp[12])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[16]);
					if(tcBom!=null && StringUtils.isNotBlank(tcBom.getTcProcBomName())){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class, "tcProcBomId", tcBom.getId());
						finishedProduct.setProcBomId(tcBomVersion.getId());//套材版本ID
					}
				}else if("非套材".equals(temp[12]) || "胚布".equals(temp[12])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
					if(ftcBom!=null && StringUtils.isNoneBlank(ftcBom.getFtcProcBomName())){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
						finishedProduct.setProcBomId(ftcBomVersion.getId());//非套材版本ID
					}
				}

			}else{//根据 工艺代码 获取 工艺名称
				if("套材".equals(temp[12])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[16]);
					if(tcBom!=null && StringUtils.isNotBlank(tcBom.getTcProcBomName())){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class, "tcProcBomId", tcBom.getId());
						finishedProduct.setProductProcessName(tcBom.getTcProcBomName());//工艺名称
						finishedProduct.setProcBomId(tcBomVersion.getId());//套材版本ID
					}
				}else if("非套材".equals(temp[12]) || "胚布".equals(temp[12])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
					if(ftcBom!=null && StringUtils.isNoneBlank(ftcBom.getFtcProcBomName())){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
						finishedProduct.setProductProcessName(ftcBom.getFtcProcBomName());//工艺名称
						finishedProduct.setProcBomId(ftcBomVersion.getId());//非套材版本ID
					}
				}
			}

			if(isDouble(temp[15])){
				Double productShelfLife = Double.parseDouble(temp[15]);
				finishedProduct.setProductShelfLife(productShelfLife.intValue());//保质期
			}
			finishedProduct.setProductProcessCode(temp[16]);//工艺代码

			if(!"".equals(temp[17]) && temp[17]!=null){
				finishedProduct.setProductProcessBomVersion(temp[17]);//工艺版本
			}else{//根据 工艺代码 获取 工艺版本
				if("套材".equals(temp[12])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[16]);
					if(tcBom != null){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class,"tcProcBomId",tcBom.getId());
						if(tcBomVersion != null){
							finishedProduct.setProductProcessBomVersion(tcBomVersion.getTcProcBomVersionCode());//工艺版本
						}
					}
				}else if("非套材".equals(temp[12]) || "胚布".equals(temp[12])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
					if(ftcBom != null){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
						if(ftcBomVersion != null){
							finishedProduct.setProductProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());//工艺版本
						}
					}

				}
			}

			if(!"".equals(temp[29]) && temp[29]!=null){
				finishedProduct.setProductConsumerBomVersion(temp[29]);//客户版本号
			}else{//根据 工艺代码 获取 客户版本号
				if("套材".equals(temp[12])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[16]);
					if(tcBom != null){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class,"tcProcBomId",tcBom.getId());
						if(tcBomVersion != null){
							finishedProduct.setProductConsumerBomVersion(tcBomVersion.getTcConsumerVersionCode());//客户版本
						}
					}
				}else if("非套材".equals(temp[12]) || "胚布".equals(temp[12])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[16]);
					if(ftcBom != null){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
						if(ftcBomVersion != null){
							finishedProduct.setProductConsumerBomVersion(ftcBomVersion.getFtcConsumerVersionCode());//客户版本
						}
					}

				}
			}

			finishedProduct.setProductPackagingCode(temp[18]);//包装代码
			if(StringUtils.isNoneBlank(temp[19])){//存在 包装版本
				finishedProduct.setProductPackageVersion(temp[19]);//包装版本
			}else{
				if("套材".equals(temp[12])){
					List<BcBom> bcBomList=finishProductDao.find(BcBom.class,"packBomCode",temp[18]);
					if(bcBomList !=null && bcBomList.size()==1){
						BCBomVersion bcBomVersion=finishProductDao.findOne(BCBomVersion.class,"packBomId",bcBomList.get(0).getId());
						if(bcBomVersion != null){
							finishedProduct.setProductPackageVersion(bcBomVersion.getPackVersion());//包装版本
						}
					}

				}
			}


			finishedProduct.setCarrierCode(temp[20]);//衬管编码
			FinishedProductCategory finishedProductCategory = finishedProductCategoryDao.findOne(FinishedProductCategory.class, "categoryCode", temp[21]);
			finishedProduct.setFpcid(finishedProductCategory.getId());//成品类别ID
			finishedProduct.setMaterielCode(temp[23]);//物料编码
			finishedProduct.setProductModel(temp[24]);//产品规格
			finishedProduct.setPackReq(temp[25]);//包装要求
			*//*
			 * 称重规则
			 *//*
			if("全称 (单卷重量300KG以上)".equals(temp[26])){
				finishedProduct.setProductWeigh(0);
			}else if("抽称 (单卷重量300KG以下)".equals(temp[26])){
				finishedProduct.setProductWeigh(1);
			}else if("不称 (套裁)".equals(temp[26])){
				finishedProduct.setProductWeigh(2);
			}else if("首卷称重 (成品胚布)".equals(temp[26])){
				finishedProduct.setProductWeigh(3);
			}
			finishedProduct.setProductMemo(temp[27]);//备注
			finishedProduct.setProcReq(temp[28]);//工艺要求
			finishedProduct.setAuditState(0);//审核状态--未提交
			finishedProduct.setAuditChange(1);//是否显示变更

			finishProductDao.save(finishedProduct);
		}
		return msg;
	}*/

	/**
	 * Excel导入保存
	 */
	@Override
	public ExcelImportMessage saveFinishedProductFromExcel(ExcelContent content)
			throws NoSuchAlgorithmException {
		ExcelImportMessage msg=new ExcelImportMessage();
		List<String[]> data=content.getData();

		FinishedProduct finishedProduct = null;

		String[] temp=null;

		for(int i=0;i < data.size();i++){
			finishedProduct = new FinishedProduct();
			temp = data.get(i);
			Consumer consumer = consumerDao.findOne(Consumer.class,"consumerCode",temp[1]);
			finishedProduct.setProductConsumerId(consumer.getId());//客户ID
			finishedProduct.setConsumerProductName(temp[2]);//客户产品名称

			if(!"".equals(temp[4]) && temp[4]!=null){
				if(isDouble(temp[4])){
					double productWidth = Double.parseDouble(temp[4]);
					finishedProduct.setProductWidth(productWidth);//门幅
				}
			}
			if(!"".equals(temp[5]) && temp[5]!=null){
				if(isDouble(temp[5])){
					double productRollLength = Double.parseDouble(temp[5]);
					finishedProduct.setProductRollLength(productRollLength);//卷长
				}
			}

			if(!"".equals(temp[6]) && temp[6]!=null){
				if(isDouble(temp[6])){
					double reserveLength = Double.parseDouble(temp[6]);
					finishedProduct.setReserveLength(reserveLength);//预留米长
				}
			}

			if(!"".equals(temp[7]) && temp[7]!=null){
				if(isDouble(temp[7])){
					double productRollWeight = Double.parseDouble(temp[7]);
					finishedProduct.setProductRollWeight(productRollWeight);//卷重
				}
			}
			finishedProduct.setProductTrayCode(temp[8]);//托唛头
			if(!"".equals(temp[7]) && temp[7]!=null){
				if(isDouble(temp[7])){
					double maxWeight = Double.parseDouble(temp[7]);
					maxWeight =maxWeight*1.03;
					BigDecimal b = new BigDecimal(maxWeight);
					double maxWeight1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					finishedProduct.setMaxWeight(maxWeight1);//最大卷重
				}
			}
			if(!"".equals(temp[7]) && temp[7]!=null){
				if(isDouble(temp[7])){
					double minWeight = Double.parseDouble(temp[7]);
					minWeight = minWeight*0.97;
					BigDecimal b = new BigDecimal(minWeight);
					double minWeight1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					finishedProduct.setMinWeight(minWeight1);//最小卷重
				}
			}
			finishedProduct.setProductRollCode(temp[11]);//卷标签
			finishedProduct.setProductBoxCode(temp[12]);//箱唛头
			/*
			 * 产品类型
			 */
			if("套材".equals(temp[13])){
				finishedProduct.setProductIsTc(1);
			}else if("非套材".equals(temp[13])){
				finishedProduct.setProductIsTc(2);
			}else if("胚布".equals(temp[13])){
				finishedProduct.setProductIsTc(-1);
			}
			/*
			 * 产品属性
			 */
			if("常规".equals(temp[14])){
				finishedProduct.setIsCommon(1);
			}else if("试样".equals(temp[14])){
				finishedProduct.setIsCommon(0);
			}else{
				finishedProduct.setIsCommon(null);
			}
			if(temp[15]!=null && !"".equals(temp[15])){
				finishedProduct.setProductProcessName(temp[15]);//工艺名称
				if("套材".equals(temp[13])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[17]);
					if(tcBom!=null && StringUtils.isNotBlank(tcBom.getTcProcBomName())){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class, "tcProcBomId", tcBom.getId());
						finishedProduct.setProcBomId(tcBomVersion.getId());//套材版本ID
					}
				}else if("非套材".equals(temp[13]) || "胚布".equals(temp[13])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[17]);
					if(ftcBom!=null && StringUtils.isNoneBlank(ftcBom.getFtcProcBomName())){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
						finishedProduct.setProcBomId(ftcBomVersion.getId());//非套材版本ID
					}
				}

			}else{//根据 工艺代码 获取 工艺名称
				if("套材".equals(temp[13])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[17]);
					if(tcBom!=null && StringUtils.isNotBlank(tcBom.getTcProcBomName())){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class, "tcProcBomId", tcBom.getId());
						finishedProduct.setProductProcessName(tcBom.getTcProcBomName());//工艺名称
						temp[15]=tcBom.getTcProcBomName();
						finishedProduct.setProcBomId(tcBomVersion.getId());//套材版本ID
					}
				}else if("非套材".equals(temp[13]) || "胚布".equals(temp[13])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[17]);
					if(ftcBom!=null && StringUtils.isNoneBlank(ftcBom.getFtcProcBomName())){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class, "ftcProcBomId", ftcBom.getId());
						finishedProduct.setProductProcessName(ftcBom.getFtcProcBomName());//工艺名称
						temp[15]=ftcBom.getFtcProcBomName();
						finishedProduct.setProcBomId(ftcBomVersion.getId());//非套材版本ID
					}
				}
			}

			if(temp[3]!=null &&!"".equals(temp[3])){
				finishedProduct.setFactoryProductName(temp[3]);//厂内名称
			}else{//根据规则自动生成：规格-门幅mm-卷长m/卷重kg
				String productModel=temp[15];		//规格
				String productWidth=temp[4];		//门幅
				String productRollLength=temp[5];	//卷长
				String productRollWeight=temp[7];	//卷重

				String factoryProductName="";
				if (productWidth != null && !productWidth.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productWidth + "mm";
					} else {
						factoryProductName += "-" + productWidth
								+ "mm";
					}

				}
				if (productRollLength != null && !productRollLength.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productRollLength + "mm";
					} else {
						factoryProductName += "-" + productRollLength + "m";
					}
				}
				if (productRollWeight != null && !productRollWeight.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productRollWeight+ "kg";
					} else if (productRollLength != null && !factoryProductName.equals("")) {
						factoryProductName += "/" + productRollWeight + "kg";
					} else if (!"".equals(factoryProductName) && ( productRollLength == null ||productRollLength.equals(""))) {
						factoryProductName += "-" + productRollWeight + "kg";
					}
				}
				if (productModel != null && !productModel.equals("")) {
					if ("".equals(factoryProductName)) {
						factoryProductName += productModel;
					} else {
						factoryProductName = productModel + "-" + factoryProductName;
					}
				}
				finishedProduct.setFactoryProductName(factoryProductName);
			}


			if(isDouble(temp[16])){
				Double productShelfLife = Double.parseDouble(temp[16]);
				finishedProduct.setProductShelfLife(productShelfLife.intValue());//保质期
			}
			finishedProduct.setProductProcessCode(temp[17]);//工艺代码

			if(!"".equals(temp[18]) && temp[18]!=null){
				finishedProduct.setProductProcessBomVersion(temp[18]);//工艺版本
			}else{//根据 工艺代码 获取 工艺版本
				if("套材".equals(temp[13])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[17]);
					if(tcBom != null){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class,"tcProcBomId",tcBom.getId());
						if(tcBomVersion != null){
							finishedProduct.setProductProcessBomVersion(tcBomVersion.getTcProcBomVersionCode());//工艺版本
						}
					}
				}else if("非套材".equals(temp[13]) || "胚布".equals(temp[13])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[17]);
					if(ftcBom != null){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
						if(ftcBomVersion != null){
							finishedProduct.setProductProcessBomVersion(ftcBomVersion.getFtcProcBomVersionCode());//工艺版本
						}
					}

				}
			}

			if(!"".equals(temp[31]) && temp[31]!=null){
				finishedProduct.setProductConsumerBomVersion(temp[31]);//客户版本号
			}else{//根据 工艺代码 获取 客户版本号
				if("套材".equals(temp[13])){
					TcBom tcBom=finishProductDao.findOne(TcBom.class,"tcProcBomCode",temp[17]);
					if(tcBom != null){
						TcBomVersion tcBomVersion=finishProductDao.findOne(TcBomVersion.class,"tcProcBomId",tcBom.getId());
						if(tcBomVersion != null){
							finishedProduct.setProductConsumerBomVersion(tcBomVersion.getTcConsumerVersionCode());//客户版本
						}
					}
				}else if("非套材".equals(temp[13]) || "胚布".equals(temp[13])){
					FtcBom ftcBom=finishProductDao.findOne(FtcBom.class,"ftcProcBomCode",temp[17]);
					if(ftcBom != null){
						FtcBomVersion ftcBomVersion=finishProductDao.findOne(FtcBomVersion.class,"ftcProcBomId",ftcBom.getId());
						if(ftcBomVersion != null){
							finishedProduct.setProductConsumerBomVersion(ftcBomVersion.getFtcConsumerVersionCode());//客户版本
						}
					}

				}
			}

			finishedProduct.setProductPackagingCode(temp[19]);//包装代码
			if(StringUtils.isNoneBlank(temp[20])){//存在 包装版本
				finishedProduct.setProductPackageVersion(temp[20]);//包装版本
			}else{
				if("套材".equals(temp[13])){
					List<BcBom> bcBomList=finishProductDao.find(BcBom.class,"packBomCode",temp[19]);
					if(bcBomList !=null && bcBomList.size()==1){
						BCBomVersion bcBomVersion=finishProductDao.findOne(BCBomVersion.class,"packBomId",bcBomList.get(0).getId());
						if(bcBomVersion != null){
							finishedProduct.setProductPackageVersion(bcBomVersion.getPackVersion());//包装版本
						}
					}

				}
			}


			finishedProduct.setCarrierCode(temp[21]);//衬管编码
			FinishedProductCategory finishedProductCategory = finishedProductCategoryDao.findOne(FinishedProductCategory.class, "categoryCode", temp[23]);
			finishedProduct.setFpcid(finishedProductCategory.getId());//成品类别ID
			finishedProduct.setMaterielCode(temp[25]);//物料编码
			finishedProduct.setProductModel(temp[15]);//产品规格同工艺名称
			finishedProduct.setPackReq(temp[27]);//包装要求
			/*
			 * 称重规则
			 */
			if("全称 (单卷重量300KG以上)".equals(temp[28])){
				finishedProduct.setProductWeigh(0);
			}else if("抽称 (单卷重量300KG以下)".equals(temp[28])){
				finishedProduct.setProductWeigh(1);
			}else if("不称 (套裁)".equals(temp[28])){
				finishedProduct.setProductWeigh(2);
			}else if("首卷称重 (成品胚布)".equals(temp[28])){
				finishedProduct.setProductWeigh(3);
			}
			finishedProduct.setProductMemo(temp[29]);//备注
			finishedProduct.setProcReq(temp[30]);//工艺要求
			finishedProduct.setAuditState(0);//审核状态--未提交
			finishedProduct.setCustomerMaterialCodeOfFP(temp[32]);
			finishedProduct.setAuditChange(1);//是否显示变更
			finishedProduct.setCreater(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
			finishedProduct.setCreateTime(new Date());
			finishProductDao.save(finishedProduct);
		}
		return msg;
	}

	/**
	 * 判断字符串是不是数字
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str){
	    try{
	        Double.parseDouble(str);
	        return true;
	    }
	    catch(NumberFormatException e){
	        return false;
	    }
	}
	@Override
	public List<Map<String, Object>> findAllFinishProduct() throws Exception {
		// TODO Auto-generated method stub
		return finishProductDao.findAllFinishProduct();
	}
	@Override
	public int querySlBycode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.querySlBycode(wlbh);
	}
	@Override
	public int querySlinfo(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.querySlinfo(wlbh);
	}
	@Override
	public List<Map<String, Object>> checkYxInfo(String planCode)
			throws SQLTemplateException {
		return finishProductDao.checkYxInfo(planCode);
	}
	@Override
	public int queryCpggInfo(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryCpggInfo(wlbh);
	}
	@Override
	public List<Map<String, Object>> queryMfinfo(String wlbh, String mf)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryMfinfo(wlbh, mf);
	}
	@Override
	public int queryMfnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryMfnewcode(wlbh);
	}
	@Override
	public List<Map<String, Object>> queryJZinfo(String wlbh, String jz)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJZinfo(wlbh, jz);
	}
	@Override
	public int queryJznewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJznewcode(wlbh);
	}
	@Override
	public List<Map<String, Object>> queryJCinfo(String wlbh, String jc)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJCinfo(wlbh, jc);
	}
	@Override
	public int queryJcnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJcnewcode(wlbh);
	}
	@Override
	public List<Map<String, Object>> queryJZCinfo(String wlbh, String jz,
			String jc) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJZCinfo(wlbh, jz, jc);
	}
	@Override
	public int queryJzcnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryJzcnewcode(wlbh);
	}
	@Override
	public List<Map<String, Object>> queryGGinfo(String wlbh, String gg)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryGGinfo(wlbh, gg);
	}
	@Override
	public String getzgmcbycode(String code) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.getzgmcbycode(code);
	}
	@Override
	public List<Map<String, Object>> queryBcinfoByBcbm(String bcmb)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryBcinfoByBcbm(bcmb);
	}

	@Override
	public void resumeFinishProduct(Long id) {
		FinishedProduct fp = findById(FinishedProduct.class, id);
		fp.setObsolete(null);
		update(fp);
	}

	@Override
	public Map<String, Object> findPageInfo1(Filter filter, Page page) {
		return finishProductDao.findPageInfo1(filter,page);
	}

	@Override
	public List<Map<String, Object>> queryYxInfo(long finishedProductId)
			throws SQLTemplateException {
		return finishProductDao.queryYxInfo(finishedProductId);
	}
	 //产品的总克重
	@Override
	public  int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException {
		// TODO Auto-generated method stub
		return finishProductDao.queryProcBomDetail(ftcBomVersionId);
	}
}
