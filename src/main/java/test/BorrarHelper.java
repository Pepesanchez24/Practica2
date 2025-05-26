package test;

import dao.AcademiaDAO;
import dao.DAOFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;

public class BorrarHelper {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PROCESO DE LIMPIEZA DE BASE DE DATOS ===");

        String tipoDAO = obtenerTipoDAO();
        System.out.println("Tipo de DAO configurado: " + tipoDAO);

        try {
            if ("JDBC".equalsIgnoreCase(tipoDAO)) {
                borrarTablasJDBC();
            } else if ("JPA".equalsIgnoreCase(tipoDAO)) {
                borrarTablasJPA();
            }

            System.out.println("Limpieza de base de datos completada exitosamente");

        } catch (Exception e) {
            System.err.println("Error durante la limpieza: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== PROCESO DE LIMPIEZA FINALIZADO ===");
    }

    private static String obtenerTipoDAO() {
        try {
            Properties props = new Properties();
            InputStream input = BorrarHelper.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(input);
            return props.getProperty("dao.tipo", "JDBC");
        } catch (Exception e) {
            System.err.println("Error al cargar configuración, usando JDBC por defecto");
            return "JDBC";
        }
    }

    private static AcademiaDAO crearAcademiaDAO() {
        String tipo = obtenerTipoDAO();
        DAOFactory factory = DAOFactory.obtenerFactory(tipo);
        return factory.crearAcademiaDAO();
    }

    private static void borrarTablasJDBC() throws Exception {
        System.out.println("Borrando tablas usando JDBC (PostgreSQL)...");

        Properties props = new Properties();
        InputStream input = BorrarHelper.class.getClassLoader().getResourceAsStream("config.properties");
        props.load(input);

        String url = props.getProperty("jdbc.url");
        String usuario = props.getProperty("jdbc.usuario");
        String password = props.getProperty("jdbc.password");
        String driver = props.getProperty("jdbc.driver");

        Class.forName(driver);

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement stmt = conexion.createStatement()) {

            stmt.execute("SET CONSTRAINTS ALL DEFERRED");

            System.out.println("Borrando datos de la tabla matriculas...");
            stmt.executeUpdate("DELETE FROM matriculas");

            System.out.println("Borrando datos de la tabla alumnos...");
            stmt.executeUpdate("DELETE FROM alumnos");

            System.out.println("Borrando datos de la tabla cursos...");
            stmt.executeUpdate("DELETE FROM cursos");

            System.out.println("Reiniciando secuencias...");
            reiniciarSecuencias(stmt);

            System.out.println("Todas las tablas han sido limpiadas");
        }
    }

    private static void reiniciarSecuencias(Statement stmt) throws Exception {
        stmt.executeUpdate("ALTER SEQUENCE IF EXISTS matriculas_id_seq RESTART WITH 1");
        stmt.executeUpdate("ALTER SEQUENCE IF EXISTS alumnos_id_seq RESTART WITH 1");
        stmt.executeUpdate("ALTER SEQUENCE IF EXISTS cursos_id_seq RESTART WITH 1");
    }

    private static void borrarTablasJPA() throws Exception {
        System.out.println("Borrando datos usando JPA...");

        AcademiaDAO dao = crearAcademiaDAO();

        try {
            // Obtener y eliminar todas las matrículas
            System.out.println("Eliminando todas las matrículas...");
            dao.listarTodasLasMatriculas().forEach(matricula -> {
                dao.eliminarMatricula(matricula.getId());
            });

            // Obtener y eliminar todos los alumnos
            System.out.println("Eliminando todos los alumnos...");
            dao.listarTodosLosAlumnos().forEach(alumno -> {
                dao.eliminarAlumno(alumno.getId());
            });

            // Obtener y eliminar todos los cursos
            System.out.println("Eliminando todos los cursos...");
            dao.listarTodosLosCursos().forEach(curso -> {
                dao.eliminarCurso(curso.getId());
            });

            System.out.println("Todos los datos han sido eliminados usando JPA");

        } finally {
            dao.cerrar();
        }
    }

    public static void borrarTablasCompletamente() throws Exception {
        System.out.println("BORRANDO TABLAS COMPLETAMENTE (DROP TABLE)...");

        Properties props = new Properties();
        InputStream input = BorrarHelper.class.getClassLoader().getResourceAsStream("config.properties");
        props.load(input);

        String url = props.getProperty("jdbc.url");
        String usuario = props.getProperty("jdbc.usuario");
        String password = props.getProperty("jdbc.password");
        String driver = props.getProperty("jdbc.driver");

        Class.forName(driver);

        try (Connection conexion = DriverManager.getConnection(url, usuario, password);
             Statement stmt = conexion.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS matriculas CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS alumnos CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS cursos CASCADE");

            stmt.executeUpdate("DROP SEQUENCE IF EXISTS matriculas_id_seq CASCADE");
            stmt.executeUpdate("DROP SEQUENCE IF EXISTS alumnos_id_seq CASCADE");
            stmt.executeUpdate("DROP SEQUENCE IF EXISTS cursos_id_seq CASCADE");

            System.out.println("Todas las tablas y secuencias han sido eliminadas completamente");
        }
    }
}