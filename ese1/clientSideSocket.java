package ese1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientSideSocket {

    public static void main(String[] args) {

        String serverIP = "127.0.0.1";
        int serverPort = 8000;
        // Scanner per leggere i dati inseriti dall'utente
        Scanner sc = new Scanner(System.in);

        try {
            Socket socketClient = new Socket(serverIP, serverPort);

            System.out.println("Connesso al server " + serverIP + ":" + serverPort);

            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

            String messaggio = in.readLine();

            System.out.println("Server: " + messaggio);
            boolean continua = true;

            while (continua) {

                System.out.print("Scrivi la stringsa da inviare al server (0 per chiudere la connessione): ");
                System.out.print("Inserisci la stringa: ");

                String testo = sc.nextLine();


                String richiesta = testo;

                System.out.println("Invio richiesta al server...");

                //invio richiesta al server
                out.println(richiesta);

                //ricevo risposta dal server

                String risposta = in.readLine();

                if (risposta == null) {

                    System.out.println("Il server ha chiuso la connessione.");
                    break;
                }

                System.out.println("Server: " + risposta);
            }

            socketClient.close();

            System.out.println("Connessione chiusa.");

        } catch (IOException e) {

            System.out.println("Errore nella comunicazione con il server: " + e);

        } finally {

            sc.close();
        }
    }
}