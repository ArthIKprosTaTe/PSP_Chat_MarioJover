package login;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Clase que representa un panel para que el usuario decida si se registra o autentica
 * el usuario hara la eleccion mediante dos botones.
 * 
 * @author Mario Jover
 */
public class PanelDecidir extends JFrame{
	
	
	private JButton btLogIn;
    private JButton btRegister;

    public PanelDecidir() {
    	this.setLayout(new FlowLayout());
        btLogIn = new JButton("Login");
        btRegister = new JButton("Register");

        setBounds(200,200,200,200); 
        
        
        btLogIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PanelLog logPanel = new PanelLog();
				
				logPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dispose();
			}
		});
        
        add(btLogIn);
        
        btRegister.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PanelRegister registerPanel = new PanelRegister();
				
				registerPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dispose();
			}
		});
        
        add(btRegister);
        this.setVisible(true);
        this.setResizable(false);
    }

    public JButton getBtLogIn() {
        return btLogIn;
    }

    public JButton getBtRegister() {
        return btRegister;
    }
	
	

}
