/**
 * 
 */
package com.cooksys.bootcamp.fileserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Mike Yarbrough Apr 1, 2014 3:44:40 PM
 * 
 */
public class FileServer {
	public static final String NEW_CLIENT_TASK = "newClient";
	public static final String DEFAULT_CONFIG_FILENAME = "serverConfig.xml";

	private static final int DEFAULT_PORT = 12345;

	private String currentTask;
	private final ArrayList<Socket> clients = new ArrayList<Socket>();
	private XMLConfig config;

	public FileServer() {
		// get configuration
		loadConfig();
		config.printXML();

		// create a new Listener
		new Thread(new PortListener(this, DEFAULT_PORT)).start();

		//
		waitForTask();
	}

	private void loadConfig() {
		try {
			// create JAXB context and instantiate unmarshaller
			JAXBContext context = JAXBContext.newInstance(XMLConfig.class);
			Unmarshaller um = context.createUnmarshaller();
			// attempt to read the xml and create a config properly
			config = (XMLConfig) um.unmarshal(new FileReader(DEFAULT_CONFIG_FILENAME));

		} catch (JAXBException | FileNotFoundException e) {

			// if the config creation failed for any reason
			// make a new config
			config = new XMLConfig();
			// and set the default values
			config.loadDefaults();
			config.saveConfigToFile(DEFAULT_CONFIG_FILENAME);
		}
	}

	private synchronized void waitForTask() {
		try {
			// halt the main server until there's something to do
			wait();
			// System.out.println("Server notified with command: " + currentTask);
			parseCurrentTask();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void parseCurrentTask() {
		if (currentTask.equals(NEW_CLIENT_TASK)) {
			new Thread(new ClientHandler(clients.remove(0), this)).start();
		}
		// done with current task, wait until i get notified again
		waitForTask();
	}

	public void addClient(Socket clientSocket) {
		clients.add(clientSocket);
		notifyServer(NEW_CLIENT_TASK);
	}

	public void notifyServer(String command) {
		currentTask = command;
		notify();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FileServer();
	}

	public int getServerPort() {
		return DEFAULT_PORT;
	}

	public XMLConfig getConfig() {
		return config;
	}

}