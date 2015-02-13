<%@ page language="java" errorPage="/profesores/error.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<%@ page import="co.edu.icesi.notas.*"%>
<%@ page import="co.edu.icesi.notas.control.ControlProfesores"%>
<%@ page import="java.util.Date"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<logic:present scope="session" name="activa">
	<html>
		<!-- InstanceBegin template="/Templates/principal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
		<head>
			<!-- InstanceBeginEditable name="doctitle" -->
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
					enableTooltips("contn")
				};
			</script>

			<script language="JavaScript" type="text/javascript">
				function funcCerrar() {
					confirma = confirm('¿Está seguro de que desea cerrar el curso? Recuerde que una vez lo cierre, no podrá realizar modificación alguna.');
					if (confirma) {
						document.cerrarForm.opcion.value = "cerrar";
						document.cerrarForm.pagina.value = "IrCierre";
						document.cerrarForm.submit();
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
                                        </div>
                                    </td>
                                </tr>
				<tr>
					<td>
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
							<html:link styleClass="links" action="/volverInicio"
								title="Regresa al listado de los cursos">
								<bean:message key="listado" />
							</html:link>
							|
							<html:link styleClass="links" action="/aEsquemaBasico"
								title="Sección de configuración del curso">
								<bean:message key="esquema" />
							</html:link>
							|
							<html:link styleClass="links" action="/aRegistroBasico"
								title="Sección del registro de notas">
								<bean:message key="registro" />
							</html:link>
							|
							<html:link styleClass="links" action="/aDefinitivasBasico"
								title="Muestra las notas definitivas">
								<bean:message key="definitivas" />
							</html:link>
							|
							<html:link styleClass="links"
								action="/xlsBasico?fw=esquemaBasico"
								title="Envía a su correo, una copia de las notas registradas, en formato Excel">
								<bean:message key="excel" />
							</html:link>
						</div>
						<br>
						<br>

						<%
							boolean cerrado = (session.getAttribute("cerrado") != null)
										&& (session.getAttribute("cerrado").equals("S"));
								if (cerrado) {
						%>
						<div align="center">
							<fieldset class="fieldset">
								<div class="error" align="left">
									<bean:message key="advertencia.cerrado" />
								</div>
								<br />
							</fieldset>
						</div>
						<%
							}
								boolean errores = (request.getAttribute("error") != null && request
										.getAttribute("error").equals("S"));
								if (errores) {
						%>
						<div align="center">
							<fieldset class="fieldset">
								<div class="error" align="left">
									<html:errors property="noCerrarCurso" />
								</div>
								<br />
							</fieldset>
						</div>
						<%
							}
						%>

						<table cellpadding="4" align="center">
							<tr>
								<td align="center">
									<h2 class="curso">
										<bean:write name="curso" scope="session"
											property="descripcionMateria" />
										<br>
										Grupo:
										<bean:write name="curso" scope="session" property="grupo" />
										<br>
										<br>
										<bean:message key="cierreCurso.cierre" />
									</h2>
								</td>
							</tr>
							<tr>
								<td align="center">
									<%
										Curso curso = (Curso) session.getAttribute("curso");
											java.util.Date fechaLimite = (java.util.Date) session
													.getAttribute("fechaLimite");
											java.util.Date fechaInicio = (java.util.Date) session
													.getAttribute("fechaInicio");
											java.util.Date hoy = (java.util.Date) session
													.getAttribute("fechaActual");
											java.text.DateFormat df = new java.text.SimpleDateFormat(
													"yyyy-MM-dd");
											String s = df.format(hoy);
											boolean disable = true;
											if (s.equals(fechaLimite.toString())) {
												disable = false;
											} else {
												boolean antes = hoy.after(fechaLimite);
												boolean despues = hoy.before(fechaInicio);
												disable = antes || despues;
											}

											if (!cerrado) {
									%>

									<p align="left" class="info">
										Ha decidido cerrar el curso. Con esta acción los datos del
										curso, incluyendo porcentajes y notas, no podrán ser
										modificados. Por tal motivo, previo a cerrar el curso, por
										favor
										<html:link styleClass="links" action="/aDefinitivasBasico">revise las notas 
              definitivas</html:link>
										y asegurese de que est&eacute;n correctas. Una vez realizado
										lo anterior, cierre el curso mediante el bot&oacute;n que
										aparece a continuaci&oacute;n.
										<br />
									</p>
									<div align="center">
										<html:form action="/cerrarCurso">
											<html:hidden property="opcion" />
											<html:hidden property="pagina" />
											<html:button property="cerrar" value="Cerrar curso"
												onclick="funcCerrar()" disabled="<%=disable||cerrado%>" />
											<%-- <html:button property="habilitar" value="Habilitar curso"  onclick="funcCerrar(2)" disabled="<%=disable||!cierra%>"></html:button>--%>
										</html:form>
									</div>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<%
										} else {
									%>

									<p align="left" class="info">
										El curso se ha cerrado satisfactoriamente. En el enlace que se
										encuentra a continuación de este párrafo podrá descargar el
										reporte con el listado de clase y las notas definitivas de
										cada estudiante. Por favor imprima dos (2) copias del reporte
										y llevar una de ellas firmada a la secretaría de su
										departamento.
									</p>
									<br />
									<br />
									<div align="center" class="info">
										<a
											href="http://www.icesi.edu.co/portal/pls/portal/psiaepre.ppregen_lista_clase?pMateria=<c:out value="${curso.codigoMateria}"/>&pCedulaProf=<c:out value="${profesores.profesor.cedula}" />"
                                                                                        target="_blank"> <span style="font-size: 30px;">Descargar reporte de notas definitivas</span> </a>
										<br />
                                                                                <br />
                                                                               <br />
										<br />
									</div>

									<div align="left" class="info">
										Si desea realizar la modificación de alguna nota, por favor
										realice el proceso de modificación de nota que usted conoce.
										<br />
										<br />
									</div>
									<%
										}
									%>

								</td>
							</tr>
						</table>
						<br>
						<br>
						<center>

							<html:link styleClass="titulo2" action="/volverInicio">
								<bean:message key="listado" />
							</html:link>
						</center>
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