<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<body>
	<form action="${pageContext.request.contextPath}/insetInfo" method="post">
		姓名：<input type="text" id="name" name="name"><br> 
		年龄：<input type="text" id="age" name="age"><br> 
		<input type="submit" value="添加">
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/queryOne" method="post">
		查询的编号:<input type="text" id="id" name="id"><br> 
		<input type="submit" value="查询">
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/queryList" method="post">
		查询全部信息：<input type="submit" value="查询">
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/requestCookie" method="post">
		保存cookie数据：<input type="submit" value="保存cookie">
	</form>
</body>
</html>
