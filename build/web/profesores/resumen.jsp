<%@ page language="java" errorPage="/profesores/error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@taglib uri="/WEB-INF/notas.tld" prefix="icesi"%>
<html>
	<head>
		<title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link href="<%=request.getContextPath()%>/css/estilos.css"
			rel="stylesheet" type="text/css">
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
					<div class="curso" align="center">
						<bean:write name="curso" scope="session"
							property="descripcionMateria" />
					</div>
					<br>
					<br>
					<div>
						<html:link styleClass="links" action="/aRegistro">Atrás</html:link>
					</div>

					<div>
						<html:link styleClass="links" action="/aRegistro">Atrás</html:link>
					</div>
					<br>
				</td>
			</tr>
		</table>
	</body>
</html>
