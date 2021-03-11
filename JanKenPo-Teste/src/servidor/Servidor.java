package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controle.ControleJogo;
import modelo.ModeloJogo;

public class Servidor extends Thread {
	private static ArrayList<Servidor> clientesServ;
	private static ServerSocket servidorSocket;
	private String nome;
	private Socket conexao;
	private InputStream inputStream; //Valor de entrada
	private InputStreamReader inputReader; //Leitura do valor
	private BufferedReader bufferReader;
	private BufferedWriter bufferWriter;
	private ControleJogo controlJog = ControleJogo.getInstance();
	
	private int teste=0;
	
	public Servidor(Socket conexao) {
		this.conexao = conexao;
		
		try {
			inputStream = conexao.getInputStream();
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendToAll(String mensagem) {
		Servidor bwS = null;
		Servidor bwP = null;
		
		for(Servidor serv: clientesServ) {
			bwS =  serv;
			try{
				sendMsg(mensagem, serv);
			}catch(Exception e){
				bwP=bwS;
			}	
		}
		if(bwP != null){
			clientesServ.remove(bwP);
		}
	}

	private void sendMsg(String mensagem, Servidor serv) throws IOException{
		serv.bufferWriter.write(mensagem + "\r\n"); //TODO verificar como funciona
		serv.bufferWriter.flush();
	}

	public void run() {
		
		try {
			String mensagem = null;
			OutputStream outputStream = this.conexao.getOutputStream();
			Writer outputWriter = new OutputStreamWriter(outputStream);
			bufferWriter = new BufferedWriter(outputWriter);
			this.nome = mensagem = bufferReader.readLine();
			mensagem=bufferReader.readLine();
			
			while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null) {
				
				
				ModeloJogo modelJog = tratamentoMsg(mensagem);
				if(!mensagem.equalsIgnoreCase("Sair"))
				{
					controlJog.insereJog(modelJog);
					
					if(controlJog.quant()==2) {
						String txtJogada = "";
						
						txtJogada = controlJog.concatenaJogada(txtJogada);
						sendToAll(txtJogada);
						txtJogada = controlJog.quemGanhou(txtJogada);
						sendToAll(txtJogada);
						
						controlJog.setQuantJog(0);
						controlJog.limpaLista();
					}
				}else {
					sendToAll(mensagem);
				}
					mensagem=null;
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ModeloJogo tratamentoMsg(String msg) {
		String[] listaJogada = msg.split(":");
		ModeloJogo modelJog = null;
		
		if(listaJogada.length>0)
		{
			modelJog = new ModeloJogo(listaJogada[0],listaJogada[1]);
		}
		return modelJog;
	}
	
	private static Integer telaPorta() {
		JLabel lblMensagem = new JLabel("Porta do Servidor:");
		JTextField txtPorta = new JTextField("12345");
		Object[] conexao = {lblMensagem, txtPorta};
		JOptionPane.showMessageDialog(null, conexao);
		
		return Integer.parseInt(txtPorta.getText());
	}
	
public static void main(String[] args) {
		
		try {
			Integer porta = telaPorta();
			servidorSocket = new ServerSocket(porta);
			clientesServ = new ArrayList<Servidor>();

			while(true) {
				System.out.println("Aguardando conexão...");
				Socket con = servidorSocket.accept();// bloqueia o programa e aguarda conexao
				System.out.println("Jogador conectado...");
				Servidor t = new Servidor(con);
				t.clientesServ.add(t); //Adiciona jogador
				t.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
