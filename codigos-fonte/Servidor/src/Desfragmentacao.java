import java.util.ArrayList;


public class Desfragmentacao 
{

	private Datagrama datagrama;    //Datagrama de restauração.
	private ArrayList<byte[]> dados = new ArrayList<byte[]>();    //Dados de restauração do datagrama original.
	private int tamanho = 0;
	
	public Desfragmentacao()
	{
	}
	
	public boolean desfragmentar(Datagrama datagrama)
	{
		dados.add(datagrama.getDados());    //Retirando a porção de dados do pacote.
		tamanho += datagrama.getDados().length;    //Calculando o tamanho do datagrama de restauração.
		
		if (datagrama.getFlag() == 0) {    //Último pacote detectado.
			this.datagrama = new Datagrama();
			this.datagrama.setIPOrigem(datagrama.getipOrigem());
			this.datagrama.setIPDestino(datagrama.getipDestino());
			this.datagrama.setTamanho(tamanho+20);    //Adicionando o tamanho do cabeçalho.
			
			int pos = 0, i;
			byte[] dadosRestaurados = new byte[tamanho];    //Necessário para restaurar a área de dados do datagrama original.
			
			for (byte[] b : dados) {    //Percorrendo todos os fragmentos de dados.
				for (i = 0; i < b.length; i++) {    //Percorrendo todos os bytes de cada fragmento.
					dadosRestaurados[pos+i] = b[i];    //Restaurando os dados originais.
				}
				pos += i;
			}
			
			this.datagrama.setDados(dadosRestaurados);
			return(false);    //Último pacote recebido: fim da restauração.
		}
		
		return(true);    //Faltam pacotes a serem restaurados.
	}
	
	public Datagrama getDatagrama()
	{
		return(datagrama);
	}

	public void resetData()
	{
		dados = new ArrayList<byte[]>();
		tamanho = 0;
	}
	
}