<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Notas parciales - Login</title>
        <style type="text/css">
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

    <body>
        <jsp:include page="template/header.jsp"></jsp:include>
        <div align="center">
            <form method="POST" action="j_security_check">
                Usuario: <input type="text" name="j_username">
                <br/>
                Contrase&ntilde;a: <input type="password" name="j_password">
                <br/>
                <input type="submit" value="Ingresar">
            </form>
        </div>
        <jsp:include page="template/footer.jsp"></jsp:include>
    </body>
</html>