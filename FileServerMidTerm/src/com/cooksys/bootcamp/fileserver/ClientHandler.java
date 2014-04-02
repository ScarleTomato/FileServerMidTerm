/**
 * 
 */
package com.cooksys.bootcamp.fileserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Mike Yarbrough Apr 1, 2014 3:56:27 PM
 * 
 */
public class ClientHandler implements Runnable {
	public static final int MAX_BUFFER_SIZE = 1024 * 8;

	private final Socket clientSocket;
	private final XMLConfig config;

	private InputStream in = null;
	private OutputStream out = null;
	private DataOutputStream dOut = null;
	private FileInputStream fileInput = null;

	public ClientHandler(Socket socket, FileServer server) {
		this.clientSocket = socket;
		this.config = server.getConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			// initialize the input and output streams for the client socket
			initializeStreams();

			// send developer name (handshake)
			sendGreetings();

			// get client request
			int response;
			while ((response = in.read()) != -1) {

				// and figure out what they want
				parseResponse(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStreams();
		}

	}

	private void parseResponse(int response) throws IOException {
		if (response == Commands.LIST_FILES) {
			listFiles();
		} else if (response == Commands.SEND_FILE) {
			sendFile();
		}
	}

	/**
	 * Sends a file determined by the name gotten from the input stream this method expects that the next set of data coming from the input stream is string
	 * containing the filename and terminated by the Commands.END byte
	 * 
	 * @throws IOException
	 */
	private void sendFile() throws IOException {
		String filename = "";
		int character;

		// read bytes until END is reached
		while ((character = in.read()) != Commands.END) {

			// build filename from stream
			filename += (char) character;
		}

		// create a file handler for the requested file
		File file = new File(config.getSharedDirectoryPath() + filename);

		// check if the file exists
		if (file.isFile()) {

			// tell client file exists
			out.write(Commands.FILE_EXISTS);

			// send file length as a long integer through the data stream
			dOut.writeLong(file.length());

			// get inputstream from file
			fileInput = new FileInputStream(file);

			// create buffer
			byte[] bytes = new byte[MAX_BUFFER_SIZE];
			int totalBytes;

			// read bytes into buffer
			while ((totalBytes = fileInput.read(bytes)) != -1) {

				// send to client
				out.write(bytes, 0, totalBytes);
			}

			// close fileinput stream
			fileInput.close();

			// send END to terminate file
			out.write(Commands.END);
		} else {
			// file does not exist
			out.write(Commands.FILE_DOES_NOT_EXIST);
		}
	}

	private void sendGreetings() throws IOException {
		// send developer name terminated by END
		out.write(config.getDeveloperName().getBytes());
		out.write(Commands.END);
	}

	private void listFiles() throws IOException {

		// get the directory from the server
		File dir = new File(config.getSharedDirectoryPath());

		// get a list of filenames from this directory
		for (String name : dir.list(config.getFilenameFilter())) {
			// send the filename and the string terminator
			out.write(name.getBytes());
			out.write(Commands.END);
		}

		// found all the files, send END_OF_LIST
		out.write(Commands.END_OF_LIST);
	}

	private void initializeStreams() throws IOException {
		in = clientSocket.getInputStream();
		out = clientSocket.getOutputStream();
		dOut = new DataOutputStream(out);
	}

	private void closeStreams() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (dOut != null)
				dOut.close();
			if (fileInput != null)
				fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}