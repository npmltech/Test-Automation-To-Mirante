# Troubleshooting

Guia prático com os problemas encontrados durante as execuções e as soluções aplicadas no projeto.

---

## 1) Bloqueio por anti-bot/reCAPTCHA em busca no Google

### Sintoma
- A busca no Google não retorna resultados no tempo esperado.
- Em alguns cenários, o fluxo é interrompido por mecanismos anti-bot.

### Causa provável
- Detecção de automação pelo Google em sessões WebDriver.

### Solução aplicada
- No `WebExecutionTestClass` e no `WebParallelExecutionTestClass` foi adotada uma contingência em camadas:
  1. Busca normal por `selenium documentation`.
  2. Navegação para URL direta de busca: `https://www.google.com/search?q=selenium+documentation`.
  3. Fallback final para documentação oficial: `https://www.selenium.dev/documentation/`.
- No `ChromeDriverManager_v2` foi aplicada injeção CDP para reduzir sinal de automação:
  - `Object.defineProperty(navigator, 'webdriver', { get: () => undefined });`

### Status
- Resolvido para o escopo de validação Web-UI do projeto.

---

## 2) Divergência de argumentos entre `ChromeDriverManager` e `ChromeDriverManager_v2`

### Sintoma
- Cada manager possuía lista própria de argumentos, sujeita a drift e inconsistência.

### Causa provável
- Duplicação de configuração em classes diferentes.

### Solução aplicada
- Criação de `ChromeDriverArguments` para centralizar os argumentos em um único ponto.
- Ambos os managers passaram a usar `ChromeDriverArguments.getArguments()`.
- Lista ordenada para facilitar manutenção e revisão.

### Status
- Resolvido.

---

## 3) Necessidade de validar isolamento por `ThreadLocal`

### Sintoma
- Dúvida se o `DriverManager` estava realmente isolando `WebDriver` por thread.

### Causa provável
- Ausência de execução de prova com múltiplas threads simultâneas no fluxo principal.

### Solução aplicada
- Criação de `WebParallelExecutionTestClass` com `ExecutorService` em pool fixo de 3 threads.
- Cada thread cria seu próprio `DriverManagerFactory` e executa o cenário completo.
- Encerramento individual via `DriverManager.closeAndQuitDriver()`.

### Evidência observada
- Três sessões de driver distintas no log (session IDs diferentes).
- Resultado final: todas as threads concluíram com sucesso.

### Status
- Resolvido e validado na prática.

---

## 4) Aviso de CDP para a versão do Chrome

### Sintoma
- Mensagem no log:
  - `Unable to find CDP implementation matching 146`

### Causa provável
- Dependência Selenium em uso sem artefato `selenium-devtools-vXXX` específico para a versão detectada do Chrome.

### Impacto
- Não bloqueou a execução dos testes neste cenário.

### Solução aplicada
- Nenhuma mudança obrigatória no fluxo, pois o cenário executou com sucesso.
- Registro do aviso como comportamento conhecido.

### Ação opcional futura
- Incluir dependência de devtools compatível com a major version do Chrome quando houver necessidade de CDP avançado.

### Status
- Conhecido / sem bloqueio funcional.

---

## 5) Aviso de threads ativas ao finalizar `exec:java`

### Sintoma
- Mensagens como:
  - `UrlChecker-* was interrupted but is still alive`
  - `ForkJoinPool.commonPool-delayScheduler will linger`

### Causa provável
- Threads internas de bibliotecas (Selenium/WebDriverManager/HTTP) podem permanecer ativas por curto período após término da aplicação quando executada com `exec-maven-plugin`.

### Impacto
- Não bloqueou build nem execução do teste (`BUILD SUCCESS`).

### Solução aplicada
- Garantia de teardown no `finally` com `DriverManager.closeAndQuitDriver()`.
- Documentação do aviso como não bloqueante no contexto atual.

### Ação opcional futura
- Revisar configurações de ciclo de vida do plugin `exec-maven-plugin` para reduzir avisos de encerramento.

### Status
- Conhecido / sem bloqueio funcional.

---

## 6) Strings com acentuação aparecendo quebradas em alguns logs

### Sintoma
- Saída de console em alguns ambientes exibindo palavras com acentuação incorreta (ex.: `concludo`, `execuo`).

### Causa provável
- Encoding do terminal/shell diferente de UTF-8 durante execução local.

### Solução aplicada
- O código mantém mensagens corretas em UTF-8.
- O projeto já possui `-Dfile.encoding=UTF-8` no `maven-surefire-plugin`.

### Ação opcional futura
- Forçar encoding UTF-8 também para execuções com `exec:java` (variável de ambiente ou argumento JVM dedicado).

### Status
- Conhecido / baixo impacto funcional.

---

## 7) Erro de display Wayland com retorno para X11 no Chrome

### Sintoma
- Mensagem no log ao iniciar o Chrome:
  ```
  Failed to open Wayland display, fallback to X11.
  WAYLAND_DISPLAY='wayland-1' DISPLAY=':1'
  ```

### Causa provável
- O Chrome, a partir da versão 113, tenta usar o protocolo Wayland por padrão em ambientes Linux que expõem `WAYLAND_DISPLAY`.
- Quando a sessão gráfica não oferece suporte completo ao protocolo Ozone/Wayland para renderização headless ou automatizada, o Chrome emite esse aviso e volta ao X11 via `DISPLAY`.

### Impacto
- Em muitos casos, o teste **executa normalmente** após o retorno para X11 (não bloqueante).
- Em ambientes CI ou headless sem servidor X11 disponível, pode causar falha na inicialização do driver.

### Solução aplicada
- Adição da flag `--ozone-platform=x11` em `ChromeDriverArguments.getArguments()` para forçar explicitamente o uso do X11, eliminando a tentativa de conexão Wayland:
  ```java
  "--ozone-platform=x11"
  ```
- A flag é compartilhada entre `ChromeDriverManager` e `ChromeDriverManager_v2` via `ChromeDriverArguments`.

### Status
- Resolvido. Aviso não ocorre mais após a adição da flag.

---

## 8) Falha na geração do Allure Report — artefato `allure-commandline` não encontrado

### Sintoma
- Erro ao executar `./mvnw allure:report`:
  ```
  Could not find artifact io.qameta.allure:allure-commandline:zip:2.29.1 in central
  ```

### Causa provável
- A propriedade `allure-report.version` era compartilhada entre os adaptadores Java (ex.: `allure-cucumber7-jvm`) e a CLI do Allure.
- O artefato `allure-commandline` possui ciclo de versões independente: enquanto os adapters chegaram até `2.33.0`, a CLI está na `2.38.1`.
- Usar a mesma versão para os dois causava resolução de artefato inexistente.

### Solução aplicada
- Separação das versões no `pom.xml`:
  - `allure-report.version` → versão dos adaptadores Java (`allure-cucumber7-jvm`, `allure-java-commons`, `allure-testng`)
  - `allure-commandline.version` → versão exclusiva da CLI usada pelo `allure-maven` plugin em `reportVersion`
- Configuração atual:
  ```xml
  <allure-report.version>2.33.0</allure-report.version>
  <allure-commandline.version>2.38.1</allure-commandline.version>
  ```

### Como gerar o relatório

```bash
# Executar testes e gerar relatório HTML estático
./mvnw clean verify -Dcucumber.filter.tags="@star_wars"
./mvnw allure:report

# Ou servir com servidor embutido (abre no browser automaticamente)
./mvnw allure:serve
```

### Status
- Resolvido.

---

## 9) Executar navegador em modo headless via Maven

### Sintoma
- Necessidade de executar testes Web sem abrir interface gráfica (execução local automatizada ou CI).

### Solução aplicada
- O projeto passou a suportar headless por propriedade JVM:
  - `-Dheadless=true`
- O projeto também suporta headless direto no arquivo `src/test/resources/config/webdriver.properties`:
  - `headless=true`
- Quando habilitada, a flag `--headless=new` é adicionada em `ChromeDriverArguments.getArguments()`.

### Prioridade de configuração
- `-Dheadless=true|false` (linha de comando) tem prioridade.
- Se o parâmetro JVM não for informado, o projeto lê `headless` do arquivo de propriedades.

### Comandos recomendados

```bash
# Apenas Blog do Agi em headless
./mvnw verify -Dcucumber.filter.tags="@agi_blog" -Dheadless=true

# Toda suíte Web em headless
./mvnw verify -Dcucumber.filter.tags="@ui" -Dheadless=true
```

### Status
- Resolvido e validado.

---

## 10) Timeout do renderizador do Chrome durante navegação em busca Web

### Sintoma
- Erro intermitente durante navegação para URL de busca, por exemplo:
  - `Timed out receiving message from renderer`

### Causa provável
- Instabilidade pontual de renderização do Chrome em ambiente local ou alta latência de carregamento da página.

### Solução aplicada
- Mantida uma alternativa de contingência de busca no fluxo do Blog do Agi para aumentar a resiliência.
- Reexecução do cenário filtrado por tag normalmente resolve a intermitência.

### Mitigações recomendadas
- Priorizar execução com `-Dheadless=true` em ambientes compartilhados.
- Executar testes por tag para reduzir carga no navegador durante depuração.

### Status
- Conhecido / baixo impacto, mitigado por contingência de busca + rerun.

---

## 11) Steps indefinidos após ajuste textual em arquivos `.feature`

### Sintoma
- O Cucumber passa a indicar steps como indefinidos (`undefined steps`) após uma revisão textual em arquivos `.feature`.
- O cenário deixa de encontrar a implementação Java mesmo com a lógica do projeto aparentemente intacta.

### Causa provável
- Alteração do texto de linhas iniciadas por `Dado`, `Quando`, `Então` ou `E` sem atualização correspondente nas Step Definitions anotadas em Java.

### Solução aplicada
- Padronização da revisão linguística dos arquivos `.feature` para atuar com segurança em:
  - `Funcionalidade:`
  - `Cenário:`
  - `Esquema do Cenário:`
  - descrições narrativas fora dos steps
- Manutenção das frases dos steps exatamente como estão quando não há intenção de alterar o vínculo com o Java.

### Como corrigir se acontecer
- Verifique qual linha do `.feature` foi alterada.
- Compare com a anotação correspondente em `@Dado`, `@Quando`, `@Então` ou `@E` na Step Definition.
- Se quiser mudar a redação do step, atualize também a anotação Java correspondente.

### Status
- Resolvido com orientação documental e prática de manutenção segura.

---

## 12) Execução com `@agi_blog and @dog_api` retorna `0 Scenarios`

### Sintoma
- Comando executa com `BUILD SUCCESS`, porém sem cenários executados:
  - `0 Scenarios`
  - `Tests run: 0`

### Causa provável
- Em expressões de tag do Cucumber, `and` exige que o mesmo cenário tenha todas as tags informadas.
- No projeto atual, `@agi_blog` (Web) e `@dog_api` (API) estão em features diferentes.

### Solução aplicada
- Documentação atualizada com o comando solicitado e explicação do comportamento.
- Para rodar os dois grupos no mesmo comando, usar `or`:

```bash
./mvnw verify -Dcucumber.filter.tags="@agi_blog or @dog_api" -Dheadless=true
```

### Evidência de falha no Cluecumber
- Resumo com cenários falhos: `target/cluecumber-reports/pages/scenario-summary.html`
  - `2 failed Scenarios` (ex.: `scenario_6.html` e `scenario_8.html`).
- Detalhe da falha com stacktrace: `target/cluecumber-reports/pages/scenario-detail/scenario_6.html`
  - `org.opentest4j.AssertionFailedError` no step de validação de títulos.
- Screenshot anexado no relatório: `target/cluecumber-reports/attachments/attachment001.png`

### Status
- Conhecido / comportamento esperado da sintaxe de tags do Cucumber.

---

## 13) Labels Allure em `.feature` sem impacto no filtro funcional

### Sintoma
- Após adicionar tags `@allure.label.*`, há dúvida se a execução por tags funcionais continua estável.

### Causa provável
- Mistura de tags funcionais (ex.: `@ui`, `@dog_api`) com tags de metadados de relatório (ex.: `@allure.label.severity:critical`).

### Solução aplicada
- Mantida separação de responsabilidades:
  - tags funcionais para filtro de execução (`-Dcucumber.filter.tags=...`);
  - tags `@allure.label.*` para enriquecimento de relatórios Allure/Cluecumber.
- Ajuste no hook de execução para identificar cenários UI pelo conjunto de tags do cenário, sem depender da ordem de leitura das tags.

### Status
- Resolvido.

---

## 14) Aviso de depreciação do Node.js 20 no GitHub Actions

### Sintoma
- Alerta exibido no CI indicando que algumas actions estavam em runtime Node.js 20, por exemplo:
  - `actions/checkout@v4`
  - `actions/setup-java@v4`
  - `actions/upload-artifact@v4`
- Mensagem de migração informando adoção de Node.js 24 como padrão nos runners.

### Causa provável
- Mudança de política do GitHub Actions para runtime JavaScript das actions.
- Workflows com actions em versões antigas ou sem opt-in explícito podem gerar aviso durante a janela de transição.

### Solução aplicada
- Atualização do workflow `.github/workflows/tests-api-web.yml` para:
  - `actions/checkout@v5`
  - `actions/setup-java@v5`
  - `actions/upload-artifact@v4`
- Ativação da variável de compatibilidade:
  - `FORCE_JAVASCRIPT_ACTIONS_TO_NODE24=true`

### Impacto
- Pipeline permanece estável durante a transição de runtime.
- Redução de risco de falha futura por remoção do Node.js 20 nos runners.

### Status
- Resolvido.

---

## 15) Erro ao gerar dashboard HTML do JMeter (`-e -o`)

### Sintoma
- A execução non-GUI falha ao gerar relatório HTML.
- Mensagem comum: diretório de saída já existe ou não está vazio.

### Causa provável
- A pasta definida em `-o` já continha arquivos de execução anterior.

### Solução aplicada
- Padronização de limpeza da pasta antes da execução:
  - Linux/macOS: `rm -rf results/<cenario>/report`
  - Windows PowerShell: `Remove-Item -Recurse -Force ...`
- Manutenção de diretório dedicado por cenário (`load` e `spike`).

### Status
- Resolvido.

---

## 16) Alto consumo de memória ao executar JMeter na GUI

### Sintoma
- Lentidão, travamentos ou `OutOfMemoryError` durante execução com interface gráfica.

### Causa provável
- Listeners de alto consumo ativos (ex.: `View Results Tree`) durante testes com alto volume.

### Solução aplicada
- Manter listeners pesados desabilitados no plano (`enabled="false"`).
- Executar carga oficial em modo non-GUI.
- Ajustar heap JVM quando necessário com `HEAP`.

### Comandos úteis

Linux/macOS:

```bash
export HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
```

Windows (PowerShell):

```powershell
$env:HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
```

### Status
- Mitigado com boas práticas de execução.

---

## Comandos rápidos para reproduzir

### Linux/macOS

```bash
./mvnw compile exec:java -Dexec.mainClass="br.com.automation.project.web.test.execution.WebExecutionTestClass"
./mvnw compile exec:java -Dexec.mainClass="br.com.automation.project.web.test.execution.WebParallelExecutionTestClass"
```

### Windows (CMD)

```cmd
mvnw.cmd compile exec:java -Dexec.mainClass="br.com.automation.project.web.test.execution.WebExecutionTestClass"
mvnw.cmd compile exec:java -Dexec.mainClass="br.com.automation.project.web.test.execution.WebParallelExecutionTestClass"
```

### Windows (PowerShell)

```powershell
.\mvnw.cmd compile exec:java "-Dexec.mainClass=br.com.automation.project.web.test.execution.WebExecutionTestClass"
.\mvnw.cmd compile exec:java "-Dexec.mainClass=br.com.automation.project.web.test.execution.WebParallelExecutionTestClass"
```
