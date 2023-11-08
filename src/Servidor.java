import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(4444);
        Socket cliente;
        System.out.println("Servidor funcionando...");


        while (true) {
            cliente = servidor.accept();
            System.out.println("Nuevo cliente se ha conectado");
            HiloServidor hilo = new HiloServidor(cliente);
            hilo.start();
        }
    }
}
