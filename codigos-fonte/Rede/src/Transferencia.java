import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Classe realiza a transferencia do objeto para a aplicacao por TCP
 */
public class Transferencia {
	private Datagrama datagrama;		//Datagrama
	private Socket cliente;				//Socket Cliente
	
	public Transferencia (){			
		setDatagrama(null);
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
			cliente = new Socket(datagrama.getipDestino(), datagrama.getChecksum());
			ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
			saida.writeObject(datagrama);
			saida.close();
			cliente.close();
			System.out.println("Datagrama "+datagrama.getIdentificador()+" enviado com sucesso...");
		}catch(Exception erro){
			System.out.println("Falha no envio do datagrama "+datagrama.getIdentificador()+"...\nErro: "+erro);
		}
	}
	
}