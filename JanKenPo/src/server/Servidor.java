
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import modeloJogo.ModeloJogo;

public class Servidor extends Thread {
	
	private static ArrayList<Servidor> clientes;
	private static ServerSocket server;
	public int numJogador;
	private String nome;
	private ModeloJogo model = new ModeloJogo();
	
	//Construtor do Servidor
	public Servidor() {
		System.out.println("----Servidor JanKenPo-----");
		
		//model possui atributos: quantidade de jogadores, e placar1 e 2
		numJogador = model.getQtdCli();
	}
	
	//Aqui aceita a conexao dos jogadores
	public void aceitarConexoes() {
		System.out.println("Aguardando conexões...");
		
		try {
				while(model.getQtdCli() < 2) {
				Socket con = server.accept();
				model.setQtdCli(++numJogador);
				System.out.println("Jogador "+numJogador+"# Se conectou");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		
		//TODO contagem dos jogadores
		//Eu nao sei como impedir o acesso de mais um jogador
		//Tentei pelo cliente com uma condicao mas ele nao passa de 1 haha
		System.out.println("Sala cheia, não pode haver mais de 2 jogadores.");
		}
	
	//Aqui eh a tela do servidor, onde ira criar uma sala onde o jogo acontece
	public void telaServer() {
		try {
			JLabel lblMessage = new JLabel("Porta do Servidor:");
			JTextField txtPorta = new JTextField("12345");
			Object[] texts = { lblMessage, txtPorta };
			JOptionPane.showMessageDialog(null, texts);
			server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
			JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public int valor() {
		return numJogador;
	}
	//Aqui eh onde a magica acontece
	public static void main(String[] args) {
	
			Servidor jogo = new Servidor();
			jogo.telaServer();
			jogo.aceitarConexoes();

	}

}
