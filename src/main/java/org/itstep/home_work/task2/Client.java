package org.itstep.home_work.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        try (var client = new Socket("localhost", 10_000);
             var in = client.getInputStream();
             var reader = new BufferedReader(new InputStreamReader(in));
             var out = client.getOutputStream();
             var printStream = new PrintStream(out, true)) {

            System.out.println("Connected to the server. You can start chatting.");

            // Создаем поток для приема сообщений от сервера
            new Thread(() -> {
                try {
                    while (true) {
                        String messageFromServer = reader.readLine();
                        if (messageFromServer == null) {
                            break; // Сервер отключился
                        }
                        System.out.println("Server: " + messageFromServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Отправляем сообщения на сервер
            while (true) {
                String message = scanner.nextLine();
                printStream.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
