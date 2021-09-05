/**
 * Classe per rappresentare i Token corrispondenti ai numeri
 * 
 * @author Daniele Di Palma
 */

public class NumberTok extends Token {

	public int value;

	public NumberTok(int v){
		super(Tag.NUM);		// Inizializza il campo tag della superclasse
		value = v;
	}

	public String toString() {
		return "<" + tag + ", " + value + ">";
	}

}
