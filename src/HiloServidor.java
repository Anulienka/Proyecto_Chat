import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/**
 * La clase HiloServidor representa un hilo que gestiona conexiones de clientes que estan en chat.
 * En esta clase se encuentra la lista de todos los clientes que están en chat
 * Añade cliente con su username a lista de clientes
 * Recibe informacion que escribe usuario en consola y la envia a otros clientes de la lista de clientes
 */

public class HiloServidor extends Thread {

    private Socket cliente;
    private String nombreCliente;
    //necesito lista de clientes dentro de chat para enviar mensaje a todos los clientes
    private static ArrayList<HiloServidor> clientes = new ArrayList<>();

    //cada cliente tiene su flujo de entrada y salida
    //br- servidor lee que escribe cliente
    private BufferedReader br;
    //bw- desde servidor se envia mensaje de cliente a otros clientes
    private BufferedWriter bw;


    /**
     * Constructor de la clase HiloServidor.
     * @param cliente El socket de conexión del cliente.
     * @throws IOException Si hay un error al establecer flujos de entrada y salida.
     */
    public HiloServidor(Socket cliente) throws IOException {
        this.cliente = cliente;
    }

    @Override
    public void run() {

        try {
            br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
            //nombre del cliente que escribe desde consola
            this.nombreCliente = br.readLine();
            //anado cliente a la lista
            clientes.add(this);
            enviarMensajeUsuarios(nombreCliente + " ha entrado en chat.");

        } catch (IOException e) {
            //si algo va mal, se cierran streams y conexiones, porque cliente ya no esta conectado
            cierraFlujos(cliente, bw, br);
        }


        String mensajeCliente = "";

        //hasta que el cliente no se desconecte cerrando ventana del chat escribe mensajes a otros clientes
        while (cliente.isConnected()) {
            try {
                //lee que envia cliente
                mensajeCliente = br.readLine();
                //envia mensajes a otros clientes
                enviarMensajeUsuarios(mensajeCliente);

            } catch (IOException e) {
                //si algo va mal, se cierran streams y conexiones y se sale del bucle while, porque cliente ya no esta conectado
                cierraFlujos(cliente, bw, br);
                break;
            }
        }
    }

    /**
     * Envía un mensaje a todos los usuarios en el chat, excepto al usuario que escribe el mensaje.
     * @param mensaje El mensaje que se envia a usuarios.
     */
    private void enviarMensajeUsuarios(String mensaje) {
        //envio mensaje a usuarios del chat
        for (HiloServidor c : clientes) {
            try {
                if (!c.nombreCliente.equals(nombreCliente)) {
                    //envia mensaje
                    c.bw.write(mensaje);
                    c.bw.newLine();
                    c.bw.flush();
                }
            } catch (IOException e) {
                //si algo va mal, se cierran todos los flujos porque cliente ya no esta conectado
                cierraFlujos(cliente, bw, br);
            }
        }
    }

    /**
     * Elimina al cliente del chat y notifica a otros usuarios que ha salido.
     * Cierra los flujos y la conexión del cliente.
     * @param cliente El socket de conexión del cliente.
     * @param bw El BufferedWriter.
     * @param br El BufferedReader.
     */
    private void cierraFlujos(Socket cliente, BufferedWriter bw, BufferedReader br) {
        //primero elimino cliente del chat
        eliminarClienteDelChat();
        //luego cierro flujos
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

    /**
     * Elimina al cliente del chat y notifica a otros usuarios que ha salido.
     */
    private void eliminarClienteDelChat() {
        //elimina cliente si ha salido del chat
        clientes.remove(this);
        //envia mensaje a otros usuarios
        enviarMensajeUsuarios(nombreCliente + " ha salido del chat.");
    }

}