<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>  
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>  
<%@ page import="java.net.URLDecoder,java.net.URLEncoder"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>test XSS</title>  
<script type="text/javascript" src="/js/test.js"></script>  
</head>  
<%  
    String param = request.getParameter("v");  
    System.out.println("original " + param);  
%>  
<script>  
    var scriptVar=<%=param%>;  
    writelnToDom("original: " + scriptVar);   
</script>  
<body>  
</body>  
</html> 