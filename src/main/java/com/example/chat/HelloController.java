package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextArea messagesArea;

    @FXML
    private TextField messageField;

    @FXML
    private TextField serverIPField;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void initialize() {
        // Inicializar serverSocket, clientSocket, in y out aquí, si es necesario.
        // También puedes iniciar el servidor o conectarte a un servidor existente aquí.
    }

    @FXML
    private void handleSendButtonAction() {
        String message = messageField.getText();
        if (out != null) {
            out.println(message);
            messagesArea.appendText("Yo: " + message + "\n");
            messageField.clear();
        } else {
            // Muestra un mensaje de error si 'out' no está inicializado
            messagesArea.appendText("Error: No estás conectado a un servidor.\n");
        }
    }

    @FXML
    private void handleConnectButtonAction() {
        String serverIP = serverIPField.getText();
        int serverPort = 12345; // Asegúrate de utilizar el mismo puerto que el servidor

        if (!serverIP.isEmpty()) {
            connectToServer(serverIP, serverPort);
        } else {
            System.out.println("No se ha introducido ninguna dirección IP");
        }
    }

    @FXML
    private void handleStartServerButtonAction() {
        int serverPort = 12345; // Asegúrate de utilizar el mismo puerto que el cliente

        startServer(serverPort);
    }

    private void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String finalInputLine = inputLine;
                    Platform.runLater(() -> messagesArea.appendText("Cliente: " + finalInputLine + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToServer(String host, int port) {
        new Thread(() -> {
            try {
                clientSocket = new Socket(host, port);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String finalInputLine = inputLine;
                    Platform.runLater(() -> messagesArea.appendText("Servidor: " + finalInputLine + "\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}