# Changelog

Todas as mudanças relevantes deste ciclo de evolução do projeto foram documentadas aqui.

---

## [Unreleased] - 2026-04-04 — Normalização de chaves JSON, robustez de seletores e refatoração de pom

### Fixed
- Chaves de URL normalizadas para minúsculas em todas as classes que referenciam `urls.json`, alinhando o código com as chaves reais definidas no arquivo:
  - `AccessTokenHerokuApi`: `"My_Heroku_API_Url"` → `"my_heroku_api_url"`.
  - `PersonApi.deletePersonById`: `"My_Heroku_API_Url"` → `"my_heroku_api_url"`.
  - `AgiBlogSearchPG.accessAgiBlog` e `navigateToSearchResults`: `"Agi_Blog"` → `"agi_blog"`.
  - `TodoListPG.getTodoUrl`: `"TodoList"` → `"todo_list"`.
- `WebHook`: substituído `Set<String>` por `Collection<String>` para `sourceTags`, corrigindo o tipo de retorno de `Scenario.getSourceTagNames()` e evitando cast silencioso.

### Improved
- `AgiBlogSearchPG`: seletores de resultados de busca reescritos com múltiplos fallbacks CSS (`article h2.entry-title a`, `h2.entry-title a`, `h2.wp-block-post-title a`) para maior resiliência contra variações de tema WordPress.
- `AgiBlogSearchPG`: adicionado método privado `extractTitle(WebElement)` com extração progressiva de título via `getText()`, atributos `title`, `aria-label`, `innerText`, `textContent` e fallback via slug de `href`.

### Changed
- `pom.xml`: migração de `<systemProperties>` (formato legado) para `<systemPropertyVariables>` (formato recomendado do Maven Surefire) na configuração do `maven-surefire-plugin`. Adicionada variável `<cucumber.filter.tags>` para passar filtro de tag via linha de comando.

---

## [Unreleased] - 2026-04-03 — Correção do comando principal do desafio

### Changed
- Comando principal do desafio corrigido em documentação (`README.md` e `KNOWLEGDE.md`):
  - De: `./mvnw verify -Dcucumber.filter.tags="@agi_blog and @dog_api" -Dheadless=true`
  - Para: `./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true`
- Motivo: A sintaxe `and` requer que um único cenário tenha ambas as tags (`@agi_blog` E `@dog_api`), o que não existe no projeto. A sintaxe `or` executa todos os cenários que possuem `@agi_blog` OU `@dog_api`, que é o comportamento desejado para executar o desafio.

## [Unreleased] - 2026-04-03 — Padronização PT-BR e revisão documental

## [Unreleased] - 2026-04-03 — Comando principal e labels Allure no Gherkin

### Changed
- `README.md` ajustado para manter o **Comando principal do desafio** somente na seção `Comandos de execução`.
- Seção de configuração no `README.md` ampliada com padrão de tags `@allure.label.*` para uso em `.feature`.
- `TROUBLESHOOTING.md` atualizado com orientação sobre coexistência de tags funcionais e labels do Allure.
- `KNOWLEGDE.md` atualizado com convenção prática de labels Allure via Gherkin.

### Added
- Labels Allure adicionadas em todas as features de API e UI em `src/test/resources/features/**`:
  - `@allure.label.owner`
  - `@allure.label.epic`
  - `@allure.label.feature`
  - `@allure.label.story`
  - `@allure.label.severity`

### Fixed
- `WebHook` e `CucumberRunner` tornados mais robustos para cenários com tags adicionais (`@allure.label.*`), evitando dependência da ordem de tags para setup/teardown Web.

### Changed
- `README.md` atualizado para refletir o estado atual do projeto com maior precisão:
  - comandos de execução por tags ajustados para as tags realmente existentes nas features;
  - explicação explícita sobre o cuidado ao editar arquivos `.feature` sem quebrar o vínculo com as Step Definitions;
  - detalhamento mais fiel da cobertura atual do Blog do Agi.
- `KNOWLEGDE.md` ampliado com orientação prática sobre a relação entre `.feature` e Step Definitions, incluindo o impacto de mudanças textuais nos steps.
- `TROUBLESHOOTING.md` atualizado com orientação sobre steps indefinidos após alterações em arquivos `.feature`.
- Arquivos `.feature` revisados em PT-BR nos títulos e descrições narrativas, preservando as frases dos steps para manter compatibilidade com o Cucumber.

### Improved
- Padronização textual do Gherkin em português com melhor gramática e consistência nos títulos de `Funcionalidade`, `Cenário` e `Esquema do Cenário`.
- Clareza documental sobre o que pode ser ajustado linguisticamente em `.feature` e o que exige alteração sincronizada em Java.

## [Unreleased] - 2026-04-03 — Documentação e onboarding do projeto

### Added
- `README.md` reestruturado para os usuários do projeto com trilha completa de onboarding:
  - instalação de Java 25 (Linux/Windows/macOS);
  - instalação de Maven global (opcional) e uso recomendado do Maven Wrapper;
  - explicação didática da arquitetura e organização do projeto;
  - guia de implementação de novos testes API e Web no padrão do repositório;
  - conceitos e motivação dos exercícios (Mirante, Dog API e Blog do Agi);
  - comandos de execução consolidados (juntos e separados), incluindo execução headless.
- Documentado no `README.md` e no `TROUBLESHOOTING.md` que o modo headless pode ser configurado também por arquivo de propriedades (`headless=true`).
- Nova seção de dúvidas comuns no `README.md` para facilitar a adoção do projeto pela equipe.

### Changed
- Feature do Blog do Agi ampliada com `Esquema do Cenário` + `Exemplos` para palavras-chave (`pix`, `empréstimos`, `dinheiro`).
- `ChromeDriverArguments` atualizado para suportar execução headless via propriedade Maven:
  - `-Dheadless=true` adiciona `--headless=new` dinamicamente.
- `ChromeDriverArguments` atualizado para ler `headless` do `webdriver.properties` quando o parâmetro `-Dheadless` não for informado.
- `src/test/resources/config/webdriver.properties` atualizado com o parâmetro `headless=false` por padrão.
- Linguagem do `README.md` ajustada para um formato inclusivo e acessível para toda a equipe.

### Verified
- `./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true` com `BUILD SUCCESS`.
- Allure gerado com sucesso para execução filtrada de `@agi_blog` via `./mvnw allure:report`.

## [Unreleased] - 2026-04-03 — Agi Blog Web

### Added
- Implementação dos testes Web do **Desafio Técnico QA — Blog do Agi** (`https://blogdoagi.com.br/`), seguindo o padrão Cucumber + Selenium + Page Objects do projeto.
- Nova página `AgiBlogSearchPG` com suporte à busca no blog pela lupa/campo de pesquisa e contingência por URL de busca (`?s=`) para maior resiliência.
- Nova step definition `AgiBlogSearchSD` com logs SLF4J em português.
- Nova feature `src/test/resources/features/ui/004_Agi_Blog_Search.feature` com 2 cenários relevantes:
  - busca por termo válido (`pix`) com validação de título, URL e presença de resultados;
  - busca por termo inexistente com validação de título, URL e ausência de artigos.
- Nova URL `agi_blog` registrada em `src/test/resources/json-repo/urls.json`.

### Changed
- `PageRegister` atualizado para registrar `AgiBlogSearchPG`.
- `ScreenshotGenerator` ajustado para criar automaticamente o diretório de evidências e não falhar ao contar screenshots quando a pasta ainda não existe.
- `README.md` atualizado com a cobertura Web do Blog do Agi e com o exemplo ampliado de `urls.json`.

### Verified
- `./mvnw verify -Dcucumber.filter.tags="@agi_blog"` executando com `BUILD SUCCESS`.
- 2 cenários Web do Blog do Agi passando: `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`.

## [Unreleased] - 2026-04-03 — Dog API

### Added
- Implementação completa dos testes automatizados para o **Desafio Técnico QA — Dog API** (`https://dog.ceo/api/`), seguindo o padrão existente do projeto:
  - Novo pacote `br.com.automation.project.api.domain.dog` com estrutura completa:
    - `DogBreedsListBO` — modelo para `GET /breeds/list/all` (`message` como `Map<String, List<String>>`)
    - `DogBreedImagesBO` — modelo para `GET /breed/{breed}/images` (`message` como `List<String>`)
    - `DogRandomImageBO` — modelo para `GET /breeds/image/random` (`message` como `String`)
    - `DogErrorBO` — modelo de resposta de erro (`status`, `message`, `code`)
    - `DogApiException` — exceção tipada para falhas da Dog API, com `httpStatusCode` e `errorResponse`
    - `DogApiRequest` — cliente stateless com 3 métodos: `requestAllBreeds`, `requestBreedImages`, `requestRandomImage`
    - `DogApiSD` — step definitions com SLF4J e logs em português com acentuação correta
  - Feature file `005_Dog_Api.feature` com 5 cenários de teste sob a tag `@api @dog_api`:
    - `@api_cn_e1` — Listagem completa: status 200, `success`, lista não vazia, mais de 100 raças
    - `@api_cn_e2` — Raças conhecidas presentes na listagem: `hound`, `retriever`, `beagle`
    - `@api_cn_e3` — Imagens de raça válida (`hound`): lista não vazia, URLs HTTPS válidas
    - `@api_cn_e4` — Imagem aleatória: status 200, URL não vazia, inicia com `https://`
    - `@api_cn_e5` — Raça inexistente: HTTP 404, status `"error"`, mensagem contém `"Breed not found"`
  - URL adicionada em `src/test/resources/json-repo/urls.json`: `"dog_api_url": "https://dog.ceo/api/"`
  - Glue `br.com.automation.project.api.domain.dog.step.definitions` registrado no `CucumberRunner`

### Verified
- 5 cenários Dog API passando: `Tests run: 24, Failures: 0, Errors: 0, Skipped: 0`
- `./mvnw verify -Dcucumber.filter.tags="@dog_api"` com `BUILD SUCCESS`

---

## [Unreleased] - 2026-04-03

### Added
- Nova seção **Relatórios de Testes** no `README.md` documentando Allure e Cluecumber: como executar, gerar e servir via `allure:serve`.
- Suporte ao `allure:serve` para visualização local do Allure Report com servidor embutido.

### Changed
- `allure-maven.version` atualizado de `2.15.2` → `2.17.0` (última versão estável do plugin).
- `allure-report.version` atualizado de `2.29.1` → `2.33.0` (última versão estável dos adaptadores Java).
- `allure-commandline.version` atualizado de `2.29.0` → `2.38.1` (última versão estável da CLI).
- Versões de Allure separadas no `pom.xml` entre adaptadores Java (`allure-report.version`) e CLI de geração de relatório (`allure-commandline.version`).
- Flag `--ozone-platform=x11` adicionada em `ChromeDriverArguments` para forçar uso do X11 em ambientes Linux com Wayland, eliminando o aviso `Failed to open Wayland display, fallback to X11`.
- `TROUBLESHOOTING.md` atualizado com duas novas entradas:
  - **#7** — Erro de display Wayland com retorno para X11 no Chrome.
  - **#8** — Falha na geração do Allure Report por artefato `allure-commandline` não encontrado.

### Improved
- Refatoração do pacote `domain.starwar`:
  - `RootApiRequest` redesenhado como cliente stateless retornando `RootApiResponseBO` diretamente.
  - `RootApiSD` passa a validar status code e serviços a partir de `RootApiResponseBO`, com falha explícita via `AssertionError`.
    - `ErrorResponseBO` recebeu setters e `toString()` para suporte a contingência de parsing de erro.
  - `ResponseException` com mensagem contextual no construtor e `ObjectMapper` estático.
  - `RootApiResponseBO` endurecido com `Objects.requireNonNull` e `@Serial`.
  - Supressões `// noinspection unused` aplicadas nos métodos de DTO/contrato não chamados diretamente: `ErrorMessageBO.getCode()`, `ErrorMessageBO.getDescription()`, `ResponseException.getErrorMessage()`, `ErrorResponseBO.getCode()`.
- `ManagerFileUtils.getUrlFromJson()` corrigido para evitar `NullPointerException` com `Objects.toString(value, null)`.

### Verified
- 4 cenários Star Wars (`@star_wars`) passando: Luke Skywalker, Darth Vader, Leia Organa, Root API.
- `./mvnw allure:report` executando com `BUILD SUCCESS` após separação das versões.

---

## [Unreleased] - 2026-04-02

### Added
- Nova classe `WebParallelExecutionTestClass` em `src/main/java/br/com/automation/project/web/test/execution/WebParallelExecutionTestClass.java` para validação de execução paralela com 3 threads.
- Nova classe utilitária `ChromeDriverArguments` em `src/main/java/br/com/automation/project/web/browser/factory/ChromeDriverArguments.java` para centralizar flags compartilhadas do Chrome.
- Documentação completa no `README.md` com comandos de execução para Linux, Windows (CMD/PowerShell) e macOS.
- Novo documento de troubleshooting em `TROUBLESHOOTING.md` com incidentes reais e soluções aplicadas.

### Changed
- `ChromeDriverManager.java` atualizado para consumir `ChromeDriverArguments.getArguments()` em vez de lista local de argumentos.
- `ChromeDriverManager_v2.java` atualizado para consumir `ChromeDriverArguments.getArguments()` em vez de lista local de argumentos.
- Ordenação dos argumentos do Chrome padronizada e compartilhada entre os dois managers.
- Fluxo de execução Web consolidado para mitigar bloqueios de anti-bot com contingência de navegação.

### Improved
- Validação prática de isolamento por `ThreadLocal<WebDriver>` no `DriverManager` com execução paralela bem-sucedida (3 sessões distintas simultâneas).
- Estabilidade operacional com contingência de resultados de busca:
  1. Resultado padrão após pesquisa.
  2. URL direta de busca no Google.
  3. Navegação direta para `https://www.selenium.dev/documentation/`.

### Verified
- Execução serial via `WebExecutionTestClass` concluída com sucesso (`BUILD SUCCESS`).
- Execução paralela via `WebParallelExecutionTestClass` concluída com sucesso (`BUILD SUCCESS`).
- Três threads paralelas finalizaram com sucesso e encerraram os drivers de forma independente.

### Notes
- Avisos de CDP para Chrome 146 foram observados durante execução (`Unable to find CDP implementation matching 146`) sem impacto funcional no cenário.
- Avisos de threads pendentes (`UrlChecker`/`ForkJoinPool`) ao finalizar com `exec:java` foram observados e documentados como não bloqueantes.

