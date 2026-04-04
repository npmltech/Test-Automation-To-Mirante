# Test-Automation-To-Mirante

> Guia de execução e entendimento do projeto em linguagem simples, pensado para toda a equipe.

---

## O que é este projeto

Este repositório foi criado para resolver desafios técnicos de QA com automação, cobrindo:

- Testes **Web** em navegador com Selenium + Cucumber.
- Testes **API REST** com RestAssured + Cucumber.
- Relatórios de execução com **Allure** e **Cluecumber**.

Este README foi escrito em linguagem simples para que qualquer pessoa do time consiga instalar, executar e entender o projeto.

### Comparação rápida: API x Web

| Aspecto | API | Web |
|---|---|---|
| Foco do teste | Contrato e regra de negócio | Comportamento da interface |
| Camada principal | Request + BO/Model | Page Object + WebDriver |
| Validação comum | Código de status, JSON, dados | Tela, elementos, navegação |
| Quando usar | Integrações de serviço | Jornada do usuário |

Para aprofundamento técnico (fluxos, Model/BO/Error e comparação completa), veja `KNOWLEGDE.md`.

---

## Por que esses testes existem

### Exercício Mirante (base)

Objetivo: provar um fluxo Web simples de ponta a ponta (pesquisa no Google e validação de navegação).

Conceitos treinados:
- navegação de páginas;
- interação com elementos;
- validação funcional simples;
- estrutura de automação com Cucumber.

### Dog API

Objetivo: validar integração com API pública (`dog.ceo`) com cenários positivos e negativos.

Conceitos treinados:
- status code;
- contrato básico de resposta;
- validação de listas e campos;
- tratamento de erro (`404` para raça inexistente).

### Blog do Agi

Objetivo: validar a busca de artigos pela lupa no blog.

Conceitos treinados:
- busca por palavra-chave;
- resultado com e sem retorno;
- busca por termo composto;
- buscas consecutivas com atualização de resultados;
- cenário com `Esquema do Cenário` e `Exemplos`;
- execução `headless` para uso em CI/pipeline.

Atualmente, a suíte do Blog do Agi cobre 5 cenários funcionais relevantes.

### Testes de performance (JMeter)

Para execução e análise dos cenários de carga/pico, use estes atalhos:

- Guia principal (instalação, execução CLI/GUI e CI): `testes-carga-pico/JMETER.md`
- Guia rápido (execução objetiva): `testes-carga-pico/GUIA-RAPIDO-EXECUCAO.md`
- Plano de teste JMeter (arquivo `.jmx`): `testes-carga-pico/site-de-viagens.jmx`
- Exemplo de relatório consolidado: `testes-carga-pico/results/relatorio-execucao-blazedemo-20260404.md`

O novo plano `site-de-viagens.jmx` já está incorporado com dois perfis no mesmo arquivo:

- `TG - Carga Sustentada 250 RPS`
- `TG - Teste Pico +250 RPS`

Também foi mantida a configuração recomendada para execução em non-GUI, com listeners de alto consumo desabilitados por padrão.

Boas práticas já documentadas no guia principal:

- configuração segura do `jmeter.properties` para reduzir consumo de disco/memória;
- geração de relatório HTML com `-e -o` em diretórios dedicados por execução;
- uso de variáveis globais via `-J...` alinhadas ao `User Defined Variables` do plano;
- ajuste de heap (`HEAP`) para testes mais pesados;
- uso da GUI apenas para depuração, com listeners pesados desabilitados.

---

## Stack e versões

| Item | Versão |
|---|---|
| Java | 25 |
| Maven Wrapper | 3.x |
| Selenium | 4.33.0 |
| Cucumber | 7.22.1 |
| JUnit | 5.12.2 |
| RestAssured | 5.5.2 |
| Allure adapters | 2.33.0 |
| Allure CLI | 2.38.1 |

---

## Arquitetura (visão simples)

### Camada Web

- `src/test/resources/features/ui/` -> cenários em Gherkin (português)
- `src/test/java/.../ui/step/definitions/` -> definições de passos (`@Dado`, `@Quando`, `@Então`)
- `src/test/java/.../ui/pages/` -> Page Objects (seletores e ações de tela)
- `src/main/java/.../web/...` -> criação e ciclo de vida do WebDriver

### Camada de API

- `src/test/resources/features/api/` -> cenários de API
- `src/test/java/.../api/step/definitions/` -> definições de passos de API
- `src/test/java/.../api/domain/...` -> request classes, modelos BO, exceções de domínio

### Execução Cucumber

- `src/test/java/br/com/automation/project/runner/CucumberRunner.java`
  - carrega features
  - aplica filtro por tag
  - gera relatórios

---

## Estrutura resumida do projeto

```text
src/
  main/java/br/com/automation/project/
    utils/
    web/
  test/java/br/com/automation/project/
    api/
    ui/
    runner/
  test/resources/
    features/
      api/
      ui/
    json-repo/urls.json
```

---

## Instalação do Java (JDK 25)

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-25-jdk -y
java -version
javac -version
```

### Windows

1. Instale o JDK 25 (Temurin/Oracle).
2. Configure `JAVA_HOME`.
3. Adicione `%JAVA_HOME%\\bin` no `PATH`.
4. Valide no CMD:

```cmd
java -version
javac -version
```

### macOS

```bash
brew update
brew install openjdk@25
java -version
javac -version
```

---

## Instalação do Maven

Este projeto já tem **Maven Wrapper** (`mvnw` e `mvnw.cmd`).

Isso significa que você **não precisa instalar Maven global** para rodar.

Mesmo assim, se quiser Maven global:

### Linux

```bash
sudo apt install maven -y
mvn -version
```

### Windows

1. Instale Apache Maven.
2. Configure `MAVEN_HOME`.
3. Adicione `%MAVEN_HOME%\\bin` no `PATH`.
4. Valide:

```cmd
mvn -version
```

### macOS

```bash
brew install maven
mvn -version
```

---

## Primeiro uso (passo a passo)

### 1) Clonar projeto

```bash
git clone <url-do-repositorio>
cd Test-Automation-To-Mirante
```

### 2) Verificar ambiente

```bash
java -version
./mvnw -version
```

### 3) Executar uma suíte simples

```bash
./mvnw verify -Dcucumber.filter.tags="@dog_api"
```

---

## Como implementar um novo teste de API (padrão do projeto)

Passo a passo declarativo:

1. **Descreva o comportamento** em Gherkin na pasta `src/test/resources/features/api/`.
2. **Modele o JSON de sucesso** com classes BO (`model`) usando Jackson Databind (`@JsonProperty`).
3. **Modele o JSON de erro** em BO de erro para cenários negativos (`404`, `500`, etc.).
4. **Implemente a chamada HTTP** na classe de request (`domain/<dominio>/request`) com RestAssured + `RequestSpecBuilder`.
5. **Implemente as definições de passos** em `step/definitions`, focando em regra de negócio (status, contrato e valores esperados).
6. **Execute por tag** e evolua os cenários (happy path + negativos).

> **Importante:** ao revisar ou editar arquivos `.feature`, tenha cuidado com as frases dos passos (`Dado`, `Quando`, `Então`, `E`). Essas linhas ficam vinculadas diretamente às Step Definitions em Java. Ajustes linguísticos seguros devem priorizar `Funcionalidade:`, `Cenário:`, `Esquema do Cenário:`, descrições narrativas e blocos de `Exemplos`, desde que isso não altere o vínculo com os passos implementados.

Exemplo real no projeto:
- Feature: `src/test/resources/features/api/005_Dog_Api.feature`
- Steps: `src/test/java/br/com/automation/project/api/domain/dog/step/definitions/DogApiSD.java`

Leitura detalhada (conceitos + arquitetura): `KNOWLEGDE.md`

---

## Como implementar um novo teste Web Browser (padrão do projeto)

Passo a passo declarativo:

1. **Escreva o comportamento esperado** na `.feature` em `src/test/resources/features/ui/`.
2. **Crie a Page Object** com locators e ações da tela em `src/test/java/.../ui/pages/`.
3. **Registre a página** no `PageRegister` para reuso por qualquer classe que estenda esse register.
4. **Crie as definições de passos** em `src/test/java/.../ui/step/definitions/` (os passos orquestram; a Page resolve HTML/locators).
5. **Configure a URL alvo** em `src/test/resources/json-repo/urls.json`.
6. **Execute por tag** (`@ui`, `@agi_blog`, etc.) e ajuste locators para robustez.

> **Importante:** a mesma regra vale para features Web. Títulos e descrições podem ser refinados para PT-BR, mas as frases dos passos só devem ser alteradas quando a Step Definition correspondente também for atualizada.

Exemplo real no projeto:
- Feature: `src/test/resources/features/ui/004_Agi_Blog_Search.feature`
- Steps: `src/test/java/br/com/automation/project/ui/step/definitions/AgiBlogSearchSD.java`
- Page: `src/test/java/br/com/automation/project/ui/pages/AgiBlogSearchPG.java`

Leitura detalhada (BDD, Cucumber, Page Object, Register, MVC/BO/Error, Builder): `KNOWLEGDE.md`

---

## Comandos de execução (juntos e separados)

### Comando principal do desafio

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true
```

### Formatação

```bash
./mvnw spotless:check
./mvnw spotless:apply
```

### Rodar tudo

```bash
./mvnw clean verify
```

### Rodar apenas API

```bash
./mvnw verify -Dcucumber.filter.tags="@api"
./mvnw verify -Dcucumber.filter.tags="@dog_api"
./mvnw verify -Dcucumber.filter.tags="@star_wars"
./mvnw verify -Dcucumber.filter.tags="@api_cn_b1"
./mvnw verify -Dcucumber.filter.tags="@api_cn_c1"
```

Observação:
- `@api` executa toda a suíte de API.
- `@dog_api` executa apenas os cenários da Dog API.
- `@star_wars` executa os cenários da Star Wars API.
- Para cenários da API Person e da geração de token, use as tags específicas do cenário (`@api_cn_*`), pois essas features não possuem uma tag exclusiva de suíte no momento.

### Rodar apenas Web

```bash
./mvnw verify -Dcucumber.filter.tags="@ui"
./mvnw verify -Dcucumber.filter.tags="@agi_blog"
```

### Rodar Web em headless

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true
./mvnw verify -Dcucumber.filter.tags="@ui" -Dheadless=true
```

Também é possível habilitar headless no arquivo de propriedades (`headless=true`) sem passar `-Dheadless=true` no comando.

### Combinar tags (AND/OR)

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog and @dog_api" -Dheadless=true
```

- A expressão `and` executa apenas cenários que tenham as duas tags ao mesmo tempo.
- No estado atual do projeto, `@agi_blog` e `@dog_api` estão em suítes diferentes, então esse filtro pode retornar `0 Scenarios`.
- Para executar cenários de uma tag ou outra no mesmo comando, use:

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true
```

### Rodar um cenário específico por tag de cenário

```bash
./mvnw verify -Dcucumber.filter.tags="@ui_cn_d5" -Dheadless=true
```

Esse é o comando principal do desafio técnico (Dog API + Blog do Agi):

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true
```

---

## GitHub Actions (CI)

Arquivo do workflow:
- `.github/workflows/tests-api-web.yml`

### Quando o pipeline roda

- `push` apenas na branch `main`
- `pull_request`

### Ações e compatibilidade de runtime

- `actions/checkout@v5`
- `actions/setup-java@v5`
- `actions/upload-artifact@v4`
- Variável de compatibilidade ativa no workflow:
  - `FORCE_JAVASCRIPT_ACTIONS_TO_NODE24=true`

Essa configuração mantém o pipeline compatível com a transição de runtime JavaScript do GitHub Actions para Node.js 24.

### O que o pipeline executa

- Job `api-dog`
  - Comando: `./mvnw verify -Dcucumber.filter.tags="@dog_api"`
  - Objetivo: validar somente os cenários da Dog API.

- Job `web-agi-blog`
  - Comando: `./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true`
  - Objetivo: validar somente os cenários Web do Blog do Agi em modo headless.

- Job `jmeter-blazedemo`
  - Comando: execução do arquivo `testes-carga-pico/site-de-viagens.jmx` em modo non-GUI no runner Ubuntu.
  - Objetivo: validar o plano de carga/pico e gerar artefatos de performance no CI.
  - Parâmetros: controle de carga por variáveis `-J` para manter reprodutibilidade do plano.

### Artefatos publicados no GitHub Actions

Ao final de cada job (inclusive quando há falha), o workflow publica:

- `target/allure-results`
- `target/site/allure-maven-plugin`
- `target/cluecumber-reports`
- `target/cucumber-reports`
- `testes-carga-pico/results/ci-load/results.jtl`
- `testes-carga-pico/results/ci-load/jmeter.log`
- `testes-carga-pico/results/ci-load/report`
- `testes-carga-pico/results/ci-spike/results.jtl`
- `testes-carga-pico/results/ci-spike/jmeter.log`
- `testes-carga-pico/results/ci-spike/report`
- `testes-carga-pico/results/relatorio-execucao-blazedemo-20260404.md`

Isso facilita a análise de erro sem precisar reproduzir localmente antes.

---

## Relatórios

### Allure

```bash
./mvnw allure:report
```

Saída:
- `target/site/allure-maven-plugin/index.html`

Para abrir com servidor local:

```bash
./mvnw allure:serve
```

### Cluecumber

Gerado automaticamente após `verify` em:
- `target/cluecumber-reports/index.html`

---

## Configuração importante

### `webdriver.properties`

Arquivo: `src/test/resources/config/webdriver.properties`

```properties
browser.name=chrome_v2
browser.version=latest
headless=false
```

- `headless=false`: abre o navegador visualmente.
- `headless=true`: executa o navegador em segundo plano (sem interface gráfica).
- Se você passar `-Dheadless=true` no Maven, esse valor tem prioridade sobre o arquivo de propriedades.

### Cuidados ao editar arquivos `.feature`

- Pode ajustar com segurança:
  - `Funcionalidade:`
  - `Cenário:`
  - `Esquema do Cenário:`
  - descrições narrativas em texto livre
- Exige cuidado redobrado:
  - linhas iniciadas por `Dado`, `Quando`, `Então` e `E`

Essas linhas são associadas diretamente aos métodos anotados nas Step Definitions. Se o texto do passo mudar, o Cucumber pode deixar de encontrar a implementação em Java.

### Tags Allure no Gherkin

- O projeto usa labels do Allure diretamente nas tags dos cenários (`.feature`).
- Padrão aplicado:
  - `@allure.label.owner:...`
  - `@allure.label.epic:...`
  - `@allure.label.feature:...`
  - `@allure.label.story:...`
  - `@allure.label.severity:critical|normal|minor|trivial|blocker`
- Exemplo:

```gherkin
@ui_cn_d1 @allure.label.severity:critical @allure.label.story:ui_cn_d1
Cenário: Pesquisar por um termo válido e exibir artigos relacionados
```

- Recomendação: mantenha as tags funcionais (`@api`, `@dog_api`, `@ui`, `@agi_blog`) para filtragem de execução e use as tags `@allure.label.*` para enriquecer relatórios.

### `urls.json`

Arquivo: `src/test/resources/json-repo/urls.json`

```json
{
  "google": "https://www.google.com",
  "my_heroku_api_url": "https://demo-spring-boot-npml.herokuapp.com/",
  "star_wars_url": "https://swapi.dev/api/",
  "todo_list": "https://todomvc-socketstream.herokuapp.com",
  "agi_blog": "https://blogdoagi.com.br/",
  "dog_api_url": "https://dog.ceo/api/"
}
```

### JMeter: parâmetros globais e memória

Arquivo principal:

- `testes-carga-pico/site-de-viagens.jmx`

Esse plano recebe parâmetros por propriedades (`__P`) e pode ser controlado por linha de comando via:

- `-JbaseUrl`
- `-JloadThreads`
- `-JloadRampUp`
- `-JloadDuration`
- `-JspikeThreads`
- `-JspikeRampUp`
- `-JspikeDelay`
- `-JspikeDuration`

Exemplo de ajuste de heap para execução pesada:

```bash
export HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
```

No Windows PowerShell:

```powershell
$env:HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
```

Guia completo com boas práticas de `jmeter.properties`, relatório HTML e Ultimate Thread Group:

- `testes-carga-pico/JMETER.md`

---

## Dúvidas comuns

- "Preciso instalar Maven global?"
  - Não. Use `./mvnw`.
- "Como rodo sem abrir navegador?"
  - Use `-Dheadless=true` no comando Maven.
- "Onde vejo erros conhecidos?"
  - Em `TROUBLESHOOTING.md`.

---

## Documentação complementar

- `CHANGELOG.md` -> histórico de mudanças
- `TROUBLESHOOTING.md` -> problemas conhecidos e soluções
- `KNOWLEGDE.md` -> guia de conhecimento técnico (conceitos e implementação)
- `testes-carga-pico/JMETER.md` -> guia principal dos testes de carga com JMeter
- `testes-carga-pico/GUIA-RAPIDO-EXECUCAO.md` -> execução rápida dos cenários de carga e pico
- `testes-carga-pico/site-de-viagens.jmx` -> plano de teste de performance (carga e pico)
