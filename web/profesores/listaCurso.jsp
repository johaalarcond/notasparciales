<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="core"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page import="java.util.*,co.edu.icesi.notas.*;"%>

<logic:present scope="session" name="activa">
    <html>
        <head>
            <title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
            <meta http-equiv="Content-Type"
                  content="text/html; charset=iso-8859-1">
            <script>
                function opmas(form, opcion){
                    if(opcion==4){
                        //if(confirm('¿Está seguro de que desea cerrar el curso? \n Si lo hace no podrá modificar ninguna información posteriormente')){
                        form.opcion.value=opcion;
                        form.submit();
                        return true;
                        //}else{
                        //return false;
                        //}
                    }
                    form.opcion.value=opcion;
                    form.submit();
                    return true;
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
                    <td>
                        <div id="menu_seguimientos" align="center">
                            <!--ffceballos 22/04/2014: se agregó el rol RSEGUIMIENTO -->
                            <c:if test="${RJEFDEPRE || RDIRPROG || RSECGEN || RSEGUIMIENTO}">
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
                        <jsp:useBean class="co.edu.icesi.notas.control.ControlProfesores"
                                     id="profesores" scope="session" />
                        <html:form action="/listaCursos">
                            <html:hidden property="opcion" value="1" />
                            <table align="center">
                                <tr>
                                    <td>
                                        &nbsp;&nbsp;
                                    </td>
                                    <td class="info" align="right">
                                        <!--<a href="<%=request.getContextPath()%>/cerrarSesion.jsp"
                                           class="links"> <bean:message key="cerrar.sesion" /> </a>
                                        <br />
                                        <br />-->
                                        <bean:message key="periodo.academico" /> 
                                        <strong><jsp:getProperty name="profesores"
                                                         property="periodoAcademico" /></strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="celda2">
                                        <bean:message key="listado" />
                                        <br>
                                        <br>
                                    </td>
                                </tr>

                                <%
                                            ArrayList cursos = profesores.getProfesor().getCursos();
                                            if (!cursos.isEmpty()) {
                                                for (int i = 0; i < cursos.size(); i++) {
                                                    Curso curso = (Curso) cursos.get(i);
                                                    
                                                  if(curso.isDicta()){
                                                  
                                                   
                                %>
                                <tr>
                                    <td>
                                        &nbsp;&nbsp;
                                    </td>
                                    <td class="info">
                                        <input type="radio" id="curso" name="curso"
                                               value="<%=curso.getId()%>"
                                        <%if (i == 0) {
                                                                                                out.println("checked=\"checked\"");
                                                                                            }%>><%=curso.getNombre()%>.
										Grupo:
                                        <%=curso.getGrupo()%>.   
                                    </td>
                                </tr>
                                <%
                                                  }      
                                                
                                                }
                                                                                java.util.Date fechaLimite = (java.util.Date) session.getAttribute("fechaLimite");
                                                                                java.util.Date fechaInicio = (java.util.Date) session.getAttribute("fechaInicio");
                                                                                java.util.Date hoy = new Date();
                                                                                boolean antes = hoy.after(fechaLimite);
                                                                                boolean despues = hoy.before(fechaInicio);
                                                                                boolean disable = antes || despues;
                                %>

                                <tr>
                                    <td colspan="2" align="center">
                                        <br>
                                        <html:submit value="Esquema de evaluación" styleClass="info" />
                                        &nbsp;
                                        <html:button onclick="opmas(listaCursosForm, 2)"
                                                     value="Registrar notas" property="boton" styleClass="info" />
                                        <%-- <html:button  onclick="opmas(listaCursosForm, 5)" value="Registrar asistencia" property="boton" styleClass="info"/> ---%>
                                        <html:button onclick="opmas(listaCursosForm, 3)"
                                                     value="Notas Definitivas" property="boton" styleClass="info" />
                                        <%--<html:button  onclick="opmas(listaCursosForm, 4)" value="Cerrar curso" property="boton" styleClass="info" disabled="<%=disable%>"></html:button>--%>
                                    </td>
                                </tr>
                                <%
                                                                            } else {
                                %>
                                <tr>
                                    <td>
                                        &nbsp;&nbsp;
                                    </td>
                                    <td class="info">
                                        <strong>Usted no posee cursos este periodo.</strong>
                                    </td>
                                </tr>
                                <%                                                                            }
                                %>
                            </table>
                        </html:form>
                        <br />
                    </td>
                </tr>
                <tr>
                    <td>
                        <noscript>
                            <div class="error">
                                <center>
                                    <strong>Su navegador no está soportando o no tiene
										activo javascript.<br />Por favor active javascript en su
										explorador para asegurar el correcto funcionamiento de la
										aplicación</strong>
                                </center>
                            </div>
                        </noscript>
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