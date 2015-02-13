<%@ page language="java" errorPage="/profesores/error.jsp"%>

<%-- Esta página se empleará tanto para esquemas básicos como esquemas
     intermedios, aunque esto vaya en contra de la forma como se venía
     trabajando el resto de acciones y páginas del esquema intermedio.
     Esto se debe en primer lugar a facilidad de implementación, y en 
     segundo lugar a la necesidad de no aumentar la complejidad de la 
     aplicación incrementando la cantidad de forwards requeridos. --%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@page import="co.edu.icesi.notas.Curso"%>
<%@ page import="java.util.*" %>

<logic:present scope="session" name="activa">
    <html>
        <head>
            <title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
            <meta http-equiv="Content-Type"
                  content="text/html; charset=iso-8859-1">
            <meta http-equiv="pragma" content="no-cache">
            <meta http-equiv="cache-control" content="no-cache">
            <meta http-equiv="expires" content="0">
            <script language="JavaScript" type="text/javascript">
                function cambioControlAsistencia(){
                    if(confirm('¿Está seguro de realizar estos cambios?')){
                        return true;
                    }
                    return false;
                }
                
                
                function enviar(accion){
                    //document.getElementById('accion').value=accion;
                    document.clonarEsquemaForm.opcion.value=accion;
                                         
                    if(accion==1){                    
                     if(confirm("ATENCIÓN:\n\nRecuerde que si copia el esquema a los demás grupos, la información actual de éstos (incluyendo las calificaciones) se ELIMINARÁ, y no podrá ser recuperada.\n\n¿Está seguro de aplicar este esquema en todos los grupos de la materia <bean:write name="curso" scope="session" property="descripcionMateria" />?")){
                        
                             if(!document.clonarEsquemaForm.todos.checked){
                              document.clonarEsquemaForm.opcion.value=3;
                             }
                            document.clonarEsquemaForm.submit();
                    }
                    }else{
                      if(confirm("Recuerde que si reinicia el esquema, perderá toda la información que ha registrado hasta el momento y los debe volver a ingresar manualmente cuando cree el esquema.\n\nSi aún no tiene una copia física de estos datos, por favor dé clic en \"Descargar formato Excel\" antes de reiniciar el esquema.\n\n¿Está seguro que desea reiniciar el esquema del curso?")){
                       
                           
                                document.clonarEsquemaForm.submit();                        
                    }                       
                        
                    }
                }
                
                
               function ocultar(){
               
               division=document.getElementById('grupos');
               check=document.getElementById('');
               if(document.clonarEsquemaForm.todos.checked){
               division.style.display='none';
               }else{
                   division.style.display='';
               }
               
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
         
            <!--ffceballos -->
             <jsp:useBean class="co.edu.icesi.notas.Curso"
                                     id="curso" scope="session" />  
             
            <!-- fin ffceballos -->
            
           
            
            
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
                            <c:if test="${RJEFDEPRE || RSECGEN }">
                                | <html:link styleClass="links" href="/notasparciales/jefes"
                                           title="Seguimiento a cursos">
                                    <bean:message key="segcursos.enlace" />
                                </html:link>
                            </c:if>
                                
                                
                                <!--ffceballos -->
                                 <c:if test="${RCOORDINMAT || RSECREMAT}">
                                | <html:link styleClass="links" href="/notasparciales/coordinadores/index.jsp"
                                           title="Coordinar materias">
                                    Coordinar materias
                                </html:link>
                            </c:if>
                             <!--fin ffceballos --> 
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
                        <div align="center" id="contn">
                           
                            <html:link styleClass="links" action="/volverInicio"
                                       title="Regresa al listado de los cursos">
                                <bean:message key="listado" />
                            </html:link>
                            <%--ffceballos    			|
                            <html:link styleClass="links" action="/aEsquemaBasico"
                                       title="Sección de configuración del curso">
                                <bean:message key="esquema" />
                            </html:link>
                            --%>            
                                            <!--ffceballos -->    
                                                <%     if(curso.isDicta()){   %>
                                                <!-- fin ffceballos -->
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
                                       action="/xlsBasico?fw=esquemaBasico"
                                       title="Envía a su correo, una copia de las notas registradas, en formato Excel">
                                <bean:message key="excel" />
                            </html:link>
                                    <!--ffceballos -->                    
                                       <% } %>	
                                       <!-- fin ffceballos -->
                            <br>
                            <%
                                        boolean cerrado = ((String) session.getAttribute("cerrado")).equals("S");
                                        String enlace = "/regresarPorcentajes?tipo="
                                                + ((Curso) session.getAttribute("curso")).getTipoConfiguracion();
                                        if (!cerrado) {
                            %>

                            <%--<html:link styleClass="titulo2" action="/copiarEsquema"  title="Permite copiar la configuración del curso inmediatamente anterior"><bean:message key="copiar.esquema"/></html:link>--%>
                            
                            <%--<c:if test="${RCOORDINMAT}">--%>
                            <html:link styleClass="links" action="/regresarIngreso"
                                       title="Permite agregar o eliminar las actividades del curso.">
                                <bean:message key="modificar.actividades" />
                            </html:link>
							|
                            <html:link styleClass="links" action='<%=enlace%>'
                                       title="Permite modificar los porcentajes establecidos para las actividades del curso.">
                                <bean:message key="modificar.porcentajes" />
                            </html:link>
							|
                            <a href="#opciones" class="links"
                               title="Configure algunas opciones del curso."><bean:message
                                    key="opciones" /> </a>
                               
                               <%--</c:if>--%>
                                <%
                                            }
                                %>
                                <logic:equal value="S" name="curso" property="controlAsistencia">
                                    <%
                                                if (!cerrado) {
                                    %>|<%                                                                            }
                                %>
                                
                                               <!--ffceballos -->    
                                                <%     if(curso.isDicta()){   %>
                                                <!-- fin ffceballos -->
                                
                                <html:link styleClass="links" action="/registroAsistencias"
                                           title="Sección de registro de asistencias">
                                    <bean:message key="registro.asistencias" />
                                </html:link> |
                                <html:link styleClass="links" action="/aConsolidadoAsistencias"
                                           title="Muestra el porcentaje total de asistencia de cada estudiante a las clases del semestre.">
                                    <bean:message key="consolidado.asistencias" />
                                </html:link>
                                
                                                 <!--ffceballos -->    
                                                <%    }  %>
                                                <!-- fin ffceballos -->
                                
                            </logic:equal>
                            <br>
                            <%
                                        if (cerrado) {
                            %>
                            <br />
                            <a class="linksExtra"
                               title="Reporte con el resumen de todas las notas definitivas de los estudiantes"
                               href="http://www.icesi.edu.co/portal/pls/portal/psiaepre.ppregen_lista_clase?pMateria=<c:out value="${curso.codigoMateria}"/>&pCedulaProf=<c:out value="${profesores.profesor.cedula}"/>"
                               target="_blank">Reporte de notas definitivas</a>
                            <br />
                            <%                                                                    }
                            %>
                        </div>

                        <br>
                        <br>

                        <table cellpadding="4" align="center">
                            <tr>
                                <td align="center">
                                    <h2 class="curso">
                                        <bean:write name="curso" scope="session"
                                                    property="descripcionMateria" />
                                        <br>
								<!--ffceballos   BORRAR!!!! -->  
                                                                <%     if(curso.isDicta()){   %>
                                                                <!--fin ffceballos -->  
                                                                        Grupo:
                                        <bean:write name="curso" scope="session" property="grupo" />
                                    <!--ffceballos  -->  
                                     <% } %>
                                    <!--fin ffceballos -->  
                                       
                                  <!--ffceballos   BORRAR!!!! -->      
                                   <%     if(curso.isDicta()){
                                        %>
                                       
                                        <br/><br/> <!--Si lo dicta-->
                                       
                                  <%       } else { %>
                                  <br/><br/> <!--No lo dicta -->
                                  <% }
                                   %>
                                    <!--fin ffceballos -->
                                        
                                        
                                        <br>
                                        <br>
                                        <bean:message key="esquema" />
                                    </h2>

                                </td>
                            </tr>
                            <tr>
                                <td align="center">
                                    <%
                                                boolean errores = (request.getAttribute("error") != null);
                                                if (cerrado || errores) {
                                    %>
                                    <FIELDSET class="fieldset">
                                        <%
                                                                                            if (cerrado) {
                                        %>
                                        <div class="error" align="left">
                                            <bean:message key="advertencia.cerrado" />
                                        </div>
                                        <%                                                                                                                                                                            }
                                        %>
                                        <div class="error" align="left">
                                            <html:errors property="copiarEsquema" />
                                        </div>
                                        <div class="error" align="left">
                                            <html:errors property="esquemaAnterior" />
                                        </div>
                                        <div class="error" align="left">
                                            <html:errors property="noCerrarCurso" />
                                        </div>
                                        <%
                                                                                            if (errores) {
                                        %>
                                        <div class="error" align="left">
                                            <html:errors property="mensaje" />
                                        </div>
                                        <div class="error" align="left">
                                            <html:errors property="errorNota" />
                                        </div>
                                        <%                                                                                                                                                                            }
                                        %>
                                    </FIELDSET>
                                    <%
                                                }
                                    %><br />
                                </td>
                            </tr>
                            <tr>
                                <TD>
                                    <icesi:resumen />
                                    <br>
                                </TD>
                            </tr>
                            
                            
                            <!--ffceballos -->
                              <html:form action="/clonarEsquema"> 
                                  
                                  <html:hidden property="opcion" value="0" />
                                  
                            <c:if test="${RCOORDINMAT || RSECREMAT}">
                            
                            <tr> 
                                <td>   
                           
                            
                                <div align="center">
                                            
                                    <% ArrayList grupos =(ArrayList) session.getAttribute("grupos");  %>
                                    
                                    Copiar esquema a todos los grupos <input type="checkbox" name="todos" value="todos" checked onclick="ocultar();">
                                  <br/>
                                  <div id="grupos" style="display:none;">
                                  <% 
                                            out.print("<table>");
                                    for(int cont=0;cont<grupos.size();cont++){                                            
                                             out.print("<tr> <td> Grupo "+grupos.get(cont)+"</td> <td><input type='checkbox' name='grupo"+grupos.get(cont)+"' value='"+grupos.get(cont)+"'</td> </tr> ");
                                            }
                                            out.print("</table>");
                                  %>
                                       
                                   <br/>
                                  </div>
                                  
                                     <br/>
                                   <html:button onclick="enviar(1)" value="Copiar Esquema" property="boton" styleClass="info" />                                
                                 <br/>
                                </div>  
                             
                                </td>
                            </tr>        
                            </c:if>
                             
                            <!--fin ffceballos -->
                            
                             <!--ffceballos 27-09-2013  -->
                            <tr> 
                                <td> 
                              
                                <div align="center">
                                    <br/>
                                 <html:button onclick="enviar(2)" value="Reiniciar esquema" property="boton" styleClass="info" />                                
                                </div>  
                              </html:form>
                             </td>
                            </tr>  
                             <!--fin ffceballos -->
                        </table>

                        <br>

                        <center>
                            <a name="opciones"></a>
                            <html:form action="/controlAsistencia"
                                       onsubmit="cambioControlAsistencia()">
                                <table class="tabla" cellpadding="4" cellspacing="1">

                                    <tr>
                                        <td colspan="2" class="celda2">
                                            <bean:message key="opciones" />
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="center">
                                            <div class="titulo2">
                                                <bean:message key="control.asistencia" />
                                            </div>
                                        </td>
                                        <td align="center">
                                            <html:select name="curso" property="controlAsistencia"
                                            disabled='<%=(session.getAttribute("cerrado").equals("N")) ? false : true%>'>
                                                <html:option value="S">Sí</html:option>
                                                <html:option value="N">No</html:option>
                                            </html:select>
                                        </td>
                                    </tr>

                                    
                                    <!-- ffceballos -->
                                                                                                     

                                <% String Cod_departamento =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                
                               <% if(Cod_departamento.equalsIgnoreCase("08")){  %>
                               
                                    <tr> 
                                        <td align="left">    
                                            <div class="titulo2">
                                            - Regla Matematicas  
                                             
                                             
                                             
                                               <% String regla = ((Curso) session.getAttribute("curso")).getControlReglaMate();
                                                   String reglaQ= ((Curso) session.getAttribute("curso")).getControlReglaQuiz();
                                               
                                               String isCerrado=(String)session.getAttribute("cerrado");
                                               
                                               %>
                                        
                                           
                                            </div>
                                        </td>
                                        <td align="center">  
                                            <select id="reglaMatematicas" name="reglaMatematicas" <% if(isCerrado.equals("S")){%>  disabled   <%} %> >
                                                <option value="S" <% if(regla.equals("S")){ %> selected='true'  <% } %> > Si</option>
                                                <option value="N" <% if(regla.equals("N")){ %> selected='true'  <% } %>> No</option>
                                            </select>  
                                        </td>
                                    </tr>
                                    <c:if test="${RCOORDINMAT || RSECREMAT}">
                                    <tr> 
                                        <td align="left"> 
                                            <div class="titulo2">
                                            - Regla Quiz mas bajo
                                            </div>
                                        </td> 
                                        
                                        
                                        <td align="center">
                                        <select id="reglaQuiz" name="reglaQuiz" <% if(isCerrado.equals("S")){%>  disabled   <%} %> >
                                                <option value="S" <% if(reglaQ.equals("S")){ %> selected='true'  <% } %> > Si</option>
                                                <option value="N" <% if(reglaQ.equals("N")){ %> selected='true'  <% } %>> No</option>
                                            </select>  
                                        </td>
                                       
                                    </tr>
                                     </c:if>
                                    
                                    <% }  %>  
                                    <!-- ffceballos -->
                                    
                                    
                                    
                                    
                                    <tr>
                                        <td>
                                            <div class="titulo2">
                                                <bean:message key="escala" />
                                            </div>
                                        </td>
                                        <td>
                                            <html:text name="curso" property="escala" size="4"
                                                       maxlength="4"
                                            disabled='<%=(session.getAttribute("cerrado").equals("N")) ? false : true%>' />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" align="center">
                                            <html:submit value="Modificar"
                                            disabled='<%=(session.getAttribute("cerrado").equals("N")) ? false : true%>' />
                                        </td>
                                    </tr>
                                </table>
                            </html:form>
                            <%-- <html:link styleClass="titulo2" action="/volverInicio"><bean:message key="listado"/></html:link> --%>

                        </center>
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