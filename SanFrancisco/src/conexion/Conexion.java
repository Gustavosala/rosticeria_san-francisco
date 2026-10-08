package conexion;

	import java.sql.*;

	public class Conexion {

	    private static final String URL = "jdbc:mysql://localhost:3306/polleria";
	    private static final String USUARIO = "root";
	    private static final String PASSWORD = "1234";

	    public static Connection conectar() {

	        Connection conexion = null;

	        try {
	            conexion = DriverManager.getConnection(
	                URL,
	                USUARIO,
	                PASSWORD
	            );

	            System.out.println("Conexion exitosa a MySQL");

	        } catch (SQLException e) {
	            System.out.println("Error al conectar:");
	            e.printStackTrace();
	        }

	        return conexion;
	    }
	}
