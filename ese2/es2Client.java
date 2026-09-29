package ese2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class es2Client {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 8000;
        Scanner sc = new Scanner(System.in);
        try{
            Socket socketClient = new Socket(serverIP, serverPort);
            //creo gli stream
            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            String messaggio = in.readLine();
            System.out.println("Server: " + messaggio);
            boolean continua = true;
            while(continua){
                System.out.print("Client: ");
                String input = sc.nextLine();
                out.println(input);
                messaggio = in.readLine();
                if(messaggio == null){
                    System.out.println("Il server ha chiuso la connessione.");
                    break;
                }
                System.out.println("Server: " + messaggio);
            }
            socketClient.close();
            System.out.println("Connessione chiusa.");
        } catch (Exception e) {
            System.out.println("Errore nella connessione al server: " + e);
        }
    }
}
