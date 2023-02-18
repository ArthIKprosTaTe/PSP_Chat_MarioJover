package login;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interfaz.PanelCliente;
import login.PanelRegister.ActionRegister;
import pojo.DatosUsuario;
import seguridad.Cifrado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * Clase que representa el panel para el log in en la aplicacion.
 * 
 * Esta clase se encarga de crear una interfaz grafica para el usuario
 * (PanelRegister)
 * Se encarga de pedir acceso al servidor mediante contraseña y alias 
 * DEBEN ser unicos
 * 
 * @author Mario Jover
 */

public class PanelLog extends JFrame implements Runnable{

	String clave = "2B7E151628AED2A6ABF7158809CF4F3C";
	private static final String IP_SERVIDOR = "192.168.1.141";
	private static final int PUERTOESCUCHA = 9090;
	private static final int PUERTOENVIA = 9999;
	private String mensaje = "registrado";
	private String alias;
	private JTextField tfContraseña;
    private JTextField tfAlias;
    private JButton btEnviar;
    Cifrado encriptador = new Cifrado();

    public PanelLog() {   	 
        setTitle("Panel de inicio de sesión");
        setSize(new Dimension(350, 200));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.cyan);    
        JLabel lblAlias = new JLabel("Alias:");
        lblAlias.setBounds(20, 20, 80, 25);
        tfAlias = new JTextField();
        tfAlias.setBounds(110, 20, 150, 25);
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setBounds(20, 60, 80, 25);
        tfContraseña = new JTextField();
        tfContraseña.setBounds(110, 60, 150, 25);
        btEnviar = new JButton("Entrar");
        btEnviar.setBounds(110, 100, 80, 25);
        btEnviar.addActionListener(new ActionLogIn());        
        panel.add(lblAlias);
        panel.add(tfAlias);
        panel.add(lblContraseña);
        panel.add(tfContraseña);
        panel.add(btEnviar);      
        add(panel);
        setVisible(true); 
        
        Thread escuchaLog = new Thread(this);
        escuchaLog.start();
    }
    /**
	 * Esta clase es el listener que implementa la logica del panel log
	 * para autenticarse en el servidor y acceder al panel del cliente
	 * 
	 * @author mario
	 *
	 */
    class ActionLogIn implements ActionListener {
        
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!tfContraseña.getText().equals("") && !tfAlias.getText().equals("")) {
				try {				
					DatosUsuario datosDelRegistro = new DatosUsuario();
					ObjectOutputStream canalSalida;
					Socket socketSalida;
					datosDelRegistro.setMensaje("login");
					datosDelRegistro.setContrasena(tfContraseña.getText());
					datosDelRegistro.setNick(tfAlias.getText());					
					Cifrado encriptador = new Cifrado();					
					socketSalida = new Socket(IP_SERVIDOR, PUERTOENVIA);
					byte[] cifrado = encriptador.cifrarObjeto(datosDelRegistro, clave);					
					canalSalida = new ObjectOutputStream(socketSalida.getOutputStream());					
					canalSalida.writeObject(cifrado);					
					socketSalida.close();										
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}else {
				JOptionPane.showMessageDialog(null, "Datos nulos", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
    
	@Override
	public void run() {
try {
			
    		ServerSocket socketEscuchaCliente = new ServerSocket(PUERTOESCUCHA);   		
    		while(mensaje.equals("registrado")){    			
   			Socket socketEntrada;
   			DatosUsuario informacionServidor;
   			ObjectInputStream canalEntradaCliente;  			  			
   			socketEntrada = socketEscuchaCliente.accept();
   			canalEntradaCliente = new ObjectInputStream(socketEntrada.getInputStream());   			
   			byte[] cifrado = (byte[]) canalEntradaCliente.readObject();   			
   			informacionServidor = (DatosUsuario) encriptador.descifrarObjeto(cifrado, clave);
   			mensaje = informacionServidor.getMensaje();
   			alias = informacionServidor.getNick();   			
   			socketEntrada.close();
    		}
    		socketEscuchaCliente.close();
    		dispose();
    		PanelCliente interfazCliente = new PanelCliente(alias);
   			interfazCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    		
   		} catch (ConnectException e ) {
   			e.printStackTrace();
   		} catch (IOException e) {
			// TODO Auto-generated catch block
   			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}		
	}  
}

