package dao;

public abstract class DAOFactory {

    public static final String JDBC_FACTORY = "JDBC";
    public static final String JPA_FACTORY = "JPA";

    public abstract AcademiaDAO crearAcademiaDAO();

    public static DAOFactory obtenerFactory(String tipo) {
        switch (tipo.toUpperCase()) {
            case JDBC_FACTORY:
                return new JDBCAOFactory();
            case JPA_FACTORY:
                return new JPADAOFactory();
            default:
                throw new IllegalArgumentException("Tipo de factory no soportado: " + tipo);
        }
    }
}