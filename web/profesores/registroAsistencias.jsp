<%@ page language="java" errorPage="/profesores/error.jsp"
	import="co.edu.icesi.notas.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
			<script language="javascript">
function cambioMesRegistro(){
	<%if (request.getAttribute("error") != null) {%>
		alert("Debe ingresar datos válidos antes de realizar un cambio de mes");
	<%} else {
					if (request.getAttribute("guardado") == null
							&& session.getAttribute("cerrado").equals("N")) {%>
		var cambiarMes=confirm('Aún no ha guardado el registro de asistencias del mes actual. Confirme si desea cambiar de mes; los datos que haya ingresado se perderán.');
		<%} else {%>var cambiarMes=true;<%}%>
		if(cambiarMes==true){
			mes = document.getElementById('mesAsistencias').value;
			window.location="<%=request.getContextPath()%>/registroAsistencias.icesi?mesAsistencias="+mes;	
		}else{
			document.getElementById('mesAsistencias').value='<%=request.getParameter("mesAsistencias")%>';
		}
	<%}%>
}
function cambioCheck100(elemento){
	var id = elemento.id;
	var numAls = <%=((Curso) (session.getAttribute("curso")))
								.getAlumnos().size()%>
	if(elemento.checked==true){
		for(i=0; i < numAls; i++){
			document.getElementById('alumno['+i+'].asistenciaProgramacionIndexada['+id+'].porcentajeAsistencia').value=100;
		}
	}else{
		for(i=0; i < numAls; i++){
			document.getElementById('alumno['+i+'].asistenciaProgramacionIndexada['+id+'].porcentajeAsistencia').value='';
		}
	}
}

function infoGuardado(){
	<%if (request.getAttribute("guardado") != null) {%>
		alert('Sus cambios han sido guardados');
	<%}%>
}

</script>
			<script type="text/javascript" src="BubbleTooltips.js"></script>
			<script type="text/javascript">
				window.onload=function(){
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
			topmargin="0" marginwidth="0" marginheight="0"
			onload="infoGuardado(); cerrarSesion();">
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
						<div class="curso" align="center">
							<bean:write name="curso" scope="session"
								property="descripcionMateria" />
							<br>
							Grupo:
							<bean:write name="curso" scope="session" property="grupo" />
							<br>
						</div>
						<br />
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
								action="/xlsBasico?fw=definitivasBasico"
								title="Envía a su correo, una copia de las notas registradas, en formato Excel">
								<bean:message key="excel" />
							</html:link>
							<br />
							<html:link styleClass="links" action="/registroAsistencias"
								title="Sección de registro de asistencias">
								<bean:message key="registro.asistencias" />
							</html:link>
							|
							<html:link styleClass="links" action="/aConsolidadoAsistencias"
								title="Muestra el porcentaje total de asistencia de cada estudiante a las clases del semestre.">
								<bean:message key="consolidado.asistencias" />
							</html:link>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
						<br />
						<br />
					</td>
				</tr>
				<%
					if (request.getAttribute("error") != null
								|| session.getAttribute("cerrado").equals("S")) {
				%>
				<tr>
					<td align="center">
						<fieldset class="fieldset">
							<div class="error" align="left">
								<html:errors property="errorEntero" />
								<%
									if (session.getAttribute("cerrado").equals("S")) {
								%>
								<bean:message key="advertencia.cerrado" />
								<%
									}
								%>
							</div>
						</fieldset>
                                                        
						<br />

					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<%
					}
				%>
				<tr>
					<td>
						<div align="center" class="resaltado">
							<bean:message key="mensaje.asistencias" />
							<br />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div align="center" class="resaltado">
							<bean:message key="mensaje.asistencias.campo" />
							<br />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<div align="center">
							<html:form action="/guardarAsistencias">
								<span class="titulo2">Mes</span>
								<html:select property="mesAsistencias"
									onchange="cambioMesRegistro()" styleId="mesAsistencias">
									<html:optionsCollection label="nombre" value="id" name="meses" />
								</html:select>

								<br>
								<br>
								<table border="1" cellspacing="0" align="center">
									<tr>
										<td class="celda2" align="center">
											Horas del curso
										</td>
										<td class="celda2" align="center">
											Máxima inasistencia permitida
										</td>
									</tr>
									<tr>
										<td class="celda2" align="center">
											<bean:write name="curso" property="totalHoras" />
										</td>
										<td class="celda2" align="center">
											<%
												out
																.write(""
																		+ ((Curso) session.getAttribute("curso"))
																				.getHorasPerdida(Integer
																						.parseInt(session
																								.getAttribute(
																										"porcentajeMinimoAsistencia")
																								.toString())));
											%>
										</td>
									</tr>
								</table>
								<br>
								<table border="1" cellspacing="0" align="center">
									<tr>
										<td class="celda2" align="center">
											C&oacute;digo
										</td>
										<td class="celda2" align="center">
											Nombre
										</td>
										<logic:iterate name="curso" property="programaciones"
											id="programacion" scope="session" type="Programacion">
											<td class="celdaProg" align="center" width="80">
												<bean:write name="programacion" property="fecha" />
												<br />
												<bean:write name="programacion"
													property="horaInicioFormateada" />
												-
												<bean:write name="programacion" property="horaFinFormateada" />
											</td>
										</logic:iterate>
										<td class="celda2" align="center">
											Asistencia
											<br>
											del mes
										</td>
									</tr>
									<tr>
										<td colspan="2" class="celda2">
											&nbsp;
										</td>
										<logic:iterate name="curso" property="programaciones"
											id="programacion" indexId="i">
											<td align="center" class="check100">
												<input onClick="cambioCheck100(this);" type="checkbox"
													name="<%=i%>" id="<%=i%>"
													<%if (!session.getAttribute("cerrado").equals("N")) {%>
													disabled="disabled" <%}%>>
												100% todos
											</td>
										</logic:iterate>
										<td class="celda2">
											&nbsp;
										</td>
									</tr>
									<logic:iterate name="registroAsistenciasForm"
										property="alumnos" id="alumno"
										type="co.edu.icesi.notas.form.Alumno" indexId="i">
										<tr>
											<td align="left" class="celda2">
												<bean:write name="alumno" property="codigo" />
											</td>
											<td align="left" class="celda2">
												<bean:write name="alumno" property="nombre" />
												&nbsp;
												<bean:write name="alumno" property="apellidos" />
											</td>
											<logic:iterate name="alumno" property="asistencias"
												id="asistencia" type="co.edu.icesi.notas.form.Asistencia"
												indexId="j">
												<td align="center" class="celda2">
													<logic:equal value="true"
														property='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].error"%>'
														name="registroAsistenciasForm">
														<html:text size="3" maxlength="3"
															name="registroAsistenciasForm"
															property='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia"%>'
															styleId='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia"%>'
															value='<%=asistencia.getPorcentajeAsistencia()%>'
															styleClass="campoError, inputRegAsist"
															disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />%
													<html:errors
															property='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia"%>' />
													</logic:equal>
													<logic:notEqual value="true"
														property='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].error"%>'
														name="registroAsistenciasForm">
														<html:text size="3" maxlength="3"
															name="registroAsistenciasForm" styleClass="inputRegAsist"
															styleId='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia"%>'
															property='<%="alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia"%>'
															value='<%=asistencia.getPorcentajeAsistencia()%>'
															disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />%
												</logic:notEqual>
												</td>
											</logic:iterate>
											<logic:lessThan name="alumno"
												property="promedioAsistenciaMes"
												value='<%((Integer) session
									.getAttribute("porcentajeMinimoAsistencia"))
									.intValue();%>'>
												<td align="center" class="celda4">
													<bean:write name="alumno" property="promedioAsistenciaMes" />
													%
												</td>
											</logic:lessThan>
											<logic:greaterEqual name="alumno"
												property="promedioAsistenciaMes"
												value='<%((Integer) session
									.getAttribute("porcentajeMinimoAsistencia"))
									.intValue();%>'>
												<td align="center" class="celda2">
													<bean:write name="alumno" property="promedioAsistenciaMes" />
													%
												</td>
											</logic:greaterEqual>
										</tr>
									</logic:iterate>
								</table>
								<p>
									&nbsp;
								</p>
								<html:submit value="Guardar" property="boton"
									disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />&nbsp;&nbsp;<html:cancel
									property="boton" value="Cancelar" />&nbsp;&nbsp;<html:submit
									property="boton" value="Recalcular"
									disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
							</html:form>
						</div>
						<div align="left">
							Dias festivos del mes:
							<br />
							<logic:iterate name="festivosMes" scope="session" id="festivo">
								<bean:write name="festivo" />
								<br />
							</logic:iterate>
						</div>
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
