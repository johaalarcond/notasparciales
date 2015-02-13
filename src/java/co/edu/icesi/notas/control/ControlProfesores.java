package co.edu.icesi.notas.control;

import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.*;

/**
 * Clase que contiene al Profesor que actualmente está trabajando contra la
 * aplicación. Además, contiene algunas de consultas principales de la
 * aplicación, como lo son la recuperar el periodo académico o la de validar el
 * usuario único.
 * 
 * @author lmdiaz, mzapata
 */
public class ControlProfesores {

    private int consecutivo;
    private String periodoAcademico;
    private String periodoAnterior;
    private int consecutivoAnterior;
    private Profesor profesor;
    private ArrayList tiposCategoria = new ArrayList();

    /**
     * Obtiene el usuario único a partir del parámetro 'user'.
     *
     * @param user
     *            Usuario a validar
     * @param conexion
     *            Conexión a la base de datos.
     * @return Usuario único
     */
    public String obtenerUsuarioUnico(String user, Connection conexion) {
        String sql = "select fintbus_usuario_portal('" + user
                + "','8') from dual";
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

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public void setPeriodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setTiposCategoria(ArrayList tiposCalificacion) {
        this.tiposCategoria = tiposCalificacion;
    }

    public ArrayList getTiposCategoria() {
        return tiposCategoria;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public String getPeriodoAcademico() {
        return periodoAcademico;
    }

    /**
     * Obtiene el periodo académico y el consecutivo del periodo académico
     * actuales, y los asigna a los atributos respectivos de este objeto.
     *
     * @param conexion
     *            Conexión a la base de datos.
     */
    public void cargarConsecutivoPeriodo(Connection conexion) {
        String sql = "select fprebus_per_notp('01',' ') periodo, fprebus_per_notp('02',' ') consecutivo  from dual";
        //String sql="select c.VALOR_VARCHAR2 periodo, c.VALOR_NUMERICO consecutivo from tbas_constantes c where c.CODIGO=002";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                consecutivo = rs.getInt("consecutivo");
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

    /**
     * Invoca los métodos que se encargan de cargar la información requerida al
     * iniciar la aplicación.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @param cedula
     *            Cédula del profesor
     */
    public void cargarTodo(Connection conexion, String cedula, boolean coordina) {
        profesor = new Profesor();
        profesor.setCedula(cedula);
        profesor.cargarMail(conexion);
        cargarConsecutivoPeriodo(conexion);
        obtenerPeriodoAnterior(conexion);
        profesor.cargarDeptoAcadémico(conexion, cedula, periodoAcademico,
                consecutivo);
        cargarTiposCategoria(conexion);
        profesor.actualizarCursosBd(conexion);
        profesor.actualizarProfesorCursosBd(conexion);
        //ffceballos:
        //se le agrego el boolean coordina tanto al metodo cargar todo como al cargar curso de profesor
        profesor.cargarCursos(conexion, consecutivo, periodoAcademico, coordina);
        profesor.cargarCurso(conexion, this);
        //para saber cuales categorías se bloquean despues de los 15 días de haber ingresado nota
        

    }
    
       

    /**
     * Recupera todos los tipos de categoría registrados en la base de datos, y
     * los agrega a la colección respectiva.
     *
     * @param conexion
     *            Conexión a la base de datos.
     */
    public void cargarTiposCategoria(Connection conexion) {
        String sql = "select consecutivo, dpto_academico, nombre, estado from tntp_tipo_categoria";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                int cons = rs.getInt("consecutivo");
                String nombre = rs.getString("nombre");
                String dpto = rs.getString("dpto_academico");
                String estado = rs.getString("estado");
                TipoCategoria tipo = new TipoCategoria(cons, nombre, dpto,
                        estado);
                tiposCategoria.add(tipo);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando tipos de categoría "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Carga los tipos de categoria de la base de datos, que correspondan al
     * departamento académico por parámetro.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @param dptoAcad
     *            Departamento académico
     */
    public void cargarTiposCategoria(Connection conexion, String dptoAcad) {
        tiposCategoria.clear();
        String sql = "select consecutivo, nombre, estado from tntp_tipo_categoria where dpto_academico='"
                + dptoAcad + "' and estado='A'";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                int cons = rs.getInt("consecutivo");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                TipoCategoria tipo = new TipoCategoria(cons, nombre, dptoAcad,
                        estado);
                tiposCategoria.add(tipo);
            }
            
            //ffceballos
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando tipos de categoría con sobrecarga por departamento "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Retorna el curso que coincida con el parámetro.
     *
     * @param id
     *            Código de la materia concatenado con el número del grupo.
     * @return Objeto de tipo Curso
     */
    public Curso getCurso(String id) {
        StringTokenizer token = new StringTokenizer(id, "-");
        String codMateria = token.nextToken();
        String grupo = token.nextToken();
        
        for (int i = 0; i < profesor.getCursos().size(); i++) {
            Curso curso = (Curso) profesor.getCursos().get(i);
            if (curso.getCodigoMateria().equals(codMateria)
                    && curso.getGrupo().equals(grupo)) {
                              
                return curso;
            }
        }
        return null;
    }

    /**
     * Retorna el tipo de categoría que coincida con el consecutivo por
     * parámetro
     *
     * @param consecutivo
     *            Consecutivo
     * @return TipoCategoria
     */
    public TipoCategoria getTipoCategoria(int consecutivo) {
        for (int i = 0; i < tiposCategoria.size(); i++) {
            TipoCategoria tc = (TipoCategoria) tiposCategoria.get(i);
            if (tc.getConsecutivo() == consecutivo) {
                return tc;
            }
        }
        return null;
    }

    /**
     * Retorna la fecha límite de cierre de cursos.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @return Fecha límite de cierre
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

    /**
     * Obtiene de la base de datos la fecha de inicio de cierre de los cursos.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @return Fecha de inicio de cierre
     */
    public java.util.Date obtenerFechaInicioCierre(Connection conexion) {
        /*
         * Se hizo un cambio de constante de 033 a 147, la constante 147
         * determina el numero de días que hay entre el fin de las clases y la
         * fecha limite de entrega de notas por parte de los profesores.
         */
        java.util.Date date = null;
        String sql = "select to_date(to_date(fprebus_constantes('033','03',NULL),'dd-mm-rrrr')-fprebus_constantes('147','04',NULL),'dd-mm-rrrr') from dual";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                date = rs.getDate(1);
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return date;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo la fecha de inicio de cierre "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return null;
    }

    /**
     * Retorna la fecha de inicio de clases.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @return Fecha de inicio de clases
     */
    public java.util.Date obtenerFechaInicioClases(Connection conexion) {
        java.util.Date date = null;
        String sql = "SELECT to_date(fprebus_constantes('032','03',NULL),'dd-mm-yyyy') FROM dual";
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
            System.out.println("Error obteniendo la fecha de inicio de clases "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return null;
    }

    /**
     * Retorna la fecha actual.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @return Fecha de inicio de clases
     */
    public java.util.Date obtenerFechaActual(Connection conexion) {
        java.util.Date date = null;
        String sql = "SELECT to_date(to_char(sysdate,'dd-mm-yyyy'),'dd-mm-yyyy') FROM dual";
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
            System.out.println("Error obteniendo la fecha actual "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return null;
    }

    /**
     * Retorna el periodo anterior al periodo enviado por par&aacute;metro
     *
     * @param java
     *            .sql.Connection conexion Conexi&oacute;n a la base de datos
     */
    public void obtenerPeriodoAnterior(Connection conexion) {
        String periodoAnterior = "";
        String sql = "SELECT FPRECAL_PERIODO_ANTERIOR('"
                + this.periodoAcademico + "') FROM dual";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                periodoAnterior = rs.getString(1);
                ControlRecursos.liberarRecursos(rs, stm);
                this.periodoAnterior = periodoAnterior;
                int periodo = Integer.parseInt(this.periodoAcademico);
                int consecAnterior = 0;
                if (periodo % 2 == 0) {
                    consecAnterior = 1;
                } else {
                    consecAnterior = 2;
                }
                this.consecutivoAnterior = consecAnterior;
            }
            ControlRecursos.liberarRecursos(rs, stm);
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo el periodo anterior al periodo "
                    + this.periodoAcademico + " " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Retorna el periodo anterior al periodo acad&eacute;mico actual.
     *
     * @return String periodoAnterior Periodo anterior
     */
    public String getPeriodoAnterior() {
        return periodoAnterior;
    }

    /**
     * Retorna el consecutivo del periodo anterior al periodo acad&eacute;mico
     * actual.
     *
     * @return int consecutivoAnterior Consecutivo anterior
     */
    public int getConsecutivoAnterior() {
        return consecutivoAnterior;
    }
   
}
