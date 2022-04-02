/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package code.generator;


import com.bluebirdme.mes.core.dev.DevHelper;
import com.bluebirdme.mes.mobile.produce.entity.FeedingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代码生成器
 * @author Goofy
 * @Date 2016年4月8日 下午12:24:50
 */
public class Gen {
	private static Logger log = LoggerFactory.getLogger(Gen.class);
	/**
	 * 生成Controller,Service.Dao
	 * @param entity 实体类.class
	 * @param author 作者
	 */
	public static <T> void csd(Class<T> entity,String author){
		try {
			DevHelper.genCSD(entity, author);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
	}
	
	/**
	 * 生成JS,JSP
	 * @param entity 实体类.class
	 * @param author 作者
	 */
	public static <T> void jsJsp(Class<T> entity,String author){
		try {
			DevHelper.genJsJsp(entity, author);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
	}
	
	/**
	 * 生成所有的Controller，Service，Dao，JS，JSP
	 * @param entity
	 * @param author
	 */
	public static <T> void genAll(Class<T> entity,String author){
		try {
			DevHelper.genCSD(entity, author);
			DevHelper.genJsJsp(entity, author);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
	}
	
	public static void main(String[] args) {
		//genAll(**.class, "");
//		jsJsp(BtwFile.class, "徐波");
		//csd(**.class, "");
//		genAll(Gpm.class, "高飞");
//		genAll(TracingLog.class, "徐波");
//		csd(BoxBarcode.class, "徐波");
//		csd(PartBarcode.class, "徐波");
//		csd(RollBarcode.class, "徐波");
//		csd(TrayBarCode.class, "徐波");
//		jsJsp(RollBarcode.class, "徐波");
//		jsJsp(PartBarcode.class, "徐波");
//		jsJsp(BoxBarcode.class, "徐波");
//		jsJsp(TrayBarCode.class, "徐波");
//		genAll(StockFabricMove.class,"徐波");
//		genAll(MaterialForceOutRecord.class,"徐波");
//		genAll(ProductForceOutRecord.class,"徐波");
		jsJsp(FeedingRecord.class, "徐波");
	}
}
