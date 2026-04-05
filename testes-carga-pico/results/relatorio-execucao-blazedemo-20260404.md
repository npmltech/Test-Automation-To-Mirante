# Relatório de Execução - Teste de Performance

Teste de performance da aplicação de compra de passagens, com dois cenários executados para validar um período de carga e outro de pico durante o fluxo de compra, com foco no objetivo definido pelo time solicitante.

## Objetivo

Executar dois cenários de teste de performance (carga e pico) para validar os critérios de aceitação definidos pelo time solicitante.

## Critérios de aceitação

- Vazão mínima: 250 requisições/segundo.
- Tempo de resposta p90: menor que 2 segundos.

## Informações iniciais

- Data do teste: 2026-04-04
- Sistema-alvo: https://www.blazedemo.com
- Plano utilizado: `site-de-viagens.jmx`

## Cenários executados

Foram executados os dois cenários abaixo, conforme solicitado e alinhado no plano de testes.

### Teste de carga (sustentado)

- Arquivo JTL: `results/blazedemo-load-20260404-111758/results.jtl`
- Relatório HTML: `results/blazedemo-load-20260404-111758/report/index.html`
- Parâmetros: `threads=1200`, `rampUp=60s`, `duration=120s`

### Teste de pico (subida abrupta)

- Arquivo JTL: `results/blazedemo-spike-20260404-111347/results.jtl`
- Relatório HTML: `results/blazedemo-spike-20260404-111347/report/index.html`
- Parâmetros: `threads=1600`, `rampUp=10s`, `duration=120s`

## Métricas consolidadas

A seguir, os resultados obtidos na execução.

### Resultado - Teste de carga

- Vazão média (throughput): 1054.91 req/s
- p90: 7665 ms (7.665 s)
- Taxa de erro: 0.18%
- Conclusão do critério: **REPROVADO** (p90 acima de 2 s)

Para o teste de carga, foram aplicadas boas práticas de ramp-up (subida gradual/graceful scaling) e, ao fim do ramp-up, período de carga constante (steady state), com execução em 1200 threads.

### Resultado - Teste de pico

- Vazão média (throughput): 771.97 req/s
- p90: 13377 ms (13.377 s)
- Taxa de erro: 4.39%
- Conclusão do critério: **REPROVADO** (p90 acima de 2 s)

Para o teste de pico, foram aplicadas boas práticas de ramp-up (subida gradual/graceful scaling) e, ao fim do ramp-up, um pico superior ao cenário de carga, com 1600 threads, simulando aumento abrupto e inesperado de concorrência.

## Coleta e análise

Durante a coleta e análise dos resultados, foram observados os seguintes pontos:

- A vazão ficou acima de 250 req/s nos dois cenários.
- O critério falhou por latência no percentil 90, que ficou muito acima de 2 s em ambos os testes.
- No pico, além da piora de latência, houve aumento relevante da taxa de erros, indicando degradação sob subida agressiva de concorrência.
- O plano possui múltiplas requisições por transação de compra, além de requisições de página e recursos; isso aumenta o volume total de requisições e pressiona mais a aplicação.

## Conclusão final

O resultado foi **NÃO SATISFATÓRIO** com base nos critérios de aceitação para os cenários de compra de passagens, pois o p90 permaneceu acima de 2 segundos.

Mesmo com vazão acima do mínimo exigido, a latência de p90 inviabiliza a aprovação do teste.

## Recomendações

Recomenda-se ao time solicitante:

- Avaliar aumento de recursos computacionais (CPU e memória) para melhorar o desempenho do sistema.
- Realizar uma revisão técnica das camadas de infraestrutura, aplicação e banco de dados para aprofundar a análise de erros (profiling).
- Incluir ferramentas de monitoramento e profiling (APM) para melhorar visibilidade, acompanhamento e coleta de dados, enriquecendo a análise e a qualidade dos relatórios.

## Referência operacional

Para reproduzir os cenários com boas práticas de execução, consulte:

- `testes-carga-pico/JMETER.md`

