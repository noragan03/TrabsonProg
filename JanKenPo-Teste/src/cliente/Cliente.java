package cliente;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controle.ControleJogo;

public class Cliente extends JFrame {

	private JPanel contentPane;
	private JTextField txtPorta;
	private JTextField txtIP;
	private JTextField txtNome;
	private JPanel painelJanela;
	private JButton btnConfirmar;
	private Socket socket;
	private OutputStream saida;
	private OutputStreamWriter decoSaida;
	private BufferedWriter leitor;
	private JTextField texto;
	private JButton btnSair;
	private JLabel Imagem1;
	private JLabel Imagem2;
	private JLabel txtJogador1;
	private JLabel txtJogador2;
	private JRadioButton rdbtnPedra;
	private JRadioButton rdbtnPapel;
	private JRadioButton rdbtnTesoura;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	
	private String txt[];
	
	public static void main(String[] args) throws IOException {

		Cliente tela = new Cliente();
		tela.conectar();
		tela.msgServer();
	}

	/*
	 * Conecta o cliente ao servidor
	 */
	public void conectar() throws IOException {

		socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
		/**
		 * faz parte do outputStream
		 */
		saida = socket.getOutputStream();
		/**
		 * outputStreamWriter
		 */
		decoSaida = new OutputStreamWriter(saida);
		/**
		 * bufferedWriter
		 */
		leitor = new BufferedWriter(decoSaida);
		leitor.write(txtNome.getText() + "\r\n");
		leitor.flush();
	}

	public void enviarMensagem(String msg) throws IOException {
		if (msg.equals("Sair")) {
			leitor.write("Sair"+"\r\n");
		} else {
			leitor.write(txtNome.getText()+":"+msg + "\r\n");

			// TODO Teste enviarMensagem()
			// Essa parte envia a mensagem para a propria tela

			texto.setText("Você jogou"+ ":" + msg + "\r\n");

		}
		leitor.flush();
	}

	/**
	 * Mensagem que vem do servidor
	 */
	public void msgServer() throws IOException {

		InputStream entrada = socket.getInputStream();
		InputStreamReader decoEntrada = new InputStreamReader(entrada);
		BufferedReader leitorEn = new BufferedReader(decoEntrada);
		String msg = "";

		while (!"Sair".equalsIgnoreCase(msg))

			if (leitorEn.ready()) {
				msg = leitorEn.readLine();

				txt= ControleJogo.getInstance().separa(msg);
				
				if (msg.equalsIgnoreCase("Sair")) {
					texto.setText("Oponente Saiu.\r\n");
				} else {
					//Se o vetor tiver todos os jogadores
					if(txt.length>1)
					{
						tipoJogada(txt);
					}else {
						ganhador(txt);
						habilitBotao(true);
					}
					
				}
			}
	}

	public void ganhador(String[] txt) {
		if(txtNome.getText().equals(txt[0])) {
			texto.setText("Você ganhou!");
		}else if(txt[0].equalsIgnoreCase("Empate")) {
			texto.setText("EMPATE!");
		}else {
			texto.setText("Você perdeu!");
		}
		
	}

	public void sair() {

		try {

			enviarMensagem("Sair");
			leitor.close();
			decoSaida.close();
			saida.close();
			socket.close();
			dispose();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param texto = Jogada que o jogador fez
	 * @param nome
	 *
	 */

	// TODO PASSAR PARA O CONTROLE
	//tipoJogada é responsavel por mudar imagem dos jogadores
	  public void tipoJogada(String[] txt) {
		  int mao=1;
		  
		  for(int posicaoNome=0;posicaoNome<=2;posicaoNome=posicaoNome+2) {
			  if(txt[mao].equalsIgnoreCase("PEDRA")){ 
				  jogadorQueJogou(txt[posicaoNome]).setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Pedra.jpg")));
		  
			  }else if(txt[mao].equalsIgnoreCase("PAPEL")) {
		  
				  jogadorQueJogou(txt[posicaoNome]).setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Papel.jpg")));
		  
			  }else if(txt[mao].equalsIgnoreCase("TESOURA")) {
			
				  jogadorQueJogou(txt[posicaoNome]).setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Tesoura.jpg"))); 
			} 
			  mao=mao+2;
		  	}
		  }
	 

	/**
	 * Identifica quem jogou, retornando onde deve ser trocada a imagem
	 * 
	 * @param nome
	 * @return
	 */
	public JLabel jogadorQueJogou(String nome) {

		if (txtNome.getText().equals(nome))
			return Imagem1;
		else
			return Imagem2;
	}
	
	/**
	 * Informa qual foi a jogada do jogador
	 * principal e envia para Mensagem
	 */
	public void infoJog() {
		try {
			if (rdbtnPapel.isSelected()) {
				enviarMensagem("PAPEL");
			} else if (rdbtnPedra.isSelected()) {
				enviarMensagem("PEDRA");
			} else if (rdbtnTesoura.isSelected()) {
				enviarMensagem("TESOURA");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		habilitBotao(false);
	}

	public void habilitBotao(Boolean bool) {
		btnConfirmar.setEnabled(bool);
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

	// Essa eh a tela do jogo
	public Cliente() {
		setResizable(false);
		{
			// Conexao do cliente no servidor
			janelaConexao();

			// Janela do cliente
			painelJanela = new JPanel();
			btnConfirmar = new JButton("Confirmar");

			// Acao do botao confirmar
			btnConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					infoJog();
				}

			});

			btnConfirmar.setBounds(168, 254, 92, 23);
			btnConfirmar.setToolTipText("Confirmar escolha");
			btnSair = new JButton("Sair");
			btnSair.setBounds(306, 254, 66, 23);
			btnSair.setToolTipText("Sair do Jogo");

			// Acao do botao sair
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

			// Etiqueta onde vai a imagem da mao jogada
			Imagem1 = new JLabel("");
			Imagem1.setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Pedra.jpg")));
			Imagem1.setBounds(10, 11, 139, 120);
			painelJanela.add(Imagem1);

			JPanel painelDoPlacar = new JPanel();
			painelDoPlacar.setBounds(147, 11, 242, 120);
			painelJanela.add(painelDoPlacar);

			// Etiqueta com informacoes de quem ganhou a partida ou se deu empate
			texto = new JTextField("JAN KEN PON!");
			texto.setEditable(false);
			texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
			painelDoPlacar.add(texto, "cell 1 2,alignx left,aligny top");

			// Etiqueta responsavel por mostrar a imagem da mao escolhida pelo adversario
			Imagem2 = new JLabel();
			Imagem2.setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Pedra.jpg")));
			Imagem2.setBounds(388, 11, 139, 120);
			painelJanela.add(Imagem2);

			// *****************************************
			txtJogador1 = new JLabel("Jogador1: ");
			txtJogador1.setBounds(52, 142, 97, 14);
			painelJanela.add(txtJogador1);

			txtJogador2 = new JLabel("Jogador2: ");
			txtJogador2.setBounds(429, 142, 77, 14);
			painelJanela.add(txtJogador2);
			// *****************************************

			JLabel lblPalavraMeio = new JLabel("VS");
			lblPalavraMeio.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblPalavraMeio.setBounds(260, 140, 19, 14);
			painelJanela.add(lblPalavraMeio);

			JPanel painelBotoes = new JPanel();
			painelBotoes.setBounds(10, 181, 517, 110);
			painelJanela.add(painelBotoes);

			// botao pedra
			rdbtnPedra = new JRadioButton("PEDRA");
			buttonGroup.add(rdbtnPedra);
			rdbtnPedra.setSelected(true);

			painelBotoes.add(rdbtnPedra);

			// botao papel
			rdbtnPapel = new JRadioButton("PAPEL");
			buttonGroup.add(rdbtnPapel);
			painelBotoes.add(rdbtnPapel);

			// botao tesoura
			rdbtnTesoura = new JRadioButton("TESOURA");
			buttonGroup.add(rdbtnTesoura);
			painelBotoes.add(rdbtnTesoura);

			// Encerramento da janela
			setLocationRelativeTo(null);
			setSize(553, 341);

			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}
}
