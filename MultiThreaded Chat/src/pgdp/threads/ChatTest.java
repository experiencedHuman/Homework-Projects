package pgdp.threads;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {
    private final String NOTIFICATION = "A new user has entered the chat!";
    private final String[] possibleFacts = new String[] {"There are approximately 12 million penguins.",
            "Different species of penguins live from 6-40 years.",
            "The smallest penguin species grow up to 25 cm / 10 inches",
            "Penguins can swim up to 35 kmh / 22 mph",
            "Penguins are cute, lovely and huggable, don't you agree?"};


    //test PENGU & normal message
    @Test
    void test_PENGU_and_Normal_Message() throws InterruptedException {
        final String u1 = "wale";
        final String u2 = "shark";
        testWithIO((in1,out1) -> {
            out1.println(u1);
            Thread.sleep(50); //wait a very short moment for the user to add his username
            assertEquals("Welcome to the chat wale",in1.readLine());

            testWithIO((in2, out2) -> {
                out2.println(u2);
                Thread.sleep(50);
                assertEquals("Welcome to the chat shark",in2.readLine());
                assertEquals(NOTIFICATION,in1.readLine());

                out2.println("PENGU");
                final String factFromServer = in2.readLine();
                assertTrue(Stream.of(possibleFacts).anyMatch(fact -> fact.matches(factFromServer)));

                final String factFromServerForUser1 = in1.readLine();
                assertTrue(Stream.of(possibleFacts).anyMatch(fact -> fact.matches(factFromServerForUser1)));


                testWithIO((in3, out3) -> {
                    out3.println("fish");
                    Thread.sleep(30);
                    assertEquals("Welcome to the chat fish", in3.readLine());
                    assertEquals(NOTIFICATION,in1.readLine());
                    assertEquals(NOTIFICATION,in2.readLine());

                    final String normalMessage = "A normal message to all species";
                    out3.println(normalMessage);

                    assertEquals(normalMessage,in1.readLine());
                    assertEquals(normalMessage,in2.readLine());
                });

            });

        });
    }

    //test WHOIS
    //test LOGOUT
    @Test
    void test_LOGOUT() throws InterruptedException {
        final String student1 = "dijkstra";
        final String student2 = "Superman";
        final String student3 = "alan turing";
        testWithIO((in1,out1) -> {
            out1.println(student1);
            LocalTime registration1 = LocalTime.now();
            Thread.sleep(50); //wait for username
            assertEquals("Welcome to the chat dijkstra", in1.readLine());
            testWithIO((in2, out2) -> {
                out2.println(student2);
                LocalTime registration2 = LocalTime.now();
                Thread.sleep(50);
                assertEquals(NOTIFICATION, in1.readLine());
                assertEquals("Welcome to the chat Superman", in2.readLine());
                testWithIO((in3,out3) -> {
                    out3.println(student3);
                    LocalTime registration3 = LocalTime.now();
                    Thread.sleep(50);
                    assertEquals("Welcome to the chat alan turing", in3.readLine());
                    assertEquals(NOTIFICATION,in1.readLine());
                    assertEquals(NOTIFICATION,in2.readLine());

                    out2.println("WHOIS");
                    List<String> expected = new ArrayList<>();
                    expected.add("dijkstra connected since "+registration1.toString().substring(0,5));
                    expected.add("you connected since "+registration2.toString().substring(0,5));
                    expected.add("alan turing connected since "+registration3.toString().substring(0,5));
                    List<String> actual = new ArrayList<>();
                    for (int i = 0; i < 3; i++)
                        actual.add(in2.readLine());

                    for (int i = 0; i < 3; i++) {
                        String expectedBegin = expected.get(i).substring(0,expected.get(i).indexOf("connected"));
                        String expectedEnd = expected.get(i).substring(20);
                        assertTrue(actual.get(i).startsWith(expectedBegin));
                        assertTrue(actual.get(i).matches(".*"+expectedEnd+".*"));
                    }


                    //student1 will log out
                    out1.println("LOGOUT");
                });
                List<String> expected = new ArrayList<>();
                List<String> actual = new ArrayList<>();

                //student2 wants to know who is still in the chat
                //in the response from server he will notice that student1('dijkstra') will no longer be in the chat
                out2.println("WHOIS");
                for (int i = 0; i < 2; i++)
                    actual.add(in2.readLine());
                expected.add("you connected since "+registration2.toString().substring(0,5));

                //we cannot use the @registration3 variable in this block
                //but since we know that student3 registered only shortly after student2, registration time of student 2 can be a reference point
                expected.add("alan turing connected since "+registration2.toString().substring(0,4));

                for (int i = 0; i < 2; i++) {
                    String expectedBegin = expected.get(i).substring(0,expected.get(i).indexOf("connected"));
                    String expectedEnd = expected.get(i).substring(25);
                    assertTrue(actual.get(i).startsWith(expectedBegin));
                    assertTrue(actual.get(i).matches(".*"+expectedEnd+".*"));
                }
            });
        });
    }

    //test @sendmessage
    @Test
    void test2Users() throws InterruptedException {
        final String user1 = "Tom";
        final String user2 = "Ben";
        testWithIO((in1, out1) -> {
            out1.println(user1);
            Thread.sleep(50);
            assertEquals("Welcome to the chat Tom", in1.readLine());

            testWithIO((in2, out2) -> {
                out2.println(user2);
                assertEquals(NOTIFICATION,in1.readLine());
                assertEquals("Welcome to the chat Ben", in2.readLine());
                out1.println("@Ben hello");
                assertTrue(in2.readLine().endsWith("hello"));

                out2.println("@Tom hi tom, how's your company doing?");
                assertTrue(in1.readLine().contains("company"));

                out1.println("@Ben very good ben, thanks for asking, just closed a deal yesterday for over $1.2Billion");
                String fromBen = in2.readLine();
                assertTrue(fromBen.contains("$1.2Billion") && fromBen.contains("yesterday"));

                out1.println("@Olivia hey olivia");
                assertEquals("User \"Olivia\" is not in the chat!", in1.readLine());
            });
        });
    }


    static void testWithIO(IOBasedTest<BufferedReader, PrintWriter> testAction) throws InterruptedException {
        try (Socket s = new Socket("localhost", 3000)) {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            testAction.runTest(in, out);
        } catch (IOException e) {
            fail("Netzwerkfehler: " + e);
        }
    }

    @FunctionalInterface
    public interface IOBasedTest<T, U> {
        void runTest(T t, U u) throws IOException, InterruptedException;
    }
}
