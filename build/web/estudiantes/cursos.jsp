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
<logic:present scope="session" name="activa">
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html>
		<!-- InstanceBegin template="/Templates/principal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
		<head>
			<title>Universidad Icesi - Portal de estudiantes de pregrado</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
			<script language="JavaScript">
				function enviar(form, opcion) {
					form.opcion.value = opcion;
					form.submit();
					return true;
				}
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
			topmargin="0" marginwidth="0" marginheight="0"
			onload="cerrarSesion()">
			<jsp:include page="../template/header_pre.jsp"></jsp:include>
			<table width="767" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td class="info">
						<br />
						<table align="center">
							<tr>
								<td colspan="2">

									<!--<p align="right">
										<a href="<%=request.getContextPath()%>/cerrarSesionEstud.jsp"
											class="links"> <bean:message key="cerrar.sesion" /> </a>
										<br />
									</p>-->

									<span class="celda2"><bean:message
											key="cursos.estudiante" /> </span>
									<br />
									<%
										if (request.getAttribute("errorCurso") != null) {
									%>
									<br />
									<div align="center">
										<fieldset class="fieldset">
											<div class="error" align="left">
												<%=request.getAttribute("errorCurso")%>
											</div>
										</fieldset>
									</div>
									<br />
									<%
										}
									%>
								</td>
							</tr>
							<tr>
								<logic:notEmpty name="estudiante" property="cursos">
									<td>
										&nbsp;
									</td>
									<td class="info">
										<html:form action="/cursosEstud">
											<html:hidden property="opcion" />
											<table>
												<logic:iterate id="actual" name="estudiante"
													property="cursos" type="co.edu.icesi.notas.Curso"
													indexId="index">
													<tr>
														<td class="info">
															<input type="radio" name="curso"
																value="<%=actual.getId()%>"
																<%if (index.intValue() == 0)
								out.println("checked=\"checked\"");%>>
															<bean:write name="actual" property="nombre" />
															. Grupo:
															<bean:write name="actual" property="grupo" />
														</td>
													</tr>
												</logic:iterate>
											</table>
											<br />
											<div align="center">
												<html:button onclick="enviar(cursosEstudianteForm, 1)"
													value="Esquema del curso" property="boton"
													styleClass="info" />
												<%-- <html:button  onclick="enviar(cursosEstudianteForm, 2)" value="Notas registradas" property="boton" styleClass="info"/> --%>
												<html:button onclick="enviar(cursosEstudianteForm, 3)"
													value="Definitivas" property="boton" styleClass="info" />
												<br />
											</div>
										</html:form>
									</td>
								</logic:notEmpty>
								<%-- En caso de que no existan cursos asignados--%>
								<logic:empty name="estudiante" property="cursos">
									<td class="info" colspan="2">
										<bean:message key="estudiante.no.curso" />
									</td>
								</logic:empty>
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