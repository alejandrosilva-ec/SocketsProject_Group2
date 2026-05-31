package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor TCP Concorrente - Módulo 1: Infraestrutura do Server TCP e Concorrência
 * 
 * Responsabilidades principais:
 * - Implementar ServerSocket para aceitar ligações
 * - Configurar ExecutorService (pool de threads) para atender cada cliente sem bloquear o servidor
 * - Criar thread independente para cada cliente conectado
 * 
 * A lógica de negócio (protocolo Echo) é delegada ao ClientHandler.
 */
public class Server {
    
    private static final int PORT = 5000;
    private static final int NUM_THREADS = 10; // Tamanho do thread pool
    private static ExecutorService executorService;
    
    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("=== Concurrent EchoServer listening on port " + PORT + " ===");
            System.out.println("Thread pool: " + NUM_THREADS);
            
            while (true) {
                try {
                    // ServerSocket.accept() bloqueia até receber uma ligação
                    Socket client = server.accept();
                    System.out.println("[NEW CONNECTION] " + client.getRemoteSocketAddress());
                    
                    // Submete o tratamento do cliente ao thread pool
                    // Cada cliente é processado numa thread independente
                    ClientHandler handler = new ClientHandler(client);
                    executorService.execute(handler);
                    
                } catch (IOException e) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Fatal server error: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }
}

/**
 * Handler para cada cliente - executado numa thread separada do pool
 * 
 * Responsabilidades:
 * - Processar o protocolo Echo (ler linhas e devolver a mesma linha)
 * - Gerir a ligação até o cliente desconectar
 * - Fechar recursos (streams e socket) ao terminar
 */
class ClientHandler implements Runnable {
    
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            // Inicializa streams para comunicação com o cliente
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            
            System.out.println("[CLIENT CONNECTED] " + socket.getRemoteSocketAddress());
            
            String line;
            
            // Ciclo de leitura: in.readLine() devolve null quando o cliente fecha a ligação
            while ((line = in.readLine()) != null) {
                System.out.println("[RECEIVED] " + socket.getRemoteSocketAddress() + " > " + line);
                
                // Echo: devolve exatamente a mesma linha
                out.write(line);
                out.write("\n");
                out.flush();
                
                System.out.println("[SENT] " + socket.getRemoteSocketAddress() + " < " + line);
            }
            
            System.out.println("[CLIENT DISCONNECTED] " + socket.getRemoteSocketAddress());
            
        } catch (IOException e) {
            System.err.println("[ERROR] " + socket.getRemoteSocketAddress() + " - " + e.getMessage());
        } finally {
            // Fecha recursos de forma segura
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("[ERROR CLOSING] " + e.getMessage());
            }
        }
    }
}