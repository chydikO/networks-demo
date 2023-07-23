package org.itstep.home_work.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 10_000;
    private static final List<PrintStream> clientOutputStreams = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (var serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket remoteClientSocket = serverSocket.accept();
                System.out.println("Client has connected: " + remoteClientSocket.getRemoteSocketAddress());

                PrintStream clientOutputStream = new PrintStream(remoteClientSocket.getOutputStream(), true);
                clientOutputStreams.add(clientOutputStream);

                executorService.submit(() -> {
                    try {
                        handleClient(remoteClientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (var in = clientSocket.getInputStream();
             var requestReader = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String messageFromClient = requestReader.readLine();
                if (messageFromClient == null) {
                    break; // Клиент отключился
                }
                System.out.println("Message from " + clientSocket.getRemoteSocketAddress() + ": " + messageFromClient);

                sendToAllClients(messageFromClient + " $$$ Time SERVER:" + new Date().getTime() + " $$$");
            }

            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
        }
    }

    private static void sendToAllClients(String message) {
        for (PrintStream clientOutputStream : clientOutputStreams) {
            clientOutputStream.println(message);
        }
    }
}
