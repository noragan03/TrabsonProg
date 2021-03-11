package modelo;

public class ModeloJogo {
	private String nomeJog;
	private String tipoJog;
	private String jogadaJogadores;
	
	public ModeloJogo(String nomeJog, String tipoJog) {
		this.nomeJog = nomeJog;
		this.tipoJog = tipoJog;
	}
	
	public String getNomeJog() {
		return nomeJog;
	}
	public void setNomeJog(String nomeJog) {
		this.nomeJog = nomeJog;
	}
	public String getTipoJog() {
		return tipoJog;
	}
	public void setTipoJog(String tipoJog) {
		this.tipoJog = tipoJog;
	}
	
	public String getJogadaJogadores() {
		return jogadaJogadores;
	}
	
	public void setJogadaJogadores(String jogadaJogadores)
	{
		this.jogadaJogadores = jogadaJogadores;
	}

}
