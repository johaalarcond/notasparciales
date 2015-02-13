
<html>
    <head>
        <title>Seguimiento a estudiantes - Universidad Icesi - Cali,
			Colombia</title>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1">
        <link href="<%=request.getContextPath()%>/css/estilos.css"
              rel="stylesheet" type="text/css">
        <style>
            .bordeError {
                width: 50%;
                min-width: 0px;
                max-width: 400px;
                background: #eee;
                border: 1px solid #c00;
                padding: 5px;
            }

            .error {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 12px;
                color: #990000;
            }

            .info {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 12px;
                color: black;
            }
        </style>
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
          onload="cerrarSesion();">
        <jsp:include page="../template/header.jsp"></jsp:include>
        <br/>
        <table border="0" align="center" cellpadding="00"
               cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <div class="error">
                            <%
                                        if (request.getAttribute("mensajeError") != null) {
                            %>
                            <strong>Error: </strong><%=request.getAttribute("mensajeError")%>
                            <%
                                        }
                            %>
                        </div>
                        <br />
                        <div class="info">
							Si lo desea, vuelva a iniciar sesión a través del
                            <a href="http://www.icesi.edu.co/servicios_profesores.php">Portal
								de Profesores</a>.
                            <br />
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <br/>
        <jsp:include page="../template/footer.jsp"></jsp:include>
    </body>
</html>
