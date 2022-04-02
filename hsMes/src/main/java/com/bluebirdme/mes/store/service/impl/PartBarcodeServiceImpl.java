/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import com.bluebirdme.mes.printer.service.impl.MergePrinterServiceImpl;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.RollBarcode;
import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.service.IPartBarcodeService;
import com.bluebirdme.mes.store.dao.IPartBarcodeDao;

/**
 *
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */
@Service
@AnyExceptionRollback
public class PartBarcodeServiceImpl extends BaseServiceImpl implements IPartBarcodeService {

	@Resource IPartBarcodeDao partBarcodeDao;

	@Resource
	IBtwFileService btwFileService;

	@Override
	protected IBaseDao getBaseDao() {
		return partBarcodeDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return partBarcodeDao.findPageInfo(filter,page);
	}

	public String clearPart(String ids) throws Exception {

		String ids_temp[] = ids.split(",");
		Serializable ids_target[] = new Serializable[ids_temp.length];
		for (int i = 0; i < ids_temp.length; i++) {
			PartBarcode partBarcode = findById(PartBarcode.class, Long.parseLong(ids_temp[i]));
			btwFileService.clearBacode(partBarcode);
		}
		return "";
	}

	public  String editBacode(long id, Integer customerBarCodeRecord, Integer agentBarCodeRecord,long btwfileId)
	{
		BoxBarcode entity=findById(BoxBarcode.class,id);
		BtwFile btwFile = findById(BtwFile.class, btwfileId);

		if(customerBarCodeRecord>0) {
			if (customerBarCodeRecord > btwFile.getConsumerBarCodeRecord()) {
				return "客户条码不能大于" + btwFile.getTagName() + btwFile.getConsumerBarCodeRecord();
			}
			String customerBarCode = MergePrinterServiceImpl.GetMaxBarCode(btwFile.getConsumerBarCodePrefix() == null ? "" : btwFile.getConsumerBarCodePrefix(), customerBarCodeRecord, btwFile.getConsumerBarCodeDigit() == null ? 0 : btwFile.getConsumerBarCodeDigit());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("btwfileId", btwfileId);
			map.put("customerBarCode", customerBarCode);
			List<RollBarcode> list = this.findListByMap(RollBarcode.class, map);
			if (list.size() == 0) {
				entity.setCustomerBarCode(customerBarCode);
			}
		}

		if(agentBarCodeRecord>0) {
			if(agentBarCodeRecord>btwFile.getAgentBarCodeRecord())
			{
				return "供销商条码不能大于"+btwFile.getTagName()+btwFile.getAgentBarCodeRecord();
			}
			String  agentBarCode=MergePrinterServiceImpl.GetMaxBarCode(btwFile.getAgentBarCodePrefix() == null ? "" : btwFile.getAgentBarCodePrefix(), agentBarCodeRecord, btwFile.getAgentBarCodeDigit() == null ? 0 : btwFile.getAgentBarCodeDigit());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("btwfileId", btwfileId);
			map.put("agentBarCode", agentBarCode);
			List<RollBarcode> list = this.findListByMap(RollBarcode.class, map);
			if (list.size() == 0) {
				entity.setAgentBarCode(agentBarCode);
			}
		}

		update(entity);
		return "更新条码成功";
	}

	public List<Map<String, Object>> findMaxPartBarCodeCount()
	{
		return partBarcodeDao.findMaxPartBarCodeCount();
	}

}
