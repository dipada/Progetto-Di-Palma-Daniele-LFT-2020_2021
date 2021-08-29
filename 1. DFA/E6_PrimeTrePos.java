/**
 * Esercitazione 1.6 LFT 
 * @author Daniele Di Palma
 * 
 * Automa per riconoscere il linguaggio delle stringhe tali che "a" occorre 
 * almeno una volta in una delle prime tre posizioni della stringa
 * 
 * Il DFA accetta anche stringhe < 3 simboli dove almeno un simbolo è "a"
 * 
 * Esempi di stringhe accettate: "ba", "abb", "a", "aa"
 * 
 * Esempi stringhe non accettate: "bbbaaa", "b", "bbbababab"
 */

public class E6_PrimeTrePos {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;
        
        while(state >= 0 && i < s.length()){

            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch == 'a')
                        state = 3;
                    else if(ch == 'b')
                        state = 1;
                    else
                        state = -1;                      
                    break;
                
                case 1:
                    if(ch == 'a')
                        state = 3;
                    else if(ch == 'b')
                        state = 2;
                    else
                        state = -1;
                    break;
                
                case 2:
                    if(ch == 'a')
                        state = 3;
                    else
                        state = -1;
                    break;
                
                case 3:
                    if(ch == 'a' || ch == 'b')
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
