package App;

import javax.swing.JFrame;
import login.PanelDecidir;

/**
 * 
 * Clase que representa el cliente en la aplicacion.
 * 
 * Esta clase se encarga de crear una interfaz grafica para el usuario
 * (PanelCliente)
 * 
 * y establecer su comportamiento al cerrarse (JFrame.EXIT_ON_CLOSE).
 * 
 * @author Mario Jover
 */
public class Client {

	public static void main(String[] args) {
	
		PanelDecidir reg = new PanelDecidir();
		reg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
	}

}
