# Melhorias do Analisador CSV

## 0. Como usar

- Execute os itens na ordem P0 → P1 → P2 → P3.
- Marque um item como concluído somente depois da verificação indicada.
- A suíte de testes não foi executada nesta rodada.

## 1. Diagnóstico executivo

A aplicação web lê CSVs locais no navegador e produz contagens, cruzamentos, estatísticas e um gráfico. O servidor agora serve somente os seis recursos necessários e fica limitado ao loopback. O parser valida estrutura e limites, enquanto a interface mostra erros, estados e resultados com DOM seguro. O README acadêmico atual não contém e-mails nem telefones nos padrões verificados; commits anteriores do repositório ainda preservam as versões antigas do arquivo.

## 2. Tabela de prioridades

| Prioridade | Itens | Estado |
|---|---|---|
| P0 | — | Nenhum item identificado |
| P1 | SEC-01, SEC-02, SEC-03, BUG-01 | Concluídos |
| P2 | UX-01, UX-02 | Concluídos |
| P3 | DOC-01 | Concluído |

## 3. Segurança

### SEC-01 · Restringir o servidor local [P1]

- **Arquivo:** `server.js:6-28,39-92`
- **Evidência:** o servidor anterior servia caminhos do repositório sem allowlist, aceitava qualquer interface e não limitava o método HTTP.
- **Mudança:** allowlist dos recursos da interface, caminhos decodificados e canônicos, GET/HEAD, loopback e cabeçalhos de segurança.
- **Aceite:** somente os seis recursos declarados são servidos; o servidor escuta em `127.0.0.1` e recusa outros métodos.
- **Verificação:** `node --check server.js`; inspecionar `ARQUIVOS_PUBLICOS`, a validação de método, `realpath`, cabeçalhos e `server.listen`.
- **Estado:** [x] concluído.

### SEC-02 · Tratar valores CSV como texto [P1]

- **Arquivo:** `js/app.js:49-53,130-170,204-228`
- **Evidência:** cabeçalhos e categorias fornecidos pelo CSV eram interpolados como HTML em opções, tabelas e SVG.
- **Mudança:** criar elementos DOM/SVG e preencher conteúdo com `textContent`; inserir lotes usando `DocumentFragment`.
- **Aceite:** nenhum valor do CSV é interpretado como markup na interface.
- **Verificação:** `node --check js/app.js` e `! rg -n 'innerHTML|outerHTML|insertAdjacentHTML|document\.write' js`.
- **Estado:** [x] concluído.

### SEC-03 · Remover contatos do documento público [P1]

- **Arquivo:** `Biblioteca/Documentos/README.md`
- **Evidência:** a versão atual continha cinco e-mails e cinco telefones pessoais em um repositório público.
- **Mudança:** substituir os contatos por marcadores de remoção, mantendo o contexto do documento.
- **Aceite:** os padrões de e-mail e telefone verificados retornam zero ocorrências no arquivo atual.
- **Verificação:** varredura local com expressões de e-mail e telefone; resultado registrado nesta rodada: 0 e-mails e 0 telefones.
- **Estado:** [x] concluído na árvore atual. Versões anteriores permanecem acessíveis no histórico; ele não foi reescrito.

## 4. Bugs

### BUG-01 · Validar a estrutura do CSV [P1]

- **Arquivo:** `js/csv.js:4-118`
- **Evidência:** o leitor descartava registros de CSVs de uma coluna e aceitava aspas sem fechamento ou linhas com largura inconsistente.
- **Mudança:** validar aspas e largura, aceitar uma coluna, normalizar cabeçalhos vazios/duplicados e limitar a 200 colunas e 50 mil registros.
- **Aceite:** registros malformados geram erro com linha de origem; registros de uma coluna são preservados; linhas físicas vazias são ignoradas.
- **Verificação:** `node --check js/csv.js`; inspeção estática do parser; CSV de exemplo conferido com Python: 1.235 registros, 7 colunas e nenhuma largura inconsistente.
- **Estado:** [x] concluído.

## 5. Qualidade e experiência

### UX-01 · Dar feedback e impor limites de entrada [P2]

- **Arquivo:** `js/app.js:7-27,39-47,235-274`
- **Evidência:** não havia estados de leitura/erro, limites de tamanho ou tratamento para CSV vazio e inválido.
- **Mudança:** validar extensão e tamanho, reportar carregamento e erros acessivelmente, tratar resultados vazios e ignorar leituras assíncronas antigas.
- **Aceite:** a pessoa recebe mensagens sobre progresso, falha, limites e análise concluída, sem substituir os resultados válidos em caso de falha de um novo arquivo.
- **Verificação:** `node --check js/app.js`; revisar os caminhos de erro, vazio e carregamento.
- **Estado:** [x] concluído.

### UX-02 · Corrigir módulo, teclado e telas pequenas [P2]

- **Arquivo:** `index.html:18-78`; `style.css:1-108`
- **Evidência:** `app.js` usa imports, mas era carregado como script clássico; o upload não recebia foco de teclado e tabelas/gráfico não tinham regiões roláveis acessíveis.
- **Mudança:** carregar como módulo, associar rótulos, foco visível, regiões roláveis com nome acessível e layout responsivo.
- **Aceite:** o HTML aponta para `type="module"`; upload e controles recebem foco; tabelas e gráfico podem ser rolados em viewport estreito.
- **Verificação:** `git diff --check`; conferir referências de IDs entre o app e HTML; revisar `type="module"`, foco e media query.
- **Estado:** [x] concluído.

## 6. DevOps

Nenhum item adicional de CI ou empacotamento foi identificado para este app local sem dependências externas. A execução permanece em Node.js 18+.

## 7. Documentação

### DOC-01 · Documentar execução e limites reais [P3]

- **Arquivo:** `README.md`
- **Evidência:** as instruções incluíam uma instalação sem efeito e não explicavam os limites de leitura nem o escopo local do servidor.
- **Mudança:** documentar `npm start`, Node 18+, URL local, privacidade, limites do parser, gráfico e estrutura atual.
- **Aceite:** instruções e limites correspondem a `package.json`, `server.js`, `js/csv.js` e à interface.
- **Verificação:** revisão estática comparando README, scripts do `package.json`, allowlist e constantes de limite.
- **Estado:** [x] concluído.

## 8. Ordem de execução

1. SEC-01, SEC-02, SEC-03 e BUG-01 (P1).
2. UX-01 e UX-02 (P2).
3. DOC-01 (P3).

Todos os itens acima foram implementados e verificados estaticamente. Commits: `0ed9809` (código/interface) e `5a7429d` (documentação/privacidade).

## 9. Fora de escopo

- Executar a suíte existente; ela não foi solicitada nesta rodada.
- Reescrever o histórico Git, que ainda contém versões anteriores do documento com contatos.
- Alterar os fontes Java originais, o banco histórico ou o dataset de exemplo.

## 10. Definição de pronto

- Itens aplicáveis deste plano marcados como concluídos após verificações estáticas.
- `node --check` nos scripts JavaScript, referências da interface e estrutura do exemplo conferidas, varredura atual de contatos zerada e `git diff --check` sem erros.
- Alterações commitadas e enviadas a `origin/master`.
