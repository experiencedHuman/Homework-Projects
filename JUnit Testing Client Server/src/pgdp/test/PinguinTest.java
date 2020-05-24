package pgdp.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Stream;

import static junit.framework.Assert.*;

public class PinguinTest {

    @org.junit.jupiter.api.Test
    @Timeout(20)
    public void testSumCalculation() {
        try(
                Socket client = new Socket("127.0.0.1",25565);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ){
            client.setSoTimeout(1000); //server responds always under 1 second
            String fromServer;
            long timeWaited;

            //ask the penguin
            String askSum = "Was ergibt die folgende Summe?";
            out.println(askSum);
            String sumMsg = "10 + 3 + 3 + "+Integer.MAX_VALUE+" + 4";
            out.println(sumMsg);

            //start waiting as we've already asked the server
            long waiting = System.currentTimeMillis();

            long expectedAnswer = Integer.MAX_VALUE;
            expectedAnswer += 20;

            //get answer from server
            fromServer = in.readLine();

            //check if the server was too quick in calculating the result
            //meaning he is a Falschuin ^-°
            timeWaited = System.currentTimeMillis() - waiting;
            System.out.println(timeWaited);
            assertFalse(timeWaited < 100);

            //test if the calculation was correct
            long answerFromServer = Long.parseLong(fromServer);
            System.out.println(answerFromServer);
            assertEquals(expectedAnswer, answerFromServer);

            String byeFromClient = "Bye";
            out.println(byeFromClient);

            //get goodbye message from server
            fromServer = in.readLine();
            if (fromServer.equals("Bye")){
                client.close();
            }else fail();


        }catch (NumberFormatException nfe) {
            fail("answer from falschuin was not a long!");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Timeout(20)
    public void testSumCalculationWithBadInputFormat() {
        try (
                Socket client = new Socket("127.0.0.1",25565);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
                ){
                client.setSoTimeout(1000);

            String fromServer;
            long timeWaited;

            String askSUm = "Was ergibt die folgende Summe?";
            out.println(askSUm);
            String msg = "???"+89+".*"+"falschuin trap";
            out.println(msg);

            long waiting = System.currentTimeMillis();
            String expectedAnswer = "Das ist keine Summe!";

            fromServer = in.readLine();

            timeWaited = System.currentTimeMillis() - waiting;
            assertTrue(timeWaited < 50);

            //evaluate answer
            assertEquals(expectedAnswer,fromServer);

            //we ask again
            String secondQuestion = "Was ergibt die folgende Summe?";
            out.println(secondQuestion);
            String msg2 = "ok + ok + ok";
            out.println(msg2);
            waiting = System.currentTimeMillis();

            fromServer = in.readLine();
            timeWaited = System.currentTimeMillis() - waiting;
            assertTrue(timeWaited < 50);
            assertEquals(expectedAnswer, fromServer);

            String servus = "Servus";
            out.println(servus);
            fromServer = in.readLine();
            if (servus.equals(fromServer)) {
                client.close();
            }else fail();


        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Timeout(20)
    public void askMultipleTimesAndSaluteMultipleTimes() {
        try (
                Socket client = new Socket("127.0.0.1",25565);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(1000);

            String fromServer;

            String question = "Zähle die Fische in";
            final int expectedAnswer = 6;
            int counter = 0;
            while (counter++ < 10) {
                Stream.of("Fischers Fritz fischt frische Fische frische Fische fischt Fischers Fritz",
                            "Früh in der Frische fischen Fischer frische Fischein der Fischach fischfisch",
                            "Mischwasserfischer heißen Mfischwasserfischer weil Mischwasserfischer im Mischwasser Mischwasserfische fischen")
                        .forEach(msg -> {
                            out.println(question);
                            out.println(msg);
                            try {
                                int answer = Integer.parseInt(in.readLine());
                                assertEquals(expectedAnswer, answer);
                            } catch (NumberFormatException | IOException e) {
                                try {
                                    client.close();
                                } catch (IOException ex) {
                                    fail();
                                }
                                fail();
                            }
                        });
            }


            String emptyQuestion = "";
            out.println(emptyQuestion);
            fromServer = in.readLine();
            assertEquals("0", fromServer);


            String bisDanKlein = "bis dann";
            out.println(bisDanKlein);
            out.println("don't hope too much");
            fromServer = in.readLine();
            assertEquals("Tut mir leid, das habe ich nicht verstanden.", fromServer);

        }catch (IOException e) {
            fail();
        }
    }

    @Test
    @Timeout(20)
    public void testNoQuestion() {
        try (
                Socket client = new Socket("127.0.0.1",25565);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(1000);

            String fromServer;
            String message = "Wie geht es dir?";
            String noHope = "don't hope too much";
            out.println(message);
            out.println(noHope);
            fromServer = in.readLine();
            assertEquals("Tut mir leid, das habe ich nicht verstanden.", fromServer);

        }catch (IOException e) {
            fail();
        }
    }


    @Test
    @Timeout(20)
    public void askBothQuestions() {
        try (
                Socket client = new Socket("127.0.0.1",25565);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
                ) {
                client.setSoTimeout(1000);
                String fromServer;
                long waiting = System.currentTimeMillis(), waited;

                String question1 = "Was ergibt die folgende Summe?";
                long bigNumber = Integer.MAX_VALUE;
                bigNumber += Integer.MAX_VALUE;
                String firstMessage = "1 + 1 + 1 + "+bigNumber+" + "+Integer.MIN_VALUE;
                long expectedAnswer = Integer.MAX_VALUE;
                expectedAnswer += 2;

                out.println(question1);
                out.println(firstMessage);
                fromServer = in.readLine();
                waited = System.currentTimeMillis() - waiting;

                assertFalse(waited < 100);

                long answer = Long.parseLong(fromServer);
                assertEquals(expectedAnswer, answer);


                String question2 = "Zähle die Fische in";
                String secondMsg = "Fisch           Fisch                   Fisch";
                final int expectedNr = 3;
                out.println(question2);
                out.println(secondMsg);
                fromServer = in.readLine();
                int nr = Integer.parseInt(fromServer);
                assertEquals(expectedNr, nr);

                String bisdann = "Bis dann";
                out.println(bisdann);
                fromServer = in.readLine();
                assertEquals(bisdann, fromServer);


        } catch (NumberFormatException | IOException e) {
            fail();
        }
    }

}
