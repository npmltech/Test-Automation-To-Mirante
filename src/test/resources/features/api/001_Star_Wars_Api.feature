# language: pt
# encoding: utf-8

@api @star_wars
@allure.label.owner:npmltech @allure.label.epic:Desafio_Mirante @allure.label.feature:API_Star_Wars
Funcionalidade: Validar a API de personagens de Star Wars

  - Eu, como testador, gostaria de realizar testes de APIs de Star Wars,
    validando alguns de seus parâmetros.
  - Neste teste, serão validados dados de personagens da franquia.

  @api_cn_a1 @allure.label.severity:normal @allure.label.story:api_cn_a1
  Esquema do Cenário: Validar a API People - Luke Skywalker
    Dado que eu acesso a api people "<people>"
    Quando o http status code da api people for <status_code>
    Então o parâmetro nome da personagem deverá ser "<nome>"
    E a altura deverá ser "<altura>"
    E o peso deverá ser "<peso>"
    E a cor de cabelo deverá ser "<cor_cabelo>"
    E a cor de pele deverá ser "<cor_pele>"
    E a cor do olho deverá ser "<cor_olho>"
    E a data de nascimento deverá ser "<data_nascimento>"
    E o gênero deverá ser "<genero>"
    E o planeta natal deverá ser apresentado no serviço "<planeta_natal>"
    E os filmes deverão ser apresentados nos serviços
      | films                          |
      | https://swapi.dev/api/films/2/ |
      | https://swapi.dev/api/films/6/ |
      | https://swapi.dev/api/films/3/ |
      | https://swapi.dev/api/films/1/ |
    E as espécies deverão ser apresentados no serviço
      | species |
      | human   |
    E os veículos deverão ser apresentados no serviço
      | vehicles                           |
      | https://swapi.dev/api/vehicles/14/ |
      | https://swapi.dev/api/vehicles/30/ |
    E as naves estelares deverão ser apresentados no serviço
      | starships                           |
      | https://swapi.dev/api/starships/12/ |
      | https://swapi.dev/api/starships/22/ |
    E a data de criação deverá ser "<data_criacao>"
    E a data de edição deverá ser "<data_edicao>"
    E o endereço de acesso contendo as informação deverá ser "<url>"

    Exemplos:
      | people | status_code | nome           | altura | peso | cor_cabelo | cor_pele | cor_olho | data_nascimento | genero | planeta_natal                    | data_criacao                | data_edicao                 | url                             |
      | 1      | 200         | Luke Skywalker | 172    | 77   | blond      | fair     | blue     | 19BBY           | male   | https://swapi.dev/api/planets/1/ | 2014-12-09T13:50:51.644000Z | 2014-12-20T21:17:56.891000Z | https://swapi.dev/api/people/1/ |

  @api_cn_a2 @allure.label.severity:normal @allure.label.story:api_cn_a2
  Esquema do Cenário: Validar a API People - Darth Vader
    Dado que eu acesso a api people "<people>"
    Quando o http status code da api people for <status_code>
    Então o parâmetro nome da personagem deverá ser "<nome>"
    E a altura deverá ser "<altura>"
    E o peso deverá ser "<peso>"
    E a cor de cabelo deverá ser "<cor_cabelo>"
    E a cor de pele deverá ser "<cor_pele>"
    E a cor do olho deverá ser "<cor_olho>"
    E a data de nascimento deverá ser "<data_nascimento>"
    E o gênero deverá ser "<genero>"
    E o planeta natal deverá ser apresentado no serviço "<planeta_natal>"
    E os filmes deverão ser apresentados nos serviços
      | films                          |
      | https://swapi.dev/api/films/1/ |
      | https://swapi.dev/api/films/2/ |
      | https://swapi.dev/api/films/3/ |
      | https://swapi.dev/api/films/6/ |
    E as espécies deverão ser apresentados no serviço
      | species |
    E os veículos deverão ser apresentados no serviço
      | vehicles |
    E as naves estelares deverão ser apresentados no serviço
      | starships                           |
      | https://swapi.dev/api/starships/13/ |
    E a data de criação deverá ser "<data_criacao>"
    E a data de edição deverá ser "<data_edicao>"
    E o endereço de acesso contendo as informação deverá ser "<url>"

    Exemplos:
      | people | status_code | nome        | altura | peso | cor_cabelo | cor_pele | cor_olho | data_nascimento | genero | planeta_natal                    | data_criacao                | data_edicao                 | url                             |
      | 4      | 200         | Darth Vader | 202    | 136  | none       | white    | yellow   | 41.9BBY         | male   | https://swapi.dev/api/planets/1/ | 2014-12-10T15:18:20.704000Z | 2014-12-20T21:17:50.313000Z | https://swapi.dev/api/people/4/ |

  @api_cn_a3 @allure.label.severity:normal @allure.label.story:api_cn_a3
  Esquema do Cenário: Validar a API People - Leia Organa
    Dado que eu acesso a api people "<people>"
    Quando o http status code da api people for <status_code>
    Então o parâmetro nome da personagem deverá ser "<nome>"
    E a altura deverá ser "<altura>"
    E o peso deverá ser "<peso>"
    E a cor de cabelo deverá ser "<cor_cabelo>"
    E a cor de pele deverá ser "<cor_pele>"
    E a cor do olho deverá ser "<cor_olho>"
    E a data de nascimento deverá ser "<data_nascimento>"
    E o gênero deverá ser "<genero>"
    E o planeta natal deverá ser apresentado no serviço "<planeta_natal>"
    E os filmes deverão ser apresentados nos serviços
      | films                          |
      | https://swapi.dev/api/films/1/ |
      | https://swapi.dev/api/films/2/ |
      | https://swapi.dev/api/films/3/ |
      | https://swapi.dev/api/films/6/ |
    E as espécies deverão ser apresentados no serviço
      | species |
    E os veículos deverão ser apresentados no serviço
      | vehicles                           |
      | https://swapi.dev/api/vehicles/30/ |
    E as naves estelares deverão ser apresentados no serviço
      | starships |
    E a data de criação deverá ser "<data_criacao>"
    E a data de edição deverá ser "<data_edicao>"
    E o endereço de acesso contendo as informação deverá ser "<url>"

    Exemplos:
      | people | status_code | nome         | altura | peso | cor_cabelo | cor_pele | cor_olho | data_nascimento | genero | planeta_natal                    | data_criacao                | data_edicao                 | url                             |
      | 5      | 200         | Leia Organa  | 150    | 49   | brown      | light    | brown    | 19BBY           | female | https://swapi.dev/api/planets/2/ | 2014-12-10T15:20:09.791000Z | 2014-12-20T21:17:50.315000Z | https://swapi.dev/api/people/5/ |
