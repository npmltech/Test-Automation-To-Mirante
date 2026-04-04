# language: pt
# encoding: utf-8

@api
Funcionalidade: Gerar o token para acesso aos endpoints publicados no Heroku

  - Eu, como testador, gostaria de gerar um token para acessar as APIs
    que foram desenvolvidas e publicadas no Heroku.

  @api_cn_b1
  Cenário: Validar a API de geração de token de acesso
    Dado que eu acesso a api para geração do token
    Então um token de acesso deverá ser gerado

  @api_cn_b2
  Esquema do Cenário: Validar a API de geração de token de acesso usando os parâmetros de usuário e senha
    Dado que eu acesso a api para geração do token usando o "<usuario>" e "<senha>"
    Então um token de acesso deverá ser gerado

    Exemplos:
      | usuario | senha    |
      | abc     | 12345678 |
