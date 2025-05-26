CREATE DATABASE academia_db;

\c academia_db;

CREATE TABLE IF NOT EXISTS alumnos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    fecha_nacimiento DATE
);

CREATE TABLE IF NOT EXISTS cursos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    creditos INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS matriculas (
    id SERIAL PRIMARY KEY,
    alumno_id INTEGER NOT NULL,
    curso_id INTEGER NOT NULL,
    fecha_matricula DATE NOT NULL,
    nota DOUBLE PRECISION DEFAULT 0.0,
    FOREIGN KEY (alumno_id) REFERENCES alumnos(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    UNIQUE (alumno_id, curso_id)
);

CREATE INDEX IF NOT EXISTS idx_alumno_email ON alumnos(email);
CREATE INDEX IF NOT EXISTS idx_matricula_alumno ON matriculas(alumno_id);
CREATE INDEX IF NOT EXISTS idx_matricula_curso ON matriculas(curso_id);