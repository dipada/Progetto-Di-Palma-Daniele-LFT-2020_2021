/**
 * Classe per i gestire le costanti per i Token che corrispondono a
 * un solo carattere (tranne < e >, che corrispondono a relop), si puo'
 * utilizzare il codice ASCII del carattere stesso. Ad esempio "+" con codice
 * ASCII 43
 * 
 * @author Daniele Di Palma
 */

public class Tag {
	public final static int EOF = -1, 
		NUM = 256, 
		ID = 257, 
		RELOP = 258, 
		COND = 259, 
		WHEN = 260, 
		THEN = 261, 
		ELSE = 262,
		WHILE = 263, 
		DO = 264, 
		SEQ = 265, 
		PRINT = 266, 
		READ = 267, 
		OR = 268, 
		AND = 269;
}
