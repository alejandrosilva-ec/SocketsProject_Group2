package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5000;
    private static final int NUM_THREADS = 10;
    private static ExecutorService executorService;

    public static void main(String[] args) {

        // Criamos o pool de threads para gerir múltiplos clientes em paralelo
        executorService = Executors.newFixedThreadPool(NUM_THREADS);

        try {
            // Inicializa o ServerSocket na porta 5000
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            // Ciclo infinito para aceitar novas ligações continuamente
            while (true) {
                // O servidor bloqueia aqui à espera de um novo cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket.getRemoteSocketAddress());

                // Criamos o handler passando apenas o socket (o mapa de clientes fica para o Módulo 2)
                ClientHandler handler = new ClientHandler(clientSocket);

                // Submete o handler para ser executado por uma thread do pool
                executorService.execute(handler);
            }

        } catch (IOException e) {
            System.out.println("Server error " + e.getMessage());
        } finally {
            // Garante que o pool de threads é desligado se o servidor parar
            executorService.shutdown();
        }
    }
}