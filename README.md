# Proyecto_Chat
Explicación del método elegido para la transmisión de datos

- Método elegido para hacer proyecto Chat es: Sockets TCP

He elegido este método porque es orientado a conexión, requiere una conexión establecida entre el cliente y el servidor antes de la transferencia de datos, garantizan la entrega de datos en el orden correcto y sin pérdida de información que en chat nos interesa.
Para este proyecto a parte de clase Cliente y Servidor, se necesitaban dos Hilos diferentes para recibir y enviar mensajes. HiloServidor que está iniciado desde servidor recibe mensajes que cliente escribe y los envia a resto de clientes que estan conectados. HiloCliente recibe mensajes que envían otros clientes y  lo muestra en pantalla.

- Método  desechado: UDP

UDP es un protocolo no orientado a la conexión que es más eficiente en términos de velocidad, pero no garantiza la entrega de datos lo que significa que algunos paquetes podrían perderse en el camino sin notificación. Tampoco garantiza el orden de paquetes. Para una aplicación de chat en tiempo real, donde el orden de los mensajes son esenciales, UDP no es una opción adecuada.

Aunque MulticastSocket (que utiliza UDP) también podría utilizarse para este proyecto porque permite enviar un solo mensaje a múltiples clientes que están en el mismo grupo.
