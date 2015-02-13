<%--
<%@ page language="java" errorPage="/profesores/error.jsp"%>
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@ page import="co.edu.icesi.notas.Categoria"%>
<%@ page import="co.edu.icesi.notas.Curso"%>
<%@ page import="co.edu.icesi.notas.Actividad"%>
<%@ page import="co.edu.icesi.notas.utilidades.OperacionesMatematicas"%>
<%@ page import="java.util.ArrayList"%>
<logic:present scope="session" name="activa">
	<html>
	<head>
	<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link href="<%=request.getContextPath()%>/css/estilos.css"
		rel="stylesheet" type="text/css">
	<script language="javascript" type="text/javascript">
	var c =
<%=session.getMaxInactiveInterval()%>
	;
	function cs() {
		c = c - 1;
		if (c == 0) {
			document.location = "/notasparciales/sesionInactiva.icesi";
		}
	}

	function cerrarSesion() {
		setInterval("cs()", 1000);
	}
</script>
	</head>

	<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
		topmargin="0" marginwidth="0" marginheight="0" onload="cerrarSesion()">
	<script language="JavaScript">
	function cambiarTipoSubmit(opcion) {
		document.calificacionForm.tipoSubmit.value = opcion;
	}
</script>
	<jsp:include page="../template/header.jsp"></jsp:include>
	<table border="0" align="center" cellpadding="00"
		cellspacing="0">
		<tr>
			<td height="18" nowrap class="info" align="center">
			<div align="center" class="error"><br />
			Recuerde que esta sesión tiene un tiempo de inactividad máximo 
			permitido de 8 minutos. <br />
			Pasado este tiempo su sesión será terminada automáticamente. <br />
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
                            <!--<a
				href="<%=request.getContextPath()%>/cerrarSesion.jsp" class="links">
			<bean:message key="cerrar.sesion" /> </a> <br />
			<br />-->
			<bean:message key="periodo.academico" /> <strong><bean:write
				name="curso" property="periodoAcademico" /> </strong></p>
			<div class="curso" align="center"><bean:write name="curso"
				scope="session" property="descripcionMateria" /> <br>
			Grupo: <bean:write name="curso" scope="session" property="grupo" />
			<br>
			</div>
			<div class="curso" align="center"><bean:write
				name="padreActual" property="nombre" /> ( <bean:write
				name="padreActual" property="porcentaje" /> %)</div>
			<br />
			<div align="center" class="resaltado"><bean:message
				key="mensaje" /> <br />
			<br />
			<bean:message key="mensaje.notas.campo" /></div>
			<br>
			</td>
		</tr>

		<%
			if (request.getAttribute("error") != null
						|| session.getAttribute("cerrado").equals("S")) {
		%>
		<tr>
			<td align="center">
			<fieldset class="fieldset">
			<div class="error" align="left"><html:errors property="boton" />
			<%
				if (session.getAttribute("cerrado").equals("S")) {
			%> <bean:message key="advertencia.cerrado" /> <%
 	}
 %>
			</div>
			</fieldset>
			<br />
			</td>
		</tr>
		<%
			}
		%>
		<tr>
			<td><html:form action="/guardarNotas">
				<html:hidden property="tipoSubmit" />
				<table border="1" cellspacing="0" align="center">
					<tr>
						<td class="celda2" align="center"><bean:message key="codigo" />
						</td>
						<td class="celda2" align="center"><bean:message key="nombre" />
						</td>
						<logic:iterate name="padreActual" id="actividad"
							property="actividades" indexId="index">
							<td class="celda2" align="center"><bean:write
								name="actividad" property="nombre" /> <br>
							<bean:write name="actividad" property="porcentaje" /> %c</td>
						</logic:iterate>
						<td class="celda2" align="center"><bean:message
							key="definitiva" /></td>
					</tr>
					<%
						int tamanno = ((ArrayList) session.getAttribute("tamannoM"))
										.size();
					%>
					<logic:iterate name="calificacionForm" id="calificacion"
						property="matriculas" indexId="index"
						type="co.edu.icesi.notas.form.Calificacion">
						<tr>
							<logic:equal value='<%="A"%>' name="calificacion"
								property="estado">
								<td class="celda"><bean:write name="calificacion"
									property="codigo" /> <html:hidden name="calificacion"
									property="codigo" indexed="true" /></td>
								<td class="celda"><bean:write name="calificacion"
									property="nombre" /> <html:hidden name="calificacion"
									property="nombre" indexed="true" /> <html:hidden
									name="calificacion" property="estado" indexed="true" /></td>
								<logic:iterate name="calificacion" id="nota" property="notas"
									indexId="index2" type="co.edu.icesi.notas.form.Nota">
									<td class="celda" align="left"><logic:equal value="true"
										property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].error"%>'
										name="calificacionForm">
										<html:text
											tabindex="<%=(((index2.intValue()+1) * tamanno)+index.intValue())+"" %>"
											name="calificacionForm"
											property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].nota"%>'
											size="3"
											disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>'
											styleClass="campoError" />
										<html:errors
											property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].nota"%>' />
									</logic:equal> <logic:notEqual value="true"
										property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].error"%>'
										name="calificacionForm">
										<html:text
											tabindex="<%=(((index2.intValue()+1) * tamanno)+index.intValue())+"" %>"
											name="calificacionForm"
											property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].nota"%>'
											size="3"
											disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
									</logic:notEqual> <html:hidden name="calificacionForm"
										property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].nombreEstructura" %>' />
									<html:hidden name="calificacionForm"
										property='<%= "calificacion[" + index + "].notaIndexada[" + index2 + "].consecutivo" %>' />
									</td>
								</logic:iterate>
								<td class="resaltado" align="center">
								<%
									Categoria pg = (Categoria) session
															.getAttribute("padreActual");
								%> <%=OperacionesMatematicas.redondear(
									pg.calcularNotaSinPorcentaje(((Curso) session
											.getAttribute("curso"))
											.getAlumno(calificacion.getCodigo())),
									1)%></td>
							</logic:equal>
						</tr>
					</logic:iterate>

					<tr>
						<%-- Con las siguientes instrucciones se muestran los promedios de cada parcial, quiz, etc. --%>
						<td class="resaltado" colspan="2" align="right"><bean:message
							key="promedio" /></td>
						<logic:iterate name="padreActual" id="estructura"
							property="actividades" indexId="index">
							<td class="celda2" align="center"><%=((Actividad) estructura).getPromedio()%>
							<br>
							</td>
						</logic:iterate>
						<td class="resaltado">&nbsp;</td>
					</tr>
					<tr>
						<%-- La siguiente expresión es para mostrar bien organizados los botones de Aceptar y Cancelar en la tabla. --%>
						<td
							colspan='<%=((Categoria) session.getAttribute("padreActual"))
							.getActividades().size() + 3%>'
							align="center" class="celda"><html:submit value="Guardar"
							onclick="cambiarTipoSubmit('guardar')"
							disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
						<html:cancel value="Cancelar" /> <html:button
							value="Recalcular notas" property="boton"
							onclick="cambiarTipoSubmit('recalcular')"
							disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
						</td>
					</tr>
				</table>
			</html:form> <br />
			<div align="center" class="resaltado"><bean:message
				key="mensaje" /> <br />
			<br />
			</div>
			<br>
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