package pgdp.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class WebServer {

    static int port = 80;
    private static PinguDatabase database = new PinguDatabase();

    public static void main(String[] args) throws IOException {
        HtmlGenerator html = new HtmlGenerator();
        ExecutorService requestBalancer = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(port);

        requestBalancer.submit(() -> {
            while (!requestBalancer.isShutdown()) {
                try {
                    new ConnectionThread(serverSocket.accept(), html).start();
                } catch (Exception e) {
                  //do nothing
                }
            }
        });

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String command = userInput.readLine();
            if (command.startsWith("add")) {
                String newPingu = command.substring(command.indexOf(" ") + 1);
                DatingPingu datingPingu;
                try {
                    datingPingu = DatingPingu.parse(newPingu);

                    boolean added;
                    synchronized (database) {
                        added = database.add(datingPingu);
                    }

                    if (added)
                        System.out.println("Pingu has been added to the database!");
                    else
                        System.out.println("Pingu already exists in the database!");
                } catch (Exception e) {
                    System.out.println("Bad input!");
                }
            } else if(command.equals("shutdown")) {
                System.out.println("Server is shutting down!");
                requestBalancer.shutdown();
                serverSocket.close();
                break;

            } else {
                System.out.println("unknown command");
            }
        }
    }

    static class ConnectionThread extends Thread{

        private Socket client;
        private HtmlGenerator html;

        public ConnectionThread(Socket client, HtmlGenerator html) {
            this.client = client;
            this.html = html;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                HttpRequest httpRequest = null;
                HttpResponse httpResponse = null;

                String request = in.readLine();

                while (!in.readLine().equals("")) {/* consume useless information */}

                boolean process_request = true;
                try {
                    httpRequest = new HttpRequest(request);

                } catch (UnsupportedMethodException  unsupported_Method) {
                    if (unsupported_Method.getMessage() != null)
                        System.out.println(unsupported_Method.getMessage());
                    httpResponse = new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, html.generateStartPage());
                    process_request = false;
                    out.print(httpResponse);

                } catch (UnsupportedRequestException unsupported_Request) {
                    if (unsupported_Request.getMessage() != null)
                        System.out.println(unsupported_Request.getMessage());
                    httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST, html.generateStartPage());
                    process_request = false;
                    out.print(httpResponse);

                }

                if (!process_request) {
                    out.close();
                    client.close();
                    return;
                }

                if (httpRequest.getPath().equals("/")) {
                    out.print(new HttpResponse(HttpStatus.OK, html.generateStartPage()));

                } else if (httpRequest.getPath().equals("/find")) {
                    String sexualOrient = httpRequest.getParameters().get("sexualOrientation");
                    int minAge  = Integer.parseInt(httpRequest.getParameters().get("minAge"));
                    int maxAge  = Integer.parseInt(httpRequest.getParameters().get("maxAge"));
                    Set<String> hobbies = Arrays.stream(httpRequest.getParameters()
                                                                   .get("hobbies")
                                                                   .split("\\+"))
                                                .collect(Collectors.toSet());

                    SearchRequest sr = new SearchRequest(sexualOrient,minAge,maxAge,hobbies);

                    List<DatingPingu> dpList;
                    synchronized (database) {
                        dpList = database.findMatchesFor(sr);
                    }

                    out.print(new HttpResponse(HttpStatus.OK, html.generateFindPage(sr, dpList)));

                } else if (httpRequest.getPath().startsWith("/user")){
                    String p = httpRequest.getPath();
                    long userID = Long.parseLong(p.substring(p.lastIndexOf("/") + 1));

                    Optional<DatingPingu> dp;
                    synchronized (database) {
                        dp = database.lookupById(userID);
                    }

                    if (dp.isPresent()) {
                        out.print(new HttpResponse(HttpStatus.OK, html.generateProfilePage(dp.get())));
                    } else {
                        out.print(new HttpResponse(HttpStatus.NOT_FOUND, html.generateStartPage()));
                    }

                } else {
                    out.print(new HttpResponse(HttpStatus.FORBIDDEN, html.generateStartPage()));
                }

                out.close();
                client.close();
            } catch (UnsupportedMethodException | UnsupportedRequestException | IOException abc) {
              //do nothing
            } catch (NumberFormatException nfe) {
                System.out.println("UserID should be of type <long>!");
            }
        }
    }
}
