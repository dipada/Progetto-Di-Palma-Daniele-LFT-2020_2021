/**
 * Esercitazione 1.9 LFT 
 * @author Daniele Di Palma
 * 
 * Automa per riconoscere il linguaggio di commenti delimitati da "/_*" e "*_/"
 * (il simbolo "_" sarà rimosso nel DFA) L'automa deve accettare le stringhe che
 * contengono almeno 4 caratteri e contenono una sola occorrenza della sequenza
 * "*_/"
 * 
 * 
 * Esempi di stringhe accettate: "/_****_/", "/_*a*_/"
 * 
 * Esempi stringhe non accettate: "/_*_/", "/_**_/_***_/"
 */

public class E9_CommentoSingolo {

    public static boolean scan(String s) {

        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {

            final char ch = s.charAt(i++);

            switch (state) {

                case 0:
                    if (ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == 'a' || ch == '/')
                        state = 2;
                    else if (ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch == '*')
                        state = 3;
                    else if (ch == '/')
                        state = 4;
                    else if (ch == 'a')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 4:
                    state = -1;
                    break;
            }
        }

        return state == 4;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
