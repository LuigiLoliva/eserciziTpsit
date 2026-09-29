package ese3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class client3 {
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
            String input;
            String n1;
            String n2;
            while(continua){
                System.out.print("Client (operazione): ");
                do{
                    input = sc.nextLine();
                }while(!input.equals("OP1") &&
                    !input.equals("OP2") &&
                    !input.equals("OP3") &&
                    !input.equals("OP4") &&
                    !input.equals("0"));
                if(input.equals("0")){
                    out.println(input);
                    break;
                }
                System.out.print("Client (n1): ");
                do{
                    n1 = sc.nextLine();
                    if(!n1.matches("-?\\d+")){ //metodo trovato su internet per verificare se una stringa è un numero intero
                        System.out.println("Errore: devi inserire un numero.");
                    }
                }while(!n1.matches("-?\\d+"));
                System.out.print("Client (n2): ");
                do{
                    n2 = sc.nextLine();
                    if(!n2.matches("-?\\d+")){
                        System.out.println("Errore: devi inserire un numero.");
                    }
                }while(!n2.matches("-?\\d+"));
                out.println(input + " " + n1 + " " + n2);
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
