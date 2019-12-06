package takuzu.jeu.graphique;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import takuzu.jeu.console.RemplissageException;
import takuzu.jeu.console.Takuzu;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Interface4x4 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8854302421532918463L;
	private JPanel contentPane; // composant JPanel : le composant principal qui contient toutes les autres composants graphiques
	private JTable table;  // la grille du jeu Takuzu
	private Takuzu takuzu=new Takuzu(4); // l'objet Takuzu
	private Date date_debut ;
	private int minute=00, seconde=00; 
	private  Timer timer1=null; // le chrono

	/**
	 * Create the frame.
	 */
	
	public Interface4x4() { //constructeur
		date_debut = new Date();
		contentPane = new JPanel(); // initialiser le composant JPanel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 612, 385);


		JPanel conteneurGrille = new JPanel(); // créer un conteneur de grille qui va contenir la matrice
		conteneurGrille.setBounds(44, 48, 542, 206);

		table = new JTable(); // la matrice
		table.setFont(new Font("Tahoma", Font.BOLD, 16)); 
		table.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		// utiliser la classe MyDeaultTableModel pour fixer des valeurs de la grille
		MyDefaultTableModel model = new MyDefaultTableModel(
				new Object[][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
				},
				new String[] {
					"New column", "New column", "New column", "New column"
				}
			) {
				Class[] columnTypes = new Class[] {
					Integer.class, Integer.class, Integer.class, Integer.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.setRowHeight(40);
		XTableRenderer rend = new XTableRenderer(); 
		table.getColumnModel().getColumn(0).setCellRenderer(rend);
		table.getColumnModel().getColumn(1).setCellRenderer(rend);
		table.getColumnModel().getColumn(2).setCellRenderer(rend);
		table.getColumnModel().getColumn(3).setCellRenderer(rend);
		
		
		// un texte "Temps : " à afficher
		JLabel label1 = new JLabel("Temps  :");
		label1.setForeground(Color.BLACK);
		label1.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		label1.setBounds(10, 271, 71, 28);
		contentPane.add(label1);
		
		// le chrono à afficher
		JLabel label2 = new JLabel("00:00");
		label2.setForeground(Color.BLACK);
		label2.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		label2.setBounds(81, 271, 51, 28);
		contentPane.add(label2);
		
		// générer un grille aléatoirement
        try {
        	takuzu=takuzu.generate();
        }	
        catch(RemplissageException e) {}
    	for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(takuzu.getGrille()[i][j]==-1) // si la case contient -1
					table.setValueAt(null, i, j); // on affiche un case vide et éditable
				else 
				    { // si la case contient une valeur autre que -1 
					table.setValueAt(takuzu.getGrille()[i][j],i,j); //afficher cette valeur
					model.setCellEditable(i, j, false); // rendre cette case non éditable
					}
			}
		}
		takuzu.toString(); // afficher la grille incomplète
		int delais = 1000; // définir un delais pour le chrono
		ActionListener tache_timer;

		/* Action réalisé par le timer */
		tache_timer = new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (seconde == 59) {
					seconde = 0;
					minute++;
				}
				seconde++; // on incrémente les secondes
				label2.setText(minute + ":" + seconde);/* rafraichir le label  et afficher le temps */
				
			}
		};
		/* instanciation du timer */
	    timer1 = new Timer(delais, tache_timer);
		timer1.start();
	
		
	
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//bouton verifier
		JButton btnVerifier = new JButton(" Verifier",new ImageIcon("verifier.jpg"));
		btnVerifier.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnVerifier.setBounds(372, 271, 147, 38);
		btnVerifier.setForeground(Color.pink);
		btnVerifier.addActionListener(new ActionListener() { //action du bouton
			public void actionPerformed(ActionEvent e) {
				// on va affecter la matrice à la matrice de l'objet Takuzu puis on verifie

				Takuzu takuzu = new Takuzu(4);
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						if(table.getValueAt(i, j)==null) // si la case est vide
							takuzu.getGrille()[i][j]=-1; // on affecte un -1
						else
						    takuzu.getGrille()[i][j] = (int) table.getValueAt(i, j);
					}
				}
				takuzu.toString();
				
				if(takuzu.estValide()) { //appel au méthode estValide() de la classe Takuzu pour tester la validité du jeu
					 timer1.stop();  // arreter le chrono
				     JOptionPane.showMessageDialog(null, "Félicitations ! Vous avez terminé en " +label2.getText());// takuzu.calcul_duree(date_debut, new Date()
				     int temps=minute*60+seconde;
				     takuzu.setTemps(temps);
				     takuzu.enreg_meilleurs_temps(4);  
				}
				else
					   JOptionPane.showMessageDialog(null, "  Dommage ! Grille invalide\n   "+"Veuillez vous réessayer ");
				
				
			}
		});
		contentPane.add(btnVerifier);
		conteneurGrille.add(table);
		contentPane.add(conteneurGrille);
		
		//barre de menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 596, 22);
		contentPane.add(menuBar);
		
		        // menu Jeu
				JMenu mnJeu = new JMenu("Jeu");
				menuBar.add(mnJeu);
				mnJeu.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
				
				        // sous menu Nouveau pour créer une nouvelle partie
						JMenuItem mntmNouveau = new JMenuItem("Nouveau");
						mnJeu.add(mntmNouveau);
						mntmNouveau.setIcon(new ImageIcon("nouveau.png"));
						mntmNouveau.addActionListener(new ActionListener() { // définir une action
							public void actionPerformed(ActionEvent e) {
								minute=00;seconde=00;
								int delais = 1000;
								ActionListener tache_timer;
				 
								/* Action réalisé par le timer */
								tache_timer = new ActionListener() {
									public void actionPerformed(ActionEvent e1) {
										if (seconde == 59) {
											seconde = 0;
											minute++;
											
										}
										seconde++;
										label2.setText(minute + ":" + seconde);/* rafraichir le label */
										
									}
								};
								/* instanciation du timer */
							    timer1 = new Timer(delais, tache_timer);
							    timer1.start();
							    
							    // générer un nouveau objet Takuzu
								takuzu=new Takuzu(4);
								try {
								   takuzu=takuzu.generate();
								   }
								catch(RemplissageException excep) {}
								for (int i = 0; i < 4; i++) {
									for (int j = 0; j < 4; j++) {
										if(takuzu.getGrille()[i][j]==-1)
											table.setValueAt(null, i, j);
										else {
											table.setValueAt(takuzu.getGrille()[i][j],i,j);
											model.setCellEditable(i, j, false);
											}
									}
								}
								takuzu.toString();
								
							}
						});
						
						
						//sous menu Ouvrir pour charger la partie enregistrée 
						JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
						mnJeu.add(mntmOuvrir);
						mntmOuvrir.setIcon(new ImageIcon("mini_charger.png"));
						mntmOuvrir.addActionListener(new ActionListener () {
							public void actionPerformed(ActionEvent e) {
								// charger l'objet Takuzu
								FileInputStream file;
								takuzu=new Takuzu('c');
								if(! takuzu.est_vide(4)) {
								 try {
										file=new FileInputStream("takuzu4x4.serial");
										ObjectInputStream stream =new ObjectInputStream(file);
						                takuzu=takuzu.readObject(stream);
						                stream.close();
						                }
									catch(IOException e1) { System.out.println("Erreur de lecture ! "); }
									catch(ClassNotFoundException e2) { System.out.println("Erreur de lecture ! "); }
								 
								 date_debut=takuzu.charger_temps(4); // charger le temps enregistré
								 int temps=takuzu.getTemps(); // accès au temps chargé
								 minute=temps/60;
								 seconde=temps%60;
								 label2.setText(minute + ":" + seconde); // commencer le chrono à partir du temps chargé
								 int delais = 1000;
									ActionListener tache_timer;
					 
									/* Action réalisé par le timer */
									tache_timer = new ActionListener() {
										public void actionPerformed(ActionEvent e1) {
											if (seconde == 59) {
												seconde = 0;
												minute++;
											}
											seconde++;
											label2.setText(minute + ":" + seconde);/* rafraichir le label */
										}
									};
									/* instanciation du timer */
								    timer1 = new Timer(delais, tache_timer);
								    timer1.start();
								 
								 int[][] grille=takuzu.getGrille();
								 for (int i = 0; i < 4; i++) {
										for (int j = 0; j < 4; j++) {
											if(grille[i][j]==-1)
												table.setValueAt(null, i, j);
											else
												table.setValueAt(grille[i][j],i,j);
										}
									}
								 takuzu.toString();
								}
								else
									JOptionPane.showMessageDialog(null, "      Pas de partie enregistrée !    ");
								
							}
						});
						
						
						// sous menu Enregistrer pour enregistrer la partie courante
						JMenuItem mntmEnregistrer = new JMenuItem("Enregistrer");
						mnJeu.add(mntmEnregistrer);
						mntmEnregistrer.setIcon(new ImageIcon("mini_save.png"));
						mntmEnregistrer.addActionListener(new ActionListener () {
							public void actionPerformed(ActionEvent e) {
								// calculer la durée pour modifier la variable temps de l'objet Takuzu
								takuzu.calcul_duree(date_debut, new Date());
								takuzu.enreg_temps(4); // enregistrer le temps dans un fichier
								// affecter la matrice à la grille de l'objet Takuzu courant
								for (int i = 0; i < 4; i++) {
									for (int j = 0; j < 4; j++) {
										if(table.getValueAt(i, j)==null)
											takuzu.getGrille()[i][j]=-1;
										else
										    takuzu.getGrille()[i][j] = (int) table.getValueAt(i, j);
									}
								}
								takuzu.toString();
								int temps=minute*60+seconde;
								takuzu.setTemps(temps);
								// enregistrer l'objet Takuzu dans un fichier
								FileOutputStream f;
								try {
									f=new FileOutputStream("takuzu4x4.serial");
									ObjectOutputStream stream =new ObjectOutputStream(f);
	                takuzu.writeObject(stream);
	                stream.close();
	                }
								catch(IOException excep) { System.out.println("Erreur d'ecriture ! "); }
								timer1.stop();
								 JOptionPane.showMessageDialog(null, "      Partie enregistrée   ");
							}
						});
						
						
						//sous menu Quitter pour quitter le jeu
						JMenuItem mntmQuitter = new JMenuItem("Quitter");
						mnJeu.add(mntmQuitter);
						mntmQuitter.setIcon(new ImageIcon("mini_quitter.png"));
						mntmQuitter.addActionListener(new ActionListener () {
							public void actionPerformed(ActionEvent e) {
								System.exit(0);
							}
						});
						
						        //menu Score
								JMenu mnScore = new JMenu("Score");
								menuBar.add(mnScore);
								mnScore.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
								
								// sous menu de Score pour afficher le meilleur score
								JMenuItem mntmMeilleurScore = new JMenuItem("Meilleur Score");
								mnScore.add(mntmMeilleurScore);
								mntmMeilleurScore.setIcon(new ImageIcon("score.jpg"));
								mntmMeilleurScore.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										int min=takuzu.min_temps(4);
										int m=min/60; // nombre minutes
										int s=min%60; // nombre secondes
										 JOptionPane.showMessageDialog(null, "   Meilleur Score  :  \n"+"            "+ m+" min "+s +" s");
									} 
								} );
								
								
								// sous menu de Score pour afficher les dix meilleurs scores
								JMenuItem mntmLesScores =new JMenuItem("Les 10 Scores");
								mnScore.add(mntmLesScores);
								mntmLesScores.setIcon(new ImageIcon("dix_scores.jpg"));
								mntmLesScores.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										String str=new String();
										int val;
										File f;
										Scanner sc;
										try {
											//lire le fichier
											f = new File("les_dix_meilleurs4x4.txt");
											if(f.length() != 0) {
												sc = new Scanner(f);
												val = sc.nextInt();
												str=str+"     "+val+" s\n";
												while (sc.hasNextInt()) {
													str=str+"     "+sc.nextInt()+" s\n";
												}
											}
											}
										 catch (IOException excep) {
											System.out.println("Erreur de lecture dans le fichier les_dix_meilleurs: " + excep.getMessage());
										}
										// afficher une alerte qui contient les dix scores
										 JOptionPane.showMessageDialog(null, "Les dix meilleurs scores : \n"+str);
									}
								});
								
								
								//bouton recommencer pour recommencer la partie courante
								JButton btnRecommencer = new JButton("Recommencer",new ImageIcon("commencer.jpg"));
								btnRecommencer.setForeground(Color.PINK);
								btnRecommencer.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
								btnRecommencer.setBounds(157, 271, 167, 38);
								btnRecommencer.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										for(int i=0;i<4;i++) {
											for(int j=0; j<4; j++) {
												if(model.isCellEditable(i, j)==true) // si la case est éditable
													table.setValueAt(null, i, j);   // alors on la vide
											}
										}
										
									}
								});
								
								contentPane.add(btnRecommencer);
												
								
							/*	setLayout(new BorderLayout());
								ImageIcon image=new ImageIcon("takuzu.png");
								JLabel background=new JLabel(image);
								background.setLocation(0,0);
								background.setSize(getSize());
								add(background);
								background.setLayout(new FlowLayout());*/
								
	}
	
	
	// cette méthode permet le formatage du temps 
	 private String TimeFormat(int count) {

	        int hours = count / 3600;
	        int minutes = (count - hours * 3600) / 60;
	        int seconds = count - minutes * 60;

	        return String.format("      Timer :" + "%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
	    }
}
