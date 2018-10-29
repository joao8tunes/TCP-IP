import java.util.BitSet;

public class ConversorBit {		//Realiza a conversao de inteiros para bits e de bits para inteiros
	
	public static int bitSetParaInt(BitSet valorbits){   //Converte bitset para inteiro
	    int valorint = 0;
	    
	    for(int i = 0 ; i < 32; i++)
	        if(valorbits.get(i))
	            valorint |= (1 << i);
	    return valorint;
	}
	
	public static BitSet intparaBitSet(int valorint){	//Converte inteiro para bitset
	    char[] bits = Integer.toBinaryString(valorint).toCharArray();  
	    BitSet valorbits = new BitSet(bits.length);  
	    
	    for(int i = 0; i < bits.length; i++){  
	        if(bits[i] == '1'){
	            valorbits.set(i, true);
	        }
	        else{
	            valorbits.set(i, false);
	        }                
	    }
	    return valorbits;
	}  
}