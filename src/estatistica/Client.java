package estatistica;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import mysql.Dao;

// manda resultados para o pacote mysql em Dao
public class Client {

	private static Client c1;
	private String cruzamento;
	private double mediana;
	private double somadosElementos;
	private double mediaAritmetica;
	private double somadosElementosaoQuadrado;
	private double variancia;
	private double desvioPadrao;
	private double pearson;
	private String moda;
	private double coeficientedeAssimetria;

	public Client calculo(ArrayList<Integer> quantidadeDeOcorrencias, String nomeArquivo,String moda) {
		Estatistica e = new Estatistica();

		// -------------------------------------------------------------------------//
		// a e b recebem valores da Classe Selecionador, exemplo: //
		// a = int variavel1 b = array1(contains(variavel2)) //
		// a = quantidade de mulheres b = array de mulheres(contains solteiros(as)) //
		// -------------------------------------------------------------------------/
		double[] array = new double[quantidadeDeOcorrencias.size()];
		
		// seta valores do array
		for (int i = 0; i < quantidadeDeOcorrencias.size(); i++) {
          array[i] = Integer.parseInt(quantidadeDeOcorrencias.get(i)+"");
		}

		// para teste com 700000 mil valores,
		// array = new double[700000];
		// Arrays.fill(array, 999);

		e.setArray(array);

		Client novoCliente = new Client();

		// cruzamento recebe nomes das duas variaveis
		novoCliente.cruzamento = nomeArquivo;
		novoCliente.mediana = e.getMediana();
		novoCliente.somadosElementos = e.getSomaDosElementos();
		novoCliente.mediaAritmetica = e.getMediaAritmetica();
		novoCliente.somadosElementosaoQuadrado = e.getSomaDosElementosAoQuadrado();
		novoCliente.variancia = e.getVariancia();
		novoCliente.desvioPadrao = e.getDesvioPadrao();
		novoCliente.pearson = e.getPearson();
		novoCliente.coeficientedeAssimetria = e.getCoefAssimetria();

		c1 = new Client();
		c1.setCruzamento(novoCliente.getCruzamento());
		c1.setMediana(novoCliente.getMediana());
		c1.setSomadosElementos(novoCliente.getSomadosElementos());
		c1.setMediaAritmetica(novoCliente.getMediaAritmetica());
		c1.setSomadosElementosaoQuadrado(novoCliente.getSomadosElementosaoQuadrado());
		c1.setVariancia(novoCliente.getVariancia());
		c1.setDesvioPadrao(novoCliente.getDesvioPadrao());
		c1.setPearson(novoCliente.getPearson());
		c1.setCoeficientedeAssimetria(novoCliente.getCoeficientedeAssimetria());
		c1.setModa(moda);
		//Dao.inserir(c1);

		/*JOptionPane.showMessageDialog(null,
				"Resultados Estatisticos \n" + "\nCruzamento: " + c1.getCruzamento() + "\nMediana: " + c1.getMediana()
						+ "\nSoma dos Elementos: " + c1.getSomadosElementos() + "\nMédia Aritmética: "
						+ c1.getMediaAritmetica() + "\nSoma dos Elementos ao Quadrado: "
						+ c1.getSomadosElementosaoQuadrado() + "\nVariância: " + c1.getVariancia() + "\nDesvio Padrão: "
						+ c1.getDesvioPadrao() + "\nPearson: " + c1.getPearson() + "\nCoeficiente de Assimetria: "
						+ c1.getCoeficientedeAssimetria() + "\n\n");*/
		return c1;
	}

	public String getCruzamento() {
		return cruzamento;
	}

	public void setCruzamento(String operação) {
		this.cruzamento = operação;
	}

	public double getMediana() {
		return mediana;
	}

	public void setMediana(double mediana) {
		this.mediana = mediana;
	}

	public double getSomadosElementos() {
		return somadosElementos;
	}

	public void setSomadosElementos(double somadosElementos) {
		this.somadosElementos = somadosElementos;
	}

	public double getMediaAritmetica() {
		return mediaAritmetica;
	}

	public void setMediaAritmetica(double mediaAritmetica) {
		this.mediaAritmetica = mediaAritmetica;
	}

	public double getSomadosElementosaoQuadrado() {
		return somadosElementosaoQuadrado;
	}

	public void setSomadosElementosaoQuadrado(double somadosElementosaoQuadrado) {
		this.somadosElementosaoQuadrado = somadosElementosaoQuadrado;
	}

	public double getVariancia() {
		return variancia;
	}

	public void setVariancia(double variancia) {
		this.variancia = variancia;
	}

	public double getDesvioPadrao() {
		return desvioPadrao;
	}

	public void setDesvioPadrao(double desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}

	public double getPearson() {
		return pearson;
	}

	public void setPearson(double pearson) {
		this.pearson = pearson;
	}

	public String getModa() {
		return moda;
	}

	public void setModa(String moda) {
		this.moda = moda;
	}

	public double getCoeficientedeAssimetria() {
		return coeficientedeAssimetria;
	}

	public void setCoeficientedeAssimetria(double coeficientedeAssimetria) {
		this.coeficientedeAssimetria = coeficientedeAssimetria;
	}
	public int[] retornaAModa(ArrayList<JCheckBox>listaAVerificar, ArrayList<Integer> Ocorrencias){
		int[] moda = new int[2];
		int max = -100000000;
		int id=0;
		for (int i = 0; i < Ocorrencias.size(); i++) {
			if(Ocorrencias.get(i)>max) {
				max = Ocorrencias.get(i);
				id = i;
			}
		}
		moda[0]=id;
		moda[1]=max;
		return moda;
		
	}

}
/*
 * // Marca o início do tempo double t1 = System.currentTimeMillis();
 * 
 * 
 * // exibe o tempo em segundos System.out.print("\n Tempo Total: " + (t2 - t1)
 * / Double.valueOf(1000) + " segundos");
 * 
 * // marca o fim do processamento double t2 = System.currentTimeMillis();
 */