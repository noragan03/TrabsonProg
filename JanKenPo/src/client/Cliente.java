package client;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import modeloJogo.ModeloJogo;
import server.Servidor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Cliente extends JFrame {
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;
	private JPanel painelJanela;
	private JButton btnConfirmar;
	private JButton btnSair;
	
	private Container buttonGroup;
	private JLabel lbltextoInforPlacar;
	private JLabel lblImagem1;
	private JLabel lblGanhou;
	private JRadioButton rdbtnPapel;
	private JRadioButton rdbtnTesoura;
	private JRadioButton rdbtnPedra;
	
	//O placar eh a pontuacao dos jogadores
	private int placar1;
	private int placar2;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private Socket socket;
	private OutputStream ou;
	private OutputStreamWriter ouw;
	private BufferedWriter bfw;
	
	public static void main(String[] args) throws IOException {
		
		//TODO condicao para impedir que mais de 2 jogadores tentei entrar
		//Tentei fazer uma condicao aqui para impedir de abrir e conectar
		//Depois de dois jogadores entrarem no servidor
			
		Cliente jogo = new Cliente();
			jogo.conectar();
	}
	
	/***
	 * Método usado para conectar no server socket, retorna IO Exception caso dê
	 * algum erro.
	 * 
	 * @throws IOException
	 */
	public void conectar() throws IOException {
		socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);

//		Foi parte de uma condicao com if que eu tinha tentado->	
//		JOptionPane.showMessageDialog(null, "O Servidor já está cheio!");			
//		

	}
	
	/***
	 * Método usado quando o usuário clica em sair
	 * 
	 * @throws IOException
	 *             retorna IO Exception caso dê algum erro.
	 */
	public void sair() throws IOException {

		this.dispose();
		socket.close();
		ouw.close();
		ou.close();
	}
	
	//Essa eh a tela do jogo
	public Cliente() {
		{
		JLabel lblMessage = new JLabel("Verificar!");
		txtIP = new JTextField("127.0.0.1");
		txtPorta = new JTextField("12345");
		txtNome = new JTextField("Cliente");
		Object[] texts = { lblMessage, txtIP, txtPorta, txtNome };
		JOptionPane.showMessageDialog(null, texts);
		
		painelJanela = new JPanel();
		btnConfirmar = new JButton("Confirmar");
		
		//Acao do botao confirmar
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO ConfirmarJogada
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
				try {
					sair();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		painelJanela.setLayout(null);
		painelJanela.add(btnSair);
		painelJanela.add(btnConfirmar);
		painelJanela.setBackground(SystemColor.inactiveCaptionBorder);
		setTitle(txtNome.getText());
		setContentPane(painelJanela);
		
		//Etiqueta onde vai a imagem da mao jogada
		lblImagem1 = new JLabel("");
		lblImagem1.setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Pedra.jpg")));
		lblImagem1.setBounds(10, 11, 139, 120);
		painelJanela.add(lblImagem1);
		
		JPanel painelDoPlacar = new JPanel();
		painelDoPlacar.setBounds(144, 11, 152, 120);
		painelJanela.add(painelDoPlacar);
		
		//Etiqueta com informacoes de quem ganhou a partida ou se deu empate
		lblGanhou = new JLabel("JAN KEN PON!");
		lblGanhou.setFont(new Font("Tahoma", Font.PLAIN, 15));
		painelDoPlacar.add(lblGanhou, "cell 1 2,alignx left,aligny top");
		
		//Etiqueta responsavel por mostrar a imagem da mao escolhida pelo adversario
		JLabel lblImagem2 = new JLabel("IMAGEM2");
		lblImagem2.setIcon(new ImageIcon(Cliente.class.getResource("/imagem/Pedra.jpg")));
		lblImagem2.setBounds(297, 11, 139, 120);
		painelJanela.add(lblImagem2);
		
		//Etiqueta onde mostra o placar do jogador 1
		lbltextoInforPlacar = new JLabel("Jogador1: "+placar1);
		lbltextoInforPlacar.setBounds(10, 142, 97, 14);
		painelJanela.add(lbltextoInforPlacar);
		
		//Etiqueta onde mostra o placar do jogador 2
		//jogador2: + placar2 <- pontos do jogador
		JLabel lblPlacar2 = new JLabel("Jogador2: "+placar2);
		lblPlacar2.setBounds(306, 142, 97, 14);
		painelJanela.add(lblPlacar2);
		
		JLabel lblPalavraMeio = new JLabel("VS");
		lblPalavraMeio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPalavraMeio.setBounds(212, 140, 19, 14);
		painelJanela.add(lblPalavraMeio);
		
		JPanel painelBotoes = new JPanel();
		painelBotoes.setBounds(10, 195, 416, 96);
		painelJanela.add(painelBotoes);
		
		//botao pedra
		rdbtnPedra = new JRadioButton("PEDRA");
		buttonGroup_1.add(rdbtnPedra);
		rdbtnPedra.setSelected(true);

		painelBotoes.add(rdbtnPedra);
		
		//botao papel
		rdbtnPapel = new JRadioButton("PAPEL");
		buttonGroup_1.add(rdbtnPapel);
		painelBotoes.add(rdbtnPapel);
		
		//botao tesoura
		rdbtnTesoura = new JRadioButton("TESOURA");
		buttonGroup_1.add(rdbtnTesoura);
		painelBotoes.add(rdbtnTesoura);
		
		//Encerramento da janela
		setLocationRelativeTo(null);
		setSize(452, 341);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	
}
