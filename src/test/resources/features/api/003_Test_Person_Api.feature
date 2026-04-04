# language: pt
# encoding: utf-8

@api
Funcionalidade: Validar a API Person

  - Eu, como testador, gostaria de realizar o teste de API
    (Person - CRUD) que publiquei no Heroku.

  Contexto:
    Dado que eu acesso a api para geração do token

  @api_cn_c1
  Esquema do Cenário: Validar inserção de uma pessoa
    Quando eu realizar a chamada para a api person inserindo "<nome>", "<idade>" e "<comentarios>"
    Então o status code deverá ser 200

    Exemplos:
      | nome            | idade | comentarios |
      | EDUARDO TESTE   | 38    | INSERT 1    |
      | JONAS SILVA ABC | 39    | INSERT 2    |

  @api_cn_c2
  Esquema do Cenário: Validar alteração de uma pessoa cadastrada
    Quando eu realizar a chamada para api person atualizando "<id>", "<nome>", "<idade>" e "<comentarios>"
    Então o status code deverá ser 200

    Exemplos:
      | id | nome         | idade | comentarios            |
      | 14 | TESTE PESSOA | 50    | TESTE TESTE COMENTÁRIO |
      | 15 | BRUNO TESTE  | 60    | Teste OK               |

  @api_cn_c3
  Cenário: Validar a lista de pessoas cadastradas
    Quando eu realizar uma chamada para a api persons
    Então a lista de pessoas cadastradas deverá ser retornada

  @api_cn_c4
  Esquema do Cenário: Validar pessoa cadastrada utilizando id para a pesquisa
    Quando eu chamo a api person e passo o paramêtro "<id>" para a pesquisa
    Então deverá retornar o "<nome>", a "<idade>" e o "<comentarios>"

    Exemplos:
      | id | nome            | idade | comentarios |
      | 43 | EDUARDO TESTE   | 38    | INSERT 1    |
      | 44 | JONAS SILVA ABC | 39    | INSERT 2    |

  @api_cn_c5
  Esquema do Cenário: Validar a exclusão de uma pessoa utilizando id
    Quando eu chamo a api person e passo o paramêtro "<id>" para exclusão
    Então o status code deverá ser 200

    Exemplos:
      | id |
      | 44 |
