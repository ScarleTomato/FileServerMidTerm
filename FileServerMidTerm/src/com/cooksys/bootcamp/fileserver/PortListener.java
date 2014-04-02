/**
 * 
 */
package com.cooksys.bootcamp.fileserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Mike Yarbrough Apr 1, 2014 9:17:31 AM
 * 
 */
public class PortListener implements Runnable {

	private final FileServer server;
	private final int portNumber;
	private final boolean listening = true;

	public PortListener(FileServer server, int portNumber) {
		System.out.println("creating listener on port: " + portNumber);
		this.server = server;
		this.portNumber = portNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		System.out.println("starting listener");
		try {
			// start litening on the server port.
			serverSocket = new ServerSocket(portNumber);

			while (listening) {
				// Wait for a client to connect
				// create a new client socket and add it to the server's list of clients
				server.addClient(serverSocket.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}