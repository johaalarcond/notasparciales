<%-- 
    Document   : concheck
    Created on : 11/10/2010, 01:43:05 PM
    Author     : 1130608864
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1" import="co.edu.icesi.notas.control.ControlRecursos" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="refresh" content="2"/>
        <title>Open connections</title>
    </head>
    <body>
        <%
            out.println("Conexiones abiertas: " + ControlRecursos.getConexionesAbiertas());
        %>
    </body>
</html>