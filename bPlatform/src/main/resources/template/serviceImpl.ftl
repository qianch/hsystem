/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package ${package};

import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import ${package2}.I${entity}Service;
import ${package3}.I${entity}Dao;

/**
 * 
 * @author ${author}
 * @Date ${date}
 */
@Service
@Transactional
public class ${entity}ServiceImpl extends BaseServiceImpl implements I${entity}Service {
	
	@Resource I${entity}Dao ${entity2}Dao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return ${entity2}Dao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return ${entity2}Dao.findPageInfo(filter,page);
	}

}
