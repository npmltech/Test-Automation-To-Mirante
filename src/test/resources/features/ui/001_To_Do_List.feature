# language: pt
# encoding: utf-8

@ui
@allure.label.owner:qualidade @allure.label.epic:Desafio_Mirante @allure.label.feature:UI_Todo_List
Funcionalidade: Carregar dados em uma lista de tarefas - Todo List

  Contexto:
    Dado que eu acesso a url de criação de lista de tarefas

  @ui_cn_a1 @allure.label.severity:normal @allure.label.story:ui_cn_a1
  Cenário: Realizar a inserção de itens em uma lista de tarefas
    Quando eu inserir os itens na lista de tarefas
      | tarefas |
      | item 1  |
      | item 2  |
      | item 3  |
      | item 4  |
      | item 5  |
    Então os dados incluídos deverão estar dispostos na lista

  @ui_cn_a2 @allure.label.severity:normal @allure.label.story:ui_cn_a2
  Cenário: Realizar a exclusão de itens em uma lista de tarefas
    Então ao clicar no botão de exclusão, os itens deverão ser excluídos

  @ui_cn_a3 @allure.label.severity:normal @allure.label.story:ui_cn_a3
  Cenário: Realizar a exclusão de dados em uma lista de tarefas - seleção e exclusão
    Quando eu inserir os itens na lista de tarefas
      | tarefas |
      | item 1  |
      | item 2  |
      | item 3  |
      | item 4  |
      | item 5  |
    Então ao clicar no botão Toggle All, todos itens deverão ser selecionados
    E ao clicar no botão Clear Completed, todos os itens deverão ser excluídos
