// UI do Analisador CSV — port do fluxo Leitor -> Selecionador -> ContaOcorrencias -> Estatistica -> Grafico.
import { parseCSV, categorias, contarOcorrencias, cruzamento } from './csv.js';
import { estatisticasCompletas } from './estatistica.js';

const $ = (id) => document.getElementById(id);
let dados = null; // { cabecalho, registros }

// ---------- dados ----------
$('inp-csv').addEventListener('change', (e) => {
  const file = e.target.files[0];
  if (!file) return;
  file.text().then((texto) => {
    dados = parseCSV(texto);
    $('arquivo-info').textContent = `${file.name} — ${dados.registros.length} registros`;
    preparar();
  });
});

$('btn-exemplo').addEventListener('click', async () => {
  const resposta = await fetch('data/Evasao.csv');
  dados = parseCSV(await resposta.text());
  $('arquivo-info').textContent = `Evasao.csv (exemplo) — ${dados.registros.length} registros`;
  preparar();
});

// ---------- variáveis ----------
function preparar() {
  $('sec-analise').hidden = false;
  const opcoes = dados.cabecalho.map((c, i) => `<option value="${i}">${c}</option>`).join('');
  $('sel-var1').innerHTML = opcoes;
  $('sel-var2').innerHTML = `<option value="">— nenhuma —</option>${opcoes}`;
  $('sel-var2').onchange = () => {
    const v2 = $('sel-var2').value;
    $('campo-valor2').hidden = v2 === '';
    if (v2 !== '') {
      const valores = categorias(dados.registros, Number(v2));
      $('sel-valor2').innerHTML = valores.map((v) => `<option>${v}</option>`).join('');
    }
    analisar();
  };
  $('sel-var1').onchange = analisar;
  $('sel-valor2').onchange = analisar;
  analisar();
}

function analisar() {
  if (!dados) return;
  const c1 = Number($('sel-var1').value);
  const c2 = $('sel-var2').value;
  const nomeV1 = dados.cabecalho[c1];
  const nomeV2 = c2 !== '' ? dados.cabecalho[Number(c2)] : null;

  let contagens, titulo;
  if (nomeV2) {
    contagens = cruzamento(dados.registros, c1, Number(c2), $('sel-valor2').value);
    titulo = `${nomeV1} × ${nomeV2} = ${$('sel-valor2').value}`;
  } else {
    contagens = contarOcorrencias(dados.registros, c1);
    titulo = `Ocorrências por ${nomeV1}`;
  }

  $('titulo-resultado').textContent = titulo;
  desenharGrafico(contagens);
  preencherTabela(contagens);

  const valores = contagens.map((c) => c.ocorrencias);
  const stats = estatisticasCompletas(valores);
  const corpoStats = [
    ['Soma dos elementos', stats.soma],
    ['Média aritmética', stats.media],
    ['Mediana', stats.mediana],
    ['Moda', stats.moda],
    ['Soma dos quadrados (Σx²)', stats.somaQuadrados],
    ['Variância amostral', stats.variancia],
    ['Desvio padrão', stats.desvioPadrao],
    ['Coef. de variação de Pearson (%)', stats.pearson],
    ['Coef. de assimetria', stats.coefAssimetria]
  ];
  $('tabela-stats').querySelector('tbody').innerHTML = corpoStats
    .map(([nome, valor]) => `<tr><td>${nome}</td><td>${Number(valor.toFixed(4))}</td></tr>`)
    .join('');
  $('sec-estatistica').hidden = false;
}

// ---------- gráfico de barras em SVG puro (substitui o JFreeChart) ----------
function desenharGrafico(contagens) {
  $('sec-resultado').hidden = false;
  const max = Math.max(...contagens.map((c) => c.ocorrencias), 1);
  const n = contagens.length;
  const largura = Math.max(320, n * 90);
  const altura = 260;
  const margemE = 40, margemB = 70, areaH = altura - margemB - 16;
  const passo = largura / n;

  const barras = contagens.map((c, i) => {
    const h = (c.ocorrencias / max) * areaH;
    const x = margemE + i * passo + 8;
    const y = altura - margemB - h;
    const centroRotulo = x + (passo - 16) / 2;
    const nome = c.categoria.length > 14 ? c.categoria.slice(0, 13) + '…' : c.categoria;
    return `
      <rect class="barra" x="${x}" y="${y}" width="${passo - 16}" height="${h}" rx="4">
        <title>${c.categoria}: ${c.ocorrencias}</title>
      </rect>
      <text class="rotulo-valor" x="${centroRotulo}" y="${y - 6}" text-anchor="middle">${c.ocorrencias}</text>
      <text class="rotulo" x="${centroRotulo}" y="${altura - margemB + 16}" text-anchor="middle"
        transform="rotate(35 ${centroRotulo} ${altura - margemB + 16})">${nome}</text>`;
  }).join('');

  const linhasEixo = [0, 0.25, 0.5, 0.75, 1].map((f) => {
    const y = altura - margemB - f * areaH;
    return `<line class="eixo" x1="${margemE}" y1="${y}" x2="${largura - 8}" y2="${y}"></line>
            <text class="rotulo" x="${margemE - 6}" y="${y + 4}" text-anchor="end">${Math.round(max * f)}</text>`;
  }).join('');

  $('grafico').innerHTML = `<svg viewBox="0 0 ${largura} ${altura}" width="${largura}" height="${altura}">
    ${linhasEixo}${barras}
    <line class="eixo" x1="${margemE}" y1="${altura - margemB}" x2="${largura - 8}" y2="${altura - margemB}"></line>
  </svg>`;
}

function preencherTabela(contagens) {
  $('tabela').querySelector('tbody').innerHTML = contagens
    .map((c) => `<tr><td>${c.categoria}</td><td>${c.ocorrencias}</td></tr>`)
    .join('');
}
