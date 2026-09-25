package estatistica;

import java.util.ArrayList;
import javax.swing.JCheckBox;

// Classe para achar o numero de vezes que uma string aparece no array de strings

public class ContaOcorrencias {
	// retorna numero de ocorrencias em um array
	public int search(ArrayList<String> arr, String s) {
		int counter = 0;
		for (int j = 0; j < arr.size(); j++)
			// verificando se a string pedida esta presente no array, se presente aumenta o
			// contador
			if (s.equals(arr.get(j)))
				counter++;

		return counter;
	}

	public ArrayList<Integer> answerQueries(ArrayList<String> arr, ArrayList<JCheckBox> q) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		for (int i = 0; i < q.size(); i++) {
			lista.add(search(arr, q.get(i).getText()));
		}
		return lista;
	}

	public ArrayList<JCheckBox> retornaListaFiltradaCheckBox(ArrayList<JCheckBox> listaCompleta) {
		try {
			for (int k = 0; k < listaCompleta.size(); k++) {
				for (int l = k; l < listaCompleta.size(); l++) {
					if (listaCompleta.get(k).getText().equalsIgnoreCase(listaCompleta.get(l).getText())) {
						listaCompleta.remove(l);
					}

				}
			}
			for (int k = 0; k < listaCompleta.size(); k++) {
				for (int l = 0; l < listaCompleta.size(); l++) {
					if (listaCompleta.get(k).getText().equalsIgnoreCase(listaCompleta.get(l).getText())) {
						listaCompleta.remove(l);
					}
				}
			}
		} catch (Exception e) {
			return listaCompleta;
		}
		return listaCompleta;
	}
 public String retornaOMaiorValor() {
	 
	return null;
	 
 }
}