package ru.otus.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.data.core.repository.DataTemplateHibernate;
import ru.otus.data.core.repository.HibernateUtils;
import ru.otus.data.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.data.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;
import ru.otus.data.crm.service.DbServiceClientImpl;
import ru.otus.web.dao.InMemoryUserDao;
import ru.otus.web.dao.UserDao;
import ru.otus.web.server.ClientsWebServer;
import ru.otus.web.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.web.services.TemplateProcessor;
import ru.otus.web.services.TemplateProcessorImpl;
import ru.otus.web.services.UserAuthService;
import ru.otus.web.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = doMigration();

        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        ClientsWebServer clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }

    private static Configuration doMigration() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        return configuration;
    }
}
