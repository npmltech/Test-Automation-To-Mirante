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

### Rodar um cenário específico por tag de cenário

```bash
./mvnw verify -Dcucumber.filter.tags="@ui_cn_d5" -Dheadless=true
```

### Comando principal do desafio

Este é o comando que executa o desafio técnico proposto (Dog API + Blog do Agi):

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true
```

Ele executa todos os cenários da Dog API e do Blog do Agi em modo headless.

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
- `KNOWLEGDE.pdf` -> versão em PDF do guia de conhecimento técnico
