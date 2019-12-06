package takuzu.jeu.console;

public class tailleException extends Exception{
	public int t;
	public tailleException() {
		System.out.println("la taille doit etre 4 ou 6 ou 8 !");
	}
}
