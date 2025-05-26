package test;

import dao.AcademiaDAO;
import dao.DAOFactory;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStream;

public class InsertarHelper {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO INSERCIÓN DE DATOS DE PRUEBA ===");

        String tipoDAO = obtenerTipoDAO();
        System.out.println("Tipo de DAO configurado: " + tipoDAO);

        AcademiaDAO dao = crearAcademiaDAO();

        try {
            insertarAlumnos(dao);
            insertarCursos(dao);
            insertarMatriculas(dao);
            mostrarResumen(dao);
            System.out.println("Inserción de datos completada exitosamente");

        } catch (Exception e) {
            System.err.println("Error durante la inserción: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dao.cerrar();
        }

        System.out.println("=== PROCESO DE INSERCIÓN FINALIZADO ===");
    }

    private static String obtenerTipoDAO() {
        try {
            Properties props = new Properties();
            InputStream input = InsertarHelper.class.getClassLoader().getResourceAsStream("config.properties");
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

    private static void insertarAlumnos(AcademiaDAO dao) {
        System.out.println("\n--- Insertando Alumnos ---");

        Calendar cal = Calendar.getInstance();
        Map<String, Alumno> existentes = dao.listarTodosLosAlumnos().stream()
                .collect(Collectors.toMap(Alumno::getEmail, a -> a));

        insertarAlumno(dao, existentes, "Ana García López", "ana.garcia@email.com", 1995, Calendar.MARCH, 15);
        insertarAlumno(dao, existentes, "Carlos Rodríguez Martín", "carlos.rodriguez@email.com", 1998, Calendar.JULY, 22);
        insertarAlumno(dao, existentes, "María Fernández Silva", "maria.fernandez@email.com", 1997, Calendar.NOVEMBER, 8);
        insertarAlumno(dao, existentes, "David González Ruiz", "david.gonzalez@email.com", 1996, Calendar.JANUARY, 30);
        insertarAlumno(dao, existentes, "Laura Sánchez Torres", "laura.sanchez@email.com", 1999, Calendar.MAY, 12);
    }

    private static void insertarAlumno(AcademiaDAO dao, Map<String, Alumno> existentes,
                                       String nombre, String email, int año, int mes, int dia) {
        if (existentes.containsKey(email)) {
            System.out.println("↪ Ya existe: " + nombre);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(año, mes, dia);
            Alumno alumno = new Alumno(nombre, email, cal.getTime());
            dao.insertarAlumno(alumno);
            System.out.println("✓ Insertado: " + nombre);
        }
    }

    private static void insertarCursos(AcademiaDAO dao) {
        System.out.println("\n--- Insertando Cursos ---");

        Map<String, Curso> existentes = dao.listarTodosLosCursos().stream()
                .collect(Collectors.toMap(Curso::getNombre, c -> c));

        insertarCurso(dao, existentes, "Programación Java Básica",
                "Introducción a la programación orientada a objetos con Java. Conceptos fundamentales, sintaxis básica y buenas prácticas.", 6);

        insertarCurso(dao, existentes, "Base de Datos Relacionales",
                "Diseño y gestión de bases de datos relacionales. SQL, normalización y optimización de consultas.", 8);

        insertarCurso(dao, existentes, "Desarrollo Web Frontend",
                "Creación de interfaces web modernas con HTML5, CSS3 y JavaScript. Frameworks y responsive design.", 7);

        insertarCurso(dao, existentes, "Algoritmos y Estructuras de Datos",
                "Estudio de algoritmos fundamentales y estructuras de datos. Análisis de complejidad y optimización.", 9);

        insertarCurso(dao, existentes, "Sistemas Operativos",
                "Fundamentos de sistemas operativos. Procesos, memoria, sistemas de archivos y concurrencia.", 8);
    }

    private static void insertarCurso(AcademiaDAO dao, Map<String, Curso> existentes,
                                      String nombre, String descripcion, int creditos) {
        if (existentes.containsKey(nombre)) {
            System.out.println("↪ Ya existe: " + nombre);
        } else {
            Curso curso = new Curso(nombre, descripcion, creditos);
            dao.insertarCurso(curso);
            System.out.println("✓ Insertado: " + nombre);
        }
    }

    private static void insertarMatriculas(AcademiaDAO dao) {
        System.out.println("\n--- Insertando Matrículas ---");

        List<Alumno> alumnos = dao.listarTodosLosAlumnos();
        List<Curso> cursos = dao.listarTodosLosCursos();
        List<Matricula> matriculasExistentes = dao.listarTodasLasMatriculas();

        Set<String> existentes = matriculasExistentes.stream()
                .map(m -> m.getAlumnoId() + "-" + m.getCursoId())
                .collect(Collectors.toSet());

        Map<String, Alumno> alumnoPorEmail = alumnos.stream()
                .collect(Collectors.toMap(Alumno::getEmail, a -> a));
        Map<String, Curso> cursoPorNombre = cursos.stream()
                .collect(Collectors.toMap(Curso::getNombre, c -> c));

        Calendar cal = Calendar.getInstance();

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "ana.garcia@email.com", "Programación Java Básica", 2024, Calendar.FEBRUARY, 1, 8.5);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "ana.garcia@email.com", "Base de Datos Relacionales", 2024, Calendar.FEBRUARY, 15, 9.0);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "carlos.rodriguez@email.com", "Programación Java Básica", 2024, Calendar.MARCH, 1, 7.5);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "carlos.rodriguez@email.com", "Desarrollo Web Frontend", 2024, Calendar.MARCH, 10, 8.0);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "maria.fernandez@email.com", "Algoritmos y Estructuras de Datos", 2024, Calendar.APRIL, 5, 9.5);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "david.gonzalez@email.com", "Sistemas Operativos", 2024, Calendar.APRIL, 15, 7.0);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "laura.sanchez@email.com", "Base de Datos Relacionales", 2024, Calendar.MAY, 1, 8.8);

        insertarMatricula(dao, existentes, alumnoPorEmail, cursoPorNombre,
                "laura.sanchez@email.com", "Desarrollo Web Frontend", 2024, Calendar.MAY, 10, 9.2);
    }

    private static void insertarMatricula(AcademiaDAO dao, Set<String> existentes,
                                          Map<String, Alumno> alumnos, Map<String, Curso> cursos,
                                          String emailAlumno, String nombreCurso, int año, int mes, int dia, double nota) {
        Alumno alumno = alumnos.get(emailAlumno);
        Curso curso = cursos.get(nombreCurso);

        if (alumno == null || curso == null) {
            System.out.println("No se puede insertar matrícula. Alumno o curso no encontrado.");
            return;
        }

        String clave = alumno.getId() + "-" + curso.getId();
        if (existentes.contains(clave)) {
            System.out.println("Ya existe matrícula: " + alumno.getNombre() + " en " + curso.getNombre());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(año, mes, dia);
            Matricula m = new Matricula(alumno.getId(), curso.getId(), cal.getTime(), nota);
            dao.insertarMatricula(m);
            System.out.println("Matrícula: " + alumno.getNombre() + " en " + curso.getNombre());
        }
    }

    private static void mostrarResumen(AcademiaDAO dao) {
        System.out.println("\n=== RESUMEN DE DATOS INSERTADOS ===");

        List<Alumno> alumnos = dao.listarTodosLosAlumnos();
        List<Curso> cursos = dao.listarTodosLosCursos();
        List<Matricula> matriculas = dao.listarTodasLasMatriculas();

        System.out.println("Total de alumnos: " + alumnos.size());
        System.out.println("Total de cursos: " + cursos.size());
        System.out.println("Total de matrículas: " + matriculas.size());

        System.out.println("\n--- Lista de Alumnos ---");
        alumnos.forEach(alumno -> System.out.println("• " + alumno.getNombre() + " (" + alumno.getEmail() + ")"));

        System.out.println("\n--- Lista de Cursos ---");
        cursos.forEach(curso -> System.out.println("• " + curso.getNombre() + " - " + curso.getCreditos() + " créditos"));

        System.out.println("\n--- Matrículas por Alumno ---");
        alumnos.forEach(alumno -> {
            List<Matricula> matriculasAlumno = dao.buscarMatriculasPorAlumno(alumno.getId());
            System.out.println("• " + alumno.getNombre() + ": " + matriculasAlumno.size() + " matrículas");
        });
    }
}