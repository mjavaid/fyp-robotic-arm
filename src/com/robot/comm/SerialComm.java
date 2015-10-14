package com.robot.comm;

import java.io.PrintWriter;

import com.fazecast.jSerialComm.SerialPort;

public class SerialComm {

	public static void main(String[] args) {
		SerialPort[] comPorts = SerialPort.getCommPorts();

		for (int i = 0; i < comPorts.length; i++) {
			System.out.println(comPorts[0].getSystemPortName());
		}

		SerialPort comPort = comPorts[0];

		try {
			comPort.openPort();
			
			System.out.println(comPort.getBaudRate() + " | "
					+ comPort.getNumDataBits() + " | "
					+ comPort.getNumStopBits() + " | "
					+ comPort.getDescriptivePortName());

			try {
				Thread.sleep(100);
			} catch (Exception e) {}

			// enter an infinite loop that sends text to the arduino
			PrintWriter output = new PrintWriter(comPort.getOutputStream());
			output.write(50);
//			output.print(50);
			output.flush();
			
			output.write(10);
//			output.print(10);
			output.flush();
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}

			output.close();

			System.out.println(comPort.bytesAvailable());
			byte[] readBuffer = new byte[comPort.bytesAvailable()];
			int numRead = comPort.readBytes(readBuffer, readBuffer.length);
			System.out.println("Read " + numRead + " bytes.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			comPort.closePort();
		}
	}

}
