# Relatório de Execução - Teste Técnico de Performance

Data: 2026-04-04
Alvo: https://www.blazedemo.com
Plano utilizado: Teste-no-Site-de-ViAGENS.jmx

## Cenários executados

1. Teste de carga (sustentado)
- Arquivo JTL: results/blazedemo-load-20260404-111758/results.jtl
- Relatório HTML: results/blazedemo-load-20260404-111758/report/index.html
- Parâmetros: threads=1200, rampUp=60s, duration=120s

2. Teste de pico (subida abrupta)
- Arquivo JTL: results/blazedemo-spike-20260404-111347/results.jtl
- Relatório HTML: results/blazedemo-spike-20260404-111347/report/index.html
- Parâmetros: threads=1600, rampUp=10s, duration=120s

## Métricas consolidadas (calculadas a partir do JTL)

Critério de Aceitação:
- Vazão mínima: 250 requisições/segundo
- Tempo de resposta p90: menor que 2 segundos

### Resultado - Carga
- Throughput médio: 1054.91 req/s
- p90: 7665 ms (7.665 s)
- Taxa de erro: 0.18%
- Conclusão do critério: REPROVADO (p90 acima de 2s)

### Resultado - Pico
- Throughput médio: 771.97 req/s
- p90: 13377 ms (13.377 s)
- Taxa de erro: 4.39%
- Conclusão do critério: REPROVADO (p90 acima de 2s)

## Análise e justificativa técnica

- A vazão ficou acima de 250 req/s nos dois cenários.
- O critério falhou por latência no percentil 90, que ficou muito acima de 2s em ambos os testes.
- No pico, além da latência pior, houve aumento relevante de erros, indicando degradação sob subida agressiva de concorrência.
- O plano original possui múltiplas requisições por transação de compra e também requisições de página/recursos; isso aumenta o volume total de requests e pressiona mais a aplicação.

## Conclusão final

O critério de aceitação NÃO foi satisfatório para o cenário de compra de passagem quando avaliado pelo p90 < 2s.
Mesmo com vazão acima do mínimo exigido, a latência de p90 invalida a aprovação do teste.
