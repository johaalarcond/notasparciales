<html>
	<head>
		<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<link href="<%=request.getContextPath()%>/css/estilos.css"
			rel="stylesheet" type="text/css">
		<style>
.bordeError {
	width: 50%;
	min-width: 0px;
	max-width: 400px;
	background: #eee;
	border: 1px solid #c00;
	padding: 5px;
}

.error {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #990000;
}

.info {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: black;
}
</style>
	</head>

	<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
		topmargin="0" marginwidth="0" marginheight="0"
		onload="document.forms[0].opcion[0].checked=true;">
		<jsp:include page="../template/header.jsp"></jsp:include>
		<table border="0" align="center" cellpadding="00"
			cellspacing="0">
			<tr>
				<td>
					<div align="center">
						<div class="error">
							<%
								if (request.getAttribute("mensajeError") != null) {
							%>
							<%=request.getAttribute("mensajeError")%>
							<%
								} else {
							%>
							<strong>Error: </strong>Su sesión de trabajo actual ha finalizado
							o ha abierto el listado de cursos
							<br />
							y no ha escogido ninguno de sus cursos
							<center>
								<a href="/notasparciales">Listado de
									cursos</a>
							</center>
							<%
								}
							%>
						</div>
						<br />
						<div class="info">
							Si lo desea, vuelva a iniciar sesión a través del Portal de
							Profesores.
							<br />
							<br />
							<a href="http://www.icesi.edu.co/servicios_profesores.php">Portal de profesores</a>
						</div>
					</div>
				</td>
			</tr>
		</table>
		<jsp:include page="../template/footer.jsp"></jsp:include>
	</body>
</html>