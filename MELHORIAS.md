# Melhorias do Analisador CSV

Objetivo: permitir que uma pessoa selecione um CSV, compare categorias e visualize estatísticas sem enviar os dados do arquivo para um servidor.

## P1 — Segurança e privacidade

- [x] **SEC-01** — Limitar `server.js:10-20,37-92` aos arquivos usados pela interface, aceitar somente GET/HEAD e escutar em loopback; validar caminho decodificado e aplicar cabeçalhos de segurança. Verificação: `node --check server.js` e revisão estática da allowlist e do caminho canônico.
- [x] **SEC-02** — Eliminar o HTML dinâmico não confiável em `js/app.js`; CSVs enviados podem conter markup em cabeçalhos e valores. Construir opções, SVG e tabelas com nós e `textContent`. Verificação: `node --check js/app.js` e busca estática sem `innerHTML`, `outerHTML` ou `insertAdjacentHTML` em `js/`.
- [x] **SEC-03** — Remover e-mails e telefones do documento público `Biblioteca/Documentos/README.md`, preservando o contexto acadêmico sem os contatos pessoais. Verificação: varredura local encontrou zero e-mails e telefones nos padrões verificados.

## P2 — Correção e experiência

- [x] **BUG-01** — Rejeitar CSVs com aspas sem fechamento ou largura de registro inconsistente e aceitar arquivos de uma coluna em `js/csv.js`; normalizar cabeçalhos vazios/duplicados, limitar colunas/registros e ignorar linhas vazias. Verificação: `node --check js/csv.js`, revisão estática do parser e conferência estrutural do CSV de exemplo.
- [x] **UX-01** — Adicionar validação de tamanho/formato, estados acessíveis de leitura/erro/resultado vazio e limites de dados em `js/app.js`. Verificação: `node --check js/app.js` e revisão estática dos caminhos de erro e vazio.
- [x] **UX-02** — Corrigir o carregamento como módulo em `index.html`, rotular os controles e tornar upload, tabelas e gráfico utilizáveis em telas pequenas e por teclado. Verificação: `git diff --check`, referências de IDs completas e inspeção estática de módulo, labels, foco e layout responsivo.

## P3 — Documentação

- [x] **DOC-01** — Atualizar `README.md` sobre uso local, limites do CSV, privacidade e disponibilidade real do aplicativo. Verificação: comparar as instruções com `package.json`, `server.js` e a interface.

## Fora deste ciclo

- Execução da suíte de testes existente; esta rodada fará verificações estáticas, sem executar testes.
