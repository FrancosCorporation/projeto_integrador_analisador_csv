package mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import estatistica.Client;

// recebe instrucoes de Client no pacote estatistica
public class Dao {

	public static boolean inserir(Client client) {
		try {
			String sql = "insert into evasoes values (?,?,?,?,?,?,?,?,?,?)";

			Connection conn = Conexao.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);

			pstm.setString(1, client.getCruzamento());
			pstm.setDouble(2, client.getMediana());
			pstm.setDouble(3, client.getSomadosElementos());
			pstm.setDouble(4, client.getMediaAritmetica());
			pstm.setDouble(5, client.getSomadosElementosaoQuadrado());
			pstm.setDouble(6, client.getVariancia());
			pstm.setDouble(7, client.getDesvioPadrao());
			pstm.setDouble(8, client.getPearson());
			pstm.setString(9, client.getModa());
			pstm.setDouble(10, client.getCoeficientedeAssimetria());
			pstm.executeUpdate();
			return true;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel inserir, o Banco de Dados pode estar Desconectado !!");
		}
		return false;
	}
	public static boolean inserirCsv(Client client) {
		try {
			String sql = "insert into evasoes values (?,?,?,?,?,?,?,?,?,?)";

			Connection conn = Conexao.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);

			pstm.setString(1, client.getCruzamento());
			pstm.setDouble(2, client.getMediana());
			pstm.setDouble(3, client.getSomadosElementos());
			pstm.setDouble(4, client.getMediaAritmetica());
			pstm.setDouble(5, client.getSomadosElementosaoQuadrado());
			pstm.setDouble(6, client.getVariancia());
			pstm.setDouble(7, client.getDesvioPadrao());
			pstm.setDouble(8, client.getPearson());
			pstm.setString(9, client.getModa());
			pstm.setDouble(10, client.getCoeficientedeAssimetria());
			pstm.executeUpdate();
			return true;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel inserir, o Banco de Dados pode estar Desconectado !!");
		}
		return false;
	}


	public static boolean inserirTudo(Client client) {
		try {
			String sql = "insert into evasoes values (?,?,?,?,?,?,?,?,?,?)";

			Connection conn = Conexao.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);

			pstm.setString(1, client.getCruzamento());
			pstm.setDouble(2, client.getMediana());
			pstm.setDouble(3, client.getSomadosElementos());
			pstm.setDouble(4, client.getMediaAritmetica());
			pstm.setDouble(5, client.getSomadosElementosaoQuadrado());
			pstm.setDouble(6, client.getVariancia());
			pstm.setDouble(7, client.getDesvioPadrao());
			pstm.setDouble(8, client.getPearson());
			pstm.setString(9, client.getModa());
			pstm.setDouble(10, client.getCoeficientedeAssimetria());
			pstm.executeUpdate();
			return true;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel inserir, o Banco de Dados pode estar Desconectado !!");
		}
		return false;
	}

	public static void criandoBancoDeDados(String nomeBd) {
		try {
			String sql = "create database " + nomeBd;

			Connection conn = Conexao.getConnection();

			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.execute();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel Criar o Banco de Dados, Talvez pode estar Desconectado !!");
		}

	}

	public static void criandoTabelas(String nomeBd, String nomeArquivo, DefaultTableModel tableModel) {

		try {
			String criandoVariaveisDoBanco = "";
			for (int i = 0; i < tableModel.getColumnCount(); i++) {
				if (i == 0) {
					criandoVariaveisDoBanco += "(" + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(250),";
				}

				if (i > 0 && i < tableModel.getColumnCount() - 1) {
					criandoVariaveisDoBanco += " " + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(250),";
				}

				if (i == tableModel.getColumnCount() - 1) {
					criandoVariaveisDoBanco += " " + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(250));";
				}
			}
			String sql = "create table " + nomeBd + "." + nomeArquivo.toLowerCase() + criandoVariaveisDoBanco;
			Connection conn = Conexao.getConnection();

			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();
			JOptionPane.showMessageDialog(null, "Inserido com Sucesso !!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel Criar o Banco de Dados, Talvez pode estar Desconectado !!");
		}
	}
	public static void inserindoDadosNoBd(String nomeBd, String nomeArquivo, DefaultTableModel tableModel) {

		try {
			String criandoVariaveisDoBanco = "";
			for (int i = 0; i < tableModel.getColumnCount(); i++) {
				if (i == 0) {
					criandoVariaveisDoBanco += "(" + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(50),";
				}

				if (i > 0 && i < tableModel.getColumnCount() - 1) {
					criandoVariaveisDoBanco += " " + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(50),";
				}

				if (i == tableModel.getColumnCount() - 1) {
					criandoVariaveisDoBanco += " " + tableModel.getColumnName(i).replaceAll(" ", "") + " varchar(50));";
				}
			}
			String sql = "create table " + nomeBd + "." + nomeArquivo.toLowerCase() + criandoVariaveisDoBanco;
			Connection conn = Conexao.getConnection();

			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();
			JOptionPane.showMessageDialog(null, "Inserido com Sucesso !!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel Criar o Banco de Dados, Talvez pode estar Desconectado !!");
		}
	}
}