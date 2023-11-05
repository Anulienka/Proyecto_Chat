import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int puerto = 4444;

        Socket cliente = new Socket(host, puerto);

        //flujo de entrada y salida
        BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        BufferedReader clienteEscribe = new BufferedReader(new InputStreamReader(System.in));

        //primero cliente introduce su username
        System.out.println("Introduce username: ");
        String username = clienteEscribe.readLine();
        bw.write(username);
        bw.newLine();
        bw.flush();

        System.out.println("Puedes empezar a chatear :)");

        //luego escribe mensaje
        while (true) {
            String mensaje = clienteEscribe.readLine();
            String mensajeEnviar = username + ": " + mensaje;
            bw.write(mensajeEnviar);
            bw.newLine();
            bw.flush();

            //llega mensaje de otros
            String mensajeDeOtros = br.readLine();
            System.out.println(mensajeDeOtros);

        }
    }
}
