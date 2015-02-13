<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Notas parciales estudiantes - Universidad Icesi - Cali,
			Colombia</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<link href="<%=request.getContextPath()%>/css/estilos.css"
			rel="stylesheet" type="text/css">
		<script type="text/javascript" src="BubbleTooltips.js"></script>
		<script type="text/javascript">
	window.onload = function() {
		enableTooltips("contn")
	};
</script>
	</head>

	<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
		topmargin="0" marginwidth="0" marginheight="0">
		<jsp:include page="../template/header_pre.jsp"></jsp:include>
		<table width="767" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td class="info">
					<br />
					<div align="center">
						<div class="error">
							<%
								if (request.getAttribute("mensajeError") != null) {
							%>
							<%=request.getAttribute("mensajeError")%>
							<%
								} else {
							%>
							<strong>Error: </strong>Su sesión de trabajo actual ha
							finalizado.
							<%
								}
							%>
						</div>
						<br />
						<div class="info">
							Si lo desea, vuelva a iniciar sesión a través del Portal de
							Estudiantes.
							<br />
							<br />
							<a href="http://www.icesi.edu.co/servicios_estudiantes.php">Portal de estudiantes</a>
							<br />
						</div>
					</div>
				</td>
			</tr>
		</table>
		<jsp:include page="../template/footer.jsp"></jsp:include>
	</body>
	<!-- InstanceEnd -->
</html>