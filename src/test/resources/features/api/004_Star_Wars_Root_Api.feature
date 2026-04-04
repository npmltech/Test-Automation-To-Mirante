# language: pt
# encoding: utf-8

@api @star_wars
@allure.label.owner:qualidade @allure.label.epic:Desafio_Mirante @allure.label.feature:API_Star_Wars_Root
Funcionalidade: Validar a API raiz de informações do Star Wars

  - Eu, como testador, gostaria de realizar o teste da API Root de Star Wars,
    a fim de validar a resposta.

  @api_cn_d1 @allure.label.severity:normal @allure.label.story:api_cn_d1
  Cenário: Validar se o retorno da API Root de Star Wars está OK
    Dado que eu acesso a api root
    Quando o http status code da api root for 200
    Então os serviços deverão ser apresentados nos serviços
      | services                         |
      | https://swapi.dev/api/films/     |
      | https://swapi.dev/api/people/    |
      | https://swapi.dev/api/planets/   |
      | https://swapi.dev/api/species/   |
      | https://swapi.dev/api/vehicles/  |
      | https://swapi.dev/api/starships/ |
