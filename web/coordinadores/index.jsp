<%@page import="java.util.Date"%>
<%@page import="co.edu.icesi.notas.Curso"%>
<%@page import="java.util.ArrayList"%>
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
	
    <!-- documento Creado por ffceballos-->
    <html>
		<head>
			<title>Seguimiento a cursos - Universidad Icesi - Cali,
				Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">

			<script>
	function enviar(form, opcionU) {
		//var formulario = document.getElementById(form);
               // var formulario = document.getElementById('cursosDeptoForm');
		//formulario.opcion.value = opcionU;
                cursosDeptoForm.opcion.value = opcionU;
              //  document.getElementById('opcion').value=2;
		document.cursosDeptoForm.submit();
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
                                                                    
                                                                                                
                        <c:if test="${RCOORDINMAT || RSECREMAT}">
                            
                           
                            <tr>
                                <td>
                                    <div id="form_busqueda_jefes">
                                        <div id="busqueda" align="center">
                                            <html:form action="/coordinadores/busquedaCursos" style="form_buscar">
                                                <p>
                                                    Criterio de búsqueda
                                                </p>
                                                <html:text property="criterio" />
                                                <html:button onclick="enviar('form_buscar', 1)" value="Buscar" property="boton" styleClass="info" />
                                                <html:hidden property="opcion" value="1" />
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            
                            </html:form>
                            
                            
                            <jsp:useBean class="co.edu.icesi.notas.control.ControlProfesores" id="profesores" scope="session" />
                            
                            <html:form action="/listaCursos">
                             <html:hidden property="opcion" value="1" />
                            <tr>
								<td class="info">
									<logic:present name="cursos">
										<p align="center">
											<logic:present name="resultadoBusqueda">
											Resultados con el criterio <b><bean:write
														name="resultadoBusqueda" /> </b>
											</logic:present>
											<logic:notPresent name="resultadoBusqueda">
											Búsqueda global
										</logic:notPresent>
										</p>
										
                                                                                <div align="center">  
                                                                                      
                                                                                    
                                                                                           <table>
                                                                                        <tr> <td class="info">
                                                                                                <%int i=0; %>
                                                                                                
                                                                                <logic:iterate name="cursos" id="materia">
                                                                                    <p>
                                                                                        <input type="radio" id="curso" name="curso" value="<bean:write name="materia" property="codigoMateria" />-<bean:write name="materia" property="grupo" />"  <%if (i == 0) {
                                                                                                out.println("checked=\"checked\"");
                                                                                            }%> > 
                                                                                         <bean:write name="materia" property="descripcionMateria" /> 
                                                                                         
                                                                                        
                                                                                    </p>
                                                                                    
                                                                                     <% i++; %>
                                                                                </logic:iterate>
                                                                                    </td> </tr>
                                                                                    
                                                                                   
                                                                                    
                                                                                
                                                                                        
                                                                                        
                                                                         <%        java.util.Date fechaLimite = (java.util.Date) session.getAttribute("fechaLimite");
                                                                                java.util.Date fechaInicio = (java.util.Date) session.getAttribute("fechaInicio");
                                                                                java.util.Date hoy = new Date();
                                                                                boolean antes = hoy.after(fechaLimite);
                                                                                boolean despues = hoy.before(fechaInicio);
                                                                                boolean disable = antes || despues;

                                                                                    
                                                                                    %>
                                                                                    
                                                                                    
                                                                                    
                                                                                  
                                                                                    </table>
                                                                                    
                                                                                </div>
                                                                                
                                                                               
                                                                                                <br/><br/>
                                                                                                <div align="center">
                                                                                                    
                                                                                                    <html:submit 
													value="Coordinar" property="boton" styleClass="info" />                                                                                                     
                                                                                                </div>	
											
                                                                                               
                                                                              <!--  </div>  -->
                                                                                
                                                                                
									</logic:present>
								</td>
							</tr>
                            
                            
                            
                             </html:form>
                        </c:if>                                                           
                                                                                                
                                                                                                
                                                               
			</table>
                        
                         <br> <br>
			<jsp:include page="../template/footer.jsp"></jsp:include>
		</body>
	</html>
</logic:present>
<logic:notPresent scope="session" name="activa">
	<jsp:forward page="/notasparciales/sesionInactiva.icesi"></jsp:forward>
</logic:notPresent>