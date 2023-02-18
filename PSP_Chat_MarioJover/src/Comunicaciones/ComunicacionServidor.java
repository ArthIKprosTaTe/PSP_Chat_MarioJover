package Comunicaciones;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import logicaBd.Registrar;
import pojo.DatosUsuario;
import seguridad.Cifrado;

/**
 * La clase ComunicacionServidor se encarga de redirigir mensajes entre
 * servidores y clientes. Utiliza un objeto de la clase Cifrado para cifrar y
 * descifrar los mensajes antes de enviarlos o despues de recibirlos. La clase
 * tambien mantiene un registro de los clientes que se conectan y desconectan en
 * un JTextArea.
 * 
 * @author Mario Jover
 */
public class ComunicacionServidor {
	/**
	 * Objeto encriptador de la clase Cifrado que se utiliza para cifrar y descifrar
	 * los mensajes.
	 */
	Cifrado encriptador = new Cifrado();
	/**
	 * La clave secreta utilizada para cifrar y descifrar los mensajes.
	 */
	String clave = "2B7E151628AED2A6ABF7158809CF4F3C";
	// VARIABLES A CAMBIAR
	/**
	 * El puerto por el cual se envian los mensajes.
	 */
	private static final int PUERTOENVIA = 9090;

	/**
	 * Este metodo redirige un mensaje desde un servidor a otro cliente.
	 * 
	 * @param socketEscuchaServer El socket del servidor que escucha a los clientes.
	 * @param log                 El JTextArea donde se mantiene el registro de los
	 *                            clientes que se conectan y desconectan.
	 */
	public void redireccionDeTexto(ServerSocket socketEscuchaServer, JTextArea log) {
		try {
			Date fechaActual = new Date();
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaFormateada = formatoFecha.format(fechaActual);
			String ipDestinatario, mensaje, alias, contrasena;
			DatosUsuario datosCliente;
			Socket socketEntradaServer, socketSalidaServer;
			ObjectInputStream canalEntradaServer;
			ObjectOutputStream canalSalidaServer;
			InetAddress ipCliente;
			/**
			 * Acepta una conexion de un cliente y obtiene su direccion IP.
			 */
			socketEntradaServer = socketEscuchaServer.accept();
			canalEntradaServer = new ObjectInputStream(socketEntradaServer.getInputStream());
			/**
			 * Lee los datos del cliente cifrados y los descifra.
			 */
			byte[] cifrado = (byte[]) canalEntradaServer.readObject();
			datosCliente = (DatosUsuario) encriptador.descifrarObjeto(cifrado, clave);
			ipDestinatario = datosCliente.getIpCliente();
			mensaje = datosCliente.getMensaje();
			ipCliente = socketEntradaServer.getInetAddress();

			/**
			 * Se crea un socket de entrada y se lee el objeto cifrado que se recibe a
			 * traves de un canal de entrada. Luego se descifra el objeto y se obtiene la
			 * informacion de la direccion IP del destinatario, el mensaje y la direccion IP
			 * del cliente. Si el mensaje recibido no es "Online", se crea un nuevo socket
			 * de salida hacia el destinatario y se envia el objeto descifrado. Si el
			 * mensaje es "out", se agrega al log que el cliente se ha desconectado. En caso
			 * contrario, se agrega al log que el cliente se ha conectado.
			 * 
			 * @throws Exception - En caso de que se produzca un error en el procesamiento,
			 *                   se lanza una excepcion.
			 */

			if (!mensaje.equals("Online") && !mensaje.equals("registro") && !mensaje.equals("login")) {
				socketSalidaServer = new Socket(ipDestinatario, PUERTOENVIA);
				byte[] cifrado2 = encriptador.cifrarObjeto(datosCliente, clave);
				canalSalidaServer = new ObjectOutputStream(socketSalidaServer.getOutputStream());
				canalSalidaServer.writeObject(cifrado2);
				canalSalidaServer.close();
				socketSalidaServer.close();
				socketEntradaServer.close();
			} else if (mensaje.equals("out")) {
				log.append(fechaFormateada + " " + ipCliente.toString() + " se desconecto." + "\n");
				socketEntradaServer.close();
			} else if (mensaje.equals("registro")) {
				String respuestaLogIn;
				alias = datosCliente.getNick();
				contrasena = datosCliente.getContrasena();
				Registrar reg = new Registrar();
				boolean esta = reg.comprobarRegistro(alias, contrasena);
				respuestaLogIn = reg.registrar(esta, alias, contrasena);
				System.out.println(respuestaLogIn);
				socketSalidaServer = new Socket(ipCliente, PUERTOENVIA);
				datosCliente.setMensaje(respuestaLogIn);
				byte[] cifrado2 = encriptador.cifrarObjeto(datosCliente, clave);
				canalSalidaServer = new ObjectOutputStream(socketSalidaServer.getOutputStream());
				canalSalidaServer.writeObject(cifrado2);
				canalSalidaServer.close();
				socketSalidaServer.close();
				socketEntradaServer.close();
			} else if (mensaje.equals("Online")) {
				log.append(fechaFormateada + " " + ipCliente.toString() + " se conecto." + "\n");
				socketEntradaServer.close();
			} else if (mensaje.equals("login")) {
				String respuestaLogIn = "registrado";
				alias = datosCliente.getNick();
				contrasena = datosCliente.getContrasena();
				Registrar reg = new Registrar();
				boolean esta = reg.comprobarRegistro(alias, contrasena);
				if (esta) {
					respuestaLogIn = "no registrado";
				}
				socketSalidaServer = new Socket(ipCliente, PUERTOENVIA);
				datosCliente.setMensaje(respuestaLogIn);
				byte[] cifrado2 = encriptador.cifrarObjeto(datosCliente, clave);
				canalSalidaServer = new ObjectOutputStream(socketSalidaServer.getOutputStream());
				canalSalidaServer.writeObject(cifrado2);
				canalSalidaServer.close();
				socketSalidaServer.close();
				socketEntradaServer.close();

			}

		} catch (Exception e) {
			System.out.println("se produce server");
		}
	}
}
