package br.com.automation.project.ui.pages;

import br.com.automation.project.utils.WebDriverUtils;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.lang.reflect.InvocationTargetException;

public class PageRegister extends LoadableComponent<PageRegister> {

    @SuppressWarnings("unchecked")
    private static <T> T getPage(Class<?> clazz) {
        T page = null;
        try {
            page = (T) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
        PageFactory.initElements(WebDriverUtils.getDriverManager().getDriver(), page);
        return page;
    }

    public static FirstWebTestPG getFirstWebTestPG() {
        return getPage(FirstWebTestPG.class);
    }

    public static TodoListPG getTodoListPG() {
        return getPage(TodoListPG.class);
    }

    public static AgiBlogSearchPG getAgiBlogSearchPG() {
        return getPage(AgiBlogSearchPG.class);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
