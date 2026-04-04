package br.com.automation.project.api.domain.dog.step.definitions;

import br.com.automation.project.api.domain.dog.error.DogApiException;
import br.com.automation.project.api.domain.dog.model.DogBreedImagesBO;
import br.com.automation.project.api.domain.dog.model.DogBreedsListBO;
import br.com.automation.project.api.domain.dog.model.DogRandomImageBO;
import br.com.automation.project.api.domain.dog.request.DogApiRequest;
import br.com.automation.project.utils.GlobalUtils;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DogApiSD {

    private static final Logger LOGGER = LoggerFactory.getLogger(DogApiSD.class);

    private DogBreedsListBO breedsListBO;
    private DogBreedImagesBO breedImagesBO;
    private DogRandomImageBO randomImageBO;
    // noinspection unused
    private DogApiException lastApiException;

    private final DogApiRequest request = new DogApiRequest();

    @Dado("que eu acesso o endpoint de listagem de todas as raças")
    public void queEuAcessoOEndpointDeListagemDeTodasAsRacas() {
        LOGGER.info("[DogApiSD] Acessando o endpoint de listagem de todas as raças.");
        resetState();
        try {
            this.breedsListBO = this.request.requestAllBreeds();
            LOGGER.info("[DogApiSD] Resposta recebida: {}", this.breedsListBO);
        } catch (DogApiException ex) {
            LOGGER.warn("[DogApiSD] Falha ao acessar o endpoint de listagem: status={}", ex.getHttpStatusCode(), ex);
            this.lastApiException = ex;
        }
    }

    @Dado("que eu acesso o endpoint de imagens da raça {string}")
    public void queEuAcessoOEndpointDeImagensDaRaca(String breed) {
        LOGGER.info("[DogApiSD] Acessando o endpoint de imagens da raça: {}", breed);
        resetState();
        try {
            this.breedImagesBO = this.request.requestBreedImages(breed);
            LOGGER.info("[DogApiSD] Resposta recebida: {}", this.breedImagesBO);
        } catch (DogApiException ex) {
            LOGGER.warn("[DogApiSD] Falha ao acessar o endpoint de imagens da raça {}: status={}", breed,
                ex.getHttpStatusCode(), ex);
            this.lastApiException = ex;
        }
    }

    @Dado("que eu acesso o endpoint de imagem aleatória de cão")
    public void queEuAcessoOEndpointDeImagemAleatoriaDeCAo() {
        LOGGER.info("[DogApiSD] Acessando o endpoint de imagem aleatória.");
        resetState();
        try {
            this.randomImageBO = this.request.requestRandomImage();
            LOGGER.info("[DogApiSD] Resposta recebida: {}", this.randomImageBO);
        } catch (DogApiException ex) {
            LOGGER.warn("[DogApiSD] Falha ao acessar o endpoint de imagem aleatória: status={}", ex.getHttpStatusCode(),
                ex);
            this.lastApiException = ex;
        }
    }

    @Quando("o http status code da dog api for {int}")
    public void oHttpStatusCodeDaDogApiFor(Integer httpStatusCode) {
        LOGGER.info("[DogApiSD] Validando código de status HTTP: esperado={}", httpStatusCode);
        GlobalUtils.assertHttpStatusCode(httpStatusCode);
    }

    @Então("o status da dog api deverá ser {string}")
    public void oStatusDaDogApiDeveraSer(String status) {
        LOGGER.info("[DogApiSD] Validando status da resposta: {}", status);
        GlobalUtils.compareValueResponseString(status);
    }

    @Então("a lista de raças não deverá estar vazia")
    public void aListaDeRacasNaoDeveraEstarVazia() {
        LOGGER.info("[DogApiSD] Validando se a lista de raças não está vazia.");
        Assertions.assertNotNull(this.breedsListBO, "O modelo de listagem de raças não foi obtido.");
        Assertions.assertFalse(this.breedsListBO.getBreeds().isEmpty(), "A lista de raças não deveria estar vazia.");
    }

    @Então("a quantidade de raças deverá ser maior que {int}")
    public void aQuantidadeDeRacasDeveraSerMaiorQue(Integer minCount) {
        LOGGER.info("[DogApiSD] Validando se a quantidade de raças é maior que {}.", minCount);
        Assertions.assertNotNull(this.breedsListBO, "O modelo de listagem de raças não foi obtido.");
        Assertions.assertTrue(this.breedsListBO.getBreeds().size() > minCount, String
            .format("A quantidade de raças (%d) não é maior que %d.", this.breedsListBO.getBreeds().size(), minCount));
    }

    @Então("a raça {string} deverá constar na lista de raças")
    public void aRacaDeveraConstarNaListaDeRacas(String breed) {
        LOGGER.info("[DogApiSD] Validando se a raça '{}' está na lista.", breed);
        Assertions.assertNotNull(this.breedsListBO, "O modelo de listagem de raças não foi obtido.");
        Assertions.assertTrue(this.breedsListBO.getBreeds().containsKey(breed),
            String.format("A raça '%s' não foi encontrada na lista de raças.", breed));
    }

    @Então("a lista de imagens da raça não deverá estar vazia")
    public void aListaDeImagensDaRacaNaoDeveraEstarVazia() {
        LOGGER.info("[DogApiSD] Validando se a lista de imagens não está vazia.");
        Assertions.assertNotNull(this.breedImagesBO, "O modelo de imagens da raça não foi obtido.");
        Assertions.assertFalse(this.breedImagesBO.getImages().isEmpty(),
            "A lista de imagens da raça não deveria estar vazia.");
    }

    @Então("as imagens da raça deverão ser URLs válidas")
    public void asImagensDaRacaDeveraoSerURLsValidas() {
        LOGGER.info("[DogApiSD] Validando URLs das imagens da raça.");
        Assertions.assertNotNull(this.breedImagesBO, "O modelo de imagens da raça não foi obtido.");
        this.breedImagesBO.getImages().forEach(url -> {
            LOGGER.debug("[DogApiSD] Validando URL: {}", url);
            Assertions.assertTrue(url != null && url.startsWith("https://"),
                String.format("A URL '%s' não é uma URL HTTPS válida.", url));
        });
    }

    @Então("a URL da imagem aleatória não deverá estar vazia")
    public void aUrlDaImagemAleatoriaNaoDeveraEstarVazia() {
        LOGGER.info("[DogApiSD] Validando se a URL da imagem aleatória não está vazia.");
        Assertions.assertNotNull(this.randomImageBO, "O modelo de imagem aleatória não foi obtido.");
        Assertions.assertFalse(this.randomImageBO.getImageUrl() == null || this.randomImageBO.getImageUrl().isBlank(),
            "A URL da imagem aleatória não deveria estar vazia.");
    }

    @Então("a URL da imagem aleatória deverá iniciar com {string}")
    public void aUrlDaImagemAleatoriaDeveraIniciarCom(String prefix) {
        LOGGER.info("[DogApiSD] Validando se a URL da imagem aleatória inicia com '{}'.", prefix);
        Assertions.assertNotNull(this.randomImageBO, "O modelo de imagem aleatória não foi obtido.");
        Assertions.assertTrue(
            this.randomImageBO.getImageUrl() != null && this.randomImageBO.getImageUrl().startsWith(prefix),
            String.format("A URL '%s' não inicia com '%s'.", this.randomImageBO.getImageUrl(), prefix));
    }

    @Então("a mensagem de erro da dog api deverá conter {string}")
    public void aMensagemDeErroDaDogApiDeveraConter(String mensagemEsperada) {
        LOGGER.info("[DogApiSD] Validando mensagem de erro: '{}'.", mensagemEsperada);
        Assertions.assertNotNull(this.lastApiException, "Nenhuma exceção foi lançada pela Dog API.");
        String mensagemAtual = this.lastApiException.getErrorResponse() != null
            ? this.lastApiException.getErrorResponse().getMessage()
            : "";
        Assertions.assertTrue(mensagemAtual != null && mensagemAtual.contains(mensagemEsperada),
            String.format("A mensagem de erro '%s' não contém '%s'.", mensagemAtual, mensagemEsperada));
    }

    private void resetState() {
        this.breedsListBO = null;
        this.breedImagesBO = null;
        this.randomImageBO = null;
        this.lastApiException = null;
    }
}
