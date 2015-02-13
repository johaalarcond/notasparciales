<%@ page language="java" errorPage="/profesores/error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<%@ page import="co.edu.icesi.notas.*;"%>


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
                enableTooltips("contn")
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
    </head>

    <body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
          topmargin="0" marginwidth="0" marginheight="0">
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
                    <div align="center"></div>
                    <div align="center"></div>
                    <div align="center"></div>
                    <div align="center"></div>
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
                        <html:link styleClass="links" action="/volverInicio"
                                   title="Regresa al listado de los cursos">
                            <bean:message key="listado" />
                        </html:link>
						|
                        <html:link styleClass="links" action="/aEsquema"
                                   title="Sección de configuración del curso">
                            <bean:message key="esquema" />
                        </html:link>
						|
                        <html:link styleClass="links" action="/aRegistro"
                                   title="Sección del registro de notas">
                            <bean:message key="registro" />
                        </html:link>
						|
                        <html:link styleClass="links" action="/definitivas"
                                   title="Muestra las notas definitivas">
                            <bean:message key="definitivas" />
                        </html:link>
						|
                        <html:link styleClass="links" action="/archivoXLS?fw=registro"
                                   title="Envía a su correo, una copia de las notas registradas, en formato Excel">
                            <bean:message key="excel" />
                        </html:link>
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
                                    boolean cerrado = session.getAttribute("cerrado").equals("S");
                                    double sumaG = cur.sumaGrupal();
                                    boolean sumas = ((sumaG > 0 && Math.round(sumaG) != 100) || Math.round(cur.sumaIndividual()) != 100);
                                    boolean errores = (request.getAttribute("error") != null);
                                    if (cerrado || sumas || errores) {
                        %>
                        <br>
                        <FIELDSET class="fieldset">
                            <%
                                                                                        if (cerrado) {
                            %>
                            <div class="error" align="left">
                                <bean:message key="advertencia.cerrado" />
                            </div>
                            <%                                                                                                                    }

                                                                                        if (sumas) {
                            %>
                            <div class="error" align="left">
                                <bean:message key="advertencia.suma" />
                            </div>
                            <br>
                            <%                                                                                                                    }
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
                            <%                                                                                                                    }
                            %>
                        </FIELDSET>
                        <%
                                    }
                        %>
                    </div>
                    <br />
                    <icesi:registro padre="resumen" estructura="calificacion" />
                    <br>
                    <center>
                        <html:link action="/definitivas" styleClass="titulo2">Definitivas</html:link>
                        <html:link action="/volverInicio" styleClass="titulo2">
                            <bean:message key="listado" />
                        </html:link>
                    </center>
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

                    <!-- InstanceEndEditable -->
                </td>
            </tr>
            <tr>
                <td>
                    <font color="#FFFFFF">h</font>
                </td>
            </tr>
            <tr>
                <td bgcolor="#CFCCC7" class="piePagina">
                    <div align="center">
                        <font color="#FFFFFF" size="1"
                              face="Verdana, Arial, Helvetica, sans-serif"><strong><a
                                    href="http://www.icesi.edu.co/" target="_blank"><font
                                        color="#666666">Icesi</font> </a> |<a
                                    href="http://www.icesi.edu.co/estudiantes/" target="_blank">
                                    <font color="#666666">Estudiantes</font> <font color="#666666">-</font>
                                    <font color="#666666">Pregrado</font> </a>| <a
                                    href="http://www.icesi.edu.co/es/egresados/index.php"
                                    target="_blank"><font color="#666666">Egresados</font> </a> | <a
                                    href="http://www.icesi.edu.co/colegios/" target="_blank"><font
                                        color="#666666">Colegios</font> </a> | <a
                                    href="http://biblioteca.icesi.edu.co/" target="_blank"><font
                                        color="#666666">Biblioteca</font> </a> | <a
                                    href="http://www.icesi.edu.co/estudiante-post" target="_blank"><font
                                        color="#666666">Estudiantes - Postgrado</font> </a> </strong> </font>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="piePagina">
                    <div align="center">
                        <table width="65%" border="0" align="center" cellpadding="3"
                               cellspacing="0">
                            <tr>
                                <td>
                                    <div align="center">
                                        <strong><font color="#666666" size="1"
                                                      face="Verdana, Arial, Helvetica, sans-serif">Universidad
												Icesi, Cali - Colombia</font> </strong><font size="1"> <font
                                                color="#666666" face="Verdana, Arial, Helvetica, sans-serif"><strong>|
													Calle 18 No. 122-135 |Tel&eacute;fono 555 23 34 | Fax 555
													14 41</strong> </font> </font>
                                        <font size="1"><br> <font color="#666666"
                                                                  face="Verdana, Arial, Helvetica, sans-serif"><strong>Comentarios,
													preguntas o sugerencias, aqu&iacute;</strong> </font> </font>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
        <jsp:include page="../template/footer.jsp"></jsp:include>
    </body>
</html>
