package org.itstep.home_work.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        try (var client = new Socket("localhost", 10_000);
             var in = client.getInputStream();
             var reader = new BufferedReader(new InputStreamReader(in));
             var out = client.getOutputStream();
             var printStream = new PrintStream(out, true)) {

            while (true) {
                System.out.println("Enter any message (or type 'exit' to exit): ");
                String request = scanner.nextLine();
                printStream.println(request  + " $$$ Time client:" + new Date().getTime() + " $$$");
                String response = reader.readLine();
                System.out.println("Server response: " + response);

                if (request.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
        }
    }
}
