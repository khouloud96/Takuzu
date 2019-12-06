package takuzu.jeu.console;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Takuzu implements Serializable {
	// les attributs de classe
	static private final long serialVersionUID = 51L;
	static public int nbre_meilleur_score = 0;
	static public int nbre_meilleur_score_4 = 0;
	static public int nbre_meilleur_score_6 = 0;
	static public int nbre_meilleur_score_8 = 0;
	private int[][] grille; // la matrice
	private int taille; // taille de matrice
	private int temps = 0; // la durée qu'on passe en jouant = score du jeu

	// constructeur par défaut
	public Takuzu() {
		// initialiser l'attribut taille
		System.out.println("Donner la taille de grille ");
		Scanner sc = new Scanner(System.in);
		taille = sc.nextInt();
		// on fait la controle
		while ((taille != 4) && (taille != 6) && (taille != 8)) 
		{
			System.out.println("Donner la taille de grille ");
			taille = sc.nextInt();
		}
        // on initialise la grille
		int[][] grille = new int[taille][taille];
		this.grille = grille;
		// on initialise le temps
		this.temps = 0;

	}

	public Takuzu(char c) {}
	
	public Takuzu(int[][] grille) {
		this.grille = grille;
	}

	public Takuzu(int taille) {
        this.taille=taille;
		int[][] grille = new int[taille][taille];
		this.grille = grille;
		this.temps = 0;
	}

	// constructeur qui donne une grille vide
	public Takuzu(int[][] grille, int taille, int temps) {
		this.taille = taille;
		this.temps = temps;
		this.grille = grille;
	}

	public boolean controle() // cette méthode controle la génération de la takuzu
	{
		int nbr1 = 0, nbr2 = 0;
		boolean res = true;

		if (taille == 4) {
			for (int i = 0; i < taille - 1; i++) {
				for (int j = 0; j < taille - 1; j++) {
					if ((grille[i][j] == 0 && grille[i + 1][j] == 0 && grille[i][j + 1] == 0 && grille[i + 1][j + 1] == 0) 
							|| (grille[i][j] == 1 && grille[i + 1][j] == 1 && grille[i][j + 1] == 1 && grille[i + 1][j + 1] == 1))
						return false;
				}
			}
		}
		// parcours des lignes
		for (int i = 0; i < taille; i++) {
			int[] tab = grille[i];
			for (int j = 0; j < taille; j++) {
				if (grille[i][j] == 0)
					nbr1++; // compter le nombre de 0 sur une ligne
				if (grille[i][j] == 1)
					nbr2++; // compter le nombre de 1 sur une ligne
			}
			if (nbr1 > taille / 2 || nbr2 > taille / 2) // si le nombre de 0 ou de 1 sur une ligne depasse la moitie de
														// la taille on retourne faux
				return false;
			nbr1 = 0;
			nbr2 = 0;
		}
		nbr1 = 0;
		nbr2 = 0;
		// parcours des colonnes
		for (int j = 0; j < taille; j++) {
			for (int i = 0; i < taille; i++) {
				if (grille[i][j] == 0)
					nbr1++; // compter le nombre de 0 sur une colonne
				if (grille[i][j] == 1)
					nbr2++; // compter le nombre de 1 sur une colonne
			}
			if (nbr1 > taille / 2 || nbr2 > taille / 2) // si le nombre de 0 ou de 1 sur une colonne depasse la moitie
														// de la taille on retourne faux
				return false;
			nbr1 = 0;
			nbr2 = 0;
		}

		return res;
	}

	public Takuzu generate() throws RemplissageException {
		Random rand = new Random();
		int nb = 0;
		int l, c, val;
		// initialiser la grille à -1
		for (int i = 0; i < grille.length; i++) {
			// Parcours des colones de la grille
			for (int j = 0; j < grille[i].length; j++) {
				this.grille[i][j] = -1; // La case est "vide"
			}
		}

		// générer des indices aléatoires pour remplir des cases avec des 0 et des 1
		while (nb < taille) { // nbre des cases= taille au maximum
			do {
				l = rand.nextInt(this.grille.length); // n° ligne
				c = rand.nextInt(this.grille.length); // n° colonne
				val = rand.nextInt(2);
				if (this.controle() && this.pas_plus_deux_chiff_ident())
					grille[l][c] = val;
				// System.out.println(l+" "+c+ " "+val);
			} while (!this.pas_plus_deux_chiff_ident() && !this.controle());

			nb++;
		}

		return this;
	}

	// Getters et setters
	public int[][] getGrille() {
		return grille;
	}

	public void setGrille(int[][] grille) {
		this.taille = grille.length;
		this.grille = grille;
	}

	public int getTemps() {
		return temps;
	}

	public void setTemps(int t) {
		temps = t;
	}

	public int getTaille() {
		return taille;
	}

	
	// cette méthode permet d'afficher le nombre de meilleurs scores pour chaque taille
	static public void affiche(int taille_g) {
		switch(taille_g) {
		case 4 : System.out.println("le nombre de meilleurs scores est " + nbre_meilleur_score_4); break;
		case 6 : System.out.println("le nombre de meilleurs scores est " + nbre_meilleur_score_6); break;
		default : System.out.println("le nombre de meilleurs scores est " + nbre_meilleur_score_8); break;
		}
	}

	
	//cette méthode permet de calculer le nombre de meilleurs scores en lisant le fichier associé et en comptant le nombre de lignes
	static public int nbre_meilleur_score(int taille_g) {
		int nb = 0; // nombre de scores
		File f=null;
		Scanner sc;
		try {
			switch(taille_g) {
			case 4 : f = new File("les_dix_meilleurs4x4.txt"); break;
			case 6 : f= new File("les_dix_meilleurs6x6.txt"); break;
			default : f= new File("les_dix_meilleurs6x6.txt"); break;
					}
			sc = new Scanner(f);
			if (f.length() <= 0) // si le fichier est vide alors le nombre est égale à 0
				nb = 0;
			else {
              //tant que le fichier n'est pas vide on incrémente le nombre de scores 
				nb++; 
				while (sc.hasNextInt()) {
					nb++; //tant qu'il existe une ligne non vide on incrémente le nombre de scores
				}
			}
		} catch (IOException e) {
			System.out.println("Erreur de lecture dans le fichier temps: " + e.getMessage());
		}
		switch(taille_g) {
		   case 4 : nbre_meilleur_score_4 = nb; break;
		   case 6 : nbre_meilleur_score_6 = nb; break;
		   default : nbre_meilleur_score_8 = nb; break;
		}
		return nb;
	}

	// cette méthode permet l'affichage du grille
	public String toString() {
		String s = "";
		for (int i = 0; i < grille.length; i++) {
			s += Arrays.toString(grille[i]);
			if (i != grille.length - 1) // on ajoute une ligne à la variable s
				s += "\n"; // on sépare les les lignes par un retour à la ligne
		}
		return s.replace(",", "").replace("-1", "_"); 
	}

	public Takuzu clone() {
		int[][] clone = new int[this.grille.length][];
		for (int i = 0; i < clone.length; i++) {
			clone[i] = this.grille[i].clone();
		}

		return new Takuzu(clone);
	};

	public boolean verif_valeur() // cette méthode vérifie si la grille ne contient que des 0 et des 1
	{
		boolean res = true;
		// on parcourt la matrice
		for (int sousgri[] : grille) {
			for (int val : sousgri) {
				if ((val != 0) && (val != 1)) {
					res = false; //si on trouve une valeur différente de 0 ou 1 on quitte la boucle
					break;
				}
			}
			if (res == false)
				break;
		}

		return res;
	}

	public boolean Compare_Ligne(int[][] mat, int i) // cette méthode retourne vrai si le nombre des 0 est égal
	{ // au celui des 1 sur la ième ligne , faux sinon
		int a = 0; // nombre de 0
		int b = 0; // nombre de 1
		for (int j = 0; j < mat.length; j++) {
			if (mat[i][j] == 0)
				++a;
			else if (mat[i][j] == 1)
				++b;
		}
		if (a == b) {
			return true;
		}
		return false;
	}

	public boolean Compare_Colonne(int[][] mat, int j) // cette méthode retourne vrai si le nombre des 0 est égal
	{ // au celui des 1 sur la jème colonne, faux sinon
		int a = 0; // nombre de 0
		int b = 0; // nombre de 1
		for (int i = 0; i < mat.length; i++) {
			if (mat[i][j] == 0)
				++a;
			else if (mat[i][j] == 1)
				++b;
		}
		if (a == b) {
			return true;
		}

		return false;
	}

	public boolean Compare() { // cette méthode retourne vrai si le nbre de 0 et celui de 1 sont égales sur les
								// lignes et les colonnes
		for (int i = 0; i < taille; i++) // faux sinon
		{
			if (!Compare_Ligne(grille, i))
				return false;
			if (!Compare_Colonne(grille, i))
				return false;
		}
		return true;
	}

	public boolean lignes_differents() // cette méthode retourne true si tous les lignes sont différents
	{                                  // et false s'il existe 2 lignes identiques
		boolean res = true;
		int c = 0; // nbre de colonnes
		for (int i = 0; i < taille - 1; i++) {
			int[] tab1 = grille[i]; // la i-éme ligne
			for (int k = i + 1; k < taille; k++) {
				int[] tab2 = grille[k]; // la k-éme ligne
				for (int j = 0; j < taille; j++) {
					if (tab1[j] == tab2[j])
						c++; 
				}
				if (c == taille) // c=taille càd la i-eme et le k-eme ligne sont égaux 
				{ 
					return false;
				}
				c = 0;
			}

		}
		return res;
	}

	
	public boolean colonnes_differents() // cette méthode retourne true si tous les colonnes sont differents
	{                                   // et false s'il existe 2 colonnes identiques
		boolean res = true;
		int c = 0; //nbre de lignes
		for (int j = 0; j < taille - 1; j++) {
			for (int k = j + 1; k < taille; k++) {
				for (int i = 0; i < taille; i++) {
					if (grille[i][j] == grille[i][k])
						c++;
				}
				if (c == taille)
					return false;
				c = 0;
			}

		}

		return res;
	}

	public boolean pas_plus_deux_chiff_ident() // cette méthode permet de vérifier s'il existe plus que deux zero
												// consécutifs
	{                                          // s'il existe elle retourne faux
		boolean res = true;                    // sinon elle retourne vrai

		// parcours des colonnes
		for (int i = 0; i < taille - 2; i++) {
			for (int j = 0; j < taille; j++) {
				if ((grille[i][j] == 0 || grille[i][j] == 1)
						&& (grille[i][j] == grille[i + 1][j] && grille[i + 1][j] == grille[i + 2][j]))
					return false;
			}
		}
		// parcours des lignes
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille - 2; j++) {
				if ((grille[i][j] == 0 || grille[i][j] == 1)
						&& (grille[i][j] == grille[i][j + 1] && grille[i][j + 1] == grille[i][j + 2]))
					return false;
			}
		}
		return res;
	}

	public boolean estValide() // cette méthode retourne vrai si la grille est valide , faux sinon
	{
		if (pas_plus_deux_chiff_ident() && lignes_differents() && colonnes_differents() && Compare()
				&& verif_valeur()) {
			return true;
		}
		return false;
	}

	public boolean existe_moins_un() // cette méthode retourne vrai s'il existe encore des -1 càd des cases vides dans la grille
	{                                  // faux sinon
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < taille; j++) {
				if (grille[i][j] == -1)
					return true;
			}
		}
		return false;
	}

	public void suppr_fichier(int taille_g) // cette méthode permet de vider le fichier où on enregistre l'objet takuzu
								// et celui où on enregistre le temps
	{
		File f=null;

		if(taille_g==4) {
		         f = new File("takuzu4x4.serial");
	    }
		if(taille_g==6) { 
			     f = new File("takuzu6x6.serial");
		}
		if(taille_g==8) { 
			     f = new File("takuzu8x8.serial");
		}
		try {
			FileOutputStream fos = new FileOutputStream(f); // dès qu'on cré un FileOutputStream (sans spécifications), le fichier est éffacé.
			fos.close( );
			boolean b=f.delete();
			} catch (IOException e) {}
		      
	}

	static public boolean est_vide(int taille_g) // cette méthode retourne vrai si le fichier est vide, faux sinon
	{
		File f=null;
		switch(taille_g) {
		   case 4 : f= new File("takuzu4x4.serial"); break;
		   case 6 : f= new File("takuzu6x6.serial"); break;
		   case 8 : f= new File("takuzu8x8.serial"); break;
		   default :  System.out.println("taille invalide ! "); break;
		}
		if(taille_g != 4 && taille_g !=6 && taille_g!= 8)
			return true;
        
		if(f.length() <= 1 || f.exists()==false)
			return true;
		return false;
	}

	
	// cette méthode retourne le meilleur score en cherchant le min parmis les meilleurs scores enregistrés dans le fichier associé
	public int min_temps(int taille_g) {
		int min = 0;
		File f=null;
		Scanner sc;
		try {
			switch(taille_g) {
			case 4 : f = new File("les_dix_meilleurs4x4.txt"); break;
			case 6 : f = new File("les_dix_meilleurs6x6.txt"); break;
			default : f = new File("les_dix_meilleurs8x8.txt"); break;
			}
			if (!(f.length() == 0)) {
				sc = new Scanner(f);
				min = sc.nextInt();
				while (sc.hasNextInt()) {
					int a = sc.nextInt();

					if (min > a)
						min = a;
				}
			}
		} catch (IOException e) {
			System.out.println("Erreur de lecture dans le fichier temps: " + e.getMessage());
		}
 
		return min;
	}

	// cette méthode permet de supprimer le score maximum afin d'ajouter un autre plus petit
	public void supprimer(int max,int taille_g) {
		String ligne = null;
		String str = "" + max;
		BufferedReader in;
		File f=null;
		File file=null; // on cré un nouveau fichier
		PrintWriter pw;
		switch(taille_g) {
		case 4 : f = new File("les_dix_meilleurs4x4.txt");
		         file = new File("les_dix4x4.txt");
		         break;
		case 6 : f = new File("les_dix_meilleurs6x6.txt");
                 file = new File("les_dix6x6.txt");
                 break;
		default : f = new File("les_dix_meilleurs8x8.txt");
                 file = new File("les_dix8x8.txt");
                 break;
		}
		try {
			in = new BufferedReader(new FileReader(f)); // flux de lecture
			pw = new PrintWriter(new FileWriter(file)); // flux d'ecriture
			while ((ligne = in.readLine()) != null) {
				if (!ligne.trim().equals(str)) { // si la ligne != max
					pw.println(ligne); // on écrit dans le nouveau fichier
					pw.flush();
				}

			}
			pw.println("" + this.temps); // on ajoute le temps actuel 
			// on ferme les flux
			in.close();
			pw.close();

		} catch (IOException e) {
			System.out.println("Erreur de lecture dans le fichier temps: " + e.getMessage());
		}

	}

	
	// cette méthode permet de vider le fichier "les_dix.txt" et le remplacer par "les_dix_mailleurs.txt"
	public void delete_file(int taille_g) {
		String ligne = null;
		BufferedReader in;
		File f=null,file=null;
		PrintWriter pw;
		switch(taille_g) {
		case 4 : file=new File("les_dix4x4.txt"); break;
		case 6 : file=new File("les_dix6x6.txt"); break;
		default : file=new File("les_dix8x8.txt"); break;
		}
		
	    f=new File("les_dix_meilleurs4x4.txt");
		if(file.length() > 0) {
		try {
			in = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new FileWriter(f));
			while ((ligne = in.readLine()) != null) {
					pw.println(ligne);
					pw.flush();
				}
			in.close();
			pw.close();

		} catch (IOException e) {
			System.out.println("Erreur ! " + e.getMessage());
		 }
		
		// on cré un flux d'ecriture pour le fichier "les_dix.txt" afin de le vider
		FileOutputStream fos;
		try {
			fos=new FileOutputStream(file);
			fos.close();
		}
		catch(IOException e1) {}
		}

	}

	//cette méthode permet de retourner le score maximum
	public int max_temps(int taille_g) {
		int max = 1000;
		int nb = 0;
		File f=null;
		Scanner sc;
		try {
			switch(taille_g) {
			case 4 : f = new File("les_dix_meilleurs4x4.txt"); break;
			case 6 : f = new File("les_dix_meilleurs6x6.txt"); break;
			default : f = new File("les_dix_meilleurs8x8.txt"); break;
			}
			if (f.length() < 0)
				nb = 0;
			else {
				sc = new Scanner(f);
				max = sc.nextInt();
				nb++;
				while (sc.hasNextInt()) {
					int a = sc.nextInt();
					nb++;
					if (max < a)
						max = a;
				}
			}
		} catch (IOException e) {
			System.out.println("Erreur de lecture dans le fichier les_dix_meilleurs: " + e.getMessage());
		}
		switch(taille_g) {
		case 4 : nbre_meilleur_score_4 = nb; break;
		case 6 : nbre_meilleur_score_6 = nb; break;
		default : nbre_meilleur_score_8 = nb; break;
		}
		return max;
	}

	
	// cette méthode permet d'enregistrer les 10 meilleurs scores 
	public void enreg_meilleurs_temps(int taille_g) {
		int max = max_temps(taille_g);
        Writer fileWriter=null;
		try {
			switch(taille_g) {
			case 4 : fileWriter = new FileWriter("les_dix_meilleurs4x4.txt", true);nbre_meilleur_score=nbre_meilleur_score_4; break;
			case 6 : fileWriter = new FileWriter("les_dix_meilleurs6x6.txt", true); nbre_meilleur_score=nbre_meilleur_score_6; break;
			default : fileWriter = new FileWriter("les_dix_meilleurs8x8.txt", true); nbre_meilleur_score=nbre_meilleur_score_8;  break;	
			}
			if (nbre_meilleur_score < 10) {
				fileWriter.write(System.lineSeparator());
				fileWriter.write("" + this.temps);
				nbre_meilleur_score++;
			}
			if (nbre_meilleur_score == 10 && this.temps < max) {
				supprimer(max,taille_g);
				nbre_meilleur_score = 10;
			}
			fileWriter.close();
			this.delete_file(taille_g);
		} catch (IOException e) {
			System.out.println("Erreur d'ecriture dans le fichier temps: " + e.getMessage());
		}
		switch (taille_g) {
		case 4 : nbre_meilleur_score_4=nbre_meilleur_score; break;
		case 6 : nbre_meilleur_score_6=nbre_meilleur_score; break;
		default : nbre_meilleur_score_8=nbre_meilleur_score;break;
		}
	}

	//cette méthode permet d'enregistrer le temps de la partie non complétée
	public void enreg_temps(int taille_g) {
		FileWriter fileWriter=null;
		try {
			switch(taille_g) {
			case 4 : fileWriter = new FileWriter("temps4x4.txt");break;
			case 6 : fileWriter = new FileWriter("temps6x6.txt");break;
			default : fileWriter = new FileWriter("temps8x8.txt");break;	
			}
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(this.temps);
			printWriter.close();
		} catch (IOException e) {
			System.out.println("Erreur d'ecriture dans le fichier temps: " + e.getMessage());
		}
	}

	//cette méthode permet de charger le temps de la partie enregistrée 
	public Date charger_temps(int taille_g) {
		Date d = new Date();
		Reader fileReader ;
		int t = 0;
		try {
		    //fileReader = new FileReader("temps4x4.txt");
			switch(taille_g) {
			case 4 : fileReader = new FileReader("temps4x4.txt"); break;
			case 6 : fileReader = new FileReader("temps6x6.txt"); break;
			default : fileReader = new FileReader("temps8x8.txt"); break;	
			}
			BufferedReader in = new BufferedReader(fileReader);
			String b = new String();
			b = in.readLine();
			t = Integer.parseInt(b);
			in.close();
		} catch (IOException e) {
			System.out.println("Erreur de lecture dans le fichier temps: " + e.getMessage());
		}
		this.temps = t;
		d.setSeconds(t);
		System.out.println("Temps : " + this.temps + " s");
		return d;
	}

	
    //cette méthode permet de lire un objet Takuzu
	public Takuzu readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {

		grille = (int[][]) stream.readObject();
		taille = (int) stream.readInt();
		temps = (int) stream.readInt();
		return new Takuzu(grille, taille, temps);
	}

	//cette méthode permet d'enregistrer un objet Takuzu
	public void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.writeObject(grille);
		stream.writeInt(taille);
		stream.writeInt(temps);
	}
	
	

	// cette méthode calcule la durée de remplissage de la grille
	public String calcul_duree(Date date_deb, Date date_fin) {
		int h, min, sec;
		String res = new String();

		h = date_fin.getHours() - date_deb.getHours(); // nbre des heures
		min = date_fin.getMinutes() - date_deb.getMinutes(); // nbre des minutes
		sec = date_fin.getSeconds() - date_deb.getSeconds(); // nbre des secondes
		if (h < 0) {
			h = 0;
			min = 60 - min;
		}
		if (sec < 0) {
			min = min - 1;
			sec = sec + 60;
		}
		if (min < 0) {
			min = min + 60;
			h = h - 1;
		}

		this.temps = this.temps + h * 3600 + min * 60 + sec;
		h = this.temps / 3600;
		min = (this.temps % 3600) / 60;
		sec = (this.temps % 3600) % 60;
		res = h + " h " + min + " min " + sec + " s";
		if (h == 0)
			res = min + " min " + sec + " s";
		if (min == 0 && h == 0)
			res = sec + " s";
		return res;
	}
}
