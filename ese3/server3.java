package ese3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class server3 {
    private ServerSocket serverSocket;
    public server3(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server avviato sulla porta " + port);
    }
    public void listen() throws IOException{
        Socket socket = serverSocket.accept();
        System.out.println("Nuovo client connesso: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        //gestione thread
        Thread thread = new Thread(() -> {
            try {
                GestoreThread(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
    private void GestoreThread(Socket socket) throws IOException{
        //creo gli stream
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Questo server svolge operazioni matematiche (OP1: somma, OP2: sottrazione, OP3: moltiplicazione, OP4: divisione) \n OP numero1 numero2: (0 chiudi la connesione)");
        //gestione richiesta
        String mess;
        String risp;
        try{
            while((mess = in.readLine()) != null){
                if(mess.equals("0")){
                    break;
                }else if(mess.equals(" ") || mess.equals("")){
                    risp = "errore";
                }else{
                    String segno = mess.split(" ")[0];
                    int numero = Integer.parseInt(mess.split(" ")[1]);
                    int numero2 = Integer.parseInt(mess.split(" ")[2]);
                    if(segno.equals("OP1")){
                        risp = String.valueOf(numero + numero2);
                    }else if(segno.equals("OP2")){
                        risp = String.valueOf(numero - numero2);
                    }else if(segno.equals("OP3")){
                        risp = String.valueOf(numero * numero2);
                    }else if(segno.equals("OP4")){
                        if(numero2 == 0){
                            risp = "errore: divisione per zero";
                        }else{
                            risp = String.valueOf((double)numero/numero2);
                        }
                    }else{
                        risp = "errore: operatore non valido";
                    }
                }
                out.println(risp);
            }
        }catch (IOException e){
            System.out.println("errore nella comunicazione");
        }finally {
            try {
                socket.close();
                System.out.println("Connessione con il client chiusa.");
            } catch (IOException e) {
                System.out.println("Errore nella chiusura del socket: " + e);
            }
        }
    }
    public static void main(String[] args) {
        int port = 8000;
        try {
            server3 server = new server3(port);
            while (true) {
                server.listen();
            }
        } catch (IOException e) {
            System.out.println("Errore nella creazione del server: " + e);
        }
    }
}
