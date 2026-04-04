package br.com.automation.project.api.step.definitions;

import br.com.automation.project.api.ApiRegister;
import br.com.automation.project.api.model.HeaderClass;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenHerokuSD extends ApiRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenHerokuSD.class);

    @Dado("que eu acesso a api para geração do token")
    public void queEuAcessoAApiParaGeraçãoDoToken() {
        LOGGER.info("[AccessTokenHerokuSD] Solicitando token de acesso com credenciais padrão.");
        getAccessTokenHerokuApi().getAccessToken();
    }

    @Dado("que eu acesso a api para geração do token usando o {string} e {string}")
    public void queEuAcessoAApiParaGeraçãoDoTokenUsandoOE(String usuario, String senha) {
        LOGGER.info("[AccessTokenHerokuSD] Solicitando token de acesso: usuário={}", usuario);
        getAccessTokenHerokuApi().getAccessToken(usuario, senha);
    }

    @Então("um token de acesso deverá ser gerado")
    public void umTokenDeAcessoDeveráSerGerado() {
        LOGGER.info("[AccessTokenHerokuSD] Token gerado: {}", HeaderClass.getToken());
    }
}
