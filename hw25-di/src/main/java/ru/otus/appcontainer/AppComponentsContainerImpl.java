package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var sortedMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        Object appConfigObj;
        try {
            appConfigObj = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("AppConfig object could not be instantiated", e);
        }

        sortedMethods.forEach(method -> {
            var appComponentName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(appComponentName)) {
                throw new RuntimeException("AppComponent with name = " + appComponentName + " already exists");
            }

            Parameter[] parameters = method.getParameters();
            Object appComponentObj;
            try {
                if (parameters.length == 0) {
                    appComponentObj = method.invoke(appConfigObj);
                } else {
                    var paramObjs = Arrays.stream(parameters).map(parameter -> getAppComponent(parameter.getType())).toArray();
                    appComponentObj = method.invoke(appConfigObj, paramObjs);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("App component object could not be instantiated", e);
            }

            appComponents.add(appComponentObj);
            appComponentsByName.put(appComponentName, appComponentObj);
        });


    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var appComponentsList = appComponents.stream().filter(componentClass::isInstance).toList();
        if (appComponentsList.isEmpty()) {
            throw new IllegalArgumentException("There is no AppComponent for specified class " + componentClass.getName() + " does not implement AppComponent");
        }
        if (appComponentsList.size() > 1) {
            throw new IllegalArgumentException("There are more than one AppComponent for specified class " + componentClass.getName());
        }
        return (C) appComponentsList.getFirst();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var appComponent = appComponentsByName.get(componentName);
        if (appComponent == null) {
            throw new IllegalArgumentException("There is no AppComponent for specified name " + componentName);
        }

        return (C) appComponent;
    }
}
