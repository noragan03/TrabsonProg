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

public class ServidorJogo extends Thread{
	
	//Armazena o cliente
	private static ServerSocket servidor;
	private static Socket conexao;
	
	//texto de cada cliente
	private InputStream entrada;
	private InputStreamReader decoEntrada;
	private BufferedReader leitor;

	//Os jogadores
	private static ArrayList<BufferedWriter> clientes;
	private String nome;
	
	public ServidorJogo(Socket con){
		   this.conexao = con;
		   
		   try {
		         entrada  = con.getInputStream();
		         decoEntrada = new InputStreamReader(entrada);
		          leitor = new BufferedReader(decoEntrada);
		   } catch (IOException e) {
		          e.printStackTrace();
		   }
		}
	
	public void run(){

		  try{

		    String msg;
		    OutputStream saida =  this.conexao.getOutputStream();
		    
		    //Escreve os dados da saida em bytes
		    Writer escreveSaida = new OutputStreamWriter(saida);
		    
		    //Pega os dados da Saida
		    BufferedWriter dadoSaida = new BufferedWriter(escreveSaida);
		    clientes.add(dadoSaida);
		    
		    nome = msg = leitor.readLine();
		    
		    System.out.println("Aqui é o metodo Run: "+msg);
		    
		    //TODO RUN() envia mensagem
		    while(!"Sair".equalsIgnoreCase(msg) && msg != null)
		      {
		       //Insere a mensagem novamente para dentro da variavel
		       msg = leitor.readLine();
		       
		       //Envia a mensagem para todos
		       sendToAll(dadoSaida, msg);
		       
		       System.out.println("Dados da mensagem enviada");
		       System.out.println(msg);
		       }

		   }catch (Exception e) {
		     e.printStackTrace();
		   }
		}

	private void sendToAll(BufferedWriter dadoSaida, String msg) throws IOException {
		  BufferedWriter dadoS;

		  for(BufferedWriter bw : clientes){
		  
			  dadoS = (BufferedWriter)bw;
		   
			  if(!(dadoSaida == dadoS)){
				  //Aqui envia a jogada de imediato para os servidores
				  bw.write(nome+":"+msg+"\r\n");
				  bw.flush();
			  }//Fim IF
		 
		  }//Fim For
	}//Fim Metodo
		
	public static void main(String[] args) {

			  try{
			    //Cria os objetos necessário para instânciar o servidor
			    JLabel lblMessage = new JLabel("Porta do Servidor:");
			    JTextField txtPorta = new JTextField("12345");
			    Object[] texts = {lblMessage, txtPorta };
			    JOptionPane.showMessageDialog(null, texts);
			    servidor = new ServerSocket(Integer.parseInt(txtPorta.getText()));
			    clientes = new ArrayList<BufferedWriter>();
			    System.out.println("Servidor ativo na porta: "+
			    txtPorta.getText());
			    
			     while(true){
			       System.out.println("Aguardando conexão...");
			       Socket con = servidor.accept();
//			       numJogador++;
			       System.out.println("Cliente conectado.");
			       Thread t = new ServidorJogo(con);
			        t.start();
			    }
			    

			  }catch (Exception e) {

			    e.printStackTrace();
			  }
	}
	}
