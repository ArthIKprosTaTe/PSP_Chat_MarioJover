package interfaz;

import java.net.ServerSocket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import Comunicaciones.ComunicacionServidor;

/**
 * 
 * Clase PanelServidor que extiende de la clase JFrame e implementa la interfaz
 * Runnable. Esta clase es el panel principal del servidor, donde se muestran
 * los logs de la conexion de los clientes.
 * 
 * @author Mario Jover
 * 
 */
@SuppressWarnings("serial")
public class PanelServidor extends JFrame implements Runnable {

	/**
	 * Puerto utilizado para la comunicacion entre el servidor y los clientes.
	 */
	private final int PUERTO = 9999;
	/**
	 * Area de texto en la que se mostraran los mensajes recibidos por el servidor.
	 */
	private JTextArea taLogs;

	/**
	 * Constructor de la clase PanelServidor.
	 * 
	 * Este metodo se encarga de inicializar los componentes de la interfaz y de
	 * iniciar el hilo de escucha de clientes.
	 */

	public PanelServidor() {

		this.setBounds(700, 700, 580, 350);
		taLogs = new JTextArea();
		taLogs.setEditable(false);
		add(taLogs);
		setResizable(false);
		setVisible(true);

		Thread escuchaCliente = new Thread(this);
		escuchaCliente.start();
	}

	/**
	 * 
	 * Metodo run de la clase PanelServidor. Este metodo crea un objeto de la clase
	 * {@link ServerSocket} en el puerto 9999, y un objeto de la clase
	 * {@link ComunicacionServidor}. Luego, entra en un ciclo infinito en el que
	 * llama al metodo redireccionDeTexto del objeto redireccionMensajes, el cual
	 * redirige los mensajes recibidos desde el socket de escucha del servidor hacia
	 * el text area taLogs. En caso de que ocurra una excepcion, se imprime el
	 * mensaje de error correspondiente.
	 */
	@Override
	public void run() {

		try {

			ServerSocket socketEscuchaServer = new ServerSocket(PUERTO);
			ComunicacionServidor redireccionMensajes = new ComunicacionServidor();
			while (true) {

				redireccionMensajes.redireccionDeTexto(socketEscuchaServer, taLogs);

			}

		} catch (Exception e) {

			System.err.println(e.getMessage());
		}

	}

}
