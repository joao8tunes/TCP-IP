import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Classe realiza a transferencia do objeto para a aplicacao por TCP
 */
public class Transferencia {
	private Datagrama datagrama;		//Datagrama
	private String ip;					//Ip do socket
	private int porta;					//Porta do Socket
	private Socket cliente;				//Socket Cliente
	
	public Transferencia (){			
		setDatagrama(null);
		setIp("127.0.0.1");		//Setando ip como loopback
		setPorta("6060");		//Setando porta como 6060
	}
	
	public Transferencia(Datagrama datagrama){		
		setDatagrama(datagrama);
	}

	public Datagrama getDatagrama() {		//Retorna o datagrama
		return datagrama;
	}

	public void setDatagrama(Datagrama datagrama) {   //Seta o datagrama
		this.datagrama = datagrama;
	}
	
	public void enviarPacote() throws Exception{		//Enviar o datagrama pelo socket cliente
		try{
			cliente = new Socket(ip, porta);
			ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
			saida.writeObject(datagrama);
			saida.close();
			cliente.close();
			System.out.println("Objeto enviado com sucesso...");
		}catch(Exception erro){
			System.out.println("Falha no envio do objeto...\nErro: "+erro);
		}
	}

	public int getPorta() {			//Retorna a porta da conexao  cliente
		return porta;
	}

	public void setPorta(String porta) {  //Seta a porta da conexao cliente
		this.porta = Integer.parseInt(porta);
	}
	
	public void setPorta(int porta) {		//Seta a porta da conexao cliente
		this.porta = porta;
	}

	public String getIp() {			//Retorna Ip da conexao cliente
		return ip;
	}

	public void setIp(String ip) {		//Seta IP
		this.ip = ip;
	}
}