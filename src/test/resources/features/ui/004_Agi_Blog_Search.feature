# language: pt
# encoding: utf-8

@ui @agi_blog
@allure.label.owner:qualidade @allure.label.epic:Desafio_Mirante @allure.label.feature:UI_Agi_Blog
Funcionalidade: Validar a busca de artigos no blog do Agi

  - Eu, como testador, gostaria de validar a busca de artigos pela lupa no blog do Agi,
    garantindo que o usuário encontre conteúdos relevantes e também tenha um comportamento consistente quando não houver resultados.

  Contexto:
    Dado que eu acesso o blog do Agi

  @ui_cn_d1 @allure.label.severity:critical @allure.label.story:ui_cn_d1
  Cenário: Pesquisar por um termo válido e exibir artigos relacionados
    Quando eu pesquiso no blog do Agi por "pix"
    Então o título da página de resultados do blog deverá ser exibido
    Então o termo pesquisado "pix" deverá ser apresentado na página de resultados do blog
    E a URL da busca deverá conter o termo pesquisado "pix"
    E ao menos 1 artigo deverá ser listado nos resultados do blog
    E os títulos dos artigos retornados não deverão estar vazios

  @ui_cn_d2 @allure.label.severity:critical @allure.label.story:ui_cn_d2
  Cenário: Pesquisar por um termo inexistente e não exibir artigos
    Quando eu pesquiso no blog do Agi por "termoquejamaisdeveexistirxyz"
    Então o título da página de resultados do blog deverá ser exibido
    Então o termo pesquisado "termoquejamaisdeveexistirxyz" deverá ser apresentado na página de resultados do blog
    E a URL da busca deverá conter o termo pesquisado "termoquejamaisdeveexistirxyz"
    E nenhum artigo deverá ser listado nos resultados do blog

  @ui_cn_d3 @allure.label.severity:critical @allure.label.story:ui_cn_d3
  Cenário: Pesquisar por termo composto e validar codificação da busca
    Quando eu pesquiso no blog do Agi por "conta digital"
    Então o título da página de resultados do blog deverá ser exibido
    E o termo pesquisado "conta digital" deverá ser apresentado na página de resultados do blog
    E a URL da busca deverá conter o termo pesquisado "conta digital"
    E ao menos 1 artigo deverá ser listado nos resultados do blog
    E os títulos dos artigos retornados não deverão estar vazios

  @ui_cn_d4 @allure.label.severity:critical @allure.label.story:ui_cn_d4
  Cenário: Realizar duas buscas consecutivas e validar atualização dos resultados
    Quando eu pesquiso no blog do Agi por "pix"
    Então ao menos 1 artigo deverá ser listado nos resultados do blog
    Quando eu pesquiso no blog do Agi por "termoquejamaisdeveexistirxyz"
    Então o título da página de resultados do blog deverá ser exibido
    E a URL da busca deverá conter o termo pesquisado "termoquejamaisdeveexistirxyz"
    E a quantidade de artigos retornados deverá ser 0

  @ui_cn_d5 @allure.label.severity:critical @allure.label.story:ui_cn_d5
  Esquema do Cenário: Pesquisar palavras-chave relevantes usando exemplos
    Quando eu pesquiso no blog do Agi por "<termo>"
    Então o título da página de resultados do blog deverá ser exibido
    E o termo pesquisado "<termo>" deverá ser apresentado na página de resultados do blog
    E a URL da busca deverá conter o termo pesquisado "<termo>"
    E ao menos 1 artigo deverá ser listado nos resultados do blog

    Exemplos:
      | termo       |
      | pix         |
      | empréstimos |
      | dinheiro    |
