package takuzu.jeu.console;

import java.io.IOException;

public interface Serializable {
	// des méthodes à redefinir
	  Takuzu readObject(java.io.ObjectInputStream stream)
			 throws IOException, ClassNotFoundException;
	   void writeObject(java.io.ObjectOutputStream stream)
			 throws IOException; 
}
