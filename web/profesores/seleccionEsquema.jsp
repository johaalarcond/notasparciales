<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@page import=" java.util.*,co.edu.icesi.notas.*; " %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">

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
			onload="document.forms[0].opcionEsquema[0].checked=true;cerrarSesion();">
			<jsp:include page="../template/header.jsp"></jsp:include>
                        
                        
                        
                        <%--Adicionado ffceballos 
                        
                      <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                      <%=enlace %>
                         <c:set var="a" value="<%=enlace %>"/>
                         <c:if test="${a=='08'}">
                              <script> 
                                //  alert('Por favor comuniquese con el coordinador de esta materia para que realice el esquema del curso');
                                //  document.location.href="configuracionEsquema.icesi";
                                //
                              //  document.formulario.radio1
//document.formulario.submit();
                                                                                           
                              </script> 
                         </c:if>
                        
                        <!--fin ffceballos --%>
                        
                        
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
							<bean:message key="periodo.academico" />
							<strong><bean:write name="curso"
									property="periodoAcademico" /> </strong>
						</p>
						<br />
						<table>
							<tr>
								<td align="center" colspan="2">
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
								<td height="21" class="curso" colspan="2" align="center"
									valign="middle">
									Selección de esquema de evaluación
								</td>
							</tr>
							<tr>
                                                            
                                                              <!--adicionado fabian ceballos  -->
                                                              <%--     <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                                
                                                                                    <c:set var="a" value="<%=enlace %>"/>
                                                                                   <% if(!enlace.equalsIgnoreCase("08")){  %>
                                                                             --%>          
                                                             
								<td class="info" height="116" colspan="2" valign="middle">
                                                                      
									En esta sección, podrá establecer qué tipo de esquema de
									evaluación desea configurar para el curso. La primera opción
									(esquema básico) le permitirá crear y editar las actividades de
									su curso, y asignarles sus respectivos porcentajes. Por otro
									lado, con la opción "Esquema intermedio" usted podrá, además,
									establecer un porcentaje para las notas individuales y un
									porcentaje para las notas grupales, de forma tal que estas
									últimas solo sean tenidas en cuenta, en caso de que la nota
									individual total sea
									<strong>superior o igual a 3.0</strong>. Este cálculo
									igualmente aplica para el esquema básico, sólo que usted tendrá
									mayor control sobre el mismo usando el esquema intermedio.
                                                                         
								</td> <%--    <% } %> --%>  <!--fin fabian ceballos -->
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
								<td width="223" height="0"></td>
								<td width="481"></td>
							</tr>
                                                                        <html:form action="/configuracionEsquema" >
								<tr>
									<td height="59"></td>
									<td align="left" valign="middle" class="info">
                                                                            <html:radio property="opcionEsquema" value="B" >
											<bean:message key="esquema.basico" />
										</html:radio>
										<br>
										<%-- 
                                                                                <!--adicionado fabian ceballos -->
                                                                                 <%  enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                                
                                                                                    <c:set var="a" value="<%=enlace %>"/>
                                                                                    
                                                                                    <% if(!enlace.equalsIgnoreCase("08")){  %>
                                                                                    --%>
                                                                                    
                                                                                        
                                                                                        
                                                                                <html:radio property="opcionEsquema" value="I">
											<bean:message key="esquema.intermedio" />
										</html:radio>
                                                                                        
                                                                                    <%--  <% } %>--%>  
                                                                                        
                                                                                        
                                                                                        <!--fin fabian ceballos -->
										<br>
									</td>
								</tr>

								<tr>
									<td align="center" valign="middle" class="info" colspan="2">
										<html:submit value="Aceptar" />
										<html:cancel value="Cancelar" />
									</td>
								</tr>
							</html:form>
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