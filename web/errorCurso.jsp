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
		topmargin="0" marginwidth="0" marginheight="0">
		<jsp:include page="template/header.jsp"></jsp:include>
		<table border="0" align="center" cellpadding="00"
			cellspacing="0">
			<tr>
				<td>
					<div align="center">
						<div class="error">
							<strong>Error: </strong>Ha vuelto al listado de cursos pero no ha
							escogido ninguno de sus cursos.
							<br />
							Por favor haga click en el siguiente enlace para ir al listado de
							cursos:
							<br />
							<center>
								<a href="/notasparciales">Listado de
									cursos</a>
							</center>
						</div>
					</div>
				</td>
			</tr>
		</table>
		<jsp:include page="template/footer.jsp"></jsp:include>
	</body>
</html>