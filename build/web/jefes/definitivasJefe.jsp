<%@ page language="java" errorPage="/profesores/error.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@ page import="co.edu.icesi.notas.*"%>
<logic:present scope="session" name="activa">
    <html>
        <head>
            <title>Seguimiento a cursos - Universidad Icesi - Cali,
				Colombia</title>
            <meta http-equiv="Content-Type"
                  content="text/html; charset=iso-8859-1">
            <script type="text/javascript" src="../BubbleTooltips.js"></script>
            <script type="text/javascript">
                window.onload = function() {
                    enableTooltips("contn");
                };
            </script>
            <script language="JavaScript" type="text/javascript">
                function funcCerrar(valor) {
                    if (valor == 1) {
                        //if(confirm('¿Está seguro de que desea cerrar el curso? Recuerde que una vez lo cierre, no podrá realizar modificación alguna.')){
                        document.cerrarForm.opcion.value = "cerrar";
                        document.cerrarForm.pagina.value = "IrDefinitiva";
                        document.cerrarForm.submit();
                        //}
                    } else {
                        document.cerrarForm.opcion.value = "abrir";
                        document.cerrarForm.pagina.value = "IrDefinitiva";
                        document.cerrarForm.submit();
                    }
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
            <link href="<%=request.getContextPath()%>/css/estilos.css"
                  rel="stylesheet" type="text/css">
        </head>

        <body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
              topmargin="0" marginwidth="0" marginheight="0"
              onload="cerrarSesion()">
            <jsp:include page="../template/header.jsp"></jsp:include>
            <table border="0" align="center" cellpadding="00"
                   cellspacing="0">
                <tr>
                    <td class="info" align="center">
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
                            <html:link styleClass="links" action="/jefes/aCursosJefe"
                                       title="Regresa al listado de cursos">
                                <bean:message key="listado" />
                            </html:link>
							|
                            <!--<html:link styleClass="links" action="/aNotasEstud" title="Sección de notas"><bean:message key="notas.registradas"/></html:link> | -->
                            <html:link styleClass="links" action="/jefes/aEsquemaJefe"
                                       title="Sección para visualizar el esquema del curso.">
                                <bean:message key="esquema" />
                            </html:link>
							|
                            <html:link styleClass="links" action="/jefes/aDefinitivasJefe"
                                       title="Muestra las notas definitivas del curso con las calificaciones ingresadas hasta el momento">
                                <bean:message key="definitivas" />
                            </html:link>
                        </div>
                        <br />
                        <table align="center">
                            <tr class="celda2">
                                <td align="center">
                                    <h2 class="curso">
                                        <bean:write name="curso" scope="session"
                                                    property="descripcionMateria" />
                                        <br>
										Grupo:
                                        <bean:write name="curso" scope="session" property="grupo" />
                                        
                                       
                                      
                                        <br>
						            

						<br />
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
								 <bean:write name="curso"  property="totalHoras"  />	
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
                                        <br>
                                        <br>
                                        <bean:message key="definitivas" />
                                    </h2>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <icesi:definitivasBasico estudiante="false" />
                                    <br />
                                </td>

                            </tr>
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