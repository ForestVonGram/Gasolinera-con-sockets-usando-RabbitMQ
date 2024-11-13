package org.forest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ClienteTanqueo {
    private static final String QUEUE_NAME = "cola_tanqueo";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("Forest");
        factory.setPassword("CAsa1573");

        try (Connection connection = factory.newConnection();
            Scanner scanner = new Scanner(System.in)) {

            Channel channel = connection.createChannel();
            try {

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                System.out.println("Ingresa el número de identificación del cliente:");
                String numeroId = scanner.nextLine();

                System.out.println("Ingrese el tipo de vehículo (automovil, motocicleta, camioneta):");
                String tipoVehiculo = scanner.nextLine();

                System.out.println("Ingrese la cantidad de gasolina cargada (en galones):");
                String cantidadGasolina = scanner.nextLine();

                System.out.println("Ingrese la fecha y hora del tanqueo (formato: yyyy-MM-dd HH:mm):");
                String fechaHora = scanner.nextLine();

                String mensaje = numeroId + "|" + tipoVehiculo + "|" + cantidadGasolina + "|" + fechaHora;
                channel.basicPublish("", QUEUE_NAME, null, mensaje.getBytes());
                System.out.println("Datos enviados a RabbitMQ: " + mensaje);
            } finally {
                channel.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
