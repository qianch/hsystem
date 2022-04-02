package code.packaging;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 类注释
 * @author Goofy
 * @Date 2017年1月22日 下午1:38:05
 */
public class RunPack {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		int i=0;
		while(true){
			try {
				System.out.println(++i);
				Jsoup.connect("http://localhost/hsmes/mobile/common/roll/info/RBZ120170117000").timeout(1000000).get();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		
		
	}
}
