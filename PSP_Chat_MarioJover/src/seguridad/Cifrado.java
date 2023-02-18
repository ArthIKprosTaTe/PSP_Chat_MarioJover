package seguridad;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * Clase Cifrado Esta clase proporciona metodos para cifrar y descifrar objetos
 * utilizando AES como algoritmo de cifrado y PKCS5Padding como forma de
 * relleno.
 * 
 * @author Mario Jover
 */
public class Cifrado {
	/**
	 * 
	 * Este metodo cifra un objeto especifico utilizando AES como algoritmo de
	 * cifrado y PKCS5Padding como forma de relleno.
	 * 
	 * @param objeto El objeto que se desea cifrar
	 * 
	 * @param clave  La clave que se utilizara para cifrar el objeto
	 * 
	 * @return El objeto cifrado en forma de un arreglo de bytes
	 * 
	 * @throws Exception Puede lanzar excepciones si hay errores en el proceso de cifrado
	 *                  
	 */
	public byte[] cifrarObjeto(Object objeto, String clave) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(objeto);

		byte[] data = baos.toByteArray();
		oos.close();

		SecretKeySpec key = new SecretKeySpec(clave.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, key);

		return cipher.doFinal(data);
	}

	/**
	 * 
	 * Metodo que permite descifrar un objeto que ha sido previamente cifrado con el
	 * algoritmo AES.
	 * 
	 * @param cifrado - Byte array con los datos cifrados del objeto
	 * 
	 * @param clave   - Cadena con la clave utilizada para cifrar el objeto
	 * 
	 * @return Object - Objeto descifrado
	 * 
	 * @throws Exception - En caso de haber algun error durante el proceso de
	 *                   descifrado.
	 */
	public Object descifrarObjeto(byte[] cifrado, String clave) throws Exception {

		SecretKeySpec key = new SecretKeySpec(clave.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] decrypted = cipher.doFinal(cifrado);

		ByteArrayInputStream bais = new ByteArrayInputStream(decrypted);

		ObjectInputStream ois = new ObjectInputStream(bais);

		Object objeto = ois.readObject();

		ois.close();

		return objeto;
	}

}
