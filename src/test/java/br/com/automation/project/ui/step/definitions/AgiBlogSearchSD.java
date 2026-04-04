package br.com.automation.project.ui.step.definitions;

import br.com.automation.project.ui.pages.PageRegister;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.Locale;

public class AgiBlogSearchSD extends PageRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgiBlogSearchSD.class);

    @Dado("que eu acesso o blog do Agi")
    public void queEuAcessoOBlogDoAgi() {
        LOGGER.info("[AgiBlogSearchSD] Acessando o blog do Agi.");
        getAgiBlogSearchPG().accessAgiBlog();
    }

    @Quando("eu pesquiso no blog do Agi por {string}")
    public void euPesquisoNoBlogDoAgiPor(String termo) {
        LOGGER.info("[AgiBlogSearchSD] Pesquisando no blog do Agi pelo termo: {}", termo);
        getAgiBlogSearchPG().searchFor(termo);
    }

    @Então("o título da página de resultados do blog deverá ser exibido")
    public void oTituloDaPaginaDeResultadosDoBlogDeveraSerExibido() {
        LOGGER.info("[AgiBlogSearchSD] Validando exibição do título da página de resultados.");
        Assertions.assertFalse(getAgiBlogSearchPG().getSearchResultsTitle().isBlank(),
            "O título da página de resultados não foi exibido.");
    }

    @Então("o termo pesquisado {string} deverá ser apresentado na página de resultados do blog")
    public void oTermoPesquisadoDeveraSerApresentadoNaPaginaDeResultadosDoBlog(String termo) {
        LOGGER.info("[AgiBlogSearchSD] Validando termo pesquisado na página de resultados: {}", termo);
        String tituloResultados = getAgiBlogSearchPG().getSearchResultsTitle();
        Assertions.assertTrue(normalize(tituloResultados).contains(normalize(termo)),
            String.format("O título de resultados '%s' não contém o termo '%s'.", tituloResultados, termo));
    }

    @Então("a URL da busca deverá conter o termo pesquisado {string}")
    public void aUrlDaBuscaDeveraConterOTermoPesquisado(String termo) {
        LOGGER.info("[AgiBlogSearchSD] Validando URL da busca para o termo: {}", termo);
        String termoNaQuery = getAgiBlogSearchPG().getSearchQueryTerm();
        Assertions.assertEquals(normalize(termo), normalize(termoNaQuery),
            String.format("O termo da query '%s' é diferente do esperado '%s'.", termoNaQuery, termo));
    }

    @Então("ao menos {int} artigo deverá ser listado nos resultados do blog")
    public void aoMenosArtigoDeveraSerListadoNosResultadosDoBlog(Integer quantidadeMinima) {
        LOGGER.info("[AgiBlogSearchSD] Validando quantidade mínima de resultados: {}", quantidadeMinima);
        int quantidadeResultados = getAgiBlogSearchPG().getResultCount();
        Assertions.assertTrue(quantidadeResultados >= quantidadeMinima,
            String.format("A quantidade de resultados (%d) é menor que o mínimo esperado (%d).", quantidadeResultados,
                quantidadeMinima));
    }

    @Então("nenhum artigo deverá ser listado nos resultados do blog")
    public void nenhumArtigoDeveraSerListadoNosResultadosDoBlog() {
        LOGGER.info("[AgiBlogSearchSD] Validando ausência de resultados na busca.");
        Assertions.assertEquals(0, getAgiBlogSearchPG().getResultCount(),
            "Foram encontrados artigos quando o esperado era zero resultados.");
    }

    @Então("a quantidade de artigos retornados deverá ser {int}")
    public void aQuantidadeDeArtigosRetornadosDeveraSer(Integer quantidadeEsperada) {
        LOGGER.info("[AgiBlogSearchSD] Validando quantidade exata de resultados: {}", quantidadeEsperada);
        Assertions.assertEquals(quantidadeEsperada.intValue(), getAgiBlogSearchPG().getResultCount(),
            "A quantidade de resultados retornada é diferente da esperada.");
    }

    @Então("os títulos dos artigos retornados não deverão estar vazios")
    public void osTitulosDosArtigosRetornadosNaoDeveraoEstarVazios() {
        LOGGER.info("[AgiBlogSearchSD] Validando se os títulos dos resultados estão preenchidos.");
        Assertions.assertFalse(getAgiBlogSearchPG().getResultTitles().isEmpty(),
            "A lista de títulos retornados está vazia.");
        long titulosPreenchidos = getAgiBlogSearchPG().getResultTitles().stream().map(String::trim)
            .filter(title -> !title.isBlank()).count();
        Assertions.assertTrue(titulosPreenchidos > 0,
            "Nenhum artigo com título preenchido foi encontrado nos resultados.");
    }

    private String normalize(String value) {
        String normalized = value == null ? "" : value.toLowerCase(Locale.ROOT);
        return Normalizer.normalize(normalized, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
    }
}
