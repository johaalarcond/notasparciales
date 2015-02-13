/*
 * Created on 12-feb-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.jefes.control;

import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.ControlRecursos;
import co.edu.icesi.notas.jefes.DeptoJefes;

/**
 * Similar a la clase de ControlProfesores. Se usa en el módulo de jefes
 * únicamente.
 * 
 * @author lmdiaz
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ControlJefe {

    private int consecutivoPeriodo;
    private String periodoAcademico;
    private Profesor jefe;
    private Map deptosCursos;

    public Profesor getJefe() {
        return jefe;
    }

    public void setJefe(Profesor jefe) {
        this.jefe = jefe;
    }

    // en esta colección se guardan los cursos que tiene matriculados un alumno.
    public ControlJefe() {
        super();
        this.deptosCursos = new HashMap();
    }

    /**
     * Método que se utiliza para obtener la cédula o identificación del usuario
     * único.
     *
     * @param user
     * @param conexion
     * @return
     */
    public String obtenerUsuarioUnico(String user, Connection conexion) {
        String sql = "select fintbus_usuario_portal('" + user
                + "','08') from dual";
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

    
    /**
     * Método que se utiliza para obtener la cédula o identificación del usuario
     * único.
     *
     * @param user
     * @param conexion
     * @return
     */
    public String obtenerUsuarioUnico2(String user, Connection conexion) {
        String sql = "select fintbus_usuario_portal('" + user
                + "','08') from dual";
        String usuario = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                usuario = rs.getString(1);
            }
        
        if(usuario.equalsIgnoreCase("0")){
        sql = "select fintbus_usuario_portal('" + user
                + "','04') from dual";
        
        stm = null;
        rs = null;
        
         stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                usuario = rs.getString(1);
            }
        
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

    
    /**
     * Obtiene el curso a partir de la identificación que se envía como
     * parámetro. EL curso se obtiene desde el arreglo de cursos que tiene la
     * clase.
     *
     * @param id
     * @return
     */
    public Curso getCurso(String id) {
        StringTokenizer token = new StringTokenizer(id, "-");
        String codMateria = token.nextToken();
        String grupo = token.nextToken();
        Set deptos = this.deptosCursos.keySet();
        for (Iterator it = deptos.iterator(); it.hasNext();) {
            DeptoJefes depto = (DeptoJefes) this.deptosCursos.get(it.next());
            ArrayList cursos = depto.getCursos();
            for (int i = 0; i < cursos.size(); i++) {
                Curso curso = (Curso) cursos.get(i);
                if (curso.getCodigoMateria().equals(codMateria)
                        && curso.getGrupo().equals(grupo)) {
                    return curso;
                }
            }
        }
        return null;
    }

    /**
     * Carga la información sobre el jefe de departamento a partir de su cédula.
     * Obtiene además el Consecutivo del periodo y carga los cursos del jefe.
     *
     * @param conexion
     * @param cedula
     * @param depto
     */
    public void cargarInformacion(Connection conexion, String cedula) {
        cargarConsecutivoPeriodo(conexion);
        cargarJefe(cedula, conexion);
        // jefe.setDeptosAcademicos(deptos);
        // cargarCursos(conexion);
    }

    /**
     * Carga específicamente la información del jefe de departamento
     *
     * @param cedula
     * @param conexion
     */
    private void cargarJefe(String cedula, Connection conexion) {
        // la consulta por ahora solo carga el nombre, la idea es irla
        // modificando
        // en la medida que se requieran más cosas.
        String sql = "select fprebus_alumnos('" + cedula
                + "','1005',' ') nombre  from dual";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                Profesor pro = new Profesor();
                jefe = pro;
                jefe.setCedula(cedula);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando jefe " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /***************************************************************************
     * Este método carga los cursos asociados a los departamentos académicos del
     * usuario.
     **************************************************************************/
    public void cargarCursos(Connection conexion, String criterioBusqueda) {
        String sql = "SELECT c.consecutivo, c.control_asistencia, c.descripmat_codigo matcod, c.descripmat_consecutivo matcon, "
                + "         fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '') descripcion, "
                + "         c.curso_actu_grupo grupo, c.estado, c.tipo_configuracion, "
                + "         fprebus_materias (c.descripmat_codigo, '04', '') depto "
                + "    FROM tntp_cursos c "
                + "   WHERE c.periodo_periodo_acad || c.periodo_consecutivo = fprebus_per_notp('0102','') "//fprebus_constantes ('002', '0304', '') "
                + "     AND (c.descripmat_codigo LIKE '"
                + criterioBusqueda
                + "' || '%' "
                + "       OR lower(fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '')) LIKE "
                + "             '%'||lower('"
                + criterioBusqueda
                + "') || '%') "
                + "     AND fprebus_materias (c.descripmat_codigo, '04', '') IN "
                + "               (SELECT dp.codigo codigo "
                + "                  FROM tbas_deptos_acad dp, empleado e "
                + "                 WHERE e.cedula = '"
                + jefe.getCedula()
                + "' "
                + "                   AND (dp.usuario = e.usuario "
                + "                     OR EXISTS (SELECT 's' "
                + "                                  FROM rol_usuarios ru "
                + "                                 WHERE ru.nombre_usuario = e.usuario "
                + "                                   AND ru.nombre_rol = 'SECGEN'))) "
                + "ORDER BY fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', ''), c.descripmat_codigo, c.curso_actu_grupo ";
       // System.out.println("*********"+sql);
        Statement stm = null;
        ResultSet rs = null;

        for (Iterator iter = this.deptosCursos.keySet().iterator(); iter.hasNext();) {
            Object key = iter.next();
            DeptoJefes depto = (DeptoJefes) this.deptosCursos.get(key);
            depto.getCursos().clear();
        }
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                DeptoJefes depto = (DeptoJefes) this.deptosCursos.get(rs.getString("depto"));
                int consecCurso = rs.getInt("consecutivo");
                String codigoMateria = rs.getString("matcod");
                String descripcionMateria = rs.getString("descripcion");
                String grupo = rs.getString("grupo");
                int materiaConsecutivo = rs.getInt("matcon");
                String est = rs.getString("ESTADO");
                String tipo = rs.getString("tipo_configuracion");
                String controlAsistencia = rs.getString("CONTROL_ASISTENCIA");
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo,
                        codigoMateria, consecutivoPeriodo, periodoAcademico,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                //modificacion bogomez
                 curso.cargarTotalHoras(conexion);
                 curso.getHorasPerdida(80);
             
                 
                depto.getCursos().add(curso);
                
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Carga específicamente el consecutivo del periodo
     *
     * @param conexion
     */
    private void cargarConsecutivoPeriodo(Connection conexion) {
     
        String sql = "select fprebus_per_notp('01',' ') periodo, fprebus_per_notp('02',' ') consecutivo  from dual";
        //   String sql = "select c.VALOR_VARCHAR2 periodo, c.VALOR_NUMERICO consecutivo from tbas_constantes c where c.CODIGO=002";
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

    /**
     * Valida que la cédula entregada corresponda efectivamente a un jefe de
     * departamento.
     *
     * @param conexion
     * @param cedula
     * @return
     */
    public void validarJefeDepto(Connection conexion, String cedula) {
        String sql = "SELECT dp.codigo codigo"
                + "  FROM tbas_deptos_acad dp, empleado e "
                + " WHERE e.cedula = '" + cedula + "' "
                + "   AND (dp.usuario = e.usuario "
                + "     OR EXISTS (SELECT 's' "
                + "                   FROM rol_usuarios ru "
                + "                  WHERE ru.nombre_usuario = e.usuario "
                + "                    AND ru.nombre_rol = 'SECGEN')) ";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            /*Cambio if*/
            while (rs.next()) {
                String codigoDepto = rs.getString("codigo");
                DeptoJefes depto = new DeptoJefes();
                depto.setCodigo(codigoDepto);
                depto.setNombre(obtenerNombreDepto(codigoDepto, conexion));
                this.deptosCursos.put(codigoDepto, depto);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error validando el usuario " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    private String obtenerNombreDepto(String codigo, Connection conexion) {
        String sql = "select descripcion " + "from tbas_deptos_acad "
                + "where codigo='" + codigo + "'";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                String descripcion = rs.getString("descripcion");
                ControlRecursos.liberarRecursos(rs, stm);
                return descripcion;
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el nombre del departamento "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return null;
    }

    public Map getDeptosUsuario() {
        return this.deptosCursos;
    }
}
