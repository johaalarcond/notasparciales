<%-- <%@ page language="java" errorPage="/profesores/error.jsp"%> ---%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
			<!-- InstanceEndEditable -->
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<!-- InstanceBeginEditable name="head" -->
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="cache-control" content="no-cache">
			<meta http-equiv="expires" content="0">
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/datePicker.js"></script>
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/overlib_mini.js"></script>
			<script type="text/javascript" src="BubbleTooltips.js"></script>
			<script type="text/javascript">
				window.onload = function() {
					enableTooltips("contn");
				};
			</script>

			<script language="JavaScript" type="text/javascript">
				function cambioControlAsistencia(formulario) {
					if (confirm('¿Está seguro de modificar el control de asistencia?')) {
						formulario.submit();
					}
				}
			</script>

			<link href="<%=request.getContextPath()%>/css/estilos.css" rel="stylesheet" type="text/css">
			<script language="javascript" type="text/javascript">
				var c = <%=session.getMaxInactiveInterval()%>;
				function cs(){
					c = c - 1;
					if(c == 0){
						document.location = "/notasparciales/sesionInactiva.icesi";
					}
				}
				
				function cerrarSesion(){
					setInterval("cs()", 1000);
				}
			</script>
		</head>

		<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
			topmargin="0" marginwidth="0" marginheight="0"
			onload="cerrarSesion()">
			<jsp:include page="../template/header.jsp"></jsp:include>
			<table border="0" align="center" cellpadding="00"
				cellspacing="0">
				<tr>
					<td height="18" nowrap class="info" align="center">
						<div align="center" class="error">
							<br />
							Recuerde que esta sesión tiene un tiempo de inactividad máximo
							permitido de 8 minutos.
							<br />
							Pasado este tiempo su sesión será terminada automáticamente.
							<br />
							<br />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="4" align="center">
							<tr>
								<td align="center">
									<h2 class="curso">
										<bean:message key="esquema.anterior" />
									</h2>
								</td>
							</tr>
							<tr>
								<td class="info" height="116" colspan="2" valign="middle">
									En esta sección, podrá observar el esquema de un curso de esta
									materia del semestre inmediatamente anterior, para que
									determine si este se ajusta a sus necesidades del presente
									semestre. De ser así, por favor confírmelo dando clic en el
									botón "Copiar esquema".
								</td>
							</tr>
							<tr>
								<td height="21" colspan="2" align="center" valign="middle">
									<SPAN class="curso">Paso 2 de 2</SPAN>
									<br />
									<br />
									<%
										if (request.getAttribute("error") != null) {
									%>
									<FIELDSET class="fieldset">
										<div class="error" align="left">
											<%-- <html:errors property="esquemaAnterior"/> ---%>
										</div>
									</FIELDSET>
									<br />
									<%
										}
									%>
								</td>
							</tr>

							<tr>
								<td>
									<icesi:copias />
									<br>
								</td>
							</tr>
						</table>

						<table border="0" cellpadding="4" cellspacing="1" align="center">
							<tr>
								<td align="center">
									<html:form action="/copiarEsquemaAnterior">
										<html:submit value="Copiar esquema" />
										<html:cancel value="Cancelar" />
									</html:form>
								</td>
							</tr>
						</table>
						<br />
						<br />
					</td>
				</tr>
			</table>
			<jsp:include page="../template/footer.jsp"></jsp:include>
		</body>
	</html>
</logic:present>
<logic:notPresent scope="session" name="activa">
	<jsp:forward page="/notasparciales/sesionInactiva.icesi"></jsp:forward>
</logic:notPresent>