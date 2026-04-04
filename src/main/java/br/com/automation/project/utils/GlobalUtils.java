package br.com.automation.project.utils;

import br.com.automation.project.api.model.ResponseClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe utilitária que reúne métodos de adequação, transformação e validação
 * de dados usados ao longo do projeto.
 *
 * @author Niky Lima
 */
public class GlobalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalUtils.class);

    static JSONParser getJSONParser() {
        return new JSONParser();
    }

    /**
     * <p>
     * Converte uma String em um {@code HashMap<String, Object>}.
     * </p>
     *
     * @param response
     *            String no formato de resposta de um serviço (JSON).
     */
    @SuppressWarnings("unchecked")
    private static HashMap<String, Object> getHashMapFromResponse(String response) {
        try {
            return new ObjectMapper().readValue(response, HashMap.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao converter resposta JSON em HashMap.", ex);
        }
    }

    /**
     * <p>
     * Converte a resposta textual de um serviço em uma lista de {@link String}.
     * </p>
     * Aplica o tratamento necessário para transformar a resposta em formato textual
     * em uma lista de strings.
     */
    public static List<String> getResponse() {
        String[] strArr = new Gson().toJson(getHashMapFromResponse(ResponseClass.getResponse()).entrySet()
            .parallelStream().map(Map.Entry::getValue).collect(Collectors.toList())).split(",");
        List<String> linkedList = new LinkedList<>();
        for (String strValue : strArr) {
            linkedList.add(removeBracketsAndDoubleQuotes(strValue));
        }
        return linkedList;
    }

    /**
     * Valida o código de status HTTP da resposta de um serviço.
     */
    public static void assertHttpStatusCode(int httpStatusCode) {
        Assertions.assertEquals(ResponseClass.getHttpStatusCode() /* Valor atual */,
            httpStatusCode /* Valor esperado */,
            String.format("O código de status HTTP atual (%s) é diferente do esperado (%s).",
                ResponseClass.getHttpStatusCode(), httpStatusCode));
        //
    }

    /**
     * Gera uma String a partir de um objeto {@code SortedMap<String, String>},
     * refletindo o formato de um arquivo JSON.
     */
    public static String generateStringJSONFormat(SortedMap<String, String> sortedMap) {
        Gson gson = new Gson();
        Type gsonType = new TypeToken<>() {
        }.getType();
        return gson.toJson(sortedMap, gsonType);
    }

    /**
     * <p>
     * Retorna um valor específico da resposta de um serviço pelo índice informado,
     * no formato de lista de strings.
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static List<String> getValueByIndexFromResponse(String key) {
        return (List<String>) new JSONObject(getHashMapFromResponse(ResponseClass.getResponse())).get(key);
        //
    }

    /**
     * Verifica se um valor específico existe na resposta do serviço.
     * <p>
     * A resposta do serviço é convertida em um {@code HashMap} para permitir a
     * comparação.
     * </p>
     */
    public static void compareValueResponseString(String value) {
        Object ObjValue;
        int itr = 0;
        HashMap<String, Object> result = getHashMapFromResponse(ResponseClass.getResponse());
        Set<String> entrySet = result.keySet();
        for (String key : entrySet) {
            ObjValue = result.get(key);
            String jsonString = new Gson().toJson(ObjValue);
            String[] values = jsonString.split(",");
            for (String item : values) {
                if (Objects.equals(removeBracketsAndDoubleQuotes(item), value)) {
                    LOGGER.debug("Resultado encontrado | {} |", item);
                    itr += 1;
                    /*
                     * Assertion.assertTrue(removeBracketsAndDoubleQuotes(values[i]).equals(value),
                     * String.format("O valor %s não é igual a %s!", values[i], value)); break;
                     */
                }
            }
        }
        //
        if (itr == 0)
            Assertions.assertEquals(1 /* Valor esperado */, itr /* Valor atual */,
                String.format("O valor %s não existe na resposta JSON.", value));
        //
    }

    /**
     * <p>
     * Gera um {@code Map<String, Object>} a partir de uma String.
     * </p>
     * Obs.: esta função foi testada com a resposta de API em formato textual
     * ({@code asPrettyString} do RestAssured).
     */
    public static Map<String, Object> generateMapFromString(String strValue) {
        String[] strArrayToMap = removeBracketsAndDoubleQuotes(strValue).split(",");
        return Arrays.asList(strArrayToMap).parallelStream().map(str -> str.split(":", 2))
            .filter(parts -> parts.length == 2)
            .collect(Collectors.toMap(parts -> StringUtils.defaultIfBlank(parts[0].trim(), " "),
                parts -> StringUtils.defaultIfBlank(parts[1].trim(), " ")));
    }

    /**
     * Executa uma busca na resposta da API utilizando uma combinação
     * ({@code matcher}).
     *
     * @param groupBy
     *            Caso o valor do parâmetro seja {@code null}, retorna toda a
     *            sequência da combinação.
     * @param matcher
     *            A combinação que se deseja encontrar na resposta da API.
     */
    public static List<String> getMatchInResponse(Integer groupBy, String matcher) {
        List<String> mList = new LinkedList<>();
        //
        Matcher _matcher = Pattern.compile(matcher).matcher(ResponseClass.getResponse());
        //
        while (_matcher.find()) {
            mList.add(groupBy != null ? _matcher.group(groupBy) : _matcher.group());
        }
        //
        return mList;
    }

    /**
     * Realiza a pesquisa e valida uma palavra dentro da resposta de um serviço.
     */
    public static void getWordInResponse(String word) {
        int count = 0;
        {
            Matcher matcher = Pattern.compile(word).matcher(ResponseClass.getResponse());
            //
            while (matcher.find())
                count++;
        }
        //
        Assertions.assertTrue(count > 0,
            String.format("A palavra pesquisada (%s) não foi encontrada na resposta do serviço.", word));
    }

    /**
     * Transforma um {@link DataTable} em uma lista de strings e retorna a primeira
     * posição.
     */
    public static String getFirstIndexDataTable(DataTable dataTable) {
        return dataTable.asList(String.class).getFirst();
    }

    /**
     * <p>
     * Transforma um {@link DataTable} do Cucumber em uma lista de strings.
     * </p>
     */
    public static List<String> getDataTable(DataTable dataTable) {
        int itr = 0;
        List<String> strList = dataTable.asList(String.class);
        for (String strData : strList) {
            itr += 1;
            LOGGER.debug("Resultado ({}): {}.", itr, strData);
        }
        //
        return strList.subList(1, strList.size());
    }

    /**
     * Primeira forma de validação.
     * <p>
     * Valida os valores de duas listas, colocando-os em ordem alfabética e
     * realizando a comparação.
     * </p>
     */
    public static void assertResponseList(List<String> listA, List<String> listB) {
        int itr = 0, lstASize = listA.size();
        for (String strA : listA) {
            for (String strB : listB) {
                if (strA.equals(strB)) {
                    itr += 1;
                    LOGGER.debug("--- VALIDADO --- Resultado da massa ({}): {} | Resultado JSON ({}): {}.", itr, strA,
                        itr, strB);
                }
            }
        }
        Assertions.assertEquals(itr /* Valor atual */, lstASize /* Valor esperado */, String.format(
            "A massa do teste apresenta diferença em relação à resposta JSON. Massa: %s | JSON: %s", itr, lstASize));
    }

    /**
     * Segunda forma de validação.
     * <p>
     * Valida os valores de duas listas, colocando-os em ordem alfabética e
     * realizando a comparação.
     * </p>
     */
    public static void assertResponseListReduced(List<String> listA, List<String> listB) {
        listA = putInAlphabeticalOrder(listA);
        listB = putInAlphabeticalOrder(listB);
        for (int i = 0; i < listA.size(); i++) {
            Assertions.assertEquals(listA.get(i) /* Valor atual */, listB.get(i) /* Valor esperado */,
                String.format("O valor esperado (%s) não é igual ao valor atual (%s).", listA.get(i), listB.get(i)));
        }
    }

    /**
     * Terceira forma de validação.
     * <p>
     * Valida os valores de duas listas, colocando-os em ordem alfabética e
     * realizando a comparação.
     * </p>
     */
    public void assertIterableEqualsResponseListReduced(List<String> listA, List<String> listB) {
        listA = putInAlphabeticalOrder(listA);
        listB = putInAlphabeticalOrder(listB);
        Assertions.assertIterableEquals(listA, listB, "O valor atual não é igual ao valor esperado.");
    }

    /**
     * <p>
     * Remove colchetes e aspas duplas de uma String.
     * </p>
     */
    public static String removeBracketsAndDoubleQuotes(String strValue) {
        if (StringUtils.isBlank(strValue)) {
            return strValue;
        }
        return strValue.replaceAll("[\\p{Ps}\\p{Pe}]", "").replace("\"", "");
    }

    /**
     * <p>
     * Remove espaços em branco e tabulações.
     * </p>
     * Indicado para remover inconsistências desse tipo encontradas nos arquivos de
     * propriedades.
     */
    public static String removeWhiteSpacesAndTabulation(String strValue) {
        if (strValue == null || strValue.isEmpty()) {
            return strValue;
        }
        return strValue.replaceFirst("\\s++$", "");
    }

    /**
     * Verifica se um valor esperado ({@link String}) consta no conteúdo de um
     * texto.
     *
     * @param text
     *            O texto onde se deseja aplicar a pesquisa.
     * @param expectedValue
     *            Valor ({@link String}) que se deseja encontrar na pesquisa.
     */
    public static void assertExpectedValueInText(String text, String expectedValue) {
        assertTrue(text != null && expectedValue != null && text.contains(expectedValue),
            String.format("O valor esperado (%s) não existe no conteúdo do texto: (%s).", expectedValue, text));
    }

    /**
     * Verifica se um ou mais valores ({@link String}) existem no conteúdo de um
     * texto.
     *
     * @param text
     *            O texto onde se deseja aplicar a pesquisa.
     * @param expectedValues
     *            Valores ({@link String}[]) que se deseja encontrar na pesquisa.
     */
    public static void assertExpectedValuesInText(String text, String... expectedValues) {
        assertTrue(Arrays.asList(expectedValues).parallelStream().allMatch(text::contains),
            String.format("O valor esperado (%s) não existe no conteúdo do texto: (%s).", expectedValues[0], text));
    }

    /**
     * Executa comandos no prompt do Windows.
     */
    public static void cmdCommand(String... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Informe ao menos um argumento para executar o comando.");
        }
        //
        try {
            LOGGER.info("Executando comando: {} {} {}", args[0], args[1], args[2]);
            Process proc = new ProcessBuilder(args[0], args[1], args[2]).start();
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERRO");
            //
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "SAÍDA");
            //
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
            LOGGER.info("Código de saída do comando: {}", exitVal);
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new IllegalStateException("Falha ao executar comando via linha de comando.", ex);
        }
    }

    /**
     * Organiza uma lista em ordem alfabética.
     */
    public static List<String> putInAlphabeticalOrder(List<String> lstString) {
        String temp;
        String[] stockArr = new String[lstString.size()];
        stockArr = lstString.toArray(stockArr);
        for (int i = 0; i < stockArr.length; i++) {
            for (int j = i + 1; j < stockArr.length; j++) {
                if (stockArr[i].compareTo(stockArr[j]) > 0) {
                    temp = stockArr[i];
                    stockArr[i] = stockArr[j];
                    stockArr[j] = temp;
                }
            }
        }
        return Arrays.asList(stockArr);
    }

    /**
     * Retorna a data atual formatada na máscara {@code dd.MM.yyyy_HH.mm.ss}.
     */
    public static String generateReferenceDate() {
        return new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss").format(Calendar.getInstance().getTime());
        //
    }
}
