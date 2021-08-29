/**
 * Esercitazione 3.1 LFT 2020/2021 @author Daniele Di Palma
 * 
 * Classe implementante un Parser che parsifica espressioni aritmetiche semplici scritte in notazione infissa
 * Utilizza il Lexer23
 *
 */

import java.io.*;

public class Parser31 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser31(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Metodo per la lettura del token successivo
     * da sinistra verso destra
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
    public void start() {
        
        switch(look.tag){   // S -> E

            case '(':
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;
            
            default:
                error("[start]>> syntax error");
        }
    }

    private void expr() {
	
        switch(look.tag){
            case '(':   // E -> TE'
            case Tag.NUM:
                term();
                exprp();
                break;
            
            default:
                error("[expr]>> syntax error");
        }
    }

    private void exprp() {

	    switch (look.tag) {

	        case '+':   // E' -> +TE'
                match(Token.plus.tag);
                term();
                exprp();
                break;
            
            case '-':   // E' -> -TE'
                match(Token.minus.tag);
                term();
                exprp();
                break;

            case Tag.EOF:   // E' -> eps
            case ')':
                break;
            
            default:
                error("[exprp]>> syntax error");
	    }
    }

    private void term() {
        switch(look.tag){
            
            case '(':   // T -> FT'
            case Tag.NUM:
                fact();
                termp();
                break;
            
            default:
                error("[term]>> syntax error");
        }
    }

    private void termp() {

        switch(look.tag){
            
            case '*':   // T' -> FT'
                match(Token.mult.tag);
                fact();
                termp();
                break;
            
            case '/':   // T' -> /FT'
                match(Token.div.tag);
                fact();
                termp();
                break;
            
            case Tag.EOF:   // T' -> eps
            case '+':
            case '-':
            case ')':
                break;

            default:
                error("[termp]>> syntax error");
        }
    }

    private void fact() {

        switch(look.tag){

            case '(': // F -> (E)
                match(Token.lpt.tag);
                expr();
                match(Token.rpt.tag);
                break;
            
            case Tag.NUM: // F -> NUM
                match(Tag.NUM);
                break;
            
            default:
                error("[fact]>> syntax error");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "E3_1_test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser31 parser = new Parser31(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}