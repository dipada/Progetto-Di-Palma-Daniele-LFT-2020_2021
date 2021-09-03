import java.util.LinkedList;
import java.io.*;

/**
 * Classe per memorizzare in una struttra apposita la lista delle istruzioni
 * (le istruzioni sono oggetti di tipo Instruction) generate durante la parsificazione.
 * header e footer, costanti in questa classe, sono il preambolo per il codice generato dal traduttore
 * per restituire tramite il metodo toJasmine() un file Output.j la cui struttura è compatibile con il programma Jasmin.
 * 
 * @author Daniele Di Palma
 */

public class CodeGenerator {

    LinkedList <Instruction> instructions = new LinkedList <Instruction>();

    int label=0;

    /**
     * Aggiunge l'istruzione alla lista delle istruzioni
     * 
     * @param opCode tipo dell'istruzione da aggiungere
     */
    public void emit(OpCode opCode) {
        instructions.add(new Instruction(opCode));
    }

    public void emit(OpCode opCode , int operand) {
        instructions.add(new Instruction(opCode, operand));
    }

    public void emitLabel(int operand) {
        emit(OpCode.label, operand);
    }

    /**
     * Genera delle label nel file .j 
     * (L0, L1, etc.)
     */
    public int newLabel() {
        return label++;
    }

    /**
     * Genera il file Jasmin con estensione .j
     * La struttura del file .j è la seguente:
     *      - header
     *      - istruzioni
     *      - footer
     * 
     */
    public void toJasmin() throws IOException{
        PrintWriter out = new PrintWriter(new FileWriter("Out/Output.j"));
        String temp = "";
        temp = temp + header;
        while(instructions.size() > 0){
            Instruction tmp = instructions.remove();
            temp = temp + tmp.toJasmin();
        }
        temp = temp + footer;
        out.println(temp);
        out.flush();
        out.close();
    }

    /**
     * Header del file Jasmin
     */
    private static final String header = ".class public Output \n"
        + ".super java/lang/Object\n"
        + "\n"
        + ".method public <init>()V\n"
        + " aload_0\n"
        + " invokenonvirtual java/lang/Object/<init>()V\n"
        + " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static print(I)V\n"
        + " .limit stack 2\n"
        + " getstatic java/lang/System/out Ljava/io/PrintStream;\n"
        + " iload_0 \n"
        + " invokestatic java/lang/Integer/toString(I)Ljava/lang/String;\n"
        + " invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V\n"
        + " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static read()I\n"        
        + " .limit stack 3\n"        
        + " new java/util/Scanner\n"        
        + " dup\n"        
        + " getstatic java/lang/System/in Ljava/io/InputStream;\n"        
        + " invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n"        
        + " invokevirtual java/util/Scanner/next()Ljava/lang/String;\n"        
        + " invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I\n"        
        + " ireturn\n"        
        + ".end method\n"
        + "\n"
        + ".method public static run()V\n"
        + " .limit stack 1024\n"
        + " .limit locals 256\n";

    /**
     * Footer del file Jasmin
    */
    private static final String footer = " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static main([Ljava/lang/String;)V\n"
        + " invokestatic Output/run()V\n"
        + " return\n"
        + ".end method\n";
}
