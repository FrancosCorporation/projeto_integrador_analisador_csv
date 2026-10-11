// Leitor + ContaOcorrencias + cruzamento do Selecionador — port para web.
// O CSV original (Evasao.csv) usa vírgula, cabeçalho na 1ª linha e campos com acento.

export const MAX_CSV_COLUMNS = 200;
export const MAX_CSV_RECORDS = 50_000;

function normalizarCabecalhos(cabecalhos) {
  const usados = new Set();
  return cabecalhos.map((original, indice) => {
    const base = original.trim() || `Coluna ${indice + 1}`;
    let nome = base;
    let sufixo = 2;
    while (usados.has(nome.toLocaleLowerCase('pt-BR'))) nome = `${base} (${sufixo++})`;
    usados.add(nome.toLocaleLowerCase('pt-BR'));
    return nome;
  });
}

function erroRegistro(mensagem, numero) {
  return new Error(`${mensagem} (registro iniciado na linha ${numero}).`);
}

export function parseCSV(texto) {
  if (typeof texto !== 'string') throw new TypeError('O conteúdo do CSV precisa ser texto.');

  const conteudo = texto.replace(/^\uFEFF/, ''); // remove BOM
  const linhas = [];
  let campo = '';
  let registro = [];
  let dentroAspas = false;
  let aposAspas = false;
  let registroTemConteudo = false;
  let numeroLinha = 1;
  let linhaDoRegistro = 1;

  const finalizarCampo = () => {
    registro.push(campo.trim());
    campo = '';
    aposAspas = false;
  };
  const finalizarRegistro = () => {
    finalizarCampo();
    if (registroTemConteudo) linhas.push({ valores: registro, linha: linhaDoRegistro });
    registro = [];
    registroTemConteudo = false;
    linhaDoRegistro = numeroLinha + 1;
  };

  for (let indice = 0; indice < conteudo.length; indice++) {
    const caractere = conteudo[indice];

    if (dentroAspas) {
      registroTemConteudo = true;
      if (caractere === '"') {
        if (conteudo[indice + 1] === '"') {
          campo += '"';
          indice++;
        } else {
          dentroAspas = false;
          aposAspas = true;
        }
      } else if (caractere === '\r' && conteudo[indice + 1] === '\n') {
        campo += '\n';
        indice++;
        numeroLinha++;
      } else {
        campo += caractere;
        if (caractere === '\n' || caractere === '\r') numeroLinha++;
      }
      continue;
    }

    if (aposAspas && caractere !== ',' && caractere !== '\n' && caractere !== '\r' && caractere !== ' ' && caractere !== '\t') {
      throw erroRegistro('Há conteúdo inesperado após o fechamento de aspas', linhaDoRegistro);
    }

    if (caractere === ',' ) {
      registroTemConteudo = true;
      finalizarCampo();
    } else if (caractere === '\n' || caractere === '\r') {
      if (caractere === '\r' && conteudo[indice + 1] === '\n') indice++;
      finalizarRegistro();
      numeroLinha++;
      linhaDoRegistro = numeroLinha;
    } else if (caractere === '"') {
      if (campo.trim() !== '' || aposAspas) {
        throw erroRegistro('Aspas só podem iniciar um campo vazio', linhaDoRegistro);
      }
      campo = '';
      dentroAspas = true;
      registroTemConteudo = true;
    } else if (!aposAspas) {
      campo += caractere;
      if (!/\s/.test(caractere)) registroTemConteudo = true;
    }
  }

  if (dentroAspas) throw erroRegistro('O CSV termina com um campo entre aspas sem fechamento', linhaDoRegistro);
  if (campo !== '' || registro.length > 0 || aposAspas || registroTemConteudo) finalizarRegistro();
  if (linhas.length === 0) return { cabecalho: [], registros: [] };

  const primeira = linhas[0];
  if (primeira.valores.length > MAX_CSV_COLUMNS) {
    throw erroRegistro(`O limite é de ${MAX_CSV_COLUMNS} colunas`, primeira.linha);
  }
  const cabecalho = normalizarCabecalhos(primeira.valores);
  const registros = linhas.slice(1);
  if (registros.length > MAX_CSV_RECORDS) {
    throw erroRegistro(`O limite é de ${MAX_CSV_RECORDS.toLocaleString('pt-BR')} registros`, registros[MAX_CSV_RECORDS].linha);
  }

  for (const item of registros) {
    if (item.valores.length !== cabecalho.length) {
      throw erroRegistro(`O registro tem ${item.valores.length} campos; o cabeçalho tem ${cabecalho.length}`, item.linha);
    }
  }

  return { cabecalho, registros: registros.map((item) => item.valores) };
}

// Valores únicos de uma coluna (dedup ignorando caixa, como retornaListaFiltradaCheckBox)
export function categorias(registros, coluna) {
  const vistas = new Set();
  const unicas = [];
  for (const r of registros) {
    const v = (r[coluna] || '').trim();
    const chave = v.toLowerCase();
    if (v && !vistas.has(chave)) { vistas.add(chave); unicas.push(v); }
  }
  return unicas;
}

// search() do ContaOcorrencias: nº de ocorrências de um valor na coluna
export function contar(registros, coluna, valor) {
  let n = 0;
  for (const r of registros) if ((r[coluna] || '').trim().toLowerCase() === valor.trim().toLowerCase()) n++;
  return n;
}

// Cruzamento (como no Client.java: "mulheres" ∩ "solteiras"):
// filtra os registros pela variável 2 = valor2 e conta as categorias da variável 1
export function cruzamento(registros, coluna1, coluna2, valor2) {
  const filtrados = registros.filter(
    (r) => (r[coluna2] || '').trim().toLowerCase() === valor2.trim().toLowerCase()
  );
  return contarOcorrencias(filtrados, coluna1);
}

// Contagem por categoria: [{ categoria, ocorrencias }] ordenado por ocorrência (desc)
export function contarOcorrencias(registros, coluna) {
  const contagens = new Map();
  for (const registro of registros) {
    const categoria = (registro[coluna] || '').trim();
    if (!categoria) continue;
    const chave = categoria.toLowerCase();
    const existente = contagens.get(chave);
    if (existente) existente.ocorrencias++;
    else contagens.set(chave, { categoria, ocorrencias: 1 });
  }
  return [...contagens.values()].sort((a, b) => b.ocorrencias - a.ocorrencias);
}
