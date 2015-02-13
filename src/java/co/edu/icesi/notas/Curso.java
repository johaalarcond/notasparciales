/*
 * Created on 02-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates 
 */
package co.edu.icesi.notas;

import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * Esta clase representa un curso en la aplicación de notas parciales. Además de
 * poseer la información básica del curso, también posee una colección con las
 * categorias del curso, y dos objetos Clasificación, uno para las categorías
 * individuales y otro para las categorías grupales.
 * 
 * @author lmdiaz, mzapata
 */
public class Curso implements java.io.Serializable {

    private int consecutivo;
    private String grupo;
    private int consecutivoMateria;
    private String codigoMateria;
    private int consecutivoPeriodo;
    private String periodoAcademico;
    private ArrayList categorias = new ArrayList();
    private ArrayList alumnos = new ArrayList();
    private String descripcionMateria;
    private Clasificacion individuales;
    private Clasificacion grupales;
    private String estado;
    private String tipoConfiguracion;
    private ArrayList programaciones;
    private String controlAsistencia;
    private double escala;
    private double totalHoras;
    private double totalHorasPermitidas;
    private double vMaxInasistencia;
    private String documento_profesor;
        
    //adicionado por ffceballos
    
    private String departamento;

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;    
    } 
    
    private ArrayList actividades;

    public ArrayList getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList actividades) {
        this.actividades = actividades;
    }
    
    private boolean dicta;   
   

    public boolean isDicta() {
        return dicta;
    }

    public void setDicta(boolean dicta) {
        this.dicta = dicta;
    }
    
 // fin ffceballos
    
      //jalarcon
    private String controlReglaMate;

    public String getControlReglaMate() {
        return controlReglaMate;
    }

    public void setControlReglaMate(String controlReglaMate) {
        this.controlReglaMate = controlReglaMate;
    }
//jalarconf
    
    //ffceballos 30/01/2014
    
     private String controlReglaQuiz;

    public String getControlReglaQuiz() {
        return controlReglaQuiz;
    }

    public void setControlReglaQuiz(String controlReglaQuiz) {
        this.controlReglaQuiz = controlReglaQuiz;
    }
    
    //fin ffceballos
    
    public Curso() {
        super();
    }

    
    
    public double getMaxInasistencia() {
        return vMaxInasistencia;
    }

    public void setMaxInasistencia(double vMaxInasistencia) {
        vMaxInasistencia= (this.totalHoras * (100 - 80) / 100);
        this.vMaxInasistencia = vMaxInasistencia;
    }
    
    
    /**
     * Este método se encarga de contar el total de actividades existentes mas
     * el total de Categorias activas. Este método se requiere para generar el
     * archivo en formato Excel.
     *
     * @param individual
     *            true si se desea contar el total de items (categorias +
     *            actividades) individuales; false si se desean contar los
     *            grupales.
     * @return Número total de items de la Clasificacion
     */
    public int getTotalItems(boolean individual) {
        Clasificacion agrupacion = individual ? individuales : grupales;
        int total = 0;
        ListIterator itera = agrupacion.getCategorias().listIterator();
        Categoria actual;
        while (itera.hasNext()) {
            actual = (Categoria) itera.next();
            if (!actual.isCancelado()) {
                total += (actual.getActividades().size() * 2);
                // total+=2;
            }
        }
        return total;
    }

    /**
     * Actualiza la fecha de cierre en la BD (TBAS_CURSOS_PROFESOR).
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarFechaCierre(Connection conexion) {
        Statement stm = null;
        try {
            String sql = "update tbas_cursos_profesor cp"
                    + "   set fecha_cierre_ntp = sysdate"
                    + " where (cp.periodo_periodo_acad, cp.periodo_consecutivo, cp.descripmat_codigo, cp.curso_actu_grupo) in ("
                    + "			                                                                                            select nc.periodo_periodo_acad, nc.periodo_consecutivo, nc.descripmat_codigo, nc.curso_actu_grupo"
                    + "                                                                                                       from tntp_cursos nc"
                    + "                                                                                                      where nc.periodo_periodo_acad = cp.periodo_periodo_acad"
                    + "                                                                                                        and nc.periodo_consecutivo = cp.periodo_consecutivo"
                    + "                                                                                                        and nc.consecutivo = "
                    + consecutivo + ")";
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando la fecha de cierre de curso "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Actualiza el atributo estado en la BD.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarEstado(Connection conexion) {
        Statement stm = null;
        try {
            String sql = "update tntp_cursos set estado='" + this.estado
                    + "' where consecutivo=" + consecutivo;
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando el estado " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    
    
    //jalarcon
    /**
     * Actualiza el atributo controlReglaMate en la BD.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarControlReglaMate(Connection conexion) {
        Statement stm = null;
        Statement stm2 = null;
        try {
            //ffceballos 15/01/2014 ==> estaba saliendo null al copiar esquema anterior! 
            String controlMate="N";
            if(this.controlReglaMate!=null){ 
            controlMate=this.controlReglaMate;
            }else{
            this.controlReglaMate="N";
            }
            String sql = "update tntp_cursos set regla_matematicas='"
                    + controlMate + "' where consecutivo="
                    + consecutivo;
            //fin ffceballos
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
            auditarControlAsistencia(conexion);
            
            if(this.controlReglaMate.equalsIgnoreCase("S")){
            
            String sql2="insert into tntp_reglas (MATERIA_CODIGO,DEPTO_ACADEMICO,NOTA_MINIMA) values ('"
                    +this.codigoMateria
                    +"','"
                    +this.departamento
                    +"',3.3)";
            
            stm2=conexion.createStatement();
            stm2.executeUpdate(sql2);            
            
            }else{
            
            String sql2="delete from tntp_reglas where "
                    +"MATERIA_CODIGO='"
                    +this.codigoMateria
                    +"' and DEPTO_ACADEMICO='"
                    +this.departamento
                    +"' and NOTA_MINIMA=3.3";
            
            stm2=conexion.createStatement();
            stm2.executeUpdate(sql2);
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando el control de matemáticas "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
        ControlRecursos.liberarRecursos(null, stm2);
    }
    //jalarcon
    
    
    public void actualizarControlReglaQuiz(Connection conexion) {
        Statement stm = null;        
        try {
           
            String controlQuiz="N";
            if(this.controlReglaQuiz!=null){ 
            controlQuiz=this.controlReglaQuiz;
            }else{
            this.controlReglaQuiz="N";
            }
            String sql = "update tntp_cursos set regla_quiz='"
                    + controlQuiz + "' where consecutivo="
                    + consecutivo;
            //fin ffceballos
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
            auditarControlAsistencia(conexion);
            
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando el control de la regla del quiz mas bajo "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }
    
    
    
    
    /**
     * Actualiza el atributo controlAsistencia en la BD.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarControlAsistencia(Connection conexion) {
        Statement stm = null;
        try {
            String sql = "update tntp_cursos set control_asistencia='"
                    + this.controlAsistencia + "' where consecutivo="
                    + consecutivo;
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
            auditarControlAsistencia(conexion);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando el control de la asistencia "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Actualiza el atributo escala en la BD.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarEscala(Connection conexion, double nuevaEscala) {
        Statement stm = null;
        try {
            if (nuevaEscala != this.getEscala()) {
                double antiguaEscala = this.getEscala();
                this.setEscala(nuevaEscala);
                String sql = "update tntp_cursos set escala="
                        + this.getEscala() + " where consecutivo="
                        + consecutivo;
                stm = conexion.createStatement();
                stm.executeUpdate(sql);
                auditarCambioEscala(conexion, antiguaEscala);
            }
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando la escala de la asistencia "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Regista en la tabla de auditorias, los cambios realizados en cuanto al
     * control de asistencia del curso (especificamente el campo
     * CONTROL_ASISTENCIA del curso respectivo).
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void auditarControlAsistencia(Connection conexion) {
        Statement stm = null;
        try {
            String datoAnterior = this.controlAsistencia.equals("S") ? "N"
                    : "S";
            String accion = "Modificación del control de asistencia del curso";
            String sql = "insert into tntp_auditorias(fecha_hora,dato_anterior,dato_nuevo,accion,consec_curso, consecutivo)";
            sql += " values (sysdate,'"
                    + datoAnterior
                    + "','"
                    + this.controlAsistencia
                    + "','"
                    + accion
                    + "',"
                    + this.consecutivo
                    + ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error auditando cambio en el control de la asistencia "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Regista en la tabla de auditorias, los cambios realizados en cuanto a la
     * escala empleada en el curso
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void auditarCambioEscala(Connection conexion, double antiguaEscala) {
        Statement stm = null;
        try {
            String accion = "Modificación de la escala del curso";
            String sql = "insert into tntp_auditorias(fecha_hora,dato_anterior,dato_nuevo,accion,consec_curso, consecutivo)";
            sql += " values (sysdate,'"
                    + antiguaEscala
                    + "','"
                    + this.getEscala()
                    + "','"
                    + accion
                    + "',"
                    + this.consecutivo
                    + ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error auditando cambio de la escala en la asistencia "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Este método se encarga de realizar el cierre del curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return true si fue exitoso el cierre, false en caso contrario.
     */
    public boolean cerrarCurso(Connection conexion) {
        String mensajeAud = "Cierre de curso";
        try {// if(todasCalificaciones()){

            this.estado = "NA";
            /*******************************************************************
             * *for(int i=0;i<this.getCategorias().size();i++){
             * pg=((Categoria)this.getCategorias().get(i));
             * pg.setEstado(estado); pg.actualizarEstado(conexion,estado); }
             ******************************************************************/
            actualizarEstado(conexion);
            this.actualizarCambioCierreBd(conexion, mensajeAud, "", "", "");
            this.actualizarDefinitivas(conexion);
            auditarCambiosAsistencias(conexion);
            // aquí se llama al procedimiento que cierra el curso.
            int resp = this.cierre(conexion);
            //
            if (resp >= 0) {
                return true;
            } else {
                return false;
            }
            // }
            // return false;

        } catch (Exception e) {
            System.out.println("Error al cerrar el curso : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método se encarga de realizar el cierre del curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param deptoAcadProfesor
     *            Departamento académico al que pertenece el profesor.
     * @return Valor entero indicando si el cierre fue exitoso o no lo fue. -1.
     *         El curso se encuentra en estado Sincronizado o es No Piloto. -2.
     *         Existen notas registradas en tbas_matriculas. -3. Excepción >=0
     *         Número de filas actualizadas en tbas_matriculas
     */
    public int cerrarCurso(Connection conexion, String deptoAcadProfesor) {
        String mensajeAud = "Cierre de curso";
        try {
            this.estado = "NA";
            this.actualizarEstado(conexion);
            this.actualizarFechaCierre(conexion);
            this.actualizarCambioCierreBd(conexion, mensajeAud, "", "", "");
            this.actualizarDefinitivas(conexion, deptoAcadProfesor);
            auditarCambiosAsistencias(conexion);
            // aquí se llama al procedimiento que cierra el curso.
            int resp = this.cierre(conexion);

            return resp;
            // }
            // return false;

        } catch (Exception e) {
            System.out.println("Error al cerrar el curso : " + e.getMessage());
            e.printStackTrace();
            return -3;
        }
    }

    /**
     * Registra en la tabla de auditorias si algún porcentaje de asistencia fue
     * modificado.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void auditarCambiosAsistencias(Connection conexion) {
        Statement stm = null;
        Statement stm2 = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            for (int i = 0; i < alumnos.size(); i++) {
                Alumno alumno = (Alumno) alumnos.get(i);
                String sql = "select fecha_perd_asist from tntp_matriculas where consec_curso="
                        + consecutivo
                        + " and codigo_alumno='"
                        + alumno.getCodigo() + "'";
                rs = stm.executeQuery(sql);
                if (rs.next()) {
                    if (rs.getDate(1) != null) {
                        if (alumno.calcularAsistenciaTotal(conexion, this) >= 80) {
                            stm2 = conexion.createStatement();
                            String accion = "Ocurrió una modificación en el registro de asistencias que ha provocado que el estudiante supere el límite de asistencia";
                            String sql2 = "insert into tntp_auditorias (alumno_codigo,fecha_hora,accion,consec_curso, consecutivo) values('"
                                    + alumno.getCodigo()
                                    + "',sysdate,'"
                                    + accion
                                    + "',"
                                    + consecutivo
                                    + ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
                            stm2.executeUpdate(sql2);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error auditando cambios en la asistencia "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        ControlRecursos.liberarRecursos(null, stm2);
    }

    /**
     * Este método determina si para todos los alumnos del curso, en todas las
     * actividades, existe una nota asociada.
     *
     * @return true si para todos los alumnos existe una nota asociada.
     */
    public boolean todasCalificaciones() {
        for (int i = 0; i < this.getCategorias().size(); i++) {
            Categoria pg = (Categoria) this.getCategorias().get(i);
            if (!pg.isCancelado()) {
                if (pg.getActividades().isEmpty()) {
                    return false;
                }
                for (int j = 0; j < pg.getActividades().size(); j++) {
                    Actividad est = (Actividad) pg.getActividades().get(j);
                    for (int k = 0; k < this.getAlumnos().size(); k++) {
                        Alumno alumno = (Alumno) this.getAlumnos().get(k);
                        if (alumno.getEstado().equals("A")) {
                            Matricula mat = est.getMatricula(alumno.getCodigo());
                            if (mat == null || !mat.isExisteBd()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Busca, entre los alumnos del curso, el estudiante que tenga el mismo
     * código que viene por parámetro.
     *
     * @param codigo
     *            Código del alumno a buscar.
     * @return Alumno que corresponde con el código, o null si no se encuentra.
     */
    public Alumno getAlumno(String codigo) {
        ListIterator iterador = alumnos.listIterator();
        Alumno retorno = new Alumno();
        Object obj;
        if (codigo != null) {
            while (iterador.hasNext()) {
                obj = iterador.next();
                if (obj instanceof Alumno) {
                    retorno = (Alumno) obj;
                    if (retorno.igualCodigo(codigo)) {
                        return retorno;
                    }
                }
            }
        }
        return retorno;
    }

    public void setCategorias(ArrayList padresGrupos) {
        this.categorias = padresGrupos;
    }

    /**
     * Determina cuántas actividades tiene la categoría que más actividades
     * tiene.
     *
     * @return Número de actividades tiene la categoría que más actividades
     *         tiene
     */
    public int sizeMayor() {
        int mayor = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getActividades().size() > mayor) {
                mayor = pg.getActividades().size();
            }
        }
        return mayor;
    }

    public ArrayList getCategorias() {
        return categorias;
    }

    public String getNombre() {
        return descripcionMateria;
    }

    /**
     * Retorna el atributo codigoMateria concatenado mediante un guión con el
     * atributo grupo.
     *
     * @return codigoMateria-grupo
     */
    public String getId() {
        return codigoMateria + "-" + grupo;
    }

    public String getDescripcionMateria() {
        return descripcionMateria;
    }

    public ArrayList getAlumnos() {
        return alumnos;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public int getConsecutivoPeriodo() {
        return consecutivoPeriodo;
    }

    public int getConsecutivoMateria() {
        return consecutivoMateria;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getPeriodoAcademico() {
        return periodoAcademico;
    }

    //ffceballos 
    
    public Curso(int consecutivoMateria,String codigoMateria,String descripcionMateria, String grupo){
      this.codigoMateria = codigoMateria;
        this.consecutivoMateria = consecutivoMateria;
     this.descripcionMateria = descripcionMateria;
     this.grupo=grupo;
    }
    //fin ffceballos
    public Curso(int consecutivo, String grupo, int consecutivoMateria,
            String codigoMateria, int consecutivoPeriodo,
            String periodoAcademico, String descripcionMateria, String estado,
            String tipo) {
        this.consecutivo = consecutivo;
        this.grupo = grupo;
        this.consecutivoMateria = consecutivoMateria;
        this.codigoMateria = codigoMateria;
        this.consecutivoPeriodo = consecutivoPeriodo;
        this.periodoAcademico = periodoAcademico;
        this.descripcionMateria = descripcionMateria;
        this.estado = estado;
        this.tipoConfiguracion = tipo;
        alumnos = new ArrayList();
        categorias = new ArrayList();
        individuales = new Clasificacion();
        grupales = new Clasificacion();
        actividades= new ArrayList();
    }

    /**
     * Obtiene la lista de actividades asociada a la categoría cuyo tipo de
     * categoría sea la que viene por parámetro.
     *
     * @param tipoC
     *            Tipo de Categoría
     * @return Lista de actividades
     */
    public List getActividad(TipoCategoria tipoC) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getTipoCategoria().equals(tipoC)) {
                return pg.getActividades();
            }
        }
        return new ArrayList();
    }

    /**
     * Obtiene la categoría cuyo tipo de categoría coincide con el parámetro.
     *
     * @param tipoC
     *            Tipo de categoría
     * @return Categoría
     */
    public Categoria getCategoria(TipoCategoria tipoC) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getTipoCategoria().equals(tipoC)) {
                return pg;
            }
        }
        return null;
    }

    /**
     * Carga desde la base de datos, los alumnos que actualmente se encuentran
     * matriculados en el curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void cargarAlumnos(Connection conexion) {
        // Hace falta incluir el grupo        
        String sql = "select mt.codigo_alumno,fprebus_alumnos(mt.codigo_alumno,'1005',' ') nombre, "        
                + "mt.ESTADO_ALUMNO from tntp_matriculas mt where mt.consec_curso="
                + this.consecutivo
                + " and mt.estado_alumno='A' order by NLSSORT(nombre,'NLS_SORT=SPANISH')";
        Statement stm = null;
        ResultSet rs = null;
       
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                
               
                String codigo = rs.getString("codigo_alumno");
               
                //ffceballos 16/08/2013
                String nombre="";
                if(rs.getString("nombre")!=null && rs.getString("nombre")!=""){
                nombre = rs.getString("nombre");
                }else{
               //consultar los nombres de los externos (que no están en tbas_alumnos)
               
                    String sql2="select APELLIDOS, NOMBRE"
                            + " from tbas_personas_externas"
                            + " where NUMID='"
                            + codigo+"'";
                    Statement stm2 = conexion.createStatement();           
                    ResultSet  rs2 = stm2.executeQuery(sql2);
                    
                    String nombreInicial="";
                    String apellidos="";
                    while (rs2.next()) {
                     nombreInicial=rs2.getString("NOMBRE");                         
                     apellidos=rs2.getString("APELLIDOS");
                    }
                    
                    nombre =apellidos+" "+nombreInicial;
                    ControlRecursos.liberarRecursos(rs2, stm2);
                }             
                //fin ffceballos
                Alumno alumno = new Alumno(codigo, nombre);
                alumno.setEstado(rs.getString("ESTADO_ALUMNO"));
                alumno.cargarMails(conexion);
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando alumnos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        
    }

    /**
     * Retorna una de las dos clasificaciones (individual o grupal) de acuerdo
     * al consecutivo por parámetro.
     *
     * @param cons
     *            Consecutivo de la clasificación
     * @return Clasificación.
     */
    private Clasificacion getClasificacion(int cons) {
        return individuales.getConsecutivo() == cons ? individuales : (grupales.getConsecutivo() == cons ? grupales : null);
    }

    /**
     * Método invocado por el método cargarCategorias, para realizar la consulta
     * a la base de datos que permita recuperar las categorias asociadas al
     * curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param profesores
     *            Objeto ControlProfesores
     */
    private void cargarCategoriasBd(Connection conexion,
            ControlProfesores profesores) {
        String sql = "select cat.consecutivo, cat.porcentaje, cat.consec_tipo_categ,cat.activo,cat.consec_clasif_categ "
                + " from tntp_categorias cat,tntp_clasif_categ clas "
                + " where clas.consec_curso="
                + this.consecutivo
                + " and cat.consec_clasif_categ=clas.consecutivo";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            Clasificacion cla;
            while (rs.next()) {
                int cons = rs.getInt("consecutivo");
                double por = rs.getDouble("porcentaje");
                int consecTipo = rs.getInt("consec_tipo_categ");
                String est = rs.getString("activo");
                Categoria pg = new Categoria(cons, por);
                int consecClasif = rs.getInt("consec_clasif_categ");
                cla = this.getClasificacion(consecClasif);
                pg.setClasificacion(cla);
                cla.getCategorias().add(pg);

                // agregado por mzapata
                pg.setCopiaPorcentaje(por);

                pg.setTipoCategoria(profesores.getTipoCategoria(consecTipo));
                pg.setExisteBd(true);
                pg.setActivo(est);

                categorias.add(pg);

            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando categorías " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Método que se encarga de recuperar desde la base de datos, las
     * clasificaciones asociadas al curso respectivo.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param cp
     *            Objeto ControlProfesores
     */
    public void cargarClasificacionesBd(Connection conexion,
            ControlProfesores cp) {
        String sql = "select clas.consecutivo, clas.porcentaje, clas.tipo "
                + "from tntp_clasif_categ clas " + "where clas.consec_curso="
                + this.consecutivo;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                int cons = rs.getInt("consecutivo");
                double porcentaje = rs.getDouble("porcentaje");
                String tipo = rs.getString("tipo");
                if (tipo.equals("I")) {
                    individuales.setNombre("Evaluaciones individuales");
                    individuales.setExisteBD(true);
                    individuales.setPorcentaje(porcentaje);
                    individuales.setCopiaPorcentaje(porcentaje);
                    individuales.setConsecutivo(cons);
                    individuales.setCurso(this);
                    individuales.setTipo(tipo);
                    individuales.setCopiaTipo(tipo);
                } else {
                    grupales.setNombre("Evaluaciones grupales");
                    grupales.setExisteBD(true);
                    grupales.setPorcentaje(porcentaje);
                    grupales.setCopiaPorcentaje(porcentaje);
                    grupales.setConsecutivo(cons);
                    grupales.setCurso(this);
                    grupales.setTipo(tipo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando clasificaciones "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Carga, en caso de que las haya, las categorias asociadas al curso actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param profesores
     *            Objeto ControlProfesores
     */
    public void cargarCategorias(Connection conexion,
            ControlProfesores profesores) {
        cargarCategoriasBd(conexion, profesores);
        for (int i = 0; i < categorias.size(); i++) {
            Categoria cat = (Categoria) categorias.get(i);

            cat.cargarActividad(conexion);
            for (int j = 0; j < cat.getActividades().size(); j++) {
                Actividad est = (Actividad) cat.getActividades().get(j);
                for (int k = 0; k < alumnos.size(); k++) {
                    Matricula matricula = new Matricula(this, (Alumno) alumnos.get(k));
                    matricula.setActividad(est);
                    matricula.cargarCalificacion(conexion);
                    est.getMatriculas().add(matricula);
                }
            }
        }
    }

    /**
     * Busca una Actividad de acuerdo al consecutivo que llega como parámetro.
     *
     * @param cons
     *            Consecutivo de la actividad a buscar.
     * @return La Actividad asociada al consecutivo por parámetro.
     */
    public Actividad getActividad(int cons) {
        Actividad respuesta = null;
        for (int i = 0; i < categorias.size() && respuesta == null; i++) {
            Categoria pg = (Categoria) categorias.get(i);
            respuesta = pg.getActividad(cons);
        }
        return respuesta;
    }

    /**
     * Obtiene la Categoría para la cual el consecutivo de su tipo de categoría
     * coincide con el parámetro.
     *
     * @param cons
     *            Consecutivo del tipo de categoría
     * @return Categoría
     */
    public Categoria getCategoriaByTipo(int cons) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getTipoCategoria().getConsecutivo() == cons) {
                return pg;
            }
        }
        return null;
    }

    /**
     * Retorna la categoría cuyo consecutivo coincida con el número por
     * parámetro.
     *
     * @param cons
     *            Consecutivo de la actividad
     * @return Categoria que corresponde con el consecutivo por parámetro
     */
    public Categoria getCategoria(int cons) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getConsecutivo() == cons) {
                return pg;
            }
        }
        return null;
    }

    /**
     * Obtiene la Categoría cuyo tipo de categoría coincide con la cadena por
     * parámetro.
     *
     * @param tipoCategoria
     *            Nombre de un tipo de categoría
     * @return Categoría correspondiente al nombre del tipo de categoría por
     *         parámetro.
     */
    public Categoria getCategoria(String tipoCategoria) {
        Iterator itera = this.categorias.iterator();
        Categoria actual;
        while (itera.hasNext()) {
            actual = (Categoria) itera.next();
            if (actual.getTipoCategoria().getNombre().equals(tipoCategoria)) {
                return actual;
            }
        }
        return null;
    }

    /**
     * Calcula el promedio de asistencia total del curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Promedio de asistencia total del curso.
     */
    public double getPromedioAsistenciaTotal(Connection conexion) {
        double total = 0;
        double contador = 0;
        Iterator iterador = this.alumnos.iterator();
        Alumno actual;

        while (iterador.hasNext()) {
            actual = (Alumno) iterador.next();
            if (actual.getEstado().equals("A")) {
                total += actual.calcularAsistenciaTotal(conexion, this);
                contador++;
            }
        }
        double promedio = OperacionesMatematicas.dividir(total, contador);
        return OperacionesMatematicas.redondear(promedio, 1);
    }
    
    /*
     *Funcion que se encarga de validar si pierde por inasistencia
     *@param alumno
     *            Alumno al cual se calculará su definitiva
     * @param conexion
     *            Conexión a la base de datos
     * @return vInasistencia booleano que indica si perdio por inasistencia.
     */
    public boolean pierdePrInasistencia(Alumno alumno, Connection conexion) {
        boolean vInasistencia = false;
     if (controlAsistencia.equals("S")) {
            boolean perdidaAsistencia = alumno.verificarPerdidaPorAsistencia(conexion, this, 80);
            if (perdidaAsistencia) {
                  vInasistencia = true;
                
            }
        }
        
        return vInasistencia;
    }
    
    
    
    /*
     *
     */
    /*
     public boolean perdidaPorInasistencia(Alumno alumno, Connection conexion) {
        boolean vInasistencia = false;
     if (controlAsistencia.equals("S")) {
           
                double inasistenci=alumno.calcularInasistenciaHoras(conexion,this);
                if((double)this.getHorasPerdida(80)<inasistenci)
                {
                    vInasistencia = true;
                }
            
        }
        
        return vInasistencia;
    }
     */
    /**
     *Se valida que la nota maxima de inasistencia permitidas no sea menor que las inasistencias del estudiante
     * en caso que suceda que el numero de inasistencias del estudiante supere el maximo permitido
     * retorna true para que se imprima el mensaje "No aprovado"
     */
     public boolean perdidaPorInasistencia(Alumno alumno, Connection conexion) {
        boolean vInasistencia = false;
     if (controlAsistencia.equals("S")) {
           
                double inasistenci=(double) alumno.calcularInasistenciaHoras(conexion,this);
               // if((double)this.getHorasPerdida(80)<inasistenci)
                if((double)this.getHorasPerdidaPermitda(80) <inasistenci)
                {
                    vInasistencia = true;
                      }
                else
                {
                 vInasistencia = false;
                 
                }
            
        }
        
        return vInasistencia;
    }
    /**
     * Calcula la definitiva total del alumno como un promedio ponderado entre
     * la definitiva individual y la definitiva grupal dependiendo del
     * departamento académico al que pertenece el curso se tiene en cuenta la
     * definitiva grupal cuando la definitiva individual es < 3. Si el profesor
     * lleva registro de asistencia, se establecerá una nota definitiva de 0.5
     * en caso de que el porcentaje de asistencia sea menor al 80%.
     *
     * @param alumno
     *            Alumno al cual se calculará su definitiva
     * @param conexion
     *            Conexión a la base de datos
     * @return Definitiva del alumno.
     */
    
   
    public double getDefinitiva(Alumno alumno, Connection conexion) {
        
        //double definitiva=0;
     
        if (controlAsistencia.equals("S")) {
            boolean perdidaAsistencia = alumno.verificarPerdidaPorAsistencia(
                    conexion, this, 80);
            if (perdidaAsistencia) {
               
               return 0.5;
              //  definitiva = 0.5;
                 
            }
        }
       
   
        // Nota máxima que puede sacar un estudiante en una materia en ICESI.
        double notaMaxima = 5;
        double definitiva1=0;
        double def = getDefinitivaIndividual(alumno,conexion);
        double def3 = getDefinitivaGrupal(alumno,conexion);
        double def2 = OperacionesMatematicas.dividir(def
                * individuales.getPorcentaje() + def3
                * grupales.getPorcentaje(), 100);

        /*
         * Este redondeo es necesario porque en casos en que existan definitivas
         * individuales como 2.995, se debe redondear la nota a 3 para que se
         * tenga en cuenta el porcentaje grupal. Si no se hace este redondeo, al
         * estudiante le quedaría la nota en 3.0, y no se tomaría en cuenta el
         * porcentaje grupal.
         */
        if(alumno.getCodigo().equals("14120026")){
        System.out.println("*********************"+alumno.getCodigo()+": "+def);
        System.out.println("*********************"+alumno.getCodigo()+": "+def2);
        System.out.println("*********************"+alumno.getCodigo()+": "+def3);
        }
       double definitiva = OperacionesMatematicas.redondear(def, 1) < 3.0 ? def
                : def2;

         definitiva = OperacionesMatematicas.redondear(definitiva, 1);
        if (definitiva > notaMaxima) {
            definitiva = notaMaxima;
        }
//AQUI HACER LA VALIDACION DE LA NOTA DEL FINAL Y LA DEFINITIVA! (SOLO SI ES DEL DEPTO DE MATEMATICAS O SI PERTENECE A LA TABLA DE REGLAS)
           definitiva1 = obtenerDefinitivaBd(conexion,  alumno.getCodigo());
        if (definitiva1 != 9.9)
        { 
             definitiva=definitiva1;
        }
        
        return definitiva;
    }

    
    /*
     *Determina si la perdio por inasistencia,en el caso que la definitva sea igual a 0.5
     * cuando el porcentaje es menor al 80%, para este caso deacuerdo al reglamento se 
     * retorna el mensaje "No Aprobado"
     **/
    public String perdidAsistencia(Alumno alumno, Connection conexion)
    {
        String nAsistencia = "";
        Curso curso = new Curso();
        /*cambio bogomez, se comento el condicional dado que puede presentarse una nota= 0.5 
         * que no necesariamente es por perdida por inasistencia 
         * */
    /*
       if ((getDefinitiva(alumno,conexion))==0.5)
        {*/
        
              Statement stm = null;
              ResultSet rs = null;
              String sql="SELECT fprebus_constantes ('010','02', '') from dual";
                 try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                nAsistencia = rs.getString(1);
                
                ControlRecursos.liberarRecursos(rs, stm);
               // return nAsistencia;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando la nota definitiva "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
            
            
           // nAsistencia="No Aprobado";
        //} 
      
        return nAsistencia;
    }
    /**
     * Calcula la suma total de los porcentajes de las categorías individuales.
     *
     * @return Suma de los porcentajes individuales
     */
    public double sumaIndividual() {
        double total = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getClasificacion().getTipo().equals("I")
                    && !pg.isCancelado()) {
                total += pg.getPorcentaje();
            }
        }
        return total;
    }

    /**
     * Calcula la suma total de los porcentajes de las categorías grupales.
     *
     * @return Suma de los porcentajes grupales
     */
    public double sumaGrupal() {
        double total = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getClasificacion().getTipo().equals("G")
                    && !pg.isCancelado()) {
                total += pg.getPorcentaje();
            }
        }
        return total;
    }

    /**
     * Calcula la definitiva individual para el alumno por parámetro.
     *
     * @param alumno
     *            Alumno al que se calculará la definitiva individual
     * @return Definitiva individual del alumno
     */
    public double getDefinitivaIndividual(Alumno alumno,Connection conexion) {
        double nota = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getClasificacion().getTipo().equals("I")) {
                nota += pg.calcularNota(alumno,conexion);

            }
        }

        return nota;
    }

    /**
     * Calcula la definitiva individual promedio del curso.
     *
     * @return La definitiva individual del curso.
     */
    public double getDefinitivaIndividual(Connection conexion) {
        double prom = 0;
        Alumno actual;
        ListIterator itera = alumnos.listIterator();
        int contador = 0;
        while (itera.hasNext()) {
            actual = (Alumno) itera.next();
            if (actual.getEstado().equals("A")) {
                prom += this.getDefinitivaIndividual(actual,conexion);
                contador++;
            }
        }
        if (contador == 0) {
            return 0;
        }
        prom = OperacionesMatematicas.dividir(prom, contador);
        return prom;
    }

    /**
     * Calcula la definitiva grupal para el alumno por parámetro.
     *
     * @param alumno
     *            Alumno al que se calculará la definitiva grupal
     * @return Definitiva grupal del alumno
     */
    public double getDefinitivaGrupal(Alumno alumno,Connection conexion) {
        double nota = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getClasificacion().getTipo().equals("G")) {
                nota += pg.calcularNota(alumno,conexion);
                // total+=pg.getPorcentaje();
            }
        }
        return nota;
    }

    /**
     * Calcula la definitiva grupal promedio del curso.
     *
     * @return La definitiva grupal del curso.
     */
    public double getDefinitivaGrupal(Connection conexion) {
        double prom = 0;
        Alumno actual;
        ListIterator itera = alumnos.listIterator();
        int contador = 0;
        while (itera.hasNext()) {
            actual = (Alumno) itera.next();
            if (actual.getEstado().equals("A")) {
                prom += this.getDefinitivaGrupal(actual,conexion);
                contador++;
            }
        }
        if (contador == 0) {
            return 0;
        }
        prom = OperacionesMatematicas.dividir(prom, contador);
        return prom;
    }

    /**
     * Calcula el promedio de las notas definitivas del curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Promedio de las notas definitivas del curso.
     */
    public double getPromedio(Connection conexion) {
        double promedio = 0;
        int contador = 0;
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = (Alumno) alumnos.get(i);
            if (alumno.getEstado().equals("A")) {
                promedio += getDefinitiva(alumno, conexion);
                contador++;
            }
        }
        if (contador == 0) {
            return 0;
        }
        promedio /= contador;
        return OperacionesMatematicas.redondear(promedio, 1);
    }
    
    
    //ffceballos  20/09/2013
    
       /**
     * Calcula la desviación de las notas definitivas del curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Desviación de las notas definitivas del curso.
     */
    public double getDesviacion(Connection conexion) {
        double promedio=this.getPromedio(conexion);
        double var=0;
        double desviacion=0;
        double n=0;
        
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = (Alumno) alumnos.get(i);
            if (alumno.getEstado().equals("A")) {
                var+=Math.pow(getDefinitiva(alumno, conexion)-promedio,2);  
                n++;
            }
        }
       
           desviacion=Math.sqrt((1/n)*var);
        
        return OperacionesMatematicas.redondear(desviacion, 2);
    }
    
    
    //fin ffceballos
    

    /**
     * Retorna la Categoría asociada a la Actividad por parámetro. Con el MER
     * para la versión Beta 2, este método no tiene ningún sentido.
     *
     * @param est
     *            Actividad de la cual se desea buscar su Categoría
     * @return Categoría padre de la Actividad por parámetro.
     */
    public Categoria getCategoria(Actividad est) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getActividades().contains(est)) {
                return pg;
            }
        }
        return null;
    }

    /**
     * Calcula el porcentaje total de las categorias que pertenecen a la misma
     * clasificación de la del parámetro, más el porcentaje por parámetro.
     *
     * @param porcentaje
     *            Porcentaje
     * @param actual
     *            Categoría
     * @return true si la suma es menor al 100%.
     */
    public boolean sumaCategoria(double porcentaje, Categoria actual) {
        double suma = 0;
        String tipo = actual.getClasificacion().getTipo();
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            if (pg.getClasificacion().getTipo().equals(tipo)
                    && !pg.isCancelado()) {
                if (pg.getConsecutivo() != actual.getConsecutivo()) {
                    suma += pg.getPorcentaje();
                }
            }
        }

        suma += porcentaje;
        return Math.round(suma) <= 100;
    }

    /**
     * Método empleado para crear categorías y clasificaciones por defecto
     * cuando se está configurando por primera vez el curso. Este método era
     * empleado en la Versión Beta 1 de la aplicación.
     *
     * @param tiposCalif
     *            ArrayList con los tipos de categoría existentes en la BD para
     *            el departamento académico al que pertenece el curso.
     */
    public void cargarCategoriasIniciales(ArrayList tiposCalif) {
        // Carga por primera vez los padresgrupo
        this.categorias = new ArrayList();
        // configuramos algunas cosas de las agrupaciones
        individuales.setNombre("Evaluaciones individuales");
        individuales.setExisteBD(false);

        grupales.setNombre("Evaluaciones grupales");
        grupales.setExisteBD(false);
        for (int i = 0; i < tiposCalif.size(); i++) {
            Categoria pg = new Categoria();
            pg.setTipoCategoria((TipoCategoria) tiposCalif.get(i));
            pg.setActualizado(false);
            pg.setCancelado(true);
            pg.setPorcentaje(0);
            this.categorias.add(pg);
            if (pg.getClasificacion().getTipo().equals("I")
                    && !individuales.getCategorias().contains(pg)) {
                individuales.getCategorias().add(pg);
            } else {
                if (!grupales.getCategorias().contains(pg)
                        && pg.getClasificacion().getTipo().equals("G")) {
                    grupales.getCategorias().add(pg);
                }
            }
        }
    }

    public Clasificacion getGrupales() {
        return grupales;
    }

    public void setGrupales(Clasificacion grupales) {
        this.grupales = grupales;
    }

    public Clasificacion getIndividuales() {
        return individuales;
    }

    public void setIndividuales(Clasificacion individuales) {
        this.individuales = individuales;
    }

    /**
     * Actualiza la nota definitiva que está guardada en la base de datos, si
     * hubo un cambio real en esta nota o cuando es la primera vez que se crea.
     *
     * @param conexion
     *            Conexión a la base de datos.
     */
    public void actualizarDefinitivas(Connection conexion) {
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = (Alumno) alumnos.get(i);
            double def = getDefinitiva(alumno, conexion);
            double copiaDef = obtenerDefinitivaBd(conexion, alumno.getCodigo());
            // 9.9 es el valor por defecto para las notas definitivas
            // No pudiendose habilitar el curso, esto ya no se necesita.
            // if(def!=copiaDef){
            if (def != copiaDef && copiaDef == 9.9) {
                actualizarDefinitivaBd(conexion, alumno.getCodigo(), def);
                // actualizarCambioCierreBd(conexion,"Cambio en definitiva de
                // alumno",alumno.getCodigo(),""+copiaDef,""+def);
            }
        }

    }

    /**
     * Actualiza la nota definitiva que está guardada en la base de datos, si
     * hubo un cambio real en esta nota o cuando es la primera vez que se crea.
     * Debido a ciertas dificultades, se agregó como parámetro el departamento
     * académico del profesor, pero lo correcto sería no emplear este método.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @param deptoAcademicoProfesor
     *            Departamento académico del profesor
     */
    public void actualizarDefinitivas(Connection conexion,
            String deptoAcademicoProfesor) {
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = (Alumno) alumnos.get(i);
            double def = getDefinitiva(alumno, conexion);
            double copiaDef = obtenerDefinitivaBd(conexion, alumno.getCodigo());
            // 9.9 es el valor por defecto para las notas definitivas
            // No pudiendose habilitar el curso, esto ya no se necesita.
            // if(def!=copiaDef){
            if (def != copiaDef && copiaDef == 9.9) {
                actualizarDefinitivaBd(conexion, alumno.getCodigo(), def);
                // actualizarCambioCierreBd(conexion,"Cambio en definitiva de
                // alumno",alumno.getCodigo(),""+copiaDef,""+def);
            }
        }
    }

    /**
     * Actualiza la definitiva para un estudiante dado en la base de datos.
     *
     * @param conexion
     *            Conexión a la base de datos.
     * @param cod_alumno
     *            Código del alumno al cual se actualizará su definitiva.
     * @param definitiva
     *            Definitiva a guardar.
     */
    private void actualizarDefinitivaBd(Connection conexion, String cod_alumno,
            double definitiva) {
        /*String sql = "update tntp_matriculas set definitiva=" + definitiva
                + " where codigo_alumno='" + cod_alumno + "'"
                + " and consec_curso=" + this.consecutivo;*/
        
         /*jalarcon: Modificacion de la definitiva por regla del examen final*/
        CallableStatement stm = null;
       // Statement stm = null;
        try {
          //  stm = conexion.createStatement();
            //stm.executeUpdate(sql);
            
            String sql = "begin ppreact_tntp_def(?,?,?); end;";
            stm = conexion.prepareCall(sql);// Prepara el
            // Statement
            stm.setString(1, cod_alumno); // set al parámetro 1 - Codigo del estudiante
            stm.setDouble(2, definitiva); // set al parámetro 2 - Definitiva
            // 2
            stm.setInt(3, this.consecutivo); // set al parametro 3 - Consecutivo del curso.
      
            
            stm.execute();
           
               ControlRecursos.liberarRecursos(null, stm);
            
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando la nota definitiva "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Recupera la definitiva registrada del alumno en la base de datos.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param cod_alumno
     *            Código del alumno
     * @return Definitiva almacenada en la base de datos.
     */
    private double obtenerDefinitivaBd(Connection conexion, String cod_alumno) {
        String sql = "select definitiva " + "from tntp_matriculas "
                + "where codigo_alumno='" + cod_alumno + "'"
                + "and consec_curso=" + this.consecutivo;
        Statement stm = null;
        ResultSet rs = null;
        double definitivaBd = 0;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                definitivaBd = rs.getDouble("definitiva");
                ControlRecursos.liberarRecursos(rs, stm);
                return definitivaBd;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando la nota definitiva "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return 0;
    }

    /**
     * Inserta una fila en la tabla de auditorias.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param accion
     *            Texto descriptivo explicando el cambio realizado
     * @param cod_alumno
     * @param datoViejo
     * @param datoNuevo
     */
    public void actualizarCambioCierreBd(Connection conexion, String accion,
            String cod_alumno, String datoViejo, String datoNuevo) {
        Statement stm = null;
        try {
            String sql = "";

            sql = "insert into tntp_auditorias(alumno_codigo, fecha_hora, dato_anterior, dato_nuevo, accion, consecutivo, consec_curso)"
                    + "values('"
                    + cod_alumno
                    + "',"
                    + "sysdate,'"
                    + datoViejo
                    + "','"
                    + datoNuevo
                    + "','"
                    + accion
                    + "',"
                    + "(select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias),'"
                    + this.consecutivo + "')";
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error auditando cambios del cierre "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Método empledo para cargar las categorías cuando se está copiando el
     * esquema del curso.
     */
    public void cargarCategoriasClasificaciones() {
        individuales.setCategorias(new ArrayList());
        grupales.setCategorias(new ArrayList());
        for (int i = 0; i < this.categorias.size(); i++) {
            Categoria pg = (Categoria) this.categorias.get(i);
            if (pg.getClasificacion().getTipo().equals("I")) {
                individuales.getCategorias().add(pg);
                pg.setClasificacion(individuales);
            } else {
                grupales.getCategorias().add(pg);
                pg.setClasificacion(grupales);
            }
        }
    }

    /**
     * Método similar al llamado cargarCategorias, excepto que este es empleado
     * para el módulo de estudiantes. La diferencia es que para este esquema,
     * solo existe un alumno asociado, que es el que está trabajando contra la
     * aplicación actualmente.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param profesores
     *            Objeto ControlProfesores
     * @param alumno
     *            Alumno que actualmente está trabajando contra el módulo de
     *            estudiantes.
     */
    public void cargarCategoriasEstudiante(Connection conexion,
            ControlProfesores profesores, Alumno alumno) {
        cargarCategoriasBd(conexion, profesores);
        if (!alumnos.contains(alumno)) {
            alumnos.add(alumno);
        }
        for (int i = 0; i < categorias.size(); i++) {
            Categoria pg = (Categoria) categorias.get(i);
            // if(!individuales.getCategorias().contains(pg) &&
            // !grupales.getCategorias().contains(pg)){

            pg.cargarActividadEstudiante(conexion);
            Actividad est;
            Matricula matricula;
            for (int j = 0; j < pg.getActividades().size(); j++) {
                est = (Actividad) pg.getActividades().get(j);
                matricula = new Matricula(this, alumno);
                matricula.setActividad(est);
                matricula.cargarCalificacion(conexion);
                est.getMatriculas().add(matricula);
            }
            // }
        }
    }

    /**
     * Actualiza las matrículas de los estudiantes que pertenecen al curso en la
     * base de datos. Se llama a un procedimiento en la base de datos que hace
     * esto.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void actualizarMatriculasBd(Connection conexion) {
        CallableStatement stm = null;
        try {
            stm = conexion.prepareCall("{call siaepre.ppreact_tntp_matriculas(?,?,?,?,?)}");
            // System.out.println("------Procedimiento de actualizacion de
            // matriculas -------");
            stm.setInt(1, Integer.parseInt(this.grupo));
            stm.setString(2, this.codigoMateria);
            stm.setInt(3, this.consecutivoMateria);
            stm.setInt(4, this.consecutivoPeriodo);
            stm.setString(5, this.periodoAcademico);
            stm.execute();
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando matriculados "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Método que llama al procedimiento que realiza el cierre en Admisiones y
     * Registro. Este método es invocado desde los métodos llamados cerrarCurso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Valor entero indicando si el cierre fue exitoso o no lo fue. -1.
     *         El curso se encuentra en estado Sincronizado o es No Piloto. -2.
     *         Existen notas registradas en tbas_matriculas. -3. Excepción >=0
     *         Número de filas actualizadas en tbas_matriculas
     */
    public int cierre(Connection conexion) {
        CallableStatement stm = null;
        try {
            String sql = "begin ppreact_cierre_notas_parciales(?,?,?,?); end;";
            stm = conexion.prepareCall(sql);// Prepara el
            // Statement
            stm.setString(1, this.getCodigoMateria()); // set al parámetro 1
            stm.setInt(2, this.getConsecutivoMateria()); // set al parámetro
            // 2
            stm.setInt(3, Integer.parseInt(this.getGrupo())); // set al
            // parámetro 3
            stm.registerOutParameter(4, Types.VARCHAR);// Se registra el
            // parámetro de salida
            stm.execute();
            int resp = stm.getInt(4);// Obtiene la respuesta del parámetro de
            // salida
			/*
             * if(resp!=-3){ boolean auditoria= auditoriaCierre(conexion); }
             */
            ControlRecursos.liberarRecursos(null, stm);
            return resp;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error en el cierre " + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(null, stm);
            return -3;
        }
    }

    /**
     * Método que devuelve el departamento académico al que pertenece el curso.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Departamento académico al que pertenece el curso, o null en caso
     *         de no encontrarlo o presentarse una excepción.
     */
    public String buscarDeptoCurso(Connection conexion) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            String sql = "select fprebus_materias('" + this.codigoMateria
                    + "','04','') from dual";
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString(1);
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return respuesta;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el departamento asociado "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        }
    }
    
    //ffceballos
     /**
     * Método que devuelve los grupos disponibles de la materia
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return  lista de grupos.
     */
    
    public ArrayList cargarGrupos(Connection conexion) {
        Statement stm = null;
        ResultSet rs = null;
        ArrayList grupos=new ArrayList();
        try {
            String sql = "select c.curso_actu_grupo "
                    + "from tntp_cursos c "
                    + "where c.descripmat_codigo='"+ this.codigoMateria+ "'"
                    + " and c.periodo_periodo_acad || c.periodo_consecutivo = fprebus_per_notp('0102','')";//fprebus_constantes ('002', '0304', '') " ;
                   
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            
            while (rs.next()) {
                 grupos.add(rs.getString(1));
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return grupos;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el departamento asociado "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return null;
        }
    }
//fin ffceballos
    public int getConsecutivo() {
        return consecutivo;
    }

    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado
     *            The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @param consecutivo
     *            The consecutivo to set.
     */
    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    /**
     * @param consecutivoMateria
     *            The consecutivoMateria to set.
     */
    public void setConsecutivoMateria(int consecutivoMateria) {
        this.consecutivoMateria = consecutivoMateria;
    }

    public void setConsecutivoPeriodo(int consecutivoPeriodo) {
        this.consecutivoPeriodo = consecutivoPeriodo;
    }

    public void setDescripcionMateria(String descripcionMateria) {
        this.descripcionMateria = descripcionMateria;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setPeriodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public ArrayList getProgramaciones() {
        return programaciones;
    }

    public void setProgramaciones(ArrayList programaciones) {
        this.programaciones = programaciones;
    }

    /**
     * Carga las programaciones del curso para un mes especifico. Usado en
     * módulo de asistencias
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param mes
     *            Mes para el cual se cargarán las programaciones.
     *            Cedula del profesor para cargar su programacion.
     * @return true si las programaciones se cargaron exitosamente
     */
    public boolean cargarProgramaciones(Connection conexion, String mes) {
        Statement stm = null;
        Statement stm2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        try {
            programaciones = new ArrayList();
            stm = conexion.createStatement();
            stm2 = conexion.createStatement();
            Profesor profesor = new Profesor();
            documento_profesor = profesor.getCedula();
            /*Alumno alumno = new Alumno(codigo, nombre);
                alumno.setEstado(rs.getString("ESTADO_ALUMNO"));*/
            String sql = "select distinct to_char(p.fecha,'DD/MM/YY') fecha, p.hr_inicio, hr_fin from tbas_programaciones p where p.descripmat_materia_codigo='"
                    + this.getCodigoMateria()
                    + "' and p.periodo_acad='"
                    + this.getPeriodoAcademico()
                    + "' and p.periodo_consecutivo="
                    + this.getConsecutivoPeriodo()
                    + " and p.materia_grupo="
                    + this.getGrupo()
                    + " and p.espacio_codigo != 'PEN' "
                    + " and p.descripmat_consecutivo="
                    + this.getConsecutivoMateria()
                    + " and (to_char(p.fecha,'DD/MM/YY') like '%/"
                    + mes
                    + "/%')"
                    + "and tipo='PRE'";
                    /*+ "and num_id='31904194'";*/
                   
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                String sql2 = "select festivo from tbas_dias_festivos f where to_char(festivo,'DD/MM/YY')='"
                        + rs.getString(1) + "'";
                rs2 = stm2.executeQuery(sql2);
                if (!rs2.next()) {
                    Programacion programacion = new Programacion();
                    programacion.setFecha(rs.getString(1));
                    programacion.setHoraInicio(rs.getInt(2));
                    programacion.setHoraFin(rs.getInt(3));
                    programacion.calcularHorarioFormateado(conexion);
                    programaciones.add(programacion);
                }
            }
            Collections.sort(programaciones);
            ControlRecursos.liberarRecursos(rs, stm);
            ControlRecursos.liberarRecursos(rs2, stm2);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando programaciones "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            ControlRecursos.liberarRecursos(rs2, stm2);
            return false;
        }
    }

    /**
     * Calcula el número total de programaciones para el curso actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Número total de programaciones
     */
    public int obtenerNumeroTotalProgramaciones(Connection conexion) {
        Statement stm = null;
        Statement stm2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
         
        try {  
            int numProg = 0;
            stm = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
           
           // String sql = "select distinct to_char(p.fecha,'DD/MM/YY') fecha, p.hr_inicio, hr_fin from tbas_programaciones p where p.descripmat_materia_codigo='"
                 
            String sql = "select distinct to_char(p.fecha,'DD/MM/YY') fecha, p.hr_inicio, hr_fin from tbas_programaciones p where p.descripmat_materia_codigo='"
              
            + this.getCodigoMateria()
                    + "' and p.periodo_acad='"
                    + this.getPeriodoAcademico()
                    + "' and p.periodo_consecutivo="
                    + this.getConsecutivoPeriodo()
                    + " and p.espacio_codigo != 'PEN' "
                    + " and p.materia_grupo="
                    + this.getGrupo()
                    + " and p.descripmat_consecutivo="
                    + this.getConsecutivoMateria() + " and tipo='PRE'";
            rs = stm.executeQuery(sql);
            if (rs.last()) {
                numProg = rs.getRow();
            }
            ControlRecursos.liberarRecursos(rs, stm);
            stm2 = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql2 = "select distinct f.festivo from tbas_dias_festivos f, tbas_programaciones p where p.descripmat_materia_codigo='"
                    + this.getCodigoMateria()
                    + "' and p.periodo_acad='"
                    + this.getPeriodoAcademico()
                    + "' and p.periodo_consecutivo="
                    + this.getConsecutivoPeriodo()
                    + " and p.espacio_codigo != 'PEN' "
                    + " and p.materia_grupo="
                    + this.getGrupo()
                    + " and p.descripmat_consecutivo="
                    + this.getConsecutivoMateria()
                    + " and tipo='PRE' and p.fecha=f.festivo";
            rs2 = stm2.executeQuery(sql2);
            if (rs2.last()) {
                numProg -= rs2.getRow();
            }
            ControlRecursos.liberarRecursos(rs, stm);
            ControlRecursos.liberarRecursos(rs2, stm2);
            return numProg;
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el número total de programaciones "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 0;
        }finally{
    //    System.out.println("vvd");
        }
    }

    /**
     * Calcula el número total de programaciones en un mes para el curso actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param mes
     *            Mes a consultar
     * @return Número total de programaciones en el mes
     */
    public int obtenerNumeroTotalProgramacionesMes(Connection conexion,
            String mes) {
        Statement stm = null;
        ResultSet rs = null;
        int cont = 0;
        try {
            stm = conexion.createStatement();
            //Se modifico para que tomara las fechas en las que para un mismo dia se tiene dos programaciones
            //String sql = "SELECT count(distinct p.fecha) "
            String sql = "SELECT count(distinct decode(p.fecha,p.fecha,p.fecha||p.hr_inicio||p.hr_fin)) "
                    + "    FROM tbas_programaciones p "
                    + "   WHERE p.descripmat_materia_codigo = '"
                    + this.codigoMateria
                    + "'"
                    + "     AND p.periodo_acad = "
                    + this.periodoAcademico
                    + "     AND p.periodo_consecutivo = "
                    + this.consecutivoPeriodo
                    + "     AND p.materia_grupo = "
                    + this.grupo
                    + "     and p.espacio_codigo != 'PEN' "
                    + "     AND p.descripmat_consecutivo = "
                    + this.consecutivoMateria
                    + "     AND p.tipo = 'PRE' "
                    //+ "     AND TO_CHAR (p.fecha, 'DD/MM/YY') LIKE '%/"
                    //+ mes
                    //+ "/%' "
                    + "     AND p.fecha NOT IN ( SELECT  DISTINCT f.festivo "
                    + "                            FROM tbas_dias_festivos f, tbas_programaciones pr "
                    + "                           WHERE pr.descripmat_materia_codigo = p.descripmat_materia_codigo "
                    + "                             AND pr.periodo_acad = p.periodo_acad "
                    + "                             AND pr.periodo_consecutivo = p.periodo_consecutivo "
                    + "                             AND pr.materia_grupo = p.materia_grupo "
                    + "                             AND pr.descripmat_consecutivo = p.descripmat_consecutivo "
                    + "                             AND pr.tipo = 'PRE' "
                    + "                             and pr.espacio_codigo != 'PEN' "
                    + "                             AND pr.fecha = f.festivo) ";
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            rs.next();
            cont = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el número total de programaciones en el mes "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return cont;
    }

    /**
     * Calcula el número total de programaciones para el curso actual hasta la
     * fecha actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Número total de programaciones hasta la fecha actual
     */
    public int obtenerProgramacionesHastaFechaActual(Connection conexion) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            int numProg = 0;
            stm = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = "select distinct to_char(p.fecha,'DD/MM/YY') fecha, p.hr_inicio, hr_fin from tbas_programaciones p where p.descripmat_materia_codigo='"
                    + this.getCodigoMateria()
                    + "' and p.periodo_acad='"
                    + this.getPeriodoAcademico()
                    + "' and p.periodo_consecutivo="
                    + this.getConsecutivoPeriodo()
                    + " and p.materia_grupo="
                    + this.getGrupo()
                    + " and p.descripmat_consecutivo="
                    + this.getConsecutivoMateria()
                    + " and p.espacio_codigo != 'PEN' "
                    + " and tipo='PRE'"
                    + " and trunc(p.fecha) < trunc(sysdate)"
                    + "     AND p.fecha NOT IN ( SELECT  DISTINCT f.festivo "
                    + "                            FROM tbas_dias_festivos f, tbas_programaciones pr "
                    + "                           WHERE pr.descripmat_materia_codigo = p.descripmat_materia_codigo "
                    + "                             AND pr.periodo_acad = p.periodo_acad "
                    + "                             AND pr.periodo_consecutivo = p.periodo_consecutivo "
                    + "                             AND pr.materia_grupo = p.materia_grupo "
                    + "                             AND pr.descripmat_consecutivo = p.descripmat_consecutivo "
                    + "                             AND pr.tipo = 'PRE' "
                    + "                             and pr.espacio_codigo != 'PEN' "
                    + "                             AND pr.fecha = f.festivo) ";
            rs = stm.executeQuery(sql);
            if (rs.last()) {
                numProg = rs.getRow();
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return numProg;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el número total de programaciones hasta la fecha actual "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 0;
        }
    }

    public String getControlAsistencia() {
        return controlAsistencia;
    }

    public void setControlAsistencia(String controlAsistencia) {
        this.controlAsistencia = controlAsistencia;
    }

    public String getTipoConfiguracion() {
        return tipoConfiguracion;
    }

    public void setTipoConfiguracion(String tipoConfiguracion) {
        this.tipoConfiguracion = tipoConfiguracion;
    }

    public void actualizarTipoConfiguracion(Connection conexion) {
        Statement stm = null;
        try {
            String sql = "update tntp_cursos set tipo_configuracion='"
                    + this.tipoConfiguracion + "' where consecutivo="
                    + consecutivo;
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error actualizando el tipo de configuración "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Obtiene el número de notas que el profesor ha registrado hasta el momento
     * en la aplicación No tiene en cuenta las notas que están en cero. Este
     * método se utiliza en el módulo de jefes únicamente.
     *
     * @return el total de notas registradas
     */
    public int obtenerNumeroNotasRegsitradas() {
        int suma = 0;
        for (int i = 0; i < categorias.size(); i++) {
            Categoria cat = (Categoria) categorias.get(i);
            for (int j = 0; j < cat.getActividades().size(); j++) {
                Actividad act = (Actividad) cat.getActividades().get(j);
                for (int k = 0; k < act.getMatriculas().size(); k++) {
                    Matricula mat = (Matricula) act.getMatriculas().get(k);
                    if (mat.getCalificacion() > 0) {
                        suma++;
                    }
                }
            }

        }
        return suma;
    }

    /**
     * Obtiene el nùmero total de las notas que deben ser registradas por un
     * profesor para este curso Incluye todas las notas de todas las
     * actividades. Se utiliza unicamente en el módulo de jefes.
     *
     * @return
     */
    public int obtenerNumeroTotalNotas() {
        int numeroActividades = 0;
        for (int i = 0; i < categorias.size(); i++) {
            numeroActividades += ((Categoria) categorias.get(i)).getActividades().size();
        }
        return numeroActividades * alumnos.size();
    }

    /**
     * @return Returns the escala.
     */
    public double getEscala() {
        return escala;
    }

    /**
     * @param escala
     *            The escala to set.
     */
    public void setEscala(double escala) {
        this.escala = escala;
    }

    
     /**
     * @return Retorna el total de horas del curso
     *
     */
    public double getTotalHoras() {
        return this.totalHoras;
    }

    /**
     * @return Retorna el total de horas de falta con las que perdería el curso
     *
     */
    public double getHorasPerdida(int porcentajeMinimo) {
        return (this.totalHoras * (100 - porcentajeMinimo) / 100);
    }
    
    /*
    public int getHorasPerdida(int porcentajeMinimo) {
        return (this.totalHoras * (100 - porcentajeMinimo) / 100);
    }
  */
   
     public double getHorasPerdidaPermitda(double porcentajeMinimo) {
        return (double)((double)this.totalHoras * (100 - porcentajeMinimo) / 100);
    }
    
    public void setTotalHoras(int pTotalHoras)
    {
        this.totalHoras =pTotalHoras;
    }
  

    /**
     * @param conexion
     *            La conexión a la base de datos
     *
     */
    public void cargarTotalHoras(Connection conexion) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //ffceballos: 2205/2014 se cambió la subconsulta del descripmat_consecutivo. Abajo se muestra la anterior consulta:
            /*select max (d.consecutivo) 
              from tbas_descrip_mat d
              where d.materia_codigo = p.descripmat_materia_codigo*/
            String sql = "select round (sum ( (tmp.hr_fin - tmp.hr_inicio) / 3600), 2) horas "
                    + "  from (select distinct p.fecha, p.hr_inicio, p.hr_fin "
                    + "          from tbas_programaciones p "
                    + "         where p.descripmat_materia_codigo = ?"
                    + "           and p.descripmat_consecutivo = (SELECT MAX (cp.descripmat_consecutivo) " +
                                            "                    FROM tbas_cursos_profesor cp " +
                                            "                    WHERE     cp.descripmat_codigo = ?  " +
                                            "                    AND curso_actu_grupo = ?  " +
                                            "                    AND  cp.periodo_periodo_acad = ?" +
                                            "                    AND  cp.periodo_consecutivo = ? ) "
                    + "          and p.materia_grupo = ?"
                    + "          and p.periodo_acad = ?"
                    + "          and p.espacio_codigo != 'PEN' "
                    + "          and p.periodo_consecutivo = ?"
                    + "          and p.fecha not in (select distinct f.festivo "
                    + "                                 from tbas_dias_festivos f, tbas_programaciones pr "
                    + "                                where pr.descripmat_materia_codigo = p.descripmat_materia_codigo "
                    + "                                  and pr.periodo_acad = p.periodo_acad "
                    + "                                  and pr.periodo_consecutivo = p.periodo_consecutivo "
                    + "                                  and pr.materia_grupo = p.materia_grupo "
                    + "                                  and pr.descripmat_consecutivo = p.descripmat_consecutivo "
                    + "                                  and pr.tipo = 'PRE' "
                    + "                                  and pr.espacio_codigo != 'PEN' "
                    + "                                  and pr.fecha = f.festivo)) tmp ";
            
            stm = conexion.prepareStatement(sql);
            stm.setString(1, this.codigoMateria);
            stm.setString(2, this.codigoMateria);
            stm.setString(3, this.grupo);
            stm.setString(4, this.periodoAcademico);
            stm.setInt(5, this.consecutivoPeriodo);
                      
            stm.setString(6, this.grupo);
            stm.setString(7, this.periodoAcademico);
            stm.setInt(8, this.consecutivoPeriodo);
           //fin ffceballos
            rs = stm.executeQuery();
            rs.next();
         this.totalHoras = rs.getDouble(1);
          //this.setTotalHoras(rs.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el total de horas "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }
    
    
    /**
   * Método que llama al procedimiento que realiza la reapertura en Admisiones y
   * Registro.
   *
   * @param conexion
   *            Conexión a la base de datos
   * @return
   */
  public int reabrir(Connection conexion, String cedulaProfesor)
  {
    CallableStatement stm = null;
    try
    {
      String sql = "begin ppreact_tntp_abrcurso(?,?,?,?,?,?); end;";
      stm = conexion.prepareCall(sql);// Prepara el
      // Statement
      stm.setString(1, this.getPeriodoAcademico());
      stm.setInt(2, this.getConsecutivoPeriodo());
      stm.setString(3, cedulaProfesor);
      stm.setString(4, this.getCodigoMateria());
      stm.setString(5, this.getGrupo());
      stm.registerOutParameter(6, Types.INTEGER);
      System.out.println(sql);
      stm.execute();
      int resp = stm.getInt(6);// Obtiene la respuesta del parámetro de
      //System.out.println("Respuesta: " + resp);
      // salida
			/*
       * if(resp!=-3){ boolean auditoria= auditoriaCierre(conexion); }
       */
      ControlRecursos.liberarRecursos(null, stm);
      return resp;
    } catch (SQLException e)
    {
      System.out.println("Error: " + this.getClass().getName());
      System.out.println("Descripción:");
      System.out.println("Error en el cierre " + e.getMessage());
      e.printStackTrace();
      ControlRecursos.liberarRecursos(null, stm);
      return -3;
    }
  }
}
