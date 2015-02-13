<%@ page language="java" errorPage="/estudiantes/errorEstud.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<!-- InstanceBegin template="/Templates/principal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<title>Notas parciales estudiantes - Universidad Icesi - Cali,
Colombia</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
		<!--<p align="right"><a
			href="<%=request.getContextPath()%>/cerrarSesionEstud.jsp"
			class="links"> <bean:message key="cerrar.sesion" /> </a> <br />
		</p>-->
		<div align="center" id="contn"><html:link styleClass="links"
			action="/aCursos" title="Regresa al listado de cursos">
			<bean:message key="listado" />
		</html:link> | <!--<html:link styleClass="links" action="/aNotasEstud" title="Sección de notas"><bean:message key="notas.registradas"/></html:link> | -->
		<html:link styleClass="links" action="/aEsquemaEstud"
			title="Sección para visualizar el esquema del curso.">
			<bean:message key="esquema" />
		</html:link> | <html:link styleClass="links" action="/aDefinitivasEstud"
			title="Muestra las notas definitivas del curso con las calificaciones ingresadas hasta el momento">
			<bean:message key="definitivas" />
		</html:link></div>
		<br />
		<table align="center">
			<tr class="celda2">
				<td
					colspan="<%=(session.getAttribute("estructura") != null) ? "2" : "1"%>"
					align="center">
				<h2 class="curso"><bean:write name="curso" scope="session"
					property="descripcionMateria" /> <br>
				Grupo: <bean:write name="curso" scope="session" property="grupo" />
				<br>
				<br>
				<bean:message key="esquema" /></h2>
				</td>
			</tr>
			<tr>
				<td><!--------------------------------------------> <%
 	if (request.getAttribute("modificarEstructuraForm") != null) {
 %> <html:form action="/actualizarActividad">
					<div class="curso" align="center"><bean:message
						key="modificacion.evaluacion" /></div>
					<br />
					<table align="center" class="tabla">
						<tr>
							<td class="celda2" align="right"><bean:message key="item" />
							</td>
							<td class="etiqueta"><bean:write
								name="actualizarActividadForm" scope="request" property="name" />
							<html:hidden property="name" /></td>
						</tr>
						<tr>
							<td class="celda2" align="right"><bean:message
								key="categoria" /></td>
							<td class="etiqueta"><bean:write
								name="actualizarActividadForm" scope="request"
								property="category" /> <html:hidden property="category" /></td>
						</tr>
						<tr>
							<td class="celda2" align="right"><bean:message
								key="porcentaje.grupo" /></td>
							<td><span class="etiqueta"><bean:write
								property="percentage" scope="request"
								name="actualizarActividadForm" />%</span> <html:hidden
								property="percentage" /></td>
						</tr>
						<tr>
							<td class="etiqueta" align="right"><bean:message
								key="fecha.realizacion" /></td>
							<td><bean:write property="date" scope="request"
								name="actualizarActividadForm" /></td>
						</tr>
						<tr>
							<td class="etiqueta" align="right"><bean:message
								key="descripcion" /></td>
							<td><bean:write property="description"
								name="actualizarActividadForm" /></td>
						</tr>
						<tr>
							<td class="etiqueta" align="right"><bean:message key="temas" />
							</td>
							<td><bean:write property="topics"
								name="actualizarActividadForm" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><html:cancel
								value="Continuar" styleClass="etiqueta" /></td>
						</tr>
					</table>
				</html:form> <%
 	}
 %> <!--------------------------------------------></td>

			</tr>
		</table>
		</td>
	</tr>
</table>
<jsp:include page="../template/footer.jsp"></jsp:include>
</body>
<!-- InstanceEnd -->
</html>