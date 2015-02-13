<%@page import="co.edu.icesi.notas.*,javax.sql.*,java.util.*,co.edu.icesi.notas.form.CalificacionForm" %>
<%@page import="co.edu.icesi.notas.form.*, java.text.SimpleDateFormat" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
    <%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
    <%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
    <%@taglib uri="http://jakarta.apache.org/struts/tags-logic"
              prefix="logic"%>
    <%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
    
    
    <logic:present scope="session" name="activa">
        <html>
            <head>
                <title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
                <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

                <link href="<%=request.getContextPath()%>/css/estilos.css"
                      rel="stylesheet" type="text/css">
                <script language="javascript" type="text/javascript">
                    var c =
                    <%=session.getMaxInactiveInterval()%>
                        ;
                        function cs() {
                            c = c - 1;
                            if (c == 0) {
                                document.location = "/notasparciales/sesionInactiva.icesi";
                            }
                        }

                        function cerrarSesion() {
                            setInterval("cs()", 1000);
                        }
                </script>
            </head>

            <body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
                  topmargin="0" marginwidth="0" marginheight="0" onload="cerrarSesion()">
                <script language="JavaScript">
                    function cambiarTipoSubmit(opcion) {
                        document.calificacionForm.tipoSubmit.value = opcion;
                    }
                </script>
                <jsp:include page="../template/header.jsp"></jsp:include>
                <table border="0" align="center" cellpadding="00"
                       cellspacing="0">
                    <tr>
                        <td height="18" nowrap class="info" align="center">
                            <div align="center" class="error"><br />
			Recuerde que esta sesión tiene un tiempo de inactividad máximo
			permitido de 8 minutos. <br />
			Pasado este tiempo su sesión será terminada automáticamente. <br />
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
                            <div align="center" id="contn"><html:link styleClass="links"
                                       action="/volverInicio" title="Regresa al listado de los cursos">
                                    <bean:message key="listado" />
                                </html:link> | <html:link styleClass="links" action="/aEsquemaBasico"
                                                          title="Sección de configuración del curso">
                                    <bean:message key="esquema" />
                                </html:link> | <html:link styleClass="links" action="/aRegistroBasico"
                                                          title="Sección del registro de notas">
                                    <bean:message key="registro" />
                                </html:link> | <html:link styleClass="links" action="/aDefinitivasBasico"
                                                          title="Muestra las notas definitivas">
                                    <bean:message key="definitivas" />
                                </html:link> | <html:link styleClass="links"
                                                          action="/xlsBasico?fw=definitivasBasico"
                                                          title="Envía a su correo, una copia de las notas registradas, en formato Excel">
                                    <bean:message key="excel" />
                                </html:link> <logic:equal value="S" name="curso" property="controlAsistencia">
                                    <br />
                                    <html:link styleClass="links" action="/registroAsistencias"
                                               title="Sección de registro de asistencias">
                                        <bean:message key="registro.asistencias" />
                                    </html:link> |
                                    <html:link styleClass="links" action="/aConsolidadoAsistencias"
                                               title="Muestra el porcentaje total de asistencia de cada estudiante a las clases del semestre.">
                                        <bean:message key="consolidado.asistencias" />
                                    </html:link>
                                </logic:equal></div>
                            <br>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div align="center">    
                                
                               
                                        </div>
                            <div align="center"></div>
                            <div align="center"></div>
                            <div align="center"></div>
                            <p class="info" align="right">
                                <!--<a
                                    href="<%=request.getContextPath()%>/cerrarSesion.jsp" class="links">
                                    <bean:message key="cerrar.sesion" /> </a> <br />
                                <br />-->
                                <bean:message key="periodo.academico" /> <strong><bean:write
                                        name="curso" property="periodoAcademico" /> </strong></p>
                            <div class="curso" align="center"><bean:write name="curso"
                                        scope="session" property="descripcionMateria" /> <br>
			Grupo: <bean:write name="curso" scope="session" property="grupo" />
                        
               <%    //ffceballos 23/10/2013
                      List actividades=((Categoria) session.getAttribute("padreActual")).getActividades();
                      int consecutivoTipoCateg=(((Categoria) session.getAttribute("padreActual")).getTipoCategoria()).getConsecutivo();
                        String noEditables="";
                       String departamento=((Curso)session.getAttribute("curso")).getDepartamento();
                        
                       int parciales=Integer.parseInt((String)session.getAttribute("codigoParciales"));
                       int finales=Integer.parseInt((String)session.getAttribute("codigoFinales"));
                            
                           //out.println("<br>(borrar)Categoría: "+consecutivoCateg);
                        if(departamento.equals("08")&&(consecutivoTipoCateg==parciales || consecutivoTipoCateg==finales )){ //falta validar que sea la categoría indicada!!

//***************************************************************************************************************************************************
                         for(int contador=0;contador<actividades.size();contador++){
                            Actividad actividad=(Actividad)actividades.get(contador);
                            Date fecha=    actividad.getFechaIngreso();
                               
                            if(fecha!=null){
                              //  out.println("<br>  "+actividad.getConsecutivo()+":"+fecha.toString());
                                //obtener fecha actual y comparar que lleve al menos 15 dias de diferencia 
                                Date fechaActual=new Date();
                                SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
                              //  out.println("  ****  actual: "+formateador.format(fechaActual));

                                Calendar calendarFecha1=new GregorianCalendar();
                                Calendar calendarFecha2=new GregorianCalendar();    

                                calendarFecha1.setTimeInMillis(fecha.getTime()); 
                                calendarFecha2.setTimeInMillis(fechaActual.getTime());     
                                
                                calendarFecha1.add(Calendar.DATE, 15);
                                //si fecha + 15 es mayor aa la actual ==> agregar    
                               // if(fecha.before(fechaActual)){
                                 if(calendarFecha1.getTimeInMillis()<calendarFecha2.getTimeInMillis()){
                                // out.println("entró");
                                noEditables+="-"+actividad.getConsecutivo()+"-";
                                }
                            
                                }
                            }   

                            }
                                //fin ffceballos
                        %>
                                <br>
                            </div>
                            <div class="curso" align="center"><bean:write
                                    name="padreActual" property="nombre" /></div>
                            <br />
                            <div align="center" class="resaltado"><bean:message
                                    key="mensaje" /> <br />
                                <bean:message key="mensaje.notas" /> <br />
                                <bean:message key="mensaje.notas.campo" /></div>
                            <br>
                        </td>
                    </tr>

                    <%
                                if (request.getAttribute("error") != null
                                        || session.getAttribute("cerrado").equals("S")) {
                    %>
                    <tr>
                        <td align="center">
                            <fieldset class="fieldset">
                                <div class="error" align="left"><html:errors property="boton" />
                                    <%
                                                                        if (session.getAttribute("cerrado").equals("S")) {
                                    %> <bean:message key="advertencia.cerrado" /> <%                                                    }
                                    %>
                                </div>
                            </fieldset>
                            <br />
                        </td>
                    </tr>
                    <%
                                }
                    %>
                    <tr>
                        <td><html:form action="/guardarNotas">
                                <html:hidden property="tipoSubmit" />
                                <html:hidden property="category" />
                                <html:hidden property="escala" />
                                <table border="1" cellspacing="0" align="center">
                                    <tr>
                                        <td class="celda2" align="center"><bean:message key="codigo" />
                                        </td>
                                        <td class="celda2" align="center"><bean:message key="nombre" />
                                        </td>
                                        <%--Recorremos las actividades para colocar su nombre y su porcentaje. --%>

                                        <logic:iterate name="subject" id="activity" property="activities"
                                                       indexId="index">
                                            <logic:equal
                                            value='<%=((Categoria) session.getAttribute("padreActual")).getNombre()%>'
                                                name="activity" property="category">
                                                <td class="celda2" align="center"> <bean:write
                                                        name="activity" property="name" /> <br>
                                                    <bean:write name="activity" property="percentage" /> %</td>
                                                </logic:equal>
                                            </logic:iterate>
                                    </tr>
                                    <%
                                                int tamanno = ((ArrayList) session.getAttribute("tamannoM")).size();
                                    %>
                                    <logic:iterate name="calificacionForm" id="calificacion"
                                                   property="matriculas" indexId="index"
                                                   type="co.edu.icesi.notas.form.Calificacion">
                                        <tr> 
                                            <logic:equal value='<%="A"%>' name="calificacion"
                                                         property="estado">
                                                <td class="celda"><bean:write name="calificacion"
                                                            property="codigo" /> <html:hidden name="calificacion"
                                                            property="codigo" indexed="true" /></td>
                                                <td class="celda"><bean:write name="calificacion"
                                                            property="nombre" /> <html:hidden name="calificacion"
                                                            property="nombre" indexed="true" /> <html:hidden
                                                            name="calificacion" property="estado" indexed="true" /> <html:hidden
                                                            name="calificacion" property="escala" indexed="true" /></td>
                                                
                                                  
                                                
                                                    <logic:iterate name="calificacion" id="nota" property="notas"
                                                                   indexId="index2" type="co.edu.icesi.notas.form.Nota">
                                                    <td class="celda" align="left"> <logic:equal value="true"
                                                        property='<%="calificacion[" + index
                                                                                + "].notaIndexada[" + index2
                                                                                + "].error"%>'
                                                                     name="calificacionForm">
                                                            <html:text
                                                            tabindex='<%=(((index2.intValue() + 1) * tamanno) + index.intValue()) + ""%>'
                                                                name="calificacionForm"
                                                            property='<%="calificacion["
                                                                                    + index.intValue()
                                                                                    + "].notaIndexada["
                                                                                    + index2.intValue() + "].nota"%>'
                                                                size="3"
                                                            disabled='<%=(session.getAttribute("cerrado").equals("N"))
                                                                                    ? false
                                                                                    : true%>'
                                                                styleClass="campoError" onfocus="this.select()" />
                                                            <html:errors
                                                            property='<%="calificacion[" + index
                                                                                    + "].notaIndexada[" + index2
                                                                                    + "].nota"%>' />
                                                        </logic:equal> <logic:notEqual value="true"
                                                        property='<%="calificacion[" + index
                                                                                + "].notaIndexada[" + index2
                                                                                + "].error"%>'
                                                                                       name="calificacionForm">
                                                            
                                                              <% //ffceballos 22/10/2013

      
                                                                
                     int consecutivoTipo=((TipoCategoria)(((Categoria) session.getAttribute("padreActual")).getTipoCategoria())).getConsecutivo();
                    

                                                    
                                                   boolean desactivar=false;
                                                    
                                                
                                                   

                                                    //usar consecutivo en vez de nota para saber el codigo de la actividad a la que pertenece la calificación
                                                        ArrayList array=calificacion.getNotas();
                                                        Nota notaActividad=(Nota)array.get(index2.intValue());
                                                        int codi=notaActividad.getConsecutivo();
                                                      // out.println("***"+codi);
                                                          //if(noEditables.contains(""+codi)){
                                                            if(noEditables.indexOf(""+codi)!= -1){
                                                           // out.println("No editable! ");
                                                               desactivar=true;
                                                            }  
                                                    %>
                                                    
                                                   
                                                 <%--       borrar <bean:write name="calificacionForm" property='<%="calificacion["+index.intValue()+"].notaIndexada["+index2.intValue()+"].consecutivo"%>' />
                                                 fin ffceballos   --%>
                                                    
                                                                                                    
                                                           <html:text
                                                            tabindex='<%=(((index2.intValue() + 1) * tamanno) + index.intValue())
                                                                                    + ""%>'
                                                                name="calificacionForm"
                                                            property='<%="calificacion["
                                                                                    + index.intValue()
                                                                                    + "].notaIndexada["
                                                                                    + index2.intValue()
                                                                                    + "].nota"%>'
                                                                size="3"
                                                            disabled='<%=((session.getAttribute("cerrado").equals("N")) )
                                                                                    ? false
                                                                                    : true%>'
                                                               readonly='<%=(!(desactivar))
                                                                                    ? false
                                                                                    : true%>'
                                                                   
                                                                onfocus="this.select()" />
                                                        </logic:notEqual> <html:hidden name="calificacionForm"
                                                        property='<%="calificacion["
                                                                                + index + "].notaIndexada["
                                                                                + index2
                                                                                + "].nombreEstructura"%>' />
                                                        <html:hidden name="calificacionForm"
                                                        property='<%="calificacion["
                                                                                + index
                                                                                + "].notaIndexada["
                                                                                + index2
                                                                                + "].consecutivo"%>' />
                                                        <html:hidden name="calificacionForm"
                                                        property='<%="calificacion["
                                                                                + index
                                                                                + "].notaIndexada["
                                                                                + index2
                                                                                + "].escala"%>' />
                                                    </td>
                                                </logic:iterate>
                                            </logic:equal>
                                        </tr>
                                    </logic:iterate>

                                    <tr>
                                        <%-- Con las siguientes instrucciones se muestran los promedios de cada parcial, quiz, etc. --%>
                                        <td class="resaltado" colspan="2" align="right"><bean:message
                                                key="promedio" /></td>
                                            <logic:iterate name="padreActual" id="estructura"
                                                           property="actividades" indexId="index">
                                            <td class="celda2" align="center"><%=((Actividad) estructura).getPromedio()%>
                                                <%--<bean:write name="estructura" property="promedio"/>--%> <br>
                                            </td>
                                        </logic:iterate>
                                    </tr>
                                    <tr>
                                        <%-- La siguiente expresión es para mostrar bien organizados los botones de Aceptar y Cancelar en la tabla. --%>
                                        <td
                                            colspan='<%=((Categoria) session.getAttribute("padreActual")).getActividades().size() + 2%>'
                                            align="center" class="celda"><html:submit value="Guardar" disabled='<%=(session.getAttribute("cerrado").equals("N")) ? false : true%>' />
                                            <html:cancel value="Cancelar" /> <%-- <html:button value="Recalcular notas" property="boton" onclick="cambiarTipoSubmit('recalcular')" disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>'/> --%>
                                        </td>
                                    </tr>
                                </table>
                            </html:form> <br />
                            <div align="center" class="resaltado"><bean:message
                                    key="mensaje" /> <br />
                            </div>
                            <div align="center" class="resaltado">  <span style="color: #c00; font-size: 25px;" > Recuerde que si un estudiante obtuvo nota de cero, es necesario colocar decimales (0.0) para evitar conflictos en el sistema.  </span>  <br />
                            </div>
                            <br>
                        </td>
                    </tr>
                </table>
                <jsp:include page="../template/footer.jsp"></jsp:include>
            </body>
            <logic:present name="notasGuardadas">
                <logic:equal name="notasGuardadas" value="S">
                    <script language="JavaScript">
                        alert('Su registro de notas ha sido almacenado.');
                    </script>
                </logic:equal>
            </logic:present>
        </html>
    </logic:present>
    <logic:notPresent scope="session" name="activa">
        <jsp:forward page="/notasparciales/sesionInactiva.icesi"></jsp:forward>
    </logic:notPresent>