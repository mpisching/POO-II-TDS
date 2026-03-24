package com.poo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GitHubService {

    private static final String GITHUB_USER = "ctii-ead-ifsc-fln";
    private static final String DOWNLOAD_DIR = System.getProperty("user.home") + "/Downloads";
    private final Gson gson = new Gson();

    public List<String> getUserRepos() throws IOException {
        String urlStr = "https://api.github.com/users/" + GITHUB_USER + "/repos?per_page=100";
        String json = get(urlStr);
        JsonArray array = gson.fromJson(json, JsonArray.class);
        List<String> names = new ArrayList<>();
        for (var el : array) {
            names.add(el.getAsJsonObject().get("name").getAsString());
        }
        return names;
    }

    public List<String> getRepoFolders(String repo) throws IOException {
        String urlStr = "https://api.github.com/repos/" + GITHUB_USER + "/" + repo + "/contents/";
        String json = get(urlStr);
        JsonArray array = gson.fromJson(json, JsonArray.class);
        List<String> folders = new ArrayList<>();
        for (var el : array) {
            JsonObject obj = el.getAsJsonObject();
            if ("dir".equals(obj.get("type").getAsString())) {
                folders.add(obj.get("name").getAsString());
            }
        }
        return folders;
    }

    public String downloadFolder(String repo, String folderName) throws IOException {
        String urlStr = "https://api.github.com/repos/" + GITHUB_USER + "/" + repo + "/zipball/main";
        byte[] zipBytes = getBytes(urlStr);

        Path targetDir = Paths.get(DOWNLOAD_DIR, folderName);
        Files.createDirectories(targetDir);

        String baseFolder = null;
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (baseFolder == null && entry.isDirectory()) {
                    baseFolder = entry.getName();
                }
                zis.closeEntry();
            }
        }

        if (baseFolder == null) throw new IOException("Não foi possível determinar a pasta base do ZIP.");

        final String prefix = baseFolder + folderName + "/";
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.startsWith(prefix) && !entry.isDirectory()) {
                    String relative = name.substring(prefix.length());
                    Path savePath = targetDir.resolve(relative);
                    Files.createDirectories(savePath.getParent());
                    try (OutputStream os = Files.newOutputStream(savePath)) {
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
        }

        return targetDir.toString();
    }

    private String get(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        int code = conn.getResponseCode();
        if (code != 200) throw new IOException("HTTP " + code);
        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes());
        }
    }

    private byte[] getBytes(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(60000);
        conn.setInstanceFollowRedirects(true);

        // Follow redirects manually if needed
        int code = conn.getResponseCode();
        if (code == 302 || code == 301) {
            String location = conn.getHeaderField("Location");
            conn = (HttpURLConnection) new URL(location).openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);
        }

        try (InputStream is = conn.getInputStream()) {
            return is.readAllBytes();
        }
    }
}
