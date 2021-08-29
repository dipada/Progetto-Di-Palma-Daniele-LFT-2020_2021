/**
 * Esercitazione 1.1 LFT
 * @author Daniele Di Palma
 *  
 * Automa per riconoscere il linguaggio delle stringhe di 0 e 1 
 * che NON contengono 3 zeri consecutivi
 */

public class E1_TreZeriConsecutiviComplementare {

    public static boolean scan(String s){
        int state = 0; 
        int i = 0;  // indice prossimo carattere della stringa s da analizzare

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch == '0')
                        state = 1;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                
                case 1:
                    if(ch == '0')
                        state = 2;
                    else if (ch == '1')
                        state = 0;
                    else 
                        state = -1;
                    break;
                
                case 2:
                    if(ch == '0')
                        state = 3;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                
                case 3:
                    if(ch == '0' || ch == '1')
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }

        return state == 0 || state == 1 || state == 2;
    }
    
    public static void main(String[] args){
        System.out.println( scan(args[0]) ? "OK" : "NOPE");
    }
}
