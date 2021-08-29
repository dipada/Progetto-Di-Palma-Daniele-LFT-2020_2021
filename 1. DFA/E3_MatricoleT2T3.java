/**
 * Esercitazione 1.3 LFT
 * @author Daniele Di Palma
 *  
 * Automa per riconoscere il linguaggio di stringhe che contengono un numero di matricola
 * immediatamente seguito da un cognome.
 * 
 * REGOLE: 
 *      - Cognomi la cui iniziale è compresa tra A e K ed il numero di matricola è PARI
 *      - Cognomi la cui iniziale è compresa tra L e Z ed il numero di matricola è DISPARI
 *      - Un numero di matricola >= 1 cifre
 *      - Un cognome deve avere almeno una lettera
 *      - Un cognome inizia con una lettera maiuscola e prosegue con lettere minuscole
 * 
 *      Esempi di stringhe accettate: 
 *          "123456Bianchi", "654321Rossi", "2B"
 * 
 *      Esempi stringhe non accettate:
 *          "654322", "Rossi", "12BIanchi"
 */

public class E3_MatricoleT2T3 {
    
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
                    }else {
                        state = -1;
                    }
                    break;

                case 1: // matricola dispari
                    if(Character.isDigit(c)){
                        if(Character.getNumericValue(c)%2 == 0)
                            state = 2;
                        else
                            state = 1;                        
                    }else if(c >= 'L' && c <= 'Z')
                        state = 3;
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
                    else 
                        state = -1;
                    break;

                case 3:
                    if(Character.isLetter(c) && Character.isLowerCase(c))
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        
        return state == 3;
    }

    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
