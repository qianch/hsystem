(function() {
	
	
	var js_path="../resources/js/";
	var css_path="../resources/css/";
	
	document.write(
		"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />" +
		"<meta charset=\"UTF-8\">" +
		"<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css_path + "/mui/mui.css\">" +
		"<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css_path + "app.css\" />" +

		"<script type=\"text/javascript\" src=\"" + js_path + "mui/mui.min.js\"></script>" +

		"<script type=\"text/javascript\" src=\"" + js_path + "config.js\"></script>" +

		"<script type=\"text/javascript\" src=\"" + js_path + "app/jquery.min.js\"></script>" +

		"<script type=\"text/javascript\" src=\"" + js_path + "app/fastclick.js\"></script>" +
		"<script type=\"text/javascript\" src=\"" + js_path + "app/template.js\"></script>" +

		"<script type=\"text/javascript\" src=\"" + js_path + "app/loading.js\"></script>" +
		"<script type=\"text/javascript\" src=\"" + js_path + "app/storage.js\"></script>" +
		"<script type=\"text/javascript\" src=\"" + js_path + "app/simple.js\"></script>" +
		"<script type=\"text/javascript\" src=\"" + js_path + "app/tip.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/jquery.base64.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/Calendar.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/valids.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/jquery.ui.touch-punch.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/scanner.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/AppConstant.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/app.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/barcode.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/AppHelper.js\"></script>"+
		"<script type=\"text/javascript\" src=\"" + js_path + "app/control.js\"></script>");
	//执行FastClick实例化，解决屏幕点击延迟
	if('addEventListener' in document) {
		document.addEventListener('DOMContentLoaded', function() {
			FastClick.attach(document.body);
		}, false);
	}
})();


	
	
	
	
	
	
	
	
	
	
		
	
		