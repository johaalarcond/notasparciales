<%-- <%@ page language="java" errorPage="/profesores/error.jsp"%> ---%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>

<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Seguimiento a cursos - Universidad Icesi - Cali,
				Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="cache-control" content="no-cache">
			<meta http-equiv="expires" content="0">
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/datePicker.js"></script>
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/overlib_mini.js"></script>
			<script type="text/javascript" src="../BubbleTooltips.js"></script>
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

			<link href="<%=request.getContextPath()%>/css/estilos.css" rel="stylesheet" type="text/css">
		</head>

		<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
			topmargin="0" marginwidth="0" marginheight="0" onload="cerrarSesion()">
			<jsp:include page="../template/header.jsp"></jsp:include>
			<table border="0" align="center" cellpadding="00"
				cellspacing="0">
				<tr>
					<td height="18" nowrap class="info" align="center">
						<div align="center" class="error">
							<br/>
							Recuerde que esta sesión tiene un tiempo de inactividad máximo permitido de 8 minutos.
							<br/>
							Pasado este tiempo su sesión será terminada automáticamente.
							<br/>
							<br/>
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
						<div align="center"></div>
						<div align="center"></div>
						<div align="center"></div>
						<div align="center"></div>
						<p class="info" align="right">
							<!--<a href="<%=request.getContextPath()%>/cerrarSesion.jsp"
								class="links"> <bean:message key="cerrar.sesion" /> </a>
							<br />
							<br />-->
							<bean:message key="periodo.academico" />
							<strong><bean:write name="curso"
									property="periodoAcademico" /> </strong>
						</p>
						<!-- InstanceBeginEditable name="contenido" -->

						<div align="center" id="contn">
							<html:link styleClass="links" action="/jefes/aCursosJefe"
								title="Regresa al listado de cursos">
								<bean:message key="listado" />
							</html:link>
							|
							<!--<html:link styleClass="links" action="/aNotasEstud" title="Sección de notas"><bean:message key="notas.registradas"/></html:link> | -->
							<html:link styleClass="links" action="/jefes/aEsquemaJefe"
								title="Sección para visualizar el esquema del curso.">
								<bean:message key="esquema" />
							</html:link>
							|
							<html:link styleClass="links" action="/jefes/aDefinitivasJefe"
								title="Muestra las notas definitivas del curso con las calificaciones ingresadas hasta el momento">
								<bean:message key="definitivas" />
							</html:link>
						</div>
						<br />
						<table align="center">
							<tr class="celda2">
								<td
									colspan="<%=(request.getAttribute("actMostrar") != null) ? "2"
								: "1"%>"
									align="center">
									<h2 class="curso">
										<bean:write name="curso" scope="session"
											property="descripcionMateria" />
										<br>
										Grupo:
										<bean:write name="curso" scope="session" property="grupo" />
										<br>
										<br>
										<bean:message key="esquema" />
									</h2>
								</td>
							</tr>
							<tr>
								<td>
									<icesi:resumen jefe="true" />
								</td>
								<%
									if (request.getAttribute("actMostrar") != null) {
								%>
								<td>
									<div class="curso" align="center">
										<bean:message key="titulo.esquema.estudiante" />
										<bean:write name="actMostrar" scope="request" property="name" />
									</div>
									<table align="center" class="tabla">
										<%-- 
	    						<tr>
	    							<td class="etiqueta" align="right">
	    								<strong><bean:message key="elemento"/></strong>
	    							</td>
	    							<td class="etiqueta">
	    								<bean:write name="modificarEstructuraForm"  scope="request" property="nombre"/>
	    							</td>
	    						</tr>
	    						
	    						--%>
										<tr>
											<td class="etiqueta" align="right">
												<strong><bean:message key="tipo.item" /> </strong>
											</td>
											<td class="etiqueta">
												<bean:write name="actMostrar" scope="request"
													property="category" />
											</td>
										</tr>
										<tr>
											<td class="etiqueta" align="right">
												<strong><bean:message key="porcentaje.grupo" /> </strong>
											</td>
											<td class="etiqueta">
												<bean:write property="percentage" name="actMostrar"
													scope="request" />
												%
											</td>
										</tr>
										<tr>
											<td class="etiqueta" align="right">
												<strong><bean:message key="fecha.realizacion" /> </strong>
											</td>
											<td class="etiqueta">
												<bean:write property="date" name="actMostrar"
													scope="request" />
											</td>
										</tr>
										<tr>
											<td class="etiqueta" align="right">
												<strong><bean:message key="descripcion" /> </strong>
											</td>
											<td class="etiqueta">
												<bean:write property="description" name="actMostrar"
													scope="request" />
											</td>
										</tr>
										<tr>
											<td class="etiqueta" align="right">
												<strong><bean:message key="temas" /> </strong>
											</td>
											<td class="etiqueta">
												<bean:write property="topics" name="actMostrar"
													scope="request" />
											</td>
										</tr>
									</table>
								</td>
								<%
									}
								%>
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