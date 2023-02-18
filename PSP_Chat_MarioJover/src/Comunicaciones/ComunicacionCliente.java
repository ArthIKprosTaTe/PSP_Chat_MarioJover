package Comunicaciones;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

import pojo.DatosUsuario;
import seguridad.Cifrado;

/**
 * Clase ComunicacionCliente encargada de enviar y recibir mensajes entre
 * clientes.
 * @author Mario Jover
 */
public class ComunicacionCliente {
	/**
	 * Objeto encargado de cifrar los datos.
	 */
	Cifrado encriptador = new Cifrado();
	/**
	 * Clave utilizada para cifrar los datos.
	 */
	String clave = "2B7E151628AED2A6ABF7158809CF4F3C";

	/**
	 * Metodo encargado de enviar un mensaje a otro cliente.
	 * 
	 * @param ip             Direccion IP del servidor.
	 * @param puerto         Puerto del servidor.
	 * @param alias          Alias del remitente.
	 * @param mensaje        Mensaje a enviar.
	 * @param ipDestinatario Direccion IP del destinatario.
	 */
	public void enviarTexto(String ip, int puerto, String alias, String mensaje, String ipDestinatario) {

		DatosUsuario datosCliente;
		ObjectOutputStream canalSalida;
		Socket socketSalida;
		try {
			/**
			 * Crea un socket para conectarse al servidor.
			 */
			socketSalida = new Socket(ip, puerto);
			/**
			 * Crea un objeto DatosUsuario con los datos del remitente.
			 */
			datosCliente = new DatosUsuario();
			/**
			 * Asigna el alias y el mensaje al objeto DatosUsuario.
			 */
			datosCliente.setNick(alias);
			datosCliente.setIpCliente(ipDestinatario);
			datosCliente.setMensaje(mensaje);
			/**
			 * Cifra el objeto DatosUsuario.
			 */
			byte[] cifrado = encriptador.cifrarObjeto(datosCliente, clave);
			/**
			 * Crea un canal de salida para enviar el mensaje cifrado al servidor.
			 */
			canalSalida = new ObjectOutputStream(socketSalida.getOutputStream());
			/**
			 * Envia el mensaje cifrado al servidor.
			 */
			canalSalida.writeObject(cifrado);
			/**
			 * Cierra el socket.
			 */
			socketSalida.close();
			/**
			 * Muestra un mensaje de error en caso de que ocurra algun problema.
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * Metodo que recibe un mensaje de otro cliente.
	 * 
	 * @param socketEscuchaCliente El socket que escucha las conexiones de los
	 *                             clientes
	 * 
	 * @param post                 La area de texto donde se mostraran los mensajes
	 *                             recibidos
	 */
	public void recibirTexto(ServerSocket socketEscuchaCliente, JTextArea post) {

		try {

			Socket socketEntrada;
			DatosUsuario informacionServidor;
			ObjectInputStream canalEntradaCliente;
			String alias, mensaje;

			/**
			 * Acepta una conexion entrante y crea un socket para el canal de entrada
			 */
			socketEntrada = socketEscuchaCliente.accept();

			canalEntradaCliente = new ObjectInputStream(socketEntrada.getInputStream());
			/**
			 * Lee los datos cifrados desde el canal de entrada
			 */

			byte[] cifrado = (byte[]) canalEntradaCliente.readObject();
			/**
			 * Descifra los datos recibidos
			 */
			informacionServidor = (DatosUsuario) encriptador.descifrarObjeto(cifrado, clave);
			/**
			 * Obtiene el alias y el mensaje del objeto descifrado
			 */
			alias = informacionServidor.getNick();
			mensaje = informacionServidor.getMensaje();
			/**
			 * Agrega el mensaje descifrado al componente JTextArea
			 */
			post.append("\n" + alias + ": " + mensaje);
			/**
			 * Cierra el socket de entrada
			 */
			socketEntrada.close();
			/**
			 * Muestra un mensaje de error en caso de que ocurra algun problema.
			 */
		} catch (Exception e) {

			System.err.println(e.getMessage());
		}

	}

}
