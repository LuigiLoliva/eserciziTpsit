package ese1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class serSock{
    private ServerSocket serverSocket;

    public serSock(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server avviato sulla porta " + port);
    }

    public void listen() throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("Nuovo client connesso: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        //gestione thread
        Thread thread = new Thread(() -> {
            try {
                GestioneRichiesta(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

    }

    public void GestioneRichiesta(Socket socket) throws IOException {
        //creazione stream
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Questo server trasforma una stringa da minuscolo a maiusolo: (0 chiudi la connesione)");
        String mess;
        String risp;
        try{
            while((mess = in.readLine()) != null){
                if(mess.equals("0")){
                    break;
                }else if(mess.equals(" ") || mess.equals("")){
                    risp = "errore";
                }else{
                    risp = toUpperCase(mess);
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
    private String toUpperCase(String s){
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray()){
            if(c >= 'a' && c <= 'z'){
                c = (char)(c - ('a' - 'A')); //sottrazione di -32 ('a'-'A') differenza che ce tra carattere minuscolo e maiuscolo
            }
            sb.append(c);
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        int serverPort = 8000;
        try {
            serSock server = new serSock(serverPort);
            // Il server continua ad accettare nuovi client
            while (true) {
                server.listen();
            }
        } catch (IOException e) {

            System.out.println("Errore durante l'avvio del server: " + e);
        }
    }

}
