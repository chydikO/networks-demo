package org.itstep.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

    public static final int PORT = 10_000;

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Wait for connection...");
                Socket remoteClientSocket = serverSocket.accept();
                System.out.println("Client has connected!");

                executorService.submit(() -> {
                    System.out.println("remoteClientSocket.getRemoteSocketAddress() = "
                            + remoteClientSocket.getRemoteSocketAddress());

                    try (var in = remoteClientSocket.getInputStream();
                         var requestReader = new BufferedReader(new InputStreamReader(in));
                         var out = remoteClientSocket.getOutputStream();
                         var responseStream = new PrintStream(out, true)) {
						while (true) {
                            String messageFromClient = requestReader.readLine();
                            System.out.println("message: " + messageFromClient);
                            if (messageFromClient.toLowerCase().equals("exit")) {
                                responseStream.println("-= Server shutdown =-");
                                executorService.shutdown();
                                break;
                            }
                            responseStream.println("Hello from server. Your message: " +
                                    messageFromClient);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
