/**
 * Esercitazione 1.5 LFT
 * @author Daniele Di Palma
 * 
 * Automa per riconoscere il linguaggio di stringhe che contengono un cognome
 * seguito da una matricola
 * 
 * REGOLE: - Cognomi la cui iniziale è compresa tra A e K ed il numero di
 * matricola è PARI - Cognomi la cui iniziale è compresa tra L e Z ed il numero
 * di matricola è DISPARI - Un numero di matricola >= 1 cifre - Un cognome deve
 * avere almeno una lettera - Un cognome inizia con una lettera maiuscola e
 * prosegue con lettere minuscole
 * 
 * Esempi di stringhe accettate: "Bianchi123456", "Rossi654321", " B2 "
 * 
 * Esempi stringhe non accettate: "654322", "21312Rossi", "Rossi21312"
 */

public class E5_MatricolaT2T3CognomeNumero{

    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()){

            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch >= 'A' && ch <= 'K'|| ch >= 'L' && ch <= 'Z'){
                        if(ch >= 'A' && ch <= 'K')
                            state = 2;
                        else 
                            state = 1;   
                    }else
                        state = -1;
                    break;

                case 1:
                    if(Character.isLetter(ch) && Character.isLowerCase(ch))
                        state = 1;
                    else if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch) % 2 == 0)
                            state = 6;
                        else
                            state = 5;
                    }else
                        state = -1;
                    break;

                case 2:
                    if(Character.isLetter(ch) && Character.isLowerCase(ch))
                        state = 2;
                    else if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch) % 2 == 0)
                            state = 4;
                        else
                            state = 3;
                    }else
                        state = -1;
                    break;

                case 3:
                    if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch) % 2 == 0)
                            state = 4;
                        else
                            state = 3;
                    }else
                        state = -1;
                    break;

                case 4:
                    if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch)%2 == 0)
                            state = 4;
                        else
                            state = 3;
                    }else
                        state = -1;
                    break;

                case 5:
                    if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch) % 2 == 0)
                            state = 6;
                        else
                            state = 5;
                    }else
                        state = -1;
                    break;
                
                case 6:
                    if(Character.isDigit(ch)){
                        if(Character.getNumericValue(ch)%2 == 0)
                            state = 6;
                        else
                            state = 5;
                    }else
                        state = -1;
                    break;

            }
        }

        return state == 4 || state == 5;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
