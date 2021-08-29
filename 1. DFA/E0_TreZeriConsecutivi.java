/**
 * Esercitazione 1.0 LFT
 * @author Daniele Di Palma
 * 
 * Implementazione di un metodo java che sia in grado di discriminare
 * le stringhe del linguaggio riconosciuto da un DFA dato.
 * 
 * Automa definito sull'alfabeto { 0 , 1 } in grado di riconoscere stringhe 
 * in cui compaiono almeno 3 zeri consecutivi
 */

public class E0_TreZeriConsecutivi {

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

        return state == 3;
    }
    
    public static void main(String[] args){
        System.out.println( scan(args[0]) ? "OK" : "NOPE");
    }
}
