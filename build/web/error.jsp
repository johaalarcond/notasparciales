<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<html>
    <head>
        <title>Notas parciales - Universidad Icesi - Cali, Colombia</title>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1">
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

            #pie {
                color:#FFFFFF;
                font-family:Verdana,Arial,Helvetica,sans-serif;
                font-size:12px;
            }

            #pie a {
                color:#FFFFFF;
                font-weight:normal;
                text-decoration:none;
            }

            #pie a:hover {
                text-decoration:underline;
            }
        </style>
    </head>

    <body link="#666666" vlink="#666666" alink="#666666" leftmargin="0"
          topmargin="0" marginwidth="0" marginheight="0">
        <jsp:include page="template/header.jsp"></jsp:include>
        <table border="0" align="center" cellpadding="00"
               cellspacing="0">
            <tr>
                <td>
                    <div align="center">
                        <div class="error">
                            <div class="error">
								Ha ocurrido un error en la transacción anterior.
                                <br />
                                <br />
                                <logic:present name="mensajeError">
                                    <strong>Error: </strong>
                                    <bean:write name="mensajeError" />
                                    <br />
                                </logic:present>
                                <br />
								Por favor haga click en el siguiente enlace para para reiniciar
								su sesión de trabajo:
                                <br />
                                <center>
                                    <a href="/notasparciales">Inicio</a>
                                    <br>
                                    <br>
									Si el error persiste por favor comun&iacute;quese al correo
                                    <a href="mailto:soporte-syri@listas.icesi.edu.co">soporte-syri@listas.icesi.edu.co</a>
                                    <br>
                                </center>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <jsp:include page="template/footer.jsp"></jsp:include>
    </body>
</html>