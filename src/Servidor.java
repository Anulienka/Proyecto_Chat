import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(4444);
        Socket cliente;


        while (true) {
            cliente = servidor.accept();
            System.out.println("Nuevo cliente se ha conectado desde : " + cliente.getInetAddress());
            HiloServidor hilo = new HiloServidor(cliente);
            hilo.start();
        }
    }
}
