package com.poo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainController {

    private final Stage primaryStage;
    private final VBox root;
    private final GitHubService service = new GitHubService();

    private String selectedRepo = "";
    private String selectedFolder = "";

    private final Button repoButton;
    private final Button folderButton;
    private final Button downloadButton;
    private final Label statusLabel;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new VBox(14);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24, 20, 24, 20));
        root.getStyleClass().add("root-pane");


        ImageView logoView = loadLogo();
        root.getChildren().add(logoView != null ? logoView : makeFallbackIcon());


        Label nameLabel = new Label("Marcos Pisching");
        nameLabel.getStyleClass().add("name-label");
        root.getChildren().add(nameLabel);


        repoButton = makeButton("🔎  Selecionar Repositório");
        repoButton.setOnAction(e -> openRepoSelector());
        root.getChildren().add(repoButton);


        folderButton = makeButton("🔽  Selecione um Projeto");
        folderButton.setOnAction(e -> openFolderSelector());
        root.getChildren().add(folderButton);


        downloadButton = makeButton("⬇️  Baixar Projeto");
        downloadButton.getStyleClass().add("download-button");
        downloadButton.setOnAction(e -> downloadProject());
        root.getChildren().add(downloadButton);


        statusLabel = new Label("");
        statusLabel.getStyleClass().add("status-label");
        root.getChildren().add(statusLabel);
    }

    public VBox getRoot() {
        return root;
    }


    private void openRepoSelector() {
        setStatus("Buscando repositórios...", false);
        setLoading(true);

        CompletableFuture.supplyAsync(() -> {
            try { return service.getUserRepos(); }
            catch (Exception e) { throw new RuntimeException(e); }
        }).whenComplete((repos, err) -> Platform.runLater(() -> {
            setLoading(false);
            setStatus("", false);
            if (err != null) {
                showError("Erro ao buscar repositórios:\n" + err.getCause().getMessage());
            } else {
                showListPopup("Selecione um Repositório", repos, value -> {
                    selectedRepo = value;
                    repoButton.setText(value);
                    selectedFolder = "";
                    folderButton.setText("🔽  Selecione um Projeto");
                });
            }
        }));
    }


    private void openFolderSelector() {
        if (selectedRepo.isBlank()) {
            showWarning("Selecione um repositório primeiro.");
            return;
        }
        setStatus("Buscando pastas...", false);
        setLoading(true);

        CompletableFuture.supplyAsync(() -> {
            try { return service.getRepoFolders(selectedRepo); }
            catch (Exception e) { throw new RuntimeException(e); }
        }).whenComplete((folders, err) -> Platform.runLater(() -> {
            setLoading(false);
            setStatus("", false);
            if (err != null) {
                showError("Erro ao acessar repositório:\n" + err.getCause().getMessage());
            } else {
                showListPopup("Selecione um Projeto", folders, value -> {
                    selectedFolder = value;
                    folderButton.setText(value);
                });
            }
        }));
    }


    private void downloadProject() {
        if (selectedRepo.isBlank()) {
            showWarning("Selecione um repositório primeiro.");
            return;
        }
        if (selectedFolder.isBlank()) {
            showWarning("Selecione um projeto válido.");
            return;
        }

        setStatus("Baixando projeto...", false);
        setLoading(true);

        final String repo = selectedRepo;
        final String folder = selectedFolder;

        CompletableFuture.supplyAsync(() -> {
            try { return service.downloadFolder(repo, folder); }
            catch (Exception e) { throw new RuntimeException(e); }
        }).whenComplete((path, err) -> Platform.runLater(() -> {
            setLoading(false);
            if (err != null) {
                setStatus("Erro no download.", true);
                showError("Download falhou:\n" + err.getCause().getMessage());
            } else {
                setStatus("Download concluído!", false);
                showInfo("Projeto '" + folder + "' salvo em:\n" + path);
            }
        }));
    }


    private void showListPopup(String title, List<String> items, java.util.function.Consumer<String> onSelect) {
        Stage popup = new Stage();
        popup.initModality(Modality.WINDOW_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(title);
        popup.setResizable(false);

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setPrefSize(380, 460);
        scroll.getStyleClass().add("scroll-pane");

        VBox list = new VBox(8);
        list.setPadding(new Insets(10));
        list.getStyleClass().add("popup-list");

        for (String item : items) {
            Button btn = makeButton(item);
            btn.setPrefWidth(340);
            btn.setOnAction(e -> {
                onSelect.accept(item);
                popup.close();
            });
            list.getChildren().add(btn);
        }

        scroll.setContent(list);

        Scene scene = new Scene(scroll, 400, 480);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        popup.setScene(scene);

        centerOnOwner(popup, 400, 480);
        popup.showAndWait();
    }


    private Button makeButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("cyan-button");
        btn.setPrefWidth(270);
        btn.setPrefHeight(42);
        return btn;
    }

    private ImageView loadLogo() {
        try {
            Image img = new Image(getClass().getResourceAsStream("/logo.png"), 100, 100, true, true);
            return new ImageView(img);
        } catch (Exception e) { return null; }
    }

    private Label makeFallbackIcon() {
        Label lbl = new Label("🔷");
        lbl.setFont(Font.font("System", FontWeight.BOLD, 56));
        return lbl;
    }

    private void setLoading(boolean loading) {
        repoButton.setDisable(loading);
        folderButton.setDisable(loading);
        downloadButton.setDisable(loading);
    }

    private void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setTextFill(isError ? Color.web("#ff4444") : Color.web("#00ffff"));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.initOwner(primaryStage);
        applyAlertStyle(alert);
        alert.showAndWait();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.initOwner(primaryStage);
        applyAlertStyle(alert);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.initOwner(primaryStage);
        applyAlertStyle(alert);
        alert.showAndWait();
    }

    private void applyAlertStyle(Alert alert) {
        try {
            alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
            );
            alert.getDialogPane().getStyleClass().add("alert-pane");
        } catch (Exception ignored) {}
    }

    private void centerOnOwner(Stage stage, int w, int h) {
        stage.setWidth(w);
        stage.setHeight(h);
        double ox = primaryStage.getX() + primaryStage.getWidth() / 2 - w / 2.0;
        double oy = primaryStage.getY() + primaryStage.getHeight() / 2 - h / 2.0;
        stage.setX(ox);
        stage.setY(oy);
    }
}
