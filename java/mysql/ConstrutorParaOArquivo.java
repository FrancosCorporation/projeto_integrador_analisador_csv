package mysql;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ConstrutorParaOArquivo {
	public ConstrutorParaOArquivo(ArrayList<String> arrayList, int tamanhoColuna, DefaultTableModel tableModel,
			String nomeBanco) {
		final String sql = "insert into " + nomeBanco + " values (";
		try {
			java.sql.Connection conn = Conexao.getConnection();
			int tamanholeng = 0;
			for (int i = tamanhoColuna; i < arrayList.size(); i=tamanhoColuna+i) {
				String nova = sql;
				tamanholeng = i+tamanhoColuna;
				for (int j = i; j < tamanholeng; j++) {
					if (j == tamanholeng-1) {
						nova += "'" + arrayList.get(j) + "');";
						PreparedStatement pstm = conn.prepareStatement(nova);
						pstm.executeUpdate();
						break;
					} else {
						nova += "'" + arrayList.get(j) + "',";
					}
				}
			}

		} catch (Exception e) {
			//
		}
	}

}
