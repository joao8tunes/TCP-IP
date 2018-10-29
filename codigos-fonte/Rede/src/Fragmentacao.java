

public class Fragmentacao 
{

	private int mtu = 21, offset;
	
	public Fragmentacao()
	{
	}
	
	public void fragmentar(Datagrama datagrama) throws Exception
	{
		Transferencia transferencia = new Transferencia();    //Necessário para enviar os datagramas fragmentados.
		int areaDados = mtu-20, posIniDatagrama, numFragmento = 0;
		Datagrama novoDatagrama;    //Necessário para enviar os dados fragmentados.
		
		byte[] dados = datagrama.getDados();    //Resgatando a área de dados do datagrama.
		
		/* Fragmentando o datagrama original: */
		for (posIniDatagrama = 0; posIniDatagrama < dados.length; posIniDatagrama += areaDados) {
			/* Dividindo os dados do datagrama em porções e agrupando em novos datagramas: */
			novoDatagrama = new Datagrama();
			novoDatagrama.setIPOrigem(datagrama.getipOrigem());
			novoDatagrama.setIPDestino(datagrama.getipDestino());
			novoDatagrama.setChecksum(datagrama.getChecksum());
			novoDatagrama.setTamanho(mtu);
			offset = areaDados/8;					//Calculo do Offset
			novoDatagrama.setOffset(posIniDatagrama*offset);
			novoDatagrama.setIdentificador(numFragmento);
			
			//Identificando o último datagrama a ser enviado:
			if (posIniDatagrama+areaDados >= dados.length) novoDatagrama.setFlag(0);
			else novoDatagrama.setFlag(1);
			
			//FAZENDO O TRATAMENTO PARA O CASO DA ÁREA DE DADOS DO NOVO PACOTE EXCEDER O Q AINDA RESTA DE DADOS NÃO ENVIADO DO DATAGRAMA ORIGINAL.
			//SE NÃO TRATAR, SÃO ESCRITOS BYTES NULOS NO ARQUIVO FINAL, NO SERVIDOR, POR CAUSA DO ÚLTIMO PACOTE ENVIADO.
			if (posIniDatagrama+areaDados > dados.length) areaDados = dados.length-posIniDatagrama;
			byte[] parcelaDados = new byte[areaDados];
			
			for (int pos = 0; pos < areaDados && posIniDatagrama+pos < dados.length; pos++) {    //Preenchendo a área de dados do novo datagrama com uma parcela da área de dados do datagrama original.
				parcelaDados[pos] = dados[posIniDatagrama+pos];
			}
			
			novoDatagrama.setDados(parcelaDados);
			
			/* Enviando um novo fragmento do datagrama original: */
			transferencia.setDatagrama(novoDatagrama);
			transferencia.enviarPacote();
			++numFragmento;
		}
 	}

	public int getMTU() 		//Retorna o mtu
	{
		return mtu;
	}

	public void setMTU(int mtu)		//Seta o MTU
	{
		this.mtu = mtu;
	}
	
}