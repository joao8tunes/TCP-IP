import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Principal {
	private static Scanner ler = new Scanner(System.in);
	
	private static void limparTela() throws IOException{		//Funcao limpar tela, so' funciona no linux
		System.out.print( "\033[H\033[2J" ); 
	}
	
	private static void informacoesAjuda(){		//Imprime menu ajuda
		System.out.println(" AJUDA:");
		System.out.println(" - ajuda/help                 - Exibir opcoes de ajuda;");
		System.out.println(" - abrir/open                 - Selecionar arquivo de upload;");
		System.out.println(" - enviar/send                - Enviar datagrama para a Rede;");
		System.out.println(" - informacao/information     - Visualizar as informacoes de conexao;");
		System.out.println(" - iprede/netip               - Definir o IP da Rede;");
		System.out.println(" - portarede/netport          - Definir a porta da Rede;");
		System.out.println(" - ipservidor/serverip        - Definir o IP do Servidor;");
		System.out.println(" - portaservidor/serverport   - Definir a porta do Servidor;");
		System.out.println(" - limpar/clear               - Limpar tela de log;");
		System.out.println(" - sair/exit                  - Finalizar programa.");
	}
	
	public static void main(String[] args) throws Exception {
		Datagrama datagrama = null;
		Transferencia transf = new Transferencia();
		String entradaTeclado;                      // Entrada do teclado
		int identificador = 0;					//Contador de identificadores 
		String ip;
		
		try{			//Tentando identificar o ip da maquina
			ip = InetAddress.getLocalHost().getHostAddress();
		}catch(UnknownHostException e){    //Caso contrario, atribui ip loopback
			ip = "127.0.0.1";
		}

		limparTela();
		System.out.println("                   SIMULADOR DE DATAGRAMA IP - Cliente");
		System.out.println(" _____________________________________________________________________________\n");
		
		while (true) {
			System.out.printf(" > ");
			
			entradaTeclado = ler.nextLine();
			if (entradaTeclado.equalsIgnoreCase("abrir") || entradaTeclado.equalsIgnoreCase("open")) { // Abrir
				JFileChooser janelaAbrir = new JFileChooser();
				if (janelaAbrir.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File arquivo = new File(janelaAbrir.getSelectedFile().getAbsolutePath());
					
					if (!arquivo.exists()) {
						JOptionPane.showMessageDialog(null, "O arquivo selecionado não existe.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					}
					else if (URLConnection.guessContentTypeFromName(arquivo.getName()) == null || !URLConnection.guessContentTypeFromName(arquivo.getName()).equals("text/plain")) {
						JOptionPane.showMessageDialog(null, "  O arquivo selecionado\nnão é do tipo texto (.txt).", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						
					}
					else {
						datagrama = new Datagrama();
						datagrama.setIPOrigem(InetAddress.getLocalHost().getHostAddress().toString());
						datagrama.setDados(arquivo);
						datagrama.setIdentificador(identificador);
						transf.setDatagrama(datagrama);
						identificador++;
					}
				}
			} 
			else if (entradaTeclado.equalsIgnoreCase("ajuda") || entradaTeclado.equalsIgnoreCase("help")) { // Ajuda
				informacoesAjuda();
			}
			else if (entradaTeclado.equalsIgnoreCase("enviar") || entradaTeclado.equalsIgnoreCase("send")){ //Enviar
				if (datagrama != null){
					System.out.println("\n-----------------------------------------------------------");
					System.out.println("\nCabecalho do datagrama:");
					datagrama.visualizarCabecalho();
					System.out.println("");
					transf.enviarPacote();
					System.out.println("-----------------------------------------------------------");
				}
			}
			else if(entradaTeclado.equalsIgnoreCase("iprede") || entradaTeclado.equalsIgnoreCase("netip")){  //Seta o ip da rede (aplicacao rede)
				String ipRede;
				ipRede = JOptionPane.showInputDialog(null, "Definir o IP da Rede:     ");
				if (ipRede != null){
					String[] aux = ipRede.split("\\.");
					boolean correct = true;
					
					if (aux.length != 4) JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					else {
						for (int j = 0; j < aux.length; j++) {    //Procurando inconsistências no IP.
							try {
								Integer.valueOf(aux[j]);
							}
							catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								correct = false;
								break;
							}
						}
						if (correct) transf.setIp(ipRede);
					}
				}
			}
			else if(entradaTeclado.equalsIgnoreCase("portarede") || entradaTeclado.equalsIgnoreCase("netport")){  //Porta da rede
				String porta;
				porta = JOptionPane.showInputDialog(null, "Definir a porta da Rede:     ");
				if (porta != null){
					boolean correct = true;
					
					try {
						Integer.valueOf(porta);
					}
					catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						correct = false;
						break;
					}
					if (correct) transf.setPorta(porta);
				}
			}		
			else if (entradaTeclado.equalsIgnoreCase("informacao") || entradaTeclado.equalsIgnoreCase("information")){
				if (datagrama != null){			//Mostra informacoes do cabecalho
					System.out.println(" INFORMACOES");
					System.out.println(" - IP: "+ip);
					System.out.println(" - IP da Rede: "+transf.getIp());
					System.out.println(" - Porta da Rede: "+transf.getPorta());
					System.out.println(" - IP do Servidor: "+datagrama.getipDestino());
					System.out.println(" - Porta do Servidor: "+datagrama.getChecksum());
				}
			}
			else if (entradaTeclado.equalsIgnoreCase("sair") || entradaTeclado.equalsIgnoreCase("exit")) { //Sair 
				limparTela();
				System.exit(0);
			}
			else if(entradaTeclado.equalsIgnoreCase("ipservidor") || entradaTeclado.equalsIgnoreCase("serverip")){  //Seta o ip da rede (aplicacao rede)
				String ipServidor;
				ipServidor = JOptionPane.showInputDialog(null, "Definir o IP do Servidor:     ");
				if (datagrama != null && ipServidor != null){
					String[] aux = ipServidor.split("\\.");
					boolean correct = true;
					
					if (aux.length != 4) JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					else {
						for (int j = 0; j < aux.length; j++) {    //Procurando inconsistências no IP.
							try {
								Integer.valueOf(aux[j]);
							}
							catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
								correct = false;
								break;
							}
						}
						if (correct) datagrama.setIPDestino(ipServidor);
					}
				}
			}
			else if(entradaTeclado.equalsIgnoreCase("portaservidor") || entradaTeclado.equalsIgnoreCase("serverport")){  //Porta da rede
				String porta;
				porta = JOptionPane.showInputDialog(null, "Definir a porta do Servidor:     ");
				if (datagrama != null && porta != null){
					boolean correct = true;
					
					try {
						Integer.valueOf(porta);
					}
					catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Dado incorreto.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						correct = false;
						break;
					}
					if (correct) datagrama.setChecksum(Integer.valueOf(porta));
				}
			}
			else if (entradaTeclado.equalsIgnoreCase("limpar") || entradaTeclado.equalsIgnoreCase("clear")){ //limpar tela
				limparTela();
				System.out.println("                     SIMULADOR DE DATAGRAMA IP - Cliente");
				System.out.println(" _____________________________________________________________________________\n");
			}
			else System.out.println("'"+entradaTeclado+"' nao e um comando valido! Digite 'ajuda' ou 'help' para obter ajuda.");
		}
	}
}