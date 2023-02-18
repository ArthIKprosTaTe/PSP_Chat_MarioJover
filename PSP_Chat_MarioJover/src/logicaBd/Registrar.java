package logicaBd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Esta clase se encarga de registrar y autenticar al cliente en el servidor
 * 
 * @author mario *
 */
public class Registrar {
	
	/**
	 * Metodo sincronizado que consulta en el servidor si el usuario existe o no
	 * utilizando alias y contraseña devolvera true si se encuentra si no, false
	 * 
	 * @param alias
	 * @param contraseña
	 * @return boolean
	 */
	
	
	public synchronized Boolean comprobarRegistro(String alias, String contrasena) {

		try {
			String sql = "SELECT * FROM registros WHERE alias = ? AND contraseña = ?";

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/psp?user=root&password=sasa");
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, alias);
			statement.setString(2, contrasena);
			ResultSet rs = statement.executeQuery();
			
			if (rs != null && rs.next()) {
				return true;
			}
			connection.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Metodo sincronizado que registra a un usuario en la base de datos, si ya esta registrado
	 * devuelve un mensaje, si no lo registra y devuelve una confirmacion.
	 * 
	 * @param registrado
	 * @param alias
	 * @param contraseña
	 * @return String
	 */
	public synchronized String registrar(Boolean registrado, String alias, String contrasena) {

		if (!registrado) {

			try {
				String sql = "INSERT INTO registros (alias, contraseña) VALUES (?, ?)";

				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/psp?user=root&password=sasa");
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, alias);
				statement.setString(2, contrasena);
				statement.executeUpdate();
				connection.close();
				statement.close();
				return "registrado";

			} catch (SQLException ex) {
				ex.printStackTrace();
			}

		}
		return "no registrado";
	}

}
