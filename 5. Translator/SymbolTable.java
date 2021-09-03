import java.util.*;

/**
 * 
 * Classe per gestire gli identificatori utilizzati nella traduzione
 * Implementazione della tabella dei simboli
 * 
 * @author Daniele Di Palma
 * 
 */

public class SymbolTable {

    Map <String, Integer> OffsetMap = new HashMap <String,Integer>();

    /**
     * 
     * Inserisce un nuovo identificatore nella tabella dei simboli.
     * In caso sia già presente viene lanciata un'eccezione.
     * 
     * @param s identificatore da aggiungere
     * @param address memory address del nuovo identificatore
    */
    
	public void insert(String s, int address){
            if(!OffsetMap.containsValue(address)) 
                OffsetMap.put(s,address);
            else 
                throw new IllegalArgumentException("Reference to a memory location already occupied by another variable");
	}

    /**
     * 
     * Restituisce l'indirizzo contenuto nell'identificatore in input
     * 
     * @param s input identificatore
     * @return memory address dell'identificatore
     */
	public int lookupAddress (String s){
            if(OffsetMap.containsKey(s)) 
                return OffsetMap.get(s);
            else
                return -1;
	}
}
