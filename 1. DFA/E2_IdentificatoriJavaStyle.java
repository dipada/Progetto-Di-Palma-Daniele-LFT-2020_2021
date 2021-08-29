/**
 * Esercitazione 1.2 LFT
 * @author Daniele Di Palma
 *  
 * Automa per riconoscere il linguaggio degli identificatori in un linguaggioin stile Java.
 * 
 * Un identificatore è una sequenza non vuota di lettere, numeri e "_"
 * non comincia con un numero e non può essere composto dal solo simbolo "_"
 */

public class E2_IdentificatoriJavaStyle {
    
    public static boolean scan(String s){
        
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){

            final char c = s.charAt(i++);

            switch(state){

                case 0:
                    if(Character.isLetter(c))
                        state = 1;
                    else if(c == '_')
                        state = 2;
                    else 
                        state = -1;
                    break;
                
                case 1: // stato finale
                    if(Character.isLetter(c) || c == '_' || Character.isDigit(c))
                        state = 1;
                    else    // simboli non riconosciuti dall'automa
                        state = -1;                        
                    break;

                case 2:
                    if(Character.isLetter(c) || Character.isDigit(c))
                        state = 1;
                    else if(c == '_')
                        state = 2;
                    else
                        state = -1;                        
                    break;
            }
        }

        return state == 1;
    }

    public static void main(String[] args){

        System.out.println( scan(args[0]) ? "OK" : "NOPE");
    }
}
