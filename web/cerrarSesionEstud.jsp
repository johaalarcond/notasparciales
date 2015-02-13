<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
    <%session.invalidate();
    response.sendRedirect("https://iden.icesi.edu.co/pls/orasso/orasso.wwsso_app_admin.ls_logout?p_done_url=http://www.icesi.edu.co/servicios_estudiantes.php");
    %>
  </body>
</html>
