package takuzu.jeu.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

   // la classe Main où on va manipuler l'objet Takuzu
public class Main {
	// la méthode principale main
	public static void main(String arg[])
	{
		Date date_debut=null,date_fin=null; // deux variables de type Date qui représentent les dates de debut et de fin du jeu
		char reponse='N'; 
		char c=' '; 
		Takuzu t=null; // objet Takuzu
		int taille=0; // taille de la grille
		
		
		// Tant que la taille de la grile est incorrect on demande de l'utilisateur de saisir la taille de la grille
		System.out.println("Donner la taille de la grille");
		Scanner scanner=new Scanner(System.in);
		taille=scanner.nextInt();
		while(taille !=4 && taille !=6 && taille != 8)
		{
			System.out.println("Donner la taille de la grille");
			taille=scanner.nextInt();
		}
		
		
		if(!t.est_vide(taille)) { // si le fichier d'enregistrement du Takuzu n'est pas vide 
		System.out.println("Voulez vous continuer la partie enregistrée ? O/N"); // alors l'utilisateur a le droit de compléter la partie enregistrée
		Scanner scan=new Scanner(System.in); 
		reponse=scan.nextLine().charAt(0); // on récupére sa réponse dans la variable reponse
		}
		else { // sinon
			System.out.println("Pas de partie enregistrée !");
		}
		
		if(reponse=='O') { // si la reponse est oui on charge la partie enregistrée
			FileInputStream file=null;
			 t=new Takuzu('c');
		 try { 
			 switch(taille) { // selon la taille, on lit le fichier correspondant
			 case 4 : file=new FileInputStream("takuzu4x4.serial"); break; 
			 case 6 : file=new FileInputStream("takuzu6x6.serial"); break; 
			 case 8 : file=new FileInputStream("takuzu8x8.serial"); break; 
				}
			 
				ObjectInputStream stream =new ObjectInputStream(file);
                t=t.readObject(stream);
                stream.close();
                }
			catch(IOException e1) { System.out.println("Erreur de lecture ! "); }
			catch(ClassNotFoundException e2) { System.out.println("Erreur de lecture ! "); }
		
            date_debut=t.charger_temps(taille); // on charge le temps associé à la partie chargée
            
		    int[][] grille =t.getGrille();
		    System.out.println(t);
				System.out.println("Voulez vous continuer ou enregistrer cette partie ? C/E"); // C:continuer ; E:enregistrer
				Scanner s =new Scanner(System.in);
				c =s.nextLine().charAt(0);
				if(c=='C') {
		     System.out.println("donner les coordonnées i et j et la valeur "); // demander les coordonnées de la case et sa valeur
		     Scanner sc =new Scanner(System.in);
		     int i=sc.nextInt();
		     int j=sc.nextInt();
		     int val=sc.nextInt();
		    
		         grille[i][j]=val;
		         t.setGrille(grille); // on modifie la grille
		         System.out.println(t);
		         }
		
		   }
		
		if(reponse=='N') { // si la reponse est non 
		        t=new Takuzu(); //alors on cré un nouveau objet Takuzu
		          // et on génére aléatoirement la grille correspondant
			try {
			        t=t.generate();      
			}
			catch(RemplissageException e) {}
			date_debut=new Date(); // affecter la date de début du jeu
			
			}
		
		
	if(t!=null) {	// si l'objet Takuzu non nul			
       int [][] grille =t.getGrille(); 
        taille=t.getTaille();
        System.out.println(t); //affichage de la grille
        
        //Tant que la grille n'est pas valide et il existe encore des cases vides, on entre dans cette boucle
 		while(!t.estValide() && t.existe_moins_un()) {
			try {
				System.out.println("Voulez vous continuer ou enregistrer cette partie ? C/E"); // C:continuer ; E:enregistrer
				Scanner s =new Scanner(System.in);
				c =s.nextLine().charAt(0);
				if(c=='C') { // si la reponse est continuer
		     System.out.println("donner les coordonnées i et j et la valeur "); // on demande les coordonnées de la case à remplir et sa valeur 
		     Scanner sc =new Scanner(System.in);
		     int i=sc.nextInt(); // n° ligne
		     int j=sc.nextInt(); // n° colonne
		     int val=sc.nextInt(); // valeur
		     if((i>=taille)||(j>=taille)||((t.getGrille())[i][j]!=-1)||(i<0 )|| (j<0)|| ((val!=0)&&(val!=1)))
		    	 throw new IndiceException(); // si la valeur !=0 et !=1 et les coordonnées i et j incorrects on jette une exception
		     else { // sinon
		         grille[i][j]=val; 
		         t.setGrille(grille); // on modifie la matrice à chaque fois
		         System.out.println(t); // afficher la matrice
		         }
				}
				else if(c=='E') // si la reponse est enregistrée on quitte la boucle
					break;
			    
			}
			catch(IndiceException e)
			{
				
			}	
			
		}
		if(c=='C') {
		if(t.estValide()) { // si la partie est valide
			date_fin=new Date(); // affecter la date de fin du jeu
			
			// calculer la durée en faisant la différence entre les deux dates
			System.out.println("Bravo! Vous avez terminé en "+t.calcul_duree(date_debut, date_fin)); 
			t.enreg_meilleurs_temps(taille); //enregistrer le score
			System.out.println("Meilleur Score : "+t.min_temps(taille)+" s"); // afficher le meilleur score
			t.affiche(taille); // afficher le nombre de scores enregistrés 
	
			}
		else { // si la partie est invalide
			date_fin=new Date(); // affecter la date de fin du jeu
			System.out.println("Temps : "+t.calcul_duree(date_debut, date_fin)); // calculer la durée en faisant la différence entre les deux dates
			System.out.println("Dommage :( Grille Invalide");
			}

		}
		else if(c=='E') {
			date_fin=new Date(); // affecter la date de fin du jeu
			System.out.println("Temps : "+t.calcul_duree(date_debut, date_fin)); // calculer la durée en faisant la différence entre les deux dates
			t.enreg_temps(taille); //enregistrer le temps 
			//enregistrer la partie en enregistrant l'objet Takuzu
			FileOutputStream f=null;
			try {
				switch(taille) { // selon la taille on enregistre l'objet dans le fichier correspondant
				   case 4 : f=new FileOutputStream("takuzu4x4.serial"); break;
				   case 6 : f=new FileOutputStream("takuzu6x6.serial"); break;
				   case 8 : f=new FileOutputStream("takuzu8x8.serial"); break;
				}
				ObjectOutputStream stream =new ObjectOutputStream(f);
                t.writeObject(stream);
                stream.close();
                }
			catch(IOException e) { System.out.println("Erreur d'ecriture ! "); }
			System.out.println("Partie enregistrée ");
			}
		if(t.estValide() && reponse=='O') // si on compléte la partie chargée et elle est valide 
			t.suppr_fichier(taille); // on vide le fichier associé
	}
		
	
	}
}

