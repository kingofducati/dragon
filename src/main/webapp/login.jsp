<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- head> <meta http-equiv="Refresh" content="1;url=view?command=authorize"> </head--->
<title>登陆</title>
<link rel="stylesheet" type="text/css" href="css/demo.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
</head>
<body>
	<div class="container">
		<section>
		<div id="container_demo">
			<div id="wrapper">
				<div id="login" class="animate form">
					<form action="view?command=login" autocomplete="on">
						<h1>登陆</h1>
						<input type="hidden" name="command" value="login"/>
						<p>
							<label for="username" class="uname" data-icon="u"> 你的公司邮箱 </label> <input id="username" name="username"
								required="required" type="text"
								placeholder="mymail@mail.com" />
						</p>
						<p>
							<label for="password" class="youpasswd" data-icon="p">
								你的邮箱密码 </label> <input id="password" name="password"
								required="required" type="password" placeholder="eg. X8df!90EO" />
						</p>
						<p class="login button">
							<input type="submit" value="登陆" />
						</p>
					</form>
				</div>
			</div>
		</div>
		</section>
	</div>
</body>
</html>