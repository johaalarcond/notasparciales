/*
 * Created on 02-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.edu.icesi.notas.control.*;

import java.sql.Statement;
import java.util.*;

/**
 * Representa al profesor que actualmente está trabajando contra la aplicación.
 * 
 * @author mzapata, lmdiaz
 */
public class Profesor implements Serializable {

    private String cedula;
    private ArrayList cursos = new ArrayList();
    private String correo;
    private String deptoAcademico;

    
    //ffceballos    
    
    private ArrayList cursosCoordinar = new ArrayList();
    
   
    public ArrayList getCursosCoordinar() {
        return cursosCoordinar;
    }

    public void setCursosCoordinar(ArrayList cursosCoordinar) {
        this.cursosCoordinar = cursosCoordinar;
    }
     //fin ffceballos
    
    public String getCorreo() {
        return correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public ArrayList getCursos() {
        return cursos;
    }

    public String getDeptoAcademico() {
        return deptoAcademico;
    }

    public void setDeptoAcademico(String deptoAcademico) {
        this.deptoAcademico = deptoAcademico;
    }

    /**
     * Carga la información de todos los cursos del profesor, directamente de la
     * base de datos.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param consecPeriodo
     *            Consecutivo del periodo académico.
     * @param periodoAcademico
     *            Periodo académico
     * @param periodoAcademico
     *            indica si tiene materias para coordinar -adiciona por ffceballos-
     */
    public void cargarCursos(Connection conexion, int consecPeriodo,
            String periodoAcademico,boolean coordina) { // jalarcon - ffceballos: se agregó regla_matematicas y regla_quiz al select y en el set
         
        String sql = "select ca.control_asistencia, ca.CONSECUTIVO, ca.DESCRIPMAT_CONSECUTIVO, ca.DESCRIPMAT_CODIGO,fprebus_descrip_mat(ca.descripmat_consecutivo,ca.descripmat_codigo,'03',''), "
                + " ca.CURSO_ACTU_GRUPO, ca.ESTADO, ca.TIPO_CONFIGURACION,ca.ESCALA,  FPREBUS_MATERIAS(ca.descripmat_codigo,'04','') as depto, ca.regla_matematicas, ca.regla_quiz from TNTP_CURSOS ca,TNTP_PFE_CURSOS pfe "
                + "where "
                + "pfe.cedula_profesor='"
                + cedula + "' and "
                + "pfe.consec_curso=ca.consecutivo ";
              //  + "and FPREBUS_MATERIAS(ca.descripmat_codigo,'04','')=08"; // OJO ADICIONADO
       // System.out.println("\n\n\n********************************Consulta de los cursos del profe******************"+sql);
        Statement stm = null;
        ResultSet rs = null;
        
        
        //aqui validar si es la condicion viene true, de ser así
        //traer todas las materias del depto y agregarlas al array 
        //pero solo las de arriba tendran la bandera en true para poder validar luego
        //tambn se debe verificar si ese profesor ya enseña la materia q no se repita en el array
        
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                int consecCurso = rs.getInt("consecutivo");
                String codigoMateria = rs.getString("descripmat_codigo");
                String descripcionMateria = rs.getString(5);
                String grupo = rs.getString("curso_actu_grupo");
                String est = rs.getString("ESTADO");
                String tipo = rs.getString("tipo_configuracion");
                int materiaConsecutivo = rs.getInt("DESCRIPMAT_CONSECUTIVO");
                String controlAsistencia = rs.getString("control_asistencia");
                double escala = rs.getDouble("ESCALA");
                
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo,
                        codigoMateria, consecPeriodo, periodoAcademico,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                curso.setEscala(escala);                
                              
               
              String   regla_mate=rs.getString("REGLA_MATEMATICAS");
                 if(regla_mate==null){  regla_mate="N";}
                if(!regla_mate.equalsIgnoreCase("N")){
                    regla_mate="S";
                }    
                    curso.setControlReglaMate(regla_mate);
                    
                  
              String regla_quiz=rs.getString("REGLA_QUIZ");
                 
                 if(regla_quiz==null){ regla_quiz="N"; }
                if(!regla_quiz.equalsIgnoreCase("N")){
                    regla_quiz="S";
                }    
                    curso.setControlReglaQuiz(regla_quiz);                   
                    
                
                //adicionado por ffceballos  --se le adiciono ademas a la consulta FPREBUS_MATERIAS(ca.descripmat_codigo,'04','') as depto
                String depto=rs.getString("depto");
                curso.setDepartamento(depto);
                curso.setDicta(true);
                //fin ffceballos
                
                cursos.add(curso);
            }
            //ffceballos
            ControlRecursos.liberarRecursos(rs, stm);
            
            //System.out.println("YA AGREGÓ LOS CURSOS DE ÉL");
            if(coordina){
             //System.out.println("ENTRÓ A LA CONDICIÓN DE COORDINADOR");             
                String sqlMaterias="SELECT distinct c.descripmat_codigo matcod "
                        + "FROM tntp_cursos c   "
                        + "WHERE c.periodo_periodo_acad || c.periodo_consecutivo =fprebus_per_notp('0102','') "// fprebus_constantes ('002', '0304', '')" 
                        +" AND fprebus_materias (c.descripmat_codigo, '04', '') ='08'";
            //System.out.println("\n\n\n********************************Consulta de los cursos del depto******************"+sqlMaterias);
            stm = conexion.createStatement();
            rs = stm.executeQuery(sqlMaterias);
                
            int tamañoActual=cursos.size();
            
                while (rs.next()) {
                   
                    boolean agregar=true;
                    String codigoMateria = rs.getString("matcod");
                    for(int cont=0;cont<tamañoActual;cont++){
                    
                       Curso cursoTemporal=(Curso)cursos.get(cont);
                       if(cursoTemporal.getCodigoMateria().equals(codigoMateria)){
                       agregar=false;
                       } 
                    
                    }
                    
                    
                    if(agregar){
                        
                         Statement stm2 = null;
                         ResultSet rs2 = null;
                    
                        //seleccione un grupo de la materia (el menor) y agreguelo pero con dicta en false!
                        String consulta="SELECT  ca.CONTROL_ASISTENCIA, ca.CONSECUTIVO, ca.DESCRIPMAT_CONSECUTIVO, "
                                + "ca.DESCRIPMAT_CODIGO,fprebus_descrip_mat(ca.descripmat_consecutivo,ca.descripmat_codigo,'03','') AS DESCRIPCION, "
                                + "ca.CURSO_ACTU_GRUPO, ca.ESTADO, ca.TIPO_CONFIGURACION,ca.ESCALA,  "
                                + "FPREBUS_MATERIAS(ca.descripmat_codigo,'04','') as DEPTO, ca.regla_matematicas, ca.regla_quiz " 
                                + "FROM tntp_cursos ca " 
                                + "WHERE ca.periodo_periodo_acad || ca.periodo_consecutivo =fprebus_per_notp('0102','') "// fprebus_constantes ('002', '0304', '') "
                                + " AND fprebus_materias (ca.descripmat_codigo, '04', '') ='08'  and ca.DESCRIPMAT_CODIGO="+codigoMateria+"  and rownum<=1"
                                + " order by CA.CURSO_ACTU_GRUPO";
                        
                       // System.out.println("\n\n\n********** consulta del grupo menor! "+consulta );
                        
                         stm2 = conexion.createStatement();
                         rs2 = stm2.executeQuery(consulta);

                            while (rs2.next()) {
                                
                                int consecCurso2 = rs2.getInt("CONSECUTIVO");
                                String codigoMateria2 = rs2.getString("DESCRIPMAT_CODIGO");
                                String descripcionMateria2 = rs2.getString("DESCRIPCION");
                                String grupo2 = rs2.getString("CURSO_ACTU_GRUPO");
                                String est2 = rs2.getString("ESTADO");
                                String tipo2 = rs2.getString("TIPO_CONFIGURACION");
                                int materiaConsecutivo2 = rs2.getInt("DESCRIPMAT_CONSECUTIVO");
                                String controlAsistencia2 = rs2.getString("CONTROL_ASISTENCIA");
                                
                                double escala2 = rs2.getDouble("ESCALA");
                               

                                Curso curso2 = new Curso(consecCurso2, grupo2, materiaConsecutivo2,
                                        codigoMateria2, consecPeriodo, periodoAcademico,
                                        descripcionMateria2, est2, tipo2);
                                curso2.setControlAsistencia(controlAsistencia2);
                                curso2.setEscala(escala2);
                                curso2.setControlReglaMate(rs2.getString("regla_matematicas"));
                                //ffceballos 30/01/2014
                                curso2.setControlReglaQuiz(rs2.getString("regla_quiz"));

                                //adicionado por ffceballos  --se le adiciono ademas a la consulta FPREBUS_MATERIAS(ca.descripmat_codigo,'04','') as depto
                                String depto2=rs2.getString("DEPTO");
                               
                                curso2.setDepartamento(depto2);
                                curso2.setDicta(false);
                                //fin ffceballos

                                cursos.add(curso2); 

                                
                            }

                                ControlRecursos.liberarRecursos(rs2, stm2);

                                }//fin if agregar
                   
                    
                } ControlRecursos.liberarRecursos(rs, stm);
                
            } //fin ffceballos
            
            
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            System.out.println("Cédula del profesor: "+ this.cedula);
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Invoca los métodos requeridos para cargar la información de todos los
     * cursos del profesor.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param profesores
     *            Objeto ControlProfesores
     */
    public void cargarCurso(Connection conexion, ControlProfesores profesores) {
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = (Curso) cursos.get(i);
            curso.actualizarMatriculasBd(conexion);
            if(curso.isDicta()){
            curso.cargarAlumnos(conexion);
            }
            curso.cargarClasificacionesBd(conexion, profesores);
            curso.cargarCategorias(conexion, profesores);
              if(curso.isDicta()){
            curso.cargarTotalHoras(conexion);
              }
            //ffceballos
          //  curso.setDicta(true);
            //fin ffceballos
        }
    }

    /**
     * Este método se utiliza en la parte de copia esquema unicamente. Su
     * proposito es cargar un curso que ya posea categorias y actividades
     * asignadas. Aunque puede ser utilizado en otros contextos por ahora se usa
     * con cursos de periodos anteriores al actual. Observar que en la consulta
     * no se tiene en cuenta el grupo del curso, por lo cual se escoge el
     * primero que satisfaga las condiciones
     *
     * @param conexion
     * @param descripmat_consecutivo
     * @param descripmat_codigo
     * @param periodoAcad
     * @param consec_periodo
     * @return
     */
    public Curso cargarUnCurso(Connection conexion, int descripmat_consecutivo,
            String descripmat_codigo, String periodoAcad, int consec_periodo) {

        String sql = "select distinct cur.consecutivo, cur.curso_actu_grupo, cur.control_asistencia,"
                + "fprebus_descrip_mat(cur.descripmat_consecutivo,cur.descripmat_codigo,'03',''),"
                + "cur.estado,cur.TIPO_CONFIGURACION, cur.escala "
                + "from tntp_cursos cur, tntp_clasif_categ cla, tbas_cursos_profesor pfe_cur "
                + "where cur.DESCRIPMAT_CODIGO='"
                + descripmat_codigo
                + "' "
                + "and cur.descripmat_consecutivo="
                + descripmat_consecutivo
                + " "
                + "and cur.periodo_periodo_acad='"
                + periodoAcad
                + "' "
                + "and cur.periodo_consecutivo="
                + consec_periodo
                + " "
                + " and cla.consec_curso=cur.consecutivo" //adicionado ffceballos (todo lo de pfe_cur)
               // + " and pfe_cur.consec_curso=cur.consecutivo"
                + " and pfe_cur.profesor_cedula='"
                +cedula                
                + "'"
                + " and pfe_cur.descripmat_codigo = cur.descripmat_codigo "
                + " and pfe_cur.curso_actu_grupo = cur.curso_actu_grupo "
                + " and pfe_cur.periodo_periodo_acad = cur.periodo_periodo_acad "
                + " and pfe_cur.periodo_consecutivo = cur.periodo_consecutivo";
        
        System.out.println("*****************************"+sql+"*******************************");
        
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                int consecCurso = rs.getInt("consecutivo");
                String codigoMateria = descripmat_codigo;
                String descripcionMateria = rs.getString(4);
                String grupo = rs.getString("curso_actu_grupo");
                String est = rs.getString("estado");
                String tipo = rs.getString("TIPO_CONFIGURACION");
                int materiaConsecutivo = descripmat_consecutivo;
                String controlAsistencia = rs.getString("control_asistencia");
                double escala = rs.getDouble("escala");
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo,
                        codigoMateria, consec_periodo, periodoAcad,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                curso.setEscala(escala);
                ControlRecursos.liberarRecursos(rs, stm);
                return curso;
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando un curso " + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        }
    }

    /**
     * Obtiene desde la base de datos el e-mail del profesor.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void cargarMail(Connection conexion) {
        String sql = "select correo_elect from tbas_profesores where cedula='"
                + cedula + "'";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                correo = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando correo " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Método empleado en la versión Beta 1 de la aplicación. Se recomienda NO
     * UTILIZARLO, pero si en realidad lo necesita, por favor revisarlo primero.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Valor booleano
     */
    public boolean inicioClases(Connection conexion) {
        String sql = "";
        String resp = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                resp = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error en función inicio de clases "
                    + e.getMessage());
            e.printStackTrace();
        }
        // ya no puede modificar
        ControlRecursos.liberarRecursos(rs, stm);
        return resp.equals("S");
    }

    /**
     * Carga el departamento académico al cual pertenece el profesor.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param cedula
     *            Cédula del profesor
     * @param periodo
     *            Periodo académico
     * @param consecutivo
     *            Consecutivo del periodo académico.
     * @return Departamento académico del profesor.
     */
    public String cargarDeptoAcadémico(Connection conexion, String cedula,
            String periodo, int consecutivo) {
        Statement stm = null;
        ResultSet rs = null;

        try {
            stm = conexion.createStatement();
            String sql = "select fprebus_pfe_depto_ctr('" + cedula + "','"
                    + periodo + "'," + consecutivo + ") from dual";
            rs = stm.executeQuery(sql);
            rs.next();
            String depto = rs.getString(1);
            ControlRecursos.liberarRecursos(rs, stm);
            return depto;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando departamento académico "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        }
    }

    /**
     * Llama a un procedimiento almacenado en la base de datos que actualiza los
     * cursos en tntp_cursos a partir de los datos en tbas_cursos.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarCursosBd(Connection conexion) {
        CallableStatement stm = null;
        try {
            stm = conexion.prepareCall("{call siaepre.ppreact_tntp_cursos(?)}");
            // System.out.println("------Procedimiento de actualizacion de
            // cursos -------");
            stm.setString(1, this.cedula);
            stm.execute();
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando cursos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Actualiza la tabla de TNTP_PFE_CURSOS a partir de la información
     * registrada en el sistema de pregrado.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarProfesorCursosBd(Connection conexion) {
        CallableStatement stm = null;
        try {
            stm = conexion.prepareCall("{call siaepre.ppreact_tntp_pfe_cursos(?)}");
            // System.out.println("------Procedimiento de actualizacion de
            // profesores cursos -------");

            stm.setString(1, this.cedula);
            stm.execute();
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando cursos profesor "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }
}
