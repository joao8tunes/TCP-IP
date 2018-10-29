import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Principal {
	private static Scanner ler;
	private static ServerSocket socketServidor;
	private static Desfragmentacao desfragmentador;
	
	private static void limparTela() throws IOException { //Limpar tela linux
		System.out.print("\033[H\033[2J");
	}

	private static void informacoesAjuda() {
		System.out.println(" AJUDA:");
		System.out.println(" - ajuda/help                 - Exibir opcoes de ajuda;");
		System.out.println(" - informacao/information     - Visualizar as informacoes de conexao;");
		System.out.println(" - porta/port                 - Definir a porta;");
		System.out.println(" - conectar/connect           - Iniciar conexao;");
		System.out.println(" - limpar/clear               - Limpar tela de log;");
		System.out.println(" - sair/exit                  - Finalizar programa.");
	}

	public static void main(String[] args) throws Exception {		
		ler = new Scanner(System.in);
		String entradaTeclado; // Entrada do teclado
		int porta = 6080;
		String ip;
		
		try{			//Tentando identificar o ip da maquina
			ip = InetAddress.getLocalHost().getHostAddress();
		}catch(UnknownHostException e){    //Caso contrario, atribui ip loopback
			ip = "127.0.0.1";
		}
		
		limparTela();
		System.out.println("                  SIMULADOR DE DATAGRAMA IP - Servidor");
		System.out.println(" _____________________________________________________________________________\n");
		
		while (true) {
			System.out.printf(" > ");
			
			entradaTeclado = ler.nextLine();

			if (entradaTeclado.equalsIgnoreCase("ajuda") || entradaTeclado.equalsIgnoreCase("help")) { // Ajuda
				informacoesAjuda();
			} else if (entradaTeclado.equalsIgnoreCase("conexao") || entradaTeclado.equalsIgnoreCase("connect")) {
				socketServidor = new ServerSocket(porta);
				desfragmentador = new Desfragmentacao();
				Datagrama datagramaRestaurado;			
			    
				if (socketServidor.isBound()) {
					System.out.println("Conexao iniciada... (Ctrl+C para encerrar)");
				
					while(true){
						Socket socketConexao = socketServidor.accept();
	
						ObjectInputStream entrada = new ObjectInputStream(socketConexao.getInputStream());    //Criando link de conexão com o objeto transferido.
						Datagrama datagrama = (Datagrama) entrada.readObject();    //Resgatando o objeto transferido através do link criado.
						entrada.close();    //Desconectando o link.
						socketConexao.close();    //Encerrando a conexão de recebimento de objetos.
						System.out.println("\n-------------------------------------------------------------");
						System.out.println("Datagrama "+datagrama.getIdentificador()+" recebido com sucesso...");
						datagrama.visualizarCabecalho();
						System.out.println("-------------------------------------------------------------");
						
						
						if (!desfragmentador.desfragmentar(datagrama)) {
							datagramaRestaurado = desfragmentador.getDatagrama();
							System.out.println("\n=============================================================");
							System.out.println("Objeto recebido com sucesso...");
							System.out.println("\nCabecalho do datagrama completo:");
							datagramaRestaurado.visualizarCabecalho();
							System.out.println("\nDados:");
							System.out.println(new String(datagramaRestaurado.getDados(), "UTF-8"));
							System.out.println("=============================================================");
							desfragmentador.resetData();
							JFileChooser path = new JFileChooser();
							path.setDialogTitle("Salvar arquivo");
							if (path.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
								File file = path.getSelectedFile();
								FileOutputStream in = new FileOutputStream(file);
								
								in.write(datagramaRestaurado.getDados());
								in.close();
							}
						}
					}
				}
				else System.out.println("Impossivel conectar!");
			} else if (entradaTeclado.equalsIgnoreCase("sair") || entradaTeclado.equalsIgnoreCase("exit")) {
				limparTela();
				System.exit(0);
			}
			else if(entradaTeclado.equalsIgnoreCase("porta") || entradaTeclado.equalsIgnoreCase("port")){
				String novaPorta;
				novaPorta = JOptionPane.showInputDialog(null, "Definir essa porta:     ");
				if (novaPorta != null){
					boolean correct = true;
					
					try {
						Integer.valueOf(novaPorta);
					}
					catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						correct = false;
					}
					if (correct) porta = Integer.valueOf(novaPorta);
				}
			}
			else if(entradaTeclado.equalsIgnoreCase("limpar") || entradaTeclado.equalsIgnoreCase("clear")){
				limparTela();
				System.out.println("                     SIMULADOR DE DATAGRAMA IP - Servidor");
				System.out.println(" _____________________________________________________________________________\n");
			}
			else if (entradaTeclado.equalsIgnoreCase("informacao") || entradaTeclado.equalsIgnoreCase("information")){
				System.out.println(" INFORMACOES:");
				System.out.println(" - IP: "+ip);
				System.out.println(" - Porta: "+porta);
			}
			else System.out.println("'"+entradaTeclado+"' nao e um comando valido! Digite 'ajuda' ou 'help' para obter ajuda.");
		}
	}
	
}
