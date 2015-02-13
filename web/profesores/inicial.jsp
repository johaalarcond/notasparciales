<%-- <%@ page language="java" errorPage="/profesores/error.jsp"%>--%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@page import=" java.util.*,co.edu.icesi.notas.*; " %>


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
			onload="document.forms[0].opcion[0].checked=true;cerrarSesion();">
			<jsp:include page="../template/header.jsp"></jsp:include>
                        
                        
			<table width="720" border="0" align="center" cellpadding="00"
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
						<br />
                                                
                                                <!-- adicionado ffceballos -->                                             
                                                
                                                 <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); 
                                                 
                                                 
                                                        if(enlace.equalsIgnoreCase("08")){

                                                    %>
                               
                                                  <c:set var="a" value="<%=enlace %>"/>
                                                    
                                                        <!--Matematicas-->   
                                                        <%
                                                        Boolean a=(Boolean)session.getAttribute("RCOORDINMAT");
                                                        boolean b=a.booleanValue();
                                                                if(b){ %>   <!--coordinador -->  <% 
                                                                }else{ %>  <script> 
                                                                      alert('Por favor comuniquese con el coordinador de esta materia para que realice el esquema del curso');
                                                                      document.location.href="volverInicio.icesi";
                                                                  </script>   <% } %>
                                                              
                                                                  <%--     <c:choose> 
                                                          <c:when test="${RCOORDINMAT || RSECREMAT}"> 
                                                              <!--coordinador -->
                                                          </c:when>  
                                                              <c:otherwise> No es  
                                                                  <script> 
                                                                      alert('Por favor comuniquese con el coordinador de esta materia para que realice el esquema del curso');
                                                                      document.location.href="volverInicio.icesi";
                                                                  </script> 
                                                              </c:otherwise> 
                                                      </c:choose>  --%>
                                                  
                                                   
                                                        <% } %>
                                                  <!--FIN ffceballos -->  
                               
						<table>
							<tr>
								<td height="21" class="curso" colspan="2" align="center"
									valign="middle">
									Configuraci&oacute;n Inicial del Curso
								</td>
							</tr>
							<tr>
								<td class="info" height="116" colspan="2" valign="middle">
									Esta es la primera vez que ingresa a la configuraci&oacute;n de
									este curso para este periodo. En el primer paso debe decidir si
									desea crear una configuraci&oacute;n desde el principio o
									copiar el esquema del periodo anterior. En la primera
									opci&oacute;n tendr&aacute; la posibilidad de decidir
									qu&eacute; tipos de calificaciones se han planeado para el
									curso y qu&eacute; porcentajes tendr&aacute; cada una. En la
									segunda opci&oacute;n se copiar&aacute; el esquema del
									per&iacute;odo inmediatamente anterior para este curso; sin
									embargo, si es la primera vez que este curso es configurado
									desde esta aplicación tendrá que hacerlo desde el principio.
								</td>
							</tr>
							<tr>
								<td height="21" colspan="2" align="center" valign="middle">
									<SPAN class="curso">Paso 1 de 2</SPAN>
									<br />
									<br />
									<%
										if (request.getAttribute("error") != null) {
									%>
									<FIELDSET class="fieldset">
										<div class="error" align="left">
											<html:errors property="esquemaAnterior" />
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
							<html:form action="/configuracionInicial">
								<tr>
									<td height="59">
										&nbsp;
									</td>
									<td align="left" valign="middle" class="info">
										<jsp:useBean
											class="co.edu.icesi.notas.control.ControlProfesores"
											id="profesores" scope="session" />
										<html:radio property="opcion" value="1">Crear 
            un esquema desde el principio</html:radio>
										<br>
										<html:radio property="opcion" value="2">Copiar el esquema anterior</html:radio>
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