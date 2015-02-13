<%@ page language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<logic:present scope="session" name="activa">
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html>
		<head>
			<title>Seguimiento a estudiantes - Universidad Icesi - Cali,
				Colombia</title>
			<meta http-equiv="Content-Type"
				content="text/html; charset=iso-8859-1">
			<meta http-equiv="pragma" content="no-cache">
			<meta http-equiv="cache-control" content="no-cache">
			<meta http-equiv="expires" content="0">
			<script language="JavaScript"
				src="http://www.icesi.edu.co/comun/overlib_mini.js"></script>
			<script type="text/javascript" src="../BubbleTooltips.js"></script>
			<script type="text/javascript">
				window.onload=function(){
					enableTooltips("contn");
				};
			</script>
			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
		</head>

		<script language="javascript" type="text/javascript">
			function mostrarDiv(enlace, prefijo){
				prefijo = (typeof prefijo == 'undefined') ? '': prefijo;
				var division = document.getElementById(prefijo+enlace.name);
				var estiloDivision = division.style;
				if(estiloDivision != null){
					var despliegueDivision = estiloDivision.display;
					if(despliegueDivision == null || despliegueDivision == '' || despliegueDivision == 'none'){
						division.style.display = 'block';
					}else{
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
				<!--<tr>
					<td>
						<p class="info" align="right">
							<a href="<%=request.getContextPath()%>/cerrarSesion.jsp"
								class="links"> <bean:message key="cerrar.sesion" /> </a>
						</p>
					</td>
				</tr>-->
				<tr>
					<td>
						<div id="wrapper" align="center">
							<div class="introduccion">
								<p align="center">
									La consulta puede demorar más de 30 segundos dependiendo del
									criterio de búsqueda
								</p>
							</div>
							<div class="texto">
								<html:form action="/segestud/busquedaSegestud">
									<div class="segestud_formulario" align="center">
										<p>
											Criterio de búsqueda
										</p>
										<html:text property="estudiante" />
										<html:submit>Buscar</html:submit>
									</div>
									<a href="#" name="criterios" onClick="mostrarDiv(this, 'div_')">Ejemplos</a>
									<div id="div_criterios" class="texto">
										Puede buscar:

										<table align="center" border="1" cellspacing="0">
											<tr>
												<td>
													Código completo
												</td>
												<td>
													0417020
												</td>
											</tr>
											<tr>
												<td>
													Código parcial
												</td>
												<td>
													041, 0417, 04170, 041702
												</td>
											</tr>
										</table>
									</div>
								</html:form>
							</div>
							<div id="resultados">
								<hr>
								<logic:notPresent name="definitivas">
									<div class="texto">
										Ingrese un criterio para realizar la búsqueda de estudiantes
									</div>
								</logic:notPresent>
								<logic:present name="definitivas">
									<div align="center" class="texto"
										style="border: #FFCCCC 2px solid; text-align: center;">
										<bean:size id="resultados" name="definitivas" />
										<b>Estudiantes obtenidos: <%=resultados%></b>
									</div>
									<div id="segestud_resultado">
										<div id="alumnos" align="left">
											<logic:iterate id="v" name="definitivas">
												<bean:define id="alumno" name="v" property="value" />
												<div class="info_alumno">
													<a href="#<%=alumno%>" name="<%=alumno%>"
														onClick="mostrarDiv(this, 'alumno_');"><bean:write
															name="alumno" property="codigo" /> - <bean:write
															name="alumno" property="nombre" /> </a>
													<div id="alumno_<%=alumno%>" style="display: none;">
														
                            												<!--   ffceballos 20/01/2014
                                        
                                                                                                                             <tr>
																<th>
																	Código de la materia
																</th>
																<th class="nombre_materia">
																	Nombre de la materia
																</th>
																<th>
																	Grupo
																</th>
																<th>
																	Definitiva 
																</th>
															</tr> -->
                                                                                                        
															<logic:iterate id="curso" name="alumno" property="cursos">
                                                                                                                            </br>
																<table class="cursos_alumno" align="center" border="1"
															cellspacing="0" cellpadding="0">	
                                                                                                                                            
                                                                                                                            <%--  <bean:write name="curso"/> --%>
                                                                                                                                            
                                                                                                                                          
                                                                                                                                            <%
                                                                                                                                            String[]materia= ((String)curso).split("\\*");
                                                                                                                                           
                                                                                                                                               
                                                                                                                                                                                                                                          
                                                                                                                                               if (materia.length>1){
                                                                                                                                                String cadenaActividades=materia[1];
                                                                                                                                                 String []actividades=cadenaActividades.split("\\]"); 
                                                                                                                                                 String materiaDef=materia[0];
                                                                                                                                                 String []arregloMateriaDef=materiaDef.split("\\#"); 
                                                                                                                                            out.print("<tr align='center' > <th colspan='"+(actividades.length+1)+"'>  "+arregloMateriaDef[1]+" </th></tr>");
                                                                                                                                            out.print("<tr align='center'>");                                                              
                                                                                                                                                                                                                                                
                                                                                                                                                for(int cont=0; cont<((actividades.length));cont++){
                                                                                                                                                   String actividadCad= actividades[cont];                                                                                                
                                                                                                                                                   String []arregloActividad=actividadCad.split("\\+");                                                                                          
                                                                                                                                                   String nombre=arregloActividad[0];                                                
                                                                                                                                                   String resto= arregloActividad[1];
                                                                                                                                                   String []porcentaje=  resto.split("%");                               
                                                                                                                                                   out.println("<th>"+nombre );   
                                                                                                                                                   double numero=Double.parseDouble(porcentaje[0]);     
                                                                                                                                                   out.println("("+Math.rint(numero*10)/10+"%)</th>") ;                                               
                                                                                                                                                }
                                                                                                                                            //   String []actividades=cadenaActividades.split("[");
                                                                                                                                          //  out.print(actividades[0]);                                                                                                
                                                                                                                                                   out.print(" <th> Definitiva </th> </tr> <tr>");  
                                                                                                                                                      
                                                                                                                                                 for(int cont=0; cont<((actividades.length));cont++){
                                                                                                                                                   String actividadCad= actividades[cont];                                                                                                
                                                                                                                                                   String []arregloActividad=actividadCad.split("\\+");                                                                                          
                                                                                                                                                   String nombre=arregloActividad[0];                                                
                                                                                                                                                   String resto= arregloActividad[1];
                                                                                                                                                   String []porcentaje=  resto.split("%");                               
                                                                                                                                                   out.print("<td align='center'>"+porcentaje[1]+"</td>");    
                                                                                                                                                } 
                                                                                                                                                    String definitivaMateria="";  
                                                                                                                                                    if(arregloMateriaDef[0].equals("9.9")){ definitivaMateria="Pendiente"; }else{ if(arregloMateriaDef[0].equals("7.7")){ definitivaMateria="N.A."; }else{ if(arregloMateriaDef[0].equals("8.8")){ definitivaMateria="A";}else{ definitivaMateria=arregloMateriaDef[0]; } } }                            
                                                                                                                                                    out.print("<td align='center'>"+definitivaMateria+"</td></tr> ");                                            
                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                    
                                                                                                                                                    }


                                                                                                                                            %>
                                                                                                                                            
																	<%--	<bean:write name="curso" property="matcod" />
																	</td>
																	<td>
																		<bean:write name="curso" property="materia" />
																	</td>
																	<td>
																		<bean:write name="curso" property="grupo" />
																	</td>
																	<td>
																		<bean:write name="curso" property="definitiva" />
                                                                                                                                        
																	</td>
																</tr>--%></table>
															</logic:iterate>
														<!--</table>  fin ffceballos -->
													</div>
												</div>
											</logic:iterate>
										</div>
									</div>
								</logic:present>
							</div>
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