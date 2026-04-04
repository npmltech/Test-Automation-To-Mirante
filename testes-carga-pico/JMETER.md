# Testes de Carga com JMeter (BlazeDemo)

Este diretório contém o plano `site-de-viagens.jmx` para validar o fluxo de compra de passagens em `https://www.blazedemo.com`.

## Visão rápida

- Cenário **carga**: volume constante de usuários virtuais.
- Cenário **pico**: carga base + aumento abrupto (spike).
- Saídas da execução: `results.jtl`, `jmeter.log` e dashboard HTML.

## Pré-requisitos

- Java 11 ou superior (recomendado: Java 17+).
- JMeter 5.6.3.

## Instalar e validar o JMeter

No diretório `testes-carga-pico`:

```bash
curl -L -o apache-jmeter.tgz https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.6.3.tgz
tar -xzf apache-jmeter.tgz
./apache-jmeter-5.6.3/bin/jmeter -v
```

No Windows (PowerShell):

```powershell
Invoke-WebRequest -Uri "https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.6.3.tgz" -OutFile ".\apache-jmeter.tgz"
tar -xzf .\apache-jmeter.tgz
.\apache-jmeter-5.6.3\bin\jmeter.bat -v
```

## Variáveis globais do plano (`-J`) e mapeamento com o JMX

O `site-de-viagens.jmx` usa `User Defined Variables` com `__P(...)`. Isso permite sobrescrever parâmetros por linha de comando sem editar o `.jmx`.

Variáveis principais:

- `-JbaseUrl` -> host alvo (padrão no JMX: `www.blazedemo.com`)
- `-JloadThreads` -> threads da carga sustentada
- `-JloadRampUp` -> rampa da carga sustentada (segundos)
- `-JloadDuration` -> duração da carga sustentada (segundos)
- `-JspikeThreads` -> threads adicionais no pico
- `-JspikeRampUp` -> rampa do pico (segundos)
- `-JspikeDelay` -> atraso para iniciar o pico (segundos)
- `-JspikeDuration` -> duração do pico (segundos)

Boas práticas:

1. Prefira sempre `-J...` no comando para manter histórico reprodutível no terminal/CI.
2. Use nomenclatura consistente entre local e CI (evita testes com perfil diferente sem perceber).
3. Evite misturar maiúsculas/minúsculas nos nomes das propriedades.
4. Inicie com carga menor e suba gradualmente (ex.: 100 -> 300 -> 700).

## Estrutura do novo `site-de-viagens.jmx`

O plano atualizado foi incluído no repositório com a seguinte organização:

- `Test Plan`: `Plano de testes para o site de viagens e compra de passagens`.
- `HTTP Defaults`: host global em `${BASE_URL}` e protocolo `https`.
- `Thread Group` de carga sustentada: `TG - Carga Sustentada 250 RPS`.
- `Thread Group` de pico: `TG - Teste Pico +250 RPS`.
- `Constant Throughput Timer` em ambos os grupos para controle de vazão.
- `ResultCollector` de GUI (`View Results Tree` e `Summary Report`) desabilitados por padrão.
- `Backend Listener` desabilitado por padrão (ativação opcional para stack externa).

Pontos importantes da atualização:

1. Variável principal de domínio padronizada em `BASE_URL`.
2. Parâmetros de carga e pico centralizados em `User Defined Variables` com `__P(...)`.
3. Plano preparado para operação non-GUI sem dependência de listeners visuais.

## Execução sem GUI (recomendado)

Use sempre os comandos a partir da pasta `testes-carga-pico`.

### Linux/macOS

```bash
mkdir -p results/load results/spike
rm -rf results/load/report results/spike/report
./apache-jmeter-5.6.3/bin/jmeter -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log -e -o results/load/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=300 -JspikeThreads=0 -JspikeDelay=99999 -JspikeDuration=1 -JspikeRampUp=1
./apache-jmeter-5.6.3/bin/jmeter -n -t site-de-viagens.jmx -l results/spike/results.jtl -j results/spike/jmeter.log -e -o results/spike/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=420 -JspikeThreads=900 -JspikeDelay=180 -JspikeDuration=60 -JspikeRampUp=10
```

### Windows PowerShell

```powershell
New-Item -ItemType Directory -Force -Path .\results\load, .\results\spike | Out-Null
Remove-Item -Recurse -Force .\results\load\report, .\results\spike\report -ErrorAction SilentlyContinue
.\apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log -e -o results/load/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=300 -JspikeThreads=0 -JspikeDelay=99999 -JspikeDuration=1 -JspikeRampUp=1
.\apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/spike/results.jtl -j results/spike/jmeter.log -e -o results/spike/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=420 -JspikeThreads=900 -JspikeDelay=180 -JspikeDuration=60 -JspikeRampUp=10
```

### Windows CMD

```bat
if not exist results\load mkdir results\load
if not exist results\spike mkdir results\spike
if exist results\load\report rmdir /s /q results\load\report
if exist results\spike\report rmdir /s /q results\spike\report
apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log -e -o results/load/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=300 -JspikeThreads=0 -JspikeDelay=99999 -JspikeDuration=1 -JspikeRampUp=1
apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/spike/results.jtl -j results/spike/jmeter.log -e -o results/spike/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=420 -JspikeThreads=900 -JspikeDelay=180 -JspikeDuration=60 -JspikeRampUp=10
```

## Boas práticas de relatório HTML (`-e -o`)

1. Sempre gere o relatório em pasta vazia (`-o`) para evitar erro de diretório existente.
2. Separe cada execução por pasta (`results/load`, `results/spike`, `results/ci-*`).
3. Não use listeners pesados para análise final; priorize o dashboard HTML.
4. Preserve `results.jtl` e `jmeter.log` junto do relatório para auditoria.
5. Em CI, publique os três artefatos do cenário: `results.jtl`, `jmeter.log`, `report/`.

## Boas práticas de `jmeter.properties` (baseado neste projeto)

Mesmo usando `-J`, vale manter um perfil de segurança no `jmeter.properties`:

```properties
# Grava apenas o necessário para reduzir uso de disco/memória
jmeter.save.saveservice.output_format=csv
jmeter.save.saveservice.response_data=false
jmeter.save.saveservice.samplerData=false
jmeter.save.saveservice.requestHeaders=false
jmeter.save.saveservice.responseHeaders=false

# Dashboard com métricas relevantes para análise de performance
jmeter.reportgenerator.apdex_satisfied_threshold=500
jmeter.reportgenerator.apdex_tolerated_threshold=1500
jmeter.reportgenerator.overall_granularity=60000
```

Recomendação prática:

- Mantenha um arquivo de propriedades versionado para o time.
- Evite sobrescrever no arquivo valores específicos de teste que já estão em `-J...`.
- Deixe `jmeter.properties` com defaults estáveis e altere perfil de carga via CLI.

## Ajuste de memória heap do JMeter

Para cargas mais altas, ajuste o heap antes da execução.

Linux/macOS:

```bash
export HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
./apache-jmeter-5.6.3/bin/jmeter -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log
```

Windows (PowerShell):

```powershell
$env:HEAP="-Xms1g -Xmx4g -XX:+UseG1GC"
.\apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log
```

Boas práticas:

- Comece com `-Xmx2g` e aumente conforme volume/erro de memória.
- Evite exagerar no heap sem monitorar CPU e GC.
- Para GUI, use heap maior apenas durante depuração; para CLI, priorize estabilidade da máquina.

## Execução com interface gráfica (GUI)

Use sempre os comandos a partir da pasta `testes-carga-pico`.

Linux/macOS:

```bash
./apache-jmeter-5.6.3/bin/jmeter
```

Windows (PowerShell):

```powershell
.\apache-jmeter-5.6.3\bin\jmeter.bat
```

Passos na interface:

1. Abra `File > Open`.
2. Selecione `site-de-viagens.jmx`.
3. Ajuste os parâmetros no `User Defined Variables`, se necessário.
4. Execute em `Run > Start`.

### Boas práticas na GUI

1. Mantenha listeners pesados desabilitados durante execução longa (`View Results Tree`, `Summary Report`).
2. Use listeners apenas para depuração rápida e com baixa concorrência.
3. Para resultados oficiais, execute em non-GUI com `-l`, `-j`, `-e`, `-o`.
4. Evite editar o `.jmx` em paralelo por várias pessoas sem revisão (risco de drift).

No plano atual, os listeners de maior consumo já estão com `enabled="false"`.

## Ultimate Thread Group (controle avançado de carga)

Quando for necessário modelar rampas em múltiplas fases (subidas/platôs/descidas), prefira o **Ultimate Thread Group**.

### Como instalar

1. Abra `Options > Plugins Manager`.
2. Procure por `Custom Thread Groups`.
3. Instale e reinicie o JMeter.

### Quando usar

- Cenários com vários degraus de carga no mesmo grupo.
- Simulação realista de horários de pico com mais de uma fase.
- Substituir múltiplos `Thread Group` simples quando a modelagem ficar complexa.

### Boas práticas

1. Nomeie cada etapa de carga com clareza.
2. Versione o `.jmx` após alterar o perfil.
3. Mantenha os parâmetros principais expostos por variáveis quando possível.

## Abrir os relatórios HTML

- Carga: `results/load/report/index.html`.
- Pico: `results/spike/report/index.html`.

Linux:

```bash
xdg-open results/load/report/index.html
xdg-open results/spike/report/index.html
```

macOS:

```bash
open results/load/report/index.html
open results/spike/report/index.html
```

Windows (PowerShell):

```powershell
Start-Process .\results\load\report\index.html
Start-Process .\results\spike\report\index.html
```

## Guia rápido

Para comandos objetivos em poucos passos, consulte `GUIA-RAPIDO-EXECUCAO.md`.

## Execução no GitHub Actions

O workflow `.github/workflows/tests-api-web.yml` também executa o plano `site-de-viagens.jmx` automaticamente no job `jmeter-blazedemo`.

Artefatos publicados no CI:

- `testes-carga-pico/results/ci-load/results.jtl`
- `testes-carga-pico/results/ci-load/jmeter.log`
- `testes-carga-pico/results/ci-load/report`
- `testes-carga-pico/results/ci-spike/results.jtl`
- `testes-carga-pico/results/ci-spike/jmeter.log`
- `testes-carga-pico/results/ci-spike/report`

