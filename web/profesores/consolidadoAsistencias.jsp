<%@ page language="java" errorPage="/profesores/error.jsp"
	import="co.edu.icesi.notas.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<logic:present scope="session" name="activa">
<html>
	<head>
		<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<script type="text/javascript" src="BubbleTooltips.js"></script>
		<script type="text/javascript">
			window.onload=function(){
				enableTooltips("contn");
			};
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
								property="periodoAcademico" />
						</strong>
					</p>
					<!-- InstanceBeginEditable name="contenido" -->

					<div class="curso" align="center">
						<bean:write name="curso" scope="session"
							property="descripcionMateria" />
						<br>
						Grupo:
						<bean:write name="curso" scope="session" property="grupo" />
						<br />
						<br />
					</div>
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
			<%if(request.getAttribute("error")!=null ||  session.getAttribute("cerrado").equals("S")){%>
			<tr>
				<td align="center">
					<fieldset class="fieldset">
						<div class="error" align="left">
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
				<td align="center">
					<logic:present name="email">
						<div id="email" class="error">
							<bean:write name="email"/>
						</div>
						<script type="text/javascript" language="javascript">
							function hideEmailDiv(){
								var divEmail = document.getElementById('email');
								var pDivMensaje = divEmail.parentNode;
								if(!pDivMensaje.removeNode){
								    pDivMensaje.removeChild(divEmail);
								}else{
									divEmail.removeNode(true);
								}
							}
							setTimeout('hideEmailDiv()', 10000);
						</script>
					</logic:present>
				</td>
			</tr>
			<tr>
				<td>
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
											out.write(""
											+ ((Curso) session.getAttribute("curso"))
											.getHorasPerdidaPermitda(Double.parseDouble(session
											.getAttribute("porcentajeMinimoAsistencia")
											.toString())));
                                                                         
                                                                
                                                               
								%>
							</td>
						</tr>
					</table>
					<br>
					<icesi:asistencias />
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