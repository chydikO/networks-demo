package org.itstep.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        try (var client = new Socket("localhost", 10_000);
             var in = client.getInputStream();
             var reader = new BufferedReader(new InputStreamReader(in));
             var out = client.getOutputStream();
             var printStream = new PrintStream(out, true)) {

            System.out.println("Enter any message: ");
            String request = scanner.nextLine();
            printStream.println(request);
            String response = reader.readLine();
            System.out.println("Server response = " + response);
        }

    }
}
