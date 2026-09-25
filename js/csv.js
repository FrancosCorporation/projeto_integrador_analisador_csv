// Leitor + ContaOcorrencias + cruzamento do Selecionador — port para web.
// O CSV original (Evasao.csv) usa vírgula, cabeçalho na 1ª linha e campos com acento.

export function parseCSV(texto) {
  const linhas = [];
  let campo = '', linha = [], dentroAspas = false;
  const t = texto.replace(/^\uFEFF/, ''); // remove BOM
  for (let i = 0; i < t.length; i++) {
    const c = t[i];
    if (dentroAspas) {
      if (c === '"') {
        if (t[i + 1] === '"') { campo += '"'; i++; } else dentroAspas = false;
      } else campo += c;
    } else if (c === '"') dentroAspas = true;
    else if (c === ',') { linha.push(campo.trim()); campo = ''; }
    else if (c === '\n') { linha.push(campo.trim()); linhas.push(linha); linha = []; campo = ''; }
    else if (c !== '\r') campo += c;
  }
  if (campo !== '' || linha.length) { linha.push(campo.trim()); linhas.push(linha); }
  if (linhas.length === 0) return { cabecalho: [], registros: [] };
  return { cabecalho: linhas[0], registros: linhas.slice(1).filter((l) => l.length > 1) };
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
  return categorias(registros, coluna)
    .map((categoria) => ({ categoria, ocorrencias: contar(registros, coluna, categoria) }))
    .sort((a, b) => b.ocorrencias - a.ocorrencias);
}
