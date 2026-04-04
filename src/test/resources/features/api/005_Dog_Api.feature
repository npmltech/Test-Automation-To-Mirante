# language: pt
# encoding: utf-8

@api @dog_api
Funcionalidade: Validar a integração com a Dog API

  - Eu, como testador, gostaria de garantir a qualidade da integração com a Dog API,
    validando os endpoints de listagem de raças, imagens por raça e imagem aleatória.

  @api_cn_e1
  Cenário: Validar a listagem completa de raças de cães
    Dado que eu acesso o endpoint de listagem de todas as raças
    Quando o http status code da dog api for 200
    Então o status da dog api deverá ser "success"
    E a lista de raças não deverá estar vazia
    E a quantidade de raças deverá ser maior que 100

  @api_cn_e2
  Cenário: Validar que raças conhecidas estão presentes na listagem
    Dado que eu acesso o endpoint de listagem de todas as raças
    Quando o http status code da dog api for 200
    Então a raça "hound" deverá constar na lista de raças
    E a raça "retriever" deverá constar na lista de raças
    E a raça "beagle" deverá constar na lista de raças

  @api_cn_e3
  Cenário: Validar as imagens de uma raça válida
    Dado que eu acesso o endpoint de imagens da raça "hound"
    Quando o http status code da dog api for 200
    Então o status da dog api deverá ser "success"
    E a lista de imagens da raça não deverá estar vazia
    E as imagens da raça deverão ser URLs válidas

  @api_cn_e4
  Cenário: Validar a obtenção de uma imagem aleatória de cão
    Dado que eu acesso o endpoint de imagem aleatória de cão
    Quando o http status code da dog api for 200
    Então o status da dog api deverá ser "success"
    E a URL da imagem aleatória não deverá estar vazia
    E a URL da imagem aleatória deverá iniciar com "https://"

  @api_cn_e5
  Cenário: Validar o retorno de erro ao consultar uma raça inexistente
    Dado que eu acesso o endpoint de imagens da raça "racaxyz_invalida"
    Quando o http status code da dog api for 404
    Então o status da dog api deverá ser "error"
    E a mensagem de erro da dog api deverá conter "Breed not found"
