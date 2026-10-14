# Plan — projeto_integrador_analisador_csv

## 1. Visão geral

- **Nome:** Analisador CSV — Projeto Integrador (Evasão Escolar)
- **Descrição:** Aplicação web local para análise estatística de CSVs (contagem por categoria, cruzamento de variáveis, estatística descritiva e gráfico SVG), convertida do projeto integrador original em Java Swing + JFreeChart + MySQL (2022).
- **Problema que resolve:** permite analisar CSVs de forma privada e local (dados processados no navegador, sem envio a servidor), exibindo estatísticas sem depender de planilhas ou de instalar o ambiente Java original.
- **Público-alvo:** avaliadores de portfólio, estudantes e qualquer pessoa que precise de estatística descritiva rápida sobre CSVs locais.
- **Visão de longo prazo:** servir como vitrine de conversão Java→JavaScript fiel, com estatística validada contra o código original e suíte de testes preservada.
- **Estado atual:** versão web 2.0.0 funcional e endurecida (servidor restrito a loopback + allowlist, parser CSV validado, DOM seguro). Sem dependências externas.

## 2. Arquitetura e tecnologias

- **Frontend:** HTML/CSS/JS vanilla em módulos ES (`index.html`, `js/app.js`, `js/csv.js`, `js/estatistica.js`), gráfico em SVG sem bibliotecas.
- **Backend:** `server.js` Node puro (`type: module`), sem dependências; aceita apenas GET/HEAD em `127.0.0.1` e serve somente os seis recursos da interface + CSV de exemplo.
- **Dados:** `data/Evasao.csv` (1.235 registros, 7 colunas) como exemplo; nenhum dado é persistido.
- **Legado preservado:** `java/` (11 fontes de 2022) e `Biblioteca/` (materiais originais, contatos já removidos).
- **Limitações:** análise síncrona no navegador limitada a 10 MB / 200 colunas / 50 mil registros; não há upload remoto, banco ou multiusuário — escopo intencional.

## 3. Funcionalidades existentes

- [x] Leitura de CSV no navegador com parser robusto (aspas, multilinha, largura consistente, normalização de cabeçalhos) — validado estaticamente.
- [x] Contagem por categoria, cruzamento de variáveis e tabela de resultados — implementado.
- [x] Estatística descritiva (média, mediana, moda, Σx, Σx², variância amostral, desvio, CV de Pearson, assimetria) — portada e corrigida em relação ao `Estatistica.java`.
- [x] Gráfico SVG com top-40 categorias — implementado.
- [x] Estados de carregamento/erro/vazio acessíveis e proteção contra leituras assíncronas antigas — implementado.
- [x] Suíte `test/estatistica-test.mjs` disponível via `npm test` — presente, **não executada** por restrição vigente.
- [ ] Integração contínua — **implementado nesta sessão** (workflow estático Node 22/24).

## 4. Objetivos futuros

- **OBJ-01 (P2, concluído nesta sessão):** criar CI estática em GitHub Actions (sintaxe JS em Node 22/24, JSON, audit, diff-check).
- **OBJ-02 (P3, recomendação):** executar a suíte `npm test` na CI quando a restrição de testes for revogada (adicionar job separado, evidenciando cobertura da estatística).
- **OBJ-03 (P3, recomendação):** publicar demo estático via GitHub Pages (o app roda 100% client-side com o CSV de exemplo embutido ou selecionável).
- **OBJ-04 (P3, recomendação):** exportar resultados (CSV/JSON) e imprimir gráfico SVG — aumenta utilidade didática.

## 5. Melhorias técnicas

- **Infraestrutura:** ✅ workflow `.github/workflows/ci.yml` adicionado; piso Node elevado para 22.12 (LTS ativa; 18/20 fora da linha de suporte do workspace).
- **Documentação:** badge e instrução de runtime atualizados no README.
- **Segurança:** manter allowlist do servidor; nenhuma nova superfície foi criada.
- **Qualidade:** quando testes forem permitidos, rode `npm test` e registre a saída no ledger.

## 6. Roadmap

- **Fase 1 — Correções críticas:** nenhuma pendente (P0 vazio no `MELHORIAS.md`).
- **Fase 2 — Estabilização:** ✅ CI estática (esta sessão); ☐ suite de testes na CI (OBJ-02).
- **Fase 3 — Melhorias funcionais:** exportação de resultados (OBJ-04).
- **Fase 4 — Novas funcionalidades:** demo público (OBJ-03).
- **Fase 5 — Longo prazo:** nenhum plano de expansão de escopo além do papel de vitrine acadêmica.

## 7. Backlog priorizado

1. OBJ-02 — executar `npm test` na CI (P3, baixa complexidade, depende da revogação da regra de testes).
2. OBJ-03 — GitHub Pages (P3, baixa).
3. OBJ-04 — exportação de resultados (P3, média).

## 8. Riscos e pendências

- Histórico Git antigo de `Biblioteca/Documentos/README.md` ainda contém contatos pessoais em commits passados; a limpeza do histórico exigiria reescrita coordenada — **decisão registrada: não reescrever**.
- Suíte de testes nunca executada na esteira atual (regra vigente); comportamento runtime validado apenas estaticamente.

## 9. Ideias adicionais

- Script de conversão automática de outros projetos integradores Java da turma, reaproveitando `js/estatistica.js` como módulo publicável.

---
*(Gerado em 2026-10-14 pelo agente orquestrador — fluxo de auditoria/planejamento/execução)*
