package co.edu.icesi.notas;

import java.io.*;
import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.control.ControlRecursos;
import co.edu.icesi.notas.utilidades.*;

/**
 * Esta clase describe a un alumno matriculado en el curso. Cada alumno tiene un
 * código, nombre, apellidos e e-mail. Además también tiene un estado que indica
 * si está activo o inactivo.
 * 
 * @author mzapata, lmdiaz
 * 
 */
public class Alumno implements Serializable {

    private String codigo;
    private String nombre;
    private String apellidos;
    private ArrayList matriculas;
    private String estado;
    private String mail;
    private String mailAlternativo;
    private boolean notificable;
    private ArrayList asistencias;
    private int promedioAsistenciaMes;

    public ArrayList getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(ArrayList asistencias) {
        this.asistencias = asistencias;
    }

    public boolean isNotificable() {
        return notificable;
    }

    public int getPromedioAsistenciaMes() {
        return promedioAsistenciaMes;
    }

    public String getMailAlternativo() {
        return mailAlternativo;
    }

    public void setMailAlternativo(String mailAlternativo) {
        this.mailAlternativo = mailAlternativo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Carga todos los correos electrónicos posibles del alumno.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void cargarMails(Connection conexion) {
        String sql = "select correo_electr,correo_inst from siaepre.tbas_alumnos where codigo='"
                + codigo + "'";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                mail = rs.getString("correo_electr");
                mailAlternativo = rs.getString("correo_inst");
            }
            if ((this.mail != null && !this.mail.trim().equals(""))
                    || (this.mailAlternativo != null && !this.mailAlternativo.trim().equals(""))) {
                this.notificable = true;
            } else {
                this.notificable = false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando los correos " + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * Carga las asistencias registradas de un estudiante para un mes
     * especificado.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param curso
     *            Curso al que pertenece el estudiante
     * @param mes
     *            Mes para el cual se desea cargar las asistencias.
     * @return Valor booleano indicando si se cargaron o no las asistencias.
     */
    public boolean cargarAsistencias(Connection conexion, Curso curso,
            String mes) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            asistencias = new ArrayList();
            stm = conexion.createStatement();
            String sql = "select to_char(a.fecha,'DD/MM/YY'), a.hora_inicio, a.hora_fin, a.asistencia from tntp_asistencias a where to_char(a.fecha,'DD/MM/YY') like '%/"
                    + mes
                    + "/%' and a.consec_curso="
                    + curso.getConsecutivo()
                    + " and codigo_alumno='"
                    + this.getCodigo()
                    + "' order by a.fecha asc";
            rs = stm.executeQuery(sql);
            double acum = 0;
            promedioAsistenciaMes = 0;
            boolean continuar = true;
            Asistencia asistencia = new Asistencia();
            for (int i = 0; i < curso.getProgramaciones().size(); i++) {
                Asistencia asistenciaProg = new Asistencia();
                asistenciaProg.setFecha(((Programacion) curso.getProgramaciones().get(i)).getFecha());
                asistenciaProg.setHoraInicio(((Programacion) curso.getProgramaciones().get(i)).getHoraInicio());
                asistenciaProg.setHoraFin(((Programacion) curso.getProgramaciones().get(i)).getHoraFin());
                asistenciaProg.setPorcentajeAsistencia(-1);
                if (continuar && rs.next()) {
                    asistencia = new Asistencia();
                    asistencia.setFecha(rs.getString(1));
                    asistencia.setHoraInicio(rs.getInt(2));
                    asistencia.setHoraFin(rs.getInt(3));
                    asistencia.setPorcentajeAsistencia(rs.getInt(4));
                }
                if (asistenciaProg.getFecha().equals(asistencia.getFecha())
                        && asistenciaProg.getHoraInicio() == asistencia.getHoraInicio()) {
                    asistencias.add(asistencia);
                    acum += asistencia.getPorcentajeAsistencia();
                    continuar = true;
                } else {
                    asistencias.add(asistenciaProg);
                    continuar = false;
                }
            }
            asistencias.trimToSize();
            if (asistencias.size() > 0) {
                promedioAsistenciaMes = (int) OperacionesMatematicas.redondear(
                        acum / asistencias.size(), 0);
            }
            Collections.sort(asistencias);
            ControlRecursos.liberarRecursos(rs, stm);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando las asistencias del mes "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return false;
    }

    /**
     * Guarda o actualiza las asistencias para el alumno actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param curso
     *            Curso al que pertenece el estudiante
     * @param porcentajeMinimoAsistencia
     *            Porcentaje mínimo de asistencias para no perder la materia por
     *            este concepto.
     * @return Valor booleano indicando si el proceso fue o no exitoso.
     */
    public boolean guardarAsistencias(Connection conexion, Curso curso,
            int porcentajeMinimoAsistencia) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            int acum = 0;
            stm = conexion.createStatement();
            for (int i = 0; i < asistencias.size(); i++) {
                Asistencia asistencia = (Asistencia) asistencias.get(i);
                String sql = "select * from tntp_asistencias where consec_curso="
                        + curso.getConsecutivo()
                        + " and codigo_alumno='"
                        + this.getCodigo()
                        + "' and fecha=to_date('"
                        + asistencia.getFecha()
                        + "','DD/MM/YY') and hora_inicio="
                        + asistencia.getHoraInicio()
                        + " and hora_fin="
                        + asistencia.getHoraFin();
                rs = stm.executeQuery(sql);
                if (asistencia.getPorcentajeAsistencia() != -1) {
                    if (rs.next()) {
                        sql = "update tntp_asistencias set asistencia="
                                + asistencia.getPorcentajeAsistencia()
                                + " where consec_curso="
                                + curso.getConsecutivo()
                                + " and codigo_alumno='" + this.getCodigo()
                                + "' and fecha=to_date('"
                                + asistencia.getFecha()
                                + "','DD/MM/YY') and hora_inicio="
                                + asistencia.getHoraInicio() + " and hora_fin="
                                + asistencia.getHoraFin();
                    } else {
                        sql = "insert into tntp_asistencias (consec_curso,codigo_alumno,fecha,hora_inicio,hora_fin,asistencia) values ("
                                + curso.getConsecutivo()
                                + ",'"
                                + this.getCodigo()
                                + "',to_date('"
                                + asistencia.getFecha()
                                + "','DD/MM/YY'),"
                                + asistencia.getHoraInicio()
                                + ","
                                + asistencia.getHoraFin()
                                + ","
                                + asistencia.getPorcentajeAsistencia() + ")";
                    }
                    stm.executeUpdate(sql);
                    acum += asistencia.getPorcentajeAsistencia();
                } else if (rs.next()) {
                    sql = "delete from tntp_asistencias where consec_curso="
                            + curso.getConsecutivo() + " and codigo_alumno='"
                            + this.getCodigo() + "' and fecha=to_date('"
                            + asistencia.getFecha()
                            + "','DD/MM/YY') and hora_inicio="
                            + asistencia.getHoraInicio() + " and hora_fin="
                            + asistencia.getHoraFin();
                    stm.executeUpdate(sql);
                }
            }
            if (asistencias.size() > 0) {
                promedioAsistenciaMes = (int) OperacionesMatematicas.redondear(
                        acum / asistencias.size(), 0);
            } else {
                promedioAsistenciaMes = 0;
            }

            if (curso.getControlAsistencia().equals("S")) {
                verificarPerdidaPorAsistencia(conexion, curso,
                        porcentajeMinimoAsistencia);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error guardando la asistencia "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcula el promedio aritmético de las asistencias en un mes especificado,
     * para el estudiante actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param mes
     *            Mes para el cual se desea hallar el promedio.
     * @param curso
     *            Curso al que pertenece el estudiante.
     * @return Promedio aritmético de las asistencias en el mes especificado
     */
    public int calcularAsistenciasMes(Connection conexion, String mes,
            Curso curso) {
        double consolidadoMes = 0;
        Statement stm = null;
        ResultSet rs = null;
        try {
            int cont = 0;
            stm = conexion.createStatement();
            String sql = "select a.asistencia from tntp_asistencias a where to_char(a.fecha,'DD/MM/YY') like '%/"
                    + mes
                    + "/%' and a.consec_curso="
                    + curso.getConsecutivo()
                    + " and codigo_alumno='"
                    + this.getCodigo()
                    + "' order by a.fecha";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                consolidadoMes += rs.getInt(1);
            }
            cont = curso.obtenerNumeroTotalProgramacionesMes(conexion, mes);
            if (cont != 0) {
                ControlRecursos.liberarRecursos(rs, stm);
                return (int) OperacionesMatematicas.redondear(consolidadoMes
                        / cont, 0);
            } else {
                ControlRecursos.liberarRecursos(rs, stm);
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando las asistencias del mes "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 0;
        }
    }

    /**
     * Calcula el promedio aritmético de las asistencias en todo el semestre,
     * para el estudiante actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param curso
     *            Curso al que pertenece el estudiante.
     * @return Promedio aritmético de las asistencias en todo el semestre.
     */
    public int calcularAsistenciaTotal(Connection conexion, Curso curso) {
        double consolidado = 0;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            String sql = "select a.asistencia from tntp_asistencias a where a.consec_curso="
                    + curso.getConsecutivo()
                    + " and codigo_alumno='"
                    + this.getCodigo()
                    + "' and trunc(a.fecha) < trunc(sysdate)"
                    + " order by a.fecha";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                consolidado += rs.getInt(1);
            }
            int totalProgramacionesHastaFecha = curso.obtenerProgramacionesHastaFechaActual(conexion);
            if (totalProgramacionesHastaFecha != 0) {
                ControlRecursos.liberarRecursos(rs, stm);
                return (int) OperacionesMatematicas.redondear(consolidado
                        / totalProgramacionesHastaFecha, 0);
            } else {
                ControlRecursos.liberarRecursos(rs, stm);
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error calculando la asistencia total "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 0;
        }
    }

    /**
     * Calcula las horas de inasistencia de las asistencias registradas hasta la
     * fecha actual, para el estudiante actual.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @param curso
     *            Curso al que pertenece el estudiante.
     * @return Horas de inasistencia de las asistencias registradas hasta la
     *         fecha actual.
     */
    public double calcularInasistenciaHoras(Connection conexion, Curso curso) {
        double inasistenciaHoras = 0;
        //int inasistenciaHoras = 0;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            String sql = "select (sum(tmp.hr_fin - tmp.hr_inicio)/3600  -  sum( decode(tna.asistencia, 0, 0 , (tna.hora_fin - tna.hora_inicio)*(tna.asistencia/100)))/3600) inasistencia "
                    + "  from (select distinct p.fecha, p.hr_inicio, p.hr_fin "
                    + "          from tbas_programaciones p "
                    + "         where p.descripmat_materia_codigo = '"
                    + curso.getCodigoMateria()
                    + "'          and p.periodo_acad = '"
                    + curso.getPeriodoAcademico()
                    + "'          and p.periodo_consecutivo = '"
                    + curso.getConsecutivoPeriodo()
                    + "'          and p.materia_grupo = '"
                    + curso.getGrupo()
                    + "'          and p.descripmat_consecutivo = '"
                    + curso.getConsecutivoMateria()
                    + "'          and tipo = 'PRE' "
                    + "           and p.fecha < trunc (sysdate) "
                    + "           and p.fecha not in (select distinct f.festivo "
                    + "                                 from tbas_dias_festivos f, tbas_programaciones pr "
                    + "                                where pr.descripmat_materia_codigo = p.descripmat_materia_codigo "
                    + "                                  and pr.periodo_acad = p.periodo_acad "
                    + "                                  and pr.periodo_consecutivo = p.periodo_consecutivo "
                    + "                                  and pr.materia_grupo = p.materia_grupo "
                    + "                                  and pr.descripmat_consecutivo = p.descripmat_consecutivo "
                    + "                                  and pr.tipo = 'PRE' "
                    + "                                  and pr.fecha = f.festivo)) tmp, tntp_asistencias tna "
                    + " where tna.consec_curso(+) = '"
                    + curso.getConsecutivo()
                    + "'  and tna.codigo_alumno(+) = '"
                    + this.codigo
                    + "'  and tmp.fecha = tna.fecha(+)"
                    + "  and tmp.hr_inicio = tna.hora_inicio(+)"
                    + "  and tmp.hr_fin = tna.hora_fin(+)";

            rs = stm.executeQuery(sql);
            rs.next();
            inasistenciaHoras += rs.getDouble(1);
            ControlRecursos.liberarRecursos(rs, stm);
            return inasistenciaHoras;

        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error calculando la inasistencia en horas "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 0;
        }
    }

    /**
     * @param conexion
     *            Conexión a la base de datos
     * @param curso
     *            Curso al que pertenece el estudiante.
     * @param porcentajeMinimoAsistencia
     *            Porcentaje mínimo de asistencias para no perder la materia por
     *            este concepto.
     * 
     *  v1.2     22-nov-2012                   csarmiento 
             * Se cambio la formade calcular el totalizado para perdida por inasistencia, esto por inconvenientes
             *  cuando una clase de un día se dividia en horas en la tarde y en la mañana y no solo
             *  una sesion en un dia.  Es decir 2 sesiones en una misma fecha. 
             *  Tambien se cambio el filtro de esa consulta para tener en cuenta las diferentes sesiones de un
             *  mismo día.  Caso SGS 66723    
             * 
     */
     public boolean verificarPerdidaPorAsistencia(Connection conexion,
            Curso curso, int porcentajeMinimoAsistencia) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            /*  v1.2  */
            double horasProg = curso.getTotalHoras();
            double horasParaPerder = horasProg - curso.getHorasPerdida(porcentajeMinimoAsistencia);
            double horasAsistencia = 0;
            /* fin  v1.2*/
            
            int numProg = curso.obtenerNumeroTotalProgramaciones(conexion);
            int cantidadNumProg = numProg * 100;
            int cantidadLimite = numProg * porcentajeMinimoAsistencia;
            int cantidadNoRegistrada = cantidadNumProg;
            int cantidadAsistida = 0;
            //ffceballos
            double cantidadHastaElMomento=0;
            double cantidadHorasTotal=curso.getTotalHoras();
            //fin ffceballos
            /*  v1.2  */
            String sql = "select asistencia, hora_inicio, hora_fin from tntp_asistencias where consec_curso="
                    + curso.getConsecutivo()
                    + " and codigo_alumno='"
                    + this.getCodigo() + "'";
           /*String sql = "select asistencia from tntp_asistencias where consec_curso="
                    + curso.getConsecutivo()
                    + " and codigo_alumno='"
                    + this.getCodigo() + "'";
                    * */
            /* fin v1.2  */
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                cantidadAsistida += rs.getInt(1);
                cantidadNoRegistrada -= 100;
                /*  v1.2  */
                if (rs.getInt(1) != 0){
                   horasAsistencia += ((rs.getInt(3) - rs.getInt(2)) * ((double)rs.getInt(1)/100))/3600;
                }
                /*fin  v1.2  */
                
               // ffceballos 25/04/2014 
                cantidadHastaElMomento+=(rs.getInt(3) - rs.getInt(2))/3600;
                //fin ffceballos
            }
            /*  v1.2  */
            //boolean auditarPerdida = (cantidadNoRegistrada + cantidadAsistida) < cantidadLimite;
           
         /*   System.out.println("***************** Cantidad programadas"+cantidadNumProg);
            System.out.println("***************** Horas para perder"+horasParaPerder);
            System.out.println("***************** horas asistencia "+horasAsistencia);
            System.out.println("***************** cant hasta el momento "+cantidadHastaElMomento);
            System.out.println("***************** cant hasta el momento "+cantidadLimite);
            System.out.println("***************** Total horas "+cantidadHorasTotal);
            double faltas=horasAsistencia+(cantidadHorasTotal-cantidadHastaElMomento);
             System.out.println("***************** Total faltas "+faltas);
            */
            
            //boolean auditarPerdida = horasParaPerder > horasAsistencia;
             //ffceballos: sumarle a las horas de asistencia, los días que no han pasado
            boolean auditarPerdida = horasParaPerder > horasAsistencia+(cantidadHorasTotal-cantidadHastaElMomento); 
            //fin ffceballos
           
            /*fin  v1.2  */
            if (auditarPerdida) {
                sql = "select fecha_perd_asist from tntp_matriculas where consec_curso="
                        + curso.getConsecutivo()
                        + " and codigo_alumno='"
                        + codigo + "'";
                rs = stm.executeQuery(sql);
                if (rs.next()) {
                    if (rs.getDate(1) == null) {
                        sql = "update tntp_matriculas set fecha_perd_asist=sysdate where consec_curso="
                                + curso.getConsecutivo()
                                + " and codigo_alumno='" + codigo + "'";
                        stm.executeUpdate(sql);
                        String accion = "Pérdida de la materia por falta de asistencia";
                        sql = "insert into tntp_auditorias (alumno_codigo,fecha_hora,accion,consec_curso, consecutivo) values('"
                                + codigo
                                + "',sysdate,'"
                                + accion
                                + "',"
                                + curso.getConsecutivo()
                                + ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
                        stm.executeUpdate(sql);
                    }
                }
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return auditarPerdida;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error verificando pérdida por asistencia "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return false;
        }
    }
 


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int hashCode() {
        return codigo.hashCode();
    }

    /*
     * (non-Javadoc) Compara los alumnos por su código.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof Alumno) {
            Alumno alu = (Alumno) obj;
            return this.igualCodigo(alu.getCodigo());
        }
        return false;
    }

    /**
     * Indica si el código del alumno actual es igual al del parámetro.
     *
     * @param cod
     *            Código con el que se desea comparar.
     * @return true si el código del alumno actual es igual al del parámetro.
     */
    public boolean igualCodigo(String cod) {
        return codigo.equals(cod);
    }

    public Alumno() {
        super();
    }

    public Alumno(String codigo, String nombre, String apellidos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Alumno(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList getMatriculas() {
        return matriculas;
    }

    public String getNombre() {
        return nombre;
    }
}
