<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- meta http-equiv="Refresh" content="1;url=view?command=authorize" -->
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<title>请等候</title>
</head>
<body>
<div id="login_container"/>
<script type="text/javascript">
	var obj=new WxLogin({
		id:'login_container',
		appid:'wxe81ab6efa72ce31a',
		scope:'snsapi_login',
		redirect_uri:'http://pm.ximucredit.com:8080/dragon/view',
		state:'login',
		style:'black'
	});
</script>

</body>
</html>