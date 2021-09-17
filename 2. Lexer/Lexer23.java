/**
 * Esercitazione 2.3 LFT 2020/2021
 * Classe implementante un lexer, legge da file un input e stampa la sequenza di token corrispondente
 * 
 * @author Daniele Di Palma
 *
 */

import java.io.*;

public class Lexer23 {

    public static int line = 1;

    /**
    *  Variabile per gestire il char in analisi
    */
    private char peek = ' ';

    /**
     * Metodo per estrarre un singolo char dal Buffer.
     * Dotato di eccezione in caso di errori o EOF.
     * 
     * @param br Buffer aperto sul file dal quale analizzo i char
     */
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    /**
     * Metodo per scan. Analizza carattere per carattere l'input individuando il token rispettivo.
     */
    public Token lexical_scan(BufferedReader br) {
        
        // Ignoro spazi, tabulazioni e ritorni a capo 
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }

        switch (peek) {
               
            // Token senza attributo
            // ! ( ) { } + - * / = ;
            case '!':
                peek = ' ';
                return Token.not;
            
            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;
                        
            case '{':
                peek = ' ';
                return Token.lpg;
            
            case '}':
                peek = ' ';
                return Token.rpg;
            
            case '+':
                peek = ' ';
                return Token.plus;
            
            case '-':
                peek = ' ';
                return Token.minus;
            
            case '*':
                peek = ' ';
                return Token.mult;
            
            case '/':
                readch(br);
                if(peek == '/'){ // caso del commento inline
                    // scorro fino ad un a capo o EOF
                    readch(br);
                    while(peek != '\n' && peek != (char)-1){
                        readch(br);
                    }
                    return lexical_scan(br);
                }else if(peek == '*'){ // caso commento Multi-linea
                    boolean commentEnd = false;
                    readch(br);
                    while (!commentEnd) {             
                        if(peek == '*'){ 
                            readch(br);
                            if(peek == '/')
                                commentEnd = true;
                        }
                        if(peek == (char)-1){
                            System.err.println("Multi-Lane comment is open and EOF occurred");
                            return null;
                        }
                        readch(br);
                    }
                    return lexical_scan(br);
                }else{
                    return Token.div;
                }       
            
                
            
            case ';':
                peek = ' ';
                return Token.semicolon;
            
            

            // token con attributo categoria operatori relazionali 
            // && || < > <= >= <> == 

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : " + peek);
                    return null;
                }
            
            case '|':
                readch(br); // rileggo dal buffer per vedere cosa c'è dopo
                if(peek == '|'){
                    peek = ' ';
                    return Word.or;
                }else {
                    System.err.println("Erroneous character" + " after | : " + peek);
                    return null;
                }

            case '<':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.le;
                }else if(peek == '>'){
                    peek = ' ';
                    return Word.ne;
                }else // significa che è solo <
                    return Word.lt;
                
            case '>':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.ge;
                }else 
                    return Word.gt;
                
            case '=':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.eq;
                }else 
                    return Token.assign;

            // Token per fine file
            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if(Character.isLetter(peek) || peek == '_'){
                    String s = "" ;

                    while(peek == '_' && peek != (char)-1){
                        s = s.concat(String.valueOf(peek));
                        readch(br);
                    }
                    
                    // Se non ha incontrato lettere, numeri simboli ecc, sarà uscito per EOF
                    if(peek == (char)-1){
                        System.err.println("Wrong identifier sequence. Only underscore.");
                        return null;
                    }
                    if(!Character.isLetter(peek) && !Character.isDigit(peek)){
                        System.err.println("Erroneous character: " + peek);
                        return null;
                    }else{
                        while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'){
                            s = s.concat(String.valueOf(peek));
                            readch(br);
                        }
                    
                        // letto qualcosa di diverso da lettere, numeri o underscore.  Il carattere appena letto non viene utilizzato.
                        // Eventuali caratteri illeciti verranno rilevati dopo aver "tokenizzato" quello già letto finora.

                        if(s.toLowerCase().equals("cond")) return Word.cond;
                        if(s.toLowerCase().equals("when")) return Word.when;
                        if(s.toLowerCase().equals("then")) return Word.then;
                        if(s.toLowerCase().equals("else")) return Word.elsetok;
                        if(s.toLowerCase().equals("while")) return Word.whiletok;
                        if(s.toLowerCase().equals("do")) return Word.dotok;
                        if(s.toLowerCase().equals("seq")) return Word.seq;
                        if(s.toLowerCase().equals("print")) return Word.print;
                        if(s.toLowerCase().equals("read")) return Word.read;
                        
                        // arrivato qui allora è un identificatore
                        return new Word(Tag.ID, s);

                    }
                
                    

                } else if (Character.isDigit(peek)) {
                    // Gestione dei numeri

                    if(peek == '0'){ // 0 non può essere seguito da altre cifre.
                        char n = peek;
                        readch(br);
                        if(Character.isDigit(peek)){
                            System.err.println("Erroneous character later number 0: " + peek);
                            return null;
                        }
                        return new NumberTok(Character.getNumericValue(n));
                    }

                    String nums = String.valueOf(peek);
                    readch(br);

                    while(Character.isDigit(peek)){ 
                        nums = nums.concat(String.valueOf(peek));
                        readch(br);
                    }
                    // letto qualcosa di diverso da un numero. Il carattere appena letto non viene utilizzato.
                    // Eventuali caratteri illeciti verranno rilevati dopo aver "tokenizzato" quello già letto finora.
                    
                    return new NumberTok(Integer.parseInt(nums));

                } else {
                    // Segnala la presenza di caratteri illeciti
                    // # @ : ~ ^ ` % $ [ ] ? ecc...
                    System.err.println("Erroneous character: " + peek);
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer23 lex = new Lexer23();
        String path = "E2_3_test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
