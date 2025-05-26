package dao;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class AcademiaDAOImplJDBC implements AcademiaDAO {

    private Connection conexion;

    public AcademiaDAOImplJDBC() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo config.properties en el classpath.");
            }

            props.load(input);

            String url = props.getProperty("jdbc.url");
            String usuario = props.getProperty("jdbc.usuario");
            String password = props.getProperty("jdbc.password");
            String driver = props.getProperty("jdbc.driver");

            Class.forName(driver);
            this.conexion = DriverManager.getConnection(url, usuario, password);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver JDBC especificado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    @Override
    public void insertarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (nombre, email, fecha_nacimiento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getEmail());
            stmt.setDate(3, new java.sql.Date(alumno.getFechaNacimiento().getTime()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                alumno.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar alumno", e);
        }
    }

    @Override
    public void actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombre = ?, email = ?, fecha_nacimiento = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getEmail());
            stmt.setDate(3, new java.sql.Date(alumno.getFechaNacimiento().getTime()));
            stmt.setInt(4, alumno.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar alumno", e);
        }
    }

    @Override
    public void eliminarAlumno(int id) {
        String sql = "DELETE FROM alumnos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar alumno", e);
        }
    }

    @Override
    public Alumno buscarAlumnoPorId(int id) {
        String sql = "SELECT * FROM alumnos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getInt("id"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setEmail(rs.getString("email"));
                alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                return alumno;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alumno", e);
        }
        return null;
    }

    @Override
    public List<Alumno> listarTodosLosAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getInt("id"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setEmail(rs.getString("email"));
                alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar alumnos", e);
        }
        return alumnos;
    }

    @Override
    public void insertarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (nombre, descripcion, creditos) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getCreditos());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                curso.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar curso", e);
        }
    }

    @Override
    public void actualizarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nombre = ?, descripcion = ?, creditos = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getCreditos());
            stmt.setInt(4, curso.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar curso", e);
        }
    }

    @Override
    public void eliminarCurso(int id) {
        String sql = "DELETE FROM cursos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar curso", e);
        }
    }

    @Override
    public Curso buscarCursoPorId(int id) {
        String sql = "SELECT * FROM cursos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNombre(rs.getString("nombre"));
                curso.setDescripcion(rs.getString("descripcion"));
                curso.setCreditos(rs.getInt("creditos"));
                return curso;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar curso", e);
        }
        return null;
    }

    @Override
    public List<Curso> listarTodosLosCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNombre(rs.getString("nombre"));
                curso.setDescripcion(rs.getString("descripcion"));
                curso.setCreditos(rs.getInt("creditos"));
                cursos.add(curso);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cursos", e);
        }
        return cursos;
    }

    @Override
    public void insertarMatricula(Matricula matricula) {
        String sql = "INSERT INTO matriculas (alumno_id, curso_id, fecha_matricula, nota) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, matricula.getAlumnoId());
            stmt.setInt(2, matricula.getCursoId());
            stmt.setDate(3, new java.sql.Date(matricula.getFechaMatricula().getTime()));
            stmt.setDouble(4, matricula.getNota());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                matricula.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar matrícula", e);
        }
    }

    @Override
    public void actualizarMatricula(Matricula matricula) {
        String sql = "UPDATE matriculas SET alumno_id = ?, curso_id = ?, fecha_matricula = ?, nota = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, matricula.getAlumnoId());
            stmt.setInt(2, matricula.getCursoId());
            stmt.setDate(3, new java.sql.Date(matricula.getFechaMatricula().getTime()));
            stmt.setDouble(4, matricula.getNota());
            stmt.setInt(5, matricula.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar matrícula", e);
        }
    }

    @Override
    public void eliminarMatricula(int id) {
        String sql = "DELETE FROM matriculas WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar matrícula", e);
        }
    }

    @Override
    public Matricula buscarMatriculaPorId(int id) {
        String sql = "SELECT * FROM matriculas WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id"));
                matricula.setAlumnoId(rs.getInt("alumno_id"));
                matricula.setCursoId(rs.getInt("curso_id"));
                matricula.setFechaMatricula(rs.getDate("fecha_matricula"));
                matricula.setNota(rs.getDouble("nota"));
                return matricula;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar matrícula", e);
        }
        return null;
    }

    @Override
    public List<Matricula> listarTodasLasMatriculas() {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matriculas";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id"));
                matricula.setAlumnoId(rs.getInt("alumno_id"));
                matricula.setCursoId(rs.getInt("curso_id"));
                matricula.setFechaMatricula(rs.getDate("fecha_matricula"));
                matricula.setNota(rs.getDouble("nota"));
                matriculas.add(matricula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar matrículas", e);
        }
        return matriculas;
    }

    @Override
    public List<Matricula> buscarMatriculasPorAlumno(int alumnoId) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matriculas WHERE alumno_id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, alumnoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id"));
                matricula.setAlumnoId(rs.getInt("alumno_id"));
                matricula.setCursoId(rs.getInt("curso_id"));
                matricula.setFechaMatricula(rs.getDate("fecha_matricula"));
                matricula.setNota(rs.getDouble("nota"));
                matriculas.add(matricula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar matrículas por alumno", e);
        }
        return matriculas;
    }

    @Override
    public List<Matricula> buscarMatriculasPorCurso(int cursoId) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matriculas WHERE curso_id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id"));
                matricula.setAlumnoId(rs.getInt("alumno_id"));
                matricula.setCursoId(rs.getInt("curso_id"));
                matricula.setFechaMatricula(rs.getDate("fecha_matricula"));
                matricula.setNota(rs.getDouble("nota"));
                matriculas.add(matricula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar matrículas por curso", e);
        }
        return matriculas;
    }

    @Override
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexión", e);
        }
    }
}