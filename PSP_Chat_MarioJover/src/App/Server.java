package App;

import javax.swing.JFrame;


import interfaz.PanelServidor;

/**
 * 
 * Clase que representa el servidor en la aplicacion.
 * 
 * Esta clase se encarga de crear una interfaz grafica para el usuario
 * (PanelServidor)
 * 
 * y establecer su comportamiento al cerrarse (JFrame.EXIT_ON_CLOSE).
 * 
 * @author Mario Jover
 */
public class Server {

	public static void main(String[] args) {
		PanelServidor interfazCliente = new PanelServidor();	
		interfazCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}

}
