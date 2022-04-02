package code.generator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.service.IBomService;

/**
 * 
 * @author Goofy
 * @Date 2016年10月21日 下午3:17:24
 */
public class ServiceTest /*extends BaseTest*/ {

	@Autowired IBomService bomService;
	
	
	//@Test
	public void test(){
		System.out.println(111);
		System.out.println(GsonTools.toJson(bomService.getBomDetails(FtcBomDetail.class, "GDDAS", "22")));
	}
	
	public static void main(String[] args) throws IOException {
		String x[]=FileUtils.readContent("C:\\1.txt", Charset.forName("UTF-8"));
		for(int i=0;i<x.length;i++){
			if(i%3==0){
				System.out.println(x[i]);
			}
		}
		
	}
}
