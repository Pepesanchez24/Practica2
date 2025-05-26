package entidades;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "alumno_id", nullable = false)
    private int alumnoId;

    @Column(name = "curso_id", nullable = false)
    private int cursoId;

    @Column(name = "fecha_matricula", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaMatricula;

    @Column(name = "nota")
    private double nota;

    public Matricula() {}

    public Matricula(int alumnoId, int cursoId, Date fechaMatricula, double nota) {
        this.alumnoId = alumnoId;
        this.cursoId = cursoId;
        this.fechaMatricula = fechaMatricula;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        this.cursoId = cursoId;
    }

    public Date getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(Date fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", alumnoId=" + alumnoId +
                ", cursoId=" + cursoId +
                ", fechaMatricula=" + fechaMatricula +
                ", nota=" + nota +
                '}';
    }
}