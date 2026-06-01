package client;

import java.io.IOException; // Importa a classe para tratar erros de entrada e saída
import java.io.PrintWriter; // Importa a classe para enviar texto ao servidor
import java.net.Socket; // Importa a classe Socket para criar a ligação com o servidor
import java.util.Scanner; // Importa a classe Scanner para ler dados do teclado

public class tarefa2 {

    // Define a porta utilizada para a comunicação com o servidor
    private static final int PORTA = 5000;

    // Define o endereço do servidor
    private static final String HOST = "localhost";

    // Método principal do programa
    public static void main(String[] args) {

        try {

            // Informa o utilizador que o cliente está a tentar ligar ao servidor
            System.out.println("Attempting to connect to the server at " + HOST + ":" + PORTA + "...");

            // Cria uma ligação ao servidor usando o endereço e a porta definidos
            Socket socket = new Socket(HOST, PORTA);

            // Mensagem de sucesso caso a ligação seja estabelecida
            System.out.println("Success! Basic connection established.");

            // ======================================================
            // STEP 2: Output stream (sending data) and keyboard input
            // ======================================================

            // Cria um canal de saída para enviar mensagens ao servidor
            // O valor 'true' ativa o envio automático (auto-flush)
            PrintWriter saidaServidor = new PrintWriter(socket.getOutputStream(), true);

            // Cria um Scanner para ler o que o utilizador escreve no teclado
            Scanner teclado = new Scanner(System.in);

            // Apresenta a interface de envio de comandos
            System.out.println("\n=== MESSAGE SENDING INTERFACE ACTIVE ===");

            // Explica ao utilizador quais os comandos que pode escrever
            System.out.println("Type your commands (e.g., NICK JOAO or QUIT to exit):");

            // Ciclo infinito para permitir o envio contínuo de comandos
            while (true) {

                // Mostra o símbolo de prompt
                System.out.print("> ");

                // Lê uma linha escrita pelo utilizador após pressionar Enter
                String comando = teclado.nextLine();

                // Envia o comando para o servidor
                saidaServidor.println(comando);

                // Verifica se o utilizador escreveu QUIT
                // equalsIgnoreCase ignora diferenças entre maiúsculas e minúsculas
                if (comando.equalsIgnoreCase("QUIT")) {

                    // Sai do ciclo
                    break;
                }
            }

            // Fecha o Scanner para libertar recursos
            teclado.close();

            // Fecha a ligação ao servidor
            socket.close();

            // Informa que a sessão foi encerrada
            System.out.println("Session closed locally.");

        } catch (IOException e) {

            // Executado caso ocorra algum erro de ligação
            System.err.println("Error: the server is offline! " + e.getMessage());
        }
    }
}