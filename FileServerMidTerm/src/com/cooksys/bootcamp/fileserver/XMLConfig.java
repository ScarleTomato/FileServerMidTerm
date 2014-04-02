package com.cooksys.bootcamp.fileserver;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XMLConfig {

	private String developerName;
	private int serverPort;
	private String sharedDirectoryPath;
	private String historyFilePath;

	public XMLConfig() {
	}

	public void printXML() {
		try {
			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(XMLConfig.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			m.marshal(this, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void saveConfigToFile(String filename) {
		try {
			File file = new File(filename);

			// clean out previous file
			if (file.exists())
				file.delete();

			// make a fresh file to write to
			file.createNewFile();

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext.newInstance(XMLConfig.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to File
			m.marshal(this, file);
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	}

	public void loadDefaults() {
		developerName = "Mike Yarbrough";
		serverPort = 12345;
		sharedDirectoryPath = "C:/Users/bondstoneXX/workspace/Cloud/store/";
		historyFilePath = "serverUploadHistory";
	}

	public FilenameFilter getFilenameFilter() {
		return null;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setSharedDirectoryPath(String sharedDirectoryPath) {
		this.sharedDirectoryPath = sharedDirectoryPath;
	}

	public String getSharedDirectoryPath() {
		return sharedDirectoryPath;
	}

	public void setHistoryFilePath(String historyFilePath) {
		this.historyFilePath = historyFilePath;
	}

	public String getHistoryFilePath() {
		return historyFilePath;
	}
}
