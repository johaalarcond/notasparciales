<%@page import="java.net.*, java.io.*"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>

	<body>
		<%
			session.invalidate();
			Cookie[] cookies = request.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setValue("No es una cookie válida");
				cookies[i].setMaxAge(0);
			}
			// Clear application session, if any
			String l_return_url = "http://www.icesi.edu.co/servicios_profesores.php";
			response.setHeader("Osso-Return-Url", l_return_url);
			response.sendError(470, "Oracle SSO");
		%>
	</body>
</html>
