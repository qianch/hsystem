/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao.impl;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.dao.IMaterialStockDao;
import com.bluebirdme.mes.stock.entity.StockCheck;
import com.bluebirdme.mes.stock.entity.StockCheckResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class MaterialStockDaoImpl extends BaseDaoImpl implements IMaterialStockDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }


    @Override
    public Material findMaterial(String produceCategory, String materialModel) {
        String sql = "select * from hs_material where  materialModel=:m  LIMIT 1";
        return (Material) getSession().createSQLQuery(sql).addEntity(Material.class).setParameter("m", materialModel).uniqueResult();
    }

    @Override
    public String getSerial() throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public String validMaterialStockState(Long[] ids) {
        String sql = "SELECT GROUP_CONCAT(palletCode) from hs_material_stock_state where id in (:ids) and stockState=1";
        return (String) getSession().createSQLQuery(sql).setParameterList("ids", ids).uniqueResult();
    }

    public void move(Long[] ids, String warehouseCode, String warehousePosCode) {
        String sql = "update hs_material_stock_state set warehouseCode=:c,warehousePosCode=:pc where id in (:ids)";
        getSession().createSQLQuery(sql).setParameter("c", warehouseCode).setParameter("pc", warehousePosCode).setParameterList("ids", ids).executeUpdate();
    }

    public void checkResult(StockCheck sc) {
        save(sc);
        List<StockCheckResult> list = sc.getList();
        for (StockCheckResult scr : list) {
            scr.setCid(sc.getId());
        }
        saveList(list);
    }

    public String getOutWorkShop(String palletCode) {
        String sql = " SELECT " +
                "	o.workShop " +
                " FROM " +
                " 	hs_material_out_order_detail d " +
                " LEFT JOIN hs_material_stock_out o ON o.id = d.outOrderId " +
                " LEFT JOIN hs_material_stock_state s on d.mssId=s.id " +
                " where s.palletCode='" + palletCode + "' order by d.id desc limit 0,1";
        return (String) getSession().createSQLQuery(sql).uniqueResult();
    }
}
