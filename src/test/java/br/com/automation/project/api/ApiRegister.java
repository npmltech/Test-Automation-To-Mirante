package br.com.automation.project.api;

import java.lang.reflect.InvocationTargetException;

public class ApiRegister {

    @SuppressWarnings("unchecked")
    private static <T> T getApi(Class<?> clazz) {
        T api = null;
        try {
            api = (T) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
        return api;
    }

    public static SWPeopleApi getSWPeopleApi() {
        return getApi(SWPeopleApi.class);
    }

    public static AccessTokenHerokuApi getAccessTokenHerokuApi() {
        return getApi(AccessTokenHerokuApi.class);
    }

    public static PersonApi getPersonApi() {
        return getApi(PersonApi.class);
    }
}
