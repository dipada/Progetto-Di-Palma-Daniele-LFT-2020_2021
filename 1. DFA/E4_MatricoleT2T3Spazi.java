/**
 * Esercitazione 1.4 LFT
 * @author Daniele Di Palma
 *  
 * Automa per riconoscere il linguaggio di stringhe che contengono un numero di matricola
 * seguito da un cognome, anche separati da spazi.
 * 
 * REGOLE: 
 *      - Cognomi la cui iniziale è compresa tra A e K ed il numero di matricola è PARI
 *      - Cognomi la cui iniziale è compresa tra L e Z ed il numero di matricola è DISPARI
 *      - Un numero di matricola >= 1 cifre
 *      - Un cognome deve avere almeno una lettera
 *      - Un cognome inizia con una lettera maiuscola e prosegue con lettere minuscole
 *      - Non ci possono essere spazi tra le cifre del numero di matricola 
 *      - Un cognome composto deve iniziare sempre con lettere Maiuscole " Inquesto Modo"
 * 
 *      Esempi di stringhe accettate: 
 *          "  123456 Bianchi   ", "654321 Rossi", " 2B "
 * 
 *      Esempi stringhe non accettate:
 *          "654322", "213 12Rossi", "12BIa nchi "
 */

public class E4_MatricoleT2T3Spazi {
 
    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){

            final char c = s.charAt(i++);

            switch(state){
                case 0:
                    if(Character.isDigit(c)){
                        if(Character.getNumericValue(c)%2 == 0)
                            state = 2;
                        else
                            state = 1;
                    }else if(Character.isWhitespace(c))
                        state = 0;
                    else
                        state = -1;              
                    break;

                case 1: // matricola dispari
                    if(Character.isDigit(c)){
                        if(Character.getNumericValue(c)%2 == 0)
                            state = 2;
                        else
                            state = 1;                        
                    }else if(c >= 'L' && c <= 'Z')
                        state = 3;
                    else if(Character.isWhitespace(c))
                        state = 5;
                    else
                        state = -1;
                    break;

                case 2: // matricola pari
                    if(Character.isDigit(c)){
                        if(Character.getNumericValue(c)%2 == 0)
                            state = 2;
                        else
                            state = 1;
                    }else if(c >= 'A' && c <= 'K')
                        state = 3;
                    else if(Character.isWhitespace(c))
                        state = 4;
                    else
                        state = -1;
                    break;

                case 3:
                    if(Character.isLetter(c) && Character.isLowerCase(c))
                        state = 3;
                    else if(Character.isWhitespace(c))
                        state = 6;
                    else 
                        state = -1;
                    break;

                case 4:
                    if(Character.isWhitespace(c))
                        state = 4;
                    else if(c >= 'A' && c <= 'K')
                        state = 3;
                    else
                        state = -1;
                    break;
                
                case 5:
                    if(Character.isWhitespace(c))
                        state = 5;
                    else if(c >= 'L' && c <= 'Z')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 6:
                    if(Character.isLetter(c) && Character.isUpperCase(c))
                        state = 3;
                    else if(Character.isWhitespace(c))
                        state = 6;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 3 || state == 6;
    }

    public static void main(String[] args){
        System.out.println( scan(args[0]) ? "OK" : "NOPE");
    }
}
