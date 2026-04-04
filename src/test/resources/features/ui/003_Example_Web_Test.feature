# language: pt
# encoding: utf-8

@ui
@allure.label.owner:npmltech @allure.label.epic:Desafio_Mirante @allure.label.feature:UI_Google_Search
Funcionalidade: Realizar pesquisa no Google

  @ui_cn_c1 @allure.label.severity:normal @allure.label.story:ui_cn_c1
  Esquema do Cenário: Realizar uma pesquisa utilizando o Google Search
    Dado que eu acesso a "<url>"
    Quando eu preencho o campo de pesquisa com "<texto>"
    E clico no primeiro link da pesquisa
    Então o "<titulo>" deverá ser apresentado

    Exemplos:
      | url    | texto                  | titulo                                                                          |
      | Google | selenium documentation | The Selenium Browser Automation Project \| Selenium                             |
      | Google | terra notícias         | Notícias do Brasil, mundo, clima, tecnologia, ciências, educação e mais - Terra |
