/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.dao.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.printer.dao.IPrinterDao;
import org.xdemo.superutil.j2se.ReflectUtils;

/**
 *
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class PrinterDaoImpl extends BaseDaoImpl implements IPrinterDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"printer-list");
	}
	@Override
	public List<Map<String,Object>> getDaiyRollCount(String barc){
		HashMap<String,Object> map=new HashMap<String,Object>();

		map.put("barc", barc);
		return findListMapByMap(SQL.get("printer-daliy-roll"), map);
	}

	@Override
	public List<Map<String,Object>> getDaiyPartCount(String barc){
		HashMap<String,Object> map=new HashMap<String,Object>();

		map.put("barc", barc);
		return findListMapByMap(SQL.get("printer-daliy-part"), map);
	}
	@Override
	public List<Map<String,Object>> getDaiyBoxCount(String barc){
		HashMap<String,Object> map=new HashMap<String,Object>();

		map.put("barc", barc);
		return findListMapByMap(SQL.get("printer-daliy-box"), map);
	}
	@Override
	public List<Map<String,Object>> getDaiyTrayCount(String barc){
		HashMap<String,Object> map=new HashMap<String,Object>();

		map.put("barc", barc);
		return findListMapByMap(SQL.get("printer-daliy-tray"), map);
	}

	public void insert(Object... object) throws Exception {
		StringBuffer sb = null;
		SQLQuery query = null;
		for (Object o : object) {
			Class<? extends Object> clazz = o.getClass();

			sb = new StringBuffer("insert " + clazz.getAnnotation(Table.class).name() + " ");

			List<Field> fields = ReflectUtils.getFields(clazz, true);
			String insert1 = "(";
			String insert2 = "(";
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getAnnotation(Transient.class) != null) continue;
				if (field.get(o) != null && !field.getName().equalsIgnoreCase("id")) {
					insert1 += field.getName() + ",";
					insert2 += ":" + field.getName() + ",";
				}
			}

			insert1 = insert1.substring(0, insert1.length() - 1) + ")";
			insert2 = insert2.substring(0, insert2.length() - 1) + ")";

			sb.append(insert1);
			sb.append("values");
			sb.append(insert2);
			query = getSession().createSQLQuery(sb.toString());

			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getAnnotation(Transient.class) != null) continue;
				if (field.get(o) != null && !field.getName().equalsIgnoreCase("id")) {
					query.setParameter(field.getName(), field.get(o));
				}
			}
			query.executeUpdate();
		}
	}


}
