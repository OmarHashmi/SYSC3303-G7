package project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ElevatorCommunication extends Thread {

    private int SCHEDULER_PORT_NUMBER = 219; // elevator sends and receives packets from scheduler
    private DatagramSocket receive, sendSocket;

    private  Elevator elevator;


    public ElevatorCommunication( ) {




        try {
            receive = new DatagramSocket(68); // wait to receive on porty 68
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public void receive(){
        DatagramPacket receivePacket;
        byte[] message = new byte[25];
        receivePacket = new DatagramPacket(message, message.length);
        try {
            System.out.println("Waiting... I am elevator communication!\n"); // we are waiting for something from the Scheduler
            receive.receive(receivePacket);
        } catch (IOException e) {
            System.out.print("IO Exception: likely:");
            System.out.println("Receive Socket Timed Out.\n" + e);
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Elevator: Packet received");


        //                          0               1               2               3
        // temp will contain [elevator number, direction/state, current floor, destination floor]

        byte[] temp = receivePacket.getData();
        int initialFloor, destinationFloor;
        initialFloor = temp[2];
        destinationFloor = temp[3];

        elevator.addRequest(temp[1]);





    }

    public void sendRequest(int elevatorNumber, int direction, int floorNumber) {

        byte data[] = new byte[4];
        data[0] = (byte) elevatorNumber;
        data[1] = (byte) direction;
        data[2] = (byte) floorNumber;

        try {
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(),
                    SCHEDULER_PORT_NUMBER);
            sendSocket.send(sendPacket);
        } catch (SocketException se) { // Can't create the socket.
            sendSocket.close();
            se.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        Elevator elevator = new Elevator(1,69,this);
        this.elevator = elevator;
        Thread elevatorThread = elevator;
        elevatorThread.start();
        while(true) {
            this.receive();
        }
    }


}
