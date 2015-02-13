/*
 * Created on 7/07/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.estudiante.control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import co.edu.icesi.notas.Alumno;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.control.ControlRecursos;

/**
 * Similar a la clase ControlProfesores, con la diferencia de que esta se emplea
 * para el módulo de estudiantes.
 * 
 * @author lmdiaz, mzapata
 */
public class ControlEstudiante {

    private int consecutivoPeriodo;
    private String periodoAcademico;
    private Alumno alumno;
    // en esta colección se guardan los cursos que tiene matriculados un alumno.
    private ArrayList cursos;

    public ControlEstudiante() {
        super();
        cursos = new ArrayList();
    }

    public String obtenerUsuarioUnico(String user, Connection conexion) {
        String sql = "select fintbus_usuario_portal('" + user
                + "','20') from dual";
        String usuario = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                usuario = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo el usuario único "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return usuario;
    }

    public Curso getCurso(String id) {
        StringTokenizer token = new StringTokenizer(id, "-");
        String codMateria = token.nextToken();
        String grupo = token.nextToken();
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = (Curso) cursos.get(i);
            if (curso.getCodigoMateria().equals(codMateria)
                    && curso.getGrupo().equals(grupo)) {
                return curso;
            }
        }
        return null;
    }

    public void cargarInformacion(Connection conexion, String codigo) {
        cargarConsecutivoPeriodo(conexion);
        cargarAlumno(codigo, conexion);
        cargarCursos(conexion);
    }

    private void cargarAlumno(String codigo, Connection conexion) {
        // la consulta por ahora solo carga el nombre, la idea es irla
        // modificando
        // en la medida que se requieran más cosas.
        String sql = "select fprebus_alumnos('" + codigo
                + "','1005',' ') nombre  from dual";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                Alumno al = new Alumno(codigo, nombre);
                alumno = al;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el alumno " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /***************************************************************************
     * Este método carga los cursos asociados a un estudiante.
     **************************************************************************/
    private void cargarCursos(Connection conexion) {
        String sql = "select cur.CONSECUTIVO, cur.CONTROL_ASISTENCIA, cur.DESCRIPMAT_CODIGO, cur.DESCRIPMAT_CONSECUTIVO, fprebus_descrip_mat(cur.DESCRIPMAT_CONSECUTIVO,cur.DESCRIPMAT_CODIGO,'03','') descripcion, cur.CURSO_ACTU_GRUPO, cur.ESTADO, cur.TIPO_CONFIGURACION "
                + " from tntp_matriculas mat,tntp_cursos cur"
                + " where codigo_alumno='"
                + alumno.getCodigo()
                + "' and mat.consec_curso=cur.consecutivo and estado_alumno='A'"
                + "and cur.periodo_periodo_acad = fprebus_per_notp('01','')"//fprebus_constantes('002', '03', '')"
                + "and cur.periodo_consecutivo = fprebus_per_notp('02','')"//fprebus_constantes('002', '04', '')"
                + "and exists ("
                + "            select 's'"
                + "              from tbas_matriculas m"
                + "             where m.descripmat_codigo = cur.descripmat_codigo"
                + "               and m.descripmat_consecutivo = cur.descripmat_consecutivo"
                + "               and m.alumno_codigo = mat.codigo_alumno"
                + "               and m.periodo_periodo_acad = fprebus_per_notp('01','')"//fprebus_constantes('002', '03', '')"
                + "               and m.periodo_consecutivo = fprebus_per_notp('02','')"//fprebus_constantes('002', '04', '')"
                + "               and (m.novedad is null or m.novedad != 'C')"
                + ")" + " order by descripcion";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                int consecCurso = rs.getInt("consecutivo");
                String codigoMateria = rs.getString("DESCRIPMAT_CODIGO");
                String descripcionMateria = rs.getString("descripcion");
                String grupo = rs.getString("CURSO_ACTU_GRUPO");
                int materiaConsecutivo = rs.getInt("DESCRIPMAT_CONSECUTIVO");
                String est = rs.getString("ESTADO");
                String tipo = rs.getString("tipo_configuracion");
                String controlAsistencia = rs.getString("CONTROL_ASISTENCIA");
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo,
                        codigoMateria, consecutivoPeriodo, periodoAcademico,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                /*Cambio bogomez*/
                 curso.cargarTotalHoras(conexion);
                 curso.getHorasPerdida(80);
                cursos.add(curso);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    private void cargarConsecutivoPeriodo(Connection conexion) {
        String sql = "select fprebus_per_notp('01',' ') periodo, fprebus_per_notp('02',' ') consecutivo  from dual";
        //String sql = "select c.VALOR_VARCHAR2 periodo, c.VALOR_NUMERICO consecutivo from tbas_constantes c where c.CODIGO=002";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                consecutivoPeriodo = rs.getInt("consecutivo");
                periodoAcademico = rs.getString("periodo");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el consecutivo y el periodo "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public ArrayList getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList cursos) {
        this.cursos = cursos;
    }

    public int getConsecutivo() {
        return consecutivoPeriodo;
    }

    public String getPeriodoAcademico() {
        return periodoAcademico;
    }

    /**
     * Obtiene la fecha límite de cierre del curso
     */
    public java.util.Date obtenerFechaLimiteCierre(Connection conexion) {
        java.util.Date date = null;
        String sql = "SELECT to_date(fprebus_constantes('119','03',NULL),'dd-mm-yyyy') FROM dual";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                date = rs.getDate(1);
                ControlRecursos.liberarRecursos(rs, stm);
                return date;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo la fecha límite de cierre "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return null;
    }
}
