# Analisador CSV — Projeto Integrador (Evasão Escolar)

![JavaScript](https://img.shields.io/badge/JavaScript-ES2022-yellow?logo=javascript&logoColor=white)
![Node](https://img.shields.io/badge/node-%3E%3D22.12-green?logo=node.js&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-green)

Aplicação web para contar ocorrências por categoria, cruzar variáveis e calcular estatística descritiva a partir de CSV. Ela atualiza o projeto integrador original de **Java Swing + JFreeChart + MySQL** (2022); os 11 fontes Java originais permanecem em [`java/`](java/).

## Executar localmente

Requer [Node.js 22.12 ou superior](https://nodejs.org/). Não há dependências externas nem etapa de instalação:

```bash
npm start
```

Abra <http://127.0.0.1:3000/> no navegador. O servidor aceita apenas conexões locais. Para usar outra porta, defina a variável `PORT`, por exemplo `PORT=8080 npm start`.

1. Carregue um arquivo `.csv` ou escolha **Usar Evasao.csv** para abrir o exemplo incluído.
2. Escolha a **variável principal** para ver as ocorrências por categoria, o gráfico e as estatísticas.
3. Se desejar, escolha outra variável e um valor para cruzar os dados.

O CSV precisa ter cabeçalho e linhas com a mesma quantidade de campos. O leitor aceita campos entre aspas, aspas duplicadas e quebras de linha dentro de campos. Linhas vazias são ignoradas; nomes de cabeçalhos vazios ou duplicados são ajustados automaticamente. O limite é **10 MB, 200 colunas e 50 mil registros**.

## Privacidade

O arquivo selecionado é lido e analisado dentro do navegador. A aplicação não envia os dados para o servidor, não os persiste e não guarda resultados. O servidor local disponibiliza somente os arquivos necessários à interface e o CSV de exemplo.

## Estatística calculada

As métricas se baseiam nas contagens de cada categoria e seguem o `Estatistica.java`:

| Métrica | Fórmula |
|---|---|
| Média, mediana e moda | média aritmética; mediana ordenada; moda por frequência |
| Soma dos elementos e Σx² | `Σx` e `Σx²` |
| Variância amostral | `s² = (Σx² − (Σx)²/n) / (n−1)` — corrige o uso de `(Σx)²` no lugar de `Σx²` no original |
| Desvio padrão | `√s²` |
| Coeficiente de variação de Pearson | `(s / média) × 100%` |
| Coeficiente de assimetria | primeira fórmula de Pearson: `(média − moda) / s` |

O gráfico usa SVG sem dependências externas. Se houver mais de 40 categorias, ele mostra as 40 mais frequentes e a tabela mantém a lista completa.

## Estrutura

```
projeto_integrador_analisador_csv/
├── index.html                # interface e controles acessíveis
├── js/
│   ├── estatistica.js        # fórmulas portadas de Estatistica.java
│   ├── csv.js                # leitura, contagem e cruzamento
│   └── app.js                # interface e gráfico SVG
├── test/estatistica-test.mjs # suíte disponível via npm test
├── data/Evasao.csv           # CSV de exemplo
├── server.js                 # servidor Node local, sem dependências
├── java/                     # projeto original de 2022
└── Biblioteca/               # materiais do projeto original
```

### Executar a versão Java original

```bash
javac -cp 'Biblioteca/*' -d out $(find java -name '*.java')
java -cp 'out:Biblioteca/*' frontEnd.Aplicativo
```

## Licença

MIT — veja [LICENSE](LICENSE).
