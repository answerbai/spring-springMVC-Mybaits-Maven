<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>登录页</title>
<link href="/js/base.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.9.1.js"></script>
<script language="JavaScript">
	var maxtime = 5 //分钟*秒，自己调整!
	var standard=maxtime;
	function CountDown() {
		if (maxtime >= 0) {
			seconds = Math.floor(maxtime % standard);
			--maxtime;
			msg = seconds;
			if(maxtime>0&&seconds==0){
				msg =standard;
			}else if(maxtime<0){
				maxtime=standard;
				//调用验证码生成服务
			}
			document.all["timer"].innerHTML = msg;
// 			if (maxtime == 5 * 30)
// 				alert('注意，还有5分钟!');
		} else {
			clearInterval(timer);
		}
	}
	timer = setInterval("CountDown()", 1000);
</script>
</head>
<!-- 用户登录 -->
<body>
	<form id="fm"
		action="${pageContext.request.contextPath}/j_spring_security_check"
		method="post">
		<div class="sign_bg">
			<div class="c-wrapper">
				<div class="sign">
					<div class="center">
						<div class="title">员&nbsp;&nbsp;工&nbsp;&nbsp;登&nbsp;&nbsp;录</div>
						<div class="text">
							<div class="input">
								<input class="in1" id="j_username" name="j_username" type="text" placeholder="邮箱" value="" /> 
								<input class="in2" id="j_password" name="j_password" type="password" placeholder="*************"/> 
								<input class="in3" id="j_verifCode" name="j_verifCode" type="text" placeholder="验证码" /> 
								<font color="red">剩</font><div class="im1" id="timer"></div><font color="red">秒</font>
<!-- 								<img class="im1" id="j_reflash" name="j_reflash" src="/js/error.png" title="刷新" /> -->
								gfhfhfghfgh
								<img class="im2" id="j_reflash" name="j_reflash" src="/js/reflash.png" title="刷新" />
								</br> 
								<font style="font-weight: bold" color="red" size="4px">验证码错误</font>
							</div>
<!-- 							<div class="clear" /> -->
							<div id="timer2" style="color:red"></div> 
							<div class="r">
								<div class="input2">
									<input class="bt" id="bt" type="button" value="登 录" />
								</div>
								<img class="im3" src="/js/reflash.gif" alt="" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
