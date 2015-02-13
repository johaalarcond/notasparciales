<%@ page language="java" errorPage="/profesores/error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@ page import="co.edu.icesi.notas.*;"%>

<logic:present scope="session" name="activa">

    <html>
        <head>
            <title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
            <meta http-equiv="Content-Type"
                  content="text/html; charset=iso-8859-1">
            <meta http-equiv="pragma" content="no-cache">
            <meta http-equiv="cache-control" content="no-cache">
            <meta http-equiv="expires" content="0">

            <script type="text/javascript" src="BubbleTooltips.js"></script>
            <script type="text/javascript">
                window.onload = function() {
                    enableTooltips("contn");
                };
            </script>
            <link href="<%=request.getContextPath()%>/css/estilos.css"
                  rel="stylesheet" type="text/css">
            <script language="JavaScript" type="text/javascript">
                function funcCerrar(valor) {
                    if (valor == 1) {
                        //if(confirm('¿Está seguro de que desea cerrar el curso? Recuerde que una vez lo cierre, no podrá realizar modificación alguna.')){
                        document.cerrarForm.opcion.value = "cerrar";
                        //document.cerrarForm.pagina.value="IrRegistro";
                        document.cerrarForm.submit();
                        //}
                    } else {
                        document.cerrarForm.opcion.value = "abrir";
                        //document.cerrarForm.pagina.value="IrRegistro";
                        document.cerrarForm.submit();
                    }
                }
            </script>
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
							|
                            <html:link styleClass="links" action="/aEsquemaBasico"
                                       title="Sección de configuración del curso">
                                <bean:message key="esquema" />
                            </html:link>
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
                                       action="/xlsBasico?fw=registroBasico"
                                       title="Envía a su correo, una copia de las notas registradas, en formato Excel">
                                <bean:message key="excel" />
                            </html:link>
                            <logic:equal value="S" name="curso" property="controlAsistencia">
                                <br />
                                <html:link styleClass="links" action="/registroAsistencias"
                                           title="Sección de registro de asistencias">
                                    <bean:message key="registro.asistencias" />
                                </html:link> |
                                <html:link styleClass="links" action="/aConsolidadoAsistencias"
                                           title="Muestra el porcentaje total de asistencia de cada estudiante a las clases del semestre.">
                                    <bean:message key="consolidado.asistencias" />
                                </html:link>
                            </logic:equal>
                        </div>
                        <br>
                        <br>

                        <div class="curso" align="center">
                            <bean:write name="curso" scope="session"
                                        property="descripcionMateria" />
                        </div>
                        <div class="curso" align="center">
							Grupo:
                            <bean:write name="curso" scope="session" property="grupo" />
                        </div>
                        <br />
                        <div class="curso" align="center">
                            <bean:message key="registro" />
                        </div>
                        <div align="center">
                            <%
                                        Curso cur = (Curso) session.getAttribute("curso");
                                        boolean cerrado = session.getAttribute("cerrado") != null
                                                && session.getAttribute("cerrado").equals("S");
                                        boolean errores = (request.getAttribute("error") != null);
                                        if (cerrado || errores) {
                            %>
                            <br>
                            <FIELDSET class="fieldset">
                                <%
                                                                            if (cerrado) {
                                %>
                                <div class="error" align="left">
                                    <bean:message key="advertencia.cerrado" />
                                </div>
                                <%                                                                                                                                            }
                                %>
                                <div class="error" align="left">
                                    <html:errors property="noCerrarCurso" />
                                </div>
                                <%
                                                                            if (errores) {
                                %>
                                <div class="error" align="left">
                                    <html:errors property="mensaje" />
                                </div>
                                <%                                                                                                                                            }
                                %>
                            </FIELDSET>
                            <%
                                        }
                            %>
                        </div>
                        <br />
                        <icesi:registroBasico />
                        <br>
                        <br>
                        <div align="center">
                            <%
                                        java.util.Date fechaLimite = (java.util.Date) session.getAttribute("fechaLimite");
                                        java.util.Date fechaInicio = (java.util.Date) session.getAttribute("fechaInicio");
                                        java.util.Date hoy = new java.util.Date();
                                        boolean antes = hoy.after(fechaLimite);
                                        boolean despues = hoy.before(fechaInicio);
                                        boolean disable = antes || despues;
                                        boolean cierra = (session.getAttribute("cerrado").equals("N"))
                                                ? false
                                                : true;
                            %>
                            <html:form action="/cerrarCurso">
                                <html:hidden property="opcion" />
                                <html:hidden property="pagina" />
                                <%--<html:button property="cerrar" value="Cerrar curso" onclick="funcCerrar(1)" disabled="<%=disable||cierra%>"></html:button>--%>
                                <%-- <html:button property="habilitar" value="Habilitar curso"  onclick="funcCerrar(2)" disabled="<%=disable||!cierra%>"></html:button>--%>
                            </html:form>
                        </div>
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
