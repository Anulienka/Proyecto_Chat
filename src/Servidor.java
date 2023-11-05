import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(4444);
        Socket cliente;


        while (true) {
            cliente = servidor.accept();
            System.out.println("Nuevo cliente se ha conectado");
            HiloChat hilo = new HiloChat(cliente);
            hilo.start();
        }
    }
}
