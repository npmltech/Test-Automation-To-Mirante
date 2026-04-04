# language: pt
# encoding: utf-8

@ui
Funcionalidade: Realizar pesquisa no Google

  @ui_cn_b1
  Esquema do Cenário: Realizar uma pesquisa utilizando o Google Search 1
    Dado que eu acesso a "<url>"
    Quando eu preencho o campo de pesquisa com "<texto>"
    E clico no primeiro link da pesquisa
    Então o "<titulo>" deverá ser apresentado

    Exemplos:
      | url    | texto                  | titulo                                                                          |
      | Google | selenium documentation | The Selenium Browser Automation Project \| Selenium                             |
      | Google | terra notícias         | Notícias do Brasil, mundo, clima, tecnologia, ciências, educação e mais - Terra |

  @ui_cn_b2
  Esquema do Cenário: Realizar uma pesquisa utilizando o Google Search 2
    Dado que eu acesso a "<url>"
    Quando eu preencho o campo de pesquisa com "<texto>"
    E clico no primeiro link da pesquisa
    Então o "<titulo>" deverá ser apresentado

    Exemplos:
      | url    | texto                  | titulo                                                                          |
      | Google | selenium documentation | The Selenium Browser Automation Project \| Selenium                             |
      | Google | terra notícias         | Notícias do Brasil, mundo, clima, tecnologia, ciências, educação e mais - Terra |
