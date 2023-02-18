package interfaz;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Comunicaciones.ComunicacionCliente;
import pojo.DatosUsuario;
import seguridad.Cifrado;

/**
 * 
 * Clase PanelCliente que extiende de JFrame y implementa la interfaz Runnable.
 * 
 * En esta clase se define la parte grafica y el funcionamiento del cliente
 * 
 * en una aplicacion de chat en tiempo real.
 * 
 * @author Mario Jover
 * 
 * 
 */
@SuppressWarnings("serial")
public class PanelCliente extends JFrame implements Runnable {
	// VARIABLES A CAMBIAR
	/**
	 * Direccion Ip de servidor
	 */
	private static final String IP_SERVIDOR = "192.168.1.141";
	/**
	 * Puerto por el que el cliente envia datos al servidor
	 */
	private static final int PUERTOENVIA = 9999;
	/**
	 * Puerto por el que el cliente escucha al servidor
	 */
	private static final int PUERTOESCUCHA = 9090;
	/**
	 * Alias del cliente
	 */
	
	/**
	 * Area de texto del mensaje del cliente y area de texto para la ip del
	 * destinatario
	 */
	private JTextField tfMensajeCliente, tfIp;
	/**
	 * Boton para realizar la accion de enviar el texto
	 */
	private JButton btnEnviar;
	/**
	 * Area de texto del chat
	 */
	private JTextArea taConversacion;
	/**
	 * Titulo de la ventana y alias del cliente
	 */
	private JLabel titulo, laAlias;
	/**
	 * Objeto de la clase Cifrado, se usa para cifrar y descifrar los paquetes de
	 * datos
	 */
	Cifrado encriptador = new Cifrado();
	/**
	 * Clave secreta para descifrar y cifrar los paquetes de datos
	 */
	String clave = "2B7E151628AED2A6ABF7158809CF4F3C";

	/**
	 * 
	 * Constructor para la clase PanelCliente.
	 * 
	 * Crea un panel para el cliente en la aplicacion de chat.
	 * 
	 * Se establecen las dimensiones y caracteristicas de la ventana.
	 * 
	 * Se pide al usuario su alias y se inicializa el hilo del cliente.
	 * 
	 * 
	 */
	public PanelCliente(String alias) {

		setBounds(600, 300, 280, 350);

		this.setLayout(new FlowLayout());
		/**
		 * Comprobacion del alias, si es nulo vuelve a aparecer para que lo introduzca
		 */
		
		laAlias = new JLabel();
		laAlias.setText(alias);
		add(laAlias);

		titulo = new JLabel("IP Destino:");
		add(titulo);

		tfIp = new JTextField(10);
		add(tfIp);

		taConversacion = new JTextArea(12, 20);
		taConversacion.setEditable(false);
		add(taConversacion);
		taConversacion.setPreferredSize(new Dimension(200, 100));

		tfMensajeCliente = new JTextField(20);
		add(tfMensajeCliente);

		btnEnviar = new JButton("Enviar");

		btnEnviar.addActionListener(new ActionEnviarTexto());
		add(btnEnviar);

		Thread hiloCiente = new Thread(this);
		hiloCiente.start();
		setVisible(true);
		setResizable(false);
		addWindowListener(new AvisoClienteOnline());

	}

	/**
	 * Clase que implementa la accion de enviar texto. Al presionar el boton
	 * "Enviar", se ejecutara esta accion y se enviara el texto escrito en el cuadro
	 * de texto correspondiente al mensaje del cliente.
	 * 
	 * @author Mario Jover
	 * 
	 */
	class ActionEnviarTexto implements ActionListener {
		/**
		 * Instancia de la clase ComunicacionCliente
		 */
		ComunicacionCliente comunicacion = new ComunicacionCliente();

		/**
		 * Metodo que se ejecuta al presionar el boton "Enviar". Este metodo agrega el
		 * mensaje enviado por el cliente a la conversacion y lo envia al servidor a
		 * traves de la clase ComunicacionCliente.
		 * 
		 * @param e ActionEvent que representa la accion de presionar el boton "Enviar".
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			taConversacion.append("\n" + "yo" + ": " + tfMensajeCliente.getText());

			comunicacion.enviarTexto(IP_SERVIDOR, PUERTOENVIA, laAlias.getText(), tfMensajeCliente.getText(),
					tfIp.getText());

			tfMensajeCliente.setText("");

			tfMensajeCliente.grabFocus();

		}

	}

	/**
	 * 
	 * AvisoClienteOnline es una clase que extiende de WindowAdapter y sobreescribe
	 * el metodo <code> windowOpened </code>. Este metodo se invoca automaticamente
	 * cuando se abre la ventana. En el metodo, se crea un socket con la IP y puerto
	 * especificados, se crea un objeto DatosUsuario y se le asigna el mensaje
	 * "Online". Luego se cifra el objeto y se envia a traves de un canal de salida
	 * de datos. Finalmente, se cierra el socket. En caso de haber una excepcion, se
	 * imprimen los detalles de la misma.
	 * 
	 * @author Mario Jover
	 *
	 */
	class AvisoClienteOnline extends WindowAdapter {

		public void windowOpened(WindowEvent e) {

			Socket socketIp;
			DatosUsuario datosConexion;
			ObjectOutputStream canalSalida;

			try {

				socketIp = new Socket(IP_SERVIDOR, PUERTOENVIA);

				datosConexion = new DatosUsuario();
				datosConexion.setMensaje("Online");

				byte[] cifrado2 = encriptador.cifrarObjeto(datosConexion, clave);

				canalSalida = new ObjectOutputStream(socketIp.getOutputStream());

				canalSalida.writeObject(cifrado2);

				socketIp.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		/**
		 * Este metodo se activa cuando se cierra la ventana de la aplicacion. Se
		 * encarga de notificar al servidor que el cliente se desconecta, para que pueda
		 * actualizar la lista de usuarios en linea.
		 * 
		 * @param e Evento que activa la ejecucion del metodo.
		 */
		public void windowClosing(WindowEvent e) {
			Socket socketIp;
			DatosUsuario datosConexion;
			ObjectOutputStream canalSalida;

			try {

				socketIp = new Socket(IP_SERVIDOR, PUERTOENVIA);

				datosConexion = new DatosUsuario();
				datosConexion.setMensaje("out");

				byte[] cifrado2 = encriptador.cifrarObjeto(datosConexion, clave);

				canalSalida = new ObjectOutputStream(socketIp.getOutputStream());

				canalSalida.writeObject(cifrado2);

				socketIp.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * Clase que implementa la interface Runnable y es utilizada para escuchar
	 * mensajes del servidor.
	 * 
	 * Crea un objeto de la clase {@link ComunicacionCliente} y usa su metodo
	 * {@link ComunicacionCliente#recibirTexto(ServerSocket, JTextArea)}
	 * 
	 * para recibir mensajes. Ejecuta este proceso en un bucle infinito.
	 * 
	 * @author Mario Jover
	 */
	@Override
	public void run() {
		try {

			ServerSocket socketEscuchaCliente = new ServerSocket(PUERTOESCUCHA);
			ComunicacionCliente comunicacion = new ComunicacionCliente();

			while (true) {

				comunicacion.recibirTexto(socketEscuchaCliente, taConversacion);

			}

		} catch (Exception e) {

			System.err.println(e.getMessage());
		}

	}

}
