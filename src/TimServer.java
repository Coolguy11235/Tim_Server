import java.net.*;

public class TimServer {
    public static void main(String args[])
            throws Exception {

// Default port number we are going to use
        int portnumber = 8080;
        if (args.length >= 1) {
            portnumber = Integer.parseInt(args[0]);
        }

// Create a MulticastSocket
        MulticastSocket serverMulticastSocket = new MulticastSocket(portnumber);
        System.out.println("Multicast Socket is created at port " + portnumber);

// Determine the IP address of a host, given the host name
        InetAddress group = InetAddress.getByName("225.4.5.6");

// getByName- returns IP address of given host
        serverMulticastSocket.joinGroup(group);
        System.out.println("joinGroup method is called...");
        boolean infinite = true;

// Continually receives data and prints them
        while (infinite) {
            byte buf[] = new byte[1024];
            DatagramPacket data = new DatagramPacket(buf, buf.length);
            serverMulticastSocket.receive(data);
            String msg = new String(data.getData(), 0, data.getLength()).trim();
            System.out.println("Message received from client = " + msg);

            // Calculation
            String result = calculateExpression(msg);
            System.out.println(msg + " = " + result);

            // Sends the results
            byte[] sendData = result.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, data.getAddress(), data.getPort());
            serverMulticastSocket.send(sendPacket);
        }
        serverMulticastSocket.leaveGroup(group);
        serverMulticastSocket.close();
    }

    private static String calculateExpression(String expression) {
        try {
            String[] parts = expression.split(" ");
            if (parts.length != 3) {
                return "ERROR: Invalid Expression";
            }

            int num1 = Integer.parseInt(parts[0]);
            String operator = parts[1];
            int num2 = Integer.parseInt(parts[2]);
            int result;

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num1 / num2;
                    break;
                default:
                    return "ERROR: Unknown Operator";
            }

            return String.valueOf(result);

        } catch (NumberFormatException e) {
            return "ERROR: Invalid Number!";
        }
    }
}