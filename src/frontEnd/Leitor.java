package frontEnd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Leitor extends JFrame {

	private JFileChooser selecaoArquivo;
	private List dadosCsv = new ArrayList();
	private ArrayList<String> dados = new ArrayList<String>();
	private File arquivoCsv =null;
	private DefaultTableModel naoexistenada = null;
	private int tamanhoDoCabecalho;
	private String nomeArquivo;
	
	public List getDadosCsv() {
		return dadosCsv;
	}

	public ArrayList<String> getDados() {
		return dados;
	}

	public int getTamanhoDoCabecalho() {
		return tamanhoDoCabecalho;
	}

	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public DefaultTableModel selecionaArquivo() {
		try {
		selecaoArquivo = new JFileChooser();
		selecaoArquivo.showOpenDialog(this);
		arquivoCsv = selecaoArquivo.getSelectedFile();		
		this.nomeArquivo = arquivoCsv.getName();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Arquivo não selecionado, tente novamente.", "Atenção",
					JOptionPane.QUESTION_MESSAGE);
			return naoexistenada;
		}
		return GravaColunas();
	}
	public DefaultTableModel selecionaArquivo(String caminho) {
		arquivoCsv = new File(caminho);
		this.nomeArquivo= arquivoCsv.getName();
			return GravaColunas();
		
	}

	public DefaultTableModel GravaColunas() {
		try {
			dadosCsv.clear();
			String line = "";
			String cvsSplitBy = ",";
			BufferedReader br = new BufferedReader(new FileReader(arquivoCsv));
			while ((line = br.readLine().replaceAll("_", " ").toLowerCase()) != null) {
				line.replaceAll(";", ",");
				String[] coluna = line.split(cvsSplitBy);
				for (int i = 0; i < coluna.length; i++) {
					this.dados.add(coluna[i]);
				}
				dadosCsv.add(coluna);
			}
			br.close();
			
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null,"Dados não foram lidos do csv ");
		}
		return gravaModelo();
		// return gravaModelo();
	}

	public DefaultTableModel gravaModelo() {
		DefaultTableModel tableModel = new DefaultTableModel();

		if (!this.dadosCsv.isEmpty()) {
			Object[] primeiraLinha = (Object[]) this.dadosCsv.get(0);
			this.tamanhoDoCabecalho = primeiraLinha.length;
			tableModel.setColumnCount(0);
			for (Object coluna : primeiraLinha) {
				tableModel.addColumn("" + coluna);
			}
			for (int i = 1; i < this.dadosCsv.size(); i++) {
				Object dado = this.dadosCsv.get(i);
				tableModel.addRow((Object[]) dado);
			}
			Aplicativo tabelaGet = new Aplicativo();
			JTable tblDados = tabelaGet.getTblDados();

			tblDados.setModel(tableModel);

		}
		return tableModel;

	}

}
