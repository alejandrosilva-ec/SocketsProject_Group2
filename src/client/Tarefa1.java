package client; // Define o pacote onde esta a class está localizada 
import java.io.IOException; // Importa a classe que trata erros de entrada e saida (I\0)
import java.net.Socket; // IMPORTA A class socket, usada para criar ligaçoes cliente servidor 


public class Tarefa1 {
	// guarda o numero da porta do servidor
    private static final int PORTA= 5000; 
    private static final String HOST = "localhost"; // localhost significa o proprio computador

    public static void main(String[] args) { 
        try { // tenta exucutar o codigo dentro deste bloco

            System.out.println("Attempting to connect to the server at " + HOST + PORTA + "...");

        // passo 1: abre a porta de comunicacao (Socket ) [span_1](end_span)
            // private String HOST;
            Socket socket = new Socket(HOST, PORTA); // cria uma ligação socket ao servidor usando o endereço e a porta indicados.

            System.out.println("Success! Database connection established");


//  fecha logo a seguir porque é só um teste
            socket.close(); // fecha a ligacao imediatamente 
            // porque este programa serve apenas para testar comunicaçao
            System.out.println("Secure connection established.");
        } catch (IOException e) {
            System.out.println("Error: the server is offline! " + e.getMessage());
        }
    }
        }



