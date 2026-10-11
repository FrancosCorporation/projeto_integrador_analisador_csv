// Interface do Analisador CSV. Os arquivos são lidos no navegador e não são enviados ao servidor.
import { parseCSV, categorias, contarOcorrencias, cruzamento, MAX_CSV_COLUMNS, MAX_CSV_RECORDS } from './csv.js';
import { estatisticasCompletas } from './estatistica.js';

const $ = (id) => document.getElementById(id);
const SVG_NS = 'http://www.w3.org/2000/svg';
const MAX_CSV_BYTES = 10 * 1024 * 1024;
const MAX_CATEGORIAS_GRAFICO = 40;
const formatarNumero = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 4 });

let dados = null;
let versaoLeitura = 0;

$('privacidade').textContent = `O arquivo é processado neste navegador e não é enviado nem salvo no servidor. Limites: ${MAX_CSV_BYTES / (1024 * 1024)} MB, ${MAX_CSV_COLUMNS} colunas e ${MAX_CSV_RECORDS.toLocaleString('pt-BR')} registros.`;

function substituirFilhos(elemento, filhos) {
  const fragmento = document.createDocumentFragment();
  for (const filho of filhos) fragmento.append(filho);
  elemento.replaceChildren(fragmento);
}

function mostrarMensagem(texto, tipo = '') {
  const elemento = $('mensagem');
  elemento.textContent = texto;
  elemento.className = `mensagem${tipo ? ` ${tipo}` : ''}`;
  elemento.setAttribute('role', tipo === 'erro' ? 'alert' : 'status');
  elemento.setAttribute('aria-live', tipo === 'erro' ? 'assertive' : 'polite');
}

function limparResultados() {
  $('sec-resultado').hidden = true;
  $('sec-estatistica').hidden = true;
  $('grafico').replaceChildren();
  $('nota-grafico').hidden = true;
  $('tabela').querySelector('tbody').replaceChildren();
  $('tabela-stats').querySelector('tbody').replaceChildren();
}

function carregarDados(texto, nomeArquivo) {
  const novosDados = parseCSV(texto);
  if (!novosDados.cabecalho.length) throw new Error('O arquivo não contém uma linha de cabeçalho.');
  if (!novosDados.registros.length) throw new Error('O CSV não contém registros para analisar.');

  dados = novosDados;
  $('arquivo-info').textContent = `${nomeArquivo} · ${dados.registros.length.toLocaleString('pt-BR')} registros · ${dados.cabecalho.length} colunas`;
  prepararAnalise();
}

function criarOpcao(valor, texto) {
  const opcao = document.createElement('option');
  opcao.value = String(valor);
  opcao.textContent = texto;
  return opcao;
}

function prepararAnalise() {
  const opcoes = dados.cabecalho.map((nome, indice) => criarOpcao(indice, nome));
  substituirFilhos($('sel-var1'), opcoes.map((opcao) => opcao.cloneNode(true)));
  substituirFilhos($('sel-var2'), [criarOpcao('', '— nenhuma —'), ...opcoes]);
  $('sel-valor2').replaceChildren();
  $('campo-valor2').hidden = true;
  $('sec-analise').hidden = false;
  limparResultados();
  analisar();
}

function atualizarValoresDoCruzamento() {
  const coluna = $('sel-var2').value;
  const campo = $('campo-valor2');
  const seletor = $('sel-valor2');
  seletor.replaceChildren();

  if (coluna === '') {
    campo.hidden = true;
    analisar();
    return;
  }

  const valores = categorias(dados.registros, Number(coluna));
  campo.hidden = false;
  substituirFilhos(seletor, valores.map((valor) => criarOpcao(valor, valor)));
  analisar();
}

function analisar() {
  if (!dados) return;
  try {
    const colunaPrincipal = Number($('sel-var1').value);
    if (!Number.isInteger(colunaPrincipal) || colunaPrincipal < 0 || colunaPrincipal >= dados.cabecalho.length) {
      throw new Error('Escolha uma variável principal válida.');
    }

    const cruzarColuna = $('sel-var2').value;
    let contagens;
    let titulo;
    if (cruzarColuna !== '') {
      const colunaSecundaria = Number(cruzarColuna);
      const valorSecundario = $('sel-valor2').value;
      if (!valorSecundario) {
        limparResultados();
        mostrarMensagem('A variável escolhida não possui valores para cruzar.', '');
        return;
      }
      contagens = cruzamento(dados.registros, colunaPrincipal, colunaSecundaria, valorSecundario);
      titulo = `${dados.cabecalho[colunaPrincipal]} × ${dados.cabecalho[colunaSecundaria]} = ${valorSecundario}`;
    } else {
      contagens = contarOcorrencias(dados.registros, colunaPrincipal);
      titulo = `Ocorrências por ${dados.cabecalho[colunaPrincipal]}`;
    }

    if (!contagens.length) {
      limparResultados();
      mostrarMensagem('Não há valores preenchidos para esta análise.', '');
      return;
    }

    $('titulo-resultado').textContent = titulo;
    desenharGrafico(contagens);
    preencherTabela(contagens);
    preencherEstatisticas(contagens);
    $('sec-resultado').hidden = false;
    $('sec-estatistica').hidden = false;
    mostrarMensagem(`Análise pronta: ${contagens.length.toLocaleString('pt-BR')} categorias encontradas.`, 'sucesso');
  } catch (error) {
    limparResultados();
    mostrarMensagem(error.message || 'Não foi possível analisar este cruzamento.', 'erro');
  }
}

function criarCelula(tag, texto) {
  const celula = document.createElement(tag);
  celula.textContent = String(texto);
  return celula;
}

function preencherTabela(contagens) {
  const linhas = contagens.map(({ categoria, ocorrencias }) => {
    const linha = document.createElement('tr');
    linha.append(criarCelula('td', categoria), criarCelula('td', ocorrencias.toLocaleString('pt-BR')));
    return linha;
  });
  substituirFilhos($('tabela').querySelector('tbody'), linhas);
}

function preencherEstatisticas(contagens) {
  const stats = estatisticasCompletas(contagens.map(({ ocorrencias }) => ocorrencias));
  const itens = [
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
  const linhas = itens.map(([nome, valor]) => {
    const linha = document.createElement('tr');
    linha.append(criarCelula('td', nome), criarCelula('td', formatarNumero.format(valor)));
    return linha;
  });
  substituirFilhos($('tabela-stats').querySelector('tbody'), linhas);
}

function elementoSvg(nome, atributos = {}, texto) {
  const elemento = document.createElementNS(SVG_NS, nome);
  for (const [atributo, valor] of Object.entries(atributos)) elemento.setAttribute(atributo, String(valor));
  if (texto !== undefined) elemento.textContent = String(texto);
  return elemento;
}

function desenharGrafico(contagens) {
  const visiveis = contagens.slice(0, MAX_CATEGORIAS_GRAFICO);
  const maximo = visiveis.reduce((maior, item) => Math.max(maior, item.ocorrencias), 1);
  const quantidade = visiveis.length;
  const largura = Math.max(320, quantidade * 90);
  const altura = 280;
  const margemEsquerda = 44;
  const margemInferior = 82;
  const areaAltura = altura - margemInferior - 18;
  const passo = largura / quantidade;

  const svg = elementoSvg('svg', {
    viewBox: `0 0 ${largura} ${altura}`,
    width: largura,
    height: altura,
    role: 'img',
    'aria-labelledby': 'titulo-grafico descricao-grafico'
  });
  svg.append(
    elementoSvg('title', { id: 'titulo-grafico' }, 'Ocorrências por categoria'),
    elementoSvg('desc', { id: 'descricao-grafico' }, `Gráfico de barras com ${visiveis.length} categorias. A tabela abaixo contém os valores completos.`)
  );

  for (const fracao of [0, 0.25, 0.5, 0.75, 1]) {
    const y = altura - margemInferior - fracao * areaAltura;
    svg.append(
      elementoSvg('line', { class: 'eixo', x1: margemEsquerda, y1: y, x2: largura - 8, y2: y }),
      elementoSvg('text', { class: 'rotulo', x: margemEsquerda - 7, y: y + 4, 'text-anchor': 'end' }, Math.round(maximo * fracao))
    );
  }

  visiveis.forEach(({ categoria, ocorrencias }, indice) => {
    const alturaBarra = (ocorrencias / maximo) * areaAltura;
    const x = margemEsquerda + indice * passo + 8;
    const y = altura - margemInferior - alturaBarra;
    const centro = x + (passo - 16) / 2;
    const rotulo = categoria.length > 14 ? `${categoria.slice(0, 13)}…` : categoria;
    const barra = elementoSvg('rect', {
      class: 'barra', x, y, width: Math.max(1, passo - 16), height: alturaBarra, rx: 4
    });
    barra.append(elementoSvg('title', {}, `${categoria}: ${ocorrencias.toLocaleString('pt-BR')}`));
    svg.append(
      barra,
      elementoSvg('text', { class: 'rotulo-valor', x: centro, y: y - 6, 'text-anchor': 'middle' }, ocorrencias),
      elementoSvg('text', {
        class: 'rotulo', x: centro, y: altura - margemInferior + 17, 'text-anchor': 'middle',
        transform: `rotate(35 ${centro} ${altura - margemInferior + 17})`
      }, rotulo)
    );
  });

  svg.append(elementoSvg('line', {
    class: 'eixo', x1: margemEsquerda, y1: altura - margemInferior,
    x2: largura - 8, y2: altura - margemInferior
  }));
  $('grafico').replaceChildren(svg);

  const nota = $('nota-grafico');
  nota.hidden = contagens.length <= MAX_CATEGORIAS_GRAFICO;
  nota.textContent = `O gráfico mostra as ${MAX_CATEGORIAS_GRAFICO} categorias mais frequentes; a tabela lista as ${contagens.length} categorias.`;
}

function falharLeitura(erro) {
  mostrarMensagem(`O arquivo não foi carregado. ${erro.message || 'Verifique se ele é um CSV válido.'}`, 'erro');
}

$('inp-csv').addEventListener('change', async (event) => {
  const [arquivo] = event.target.files || [];
  if (!arquivo) return;
  const versao = ++versaoLeitura;

  try {
    if (!/\.csv$/i.test(arquivo.name)) throw new Error('Escolha um arquivo com extensão .csv.');
    if (arquivo.size > MAX_CSV_BYTES) throw new Error('O limite do arquivo é 10 MB.');
    mostrarMensagem(`Lendo ${arquivo.name}…`);
    const texto = await arquivo.text();
    if (versao !== versaoLeitura) return;
    carregarDados(texto, arquivo.name);
  } catch (error) {
    if (versao === versaoLeitura) falharLeitura(error);
  } finally {
    event.target.value = '';
  }
});

$('btn-exemplo').addEventListener('click', async () => {
  const versao = ++versaoLeitura;
  const botao = $('btn-exemplo');
  botao.disabled = true;
  mostrarMensagem('Carregando o arquivo de exemplo…');
  try {
    const resposta = await fetch('data/Evasao.csv', { cache: 'no-store' });
    if (!resposta.ok) throw new Error('Não foi possível acessar Evasao.csv.');
    const texto = await resposta.text();
    if (versao !== versaoLeitura) return;
    carregarDados(texto, 'Evasao.csv (exemplo)');
  } catch (error) {
    if (versao === versaoLeitura) falharLeitura(error);
  } finally {
    botao.disabled = false;
  }
});

$('sel-var1').addEventListener('change', analisar);
$('sel-var2').addEventListener('change', atualizarValoresDoCruzamento);
$('sel-valor2').addEventListener('change', analisar);
