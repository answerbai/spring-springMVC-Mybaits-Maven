
//window.onload = CountDown;
window.onload =reload_sign_img;

//页面倒计时
var maxtime = 5 // 秒，自己调整!
var standard = maxtime;
//验证码
var img;
function CountDown() {
	if (maxtime >= 0) {
		seconds = Math.floor(maxtime % standard);
		--maxtime;
		msg = seconds;
		if (maxtime > 0 && seconds == 0) {
			msg = standard;
		} else if (maxtime < 0) {
			maxtime = standard;
			// 调用验证码生成服务
			reload_sign_img();
		}
		document.all["timer"].innerHTML = msg;
		// if (maxtime == 5 * 30)
		// alert('注意，还有5分钟!');
	} else {
		clearInterval(timer);
	}
}
timer = setInterval("CountDown()", 1000);

//获取验证码
function reload_sign_img() {
	$.ajax({
		type : "post",
		url : "/ajaxRequest",
		dataType : "text",
		success : function(json) {
			//成功后刷新
			img = new Function("return" + json)();//转换后的JSON对象
			alert(json);
			$("#verifCode_img")[0].src ="../img/verif.jpg";
			$("#value2").val(obj.name);
			//window.location.reload(true);
		}
	});
//	$("#verifCode_img")[0].src =img;	
}







