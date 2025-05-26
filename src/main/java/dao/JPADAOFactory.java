package dao;

public class JPADAOFactory extends DAOFactory {

    @Override
    public AcademiaDAO crearAcademiaDAO() {
        return new AcademiaDAOImplJPA();
    }
}