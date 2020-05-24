package pgdp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private static int portNumber = 3000;
    private static String serverAddress = "localhost";
    private static BufferedReader in;
    private static boolean serverRunning = false;

    public static void main(String[] args) {
        if (args.length == 2) {
            portNumber = Integer.parseInt(args[0]);
            serverAddress = args[1];
        }

        try (Socket socket = new Socket(serverAddress,portNumber)){
            in = new BufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            serverRunning = true;
            System.out.println("Welcome to the chat\n" +
                                "Please give your username: ");
            out.println(userInput.readLine());
            Receiver msgReceiver = new Receiver();
            msgReceiver.start();

            while (serverRunning) {
                System.out.println("1. Type @username<space><message> to message someone in the chat\n"+
                                    "2. Type 'WHOIS' to get a list of all connected users\n"+
                                    "3. Type 'LOGOUT' to leave the chat!\n"+
                                    "4. Type 'PENGU' to find out a penguin fact!");

                String input = userInput.readLine();
                out.println(input);
                if (input.equals("LOGOUT")) {
                    socket.close();
                    break;
                }
            }


        } catch (IOException e) {
            //do nothing;
        }
    }

    public static class Receiver extends Thread {
        @Override
        public void run() {
            String fromServer;
            try {
                while (/*true*/(fromServer = in.readLine()) != null) {
                        System.out.println(fromServer);
                }

            }catch (IOException e) {
                System.out.println("Connection with server ended!");
                serverRunning = false;
            }
        }
    }
}
