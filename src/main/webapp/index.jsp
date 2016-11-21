<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.*"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>导航页</title>
<script type="text/javascript" src="/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>
</head>
<script>
	$(function() {
		$(".request1").click(function() {
			var value = $(".common").attr("value");
			$("#value1").val(value);
			$("#fm1").submit();
		});

		$("#request2").click(function() {
			var type = $(".value2").attr("type");//获取当前元素的第一个属性值
			var value = $(".value2").val();//获取当前元素的当前值
			alert(value);
			alert(type);
			$("#fm2").ajaxSubmit({
				url : "/ajaxRequest",
				type : "post",
				data : {
					"common2" : $(".common2").attr("value"),
					"value2" : $(".value2").val()
				},
				dataType : "text",
				success : function(json) {
					//成功后刷新

					var obj = new Function("return" + json)();//转换后的JSON对象
					$("#value2").val(obj.name);
					//window.location.reload(true);
				}
			});
		});
	});
</script>
<body>
	<%
// 	   HttpClient.requestGet("http://dyctdy.oicp.net/http"); 
		String headerName = "";
		String result1 = "";
		String result2 = "";
		String result3 = "";
		String result4 = "";
		String result5 = "";
		String result6 = "";

		Enumeration enu = request.getHeaderNames();//取得全部头信息
		while (enu.hasMoreElements()) {//以此取出头信息
			headerName = (String) enu.nextElement();
			System.out.println(headerName+":"+request.getHeader(headerName));
%>
<%-- headers:<%=headerName+":"+request.getHeader(headerName) %> --%>
<%
		}
		System.out.println("====================================");
		String ip = request.getHeader("X-Forwarded-For");
		result1 = "x-forwarded-for:" + ip;
		System.out.println(result1);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			result2 = "HTTP_X_FORWARDED_FOR:" + ip;
			System.out.println(result2);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			result3 = "Proxy-Client-IP:" + ip;
			System.out.println(result3);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			result4 = "WL-Proxy-Client-IP:" + ip;
			System.out.println(result4);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			result5 = "Remote:" + ip;
			System.out.println(result5);
		}
		result6 = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
		System.out.println(result6);
		System.out.println("RemoteAddr--》"+request.getRemoteAddr());
	%>
<!-- 	result1 = -->
<%-- 	<%=result1%> --%>
<!-- 	<br> result2 = -->
<%-- 	<%=result2%> --%>
<!-- 	<br> result3 = -->
<%-- 	<%=result3%> --%>
<!-- 	<br> result4 = -->
<%-- 	<%=result4%> --%>
<!-- 	<br> result5 = -->
<%-- 	<%=result5%> --%>
<!-- 	<br> result6 = -->
<%-- 	<%=result6%> --%>
	<br>
	<br>
	<form action="${pageContext.request.contextPath}/insetInfo"
		method="post">
		姓名：<input type="text" id="name" name="name"><br> 年龄：<input
			type="text" id="age" name="age"><br> <input
			type="submit" value="添加">
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/queryOne"
		method="post">
		查询的编号:<input type="text" id="id" name="id"><br> <input
			type="submit" value="查询">
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/queryList"
		method="post">
		查询全部信息：<input type="submit" value="查询"> <input type="hidden"
			name="value" id="value" value="<%=request.getParameter("v")%>" />
	</form>
	<a
		href="${pageContext.request.contextPath}/queryList?value=123&key=888">测试反射性xss防止注入脚本</a>
	<br>
	<form action="${pageContext.request.contextPath}/requestCookie"
		method="post">
		保存cookie数据：<input type="submit" value="保存cookie">
	</form>
	<br>
	<form id="fm1"
		action="${pageContext.request.contextPath}/commonRequest"
		method="post">
		普通的js请求：<input type="text" class="common" name="common"
			value="普通的js请求" /> <input type="text" id="value1" name="value1"
			value="" /> <input type="button" class="request1" value="请求" />
	</form>
	<br>
	<form id="fm2" method="post">
		Ajax的请求： <input type="text" id="common2" class="common2"
			name="common2" value="Ajax的请求" /> <input type="text" id="value2"
			class="value2" name="value2" value="" /> <input type="button"
			id="request2" value="请求" />
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/fastJson"method="post">
		查询(结果返回json)<input type="submit" value="查询">
	</form>
	<br>
</body>
</html>