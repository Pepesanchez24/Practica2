package dao;

public class JDBCAOFactory extends DAOFactory {

    @Override
    public AcademiaDAO crearAcademiaDAO() {
        return new AcademiaDAOImplJDBC();
    }
}