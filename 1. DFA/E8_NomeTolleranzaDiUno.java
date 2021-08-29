/**
 * Esercitazione 1.8 LFT 
 * @author Daniele Di Palma
 * 
 * Automa per riconoscere il linguaggio delle stringhe contenenti il nome DANIELE e tutte le stringhe sostituendo UN SOLO carattere del nome
 * 
 * 
 * Esempi di stringhe accettate: "DANIELE", "DA%IELE", "DAnIELE"
 * 
 * Esempi stringhe non accettate: "DANI%%E", "D*N%ELE" 
 */

public class E8_NomeTolleranzaDiUno {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){

            final char ch = s.charAt(i++);

            switch(state){
                
                case 0:
                    if(ch == 'D')
                        state = 1;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 8;
                    else
                        state = -1;
                    break;

                case 1:
                    if(ch == 'A')
                        state = 2;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 9;
                    else
                        state = -1;
                    break;

                case 2:
                    if(ch == 'N')
                        state = 3;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 10;
                    else
                        state = -1;
                    break;
            
                case 3:
                    if(ch == 'I')
                        state = 4;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 11;
                    else
                        state = -1;
                    break;

                case 4:
                    if(ch == 'E')
                        state = 5;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 12;
                    else
                        state = -1;
                    break;
                
                case 5:
                    if(ch == 'L')
                        state = 6;
                    else if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 13;
                    else
                        state = -1;
                    break;

                case 6:
                    if(Character.isDefined(ch) && !Character.isDigit(ch))
                        state = 7;
                    else
                        state = -1;
                    break;

                case 7:     /* Stato accettante */
                    if(Character.isDefined(ch))
                        state = -1;
                    break;
                
                case 8:
                    if(ch == 'A')
                        state = 9;
                    else
                        state = -1;
                    break;
                
                case 9:
                    if(ch == 'N')
                        state = 10;
                    else
                        state = -1;
                    break;
                
                case 10:
                    if(ch == 'I')
                        state = 11;
                    else
                        state = -1;
                    break;

                case 11:
                    if(ch == 'E')
                        state = 12;
                    else
                        state = -1;
                    break;
                
                case 12:
                    if(ch == 'L')
                        state = 13;
                    else
                        state = -1;
                    break;
                
                case 13:
                    if(ch == 'E')
                        state = 7;
                    else
                        state = -1;
                    break;
            }
        }

        return state == 7;
    }
    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
