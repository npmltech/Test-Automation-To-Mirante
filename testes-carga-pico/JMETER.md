# Testes de Carga com JMeter (BlazeDemo)

Este diretório contém o plano `site-de-viagens.jmx` para validar o fluxo de compra de passagens em `https://www.blazedemo.com`.

## Visão rápida

- Cenário **carga**: volume constante de usuários virtuais.
- Cenário **pico**: carga base + aumento abrupto (spike).
- Saídas da execução: `results.jtl`, `jmeter.log` e dashboard HTML.

## Pré-requisitos

- Java 11 ou superior (recomendado: Java 17+)
- JMeter 5.6.3

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

## Execução sem GUI (mais simples)

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

## Abrir os relatórios HTML

- Carga: `results/load/report/index.html`
- Pico: `results/spike/report/index.html`

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

## Execução com interface gráfica

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
5. Analise os listeners configurados no plano.

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

