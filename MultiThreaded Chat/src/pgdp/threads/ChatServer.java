package pgdp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static int port;
    private static final List<ChatThread> connections = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length == 1)
            port = Integer.parseInt(args[0]);
        else port = 3000;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (connections.size() < 49) {
                Socket clientSocket = serverSocket.accept();

                synchronized (connections) {
                    ChatThread thread = new ChatThread(clientSocket,"");
                    thread.start();
                    connections.add(thread);
                    for (int i = 0; i < connections.size() - 1; i++) {
                        if (connections.get(i).hasEnteredTheChat()) {
                            connections.get(i).notifyClient();
                        }

                    }
                }
            }

        } catch (IOException e) {
            //do nothing
        }
    }

    private static class ChatThread extends Thread{

        private BufferedReader in;
        private PrintWriter out;
        private Socket clientSocket;
        private String username;
        private boolean waitingUsername = true;
        private LocalTime registrationTime;
        private String[] penguinFacts = new String[] {"There are approximately 12 million penguins.",
                                                        "Different species of penguins live from 6-40 years.",
                                                        "The smallest penguin species grow up to 25 cm / 10 inches",
                                                        "Penguins can swim up to 35 kmh / 22 mph",
                                                        "Penguins are cute, lovely and huggable, don't you agree?"};
        private int cnt = 0;

        public boolean hasEnteredTheChat() {return  !waitingUsername; }

        public ChatThread(Socket clientSocket, String username) {
            this.clientSocket = clientSocket;
            this.username = username;
            registrationTime = LocalTime.now();
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String fromClient;
                while ((fromClient = in.readLine()) != null) {
                    synchronized (connections) {
                        if (waitingUsername) {
                            username = fromClient;
                            waitingUsername = false;
                            out.println("Welcome to the chat "+ username); //todo comment out ?
                            continue;
                        }

                        if (fromClient.startsWith("@")) {
                            int space = fromClient.indexOf(" ");
                            String otherUser = fromClient.substring(1,space);
                            String message = fromClient.substring(space + 1);
                            int index = chatContainsUser(otherUser);
                            if (index != -1) {
                                connections.get(index).sendMessage(LocalTime.now().toString()+" "+username+": "+message);
                            } else {
                                out.println("User \""+otherUser+"\" is not in the chat!");
                            }

                        } else if (fromClient.startsWith("WHOIS")) {
                            for (ChatThread user : connections) {
                                if (user.getUsername().equals(username)) {
                                    out.println("you connected since " + user.getTime().toString());
                                    continue;
                                }
                                out.println(user.getUsername() + " connected since " + user.getTime().toString());
                            }

                        } else if (fromClient.startsWith("LOGOUT")) {
                            connections.remove(this);
                            in.close();
                            out.close();
                            clientSocket.close();
                            break;

                        } else if (fromClient.startsWith("PENGU")) {
                            cnt++;
                            for (ChatThread user : connections) {
                                user.sendMessage(penguinFacts[cnt % 5]);
                            }
                        } else {
                            for (ChatThread user : connections) {
                                if (user.getUsername().equals(username))
                                    continue;
                                user.sendMessage(fromClient);
                            }
                        }
                    }
                }

                clientSocket.close();
            } catch (IOException e) {
                //do nothing
            }
        }

        //helper method
        //return index of user if he is present in the chat
        //otherwise return -1
        private synchronized int chatContainsUser(String username) {
            for (int i = 0; i < connections.size(); i++) {
                if (connections.get(i).getUsername().equals(username)) {
                    return i;
                }
            }
            return -1;
        }

        public synchronized void notifyClient() {
            this.out.println("A new user has entered the chat!");
        }


        public synchronized void sendMessage(String message) {
            this.out.println(message);
        }

        //----getters---
        public String getUsername() {
            return username;
        }

        public LocalTime getTime() {
            return registrationTime;
        }
    }
}
    
