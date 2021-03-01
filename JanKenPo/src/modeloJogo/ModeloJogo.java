package modeloJogo;

public class ModeloJogo {
	private int ponto1;
	private int ponto2;
	private int qtdCli;
	
	public void ControleJogo() {
		qtdCli=0;
		ponto1=0;
		ponto2=0;
	}

	public int getPonto1() {
		return ponto1;
	}

	public void setPonto1(int ponto1) {
		this.ponto1 = ponto1;
	}

	public int getPonto2() {
		return ponto2;
	}

	public void setPonto2(int ponto2) {
		this.ponto2 = ponto2;
	}

	public int getQtdCli() {
		return qtdCli;
	}

	public void setQtdCli(int qtdCli) {
		this.qtdCli = qtdCli;
	}
}
