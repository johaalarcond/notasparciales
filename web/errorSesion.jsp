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
		<script type="text/javascript" language="javascript">
	function cerrarSesion() {
		document.getElementById('cerrarSesion').src = 'https://iden.icesi.edu.co/pls/orasso/orasso.wwsso_app_admin.ls_logout?p_done_url=http://www.icesi.edu.co/notasparciales';
		document.getElementById('cerrarSesion').style.display = 'block';
		document.getElementById('cerrarSesion').style.display = 'none';
	}
</script>
	</head>
	<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
		topmargin="0" marginwidth="0" marginheight="0" onload="cerrarSesion()">
		<jsp:include page="template/header.jsp"></jsp:include>
		<table border="0" align="center" cellpadding="00"
			cellspacing="0">
			<tr>
				<td>
					<div align="center">
						<div class="error">
							<strong>Error: </strong>Su sesión de trabajo actual ha finalizado
							<br />
							Por favor haga click en el siguiente enlace para reiniciar su
							sesión de trabajo:
							<br />
							<center>
								<a href="/notasparciales">Inicio</a>
							</center>
						</div>
						<noscript>
							<iframe id="cerrarSesion"
								src="https://iden.icesi.edu.co/pls/orasso/orasso.wwsso_app_admin.ls_logout?p_done_url=http://www.icesi.edu.co/notasparciales"
								style="width: 100%; height: 200px;"></iframe>
						</noscript>
						<script type="text/javascript">
	document.write('<iframe id="cerrarSesion" style="display:none;"></iframe>');
</script>
					</div>
				</td>
			</tr>
		</table>
		<jsp:include page="template/footer.jsp"></jsp:include>
	</body>
</html>