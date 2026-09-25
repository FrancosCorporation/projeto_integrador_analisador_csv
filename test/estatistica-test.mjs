// Testes do Analisador CSV (Node puro). Uso: npm test
import { parseCSV, categorias, contarOcorrencias, cruzamento, contar } from '../js/csv.js';
import { mediaAritmetica, mediana, moda, varianciaAmostral, desvioPadrao, pearson, coefAssimetria, estatisticasCompletas, soma, somaDosQuadrados } from '../js/estatistica.js';

let falhas = 0;
function check(nome, cond) {
  console.log(`${cond ? 'PASS' : 'FAIL'} - ${nome}`);
  if (!cond) falhas++;
}

// ---------- estatística com valores conhecidos ----------
const amostra = [2, 4, 4, 4, 5, 5, 7, 9]; // média 5, mediana 4.5, moda 4
check('soma [2,4,4,4,5,5,7,9] = 40', soma(amostra) === 40);
check('média = 5', mediaAritmetica(amostra) === 5);
check('mediana = 4.5', mediana(amostra) === 4.5);
check('moda = 4', moda(amostra) === 4);
check('soma dos quadrados = 232', somaDosQuadrados(amostra) === 232);
check('variância amostral = 4.571428...', Math.abs(varianciaAmostral(amostra) - 32 / 7) < 1e-9);
check('desvio padrão = √(32/7)', Math.abs(desvioPadrao(amostra) - Math.sqrt(32 / 7)) < 1e-9);
check('pearson ≈ 42.78%', Math.abs(pearson(amostra) - (Math.sqrt(32 / 7) / 5) * 100) < 1e-6);
check('assimetria = (5-4)/dp', Math.abs(coefAssimetria(amostra) - 1 / Math.sqrt(32 / 7)) < 1e-9);

// mediana ímpar e casos-borda
check('mediana de [3] = 3', mediana([3]) === 3);
check('variância com n<2 = 0', varianciaAmostral([5]) === 0);
check('moda única de [7] = 7', moda([7]) === 7);

// ---------- CSV: parse com aspas, vírgulas e acento ----------
{
  const csv = 'NOME,CIDADE,IDADE\n"Silva, Jr",São Paulo,30\nAna, "Belo Horizonte" ,25\n';
  const { cabecalho, registros } = parseCSV(csv);
  check('parse: cabeçalho correto', cabecalho.join('|') === 'NOME|CIDADE|IDADE');
  check('parse: campo com vírgula dentro de aspas', registros[0][0] === 'Silva, Jr');
  check('parse: trim em campo com aspas', registros[1][1] === 'Belo Horizonte');
  check('parse: 2 registros', registros.length === 2);
}

// ---------- contagem e cruzamento (espírito do ContaOcorrencias/Client) ----------
{
  const { cabecalho, registros } = parseCSV(
    'SEXO,ESTADO\nF,Solteira\nF,Solteira\nF,Casada\nM,Solteira\nM,Masculino?\nF,Solteira\n');
  const cats = categorias(registros, 0);
  check('categorias únicas por coluna SEXO', cats.join('|') === 'F|M');
  check('contar F = 4', contar(registros, 0, 'F') === 4);
  const contagens = contarOcorrencias(registros, 1);
  check('contagens por ESTADO ordenadas', JSON.stringify(contagens) ===
    JSON.stringify([{ categoria: 'Solteira', ocorrencias: 4 }, { categoria: 'Casada', ocorrencias: 1 }, { categoria: 'Masculino?', ocorrencias: 1 }]));
  const cruz = cruzamento(registros, 0, 1, 'Solteira'); // SEXO × ESTADO=Solteira
  check('cruzamento F∩Solteira = 3 e M∩Solteira = 1',
    JSON.stringify(cruz) === JSON.stringify([{ categoria: 'F', ocorrencias: 3 }, { categoria: 'M', ocorrencias: 1 }]));
}

// ---------- estatísticasCompletas integra ----------
{
  const s = estatisticasCompletas([1, 2, 3, 4]);
  check('estatisticasCompletas: n e soma', s.n === 4 && s.soma === 10);
  check('estatisticasCompletas: variância = 1.666...', Math.abs(s.variancia - 5 / 3) < 1e-9);
}

console.log(falhas === 0 ? '\nTODOS OS TESTES PASSARAM ✔' : `\n${falhas} TESTE(S) FALHARAM ✘`);
process.exit(falhas === 0 ? 0 : 1);
