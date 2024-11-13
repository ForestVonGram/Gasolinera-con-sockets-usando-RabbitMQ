package org.forest;

import com.rabbitmq.client.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Servidor {

    private static final String QUEUE_NAME = "cola_tanqueo";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("Forest");
        factory.setPassword("CAsa1573");

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();

            try {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                System.out.println("Servidor RabbitMQ iniciado, esperando mensajes...");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String mensaje = new String(body, "UTF-8");
                        String[] datos = mensaje.split("\\|");

                        String numeroId = datos[0];
                        String tipoVehiculo = datos[1];
                        String cantidadGasolina = datos[2];
                        String fechaHora = datos[3];

                        System.out.println("Datos recibidos:");
                        System.out.println("ID Cliente: " + numeroId);
                        System.out.println("Tipo de Vehículo: " + tipoVehiculo);
                        System.out.println("Cantidad de Gasolina: " + cantidadGasolina + " galones");
                        System.out.println("Fecha y Hora: " + fechaHora);
                    }
                };

                channel.basicConsume(QUEUE_NAME, true, consumer);

                System.out.println("Esperando mensajes...");
                Thread.sleep(Long.MAX_VALUE);
            } finally {
                channel.close();
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
