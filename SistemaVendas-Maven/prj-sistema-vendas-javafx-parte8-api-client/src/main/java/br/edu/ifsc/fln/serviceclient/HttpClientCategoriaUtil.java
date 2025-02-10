package br.edu.ifsc.fln.serviceclient;

import br.edu.ifsc.fln.model.domain.Categoria;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClientCategoriaUtil {


    private static final String API_URL = "http://localhost:8081/categorias"; // Ajuste conforme necessário
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Método GET para obter lista de categorias
    public static List<Categoria> getCategorias() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Erro HTTP: " + conn.getResponseCode());
        }

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        List<Categoria> categorias = objectMapper.readValue(reader, new TypeReference<List<Categoria>>() {
        });
        conn.disconnect();
        return categorias;
    }

    // Método GET para obter uma categoria por ID
    public static Categoria getCategoriaById(Integer id) throws IOException {
        URL url = new URL(API_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 404) {
            System.out.println("Categoria com ID " + id + " não encontrada.");
            return null;
        } else if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Erro HTTP: " + conn.getResponseCode());
        }

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        Categoria categoria = objectMapper.readValue(reader, Categoria.class);
        conn.disconnect();
        return categoria;
    }

    // Método POST para criar uma categoria
    public static Categoria createCategoria(Categoria categoria) throws IOException {
        return sendRequest(categoria, "POST", API_URL);
    }

    // Método PUT para atualizar uma categoria existente
    public static Categoria updateCategoria(Integer id, Categoria categoria) throws IOException {
        return sendRequest(categoria, "PUT", API_URL + "/" + id);
    }

    // Método DELETE para remover uma categoria por ID
    public static boolean deleteCategoria(Integer id) throws IOException {
        URL url = new URL(API_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        conn.disconnect();

        if (responseCode == 200 || responseCode == 204) {
            System.out.println("Categoria removida com sucesso!");
            return true;
        } else if (responseCode == 404) {
            System.out.println("Categoria com ID " + id + " não encontrada.");
            return false;
        } else {
            throw new RuntimeException("Erro ao remover categoria. Código HTTP: " + responseCode);
        }
    }

    // Método auxiliar para enviar requisições POST e PUT
    private static Categoria sendRequest(Categoria categoria, String method, String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = objectMapper.writeValueAsString(categoria);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() >= 400) {
            throw new RuntimeException("Erro HTTP: " + conn.getResponseCode());
        }

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        Categoria responseCategoria = objectMapper.readValue(reader, Categoria.class);
        conn.disconnect();
        return responseCategoria;
    }
}


