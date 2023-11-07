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
        System.out.println("Introduce tu username: ");
        String username = clienteEscribe.readLine();
        bw.write(username);
        bw.newLine();
        bw.flush();

        //luego escribe mensaje
        System.out.println("Puedes empezar a chatear :)");
        String mensaje = " ";

        do{
            System.out.println("Introduce mensaje o fin para terminar:");
            mensaje =clienteEscribe.readLine();
            String mensajeEnviar = username + ": " + mensaje;
            bw.write(mensajeEnviar);//envio cadena al servidor
            bw.newLine();
            bw.flush();

            //recibo mensaje de otros clientes
            String mensajeOtros = br.readLine();
            System.out.println(mensajeOtros);

        }while (!mensaje.equalsIgnoreCase("fin"));
    }
}
