<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<html>
	<head>
		<link href="<%=request.getContextPath()%>/css/estilos.css"
			rel="stylesheet" type="text/css">
	</head>
	<body>
		<jsp:include page="../template/header.jsp"></jsp:include>
		<icesi:definitivasBasico />
		<jsp:include page="../template/footer.jsp"></jsp:include>
	</body>
</html>