import java.io.*;

/**
 * Esercitazione 5.1 LFT 2020/2021
 * Classe implementante un traduttore del linguaggio descritto nella grammatica 3.2
 * Il traduttore traduce in linguaggio assembler Jasmin.
 * 
 * @author Daniele Di Palma
 *
 */

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
    public void prog() {
        
        switch(look.tag){   
            // GUIDA(P -> SL) = { '=' , 'print' , 'read' , 'cond' , 'while' , '{' }

            case '=':  
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                    System.out.println("IO error\n");
                };
                break;
            default:
                error("[prog]>> syntax error");
        }
    }

    private void statlist(int l_next){

        switch(look.tag){   
            // GUIDA( SL -> S SLP ) = { '=' , 'print' , 'read' , 'cond' , 'while' , '{' }

            case '=':  
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                int lnext_s = code.newLabel();
                stat(lnext_s);
                code.emitLabel(lnext_s);
                statlistp();
                break;

            default:
                error("[statlist]>> syntax error");
        }
    }

    private void statlistp(){
        
        switch(look.tag){

            case ';':               // GUIDA( SLP -> ; S SLP ) = { ; }
                int lnext_s = code.newLabel();   
                match(Token.semicolon.tag);
                stat(lnext_s);
                code.emitLabel(lnext_s);
                statlistp();
                break;
            
            case Tag.EOF:           // GUIDA( SLP -> EPS )  = { 'EOF' , '}' }
            case '}':
                break;

            default:
                error("[statlistp]>> syntax error");
        }
    }

    public void stat(int l_next) {
        
        switch(look.tag) {
            
            case '=':               // GUIDA( S -> = ID E ) = { = }
                match(Token.assign.tag);
                if(look.tag == Tag.ID){
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if(id_addr == -1){
                        id_addr = count;
                        st.insert(((Word)look).lexeme, count++);
                    }
                    match(Tag.ID);
                    expr(0, false);
                    code.emit(OpCode.istore, id_addr);
                }else{
                    error("Error in grammar (stat) after = with " + look);
                }
                break;

            case Tag.PRINT:         // GUIDA( S -> print(EL) ) = { print }
                match(Tag.PRINT);
                match(Token.lpt.tag);
                exprlist(0, true);
                match(Token.rpt.tag);
               
                break;
            
            case Tag.READ:          // GUIDA( S -> read(ID) ) = { read }
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,id_addr); 
                }else
                    error("Error in grammar (stat) after read( with " + look);
                break;
            
            case Tag.COND:          // GUIDA( S -> cond WL else S ) = { cond }
                match(Tag.COND);
                int l_false = code.newLabel();

                whenlist(l_next, l_false);
                match(Tag.ELSE);
                code.emitLabel(l_false);
                stat(l_next);
                
                break;         
            
            case Tag.WHILE:         // GUIDA(S -> while ( B ) S ) = { while }
                match(Tag.WHILE);
                match(Token.lpt.tag);
                int l_loop = code.newLabel();
                int l_e = code.newLabel();

                code.emitLabel(l_loop);
                bexpr(l_e, l_next);

                match(Token.rpt.tag);
                code.emitLabel(l_e);
                stat(l_loop);
                
                code.emit(OpCode.GOto, l_loop);
                break;

            case '{':               // GUIDA( S -> {SL} ) = { '{' }
                match(Token.lpg.tag);
                statlist(l_next);
                match(Token.rpg.tag);
                break;
        }
    }

    private void whenlist(int l_next, int l_false){
        switch(look.tag){

            case Tag.WHEN:          // GUIDA(WL -> WI WLP) = { when }
                whenitem(l_next, l_false);
                whenlistp(l_next, l_false);
                break;
            
            default:
                error("[whenlist]>> syntax error");        
        }
    }

    private void whenlistp(int l_next, int l_false){

        switch(look.tag){

            case Tag.WHEN:          // GUIDA( WLP -> WI WLP ) = { when }
                whenitem(l_next, l_false);
                whenlistp(l_next, l_false);
                break;

            case Tag.ELSE:          // GUIDA( WLP -> EPS ) = { else }
                break;

            default:
                error("[whenlistp]>> syntax error");
        }
    }

    private void whenitem(int l_next, int l_false){
        
        switch(look.tag){

            case Tag.WHEN:          // GUIDA( WI -> when ( B ) do S ) = { when }
                match(Tag.WHEN);
                match(Token.lpt.tag);
                int l_Btrue = code.newLabel();
                int l_Bfalse = code.newLabel();
                
                bexpr(l_Btrue, l_Bfalse);
                
                match(Token.rpt.tag);
                match(Tag.DO);
                code.emitLabel(l_Btrue);
                stat(l_next);
                
                code.emit(OpCode.GOto, l_next);
                code.emitLabel(l_Bfalse);                
                break;
            
            default:
                error("[whenitem]>> syntax error");
        }
    }

    private void bexpr(int l_Btrue, int l_Bfalse){
        switch(look.tag){

            case Tag.RELOP:         // GUIDA( B -> RELOP E E ) = { RELOP }
                if(((Word)look).lexeme == "=="){
                    match(Word.eq.tag);
                    expr(0, false);    
                    expr(0, false);    
                    code.emit(OpCode.if_icmpeq, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }else if(((Word)look).lexeme == "<>"){
                    match(Word.ne.tag);
                    expr(0, false);    
                    expr(0, false);    
                    code.emit(OpCode.if_icmpne, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }else if(((Word)look).lexeme == "<="){
                    match(Word.le.tag);
                    expr(0, false);    
                    expr(0, false);    
                    code.emit(OpCode.if_icmple, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }else if(((Word)look).lexeme == ">="){
                    match(Word.ge.tag);
                    expr(0, false);    
                    expr(0, false);    
                    code.emit(OpCode.if_icmpge, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }else if(((Word)look).lexeme == "<"){
                    match(Word.lt.tag);
                    expr(0, false);    
                    expr(0, false);      
                    code.emit(OpCode.if_icmplt, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }else if(((Word)look).lexeme == ">"){
                    match(Word.gt.tag);
                    expr(0, false);    
                    expr(0, false);    
                    code.emit(OpCode.if_icmpgt, l_Btrue);
			        code.emit(OpCode.GOto, l_Bfalse);
                    break;
                }

            default:
                error("[bexpr]>> syntax error");
        }
    }

    private int expr(int n, boolean printList) {
        switch(look.tag) {
            
            case '+':  {             // GUIDA( E -> + ( EL ) ) = { + }
                match(Token.plus.tag);
                match(Token.lpt.tag);                
                int temp = n;
                int nt = 1;
                n = exprlist(nt, false);                 
                match(Token.rpt.tag);

                for(int i = 0; i < n - 2 ; i++)
                    code.emit(OpCode.iadd);

                if(printList)
                    code.emit(OpCode.invokestatic, 1);
                       
                n = temp + 1;
                break;
        }
            case '-':               // GUIDA( E -> - E E) = { - }
                match('-');
                n = expr(n, false); 
                n = expr(n, false); 
                n -= 1;
                code.emit(OpCode.isub);
                
                if(printList)
                    code.emit(OpCode.invokestatic, 1);

                break;
            
            case '*':{               // GUIDA( E -> * ( EL ) ) = { * }
                match(Token.mult.tag);
                match(Token.lpt.tag);

                int temp = n;
                int nt = 1;
                n = exprlist(nt, false);                 
                match(Token.rpt.tag);
                
                for(int i = 0; i < n - 2  ; i++)
                    code.emit(OpCode.imul);
                
                if(printList)
                    code.emit(OpCode.invokestatic, 1);

                n = temp + 1 ;
                break;
            }

            case '/':               // GUIDA( E -> / E E ) = { / }
                match(Token.div.tag);
                n = expr(n, false); 
                n = expr(n, false); 
                n -= 1;
                code.emit(OpCode.idiv);

                if(printList)
                    code.emit(OpCode.invokestatic, 1);
                break;

            case Tag.NUM:           // GUIDA( E -> NUM )  = { NUM }
                code.emit(OpCode.ldc, ((NumberTok)look).value);
                match(Tag.NUM);
                if(printList)
                    code.emit(OpCode.invokestatic, 1);
                ++n;
                break;

            case Tag.ID:            // GUIDA( E -> ID ) = { ID }
                if(look.tag == Tag.ID){
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr == -1) {
                        error("identifier " + look + " not exist ");
                    }else{
                        match(Tag.ID);
                        code.emit(OpCode.iload, id_addr);
                        if(printList)
                            code.emit(OpCode.invokestatic, 1);
                        ++n;
                    }
                }
                break;

            default:
                error("[expr]>> syntax error");
        }
        return n;
    }

    private int exprlist(int n, boolean printList){

        switch(look.tag){

            case '+':       // GUIDA( EL -> E ELP ) = { '+' , '-' , '*' , '/' , 'NUM' , 'ID' }
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                n = expr(n, printList);                 
                n = exprlistp(n, printList); 
                break;
            
            default:
                error("[exprlist]>> syntax error");
        }
        return n;
    }

    private int exprlistp(int n, boolean printList){

        switch(look.tag){

            case '+':       // GUIDA( ELP -> E ELP ) = { '+' , '-' , '*' , '/' , 'NUM' , 'ID' }
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                n = expr(n, printList);
                n = exprlistp(n, printList);
                break;
            
            case ')':       // GUIDA( ELP -> EPS ) = { ')' }
                break;

            default:
                error("[exprlistp]>> syntax error");
        }
        return n;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "E5_1_test.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

