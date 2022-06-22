package frontEnd;

import javax.swing.table.DefaultTableModel;

import mysql.ConstrutorParaOArquivo;
import mysql.Dao;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Aplicativo extends javax.swing.JFrame {
	private javax.swing.JButton btnAnalizar;
	private javax.swing.JButton btnLerCsv;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable tblDados;
	private DefaultTableModel tableModel;
	private Leitor inicia;
	private ArrayList<String> arrayList;
	private String nomeArquivo;
	private ConstrutorParaOArquivo construtorDoArquivo;

	public Aplicativo() {
		setFont(new Font("Ubuntu", Font.BOLD, 12));
		setTitle("Analizador de Arquivos CSV");
		initComponents();

	}

	public Aplicativo(DefaultTableModel tableModel, JTable tblDados, ArrayList<String> arrayList,String nomeArquivo) {
		this.tableModel = tableModel;
		this.tblDados = tblDados;
		this.arrayList = arrayList;
		this.nomeArquivo = nomeArquivo;
		setFont(new Font("Ubuntu", Font.BOLD, 12));
		setTitle("Analizador de Arquivos CSV");
		initComponents();

	}

	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblDados = new javax.swing.JTable();
		tblDados.setAutoCreateRowSorter(true);
		btnLerCsv = new javax.swing.JButton();
		btnLerCsv.setHorizontalTextPosition(SwingConstants.CENTER);
		btnLerCsv.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnLerCsv.setMargin(new Insets(2, 5, 2, 5));
		btnLerCsv.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		btnAnalizar = new javax.swing.JButton();
		btnAnalizar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnAnalizar.setMargin(new Insets(2, 5, 2, 5));
		btnAnalizar.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		btnAnalizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		tblDados.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null } }, new String[] { "" }) {
			Class[] types = new Class[] { java.lang.String.class };

			@Override
			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});

		jScrollPane1.setViewportView(tblDados);
		btnLerCsv.setText("Ler CSV");

		btnLerCsv.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BotaoLer(evt);
			}
		});

		btnAnalizar.setText("Analizar");
		btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					if (arrayList == null) {
						Selecionador sel = new Selecionador(tableModel, tblDados, arrayList,nomeArquivo);
						sel.setVisible(true);
						setLocationRelativeTo(null);
						dispose();
					} else {
						Selecionador sel = new Selecionador(tableModel, tblDados, arrayList, nomeArquivo);
						sel.setVisible(true);
						setLocationRelativeTo(null);
						dispose();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Identificamos que não existem dados selecionados, verifique se selecionou o arquivo.",
							"Cuidado", JOptionPane.QUESTION_MESSAGE);
				}
			}
		});

		JButton btnExportarParaO = new JButton();
		btnExportarParaO.setText("Exportar");
		btnExportarParaO.setMargin(new Insets(2, 5, 2, 5));
		btnExportarParaO.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnExportarParaO.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnExportarParaO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					java.util.Date data = new java.util.Date();
					String nomeBanco = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")).toLowerCase()
							+ data.getDate() + "$" + (data.getMonth() + 1) + "$" + (data.getYear() + 1900) + "$"
							+ (data.getHours() - 1) + "$" + data.getMinutes() + "$" + data.getSeconds();
					Dao.criandoBancoDeDados(nomeBanco);
					Dao.criandoTabelas(nomeBanco, nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")),tableModel);
					construtorDoArquivo = new ConstrutorParaOArquivo(arrayList, tableModel.getColumnCount(),tableModel,nomeBanco+"."+nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")).toLowerCase());

				}

				catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Identificamos que não existem dados selecionados, verifique se selecionou o arquivo.",
							"Cuidado", JOptionPane.QUESTION_MESSAGE);
				}
			}
		});
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE).addGap(13))
						.addGroup(Alignment.TRAILING,
								layout.createSequentialGroup().addGap(222)
										.addComponent(btnLerCsv, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnAnalizar, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnExportarParaO,
												GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
										.addGap(209)));
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE)
												.addComponent(btnAnalizar).addComponent(btnExportarParaO))
										.addComponent(btnLerCsv))
								.addGap(20)));
		getContentPane().setLayout(layout);

		pack();
		setLocationRelativeTo(null);
		if (tableModel != null) {
			this.tblDados.setModel(tableModel);
		}
	}

	private void BotaoLer(java.awt.event.ActionEvent evt) {
		inicia = new Leitor();
		tableModel = inicia.selecionaArquivo();
		//tableModel =inicia.selecionaArquivo("C:\\Users\\rodol\\git\\PI2\\Documentos\\Evasao.csv");
		if (tableModel != null) {
			this.tblDados.setModel(tableModel);
		}
		this.arrayList = inicia.getDados();
		this.nomeArquivo = inicia.getNomeArquivo();

	}

	public static void main(String args[]) {
		try {
			new Splash();
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Aplicativo.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Aplicativo.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Aplicativo.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Aplicativo.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Aplicativo().setVisible(true);

			}
		});
	}

	public javax.swing.JTable getTblDados() {
		return tblDados;
	}

	public void setTblDados(javax.swing.JTable tblDados) {
		this.tblDados = tblDados;
	}
}