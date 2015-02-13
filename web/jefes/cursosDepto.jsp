<%@ page language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="core"%>
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Seguimiento a cursos - Universidad Icesi - Cali,
				Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">

			<script>
	function enviar(form, opcion) {
		var formulario = document.getElementById(form);
		formulario.opcion.value = opcion;
		formulario.submit();
		return true;
	}
</script>
			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
			<script language="javascript" type="text/javascript">
	function mostrarDiv(enlace, prefijo) {
		prefijo = (typeof prefijo == 'undefined') ? '' : prefijo;
		var division = document.getElementById(prefijo + enlace.name);
		var estiloDivision = division.style;
		if (estiloDivision != null) {
			var despliegueDivision = estiloDivision.display;
			if (despliegueDivision == null || despliegueDivision == ''
					|| despliegueDivision == 'none') {
				division.style.display = 'block';
			} else {
				division.style.display = 'none';
			}
		}
		return false;
	}
	
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
                                        <div id="menu_seguimientos" align="center">
                                            <c:if test="${RJEFDEPRE || RDIRPROG || RSECGEN ||RSEGUIMIENTO}">
                                                <html:link styleClass="links" href="/notasparciales/segestud"
                                                           title="Seguimiento a estudiantes">
                                                    <bean:message key="segestud.enlace" />
                                                </html:link>
                                            </c:if>
                                            <c:if test="${RJEFDEPRE || RSECGEN}">
                                                | <html:link styleClass="links" href="/notasparciales/jefes"
                                                           title="Seguimiento a cursos">
                                                    <bean:message key="segcursos.enlace" />
                                                </html:link>
                                            </c:if>
                                            | <html:link styleClass="links" action="/inicio"
								title="Notas parciales">Notas parciales</html:link>
                                        </div>
                                    </td>
                                </tr>
				<tr>
					<td>
						<table align="center" width="640px">
							<tr>
								<td colspan="2">

									<!--<p align="right">
										<a href="<%=request.getContextPath()%>/cerrarSesionEstud.jsp"
											class="links"> <bean:message key="cerrar.sesion" /> </a>
									</p>
									<br />-->
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
								<td>
									<div id="form_busqueda_jefes">
										<div id="busqueda" align="center">
											<html:form action="/jefes/busquedaCursos" style="form_buscar">
												<p>
													Criterio de búsqueda
												</p>
												<html:text property="criterio" />
												<html:submit>Buscar</html:submit>
											</html:form>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td class="info">
									<logic:present name="deptos">
										<p align="center">
											<logic:present name="resultadoBusqueda">
											Resultados con el criterio <b><bean:write
														name="resultadoBusqueda" /> </b>
											</logic:present>
											<logic:notPresent name="resultadoBusqueda">
											Búsqueda global
										</logic:notPresent>
										</p>
										<html:form action="/jefes/cursosDepto" styleId="form_cursos">
											<html:hidden property="opcion" />
											<logic:iterate id="v" name="deptos">
												<bean:define id="depto" name="v" property="value" />
												<bean:size id="resultados" name="depto" property="cursos" />
												<div id="depto" align="center">
													<a href="#<%=depto%>" name="<%=depto%>"
														onclick="mostrarDiv(this, 'cursos_');"> <bean:write
															name="depto" property="codigo" /> - <bean:write
															name="depto" property="nombre" /> (<%=resultados%>) </a>
													<div id="cursos_<%=depto%>" style="display: none;">
														<table>
															<logic:iterate id="curso" name="depto" property="cursos"
																type="co.edu.icesi.notas.Curso" indexId="index">
																<tr>
																	<td class="info">
																		<input type="radio" name="curso"
																			value="<%=curso.getId()%>"
																			<%if (index.intValue() == 0)
									out.println("checked=\"checked\"");%> />
																		<bean:write name="curso" property="nombre" />
																		. Grupo:
																		<bean:write name="curso" property="grupo" />
																	</td>
																</tr>
															</logic:iterate>
														</table>
													</div>
												</div>
											</logic:iterate>

											<br />
											<div align="center">
												<html:button onclick="enviar('form_cursos', 1)"
													value="Esquema del curso" property="boton"
													styleClass="info" />
												<%-- <html:button  onclick="enviar(cursosEstudianteForm, 2)" value="Notas registradas" property="boton" styleClass="info"/> --%>
												<html:button onclick="enviar('form_cursos', 3)"
													value="Definitivas" property="boton" styleClass="info" />
											</div>
										</html:form>
									</logic:present>
								</td>
							</tr>
						</table>
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