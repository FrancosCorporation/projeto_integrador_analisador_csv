package frontEnd;

import estatistica.Client;
import estatistica.ContaOcorrencias;
import grafico.Grafico;
import mysql.Dao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.JTextPane;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.border.TitledBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;

public class Selecionador extends JFrame {

	private JPanel contentPane;
	private Aplicativo app;
	private JCheckBox[] chckbx;
	private JButton btnSelecionar;
	private ArrayList<JCheckBox> listaDasChckbx;
	private ArrayList<JCheckBox> listaDasChckbxParaSelecionar;
	private ContaOcorrencias contOcor = new ContaOcorrencias();
	private ArrayList<Integer> quantidadeDeOcorrencias;
	private ArrayList<Integer> quantidadeDeOcorrenciasTotal;
	private ArrayList<String> listaDasVariaveisDosGraficos;
	private ArrayList<String> subCategoria;
	private ArrayList<String> listaDasCategorias;
	private JPanel panelLista2Variaveis;
	private JPanel panelLista1Variaveis;
	private DefaultTableModel tableModel;
	private JTextPane textSelecao;
	private Client cliente = new Client();
	private Client c1;
	private String nomeArquivo;

	public Selecionador(DefaultTableModel tableModel, JTable tblDados, ArrayList<String> arrayList,
			String nomeVariavelCabecalho) {
		this.tableModel = tableModel;
		this.nomeArquivo = nomeVariavelCabecalho;
		setFont(new Font("Ubuntu", Font.BOLD, 12));
		setTitle("Análises");
		chckbx = new JCheckBox[tableModel.getColumnCount()];

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				app = new Aplicativo(tableModel, tblDados, arrayList, nomeArquivo);
				app.setVisible(true);
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panelSelecao = new JPanel();

		JScrollPane scrollPane = new JScrollPane();

		textSelecao = new JTextPane();
		textSelecao.setFont(new Font("Ubuntu", Font.PLAIN, 11));
		textSelecao.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setViewportView(textSelecao);
		textSelecao.setEditable(false);

		JScrollPane scrollPane_1 = new JScrollPane();

		panelLista1Variaveis = new JPanel();
		scrollPane_1.setViewportView(panelLista1Variaveis);
		panelLista1Variaveis.setLayout(new GridLayout(tableModel.getColumnCount(), 1, 5, 5));
		setLocationRelativeTo(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		setLocationRelativeTo(null);

		panelLista2Variaveis = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelLista2Variaveis.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		scrollPane_2.setViewportView(panelLista2Variaveis);
		setLocationRelativeTo(null);

		// funcao de ler a primeira linha
		// E setar as check box dentro do painel 1
		setandoPainel1ComAsVariaveis();

		JPanel panel = new JPanel();

		JLabel lblNewLabel = new JLabel("Estatistica:");
		lblNewLabel.setFont(new Font("Ubuntu", Font.BOLD, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblCabealho = new JLabel("Cabeçalho");
		lblCabealho.setHorizontalAlignment(SwingConstants.CENTER);
		lblCabealho.setFont(new Font("Ubuntu", Font.BOLD, 12));

		JLabel lblItensDentroDo = new JLabel("Itens Para Gerar Grafico");
		lblItensDentroDo.setHorizontalAlignment(SwingConstants.CENTER);
		lblItensDentroDo.setFont(new Font("Ubuntu", Font.BOLD, 12));

		JPanel panel_1 = new JPanel();

		btnSelecionar = new JButton("Selecionar");
		btnSelecionar.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSelecionar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnSelecionar.setFont(new Font("Ubuntu", Font.PLAIN, 12));

		JButton btnGerarGrafico = new JButton("Gerar Gráfico");
		btnGerarGrafico.setHorizontalTextPosition(SwingConstants.CENTER);
		btnGerarGrafico.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnGerarGrafico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					panelLista2Variaveis.removeAll();
					setVisible(false);
					setVisible(true);
					listaDasVariaveisDosGraficos = new ArrayList<String>();
					for (int i = 0; i < listaDasChckbxParaSelecionar.size(); i++) {
						if (listaDasChckbxParaSelecionar.get(listaDasChckbxParaSelecionar.size() - 1).isSelected()) {
							for (int j = 0; j < listaDasChckbxParaSelecionar.size() - 1; j++) {
								listaDasVariaveisDosGraficos.add(listaDasChckbxParaSelecionar.get(j).getText());
								listaDasVariaveisDosGraficos.add(quantidadeDeOcorrencias.get(j) + "");
							}
							break;
						}
						if (listaDasChckbxParaSelecionar.get(i).isSelected()) {
							listaDasVariaveisDosGraficos.add(listaDasChckbxParaSelecionar.get(i).getText());
							listaDasVariaveisDosGraficos.add(quantidadeDeOcorrencias.get(i) + "");
						}

					}
					Grafico grafico = new Grafico(listaDasVariaveisDosGraficos);
					grafico.createChart();
					listaDasChckbxParaSelecionar = null;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Desculpe, mais voce nao pode acionar o Grafico sem nenhum parametro, verifique se selecionou as variaveis !!");
				}
			}
		});
		btnGerarGrafico.setFont(new Font("Ubuntu", Font.PLAIN, 12));

		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] moda = new int[2];
				String textoDoPainel = "";
				quantidadeDeOcorrenciasTotal = new ArrayList<Integer>();
				listaDasChckbxParaSelecionar = new ArrayList<JCheckBox>();
				quantidadeDeOcorrencias = new ArrayList<Integer>();
				subCategoria = new ArrayList<String>();
				// resetando o painel 2
				panelLista2Variaveis.removeAll();
				// criando um for para verificar qual check box esta selecionada
				for (int i = 0; i < chckbx.length; i++) {
					if (chckbx[i].isSelected()) {
						listaDasChckbx = new ArrayList<JCheckBox>();
						listaDasCategorias = new ArrayList<String>();
						int j = i;
						listaDasCategorias.add(chckbx[i].getText());
						subCategoria.add(chckbx[i].getText());
						i += tableModel.getColumnCount();
						while (i < arrayList.size()) {
							JCheckBox nvJbox = new JCheckBox();
							nvJbox.setText(arrayList.get(i));
							listaDasChckbx.add(nvJbox);
							i += tableModel.getColumnCount();
						}
						i = j;
						// enviando as check box e trazendo apenas as que sao iguais, (removendo as
						// duplicadas)
						listaDasChckbx = contOcor.retornaListaFiltradaCheckBox(listaDasChckbx);
						// adicionando as chceck no painel
						addCheckBoxAoPainel2(listaDasChckbx);
						listaDasChckbxParaSelecionar.addAll(listaDasChckbx);
						// contando ocorrencias das check
						quantidadeDeOcorrencias = contOcor.answerQueries(arrayList, listaDasChckbxParaSelecionar);
						// adicionando a matriz para o calculo estatistico
						quantidadeDeOcorrenciasTotal.addAll(quantidadeDeOcorrencias);
						// laço para pegar a categoria e inserir no painel junto com a contagem das
						// ocorrencias
						// adicionando ao painel estatistico os nomes das checkbox

						textoDoPainel += chckbx[i].getText().toUpperCase();
						if (i >= 0 && i < chckbx.length - 1) {
							textoDoPainel += " x ";
						}
						btnSelecionar.setVisible(false);
						btnSelecionar.setVisible(true);
					}

				}
				// setando grade com o tamanho da lista de checkboxs
				panelLista2Variaveis.setLayout(new GridLayout(listaDasChckbx.size(), 1, 0, 0));
				// enviando para a classe da estatistica
				textoDoPainel=textoDoPainel.substring(0, textoDoPainel.length()-3);
				c1 = cliente.calculo(quantidadeDeOcorrenciasTotal, textoDoPainel.toLowerCase(),
						listaDasChckbxParaSelecionar.get(moda[0]).getText());
				// calculando a moda
				moda = cliente.retornaAModa(listaDasChckbxParaSelecionar, quantidadeDeOcorrencias);
				textoDoPainel += "\nModa: " + listaDasChckbxParaSelecionar.get(moda[0]).getText() + " = " + moda[1];

				// Setando os Valores no painel puxando da classe client a estatistica !!
				textoDoPainel += setaEstatistica(c1);
				textSelecao.setText(textoDoPainel);

				// adicionando botao selectall
				JCheckBox selectall = new JCheckBox();
				selectall.setText("Select All");
				panelLista2Variaveis.add(selectall);
				listaDasChckbxParaSelecionar.add(selectall);
			}
		});
		JButton btnInserirNoBanco = new JButton("Inserir no Banco de Dados");
		btnInserirNoBanco.setHorizontalTextPosition(SwingConstants.CENTER);
		btnInserirNoBanco.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnInserirNoBanco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Dao.inserir(c1);
					JOptionPane.showMessageDialog(null, "Dados inseridos Com Sucesso !!");

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Verifique se o quadro de Estatistica contem as informações que seram inseridas, caso não exista, selecione novamente !!");
				}
			}
		});
		btnInserirNoBanco.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_1.add(btnSelecionar);
		panel_1.add(btnGerarGrafico);
		panel_1.add(btnInserirNoBanco);

		GroupLayout gl_panelSelecao = new GroupLayout(panelSelecao);
		gl_panelSelecao
				.setHorizontalGroup(gl_panelSelecao.createParallelGroup(Alignment.LEADING).addComponent(scrollPane));
		gl_panelSelecao
				.setVerticalGroup(gl_panelSelecao.createParallelGroup(Alignment.LEADING).addComponent(scrollPane));
		panelSelecao.setLayout(gl_panelSelecao);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(16)
						.addComponent(lblCabealho, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addGap(18)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE).addGap(18))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(16)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addGap(18)
						.addComponent(panelSelecao, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE).addGap(18))
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE).addGap(18))
				.addGroup(
						gl_contentPane.createSequentialGroup().addGap(16)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblItensDentroDo, GroupLayout.DEFAULT_SIZE, 283,
														Short.MAX_VALUE)
												.addGap(10))
										.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE).addGap(26)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(9)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCabealho, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGap(6)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
						.addComponent(panelSelecao, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
				.addGap(6)
				.addComponent(lblItensDentroDo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(6)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		contentPane.setLayout(gl_contentPane);

	}

	public String setaEstatistica(Client c1) {
		String vaiRetornar = "\nMediana: " + c1.getMediana() + "\n";
		vaiRetornar += "Soma Dos Elementos: " + c1.getSomadosElementos() + "\n";
		vaiRetornar += "Média Aritmética: " + c1.getMediaAritmetica() + "\n";
		vaiRetornar += "Soma dos Elementos ao Quadrado: " + c1.getSomadosElementosaoQuadrado() + "\n";
		vaiRetornar += "Variância: " + c1.getVariancia() + "\n";
		vaiRetornar += "Desvio Padrão: " + c1.getDesvioPadrao() + "\n";
		vaiRetornar += "Pearson: " + c1.getPearson();
		vaiRetornar += "Coeficiente de Assimetria: " + c1.getCoeficientedeAssimetria();
		return vaiRetornar;

	}

	public void addCheckBoxAoPainel2(ArrayList<JCheckBox> listaDasChckbx) {
		for (int i = 0; i < listaDasChckbx.size(); i++) {
			panelLista2Variaveis.add(listaDasChckbx.get(i));
		}
	}

	public void setandoPainel1ComAsVariaveis() {
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			chckbx[i] = new JCheckBox();
			chckbx[i].setText(tableModel.getColumnName(i));
			panelLista1Variaveis.add(chckbx[i]);
		}
	}

	public JPanel getPanelLista2Variaveis() {
		return panelLista2Variaveis;
	}

	public JTextPane getTextSelecao() {
		return textSelecao;
	}

	public JPanel getPanelLista1Variaveis() {
		return panelLista1Variaveis;
	}
}