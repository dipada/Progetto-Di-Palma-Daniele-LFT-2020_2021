/**
 * Classe per rappresentare i Token corrispondenti ai numeri
 * 
 * @author Daniele Di Palma
 */

public class NumberTok extends Token {

	public final int value;

	public NumberTok(int value){
		super(Tag.NUM);		// Inizializza il campo tag della superclasse
		this.value = value;
	}

	public String toString() {
		return "<" + tag + ", " + value + ">";
	}

}
