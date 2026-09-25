# Analisador CSV — Projeto Integrador (Evasão Escolar)

![JavaScript](https://img.shields.io/badge/JavaScript-ES2022-yellow?logo=javascript&logoColor=white)
![Node](https://img.shields.io/badge/node-%3E%3D18-green?logo=node.js&logoColor=white)
![Tests](https://img.shields.io/badge/testes-18%2F18%20passando-brightgreen)
![License](https://img.shields.io/badge/license-MIT-green)

Aplicação **web** que lê arquivos **CSV**, conta ocorrências por categoria, cruza variáveis
e calcula **estatística descritiva** com gráfico — versão (2026) do projeto integrador
**Java Swing + JFreeChart + MySQL** de 2022, com os 11 fontes originais preservados em [`java/`](java/).

## Instalação e execução

Requer [Node.js 18+](https://nodejs.org/). Sem dependências externas:

```bash
npm install   # no-op (zero dependências)
npm start     # abre http://localhost:3000
npm test      # 18 testes: estatística com valores conhecidos, parse de CSV, cruzamento
```

1. Carregue um CSV (ou clique em **Usar Evasao.csv** — exemplo real embutido em `data/`).
2. Escolha a **variável principal** → ocorrências por categoria + gráfico + estatísticas.
3. Opcionalmente **cruze com outra variável** (ex.: `SEXO_ALUNO × SITUACAO_CIVIL_ALUNO = Solteiro(a)`,
   como no original: "mulheres ∩ solteiras").

## O que a versão web calcula (fiel ao `Estatistica.java`)

| Métrica | Fórmula |
|---|---|
| Média / Mediana / Moda | aritmética; mediana ordenada; moda por frequência |
| Soma dos elementos / Σx² | `Σx` e `Σx²` |
| Variância amostral | `s² = (Σx² − (Σx)²/n) / (n−1)` — **correção**: o original usava `(Σx)²` no lugar de `Σx²` (bug documentado) |
| Desvio padrão | `√s²` |
| Coef. de variação de Pearson | `(s / média) × 100%` |
| Coef. de assimetria | 1ª fórmula de Pearson: `(média − moda) / s` |

O gráfico de barras em **SVG puro** substitui o JFreeChart — continua zero-dependência.

> **MySQL:** a persistência do original (`mysql/Dao.java`) fica no `java/` histórico; a versão web
> é 100% client-side (o dado não sai do navegador).

## Estrutura

```
projeto_integrador_analisador_csv/
├── index.html               # UI: carga de CSV, seleção de variáveis, gráfico, tabelas
├── js/
│   ├── estatistica.js       # port do Estatistica.java (fórmulas acima)
│   ├── csv.js               # parse + contagem + cruzamento (Leitor/ContaOcorrencias/Selecionador)
│   └── app.js               # UI e gráfico SVG
├── test/estatistica-test.mjs# 18 testes (npm test)
├── data/Evasao.csv          # exemplo real (cópia do dataset original)
├── server.js                # servidor estático Node puro (zero deps)
├── java/                    # ✔ PROJETO ORIGINAL 2022 preservado (11 fontes)
└── Biblioteca/              # jars, docs acadêmicos, CSVs originais
```

### Rodar a versão Java original (histórica)

```bash
javac -cp 'Biblioteca/*' -d out $(find java -name '*.java')
java -cp 'out:Biblioteca/*' frontEnd.Aplicativo
```

## Licença

MIT — veja [LICENSE](LICENSE).
