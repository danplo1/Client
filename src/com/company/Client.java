package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    public static final int PORT = 5000; //zdefiniowanie portu na który idą komunikaty
    public static final String IP = "127.0.0.1";

    BufferedReader bufferedReader;
    String name;

    //start programu
    public static void main(String[] args) {
        Client client = new Client();
        client.clientStart();


    }

    //uruchomienie klienta

    public void clientStart() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj swoje imię: ");
        name = sc.nextLine();

        try {
            Socket socket = new Socket(IP, PORT);
            System.out.println("Podłączono do: " + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream()); //  prinWr wysyła komunikaty na zewnątrz poprzez socket
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// odbiór komunikatów poprzez socket

            Thread thread = new Thread(new Customer());
            thread.start();

            while (true) {
                System.out.println(" >> ");
                String string = sc.nextLine();
                if (!string.equalsIgnoreCase("q")) {
                    printWriter.println(name + " : " + string);
                } else {
                    printWriter.println(name + " rozłączył się.");
                    printWriter.flush();
                    printWriter.close();
                    sc.close();
                    socket.close();
                }
            }

        } catch (Exception e) {

        }
    }

    class Customer implements Runnable {

        @Override
        public void run() {
            String message;

            try {

                while ((message = bufferedReader.readLine()) != null) {
                    String subString[] = message.split(" : ");
                    if (!subString[0].equals(name)) {
                        System.out.println(message);
                        System.out.println(">> ");
                    }
                }
            } catch (Exception e) {

                System.out.println("Połączenie zostało zakończone. ");
            }
        }
    }
}

