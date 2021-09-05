/**
 * Esercitazione 4.1 LFT 2020/2021 @author Daniele Di Palma
 * 
 * Classe implementante un Valutatore di espressioni semplici
 * Utilizza il Lexer23 ed estende il Parser31
 *
 * Utilizza le azioni semantiche descritte dalla SDT 
 * associata alla grammatica
 */

import java.io.*; 

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
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
	    throw new Error("near line " + Lexer.line + ": " + s);
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
	    
        // GUIDA( S -> E) = { '(' , 'NUM' }
        // S -> E { print(E.val) }
        
        int expr_val;   // attributo sintetizzato
                        // memorizzato in una variabile locale    	

        switch(look.tag){ 
            case '(':
            case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println("\nRisultato: " + expr_val);
                break;
            default:
                error("[start]>> syntax error");
        }
    }

    private int expr() { 

        // GUIDA( E -> TE') = { '(' , 'NUM' }
        // E -> T { E'.i = T.val } E' { E.val = E'.val }

        int term_val, exprp_val;

        switch(look.tag){
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                return exprp_val;
                //return exprp(term());  anche così

            default:
                error("[expr]>> syntax error");
                return -1;
        }
    }

    private int exprp(int exprp_i) {
	    
        int term_val, exprp_val;
	    
        switch (look.tag) {
            // GUIDA(E' -> +TE') = { '+' }
            // E' -> + T { E_1'.i = E'.i + T.val } E_1' { E'.val = E_1'.val }
	        case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                return exprp_val;
            
            // GUIDA(E' -> -TE') = { '-' }
            // E' -> - T { E_1'.i = E'.i - T.val } E_1' { E'.val = E_1'.val }
            case '-':   // E' -> -TE'
                match(Token.minus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                return exprp_val;

            // GUIDA(E' -> EPS) = { 'EOF', ')' }
            // E' -> { E'.val = E'.i }
            case Tag.EOF:   
            case ')':
                return exprp_i;
            
            default:
                error("[exprp]>> syntax error");    	
                return -1;
	    }
    }

    private int term() { 
        
        // GUIDA(T -> FT') = { '(' , NUM }
        // T -> F { T'.i = F.val } T' { T.val = T'.val }
        
        int fact_val, term_val;

        switch(look.tag){
            
            
            case '(':   // T -> FT'
            case Tag.NUM:
                fact_val = fact();
                term_val = termp(fact_val);
                return term_val;
            
            default:
                error("[term]>> syntax error");
                return -1;
        }
    }
    
    private int termp(int termp_i) { 

        int fact_val, termp_val;

        switch(look.tag){
            
            // GUIDA(T' -> *FT') = { '*' }
            // T' -> * F { T_1'. = T'.i * F.val } T_1' { T'.val = T_1'.val }
            case '*':   
                match(Token.mult.tag);
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                return termp_val;
            
            // GUIDA(T' -> /FT') = { '/' }
            // T' -> / F { T_1'.i = T'.i / F.val } T_1' { T'.val = T_1'.val }
            case '/':   // T' -> /FT'
                match(Token.div.tag);
                fact_val = fact();
                termp_val = termp(termp_i/fact_val);
                return termp_val;
            

            // GUIDA(T' -> EPS) = { 'EOF' , '+' , '-' }
            // T' -> { T'.val = T'.i}
            case Tag.EOF:   
            case '+':
            case '-':
            case ')':
                return termp_i;

            default:
                error("[termp]>> syntax error");
                return -1;
        }
	
    }
    
    private int fact() { 
        
        int expr_val;
        
        switch(look.tag){

            // GUIDA( F -> (E) ) = { '(' }
            // F -> ( E ) { F.val = E.val }
            case '(': 
                match(Token.lpt.tag);
                expr_val = expr();
                match(Token.rpt.tag);
                return expr_val;
            
            // GUIDA( F -> NUM ) = { 'NUM' }
            // F -> NUM { F.val = NUM.value }
            case Tag.NUM: 
                match(Tag.NUM);
                return NumberTok.value;
                
            
            default:
                error("[fact]>> syntax error");
                return -1;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "E4_1_test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
