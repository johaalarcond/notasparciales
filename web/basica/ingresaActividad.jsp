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
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%> 
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
			<SCRIPT language="javascript">
	function funcion(opt){
      modificarEsquemaForm.opcion.value=opt; 
      modificarEsquemaForm.submit();
	}
	
	function remove(index){
		if(confirm('¿Está seguro de eliminar este ítem?\n Recuerde que todas las notas de este ítem serán eliminadas permanentemente')){
			modificarEsquemaForm.opcion.value='0'
			modificarEsquemaForm.indice.value=index;
			modificarEsquemaForm.submit();
		} 
	}
	
	function habilitar(campoSelect,campoTexto){
		if(campoSelect.value=="Otra"){
			campoTexto.disabled=false;
		}else{
			campoTexto.disabled=true;
			campoTexto.value='';
		}
	}
	
	function habilitarTodos(){
			numItems =<%=((ModificarEsquemaForm) request
								.getAttribute("modificarEsquemaForm"))
								.getActivities().size()%>;
			i=0;
			while(i<numItems){
				habilitar(modificarEsquemaForm["elemento["+i+"].category"],modificarEsquemaForm["elemento["+i+"].other"]);
				i++;
			}
	}
	</SCRIPT>

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
                        
                        
                        <script> //ffceballos
                        var prueba=true;
                        </script>
            
		</head>

		<body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
			topmargin="0" marginwidth="0" marginheight="0"
			onload="cerrarSesion()" >
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
						<div class="curso" align="center">
							<bean:write name="curso" scope="session"
								property="descripcionMateria" />
							<br>
							Grupo:
							<bean:write name="curso" scope="session" property="grupo" />
						</div>
						<br>
						<div class="curso" align="center">
							<bean:message key="modificacion.actividades" />
						</div>
						<br>
						<%
							boolean hayError = request.getSession().getAttribute("errores") != null;
								if (hayError) {
						%>
						<div align="center">
							<FIELDSET class="fieldset">
								<div class="error" align="left">
									<html:errors property="nombresUnicos" />
									<html:errors property="nombresVacios" />
									<html:errors property="actividadCero" />
									<html:errors property="errorEliminar" />
									<html:errors property="categoriasRepetidas" />
									<html:errors property="categoriaClasificacion" />
									<html:errors property="nombresRepetidos" />
									<html:errors property="errorCantIndGrupal" />
                                                                        <html:errors property="nombresOtrasVacios"/>
                                                                        <html:errors property="categoriaCero"/>
								</div>
							</FIELDSET>
							<br />
						</div>
						<%
							}
						%>

                                                
                                                <%
                                                //ffceballos prueba
                                                  ArrayList categoriasActivas=((Curso)session.getAttribute("curso")).getCategorias();
                                                  ArrayList categoriasValidas=new ArrayList();
                                                                                                     
                                                                                                     for(int a=0;a<categoriasActivas.size();a++){
                                                                                                         Categoria cate=((Categoria)categoriasActivas.get(a));
                                                                                                         if(cate.getActividades().size()>=1){
                                                                                                             categoriasValidas.add(cate);
                                                                                                         
                                                                                                     } }
                                                  //fin ffceballos prueba
                                                                                                     %>
                                                
						<html:form action="/modificarEsquema">
							<table border="1" cellspacing="1" align="center" cellpadding="2">
								<tr>
									<td class="celda2">
										<bean:message key="nombre" />
									</td>
									<td class="celda2">
										<bean:message key="categorias" />
									</td>
                                                                        
                                                                         
                                                                        <!-- ffceballos -->
                                                                        <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                            <c:set var="a" value="<%=enlace %>"/>
                                                                           <%--  <% if(!enlace.equalsIgnoreCase("08")){  %> --%> 
									<td class="celda2">
										<bean:message key="clasificacion" />
									</td>
                                                                     <%--       <% } %> --%> 
                                                                        <!-- ffceballos -->
									<td class="celda2">
										<bean:message key="otraCategoria" />
									</td>
                                                                           <% if(!enlace.equalsIgnoreCase("08")){  %> 
									<td class="celda2">
										&nbsp;
									</td>    <% } %> 
								</tr>
								<%
									boolean nuevo;
                                                                        
                                                                        ArrayList categorias=new ArrayList();
								%>
								<logic:iterate name="modificarEsquemaForm" id="elemento"
									property="activities" indexId="index" scope="request">
									<%
										nuevo = ((Activity) elemento).getConsecutive() == 0;
                                                                                
                                                                                String  categoriaActividad="";    
                                                                                categoriaActividad += ((Activity) elemento).getCategory();
                                                                                categorias.add(categoriaActividad);
									%>
									<tr>
										<td class="celda">
											<html:hidden name="elemento" property="consecutive"
									  			indexed="true" />
											<%
												if (!nuevo) {
											%>
											<html:hidden name="elemento" property="name" indexed="true" />
											<html:hidden name="elemento" property="type" indexed="true" />
											<html:hidden name="elemento" property="category"
												indexed="true" />
											<html:hidden name="elemento" property="other" indexed="true" />
											<%
												}
											%>
											<html:hidden name="elemento" property="topics" indexed="true" />
											<html:hidden name="elemento" property="description"
												indexed="true" />
											<html:hidden name="elemento" property="date" indexed="true" />
											<html:hidden name="elemento" property="percentage"
												indexed="true" />
											<html:text styleClass="nombre" name="elemento"
												property="name" indexed="true" disabled='<%=!nuevo%>' />
										</td>
										<td class="celda">
											<%
                                                                                            
                                                                                            
												String habilitar = "habilitar(modificarEsquemaForm['elemento["
																	+ index
																	+ "].category'],modificarEsquemaForm['elemento["
																	+ index + "].other'])";
											%>

											
                                                                                         <!--ffceballos-->
                                                                                                                
                                                                                                                <%  enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                                                                <c:set var="a" value="<%=enlace %>"/>
                                                                                                                
                                                                                                                <% if(enlace.equalsIgnoreCase("08")){  %> 
                                                                                                                    
                                                                                                                       <% Boolean a=(Boolean)session.getAttribute("RCOORDINMAT");
                                                                                                                          boolean b=a.booleanValue();
                                                                                                                          if(b){ %>
                                                                                                                                
                                                                                                                                      <!-- original -->
                                                                                                                                <html:select styleClass="nombre" name="elemento"
                                                                                                                                        property="category" indexed="true" disabled='<%=!nuevo%>'
                                                                                                                                        onclick='<%=habilitar%>'>
                                                                                                                                            <html:options collection="tiposCategoria" property="nombre"/>

                                                                                                                                            <html:option value="Otra" >Otra</html:option>
                                                                                                                                             
                                                                                                                                </html:select>  
                                                                                                                                        <!-- original -->     
                                                                                                
                                                                                                                              <% }else{ %> 
                                                                                                                                
                                                                                                                              <!-- ffceballos -->
                                                                                                     <% String curso =((Curso) session.getAttribute("curso")).getDepartamento();  %>
                                                                                                     
                                                                                                     
                                                                                                   
                                                                                                     
                                                                                                     
                                                                                                                <c:set var="a" value="<%=curso %>"/>
                                                                                                                
                                                                                                     <html:select styleClass="nombre" name="elemento" property="category" indexed="true" disabled='<%=!nuevo%>'
												onclick='<%=habilitar%>'>
                                                                                                  
                                                                                                    
                                                                                                    <%--      <c:forEach items="<%=categoriasValidas%>" var="var">  
                                                                                                       
                                                                                                         <c:choose>
                                                                                                             
                                                                                                             <c:when test="${var.nombre=='Parciales'}">  <html:option value="${var.nombre}" disabled='true' > <c:out value="${var.nombre}" /> </html:option> </c:when>
                                                                                                             <c:when test="${var.nombre=='Examen Final'}">  <html:option value="${var.nombre}" disabled='true' > <c:out value="${var.nombre}" /> </html:option> </c:when>
                                                                                                             <c:otherwise>  
                                                                                                                        
                                                                                                                 <html:option value="${var.nombre}" > <c:out value="${var.nombre}" /> </html:option>
                                                                                                             </c:otherwise>
                                                                                                             
                                                                                                         </c:choose>
                                                                                                        
                                                                                                         
                                                                                                         
                                                                                                     </c:forEach>   --%>
                                                                                                    
                                                                                                    
                                                                                                    <% for(int cont=0;cont<categoriasValidas.size();cont++){ 
                                                                                                        
                                                                                                        Categoria categoria=(Categoria)categoriasValidas.get(cont);
                                                                                                        
                                                                                                        if(categoria.getNombre().equalsIgnoreCase("Parciales")||categoria.getNombre().equalsIgnoreCase("Examen Final")){
                                                                                                        %> <html:option value="<%=categoria.getNombre() %>" disabled='true' > <%=categoria.getNombre() %> </html:option>
                                                                                                      <%  }else {
                                                                                                        %>
                                                                                                            <html:option value="<%=categoria.getNombre() %>" > <%=categoria.getNombre() %> </html:option>
                                                                                                       <% }  } %>
                                                                                                    
                                                                                                    
                                                                                                     </html:select>  
                                                                                                     <!-- ffceballos -->
                                                                                                     
                                                                                                                            <% } %>                                                                                                                                                                                                                     
                                                                                                                            
                                                                                                                        
                                                                                                                 <% } 
                                                                                                                     else{ %>
                                                                                                                        
                                                                                                                         <!-- original -->
                                                                                        <html:select styleClass="nombre" name="elemento"
												property="category" indexed="true" disabled='<%=!nuevo%>'
												onclick='<%=habilitar%>'>
                                                                                                    <html:options collection="tiposCategoria" property="nombre"/>
                                                                                                   
                                                                                                    <html:option value="Otra" >Otra</html:option>
                                                                                                    
											</html:select>  
                                                                                                <!-- original -->     
														
                                                                                                                 <% } %>
                                                                                                                    
                                                                                                                
                                                                                                                <!--ffceballos-->
                                                                                        
                                                                                                  
                                                                                                     
                                                                                                     
										</td>
                                                                                <!--ffceballos-->
                                                                                <%  enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                               
                                                                            <c:set var="a" value="<%=enlace %>"/>
                                                                            <%-- <% if(!enlace.equalsIgnoreCase("08")){  %> --%>
                                                                                 
										<td class="celda">
											<html:radio styleClass="nombre" name="elemento"
								 				property="type" value="I" indexed="true"
												disabled='<%=!nuevo%>'>Individual</html:radio>
											<html:radio styleClass="nombre" name="elemento"
												property="type" value="G" indexed="true"
												disabled='<%=!nuevo%>'>Grupal</html:radio>
										</td>  <%-- <% } %>--%>
                                                                                <!--ffceballos-->
										<td class="celda">
                                                                                    
                                                                                    
                                                                                    <!--ffceballos -->
                                                                                    
                                                                                    
                                                                                    
                                                                                    
                                                                                                                                                                          
                                                                                       <% if(enlace.equalsIgnoreCase("08")){  %>
                                                                                            
                                                                                                   <% Boolean a=(Boolean)session.getAttribute("RCOORDINMAT");
                                                                                                        boolean b=a.booleanValue();
                                                                                                        if(b){ %> 
                                                                                                                        <html:text name="elemento" property="other" indexed="true"
                                                                                                                              disabled="true" />
                                                                                                                        <a href='javascript:remove(<%=index%>)' onclick='prueba=false;'>Borrar  </a>				
                                                                                                           <% }else{ %> 
                                                                                                                       <% 
                                                                                                                        String  nombreCategoría="";    
                                                                                                                        nombreCategoría += ((Activity) elemento).getCategory();
                                                                                                                     if(!nombreCategoría.equals("Parciales")&& !nombreCategoría.equals("Examen Final") &&!nombreCategoría.equals("Pruebas cortas") ){ %>  
                                                                                                                     
                                                                                                                      <html:text name="elemento" property="other" indexed="true"
                                                                                                                        disabled="true" />
                                                                                                                     <a href='javascript:remove(<%=index%>)' onclick='prueba=false;'>Borrar  </a>
                                                                                                                     
                                                                                                                       <% }else {
                                                                                                                       %>
                                                                                                                       
                                                                                                                       <html:text name="elemento" property="other" indexed="true"
                                                                                                                        disabled="true" />
                                                                                                                     <a>Borrar  </a>
                                                                                                                       
                                                                                                                       <% }%>
                                                                                                                        
                                                                                                            <% } %>
                                                                                                    
                                                                                        <% } 	
                                                                                            else{%>
                                                                                            
                                                                                            
                                                                                                    <html:text name="elemento" property="other" indexed="true"
                                                                                                         disabled="true" />
                                                                                <td>  <a href='javascript:remove(<%=index%>)' onclick='prueba=false;'>Borrar  </a> </td>
                                                                                                    
                                                                                           <% } %>
                                                                                   
                                                                                    
                                                                                    
                                                                                    
                                                                                    
                                                                                    
                                                                                    <!--fin ffceballos-->
                                                                                    
                                                                                    
											
										</td>
									</tr>
								</logic:iterate>
								<%--2 es el valor por defecto para la propiedad 'opcion' cuando
        se vaya a hacer submit al formulario. --%>
								<html:hidden property="opcion" value="2" />
								<html:hidden property="indice" />
							</table>
							<br>
							<br>
							<center>
								<%
									boolean eliminoActividad = session
													.getAttribute("actividadEliminada") != null
													&& session.getAttribute("actividadEliminada")
															.equals("S");
								%>
                                                                <html:submit value="Aceptar" onclick="prueba=false;" />
								<html:button property="boton" value="Agregar Actividades"
									onclick="prueba=false; funcion(1);" />
								<%
									if (session.getAttribute("nuevo") != null) {
								%>
								<html:cancel value="Cancelar" />
								<%
									}
								%>
							</center>
						</html:form>

						<script language="javascript">
							habilitarTodos();
						</script>
						<br />
					</td>
				</tr>
			</table>
			<jsp:include page="../template/footer.jsp"></jsp:include>
                        
                        <script>
                 
//ffceballos
                        //detectamos el evento beforeunload de la ventana, el cual nos permite
//mostrar la confirmacion de salida
window.onbeforeunload=function(){
	
	if(prueba){ 
		return 'Si abandona la página en este momento y ha realizado un cambio en el esquema, quedará con errores. Para navegar por la página, por favor de utilice los botones';
	}
};
                   
                        </script>
                        
		</body>
	</html>
</logic:present>
<logic:notPresent scope="session" name="activa">
	<jsp:forward page="/notasparciales/sesionInactiva.icesi"></jsp:forward>
</logic:notPresent>