/*
 * Created on 8/06/2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.control;

import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.*;

/**
 * Esta clase se encarga de controlar todas las acciones requeridas para la
 * implementacion de copias de esquemas de evaluación de otros cursos distintos
 * al actual.
 * 
 * @author ffceballos
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ControlCopiaEsquemas {

	private ArrayList cursosDepto;
	private Curso cursoAnterior;
        
     /*   //ffceballos
        private String cedulaProfesor;
        

        public String getCedulaProfesor() {
           return cedulaProfesor;
        }

        public void setCedulaProfesor(String cedulaProfesor) {
           this.cedulaProfesor = cedulaProfesor;
        }
        //ffceballos*/
	public ControlCopiaEsquemas() {
		super();
	}

	/**
	 * Este metodo se encarga de obtener el curso del periodo inmediatamente
	 * anterior.
	 * 
	 * @return Curso anterior
	 */
	public void obtenerCursoAnterior(ControlProfesores profesores,
			Connection conexion, Curso actual) throws Exception {
		// Para obtener el periodo academico anterior (junto con el
		// consecutivo)
		// No se tiene en cuenta consecutivos de 3. Ni cursos de verano.
		String periodoAnterior = profesores.getPeriodoAnterior();
		int consecAnterior = profesores.getConsecutivoAnterior();
		// Fin de obtencion de periodo y consecutivo academico

		// Se debe crear un control anterior para crear de nuevo un Curso

		ControlProfesores controlAnterior = new ControlProfesores();
		controlAnterior.setConsecutivo(consecAnterior);
		controlAnterior.setPeriodoAcademico(periodoAnterior);

		Profesor pro = new Profesor();
              //  pro.setCedula(cedulaProfesor);
		controlAnterior.setProfesor(pro);
		controlAnterior.setTiposCategoria(profesores.getTiposCategoria());
		pro.setCedula(profesores.getProfesor().getCedula());
             //   System.out.println("1111111111111111111111111111111111111111111111"+cedulaProfesor);
		cursoAnterior = pro.cargarUnCurso(conexion, actual
				.getConsecutivoMateria(), actual.getCodigoMateria(),
				periodoAnterior, consecAnterior);
		if (cursoAnterior != null) {
			cursoAnterior.cargarClasificacionesBd(conexion, controlAnterior);
			cursoAnterior.cargarCategorias(conexion, controlAnterior);
		}
	}

	/**
	 * Obtiene los cursos que estan inscritos a notas parciales y pertenecen al
	 * mismo departamento academico.
	 * 
	 */
	public void obtenerCursosDepto() {

	}

	/**
	 * Este método elimina todos los elementos de un Curso tanto en memoria como
	 * en la base de datos.
	 * 
	 */
	public void desaparecerCurso(Curso actual, Connection conexion) {
		// Eliminar en cascada
		// 1. Calificaciones
		// 2. Actividades
		// 3. Categorias
		// 4. Clasificaciones

	}
        
        
        //ffceballos --copiar un esquema en todos los grupos de una materia
        
        public void clonarEsquema(Curso cursoGuia, Connection conexion){
        
        
        String sql="SELECT c.periodo_periodo_acad, c.periodo_consecutivo, c.control_asistencia, c.CONSECUTIVO, c.DESCRIPMAT_CONSECUTIVO, c.DESCRIPMAT_CODIGO,fprebus_descrip_mat(c.descripmat_consecutivo,c.descripmat_codigo,'03',''), " +
"          c.CURSO_ACTU_GRUPO, c.ESTADO, c.TIPO_CONFIGURACION,c.ESCALA,  FPREBUS_MATERIAS(c.descripmat_codigo,'04','') as depto, c.regla_matematicas, c.regla_quiz " +
" FROM tntp_cursos c " +
"WHERE c.periodo_periodo_acad || c.periodo_consecutivo =fprebus_per_notp('0102','') "+//fprebus_constantes ('002', '0304', '')     " +
" AND (c.descripmat_codigo LIKE '%"+cursoGuia.getCodigoMateria()+"%' || '%' )     " +
" AND fprebus_materias (c.descripmat_codigo, '04', '')='08'   and   C.CURSO_ACTU_GRUPO != "+cursoGuia.getGrupo()+"       " +
" ORDER BY fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '')";
        
        Statement stm = null;
        ResultSet rs = null;
        
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
                
                int consecPeriodo=rs.getInt("periodo_consecutivo");
                String periodoAcademico=rs.getString("periodo_periodo_acad");
                
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo, codigoMateria, consecPeriodo, periodoAcademico,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                curso.setEscala(escala);
                 
                String depto=rs.getString("depto");
                curso.setDepartamento(depto);
                curso.setControlReglaMate(rs.getString("regla_matematicas"));
                curso.setControlReglaQuiz(rs.getString("regla_quiz"));
                
                
            
                
               // System.out.println("Repetitiva Materia: "+codigoMateria+"-"+grupo);
                realizarCopiaCurso(curso, cursoGuia, conexion);
               // System.out.println("Copiado esquema Materia: "+codigoMateria+"-"+grupo);
                         
            }
        
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
        
            
            ControlRecursos.liberarRecursos(rs, stm);
        
                
                
        
        }//ffceballos
        
        
        
        //ffceballos --copiar un esquema en todos los grupos de una materia
        
        public void copiarEsquema(Curso cursoGuia, Connection conexion, String grupoClonar){
        
        
        String sql="SELECT c.periodo_periodo_acad, c.periodo_consecutivo, c.control_asistencia, c.CONSECUTIVO, c.DESCRIPMAT_CONSECUTIVO, c.DESCRIPMAT_CODIGO,fprebus_descrip_mat(c.descripmat_consecutivo,c.descripmat_codigo,'03',''), " +
"          c.CURSO_ACTU_GRUPO, c.ESTADO, c.TIPO_CONFIGURACION,c.ESCALA,  FPREBUS_MATERIAS(c.descripmat_codigo,'04','') as depto, c.regla_matematicas, c.regla_quiz" +
" FROM tntp_cursos c " +
"WHERE c.periodo_periodo_acad || c.periodo_consecutivo =fprebus_per_notp('0102','')  "+//fprebus_constantes ('002', '0304', '')     " +
" AND (c.descripmat_codigo LIKE '%"+cursoGuia.getCodigoMateria()+"%' || '%' )     " +
" AND fprebus_materias (c.descripmat_codigo, '04', '')='08'   and   C.CURSO_ACTU_GRUPO = "+grupoClonar+"       " +
" ORDER BY fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '')";
        
        Statement stm = null;
        ResultSet rs = null;
        
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
                
                int consecPeriodo=rs.getInt("periodo_consecutivo");
                String periodoAcademico=rs.getString("periodo_periodo_acad");
                
                Curso curso = new Curso(consecCurso, grupo, materiaConsecutivo, codigoMateria, consecPeriodo, periodoAcademico,
                        descripcionMateria, est, tipo);
                curso.setControlAsistencia(controlAsistencia);
                curso.setEscala(escala);
                 
                String depto=rs.getString("depto");
                curso.setDepartamento(depto);
                curso.setControlReglaMate(rs.getString("regla_matematicas"));
                curso.setControlReglaQuiz(rs.getString("regla_quiz"));
            
                
               // System.out.println("Repetitiva Materia: "+codigoMateria+"-"+grupo);
                realizarCopiaCurso(curso, cursoGuia, conexion);
               // System.out.println("Copiado esquema Materia: "+codigoMateria+"-"+grupo);
                         
            }
        
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
        
            
            ControlRecursos.liberarRecursos(rs, stm);
        
                
                
        
        }//ffceballos
        
        
        
        //ffceballos borrar TODO el esquema de un curso
        
        public void eliminarEsquemas(Curso curso, Connection conexion){
          
            String codigoMateria=curso.getCodigoMateria();
            String grupo=curso.getGrupo();
            
            String sql="begin pprebor_eliminaresquemas('"+codigoMateria+"',"+grupo+"); end;";//peliminarEsquemas
            //System.out.println("******************************************************************************************"+sql);
            Statement stm = null;
            ResultSet rs = null;
            //int filasAfectadas
            try {
                
            stm = conexion.createStatement();
            boolean a=stm.execute(sql);
            if(a){
                //System.out.println("***************************************************************Si ejecutaaaaaa");
            }
                
                
                } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
   
        
            
            ControlRecursos.liberarRecursos(rs, stm);
            }
            
        }//ffceballos
        
        
       
        
        //ffceballos 30/09/2013 reiniciar el esquema de un grupo determinado de una materia 
        
        public void reiniciarEsquema(Curso curso, Connection conexion){
          
            String codigoMateria=curso.getCodigoMateria();
            String grupo=curso.getGrupo();
            
            String sql="begin pprebor_reiniciaresquema('"+codigoMateria+"',"+grupo+"); end;";
            
            Statement stm = null;
            ResultSet rs = null;
            
            try {
                
            stm = conexion.createStatement();
            stm.execute(sql);
                          
                } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
   
        
            
            ControlRecursos.liberarRecursos(rs, stm);
            }
            
        }//ffceballos
        
        
          //ffceballos 17/10/2013 reiniciar el esquema de un grupo determinado de una materia 
        
        public void eliminarEsquema(String codigoMateria, String grupo, Connection conexion){
              
            String sql="begin pprebor_reiniciaresquema('"+codigoMateria+"',"+grupo+"); end;";
            
            Statement stm = null;
            ResultSet rs = null;
            
            try {
                
            stm = conexion.createStatement();
            stm.execute(sql);
                          
                } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
   
        
            
            ControlRecursos.liberarRecursos(rs, stm);
            }
            
        }//ffceballos
        
        
        
        

	/**
	 * Este método se encarga de realizar una copia exacta del Curso anterior,
	 * clonando sus elementos y pasándoselos al actual.
	 * 
	 * @param actual
	 * @param anterior
	 *            Puede ser el mismo curso del periodo anterior o puede ser otro
	 *            curso del mismo departamento.
	 */
	public void realizarCopiaCurso(Curso actual, Curso anterior,
			Connection conexion) {
		actual.setCategorias(anterior.getCategorias());
		actual.setIndividuales(anterior.getIndividuales());
		actual.setGrupales(anterior.getGrupales());

		// Asignar el curso a las calificaciones
		actual.getIndividuales().setCurso(actual);
		actual.getGrupales().setCurso(actual);

		// Aqui se guardan en la base de datos tanto la agrupacion individual
		// como la grupal
		actual.getIndividuales().guardarBD(conexion);
		actual.getGrupales().guardarBD(conexion);

		/*
		 * Modificación realizada por mzapata el 25-Jun-07 Se copian otros
		 * atributos del curso como el control de asistencia y la escala
		 */
		if (!actual.getControlAsistencia().equals(
				anterior.getControlAsistencia())) {
			actual.setControlAsistencia(anterior.getControlAsistencia());
			actual.actualizarControlAsistencia(conexion);
		}
		if (actual.getEscala() != anterior.getEscala()) {
			actual.actualizarEscala(conexion, anterior.getEscala());
		}
		if (!actual.getTipoConfiguracion().equals(
				anterior.getTipoConfiguracion())) {
			actual.setTipoConfiguracion(anterior.getTipoConfiguracion());
			actual.actualizarTipoConfiguracion(conexion);
		}
                
		// Fin de la modificación
                
                
                //ffceballos
                
                String controlMateActual=actual.getControlReglaMate();
                String controlMateAnterior=anterior.getControlReglaMate();
                
              // System.out.println("Actual: "+controlMateActual);
              //  System.out.println("\n Anterior: "+controlMateAnterior);
                
                if (!(controlMateActual.equals(controlMateAnterior))) {                   
                    
                  //  System.out.println("************************************entro a cambiar el control de matematicas*******************");
                    
			actual.setControlReglaMate(anterior.getControlReglaMate());
			actual.actualizarControlReglaMate(conexion);
		}
                
                
                //ffceballos
                
                
                 //ffceballos
                
                String controlQuizActual=actual.getControlReglaQuiz();
                String controlQuizAnterior=anterior.getControlReglaQuiz();
                               
                if (!(controlQuizActual.equals(controlQuizAnterior))) {                   
                    
			actual.setControlReglaQuiz(anterior.getControlReglaQuiz());
			actual.actualizarControlReglaQuiz(conexion);
		}
                
                
                //ffceballos
                

		actual.cargarCategoriasClasificaciones();
		// recorrer las categorias y sus actividades cambiandoles los valores de
		for (int cont = 0; cont < actual.getCategorias().size(); cont++) {
			Categoria temp = (Categoria) actual.getCategorias().get(cont);
			temp.guardarBd(conexion);
			for (int cont2 = 0; cont2 < temp.getActividades().size(); cont2++) {
				Actividad est = (Actividad) temp.getActividades().get(cont2);
				est.setMatriculas(new ArrayList());
				est.setCategoria(temp);
				est.guardarBd(conexion);
				for (int k = 0; k < actual.getAlumnos().size(); k++) {
					Matricula matricula = new Matricula(actual, (Alumno) actual
							.getAlumnos().get(k));
					matricula.setActividad(est);
					// matricula.cargarCalificacion(conexion);
					est.getMatriculas().add(matricula);
				}

			}

		}
	}

	/**
	 * @return Returns the cursoAnterior.
	 */
	public Curso getCursoAnterior() {
		return cursoAnterior;
	}

	/**
	 * @param cursoAnterior
	 *            The cursoAnterior to set.
	 */
	public void setCursoAnterior(Curso cursoAnterior) {
		this.cursoAnterior = cursoAnterior;
	}

	/**
	 * @return Returns the cursosDepto.
	 */
	public ArrayList getCursosDepto() {
		return cursosDepto;
	}

	/**
	 * @param cursosDepto
	 *            The cursosDepto to set.
	 */
	public void setCursosDepto(ArrayList cursosDepto) {
		this.cursosDepto = cursosDepto;
	}
}
