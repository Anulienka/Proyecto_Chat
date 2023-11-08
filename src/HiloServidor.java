import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {

    private Socket cliente;
    private String nombreCliente;
    //necesito lista de clientes dentro de chat para enviar mensaje a todos los clientes
    private static ArrayList<HiloServidor> clientes = new ArrayList<>();
    private BufferedReader br;
    private BufferedWriter bw;

    public HiloServidor(Socket cliente) throws IOException {
        this.cliente = cliente;
        //cada cliente tiene su flujo de entrada y salida
        //br- servidor lee que escribe cliente
        this.br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        //bw- desde servidor se envia mensaje de cliente a otros clientes
        this.bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        //nombre del cliente que escribe desde consola
        this.nombreCliente = br.readLine();
        //anado cliente a la lista
        clientes.add(this);
        //en constructor ponemos y mensaje que un cliente ha entrado en chat, este mensaje se envia una vez
        enviarMensajeUsuarios(nombreCliente + " ha entrado en chat.");
    }

    @Override
    public void run() {
        String mensajeCliente = "";

        //hasta que el cliente no se desconecte cerrando ventana del chat escribe mensajes a otros clientes
        while (cliente.isConnected()) {
            try {
                //lee que envia cliente
                mensajeCliente = br.readLine();
                //envia mensajes a otros clientes
                enviarMensajeUsuarios(mensajeCliente);

            } catch (IOException e) {
                //si algo va mal, se cierran todos los flujos y se sale del bucle while, porque cliente ya no esta conectado
                cierraFlujos(cliente, bw, br);
                break;
            }
        }
    }

    private void enviarMensajeUsuarios(String mensaje) {
        //envio mensaje a usuarios del chat
        for (HiloServidor c : clientes) {
            try {
                if (c.nombreCliente != nombreCliente) {
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

    private void eliminarClienteDelChat() {
        //elimina cliente si ha salido del chat
        clientes.remove(this);
        //envia mensaje a otros usuarios
        enviarMensajeUsuarios(nombreCliente + " ha salido del chat.");
    }

}