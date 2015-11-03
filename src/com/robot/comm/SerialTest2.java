package com.robot.comm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
public class SerialTest2 {
	
	public SerialTest2() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);

				(new Thread(new SerialWriter(out))).start();

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			buffer = new byte[1024];
			try {
				int len = 0;
				
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
					
					System.out.print(data + "|" + ((byte) data) + " ");
				}
				
				System.out.println("\n\n-----------------------------------");
				
				StringBuilder sb = new StringBuilder();
			    for (byte b : buffer) {
			        sb.append(String.format("%02X ", b));
			    }
			    System.out.println(sb.toString());
				
				System.out.println("BUFFER: " + buffer);
				System.out.println("ARRAYS: " + Arrays.toString(buffer));
				System.out.println("DEFAULT: " + new String(buffer, 0, len));
				System.out.println("US-ASCII: " + new String(buffer, StandardCharsets.US_ASCII));
				System.out.println("US-ASCII (2): " + new String(buffer, "US-ASCII"));
				System.out.println("UTF-8: " + new String(buffer, StandardCharsets.UTF_8));
				System.out.println("UTF_8 (2): " + new String(buffer, "UTF-8"));
				System.out.println("Windows-1252: " + new String(buffer, "Windows-1252"));
				System.out.println("ISO-8859-1: " + new String(buffer, StandardCharsets.ISO_8859_1));
				System.out.println("ISO-8859-1 (2): " + new String(buffer, "ISO-8859-1"));
				System.out.println("UTF_16: " + new String(buffer, StandardCharsets.UTF_16));
				System.out.println("UTF_16BE: " + new String(buffer, StandardCharsets.UTF_16BE));
				System.out.println("UTF_16LE: " + new String(buffer, StandardCharsets.UTF_16LE));
				System.out.println("-----------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					System.out.println(c);
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void main(String[] args) {
		try {
			(new SerialTest2()).connect("COM4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
