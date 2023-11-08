import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class HiloCliente extends Thread{

    private Socket cliente;
    private BufferedReader br;

    public HiloCliente(Socket cliente, BufferedReader br) {
        this.cliente = cliente;
        this.br = br;
    }

    @Override
    public void run() {
        String mensajeRecibido;
        //hasta que el cliente no se desconecte cerrando ventana del chat recibe mensajes de otros clientes
        while (cliente.isConnected()){
            try {
                //mensaje de otros clientes
                mensajeRecibido = br.readLine();
                //imprime mensaje por pantalla
                System.out.println(mensajeRecibido);
            } catch (IOException e) {
                //cerrar streams y conexiones
                cierraFlujos(cliente, br);
            }
        }
    }

    private void cierraFlujos(Socket cliente, BufferedReader br) {
        try {
            if(br != null){
                br.close();
            }
            if(cliente != null){
                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
