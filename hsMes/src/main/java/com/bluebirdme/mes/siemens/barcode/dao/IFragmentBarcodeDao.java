/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-8-3 20:38:40
 */

public interface IFragmentBarcodeDao extends IBaseDao {
    String fragmentCheckPacked(String[] fragmentBarcodes);

    void updateFragmentBarcodeInfo(String codes, String user, String date, String device) throws SQLTemplateException;

    List<String> getFeedingFarbic(Long cutId);

    List<Map<String, Object>> getSuitInfo(String code);
}
