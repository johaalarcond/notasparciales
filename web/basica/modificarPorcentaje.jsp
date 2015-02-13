<%@ page language="java" errorPage="/profesores/error.jsp"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
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
			<script language="javascript"
				src="<%=request.getContextPath()%>/js/porcentajes.js"></script>
			<SCRIPT language="javascript">
	function Modificar(index,index2){
      modificarPorcentajesForm.opcion.value='1'; 
      modificarPorcentajesForm.indiceUno.value=index;
	  modificarPorcentajesForm.indiceDos.value=index2;
      modificarPorcentajesForm.submit();
	}
	
	function Regresar(){
		modificarPorcentajesForm.opcion.value='2';
		modificarPorcentajesForm.submit();
	}
	
	function Redistribuir(index){
            //ffceballos
            prueba=false;
            //fin ffceballos
		modificarPorcentajesForm.opcion.value='3';
		modificarPorcentajesForm.indiceUno.value = index;
		modificarPorcentajesForm.submit();
	}
	</SCRIPT>

			<link href="<%=request.getContextPath()%>/css/estilos.css"
				rel="stylesheet" type="text/css">
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
						<div class="curso" align="center">
							<bean:write name="curso" scope="session"
								property="descripcionMateria" />
							<br>
							Grupo:
							<bean:write name="curso" scope="session" property="grupo" />
						</div>
						<br>
						<div class="curso" align="center">
							<bean:message key="modificacion.porcentajes" />
						</div>
						<br>

						<%
							boolean hayError = request.getSession().getAttribute("errores") != null;
								//Variables empleada para seleccionar los estilos en la tabla de abajo
								boolean primeraCat1 = true, primeraCat2 = true, primeraCat3 = true;
								if (hayError) {
						%>
						<div align="center">
							<fieldset class="fieldset">
								<div class="error" align="left">
									<html:errors property="errorSuma" />
									<html:errors property="diferenteCien" />
									<html:errors property="errorCat" />
								</div>
							</fieldset>
							<br />
						</div>
						<%
							}
						%>

						<html:form action="/modificarPorcentajes">

							<table class="tabla" align="center" cellspacing="0">
								<logic:iterate name="modificarPorcentajesForm" id="elemento"
									property="listaCategorias" indexId="index"
									type="co.edu.icesi.notas.form.Category">
									<html:hidden name="modificarPorcentajesForm"
										property='<%="elemento["+index+"].name"%>' />
									<logic:iterate name="elemento" property="listActivities"
										id="actividad" indexId="index2"
										type="co.edu.icesi.notas.basica.Activity">

										<%
											if (((Integer) index2).intValue() == 0) {
										%>
										<tr>
											<%
												if (primeraCat1) {
											%>
											<td class="titulo7" align="left">
												<%
													primeraCat1 = false;
																		} else {
												%>
												<td class="titulo6" align="left">
													<%
														}
													%>
													<bean:write name="actividad" property="category" />
												</td>
												<%
													if (primeraCat2) {
												%>
												<td class="titulo8">
													<%
														primeraCat2 = false;
																			} else {
													%>
													<td class="titulo4">
														<%
															}
														%> 
                                                                                                                <!--ffceballos-->
                                                                                                                
                                                                                                                <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                                                                <c:set var="a" value="<%=enlace %>"/>
                                                                                                                
                                                                                                               <% if(enlace.equalsIgnoreCase("08")){  %>
                                                                                                                        <% Boolean a=(Boolean)session.getAttribute("RCOORDINMAT");
                                                                                                                            boolean b=a.booleanValue();
                                                                                                                            if(b){ %>
                                                                                                                                <html:text name="modificarPorcentajesForm"
															property='<%="elemento["+index+"].percentage"%>' size="4"
															disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
														%
                                                                                                                           <% }else{ %> 
                                                                                                                                <html:text readonly="true" name="modificarPorcentajesForm"
															property='<%="elemento["+index+"].percentage"%>' size="4"
                                                                                                                        disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' onclick="this.blur();"  />
                                                                                                                                <!--   disabled='true' />%  -->
                                                                                                                        
                                                                                                                          <% } %>
                                                                                                                        
                                                                                                                    <% }
                                                                                                                   
                                                                                                                             else{  %>
														<html:text name="modificarPorcentajesForm"
															property='<%="elemento["+index+"].percentage"%>' size="4"
															disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
														%
                                                                                                             <% } %>
                                                                                                                <!--ffceballos-->
													</td>

													<%
														if (primeraCat3) {
													%>
													<td class="titulo9">
														<%
															primeraCat3 = false;
																				} else {
														%>
														<td class="titulo5">
															<%
																}
															%>
															<html:button property="redistribuir" value="Redistribuir"
																onclick='<%="Redistribuir("+index+")"%>'
																disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>'>Redistribuir</html:button>
														</td>
										</tr>
										<%
											}
										%>
										<tr>
											<td>
												&nbsp;&nbsp;
											</td>
											<td class="celda7">
												-
												<a href="javascript:Modificar(<%=index%>,<%=index2%>)">
													<bean:write name="actividad" property="name" /> - <bean:write
														name="actividad" property="type" /> </a>
												<%--Campos ocultos requeridos para que la información no se pierda. --%>
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].name"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].type"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].category"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].consecutive"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].date"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].description"%>' />
												<html:hidden name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].topics"%>' />
											</td>
											<td class="celda8">
                                                                                            
                                                                                            <!--ffceballos-->
                                                                                            
                                                                                            <% String enlace =((Curso) session.getAttribute("curso")).getDepartamento(); %>
                                                                                                                <c:set var="a" value="<%=enlace %>"/>
                                                                                                               
                                                                                                                
                                                                                                               <% if(enlace.equalsIgnoreCase("08")){  %> 
                                                                                                                    

                                                                                                                    <% Boolean a=(Boolean)session.getAttribute("RCOORDINMAT");
                                                                                                                       boolean b=a.booleanValue();
                                                                                                                       if(b){ %> 
                                                                                                                       
                                                                                                                       <html:text name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].percentage"%>'
													size="4" onchange='<%="totalCategoria("+index+")"%>'
													disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
												%
                                                                                                                            <% }else{ %> 
                                                                                                                                <html:text  readonly="true" name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].percentage"%>'
													size="4" onchange='<%="totalCategoria("+index+")"%>'
                                                                                                        disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' onclick=" this.blur();"  />
                                                                                                        
                                                                                                       <!-- disabled='true' /> -->
												%
                                                                                                                        
                                                                                                                      
                                                                                                                        <% } %>
                                                                                                                        
                                                                                                                    <% } 
                                                                                                                   else{ %>
														<html:text name="modificarPorcentajesForm"
													property='<%="elemento["+index+"].activity["+index2+"].percentage"%>'
													size="4" onchange='<%="totalCategoria("+index+")"%>'
													disabled='<%=(session.getAttribute("cerrado").equals("N"))?false:true%>' />
												%
                                                                                                                <% } %>
												
                                                                                                
                                                                                                
                                                                                                
                                                                                               <!--ffceballos--> 
											</td>
										</tr>
									</logic:iterate>
								</logic:iterate>

								<html:hidden property="opcion" value="4" />
								<html:hidden property="indiceUno" />
								<html:hidden property="indiceDos" />
							</table>
							<br>
							<br>
							<center>
								<html:submit value="Aceptar" onclick="prueba=false"/>
								<html:button property="boton" value="Regresar a Configuración"
									onclick="prueba=false; Regresar();">Regresar a Configuración</html:button>
								<%--   <html:cancel value="Cancelar"/> ---%>
							</center>
						</html:form>
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