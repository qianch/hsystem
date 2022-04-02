/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package ${package};

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;
import ${package2}.I${entity}Dao;

/**
 * 
 * @author ${author}
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ${entity}DaoImpl extends BaseDaoImpl implements I${entity}Dao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"${entity2}-list");
	}

}
