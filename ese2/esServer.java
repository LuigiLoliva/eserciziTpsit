package ese2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class esServer {
    private ServerSocket serverSocket;
    public esServer(int port) throws IOException {
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
    public void GestoreThread(Socket socket) throws IOException{
        //creo gli stream
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Questo server verifica se una stringa è palindroma: (0 chiudi la connesione)");
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
                    risp = VerificaPalindromo(mess);
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
    public static String VerificaPalindromo(String str) {
        StringBuilder sb = new StringBuilder(str);
        String reversedStr = sb.reverse().toString();
        if (str.equals(reversedStr)) {
            return "MIRROR";
        } else {
            return reversedStr;
        }
    }
    public static void main(String[] args) {
        int port = 8000;
        try {
            esServer server = new esServer(port);
            while (true) {
                server.listen();
            }
        } catch (IOException e) {
            System.out.println("Errore nell'avvio del server: " + e);
        }
    }
}
