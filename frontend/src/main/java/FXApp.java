import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FXApp extends Application {
    @Override
    public void start(Stage stage) {
        TextArea codeArea = new TextArea();
        codeArea.setPromptText("Enter your code here...");
        ComboBox<String> languageBox = new ComboBox<>();
        languageBox.getItems().addAll("python", "cpp", "java");
        languageBox.setValue("python");

        Button runButton = new Button("Run");

        Label outputLabel = new Label("Output:");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            String code = codeArea.getText();
            String language = languageBox.getValue();
            try {
                URL url = URI.create("http://127.0.0.1:5000/run").toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                String payload = String.format(
                        "{\"language\":\"%s\",\"code\":\"%s\"}",
                        language,
                        code.replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\t", "\\t")
                        );

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes(StandardCharsets.UTF_8));
                }

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                if (jsonResponse.has("output")) {
                    response = jsonResponse.get("output").getAsString();
                }

                outputArea.setText(response);
            } catch (Exception ex) {
                outputLabel.setText("Error:");
                outputArea.setText(ex.getMessage());
            }
        });

        VBox root = new VBox(10, languageBox, codeArea, runButton, outputLabel, outputArea);
        root.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
        root.setPrefSize(600, 400);
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.setTitle("Code Judge");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
