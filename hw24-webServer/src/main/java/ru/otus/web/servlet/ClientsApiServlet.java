package ru.otus.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.data.crm.dto.ClientRecord;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.service.DBServiceClient;

import java.io.IOException;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient clientDao;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = clientDao.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = gson.fromJson(request.getReader(), ClientRecord.class).build();
        Client savedClient = clientDao.saveClient(client);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(savedClient));
    }

}
