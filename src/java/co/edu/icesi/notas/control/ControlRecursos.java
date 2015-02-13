package co.edu.icesi.notas.control;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;

public class ControlRecursos {

    private static Map conexiones;
    private static DataSource dataSource;
    private static Context initContext;
    private static int conexionesAbiertas = 0;

    // Context
    public static void iniciarRecursos(String recurso) {
        try {
            if (dataSource == null) {
                initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:comp/env");
                dataSource = (DataSource) envContext.lookup(recurso);
            }
        } catch (NameNotFoundException e) {
            // No se encontró en el contexto de Tomcat
            try {
                dataSource = (DataSource) initContext.lookup(recurso);
            } catch (NamingException e1) {
                // No se encontró en el contexto inicial
                e1.printStackTrace();
            }
        } catch (NamingException e) {
            // Error desconocido con el contexto inicial
            e.printStackTrace();
        } finally {
            if (conexiones == null) {
                conexiones = new HashMap();
            }
            try {
                initContext.close();
            } catch (NamingException e) {
                // Error al cerrar el contexto inicial
                e.printStackTrace();
            }
        }
    }

    // Datasource struts-config.xml
    public static void iniciarRecursos(DataSource ds) {
        dataSource = ds;
        conexiones = new HashMap();
    }

    // Conexiones
    /*public static Connection obtenerConexion(String idUsuario) throws SQLException {
    Connection conexion = null;
    conexion = dataSource.getConnection();
    //Guardar los datos para el usuario actual
    ArrayList conexionesUsuario;
    if ((conexionesUsuario = (ArrayList) conexiones.get(idUsuario)) == null) {
    conexionesUsuario = new ArrayList();
    conexiones.put(idUsuario, conexionesUsuario);
    }
    conexionesUsuario.add(conexion);
    conexiones.put(idUsuario, conexionesUsuario);
    return conexion;
    }*/
    public static Connection obtenerConexion() throws SQLException {
        Connection conexion = null;
        conexion = dataSource.getConnection();
        aumentarConexionesAbiertas();
        return conexion;
    }

    /*public static void liberarRecursos(String idUsuario) {

    if (idUsuario != null && !idUsuario.trim().equals("") && conexiones != null) {
    ArrayList conexionesUsuario = (ArrayList) conexiones.get(idUsuario);
    for (Iterator i = conexionesUsuario.iterator(); i.hasNext();) {
    Connection conexion = (Connection) i.next();
    try {
    if (!conexion.isClosed()) {
    conexion.close();
    }
    } catch (SQLException e) {
    e.printStackTrace();
    }
    }
    conexionesUsuario.clear();
    }
    }*/
    public static void liberarRecursos(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                disminuirConexionesAbiertas();
            }
        } catch (SQLException e) {
            // Error en cierre de conexión
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar la conexión");
            e.printStackTrace();
        }
    }

    public static void listarConexiones() {
        System.out.println("Conexiones abiertas");
        Set keys = conexiones.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            Object key = it.next();
            ArrayList conexionesAbiertas = (ArrayList) conexiones.get(key);
            for (Iterator it1 = conexionesAbiertas.iterator(); it1.hasNext();) {
                Object object = it1.next();
                System.out.println(key.toString() + " - " + object);
            }
        }
    }

    public static void liberarRecursos(ResultSet rs, Statement stm) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // Error en cierre de result set
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar el result set");
            e.printStackTrace();
        }
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            // Error en cierre de statement
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar el statement");
            e.printStackTrace();
        }
    }

    public static void liberarRecursos(ResultSet rs, Statement stm,
            Connection con) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            // Error en cierre de result set
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar el result set");
            e.printStackTrace();
        }
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException e) {
            // Error en cierre de statement
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar el statement");
            e.printStackTrace();
        }
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            // Error en cierre de conexión
            System.out.println("Error: " + e.getMessage());
            System.out.println("Descripción:");
            System.out.println("Error al tratar de cerrar la conexión");
            e.printStackTrace();
        }
    }

    private static synchronized void aumentarConexionesAbiertas() {
        conexionesAbiertas++;
    }

    private static synchronized void disminuirConexionesAbiertas() {
        conexionesAbiertas--;
    }

    public static int getConexionesAbiertas() {
        return conexionesAbiertas;
    }
}
