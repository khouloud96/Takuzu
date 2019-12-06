package takuzu.jeu.graphique;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

// cette classe représente la premiere fenetre à afficher
public class MainFrame {

	private JFrame frmTitrep; // composant JFrame c'est la page à afficher
	private JTextField taille_de_la_grille; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frmTitrep.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() { // construteur
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() { 
		frmTitrep = new JFrame();
		frmTitrep.setTitle("Jeu Takuzu");
		frmTitrep.setBounds(100, 100, 605, 417);
		frmTitrep.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTitrep.getContentPane().setLayout(null);
		
		//un composant pour récupérer la taille de la grille
		taille_de_la_grille = new JTextField();
		taille_de_la_grille.setForeground(Color.BLUE);
		taille_de_la_grille.setText("");
		taille_de_la_grille.setFont(new Font("Tahoma", Font.BOLD, 20));
		taille_de_la_grille.setBounds(402, 86, 69, 35);
		taille_de_la_grille.setHorizontalAlignment(JTextField.CENTER);
		frmTitrep.getContentPane().add(taille_de_la_grille);
		taille_de_la_grille.setColumns(10);
		
		
		JLabel lblDonnerLaTaill = new JLabel("      Donner la taille de la grille :");
		lblDonnerLaTaill.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 22));
		lblDonnerLaTaill.setBounds(0, 71, 374, 58);
		lblDonnerLaTaill.setForeground(Color.pink);
		frmTitrep.getContentPane().add(lblDonnerLaTaill);
		
		
		// bouton commencer qui possède une action
		JButton btnCommencer = new JButton("Commencer",new ImageIcon("commencer.jpg"));
		btnCommencer.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCommencer.setForeground(Color.pink);
		btnCommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Selon la taille on envoie une page
				if(Integer.parseInt(taille_de_la_grille.getText())==4) {	//si la taille=4			
			      	Interface4x4 interface4x4 = new Interface4x4(); // on cré un objet Interface4x4
			    	interface4x4.setTitle("Jeu Takuzu 4x4");			
				    interface4x4.setVisible(true);
				    frmTitrep.setVisible(false);
				}
				else if(Integer.parseInt(taille_de_la_grille.getText())==6) // si la taille=6
				{
					Interface6x6 interface6x6 = new Interface6x6(); // on cré un objet Interface6x6
			    	interface6x6.setTitle("Jeu Takuzu 6x6");			
				    interface6x6.setVisible(true);
				    frmTitrep.setVisible(false);
				}
				else if(Integer.parseInt(taille_de_la_grille.getText())==8) { // si la taille=8
					Interface8x8 interface8x8 = new Interface8x8();     // on cré un objet Interface8x8
			    	interface8x8.setTitle("Jeu Takuzu 8x8");			
				    interface8x8.setVisible(true);
				    frmTitrep.setVisible(false);
				}
				else { //sinon si la taille est incorrect , on affiche une alerte
					JOptionPane.showMessageDialog(null, " La taille de la grille doit etre 4 ou 6 ou 8 ");
					frmTitrep.setVisible(true);
				}
				
			}
		});
		
		
		btnCommencer.setBounds(230, 238, 167, 43);
		frmTitrep.getContentPane().add(btnCommencer);
		
		// créer une barre de menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 592, 22);
		frmTitrep.getContentPane().add(menuBar);
		
		// menu Aide
		JMenu mnAide = new JMenu("Aide");
		mnAide.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		menuBar.add(mnAide);
		
		// sous menu de Aide : Régles du jeu
		JMenuItem mntmRglesDuJeu = new JMenuItem("R\u00E9gles du jeu");
		mntmRglesDuJeu.setIcon(new ImageIcon("aide.png"));
		mnAide.add(mntmRglesDuJeu);
		// on ajoute une action au sous menu
		mntmRglesDuJeu.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//affichage d'une alerte pour expliquer les régles du jeu
				JOptionPane.showMessageDialog(null, "        Les régles du jeu Takuzu : \n\n"+
			            "  Chaque grille ne contient que des 0 et des 1 ;\r\n"+
			            "  Autant de 1 et de 0 sur chaque ligne et sur chaque colonne ;\r\n" + 
						"  Pas plus de 2 chiffres identiques côte à côte ;\r\n" + 
						"  2 lignes ne peuvent pas être identiques ;\r\n" + 
						"  2 colonnes ne peuvent pas être identiques. ");
			}
		} );
		
		
		// définir le background comme une image
		frmTitrep.setLayout(new BorderLayout());
		ImageIcon image=new ImageIcon("takuzu.png");
		JLabel background=new JLabel(image);
		background.setLocation(0,0);
		background.setSize(frmTitrep.getSize());
		frmTitrep.add(background);
		background.setLayout(new FlowLayout());
		
		
	}
}
