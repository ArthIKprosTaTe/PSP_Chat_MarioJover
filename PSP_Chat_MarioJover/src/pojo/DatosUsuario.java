package pojo;

import java.io.Serializable;

/**
 * 
 * La clase {@code DatosUsuario} almacena informacion de un usuario en una
 * conversacion de chat.
 * 
 * Implementa la interfaz {@code Serializable} para permitir su envio a traves
 * de un flujo de salida.
 * 
 * @author Mario Jover
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DatosUsuario implements Serializable {

	/**
	 * Tiene tres atributos: el alias del usuario, su ip y el mensaje.
	 */
	private String alias, ipUsuario, mensaje, Contrasena;

	/**
	 * 
	 * Obtiene el alias del usuario.
	 * 
	 * @return El alias del usuario
	 */

	public String getNick() {
		return alias;
	}

	public String getContrasena() {
		return Contrasena;
	}

	public void setContrasena(String contraseña) {
		Contrasena = contraseña;
	}

	/**
	 * 
	 * Establece el alias del usuario.
	 * 
	 * @param alias El alias a establecer
	 */
	public void setNick(String alias) {
		this.alias = alias;
	}

	/**
	 * 
	 * Obtiene la direccion IP del usuario.
	 * 
	 * @return La direccion IP del usuario
	 */
	public String getIpCliente() {
		return ipUsuario;
	}

	/**
	 * 
	 * Establece la direccion IP del usuario.
	 * 
	 * @param ipCliente La direccion IP a establecer
	 */
	public void setIpCliente(String ipCliente) {
		this.ipUsuario = ipCliente;
	}

	/**
	 * 
	 * Obtiene el mensaje enviado por el usuario.
	 * 
	 * @return El mensaje enviado por el usuario
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * Establece el mensaje enviado por el usuario.
	 * 
	 * @param mensaje El mensaje a establecer
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
