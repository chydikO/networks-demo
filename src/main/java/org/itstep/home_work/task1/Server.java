package org.itstep.home_work.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 10_000;

    public static void main(String[] args) throws IOException {
        boolean isShutdown = false;
        try (var serverSocket = new ServerSocket(PORT)) {
            while (!isShutdown) {
                System.out.println("Wait for connection...");
                Socket remoteClientSocket = serverSocket.accept();
                System.out.println("Client has connected!");

                try (var in = remoteClientSocket.getInputStream();
                     var requestReader = new BufferedReader(new InputStreamReader(in));
                     var out = remoteClientSocket.getOutputStream();
                     var responseStream = new PrintStream(out, true)) {

                    while (true) {
                        String messageFromClient = requestReader.readLine();
                        System.out.println("Message from client: " + messageFromClient);
                        if (messageFromClient.equalsIgnoreCase("exit")) {
                            responseStream.println("-= Server shutdown =-");
                            isShutdown = true;
                            break;
                        }
                        responseStream.println("Hello from server. Your message: " +
                                messageFromClient);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("-= Server shutdown =-");
        }
    }
}
