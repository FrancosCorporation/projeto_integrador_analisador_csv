# Analisador CSV — Projeto Integrador

## 🐳 Instalação e Execução (Docker) — recomendado

### Pré-requisitos
- [Docker](https://docs.docker.com/get-docker/) + Docker Compose

### Rodar com Docker
```bash
docker compose up --build
```
```bash
docker run --rm -v $(pwd):/src -w /src maven:3.8-openjdk-17 sh -c 'mvn -q dependency:copy -Dartifact=org.jfree:jfreechart:1.5.4 -DoutputDirectory=lib && javac -cp "lib/*" -d out $(find src -name "*.java")'
```

### Sem Docker (local)
```bash
# Requer JDK + JFreeChart (lib/jfreechart-1.5.4.jar)
javac -cp 'lib/*' -d out $(find src -name '*.java')
java -cp 'out:lib/*' frontEnd.Aplicativo
```

Aplicação desktop em **Java** que lê arquivos **CSV**, calcula estatísticas e
exibe os resultados em gráficos interativos, com persistência em **MySQL**.

![Java](https://img.shields.io/badge/Java-8-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-orange?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![JFreeChart](https://img.shields.io/badge/JFreeChart-1.5.0-blue?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)
![Status](https://img.shields.io/badge/status-acad%C3%AAmico-lightgrey?style=flat-square)

## Sobre

**Projeto integrador acadêmico** (Senac) desenvolvido em equipe em 2022 com o
tema "Analisador CSV em Java para Data Science". A aplicação recebe um arquivo
CSV de registros (por exemplo, dados de RH/evasão), calcula métricas
estatísticas e gera gráficos para apoiar a análise.

## Funcionalidades

Comprovadas pelo código em `src/`:

- **Leitura de CSV** com seleção de arquivo (`frontEnd/Leitor`,
  `Selecionador`) e tela de abertura (`Splash`).
- **Estatística descritiva** (`estatistica/Estatistica`,
  `ContaOcorrencias`) — contagem de ocorrências e cálculos sobre colunas.
- **Gráficos** com JFreeChart (`grafico/Grafico`).
- **Persistência em MySQL** (`mysql/Conexao`, `Dao`,
  `ConstrutorParaOArquivo`).
- **Interface desktop** em Java Swing (`frontEnd/Aplicativo`).

## Como rodar

Requisitos: JDK 8+, MySQL e as bibliotecas em `Biblioteca/`
(`jfreechart-1.5.0.jar`, `mysql-connector-java-8.0.18.jar`).

1. Crie o banco no MySQL e ajuste as credenciais em `src/mysql/Conexao.java`.
2. Importe o projeto no Eclipse/IntelliJ (arquivos `.classpath`/`.project`
   já configurados) ou compile manualmente com os JARs de `Biblioteca/` no
   classpath.
3. Execute a classe `frontEnd.Aplicativo`.

Há um executável pronto em `Biblioteca/Documentos/Analizador Csv.jar` e uma
apresentação do projeto em `Biblioteca/Documentos/Apresentação.pptx`.

## Equipe

- Bruno Camargo Manso (chefe do projeto)
- Guilherme Mello de Santana
- Luys Fernnando Ribeiro Caetano Brasil
- Matheus Vieira Tavares
- Rodolfo Franco

## Estrutura do projeto

```
src/
├── estatistica/     # Cálculos estatísticos sobre o CSV
├── frontEnd/        # Interface Swing e leitura de arquivos
├── grafico/         # Gráficos com JFreeChart
└── mysql/           # Conexão, DAO e persistência
Biblioteca/
├── Documentos/      # Relatórios, apresentação e executável
└── *.jar            # Dependências (JFreeChart, MySQL Connector)
```

## Licença

MIT — veja [LICENSE](LICENSE).
