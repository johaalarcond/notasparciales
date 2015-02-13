package co.edu.icesi.notas.segestud.control;

import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.control.ControlRecursos;
import co.edu.icesi.notas.segestud.*;

public class ControlSegestud {

    public static boolean usuarioValido(Connection conexion,
            String identificacion) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery("SELECT DISTINCT 's' "
                    + "  FROM rol_usuarios ru, empleado e "
                    + " WHERE e.cedula = '"
                    + identificacion
                    + "'  AND ru.nombre_rol IN ('SECGEN', 'DIRPROGR', 'JEFDEPRE') "
                    + "   AND ru.nombre_usuario = e.usuario " // ffceballos 23/04/2014 a partir de aquí se agregó 
                    + "or (select count(cedula) "
                    + "from tntp_roles_notpa r "
                    + "where r.cedula='"
                    + identificacion
                    + "' and r.nombre_rol='SEGUIMIENTO') > 0");
            
            if (rs.next()) {
                ControlRecursos.liberarRecursos(rs, stm);
                return true;
            }
            ControlRecursos.liberarRecursos(rs, stm);
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error validando el usuario " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return false;
    }

    public static UsuarioSegestud cargarUsuario(Connection conexion,
            String identificacion) {
        UsuarioSegestud usuario = new UsuarioSegestud();
        usuario.setIdentificacion(identificacion);
        usuario.setNombreUsuario(nombreUsuario(conexion, usuario));
        usuario.setTipo(tipoUsuario(conexion, usuario));
        return usuario;
    }

    public static String nombreUsuario(Connection conexion,
            UsuarioSegestud usuario) {
        String nombreUsuario = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery("SELECT e.usuario FROM empleado e WHERE e.cedula = '"
                    + usuario.getIdentificacion() + "'");
           
            //ffceballos ==> se agregó el while para cuando viene vacío debido a que la psicologa de seguimiento no se encuentra en esta tabla
            while (rs.next()) {
            nombreUsuario = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el nombre del usuario "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return nombreUsuario;
    }

    public static String tipoUsuario(Connection conexion,
            UsuarioSegestud usuario) {
        String tipoUsuario = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery("SELECT COUNT (dp.codigo) d, COUNT (pr.codigo) p, SUM (DECODE (ru.nombre_rol, 'SECGEN', 1, 0)) r "
                    + "  FROM (tbas_deptos_acad dp FULL JOIN tbas_programas pr ON dp.usuario = pr.usuario) "
                    + "       full JOIN rol_usuarios ru "
                    + "          ON ru.nombre_usuario = dp.usuario "
                    + "          OR ru.nombre_usuario = pr.usuario "
                    + " WHERE dp.usuario = '"
                    + usuario.getNombreUsuario()
                    + "' "
                    + "    OR pr.usuario = '"
                    + usuario.getNombreUsuario()
                    + "' "
                    + "    OR ru.nombre_usuario = '"
                    + usuario.getNombreUsuario() + "' ");
            rs.next();
            int dpto = rs.getInt(1);
            int prog = rs.getInt(2);
            int secg = rs.getInt(3);
            if (dpto > 0) {
                tipoUsuario = "D";
            } else if (prog > 0) {
                tipoUsuario = "P";
            } else if (secg > 0) {
                tipoUsuario = "B";
            }
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el tipo de usuario "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return tipoUsuario;
    }

    public static Map obtenerDefinitivas(Connection conexion,
            UsuarioSegestud usuario) {
        Map alumnosDefinitivas = new HashMap();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            String sqlDefinitivas = "select CURSO, DESCRIPMAT_CODIGO, DESCRIPMAT_CONSECUTIVO, NOMBRE_MATERIA, GRUPO, ALUMNO_CODIGO, FPRECAL_INITCAP(NOMBRE), "
                    + "decode(DEFINITIVA,fprebus_constantes ('010', '04', ';'),fprebus_constantes ('010', '02', ';'),to_number( to_char(DEFINITIVA, '9.99'), '9.99')) from ttem_tntp_definitivas where lower(usuario) = lower('"
                    + usuario.getNombreUsuario() + "') order by ALUMNO_CODIGO";
            rs = stm.executeQuery(sqlDefinitivas);
            AlumnoSegestud alumno = null;
            while (rs.next()) {
                String codigoAlumno = rs.getString(6);
                String nombreAlumno = rs.getString(7);
                if (alumnosDefinitivas.get(codigoAlumno) == null) {
                    alumno = new AlumnoSegestud(codigoAlumno);
                    alumno.setNombre(nombreAlumno);
                    alumnosDefinitivas.put(codigoAlumno, alumno);
                }
                CursoSegestud curso = new CursoSegestud();
                curso.setCurso(rs.getString(1));
                curso.setMatcod(rs.getString(2));
                curso.setMatcon(rs.getString(3));
                curso.setMateria(rs.getString(4));
                curso.setGrupo(rs.getString(5));
                curso.setDefinitiva(rs.getString(8));
                alumno.getCursos().add(curso);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando las definitivas "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return alumnosDefinitivas;
    }

    //ffceballos: método para obtener toda la información necesaria para el seguimiento a estudiantes
    public static Map obtenerSeguimiento(Connection conexion,
            String pcod, String identificacion) {
        Map alumnosDefinitivas = new HashMap();
        //CAMBIAR EL PPROGRAMA POR EL DE LA PERSONA QUE INICIA SESION A MENOS QUE SEA DE BIENESTAR!
        //String pprograma = "FPREBUS_PROG_ALUMNO(alu.codigo,'S', fprebus_constantes('001','03',null) ,to_number(fprebus_constantes('001','04',null)),'S')"; // ==> OJO ESTÁ QUEMADO!! CAMBIAR !!!!
        Statement stm = null;
        ResultSet rs = null;
        Statement stm2 = null;
        ResultSet rs2 = null;
     /*   Statement stm3 = null;
        ResultSet rs3 = null;*/
        
       /*String programas=" SELECT  count (pr.codigo) p" +
"                 FROM (tbas_deptos_acad dp\n" +
"                       FULL JOIN tbas_programas pr ON dp.usuario = pr.usuario)" +
"                      FULL JOIN" +
"                      rol_usuarios ru" +
"                         ON    ru.nombre_usuario = dp.usuario" +
"                            OR ru.nombre_usuario = pr.usuario" +
"                      JOIN" +
"                      empleado e" +
"                         ON    dp.usuario = e.usuario" +
"                            OR pr.usuario = e.usuario" +
"                            OR ru.nombre_usuario = e.usuario" +
"                WHERE e.cedula = "+identificacion;*/
        
        try {
          /*   stm3= conexion.createStatement();  
             rs = stm.executeQuery(programas);
             while (rs.next()) {
                 String cantProgr = rs.getString(1);
             }*/
             
            stm = conexion.createStatement();
            //se cambió fprebus_constantes('001') por ('002)
            String estudiantes = "select  distinct alu.codigo, FPRECAL_INITCAP(alu.apellidos) ||' '|| FPRECAL_INITCAP(alu.nombre) "
                    + "from  tbas_alumnos alu, tntp_matriculas ma, tntp_cursos cu "
                    + "where alu.codigo like ('" + pcod + "'||'%')"
                    + " and ma.codigo_alumno=alu.codigo"
                    + " and ma.estado_alumno='A'"
                    + " and cu.periodo_periodo_acad= fprebus_per_notp('01','')"//fprebus_constantes('002','03',null)"
                    + " and cu.periodo_consecutivo= to_number(fprebus_per_notp('02',''))"//fprebus_constantes('002','04',null)
                    + " and cu.consecutivo=ma.consec_curso"
                    + " and (( FPREBUS_PROG_ALUMNO(alu.codigo,'S', fprebus_per_notp('01','') ,to_number(fprebus_per_notp('02','')),'S')"
                    + " in (select  distinct (pr.codigo) p" +
"                              from (   tbas_deptos_acad dp " +
"                                   full join \n" +
"                                     tbas_programas pr " +
"                                   on dp.usuario = pr.usuario) " +
"                                 full join rol_usuarios ru " +
"                                      on ru.nombre_usuario = dp.usuario " +
"                                      or  ru.nombre_usuario = pr.usuario " +
"                                   join empleado e " +
"                                      on dp.usuario = e.usuario " +
"                                      or  pr.usuario = e.usuario " +
"                                      or  ru.nombre_usuario = e.usuario " + 
"                             where e.cedula ="+identificacion+") ) "
                    //Desde aquí se cambió la consulta!
                    + " OR ( ( select  sum (decode (ru.nombre_rol, 'SECGEN', 1, 0)) r " +
"                             from (   tbas_deptos_acad dp " +
"                                  full join " +
"                                      tbas_programas pr " +
"                                    on dp.usuario = pr.usuario) " +
"                                  full join rol_usuarios ru " +
"                                   on ru.nombre_usuario = dp.usuario " +
"                                    or  ru.nombre_usuario = pr.usuario " +
"                                  join empleado e " +
"                                    on dp.usuario = e.usuario " +
"                                     or  pr.usuario = e.usuario " +
"                                     or  ru.nombre_usuario = e.usuario " +
"                             where e.cedula = '"+identificacion+"'   ) > 0" +
"                 )  " +
"                OR ( FPREBUS_PROG_ALUMNO (" +
"                   alu.codigo," +
"                   'S'," +
"                   fprebus_per_notp('01',''),"+//fprebus_constantes ('002', '03', NULL)," +
"                   TO_NUMBER (fprebus_per_notp('02',''))," +//fprebus_constantes ('002', '04', NULL)
"                   'S') IN ( select sm.codigo_programa p  from tntp_seguimiento_mat sm where sm.cedula='"+identificacion+"' )    )" +
"                 ) ";
             //se cambió esta parte de la consulta xq entró un nuevo rol (SEGUIMIENTO) que solo puede ver ciertos programas
                 /*   + "or ( SELECT  count (pr.codigo) p" +
"                 FROM (tbas_deptos_acad dp\n" +
"                       FULL JOIN tbas_programas pr ON dp.usuario = pr.usuario)" +
"                      FULL JOIN" +
"                      rol_usuarios ru" +
"                         ON    ru.nombre_usuario = dp.usuario" +
"                            OR ru.nombre_usuario = pr.usuario" +
"                      JOIN" +
"                      empleado e" +
"                         ON    dp.usuario = e.usuario" +
"                            OR pr.usuario = e.usuario" +
"                            OR ru.nombre_usuario = e.usuario" +
"                WHERE e.cedula = "+identificacion+" )=0 )";/*=" + pprograma; */  
            
          //  System.out.println("++++++++++++++++"+estudiantes+"\n\n");
            rs = stm.executeQuery(estudiantes);
            AlumnoSegestud alumno = null;


            while (rs.next()) {
                String codigoAlumno = rs.getString(1);
                String nombreAlumno = rs.getString(2);

                alumno = new AlumnoSegestud(codigoAlumno);
                alumno.setNombre(nombreAlumno);

                ArrayList listaCursos = new ArrayList();



                stm2 = conexion.createStatement();

                String cursos = "select FPREBUS_DESCRIP_MAT(cur.descripmat_consecutivo,cur.descripmat_codigo,'03','') nombreCurso, act.consecutivo, ACT.nombre,   (CATE.PORCENTAJE*(ACT.PORCENTAJE/100)) porcentaje, CALIF.CALIFICACION,   (select definitiva  from tntp_matriculas where codigo_alumno='"+ codigoAlumno+"'  and consec_curso=CUR.CONSECUTIVO ) definitiva"
                        + " from tntp_clasif_categ cla, tntp_categorias cate, tntp_tipo_categoria tica, tntp_actividades act, tntp_calificaciones calif, tntp_cursos cur"
                        + " where "
                        + " "
                        + " cate.consec_clasif_categ=cla.consecutivo"
                        + " and CATE.CONSEC_TIPO_CATEG=TICA.CONSECUTIVO"
                        + " and ACT.CONSEC_CATEGORIA=CATE.CONSECUTIVO"
                        + " and CALIF.CODIGO_ALUMNO='" + codigoAlumno
                        + "' "
                        + " and CALIF.CONSEC_ACTIVIDAD=ACT.CONSECUTIVO"
                        + " and cur.consecutivo=cla.consec_curso"
                        + " and CALIF.CONSEC_CURSO in ("
                        + " select cu.consecutivo"
                        + " from tntp_cursos cu, tntp_matriculas ma"
                        + " where ma.codi"
                        + ""
                        + "go_alumno='" + codigoAlumno
                        + "' and ma.estado_alumno='A'"
                        + " and cu.periodo_periodo_acad=fprebus_per_notp('01','') "//fprebus_constantes ('002', '03', NULL)
                        + " and cu.periodo_consecutivo= TO_NUMBER (fprebus_per_notp('02',''))"//fprebus_constantes ('002', '04', NULL)
                        + " and cu.consecutivo=ma.consec_curso )";

                //System.out.println("*********************************"+cursos);
                
                rs2 = stm2.executeQuery(cursos);

                String cursoActual = "none";
                boolean primeraVez = true;
                String curso = "";
                int cont=0;
                while (rs2.next()) {

                    String cursoNuevo = rs2.getString(1);
                    String definitiva=rs2.getString(6);

                    
                      if (!cursoActual.equals(cursoNuevo)) {
                        if (!primeraVez) {
                            listaCursos.add(curso);
                            curso = "";
                        }
                        
                         curso = definitiva+"#"+cursoNuevo+"*";
                        primeraVez = false;
                        cursoActual = cursoNuevo;
                    }
                     
                    
                    String nombreActividad = rs2.getString(3);
                    String porcentaje = rs2.getString(4);
                    String calificacion = rs2.getString(5);

                    curso = curso + nombreActividad + "+" + porcentaje + "%" + calificacion + "]";

                  
                  

                }
              //  if (primeraVez) {
                    listaCursos.add(curso);
               // }


                alumno.setCursos(listaCursos);

                alumnosDefinitivas.put(codigoAlumno, alumno);


            }
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando las definitivas "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        ControlRecursos.liberarRecursos(rs2, stm2);
        return alumnosDefinitivas;
    }

    //fin ffceballos
    
    public static ArrayList obtenerAlumnos(Connection conexion,
            UsuarioSegestud usuario) {
        ArrayList alumnos = new ArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery("SELECT DISTINCT mtr.alumno_codigo "
                    + "  FROM ((tbas_matriculas mtr JOIN tbas_materias mat ON mtr.descripmat_codigo = mat.codigo) "
                    + "       JOIN tbas_programas pr "
                    + "          ON mtr.programa_codigo = pr.codigo) "
                    + "       JOIN tbas_deptos_acad dp "
                    + "          ON mat.deptos_acad_codigo = dp.codigo "
                    + " WHERE mtr.periodo_periodo_acad || mtr.periodo_consecutivo = fprebus_per_notp('0102','') "//fprebus_constantes ('002', '0304', '')
                    + "   AND (mtr.novedad IS NULL "
                    + "     OR mtr.novedad != 'C') "
                    + "   AND pr.usuario LIKE DECODE ('"
                    + usuario.getTipo() + "', 'P', '"
                    + usuario.getNombreUsuario() + "', '%') "
                    + "   AND dp.usuario LIKE DECODE ('"
                    + usuario.getTipo() + "', 'D', '"
                    + usuario.getNombreUsuario() + "', '%'); ");
            while (rs.next()) {
                AlumnoSegestud alumno = new AlumnoSegestud(rs.getString(1));
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + ControlSegestud.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando alumnos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return alumnos;
    }
}
