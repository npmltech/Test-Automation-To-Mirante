package br.com.automation.project.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe utilitária para conversão de arquivos em objeto JSON, mapa e string,
 * além de operações de criação, verificação e exclusão de diretórios e
 * arquivos.
 *
 * @author Niky Lima
 */
public class ManagerFileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerFileUtils.class);

    private ManagerFileUtils() {
    }

    /**
     * <p>
     * Converte um arquivo JSON em um objeto JSON.
     * </p>
     *
     * @param filePath
     *            Caminho do arquivo no formato JSON.
     */
    private static JSONObject getJSONObjectFromReader(String filePath) {
        Objects.requireNonNull(filePath, "O caminho do arquivo não pode ser nulo.");
        try (Reader reader = Files.newBufferedReader(Path.of(filePath))) {
            return (JSONObject) GlobalUtils.getJSONParser().parse(reader);
        } catch (IOException | ParseException ex) {
            throw new IllegalStateException("Falha ao ler ou converter o arquivo JSON: " + filePath + ".", ex);
        }
    }

    /**
     * <h1><i>Exemplo de uso</i></h1> Lê e exibe os valores a partir de suas
     * respectivas chaves, dispostas no arquivo {@code example.json}.
     */
    public static void readJsonFileExample() {
        JSONObject jsonObject = getJSONObjectFromReader("src/test/resources/json-repo/example.json");
        if (jsonObject == null) {
            return;
        }
        LOGGER.info("Objeto JSON carregado: {}", jsonObject);
        long id = (Long) jsonObject.get("Id");
        LOGGER.info("Id: {}", id);
        String name = (String) jsonObject.get("Name");
        LOGGER.info("Nome: {}", name);
        String author = (String) jsonObject.get("Author");
        LOGGER.info("Autor: {}", author);
        JSONArray msg = (JSONArray) jsonObject.get("Company List");
        for (Object company : msg) {
            LOGGER.info("Empresa: {}", company);
        }
    }

    /**
     * <h1><i>Exemplo de uso</i></h1> Lê e exibe as chaves e seus valores, contidos
     * no arquivo {@code example.json}.
     */
    @SuppressWarnings("unchecked")
    public static void printJsonObject() {
        JSONObject jsonObject = getJSONObjectFromReader("src/test/resources/json-repo/example.json");
        //
        if (jsonObject == null) {
            return;
        }
        jsonObject.keySet().forEach(key -> {
            Object value = jsonObject.get(key);
            LOGGER.info("Chave: {} - Valor: {}", key, value);
        });
    }

    /**
     * Gera um {@code Map<String, Object>} a partir da leitura de um
     * {@code JSONObject}.
     *
     * @param strPath
     *            Caminho do arquivo JSON com as chaves e os valores que se deseja
     *            ler e converter.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParamsFromJson(String strPath) {
        return getJSONObjectFromReader(strPath);
    }

    /**
     * Retorna os dados de credencial a partir da chave disposta no arquivo JSON
     * {@code custom_credentials_(env).json}.
     *
     * @param keyValue
     *            Valor da chave do arquivo JSON.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getCustomCredentiaFromJSON(String keyValue) {
        Map<String, Object> map = new LinkedHashMap<>();
        // JSONObject jsonObject =
        // getJSONObjectFromReader("src/test/resources/json-params-config/custom_credentials_dev.json");
        // O valor do ambiente é indicado via argumentos de VM.
        JSONObject jsonObject = getJSONObjectFromReader(
            String.format("src/test/resources/proof-mass/json-credentials-params/custom_credentials_%s.json",
                JavaCMDLineClass.getEnv()));
        if (jsonObject == null) {
            return map;
        }
        //
        jsonObject.keySet().forEach(key -> {
            if (key.toString().equalsIgnoreCase(keyValue))
                map.put(key.toString(), jsonObject.get(key));
        });
        //
        return map;
    }

    /**
     * Gera um {@code Map<String, Object>} devolvendo as chaves e os respectivos
     * valores dispostos no arquivo JSON de credenciais.
     *
     * @param keyValue
     *            Valor da chave do arquivo JSON.
     */
    public static Map<String, Object> generateMapFromCredetialsJSON(String keyValue) {
        Map<String, Object> credentialsMap = getCustomCredentiaFromJSON(keyValue);
        if (credentialsMap.isEmpty()) {
            return Map.of();
        }
        Object rawValue = credentialsMap.values().iterator().next();
        return parseDelimitedPairs(String.valueOf(rawValue));
    }

    private static Map<String, Object> parseDelimitedPairs(String rawValue) {
        if (StringUtils.isBlank(rawValue)) {
            return Map.of();
        }
        String normalizedValue = GlobalUtils.removeBracketsAndDoubleQuotes(rawValue);
        return Arrays.stream(normalizedValue.split(",")).map(String::trim).filter(StringUtils::isNotBlank)
            .map(pair -> pair.split(":", 2)).filter(parts -> parts.length == 2)
            .collect(Collectors.toMap(parts -> StringUtils.defaultIfBlank(parts[0].trim(), " "),
                parts -> StringUtils.defaultIfBlank(parts[1].trim(), " "), (_, second) -> second, LinkedHashMap::new));
    }

    /**
     * Retorna um ou mais arquivos JSON especificados, filtrando-os pelo trecho
     * inicial dos seus nomes.
     *
     * @param directory
     *            Diretório onde os arquivos se encontram.
     * @param charSequence
     *            Trecho ou palavra que pode ser encontrado no nome do arquivo.
     * @param env
     *            Valor referente ao ambiente em que a automação de testes está
     *            sendo executada.
     * @param endsWith
     *            Trecho final do nome de um ou mais arquivos.
     */
    public static File[] getSpecificFilesFromDir(String directory, String charSequence, String env, String endsWith) {
        File[] files = new File(directory)
            .listFiles((_, name) -> name.contains(charSequence) && name.endsWith(env + endsWith));
        return files != null ? files : new File[0];
    }

    /**
     * <p>
     * Retorna um objeto JSON a partir de um diretório de arquivos JSON.
     * </p>
     * Realiza o filtro utilizando {@code startsWith}.
     *
     * @param directory
     *            Diretório com os arquivos JSON.
     * @param charSequence
     *            Trecho ou palavra que pode ser encontrado no nome do arquivo.
     */
    public static JSONObject getJSONObjectFromReaderUsingFilter(String directory, String charSequence) {
        File[] matchedFiles = getSpecificFilesFromDir(directory, charSequence, JavaCMDLineClass.getEnv(), ".json");
        if (matchedFiles.length == 0) {
            return null;
        }
        try (Reader reader = Files.newBufferedReader(matchedFiles[0].toPath())) {
            return (JSONObject) GlobalUtils.getJSONParser().parse(reader);
        } catch (IOException | ParseException ex) {
            throw new IllegalStateException("Falha ao ler o arquivo JSON filtrado no diretório: " + directory + ".",
                ex);
        }
    }

    /**
     * Gera um {@code Map<String, Object>} devolvendo as chaves e os respectivos
     * valores dispostos em um arquivo JSON de um diretório específico. Realiza o
     * filtro utilizando {@code startsWith}.
     *
     * @param directory
     *            Diretório com os arquivos JSON.
     * @param charSequence
     *            Trecho ou palavra que pode ser encontrado no nome do arquivo.
     * @param keyValue
     *            Valor da chave do arquivo JSON.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJSONFileUsingFilterAndKey(String directory, String charSequence,
        String keyValue) {
        JSONObject jsonObject = getJSONObjectFromReaderUsingFilter(directory, charSequence);
        if (jsonObject == null) {
            return Map.of();
        }
        Map<String, Object> map = new LinkedHashMap<>();
        jsonObject.keySet().forEach(key -> {
            if (key.toString().equalsIgnoreCase(keyValue)) {
                map.put(key.toString(), jsonObject.get(key));
            }
        });
        if (map.isEmpty()) {
            return Map.of();
        }
        Object rawValue = map.values().iterator().next();
        return parseDelimitedPairs(String.valueOf(rawValue));
    }

    /**
     * Retorna o valor de uma chave de URL contida no arquivo JSON urls.json. A
     * busca é realizada sem distinção de maiúsculas e minúsculas.
     */
    public static String getUrlFromJson(String url) {
        JSONObject jsonObject = getJSONObjectFromReader("src/test/resources/json-repo/urls.json");
        if (jsonObject == null) {
            return null;
        }
        for (Object key : jsonObject.keySet()) {
            if (key.toString().equalsIgnoreCase(url)) {
                return Objects.toString(jsonObject.get(key), null);
            }
        }
        return null;
    }

    /**
     * Verifica se um determinado arquivo existe em um diretório.
     */
    public static void checkFileExists(File file) {
        while (!file.exists()) {
            Thread.onSpinWait();
        }
        LOGGER.info("O arquivo {} existe ou foi criado com sucesso.", file.getName());
    }

    /**
     * Verifica se o diretório informado existe. Caso não exista, ele será criado.
     */
    public static File checkAndGenerateFilePath(String filePath) {
        Path path = Path.of(filePath);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
                LOGGER.info("Diretório criado com sucesso: {}", path.toAbsolutePath());
            } catch (IOException | SecurityException ex) {
                throw new IllegalStateException("Falha ao criar o diretório: " + path.toAbsolutePath() + ".", ex);
            }
        }
        return path.toAbsolutePath().toFile();
    }

    /**
     * Exclui um determinado arquivo existente no diretório.
     */
    public static void deleteFileIfExists(File file) {
        try {
            boolean wasDeleted = Files.deleteIfExists(file.toPath());
            if (wasDeleted) {
                LOGGER.info("Arquivo {} removido com sucesso.", file.getName());
            } else {
                LOGGER.warn("Não foi possível remover o arquivo {}.", file.getName());
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao remover o arquivo: " + file.getAbsolutePath() + ".", ex);
        }
    }
}
