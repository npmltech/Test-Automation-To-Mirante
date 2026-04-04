# Guia Rápido de Execução

Guia curto para rodar os testes de carga e pico, com comandos diretos por sistema operacional.

## Passo 1) Validar o JMeter

Linux/macOS:

```bash
./apache-jmeter-5.6.3/bin/jmeter -v
```

Windows (PowerShell):

```powershell
.\apache-jmeter-5.6.3\bin\jmeter.bat -v
```

## Passo 2) Rodar os testes

Linux/macOS:

```bash
mkdir -p results/load results/spike
rm -rf results/load/report results/spike/report
./apache-jmeter-5.6.3/bin/jmeter -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log -e -o results/load/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=300 -JspikeThreads=0 -JspikeDelay=99999 -JspikeDuration=1 -JspikeRampUp=1
./apache-jmeter-5.6.3/bin/jmeter -n -t site-de-viagens.jmx -l results/spike/results.jtl -j results/spike/jmeter.log -e -o results/spike/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=420 -JspikeThreads=900 -JspikeDelay=180 -JspikeDuration=60 -JspikeRampUp=10
```

Windows (PowerShell):

```powershell
New-Item -ItemType Directory -Force -Path .\results\load, .\results\spike | Out-Null
Remove-Item -Recurse -Force .\results\load\report, .\results\spike\report -ErrorAction SilentlyContinue
.\apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/load/results.jtl -j results/load/jmeter.log -e -o results/load/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=300 -JspikeThreads=0 -JspikeDelay=99999 -JspikeDuration=1 -JspikeRampUp=1
.\apache-jmeter-5.6.3\bin\jmeter.bat -n -t site-de-viagens.jmx -l results/spike/results.jtl -j results/spike/jmeter.log -e -o results/spike/report -JbaseUrl=www.blazedemo.com -JloadThreads=700 -JloadRampUp=120 -JloadDuration=420 -JspikeThreads=900 -JspikeDelay=180 -JspikeDuration=60 -JspikeRampUp=10
```

## Passo 3) Abrir os relatórios

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

## GUI rápida

Linux/macOS:

```bash
./apache-jmeter-5.6.3/bin/jmeter
```

Windows (PowerShell):

```powershell
.\apache-jmeter-5.6.3\bin\jmeter.bat
```

Na interface, abra `site-de-viagens.jmx` em `File > Open`.

