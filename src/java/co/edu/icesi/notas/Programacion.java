/*
 * Created on Dec 12, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import co.edu.icesi.notas.control.ControlRecursos;

/**
 * Representa una clase del curso, especificando el día, la hora inicial y la
 * hora final.
 * 
 * @author rescobar
 */
public class Programacion implements java.io.Serializable, Comparable {

    private String fecha;
    private int horaInicio;
    private int horaFin;
    private String horaInicioFormateada;
    private String horaFinFormateada;

    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha
     *            The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return Returns the horaFin.
     */
    public int getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin
     *            The horaFin to set.
     */
    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @return Returns the horaInicio.
     */
    public int getHoraInicio() {
        return horaInicio;
    }

    /**
     * @param horaInicio
     *            The horaInicio to set.
     */
    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Invoca una función de la base de datos para calcular, con un formato
     * específico, la hora inicial y la hora final (que están expresadas como el
     * tiempo en segundos desde las 00:00).
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void calcularHorarioFormateado(Connection conexion) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            // Hora inicio
            String sql = "select fprecal_tiempo('" + horaInicio
                    + "','SH') from dual";
            rs = stm.executeQuery(sql);
            rs.next();
            setHoraInicioFormateada(rs.getString(1));
            ControlRecursos.liberarRecursos(rs, stm);
            // hora fin
            stm = conexion.createStatement();
            sql = "select fprecal_tiempo('" + horaFin + "','SH') from dual";
            rs = stm.executeQuery(sql);
            rs.next();
            setHoraFinFormateada(rs.getString(1));
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando horario formateado "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    /**
     * @return Returns the horaFinFormateada.
     */
    public String getHoraFinFormateada() {
        return horaFinFormateada;
    }

    /**
     * @param horaFinFormateada
     *            The horaFinFormateada to set.
     */
    public void setHoraFinFormateada(String horaFinFormateada) {
        this.horaFinFormateada = horaFinFormateada;
    }

    /**
     * @return Returns the horaInicioFormateada.
     */
    public String getHoraInicioFormateada() {
        return horaInicioFormateada;
    }

    /**
     * @param horaInicioFormateada
     *            The horaInicioFormateada to set.
     */
    public void setHoraInicioFormateada(String horaInicioFormateada) {
        this.horaInicioFormateada = horaInicioFormateada;
    }

    public int compareTo(Object o) {
        Programacion p = (Programacion) o;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
        try {
            Date d1 = formato.parse(p.fecha);
            Date d2 = formato.parse(this.fecha);
            if (d2.after(d1)) {
                return 1;
            } else if (d2.before(d1)) {
                return -1;
            } else {
                if (this.horaInicio > p.getHoraInicio()) {
                    return 1;
                } else if (this.horaInicio < p.getHoraInicio()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } catch (ParseException exc) {
            exc.printStackTrace();
            return 0;
        }
    }
}
