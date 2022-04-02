/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package word;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.freemarker.FreemarkerUtils;

/**
 * 
 * @author Goofy
 * @Date 2018年10月23日 下午5:24:39
 */
public class Tool {
	
	public static void main(String[] args) throws IOException {
		String[] rows=FileUtils.readContent("D:\\1.txt", Charset.forName("UTF-8"));
		
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		
		Map<String,String> map=new HashMap<String, String>();
		String[] d=null;
		for(String row:rows){
			map=new HashMap<String, String>();
			d=row.split("	");
			map.put("xuhao",parse(d[0]));
			map.put("bumen",parse(d[2]));
			map.put("xuqiu",parse(d[3]));
			map.put("mudi",parse(d[4]));
			map.put("miaoshu",parse(d[5]));
			map.put("shijian",parse(d[1]));
			map.put("diaoyanqianzi",parse(null));
			map.put("duijieqianzi",parse(null));
			map.put("jieguo",parse(null));
			map.put("gongshi",parse(null));
			list.add(map);
		}
		
		Map<String,Object> data=new HashMap<String, Object>();
		
		data.put("list", list);
		
		FreemarkerUtils.generateToFile("D:\\", "confirm.ftl", "D:\\confirm.html", data , "UTF-8");
	}
	
	static String parse(String s){
		
		if(StringUtils.isBlank(s)){
			return "";
		}
		
		return s;
	}

}
