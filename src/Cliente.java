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

        //para que cliente pueda recibir mensajes de otros clientes, hay que crear hilo que recibe mensajes
        HiloCliente recibir = new HiloCliente(cliente, br);
        recibir.start();


        //primero cliente introduce su username
        System.out.println("Introduce tu username: ");
        String username = clienteEscribe.readLine();
        //enviamos nombre de usuario (eso recoge hilo servisor, que lee mensajes de clientes y lo utiliza para crear hiloServidor)
        bw.write(username);
        bw.newLine();
        bw.flush();

        //luego escribe mensaje
        System.out.println("Puedes empezar a chatear :)");
        String mensaje = " ";

        try {
            //hasta que cliente no sale del chat puede enviar mensajes
            while (cliente.isConnected()){
                //System.out.println("Introduce mensaje o fin para terminar:");
                mensaje =clienteEscribe.readLine();
                String mensajeEnviar = username.toUpperCase() + ": " + mensaje;
                System.out.println(mensajeEnviar);
                //envio cadena al servidor
                bw.write(mensajeEnviar);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            //cerrar streams y conexiones
            cierraFlujos(cliente, bw, br);
        }
        
    }

    private static void cierraFlujos(Socket cliente, BufferedWriter bw, BufferedReader br)  {
        //cierro flujos
        try {
            if(br != null){
                br.close();
            }
            if(bw != null){
                bw.close();
            }
            if(cliente != null){
                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
