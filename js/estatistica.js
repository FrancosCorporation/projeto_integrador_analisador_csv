// Estatística — port do src/estatistica/Estatistica.java (projeto integrador 2022).
// Correção documentada: a variância original usava (Σx)² no lugar de Σx² —
// aqui a fórmula amostral correta: s² = (Σx² − (Σx)²/n) / (n−1).

export const soma = (xs) => xs.reduce((t, x) => t + x, 0);

export const mediaAritmetica = (xs) => soma(xs) / xs.length;

export const somaDosQuadrados = (xs) => xs.reduce((t, x) => t + x * x, 0);

export function varianciaAmostral(xs) {
  const n = xs.length;
  if (n < 2) return 0;
  return (somaDosQuadrados(xs) - Math.pow(soma(xs), 2) / n) / (n - 1);
}

export const desvioPadrao = (xs) => Math.sqrt(varianciaAmostral(xs));

// Coeficiente de Variação de Pearson (%)
export const pearson = (xs) => xs.length && mediaAritmetica(xs) !== 0
  ? (desvioPadrao(xs) / mediaAritmetica(xs)) * 100
  : 0;

export function mediana(xs) {
  const o = [...xs].sort((a, b) => a - b);
  const n = o.length;
  if (n === 0) return 0;
  return n % 2 === 1 ? o[(n + 1) / 2 - 1] : (o[n / 2 - 1] + o[n / 2]) / 2;
}

export function moda(xs) {
  if (xs.length === 0) return 0;
  const contagem = new Map();
  let melhor = xs[0], melhorN = 0;
  for (const x of xs) {
    const n = (contagem.get(x) || 0) + 1;
    contagem.set(x, n);
    if (n > melhorN) { melhorN = n; melhor = x; }
  }
  return melhor;
}

// Coeficiente de Assimetria de Pearson (1ª fórmula): (média − moda) / desvio padrão
export const coefAssimetria = (xs) => {
  const dp = desvioPadrao(xs);
  return dp === 0 ? 0 : (mediaAritmetica(xs) - moda(xs)) / dp;
};

export function estatisticasCompletas(xs) {
  return {
    n: xs.length,
    soma: soma(xs),
    media: mediaAritmetica(xs),
    somaQuadrados: somaDosQuadrados(xs),
    mediana: mediana(xs),
    moda: moda(xs),
    variancia: varianciaAmostral(xs),
    desvioPadrao: desvioPadrao(xs),
    pearson: pearson(xs),
    coefAssimetria: coefAssimetria(xs)
  };
}
