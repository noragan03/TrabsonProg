package cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;

public class ClienteJogo extends JFrame {

	private JPanel contentPane;
	private JTextField txtPorta;
	private JTextField txtIP;
	private JTextField txtNome;
	private JPanel painelJanela;
	private JButton btnConfirmar;
	private JButton btnSair;
	private JLabel txtJogador1;
	private JRadioButton rdbtnPedra;
	private JRadioButton rdbtnPapel;
	private JRadioButton rdbtnTesoura;
	private JLabel txtJogador2;
	
	//------------------------Variaveis Uteis
	private JLabel Imagem1;
	private JLabel Imagem2;
	private JTextField texto;
	private int vezJog =0;
	//------------------------Jogada
	private String jogada;
	//------------------------Leitor
	private Socket socket;
	private OutputStream saida;
	private OutputStreamWriter decoSaida;
	private BufferedWriter leitor;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public static void main(String []args) throws IOException{

		   ClienteJogo tela = new ClienteJogo();
		   tela.conectar();
		   tela.msgServer();
	}
	
	/*
	 * Conecta o cliente ao servidor
	 */
	public void conectar() throws IOException{
		  
		  socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
		  saida = socket.getOutputStream();
		  decoSaida = new OutputStreamWriter(saida);
		  leitor = new BufferedWriter(decoSaida);
		  leitor.write(txtNome.getText()+"\r\n");
		  leitor.flush();
	}
	
	public void enviarMensagem(String msg) throws IOException{

	    if(msg.equals("Sair")){
	      leitor.write("Jogador Desconectou");
	    }else{
	    	leitor.write(msg+"\r\n");
	    	
	    	//TODO Teste enviarMensagem()
	    	//Essa parte envia a mensagem para a propria tela
	    	
	        texto.setText( txtNome.getText() + ">" + msg+"\r\n");
	      
	    }
	      leitor.flush();
	}
	
	public void msgServer() throws IOException{

		   InputStream entrada = socket.getInputStream();
		   InputStreamReader decoEntrada = new InputStreamReader(entrada);
		   BufferedReader leitorEn = new BufferedReader(decoEntrada);
		   String msg = "";

		    while(!"Sair".equalsIgnoreCase(msg))

		       if(leitorEn.ready()){
		         msg = leitorEn.readLine();
		       if(msg.equals("Sair")) {
		    	   texto.setText("Servidor caiu! \r\n");
		    	   
		       }else {
		    	   texto.setText(msg+"\r\n");
		    	   
		    	   //TODO msgServer() 
		    	   //troca da imagem jogador2 nao funciona
		    	   
		    	   //jogada2(texto);
		       }
		       		
		        }
		}

	public void sair() {
		
		try {
			enviarMensagem("Sair");
			
			leitor.close();
			decoSaida.close();
			saida.close();
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void jogada2(JTextField texto) {
//		String[] txt = texto.getText().split(":");
//		
//		if(txt[1].equalsIgnoreCase("PEDRA")){
//			System.out.println("METODO JOGADA2: ");
//			   Imagem2.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Pedra.jpg")));
//		   }else if(txt[1].equalsIgnoreCase("PAPEL")) {
//			   Imagem2.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Papel.jpg")));
//		   }else if(txt[1].equalsIgnoreCase("TESOURA")) {
//			   Imagem2.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Tesoura.jpg")));
//		   }
//	}
//		
	
	/**
	 * Faz um texto da jogada ser enviada
	 */
	private void jogada() {
		String msgJog = null;
		
		if(rdbtnPapel.isSelected()) {
			Imagem1.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Papel.jpg")));
			msgJog = "PAPEL";
		
		}else if(rdbtnPedra.isSelected()) {
			Imagem1.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Pedra.jpg")));
			msgJog = "PEDRA";
		
		}else if(rdbtnTesoura.isSelected()) {
			Imagem1.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Tesoura.jpg")));
			msgJog = "TESOURA";
		}//Fim IF
		
		try {
			enviarMensagem(msgJog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * A tela de conexao para inserir os dados
	 */
	public void janelaConexao() {
			JLabel lblMessage = new JLabel("Verificar!");
			txtIP = new JTextField("127.0.0.1");
			txtPorta = new JTextField("12345");
			txtNome = new JTextField("Cliente");
			Object[] texts = { lblMessage, txtIP, txtPorta, txtNome };
			JOptionPane.showMessageDialog(null, texts);
		}
	
		//Essa eh a tela do jogo
		public ClienteJogo() {
			{
			//Conexao do cliente no servidor
			janelaConexao();
			
			//Janela do cliente
			painelJanela = new JPanel();
			btnConfirmar = new JButton("Confirmar");
			
			//Acao do botao confirmar
			btnConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jogada();
				}

			});
			
			btnConfirmar.setBounds(128, 254, 92, 23);
			btnConfirmar.setToolTipText("Confirmar escolha");
			btnSair = new JButton("Sair");
			btnSair.setBounds(230, 254, 66, 23);
			btnSair.setToolTipText("Sair do Jogo");
			
			//Acao do botao sair
			btnSair.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sair();
				}
			});
			
			painelJanela.setLayout(null);
			painelJanela.add(btnSair);
			painelJanela.add(btnConfirmar);
			painelJanela.setBackground(SystemColor.inactiveCaptionBorder);
			setTitle(txtNome.getText());
			setContentPane(painelJanela);
			
			//Etiqueta onde vai a imagem da mao jogada
			Imagem1 = new JLabel("");
			Imagem1.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Pedra.jpg")));
			Imagem1.setBounds(10, 11, 139, 120);
			painelJanela.add(Imagem1);
			
			JPanel painelDoPlacar = new JPanel();
			painelDoPlacar.setBounds(144, 11, 152, 120);
			painelJanela.add(painelDoPlacar);
			
			//Etiqueta com informacoes de quem ganhou a partida ou se deu empate
			texto = new JTextField("JAN KEN PON!");
			texto.setEditable(false);
			texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
			painelDoPlacar.add(texto, "cell 1 2,alignx left,aligny top");
			
			//Etiqueta responsavel por mostrar a imagem da mao escolhida pelo adversario
			Imagem2 = new JLabel();
			Imagem2.setIcon(new ImageIcon(ClienteJogo.class.getResource("/imagem/Pedra.jpg")));
			Imagem2.setBounds(297, 11, 139, 120);
			painelJanela.add(Imagem2);
			
			//*****************************************
			txtJogador1 = new JLabel("Jogador1: ");
			txtJogador1.setBounds(10, 142, 97, 14);
			painelJanela.add(txtJogador1);
			
			txtJogador2 = new JLabel("Jogador2: ");
			txtJogador2.setBounds(306, 142, 97, 14);
			painelJanela.add(txtJogador2);
			//*****************************************
			
			JLabel lblPalavraMeio = new JLabel("VS");
			lblPalavraMeio.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblPalavraMeio.setBounds(212, 140, 19, 14);
			painelJanela.add(lblPalavraMeio);
			
			JPanel painelBotoes = new JPanel();
			painelBotoes.setBounds(10, 195, 416, 96);
			painelJanela.add(painelBotoes);
			
			//botao pedra
			rdbtnPedra = new JRadioButton("PEDRA");
			buttonGroup.add(rdbtnPedra);
			rdbtnPedra.setSelected(true);

			painelBotoes.add(rdbtnPedra);
			
			//botao papel
			rdbtnPapel = new JRadioButton("PAPEL");
			buttonGroup.add(rdbtnPapel);
			painelBotoes.add(rdbtnPapel);
			
			//botao tesoura
			rdbtnTesoura = new JRadioButton("TESOURA");
			buttonGroup.add(rdbtnTesoura);
			painelBotoes.add(rdbtnTesoura);
			
			//Encerramento da janela
			setLocationRelativeTo(null);
			setSize(452, 341);
			
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		}

}
