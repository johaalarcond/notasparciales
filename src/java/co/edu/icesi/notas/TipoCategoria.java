/*
 * Created on 02-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import co.edu.icesi.notas.control.ControlRecursos;

/**
 * Representa el tipo de categoría a la cual pertenece un objeto Categoria.
 * Estos tipos de categoría están asociados a un departamento académico.
 * 
 * @author mzapata, lmdiaz
 */
public class TipoCategoria implements Serializable {

    private int consecutivo;
    private String nombre;
    private String departamentoAcademico;
    private String estado;

    /**
     * Guarda la categoría en la base de datos.
     *
     * @param conexion
     *            Conexión a la base de datos
     */
    public void guardarBD(Connection conexion) {
        Statement stm = null;
        try {
            String sql = "insert into tntp_tipo_categoria(nombre,dpto_academico,estado)";
            sql += " values ('" + nombre + "','" + departamentoAcademico
                    + "','" + estado + "')";
            stm = conexion.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error guardando el tipo de categoría "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(null, stm);
    }

    /**
     * Consulta a la base de datos para determinar el consecutivo del
     * TipoCategoria actual a partir del departamento académico y su nombre.
     *
     * @param conexion
     *            Conexión a la base de datos
     * @return Consecutivo del TipoCategoria actual.
     */
    public int getConsecutivoTipoCateg(Connection conexion) {
        int cons = 0;
        String sql = "select consecutivo " + "from tntp_tipo_categoria "
                + "where nombre='" + this.nombre + "' and dpto_academico="
                + this.departamentoAcademico;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                cons = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando el consecutivo del siguiente tipo de categoría "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
        return cons;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamentoAcademico() {
        return departamentoAcademico;
    }

    public void setDepartamentoAcademico(String departamentoAcademico) {
        this.departamentoAcademico = departamentoAcademico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoCategoria(int consecutivo, String nombre, String depto,
            String estado) {
        this.consecutivo = consecutivo;
        this.nombre = nombre;
        this.departamentoAcademico = depto;
        this.estado = estado;
    }

    public TipoCategoria(int consecutivo, String nombre) {
        this.consecutivo = consecutivo;
        this.nombre = nombre;

    }

    public TipoCategoria() {
        super();
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public int hashCode() {
        return consecutivo;
    }

    /**
     * Determina si dos objetos TipoCategoria son iguales, comparando sus
     * consecutivos.
     *
     * @return true si los objetos son iguales.
     */
    public boolean equals(Object obj) {
        if (obj instanceof TipoCategoria) {
            TipoCategoria calif = (TipoCategoria) obj;
            return calif.consecutivo == this.consecutivo;
        }
        return false;
    }

    /**
     * Retorna el nombre del TipoCategoria.
     *
     * @return Nombre de TipoCategoria actual
     */
    public String toString() {
        return nombre;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }
}
