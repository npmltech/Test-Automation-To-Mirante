# Guia de Conhecimento Técnico

Guia de conhecimento prático para implementar testes de API e Web neste projeto.

---

## 1) BDD e Cucumber: o que são e por que usar

### O que é BDD
BDD (Behavior Driven Development) é uma forma de escrever testes orientados ao comportamento do usuário/sistema.

Em vez de começar pelo código, começamos pelo comportamento esperado:
- `Dado` um contexto
- `Quando` uma ação acontece
- `Então` um resultado é esperado

### O que é Cucumber
Cucumber é a ferramenta que executa esse comportamento descrito em `.feature`.

No projeto:
- o arquivo `.feature` descreve o cenário em linguagem natural;
- as Step Definitions implementam o cenário em Java;
- o `CucumberRunner` executa tudo por tag.

### Benefícios práticos
- Linguagem comum entre QA, dev e negócio.
- Cenários legíveis e rastreáveis.
- Reuso de passos (`steps`) em vários cenários.
- Facilidade de filtrar execução por tags (`@dog_api`, `@agi_blog`, etc.).

---

## 2) O que são `.feature`, Step Definitions e Pages

### `.feature`
Arquivo Gherkin com o comportamento.

No projeto, os arquivos `.feature` estão escritos em português e servem como ponto de entrada funcional da automação.

Exemplo real:
- `src/test/resources/features/api/005_Dog_Api.feature`
- `src/test/resources/features/ui/004_Agi_Blog_Search.feature`

### Step Definitions
Classe Java com métodos anotados por `@Dado`, `@Quando`, `@Então`.

Essas anotações fazem o vínculo direto entre o texto do step no Gherkin e o método Java que executa a ação ou validação.

Exemplo real:
- `src/test/java/br/com/automation/project/api/domain/dog/step/definitions/DogApiSD.java`
- `src/test/java/br/com/automation/project/ui/step/definitions/AgiBlogSearchSD.java`

### Pages (Page Object)
Classes que concentram seletores e ações da tela, evitando espalhar XPath/CSS em steps.

Exemplo real:
- `src/test/java/br/com/automation/project/ui/pages/AgiBlogSearchPG.java`
- `src/test/java/br/com/automation/project/ui/pages/TodoListPG.java`

---

## 3.1) Cuidado ao editar arquivos `.feature`

Em revisões textuais, existe uma regra importante:

- **Pode ser ajustado com segurança**:
  - `Funcionalidade:`
  - `Cenário:`
  - `Esquema do Cenário:`
  - descrições narrativas em texto livre
- **Exige atualização sincronizada com Java**:
  - linhas iniciadas por `Dado`, `Quando`, `Então` e `E`

Por quê?

Porque o Cucumber localiza a implementação a partir do texto definido nas anotações das Step Definitions. Se o texto do passo mudar no `.feature` e a anotação Java permanecer igual, o cenário passa a falhar como step indefinido.

Exemplo conceitual:

```gherkin
Então o status da dog api deverá ser "success"
```

Esse texto precisa continuar compatível com a anotação correspondente na classe de steps.

Em resumo:
- revisão gramatical em `.feature` é bem-vinda;
- revisão de steps exige cuidado funcional, e não apenas textual.

### 3.2) Labels do Allure via tags no `.feature`

Além das tags funcionais de execução (`@api`, `@dog_api`, `@ui`, `@agi_blog`), os cenários podem receber labels do Allure para enriquecer os relatórios.

Padrão usado no projeto:
- `@allure.label.owner:<responsavel>`
- `@allure.label.epic:<contexto_macro>`
- `@allure.label.feature:<feature_do_produto>`
- `@allure.label.story:<cenario_ou_fluxo>`
- `@allure.label.severity:<nivel>`

Exemplo:

```gherkin
@api_cn_e1 @allure.label.severity:critical @allure.label.story:api_cn_e1
Cenário: Validar a listagem completa de raças de cães
```

Boas práticas:
- mantenha tags funcionais para filtro de execução no Maven/Cucumber;
- use tags `@allure.label.*` apenas para metadados de relatório;
- prefira valores curtos e estáveis para `story` e `owner`.

---

## 3) O que é Page Object

Page Object é um padrão para automação Web:
- cada página/componente da UI vira uma classe;
- nela ficam locators, ações e pequenas validações da tela;
- os steps só “orquestram” o comportamento.

### Vantagens
- Menos duplicação de locators.
- Manutenção mais simples quando o HTML muda.
- Steps mais limpos e legíveis.

---

## 4) O que é Register de Pages (`PageRegister`)

`PageRegister` é uma classe que centraliza o acesso às páginas.

No projeto:
- `src/test/java/br/com/automation/project/ui/pages/PageRegister.java`

Ela expõe métodos como:
- `getAgiBlogSearchPG()`
- `getTodoListPG()`

Qualquer classe que **estenda** `PageRegister` consegue usar esses métodos diretamente. Isso evita criar instâncias manualmente em cada step definition e padroniza o acesso às pages.

---

## 5) Jackson Databind: o que é e como usamos

Jackson Databind converte JSON <-> objeto Java.

### Na prática deste projeto
Quando uma API retorna JSON, mapeamos para BOs (Business Objects) com anotações como:
- `@JsonProperty`
- `@JsonIgnoreProperties(ignoreUnknown = true)`

Exemplo real:
- `src/test/java/br/com/automation/project/api/domain/dog/model/DogBreedsListBO.java`

Isso permite validar campos com tipagem forte em vez de manipular string JSON “na mão”.

---

## 6) RestAssured + Builder Pattern (`RequestSpecBuilder`)

### O que é Builder Pattern
Builder é um padrão de criação de objeto “passo a passo”, útil quando há muitos parâmetros.

### Como aparece no RestAssured
Usamos `RequestSpecBuilder` para montar especificações de requisição reutilizáveis:
- base URI
- headers
- content type
- configs comuns

Exemplo conceitual:
```java
RequestSpecBuilder builder = new RequestSpecBuilder();
builder.setBaseUri(baseUri);
builder.setRelaxedHTTPSValidation();
RequestSpecification request = RestAssured.given().spec(builder.build());
```

Vantagem: padroniza requests e evita repetição.

---

## 7) Model, BO e Error no contexto MVC (adaptado para testes)

Em automação de API, usamos uma estrutura inspirada em separação de responsabilidades:

- **Model**: representa o contrato bruto do JSON (campos e tipos da resposta).
- **BO (Business Object)**: encapsula o model para uso no fluxo de teste (regra de negócio/estado auxiliar).
- **Error BO**: representa payload de erro.
- **Request class**: executa chamada HTTP.
- **Step Definition**: valida comportamento esperado.

### Fluxo mental rápido (API)

```text
Request -> Model -> BO -> Step
           \-> Error BO (quando falha)
```

Leitura do fluxo:
- **Request** chama a API e recebe o JSON.
- **Model** representa a estrutura desse JSON (contrato de campos/tipos).
- **BO** organiza o model para facilitar validação no cenário.
- **Step** valida a regra de negócio esperada.
- Em caso de erro, o fluxo usa **Error BO** para validar o contrato de falha.

### Exemplo do projeto
- **Dog API**
  - `DogBreedsListBO`, `DogBreedImagesBO`, `DogRandomImageBO` -> contrato de sucesso (model/bo simples)
  - `DogErrorBO` -> contrato de erro
  - `DogApiRequest` -> comunicação HTTP
  - `DogApiSD` -> asserções de negócio

- **Star Wars Root**
  - `RootApiModelBO` -> model com campos da API root (`films`, `people`, `planets`, ...)
  - `RootApiResponseBO` -> BO que encapsula `RootApiModelBO` + `httpStatusCode`
  - `RootApiRequest` -> request + desserialização
  - `RootApiSD` -> validação de negócio (status e serviços)

Esse formato mostra o papel de cada camada:
- o **Model** descreve a estrutura da resposta;
- o **BO** facilita o consumo/validação dessa resposta nos steps.

Essa separação melhora legibilidade, reuso e manutenção.

### Como o BO ajuda a validar o schema JSON da resposta

Quando dizemos "validar schema" neste projeto, fazemos isso em duas camadas:

1. **Schema estrutural via BO (contrato de campos/tipos)**
   - O JSON é convertido para BO com Jackson (`response.as(SeuBO.class)`).
   - Se o JSON vier com estrutura incompatível com o BO esperado, a desserialização já acusa problema.
   - Exemplo: em `DogBreedsListBO`, o campo `message` é `Map<String, List<String>>`. Se a API mudar para outro tipo, o mapeamento quebra/alerta.

2. **Schema de negócio via asserções nos steps**
   - Após mapear para o BO, os steps validam regras funcionais: status, lista vazia/não vazia, quantidade mínima, etc.
   - Isso garante que além da estrutura estar correta, o comportamento também está correto.

Exemplo prático no fluxo Dog API:
- `DogApiRequest` desserializa o JSON em `DogBreedsListBO`, `DogBreedImagesBO` e `DogRandomImageBO`.
- `DogApiSD` valida os campos esperados (status, listas, URLs).
- `DogErrorBO` representa o contrato de erro (`status`, `message`, `code`) para cenários negativos.

Em resumo:
- **Model** descreve o contrato estrutural da resposta.
- **BO** organiza o dado para o fluxo de validação.
- **Step Definition** valida a regra de negócio.

---

## 8) Como implementar um teste API neste projeto (passo a passo declarativo)

1. Escreva o cenário em `.feature` na pasta `features/api`.
2. Crie/ajuste o BO de sucesso para mapear o JSON da API.
3. Crie/ajuste o BO de erro para cenários negativos.
4. Implemente a classe de request usando RestAssured (`RequestSpecBuilder`).
5. Grave status/body em `ResponseClass` quando necessário para utilitários existentes.
6. Implemente os steps no SD validando status, contrato e regra de negócio.
7. Execute por tag e evolua cenários positivos/negativos.

---

## 9) Como implementar um teste Web neste projeto (passo a passo declarativo)

### Fluxo mental rápido (Web)

```text
Feature -> Step Definition -> PageRegister -> Page Object -> WebDriver
```

Leitura do fluxo:
- **Feature** descreve o comportamento esperado em Gherkin.
- **Step Definition** executa esse comportamento em Java.
- **PageRegister** entrega a página correta para o step (centraliza acesso).
- **Page Object** contém locators e ações da tela.
- **WebDriver** interage de fato com o navegador.

1. Escreva o comportamento na `.feature` em `features/ui`.
2. Crie Page Object com locators e ações da página.
3. Registre a page no `PageRegister`.
4. Crie os steps em `ui/step/definitions` estendendo `PageRegister`.
5. Mantenha validações de comportamento no step e detalhes de HTML na page.
6. Use tags para execução seletiva (`@ui`, `@agi_blog`, etc.).
7. Se necessário para CI, execute com `headless=true`.

---

## 10) Comparação rápida: API x Web

| Aspecto | Teste API | Teste Web |
|---|---|---|
| Entrada principal | `.feature` em `features/api` | `.feature` em `features/ui` |
| Camada de execução | `Request class` + RestAssured | `Page Object` + WebDriver |
| Estrutura de dados | Model/BO/Error BO | Elementos da página (locators) |
| Validação principal | status, contrato JSON, regra de negócio | comportamento visual/funcional da interface |
| Classe de apoio | `DogApiRequest`, `RootApiRequest` | `AgiBlogSearchPG`, `TodoListPG` |
| Orquestração | `Step Definition` API | `Step Definition` Web |
| Execução por tag | `@dog_api`, `@star_wars` | `@ui`, `@agi_blog` |

Esse comparativo ajuda a decidir rapidamente por onde começar quando você for criar um cenário novo.

---

## 11) Comandos úteis (resumo)

```bash
# Comando principal do desafio (Dog API + Blog do Agi)
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true

# API - Dog
./mvnw verify -Dcucumber.filter.tags="@dog_api"

# Web - Agi (visível)
./mvnw verify -Dcucumber.filter.tags="@agi_blog"

# Web - Agi (headless por JVM)
./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true
```

Se preferir, também pode configurar no arquivo:
- `src/test/resources/config/webdriver.properties`
- `headless=true`

---

## 12) Mapa mental rápido

- `.feature` = o que o sistema deve fazer
- `step definition` = código que executa o que está na feature
- `page object` = camada de tela (locators + ações)
- `request class` = camada HTTP para API
- `BO/model/error` = objetos que representam payloads
- `runner` = ponto de execução da suíte

Com isso, você consegue implementar cenários novos mantendo o mesmo padrão do projeto.

---

## 13) CI com GitHub Actions (pipeline do projeto)

Arquivo:
- `.github/workflows/tests-api-web.yml`

### Quando executa
- `push` na branch `main`
- `pull_request`

### Jobs da pipeline
- `api-dog`
  - Executa: `./mvnw verify -Dcucumber.filter.tags="@dog_api"`
- `web-agi-blog`
  - Executa: `./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true`

### Actions utilizadas
- `actions/checkout@v5`
- `actions/setup-java@v5`
- `actions/upload-artifact@v4`

### Compatibilidade de runtime JavaScript das actions
- A pipeline define `FORCE_JAVASCRIPT_ACTIONS_TO_NODE24=true`.
- Essa variável garante compatibilidade com a migração dos runners do GitHub Actions para Node.js 24.

### Artefatos publicados ao final dos jobs
- `target/allure-results`
- `target/site/allure-maven-plugin`
- `target/cluecumber-reports`
- `target/cucumber-reports`

Esses artefatos ajudam a analisar falhas sem depender de reprodução local imediata.

---

## 14) Fundamentos de performance com JMeter neste projeto

Arquivo de referência:

- `testes-carga-pico/site-de-viagens.jmx`

### Como o plano foi modelado

- O plano usa `User Defined Variables` no `Test Plan`.
- Cada variável é lida com `__P(...)`, permitindo sobrescrita por CLI (`-J...`).
- Existem dois grupos de carga: sustentada e pico.

### Variáveis globais (CLI)

As propriedades abaixo controlam a execução sem editar o JMX:

- `baseUrl`
- `loadThreads`
- `loadRampUp`
- `loadDuration`
- `spikeThreads`
- `spikeRampUp`
- `spikeDelay`
- `spikeDuration`

Boas práticas:

1. Versione os comandos executados no histórico de CI/README.
2. Não altere nomes de propriedades sem atualizar JMX e documentação.
3. Mantenha defaults estáveis no JMX e perfil de carga no comando.

### Relatório HTML

- O dashboard é gerado com `-e -o <pasta>`.
- A pasta de saída precisa estar vazia.
- O conjunto mínimo de evidências é: `results.jtl`, `jmeter.log` e `report/`.

### Heap e estabilidade

- Para cenários mais pesados, ajuste `HEAP` antes de executar.
- Exemplo comum: `-Xms1g -Xmx4g -XX:+UseG1GC`.
- Ajuste gradualmente, observando CPU e GC.

### GUI x Non-GUI

- GUI: modelagem e depuração.
- Non-GUI: execução oficial (mais estável e mais econômica).
- Listeners pesados devem permanecer desabilitados em testes longos.

### Ultimate Thread Group

- Recomendado para perfis de carga em múltiplas fases.
- Útil quando o `Thread Group` padrão não representa bem a curva de acesso.
- Deve ser instalado via Plugins Manager (`Custom Thread Groups`).

Guia operacional completo:

- `testes-carga-pico/JMETER.md`

