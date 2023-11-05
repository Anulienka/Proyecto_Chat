import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HiloChat extends Thread {

    private Socket cliente;
    private String nombreCliente;
    //necesito lista de clientes dentro de chat para enviar mensaje a todos los clientes
    private static ArrayList<HiloChat> clientes = new ArrayList<>();
    private BufferedReader br;
    private BufferedWriter bw;

    public HiloChat(Socket cliente) throws IOException {
        this.cliente = cliente;
        //cada clienmte tiene su flujo de entrada y salida
        this.br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        //nombre del cliente que escribe desde consola
        this.nombreCliente = br.readLine();
        enviarMensajeUsuarios("Cliente " + nombreCliente + " ha entrado en chat.");
        //anado cliente a la lista
        clientes.add(this);
    }

    @Override
    public void run() {
        String mensajeCliente = "";

        while (cliente.isConnected()) {
            try {
                //que nos envia cliente
                mensajeCliente = br.readLine();
                enviarMensajeUsuarios(mensajeCliente);

            } catch (IOException e) {
                System.out.println(e);
                break;
            }
        }


    }

    private void enviarMensajeUsuarios(String mensaje) throws IOException {
        //escribe en chat que cliente ha entrado
        for (HiloChat c : clientes) {
            //envio mensaje a resto de usuarios del chat
            if(!c.nombreCliente.equals(this.nombreCliente)){
                c.bw.write(mensaje);
                c.bw.newLine();
                c.bw.flush();
            }
        }
    }
}