package controle;

import java.util.ArrayList;

import modelo.ModeloJogo;

public class ControleJogo {
	
	private static ControleJogo controle;
	
	private ArrayList<String> listaJogada;

	private static int quantJog;

	private ControleJogo() {
		this.listaJogada = new ArrayList<>();
		this.quantJog=0;
	}
	
	public static ControleJogo getInstance() {
		if(controle == null) {
			controle = new ControleJogo();
		}
		
		return controle;
	}
	
	public static int quant() {
		quantJog++;
		return quantJog;
	}

	
	 public void insereJog(ModeloJogo modelJog) {
	 listaJogada.add(modelJog.getNomeJog());
	 listaJogada.add(modelJog.getTipoJog());
		 
	 }
	 
	public void limpaLista() {
		listaJogada.removeAll(listaJogada);
	}
	

	public String concatenaJogada(String txtJogada) {
		for(String lista : listaJogada) {
			txtJogada+=txtJogada.equals("")? lista:":"+lista;
		}
		
		return txtJogada;
	}
	
	public String[] separa(String txtJogada) {
		return txtJogada.split(":");
	}
	public String quemGanhou(String txtJogada){
		String[] jogadaJogada = separa(txtJogada);
		
		if(jogadaJogada[1].equalsIgnoreCase("PEDRA")&&jogadaJogada[3].equalsIgnoreCase("PAPEL")) {
			return jogadaJogada[2];
		}else if(jogadaJogada[1].equalsIgnoreCase("PEDRA")&&jogadaJogada[3].equalsIgnoreCase("TESOURA")) {
			return jogadaJogada[0];
		}else if(jogadaJogada[1].equalsIgnoreCase("PAPEL")&&jogadaJogada[3].equalsIgnoreCase("PEDRA")) {
			return jogadaJogada[0];
		}else if(jogadaJogada[1].equalsIgnoreCase("PAPEL")&&jogadaJogada[3].equalsIgnoreCase("TESOURA")) {
			return jogadaJogada[2];
		}else if(jogadaJogada[1].equalsIgnoreCase("TESOURA")&&jogadaJogada[3].equalsIgnoreCase("PAPEL")) {
			return jogadaJogada[0];
		}else if(jogadaJogada[1].equalsIgnoreCase("TESOURA")&&jogadaJogada[3].equalsIgnoreCase("PEDRA")) {
			return jogadaJogada[2];
		}
		return "EMPATE";
	}
	
	public static int getQuantJog() {
		return quantJog;
	}

	public static void setQuantJog(int quantJog) {
		ControleJogo.quantJog = quantJog;
	}
}
