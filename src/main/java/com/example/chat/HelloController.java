package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController implements Initializable {

    @FXML
    private TextArea messagesArea;

    @FXML
    private TextField messageField;

    @FXML
    private TextField serverIPField;

    // Variables para la comunicación entre el servidor y el cliente
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    // Bandera para controlar el estado del servidor
    private volatile boolean serverRunning = false;

    // Mensaje de cierre del servidor
    private static final String SERVER_SHUTDOWN_MESSAGE = "SERVER_SHUTDOWN";

    /**
     * Método para inicializar las variables al inicio de la aplicación
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverSocket = null;
        clientSocket = null;
        in = null;
        out = null;
    }

    /**
     * Método para manejar el envío de mensajes
     */
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

    /**
     * Método para manejar la conexión al servidor
     */
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

    /**
     * Método para manejar el inicio del servidor
     */
    @FXML
    private void handleStartServerButtonAction() {
        int serverPort = 12345; // Asegúrate de utilizar el mismo puerto que el cliente

        startServer(serverPort);
    }

    /**
     * Método para manejar la detención del servidor
     */
    @FXML
    private void handleStopServerButtonAction() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                if (out != null) {
                    out.println(SERVER_SHUTDOWN_MESSAGE);
                }
                serverRunning = false;
                serverSocket.close();
                messagesArea.appendText("Servidor detenido.\n");
            } catch (IOException e) {
                messagesArea.appendText("Error: No se pudo detener el servidor.\n");
            }
        }
    }

    /**
     * Método para iniciar el servidor
     * @param serverPort
     */
    public void startServer(int serverPort) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(serverPort);
                messagesArea.appendText("Esperando conexión en el puerto " + serverPort + "...\n");

                clientSocket = serverSocket.accept();
                messagesArea.appendText("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress() + "\n");

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                serverRunning = true;
                while (serverRunning && (inputLine = in.readLine()) != null) {
                    if (SERVER_SHUTDOWN_MESSAGE.equals(inputLine)) {
                        serverRunning = false;
                        messagesArea.appendText("El servidor ha sido detenido.\n");
                    } else {
                        messagesArea.appendText("Cliente: " + inputLine + "\n");
                    }
                }
            } catch (BindException e) {
                Platform.runLater(() -> messagesArea.appendText("Error: No se pudo iniciar el servidor en el puerto " + serverPort + ". El puerto ya está en uso.\n"));
            } catch (IOException e) {
                if (!serverRunning) {
                    Platform.runLater(() -> messagesArea.appendText("El servidor fue detenido correctamente.\n"));
                } else {
                    Platform.runLater(() -> messagesArea.appendText("Error: Hubo un problema al iniciar el servidor o durante la comunicación.\n"));
                }
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (clientSocket != null) clientSocket.close();
                    if (serverSocket != null) serverSocket.close();
                } catch (IOException e) {
                    Platform.runLater(() -> messagesArea.appendText("Error: Hubo un problema al cerrar los recursos del servidor.\n"));
                }
            }
        }).start();
    }

    /**
     * Método para conectar al servidor
     * @param host
     * @param port
     */
    private void connectToServer(String host, int port) {
        new Thread(() -> {
            try {
                clientSocket = new Socket(host, port);
                messagesArea.appendText("Conectado al servidor en " + host + ":" + port + "\n");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                serverRunning = true;
                while (serverRunning && (inputLine = in.readLine()) != null) {
                    if (SERVER_SHUTDOWN_MESSAGE.equals(inputLine)) {
                        serverRunning = false;
                        messagesArea.appendText("El servidor ha sido detenido.\n");
                    } else {
                        messagesArea.appendText("Servidor: " + inputLine + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}