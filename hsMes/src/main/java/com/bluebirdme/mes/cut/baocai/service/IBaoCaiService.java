/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.baocai.service;


import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.cut.baocai.entity.BaoCaiKu;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 季晓龙
 * @Date 2018-8-20 11:10:00
 */
public interface IBaoCaiService extends IBaseService {

	/**
	 * 根据mainId查询裁剪图纸bom明细
	 *
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> findPackingDetailByPackingID(Long packingID);

	/**
	 * 保存裁剪套材bom信息
	 *
	 * @param baoCaiKu
	 * @return
	 */
	public String saveBaoCaiKu(BaoCaiKu baoCaiKu, String userName) throws Exception;
}
