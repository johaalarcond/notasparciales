<!-- <%@ page language="java" errorPage="/estudiantes/errorEstud.jsp"%> -->
<%@ page import="java.util.Date"%>
<%
	String fechaLimite = ((Date) session.getAttribute("fechaLimite"))
			.toString();
%>
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
		<title>Universidad Icesi - Portal de estudiantes de pregrado</title>
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
					<!--<p align="right">
						<a href="<%=request.getContextPath()%>/cerrarSesionEstud.jsp"
							class="links"> <bean:message key="cerrar.sesion" /> </a>
						<br />
					</p>-->
					<div align="center" id="contn">
						<html:link styleClass="links" action="/aCursos"
							title="Regresa al listado de cursos">
							<bean:message key="listado" />
						</html:link>
						|
						<html:link styleClass="links" action="/aNotasEstud"
							title="Sección de notas">
							<bean:message key="notas.registradas" />
						</html:link>
						|
						<html:link styleClass="links" action="/aEsquemaEstud"
							title="Sección para visualizar el esquema del curso.">
							<bean:message key="esquema" />
						</html:link>
						|
						<html:link styleClass="links" action="/aDefinitivasEstud"
							title="Muestra las notas definitivas del curso con las calificaciones ingresadas hasta el momento">
							<bean:message key="definitivas" />
						</html:link>
					</div>
					<br />
					<table align="center">
						<tr class="celda2">
							<td align="center">
								<div class="error">
									<bean:message key="advertencia.definitivas.estudiantes"
										arg0='<%=fechaLimite%>' />
								</div>
								<h2 class="curso">
									<bean:write name="curso" scope="session"
										property="descripcionMateria" />
									<br>
									Grupo:
									<bean:write name="curso" scope="session" property="grupo" />
									<br>
                                                                        
									<br>
									<bean:message key="definitivas" />
								</h2>
							</td>
							</tr>
							<tr>
								<td>
									<icesi:definitivas estudiante="true" />
									<br />
								</td>

							</tr>
                                                        
                                                        
					</table>
				</td>
			</tr>
		</table>
		<jsp:include page="../template/footer.jsp"></jsp:include>
	</body>
</html>