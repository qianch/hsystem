/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsFinishedWeightEmbryoCloth;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 肖文彬
 * @Date 2016-10-9 9:19:51
 */

public interface ITcBomDao extends IBaseDao {
	//查询Bom
	public List<Map<String, Object>> findBom(String data,String state) throws SQLTemplateException;
	public List<Map<String, Object>> getTcBomJsonTest(String data,String state) throws SQLTemplateException;
	//获取工艺Bom下的版本
	public List<Map<String, Object>> findV(String id);
	//查询一级部件
	public List<Map<String, Object>> findP(String id);
	//删除部件
	public void deleteP(String ids);
	//查出一级部件的子部件
	public List<Map<String, Object>> findPC(String id);
	//按同级父部件查出下一级子节点
	public List<TcBomVersionParts> findParts(ArrayList<Long> list1);
	//查部件
	public List<TcBomVersionParts> findPP(Long id);
	//删除版本
	public void deleteV(String id);
	//删除套材bom版本时下的部件
	public void deleteAP();
	//删除套材bom版本时下的部件的明细
	public void deleteAPD();
	//查询部件列表
	public List<TcBomVersionParts> findAP(List<Long> list);
	//根据子部件删除下级部件
	public void deleteParts(Set<Long> partsId);
	//根据子部件删除对应明细
	public void deleteD(Set<Long> partsId);
	//查询版本下的部件
	public List<TcBomVersionParts> find(String id);
	//查询成品ftc信息
//	public List<Map<String,Object>> findFtc(String id);
	public Map<String,Object> findFtc(Filter filter, Page page);
	//查询bom客户信息
	public List<Map<String,Object>> findConsumer(String id);
	//查询部件下的明细
	public List<TcBomVersionPartsDetail> findPartsDetais(Long id);
	//查询部件下的成品重量胚布信息
	public List<TcBomVersionPartsFinishedWeightEmbryoCloth> findPartsFinishedWeightEmbryoCloths(Long id);
	public List<Map<String, Object>> getTcBomJsonTest1(String data,String state) throws SQLTemplateException ;
}
