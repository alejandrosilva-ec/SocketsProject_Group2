package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// Esta classe agora vive no seu próprio ficheiro para facilitar o trabalho em equipa no Git
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // O construtor agora só precisa de receber o socket do cliente
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // COMENTADO POR AGORA: Evita expulsar utilizadores inativos no chat após 30 segundos
            // socket.setSoTimeout(30000);

            // Inicializa o leitor de texto forçando o encoding UTF-8 (requisito obrigatório)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            
            // Inicializa o escritor de texto forçando também o encoding UTF-8 e com auto-flush ativo
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            String message;

            // Ciclo de leitura que processa as mensagens recebidas do cliente
            while ((message = in.readLine()) != null) {
                System.out.println("Received from " + socket.getRemoteSocketAddress() + ": " + message);

                // Protocolo Echo temporário: Devolve a mensagem recebida ao cliente
                out.println(message);

                System.out.println("Sent to " + socket.getRemoteSocketAddress() + ": " + message);
            }

        } catch (IOException e) {
            System.out.println("Client error " + socket.getRemoteSocketAddress() + " " + e.getMessage());
        } finally {
            // Bloco cleanup: Garante o fecho dos fluxos e do socket quando o cliente sai
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
                System.out.println("Client disconnected and resources cleaned: " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                System.out.println("Close error " + e.getMessage());
            }
        }
    }
}