function notify(title, content) {
	
	Tip.success(content);
	
	if (!("Notification" in window)) {
	} else if (Notification.permission === "granted") {
		show(title, content);
	} else if (Notification.permission !== 'denied') {
		Notification.requestPermission(function(permission) {
			if (permission === "granted") {
				show(title, content);
			}
		});
	}
}

function show(title, content) {
	var title = title;
	var options = {
		body : content,
		icon : "resources/notification/notice.png"
	};
	if (document.getElementById("_notice_bg_sound") == null) {
		$("body").append('<audio id="_notice_bg_sound" src="resources/notification/notice.mp3" ></audio>');
	}
	document.getElementById("_notice_bg_sound").play();
	var n=new Notification(title, options);
	n.onclick=function(){
		window.focus();
		n.close();
	}
}