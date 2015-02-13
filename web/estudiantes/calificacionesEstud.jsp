<%@ page language="java" errorPage="/estudiantes/errorEstud.jsp"%>

<%
	String sitio = "http://www.icesi.edu.co/servicios_estudiantes.php";
%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="co.edu.icesi.notas.Categoria"%>
<%@ page import="co.edu.icesi.notas.Curso"%>
<logic:present scope="session" name="activa">
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html>
		<!-- InstanceBegin template="/Templates/principal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
		<head>
			<!-- InstanceBeginEditable name="doctitle" -->
			<title>Universidad Icesi - Portal de estudiantes de pregrado</title>
			<!-- InstanceEndEditable -->
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<link href="<%=request.getContextPath()%>/css/estilos.css" rel="stylesheet" type="text/css">
			<script type="text/javascript" src="BubbleTooltips.js"></script>
			<script type="text/javascript">
				window.onload = function() {
					enableTooltips("contn");
				};
			</script>
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
			topmargin="0" marginwidth="0" marginheight="0" onload="cerrarSesion()">
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
									<div class="curso" align="center">
										<bean:write name="curso" scope="session"
											property="descripcionMateria" />
									</div>
									<div class="curso" align="center">
										<bean:write name="calificacionForm" property="nombre" />
										(
										<bean:write name="padreActual" property="porcentaje" />
										%)
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<br />
									<table border="1" cellspacing="0" align="center">
										<tr>
											<td class="celda2" align="center">
												<bean:message key="codigo" />
											</td>
											<td class="celda2" align="center">
												<bean:message key="nombre" />
											</td>
											<logic:iterate name="padreActual" id="estructura"
												property="estructura" indexId="index">
												<td class="celda2" align="center">
													<bean:write name="estructura" property="nombre" />
													<br>
													<bean:write name="estructura" property="porcentaje" />
													%
												</td>
											</logic:iterate>
											<td class="celda2" align="center">
												<bean:message key="definitiva" />
											</td>
										</tr>

										<logic:iterate name="calificacionForm" id="calificacion"
											property="matriculas" indexId="index"
											type="co.edu.icesi.notas.form.Calificacion">
											<tr>
												<td class="celda">
													<bean:write name="calificacion" property="codigo" />
												</td>
												<td class="celda">
													<bean:write name="calificacion" property="nombre" />
												</td>
												<logic:iterate name="calificacion" id="nota"
													property="notas" indexId="index2"
													type="co.edu.icesi.notas.form.Nota">
													<td class="celda" align="left">
														<bean:write name="calificacionForm"
															property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].nota"%>' />
													</td>
												</logic:iterate>
												<td class="resaltado" align="center">
													<%=((Categoria) session.getAttribute("padreActual")).calcularNotaSinPorcentaje(((Curso) session.getAttribute("curso")).getAlumno(calificacion.getCodigo()))%>
												</td>
											</tr>
										</logic:iterate>
									</table>
								</td>
							</tr>
						</table>
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