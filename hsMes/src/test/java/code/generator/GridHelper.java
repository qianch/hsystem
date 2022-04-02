/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package code.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.xdemo.superutil.j2se.ReflectUtils;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.stock.entity.MaterialInRecord;
import com.bluebirdme.mes.stock.entity.MaterialStockState;

/**
 * 
 * @author Goofy
 * @Date 2018年3月1日 上午10:16:22
 */
public class GridHelper {
	
	public static void main(String[] args) {
		List<Field> fields=ReflectUtils.getFields(MaterialStockState.class, true);
		for(Field field:fields){
			Desc desc=field.getAnnotation(Desc.class);
			//<th field="MATERIALCODE" width="150">物料代码</th>
			if(desc==null)desc=new Desc() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return Desc.class;
				}
				
				@Override
				public String value() {
					return "未知";
				}
			};
			System.out.println("<th field=\""+field.getName()+"\" width=\"150\">"+desc.value()+"</th>");
		}
	}

}
