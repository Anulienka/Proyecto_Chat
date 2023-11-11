
import java.io.*;
import java.net.Socket;

/**
 * @author Anna
 *
 * La clase Cliente representa un cliente en la aplicación de chat
 * Se conecta al servidor y permite a usuario enviar mensaje a otros clientes y recibir mensajes de otros clientes.
 */

public class Cliente {

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int puerto = 4444;

        Socket cliente = new Socket(host, puerto);

        //flujo de entrada y salida
        BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        BufferedReader clienteEscribe = new BufferedReader(new InputStreamReader(System.in));

        //para que cliente pueda recibir mensajes de otros clientes, hay que crear hilo que recibe mensajes
        HiloCliente recibir = new HiloCliente(cliente, br);
        recibir.start();


        //primero cliente introduce su username
        System.out.println("Introduce tu nombre: ");
        String username = clienteEscribe.readLine();
        //enviamos nombre de usuario (eso recoge hilo servidor)
        bw.write(username);
        bw.newLine();
        bw.flush();

        //luego escribe mensaje
        System.out.println("Puedes empezar a chatear :)");
        String mensaje = " ";

        //hasta que cliente no sale del chat puede enviar mensajes
        while (cliente.isConnected()) {
            try {
                //System.out.println("Introduce mensaje o fin para terminar:");
                mensaje = clienteEscribe.readLine();
                String mensajeEnviar = username.toUpperCase() + ": " + mensaje;
                System.out.println(mensajeEnviar);
                //envio cadena al servidor
                bw.write(mensajeEnviar);
                bw.newLine();
                bw.flush();

        } catch(IOException e){
            //cerrar streams y conexiones
            cierraFlujos(cliente, bw, br);
        }
    }
}

    /**
     * Cierra los flujos y la conexión del cliente.
     *
     * @param cliente El socket de conexión del cliente.
     * @param bw El BufferedWriter.
     * @param br El BufferedReader.
     */
    private static void cierraFlujos(Socket cliente, BufferedWriter bw, BufferedReader br) {
        //cierro flujos
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (cliente != null) {
                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
