/**
 * Esercitazione 3.2 LFT 2020/2021 @author Daniele Di Palma
 * 
 * Classe implementante un Parser per un semplice linguaggio di programmazione
 * Utilizza il Lexer23
 *
 */

import java.io.*;

public class Parser32 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser32(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Metodo per la lettura del token successivo
     * 
     * Stampa il token se è grammaticalmente corretto
     */
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    /**
     * Metodo per la segnalazione dell'errore
     * 
     * @param s messaggio dell'errore
     * 
    */
    void error(String s) {
	    throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * 
     * Metodo che controlla il simbolo 
     * se è coincidente avanza con la lettura del simbolo successivo
     * altrimenti lancia un'errore
     * 
     * @param t simbolo in ASCII code
    */
    void match(int t) {
	    if (look.tag == t) {
	        if (look.tag != Tag.EOF) 
            move();
	    } else 
            error("syntax error");
    }

    /**
     * Metodo che avvia il parsing
     * 
     */
    public void prog() {    
        
        switch(look.tag){   

            case '=':  // P -> SL
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                statlist();
	            match(Tag.EOF);
                break;
            
            default:
                error("[prog]>> syntax error");
        }
    }

    private void statlist(){

        switch(look.tag){   

            case '=':  // SL -> S SLP
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                stat();
                statlistp();
                break;

            default:
                error("[statlist]>> syntax error");
        }
    }

    private void statlistp(){
        
        switch(look.tag){

            case ';':   // SLP -> ; S SLP
                match(Token.semicolon.tag);
                stat();
                statlistp();
                break;
            
            case Tag.EOF:           // SLP -> EPS
            case '}':
                break;

            default:
                error("[statlistp]>> syntax error");
        }
    }

    private void stat(){
        
        switch(look.tag){

            case '=':  // S -> = ID E
                match(Token.assign.tag);
                match(Tag.ID);
                expr();
                break;
            
            case Tag.PRINT:         // S -> print ( EL ) 
                match(Tag.PRINT);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;
            
            case Tag.READ:          // S -> read ( ID )
                match(Tag.READ);
                match(Token.lpt.tag);
                match(Tag.ID);
                match(Token.rpt.tag);
                break;
            
            case Tag.COND:          // S -> cond WL else S
                match(Tag.COND);
                whenlist();
                match(Tag.ELSE);
                stat();
                break;
            
            case Tag.WHILE:         // S -> while ( B ) S
                match(Tag.WHILE);
                match(Token.lpt.tag);
                bexpr();
                match(Token.rpt.tag);
                stat();
                break;
            
            case '{':     // S -> { SL }
                match(Token.lpg.tag);
                statlist();
                match(Token.rpg.tag);
                break;
            
            default:
                error("[stat]>> syntax error");
        }
    }
    
    private void whenlist(){

        switch(look.tag){

            case Tag.WHEN:          // WL -> WI WLP
                whenitem();
                whenlistp();
                break;
            
            default:
                error("[whenlist]>> syntax error");        
        }
    }

    private void whenlistp(){

        switch(look.tag){

            case Tag.WHEN:          // WLP -> WI WLP
                whenitem();
                whenlistp();
                break;

            case Tag.ELSE:          // WLP -> EPS
                break;

            default:
                error("[whenlistp]>> syntax error");
        }
    }

    private void whenitem(){
        
        switch(look.tag){

            case Tag.WHEN:          // WI -> when ( B ) do S
                match(Tag.WHEN);
                match(Token.lpt.tag);
                bexpr();
                match(Token.rpt.tag);
                match(Tag.DO);
                stat();
                break;
            
            default:
                error("[whenitem]>> syntax error");
        }
    }

    private void bexpr(){

        switch(look.tag){

            case Tag.RELOP:   // B -> RELOP E E 
                if(look.tag == Word.eq.tag){
                    match(Word.eq.tag);
                    expr();
                    expr();
                    break;
                }else if(look.tag == Word.ne.tag){
                    match(Word.ne.tag);
                    expr();
                    expr();
                    break;
                }else if(look.tag == Word.le.tag){
                    match(Word.le.tag);
                    expr();
                    expr();
                    break;
                }else if(look.tag == Word.ge.tag){
                    match(Word.ge.tag);
                    expr();
                    expr();
                    break;
                }else if(look.tag == Word.lt.tag){
                    match(Word.lt.tag);
                    expr();
                    expr();
                    break;
                }else if(look.tag == Word.gt.tag){
                    match(Word.gt.tag);
                    expr();
                    expr();
                    break;
                }

            default:
                error("[bexpr]>> syntax error");
        }
    }

    private void expr(){
        
        switch(look.tag){

            case '+':    // E -> + ( EL )
                match(Token.plus.tag);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;
            
            case '-':   // E -> - E E
                match(Token.minus.tag);
                expr();
                expr();
                break;

            case '*':    // E -> * ( EL )
                match(Token.mult.tag);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;

            case '/':     // E -> / E E 
                match(Token.div.tag);
                expr();
                expr();
                break;

            case Tag.NUM:           // E -> NUM
                match(Tag.NUM);
                break;

            case Tag.ID:            // E -> ID
                match(Tag.ID);
                break;

            default:
                error("[expr]>> syntax error");
        }
    }

    private void exprlist(){

        switch(look.tag){

            case '+':    // EL -> E ELP
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            
            default:
                error("[exprlist]>> syntax error");
        }
    }

    private void exprlistp(){

        switch(look.tag){

            case '+':    // ELP -> E ELP
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            
            case ')':   // ELP -> EPS
                break;

            default:
                error("[exprlistp]>> syntax error");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "E3_2_test2.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser32 parser = new Parser32(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}