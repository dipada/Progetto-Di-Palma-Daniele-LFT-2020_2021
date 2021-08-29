/**
 * Esercitazione 1.10 LFT 
 * @author Daniele Di Palma
 * 
 * Automa per riconoscere il linguaggio di commenti delimitati da "/_*" e "*_/"
 * (il simbolo "_" sarà rimosso nel DFA) 
 * L'automa deve accettare le stringhe con commenti multipli, stringhe avanti ai commenti e dopo.
 *
 * Unico vincolo: ogni occorrenza di "/_*" deve essere seguita prima o poi da "*_/"
 * 
 * 
 * Esempi di stringhe accettate: "aaa/_****_/aa", "aa/_*a*a*_/"
 * 
 * Esempi stringhe non accettate: "aaa/_*_/aa", "a/_**_//_***a", "aa/_*aa"
 */

public class E10_CommentiMultipli {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){

            final char ch = s.charAt(i++);

            switch(state){

                case 0:
                    if(ch == 'a' || ch == '*')
                        state = 0;
                    else if(ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;

                case 1:
                    if(ch == '/')
                        state = 1;
                    else if(ch == 'a')
                        state = 0;
                    else if(ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if(ch == 'a' || ch == '/')
                        state = 2;
                    else if(ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 3:
                    if(ch == '*')
                        state = 3;
                    else if(ch == 'a')
                        state = 2;
                    else if(ch == '/')
                        state = 0;
                    else
                        state = -1;
                    break;
            }
        }

        return state == 0 || state == 1;
    }

    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
    
}
