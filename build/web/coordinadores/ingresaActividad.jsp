<%-- <%@ page language="java" errorPage="/profesores/error.jsp"%> ---%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="co.edu.icesi.notas.basica.Activity"%>
<%@ page import="co.edu.icesi.notas.form.ModificarEsquemaForm"%>
<!--ffceballos -->
<%@page import=" java.util.*,co.edu.icesi.notas.*; " %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%> 
<!--ffceballos -->
<logic:present scope="session" name="activa">
	<html>
		<head>
			<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/datePicker.js"></script>
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/overlib_mini.js"></script>
			
                        <script>
                            
                            
                            function agregar(){
                                
                                
                                
                                
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
                                    
                                <table border="1" cellspacing="1" align="center" cellpadding="2">
                                    <tr>
                                    <td class="celda2">
                                    <bean:message key="nombre" />
                                    </td>
                                    <td class="celda2">
                                        <bean:message key="categorias" />
                                    </td>  
                                    <td class="celda2">
                                        <bean:message key="otraCategoria" />
                                    </td>
                                    <td class="celda2">
                                        &nbsp;
                                    </td>
                                </tr>
                                
                                
                                
                                
                                
                                
                                
                                </table>
                                
                                
                                <br>
                                <br>
                                <center>
                                    <% boolean eliminoActividad = session.getAttribute("actividadEliminada") != null
													&& session.getAttribute("actividadEliminada")
															.equals("S");
								%> 
                                                                <html:submit value="Aceptar" />
								<html:button property="boton" value="Agregar Actividades"
									onclick="agregar()" />
								<%
									if (session.getAttribute("nuevo") != null) {
								%>
								<html:cancel value="Cancelar" />
								<%
									}
								%>
                                </center>
                                
                                
                                
                               </tr> 
                                
			</table>
			<jsp:include page="../template/footer.jsp"></jsp:include>
		</body>
	</html>
</logic:present>
<logic:notPresent scope="session" name="activa">
	<jsp:forward page="/notasparciales/sesionInactiva.icesi"></jsp:forward>
</logic:notPresent>