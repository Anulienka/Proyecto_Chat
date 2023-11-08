import java.io.*;
import java.net.Socket;

public class Cliente {

    public static Socket cliente;
    public static BufferedReader br;
    public static BufferedWriter bw;
    public static BufferedReader clienteEscribe;

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int puerto = 4444;

         cliente = new Socket(host, puerto);

        //flujo de entrada y salida
        br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        clienteEscribe = new BufferedReader(new InputStreamReader(System.in));

        enviarMensaje();
        recibirMensajes();
        
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

    public static void enviarMensaje() throws IOException {
        //primero cliente introduce su username
        System.out.println("Introduce tu username: ");
        String username = clienteEscribe.readLine();
        //enviamos nombre de usuario
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
                String mensajeEnviar = username + ": " + mensaje;
                //envio cadena al servidor
                bw.write(mensajeEnviar);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            cierraFlujos(cliente, bw, br);
        }
    }

    public static void recibirMensajes(){
        //HiloCliente recibir = new HiloCliente(cliente, br);
        //recibir.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensajeRecibido;
                while (cliente.isConnected()){
                    try {
                        mensajeRecibido = br.readLine();
                        System.out.println(mensajeRecibido);
                    } catch (IOException e) {
                        cierraFlujos(cliente, bw,br);
                    }
                }
            }
        }).start();
    }
}
