package login;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interfaz.PanelCliente;
import pojo.DatosUsuario;
import seguridad.Cifrado;
/**
 * 
 * Clase que representa el panel para el reistro en la aplicacion.
 * 
 * Esta clase se encarga de crear una interfaz grafica para el usuario
 * (PanelRegister)
 * Se encarga de pedir acceso al servidor
 * 
 * 
 * @author Mario Jover
 */
public class PanelRegister extends JFrame implements Runnable{
	String clave = "2B7E151628AED2A6ABF7158809CF4F3C";
	private static final String IP_SERVIDOR = "192.168.1.141";
	private static final int PUERTOESCUCHA = 9090;
	private static final int PUERTOENVIA = 9999;
	private String mensaje = "no registrado";
	private String alias;
	private JTextField tfContraseña;
    private JTextField tfAlias;
    private JButton btEnviar;
    Cifrado encriptador = new Cifrado();
    public PanelRegister() {
    	  // Configurar el JFrame
        setTitle("Panel de registro");
        setSize(new Dimension(350, 200));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.lightGray);       
        JLabel lblAlias = new JLabel("Alias:");
        lblAlias.setBounds(20, 20, 80, 25);
        tfAlias = new JTextField();
        tfAlias.setBounds(110, 20, 150, 25);
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setBounds(20, 60, 80, 25);
        tfContraseña = new JTextField();
        tfContraseña.setBounds(110, 60, 150, 25);
        btEnviar = new JButton("Registro");
        btEnviar.setBounds(110, 100, 80, 25);
        panel.add(lblAlias);
        panel.add(tfAlias);
        panel.add(lblContraseña);
        panel.add(tfContraseña);
        btEnviar.addActionListener(new ActionRegister());
        panel.add(btEnviar);       
        add(panel);
        setVisible(true);
        Thread escuchaRegister = new Thread(this);
        escuchaRegister.start();       
    }
	/**
	 * Esta clase es el listener que implementa la logica del panel registro
	 * pafa pedir acceso al servidor
	 * 
	 * @author mario
	 *
	 */
    class ActionRegister implements ActionListener {
    
		@Override
		public void actionPerformed(ActionEvent e) {

			if (!tfContraseña.getText().equals("") && !tfAlias.getText().equals("")) {
				try {					
					DatosUsuario datosDelRegistro = new DatosUsuario();
					ObjectOutputStream canalSalida;
					Socket socketSalida;
					datosDelRegistro.setMensaje("registro");
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
    		while(mensaje.equals("no registrado")){    			
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
			
	}}
		
	
	

