package dao;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;
import javax.persistence.*;
import java.util.List;

public class AcademiaDAOImplJPA implements AcademiaDAO {

    private EntityManagerFactory emf;
    private EntityManager em;

    public AcademiaDAOImplJPA() {
        try {
            this.emf = Persistence.createEntityManagerFactory("academiaPU");
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar JPA", e);
        }
    }

    @Override
    public void insertarAlumno(Alumno alumno) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(alumno);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al insertar alumno", e);
        }
    }

    @Override
    public void actualizarAlumno(Alumno alumno) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(alumno);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar alumno", e);
        }
    }

    @Override
    public void eliminarAlumno(int id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Alumno alumno = em.find(Alumno.class, id);
            if (alumno != null) {
                em.remove(alumno);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar alumno", e);
        }
    }

    @Override
    public Alumno buscarAlumnoPorId(int id) {
        try {
            return em.find(Alumno.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar alumno", e);
        }
    }

    @Override
    public List<Alumno> listarTodosLosAlumnos() {
        try {
            TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a", Alumno.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar alumnos", e);
        }
    }

    @Override
    public void insertarCurso(Curso curso) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(curso);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al insertar curso", e);
        }
    }

    @Override
    public void actualizarCurso(Curso curso) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(curso);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar curso", e);
        }
    }

    @Override
    public void eliminarCurso(int id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Curso curso = em.find(Curso.class, id);
            if (curso != null) {
                em.remove(curso);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar curso", e);
        }
    }

    @Override
    public Curso buscarCursoPorId(int id) {
        try {
            return em.find(Curso.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar curso", e);
        }
    }

    @Override
    public List<Curso> listarTodosLosCursos() {
        try {
            TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c", Curso.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar cursos", e);
        }
    }

    @Override
    public void insertarMatricula(Matricula matricula) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(matricula);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al insertar matrícula", e);
        }
    }

    @Override
    public void actualizarMatricula(Matricula matricula) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(matricula);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar matrícula", e);
        }
    }

    @Override
    public void eliminarMatricula(int id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Matricula matricula = em.find(Matricula.class, id);
            if (matricula != null) {
                em.remove(matricula);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar matrícula", e);
        }
    }

    @Override
    public Matricula buscarMatriculaPorId(int id) {
        try {
            return em.find(Matricula.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar matrícula", e);
        }
    }

    @Override
    public List<Matricula> listarTodasLasMatriculas() {
        try {
            TypedQuery<Matricula> query = em.createQuery("SELECT m FROM Matricula m", Matricula.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar matrículas", e);
        }
    }

    @Override
    public List<Matricula> buscarMatriculasPorAlumno(int alumnoId) {
        try {
            TypedQuery<Matricula> query = em.createQuery(
                    "SELECT m FROM Matricula m WHERE m.alumnoId = :alumnoId", Matricula.class);
            query.setParameter("alumnoId", alumnoId);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar matrículas por alumno", e);
        }
    }

    @Override
    public List<Matricula> buscarMatriculasPorCurso(int cursoId) {
        try {
            TypedQuery<Matricula> query = em.createQuery(
                    "SELECT m FROM Matricula m WHERE m.cursoId = :cursoId", Matricula.class);
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar matrículas por curso", e);
        }
    }

    @Override
    public void cerrar() {
        try {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cerrar EntityManager", e);
        }
    }
}