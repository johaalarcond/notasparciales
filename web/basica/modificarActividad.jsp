<%@ page language="java" errorPage="/profesores/error.jsp"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="co.edu.icesi.notas.*"%>
<%@ page import="java.util.Date"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="cache-control" content="no-cache">
			<meta http-equiv="expires" content="0">
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/datePicker.js"></script>
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/overlib_mini.js"></script>
			<script type="text/javascript" src="BubbleTooltips.js"></script>
			<script type="text/javascript">
				window.onload=function(){
					enableTooltips("contn");
				};
			</script>

			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
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
						<p class="info" align="right">
							<!--<a href="<%=request.getContextPath()%>/cerrarSesion.jsp"
								class="links"> <bean:message key="cerrar.sesion" /> </a>
							<br />
							<br />-->
							<bean:message key="periodo.academico" />
							<strong><bean:write name="curso"
									property="periodoAcademico" /> </strong>
						</p>
						<br>
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
									</h2>
								</td>
							</tr>

							<tr>
								<TD>
									<%
										boolean cerrado = ((String) session.getAttribute("cerrado"))
													.equals("S");
									%>
									<html:form action="/actualizarActividad">
										<div class="curso" align="center">
											<bean:message key="modificacion.evaluacion" />
										</div>
										<br />
										<table align="center" class="tabla">
											<tr>
												<td class="celda2" align="right">
													<bean:message key="item" />
												</td>
												<td class="etiqueta">
													<bean:write name="actualizarActividadForm" scope="request"
														property="name" />
													<html:hidden property="name" />
												</td>
											</tr>
											<tr>
												<td class="celda2" align="right">
													<bean:message key="categoria" />
												</td>
												<td class="etiqueta">
													<bean:write name="actualizarActividadForm" scope="request"
														property="category" />
													<html:hidden property="category" />
												</td>
											</tr>
											<tr>
												<td class="celda2" align="right">
													<bean:message key="porcentaje.grupo" />
												</td>
												<td>
													<span class="etiqueta"><bean:write
															property="percentage" scope="request"
															name="actualizarActividadForm" />%</span>
													<html:hidden property="percentage" />
												</td>
											</tr>
											<tr>
												<td class="etiqueta" align="right">
													<bean:message key="fecha.realizacion" />
													<div class="aclaracion">
														<bean:message key="fecha.formato" />
													</div>
												</td>
												<td>
													<html:text styleClass="etiqueta" property="date"
														disabled='<%=(!cerrado)?false:true%>' />
													<%
														if (session.getAttribute("cerrado").equals("N")) {
													%>
													<script language="JavaScript" type="text/JavaScript">
			if (navigator.appName.lastIndexOf('Explorer') == -1){
				if(document.actualizarActividadForm.date.value==null || document.actualizarActividadForm.date.value==''){
					document.actualizarActividadForm.date.value="aaaa-mm-dd";
					}
			}else{
			  document.write("<div id=\"overDiv\" style=\"position:absolute; z-index:1002; visibility: inherit;\"></div>");
              <!-- ggPosX and ggPosY not set, so popup will autolocate to the right of the graphic -->
              document.write("<a href=\"javascript:show_calendar('actualizarActividadForm.date');\" onMouseOver=\"window.status='Date Picker'; overlib('Click aquí para seleccionar una fecha'); return true;\" onMouseOut=\"window.status=''; nd(); return true;\"><img src=\"./imgs/calendar.gif\" width=24 height=22 border=0></a>");
            }
		</script>
													<%
														}
													%>
													<div class="error">
														<html:errors property="fechaRealizacion" />
													</div>
												</td>
											</tr>
											<tr>
												<td class="etiqueta" align="right">
													<bean:message key="descripcion" />
												</td>
												<td>
													<html:textarea property="description" styleClass="etiqueta"
														disabled='<%=(!cerrado)?false:true%>' />
												</td>
											</tr>
											<tr>
												<td class="etiqueta" align="right">
													<bean:message key="temas" />
												</td>
												<td>
													<html:textarea property="topics" styleClass="etiqueta"
														disabled='<%=(!cerrado)?false:true%>' />
												</td>
											</tr>
											<tr>
												<td colspan="2" align="center">
													<html:submit value="Modificar" styleClass="etiqueta"
														disabled='<%=(!cerrado)?false:true%>' />
													&nbsp;
													<html:cancel value="Cancelar" styleClass="etiqueta" />
												</td>
											</tr>
										</table>
									</html:form>

									<br>
								</TD>
							</tr>
						</table>

						<br>
						<br>

						<center>

							<html:link styleClass="titulo2" action="/volverInicio">
								<bean:message key="listado" />
							</html:link>
						</center>
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