import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.BitSet;

@SuppressWarnings("serial")

public class Datagrama implements Serializable{
	//Cabecalho: tamanho total = 20 bytes
	private byte [] ipOrigem, ipDestino;			    //Campos IP Origem e IP Destino
	private byte tipoServico, tempoDeVida, protocolo;   //Tipo de Servico, Tempo de Vida e Protocolo
	private char tamanho, identificador, checksum, offset;  //Tamanho Datagrama, ID, Checksum e OFFSET
	private BitSet flag, versao, tamCabecalho;    //Flag, versao e Tamanho do Cabecalho do datagrama
	//Dados:
	private byte[] dados;  	

	public Datagrama() {
		dados        = null;
		ipOrigem     = new byte[4];
		ipDestino    = new byte[4];
		flag         = new BitSet(1);		// 1 bit
		versao       = new BitSet(3);       // 3 bits
		tamCabecalho = new BitSet(4);       // 4 bits
		
		try{			//Tentando identificar o ip da maquina
			setIPOrigem(InetAddress.getLocalHost().getHostAddress());
		}catch(UnknownHostException e){    //Caso contrario, atribui ip loopback
			setIPOrigem("127.0.0.1");
		}
		setChecksum(6080);			//Define o checksum como 6080
		setIPDestino("127.0.0.1");  //Define o IP Destino como
		setTamanho(20);				//Define o Tamanho como 20 bytes
		setFlag(0);					//Define a flag 0
		setOffset(0);				//Define o offset como 0
		setTamCabecalho(20);		//Define o Tamnho do Cabecalho como 20 bytes
	}
	
	public void visualizarCabecalho(){      //Mostra algumas informacoes do cabecalho		
		System.out.println(" - IP origem: " + getipOrigem());
		System.out.println(" - IP destino: " + getipDestino());
		System.out.println(" - Porta destino: "+getChecksum());
		System.out.println(" - Flag: " + getFlag());
		System.out.println(" - Tamanho datagrama: " + getTamanho());
		System.out.println(" - Offset: " + getOffset());
	}
	
	public String getipDestino() {			//Retorna o ipDestino
		int [] ipConversor = new int [4];
		
		for (int i = 0; i < 4; i++){		//Convertendo os valores, pois oi
			ipConversor[i] = ipDestino[i] + 128;
		}
		return ipConversor[0]+"."+ipConversor[1]+"."+ipConversor[2]+"."+ipConversor[3];
	}
	
	public void setIPDestino(String ip){		//Seta o ip destino	
		String[] faixasIP = ip.split("\\.");    //Considerando IPv4.
		
		for (int i = 0; i < 4; i++){	                        //Convertendo para o "unsigned"
			ipDestino[i] = Byte.parseByte(Integer.toString( Integer.parseInt(faixasIP[i]) - 128 ));
		}
	}	

	public String getipOrigem() {			//Retorna o ipOrigem
		int [] ipConversor = new int [4];
		for (int i = 0; i < 4; i++){		//Convertendo o valor devido ao "unsigned"
			ipConversor[i] = ipOrigem[i] + 128;
		}
		return ipConversor[0]+"."+ipConversor[1]+"."+ipConversor[2]+"."+ipConversor[3];
	}
	
	public void setIPOrigem(String ip){				//Seta ip origem
		String[] faixasIP = ip.split("\\.");    //Considerando IPv4.
		for (int i = 0; i < 4; i++){	                        //Convertendo para o "unsigned"
			ipOrigem[i] = Byte.parseByte(Integer.toString( Integer.parseInt(faixasIP[i]) - 128 ));
		}
	}
	
	public void setDados(byte [] dados){			//Setando os dados no arquivo, array de bytes
		this.dados = dados;
	}

	public void setDados(File arquivo) throws IOException {      //Setando Dados do Arquivo, arquivo
		String todoArquivo  = "";
		@SuppressWarnings("resource")
		BufferedReader entrada = new BufferedReader(new FileReader(arquivo));
		while (entrada.ready()) {                     //Lendo o arquivo de upload.
			todoArquivo += entrada.readLine()+"\n";
		}
		dados = todoArquivo.getBytes();       //Convertendo string para bytes
		setTamanho(getTamanho()+dados.length);
	}
	
	public byte[] getDados() { //Retorna array de bytes (dados)
		return dados;
	}

	public int getIdentificador() {		//Retorna o identificadors
		return identificador;
	}

	public void setIdentificador(int identificador) {  //Seta o identificador
		this.identificador = (char) identificador;
	}

	public int getTipoServico() {   //Retorna o tipo de servico
		int aux = tipoServico;
		aux -= 128;
		return aux;
	}

	public void setTipoServico(int tipoServico) {   //Seta o tipo de servico
		this.tipoServico = (byte) (tipoServico -128);
	}

	public int getFlag() {				//Retorna a flag
		return ConversorBit.bitSetParaInt(flag);
	}

	public void setFlag(int flag) {     //Seta  a flag
		this.flag = ConversorBit.intparaBitSet(flag);
	}

	public int getProtocolo() {			//Retorna o protocolo
		int aux = protocolo;
		aux -= 128;
		return aux;
	}

	public void setProtocolo(int protocolo) {   //Seta o protocolo
		this.protocolo = (byte) (protocolo-128);
	}

	public int getTempoDeVida() {			//Retorna o tempo de vida
		int aux = tempoDeVida;
		aux -= 128;
		return aux;
	}

	public void setTempoDeVida(int tempoDeVida) {  //Seta o tempo de vida
		this.tempoDeVida = (byte) tempoDeVida;
	}

	public int getVersao() {			//Retorna a versao
		return ConversorBit.bitSetParaInt(versao);
	}

	public void setVersao(int versao) {   //Seta a aversao
		this.versao = ConversorBit.intparaBitSet(versao);
	}

	public int getTamCabecalho() {		//Retorna o tamanho do cabecalho do datagrama
		return ConversorBit.bitSetParaInt(tamCabecalho);
	}

	public void setTamCabecalho(int tamCabecalho) {   //Seta o tamanho do cabecalho do datagrama
		this.tamCabecalho = ConversorBit.intparaBitSet(tamCabecalho);
	}

	public int getTamanho() {			//Retorma o tamanho do datagrama
		return tamanho;
	}

	public void setTamanho(int tamanho) {		//Seta o tamanho do datagrama
		this.tamanho = (char) tamanho;
	}

	public int getChecksum() {			//Retorna o checksum
		return checksum;
	}

	public void setChecksum(int checksum) {		//Seta o checksum
		this.checksum = (char)checksum;
	}

	public int getOffset() {			//Retorna o Offset
		return offset;
	}

	public void setOffset(int offset) {  //Seta o offset
		this.offset = (char) offset;
	}
}
