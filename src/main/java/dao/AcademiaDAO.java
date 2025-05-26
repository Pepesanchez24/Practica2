package dao;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;
import java.util.List;

public interface AcademiaDAO {

	void insertarAlumno(Alumno alumno);
	void actualizarAlumno(Alumno alumno);
	void eliminarAlumno(int id);
	Alumno buscarAlumnoPorId(int id);
	List<Alumno> listarTodosLosAlumnos();

	void insertarCurso(Curso curso);
	void actualizarCurso(Curso curso);
	void eliminarCurso(int id);
	Curso buscarCursoPorId(int id);
	List<Curso> listarTodosLosCursos();

	void insertarMatricula(Matricula matricula);
	void actualizarMatricula(Matricula matricula);
	void eliminarMatricula(int id);
	Matricula buscarMatriculaPorId(int id);
	List<Matricula> listarTodasLasMatriculas();
	List<Matricula> buscarMatriculasPorAlumno(int alumnoId);
	List<Matricula> buscarMatriculasPorCurso(int cursoId);

	void cerrar();
}
