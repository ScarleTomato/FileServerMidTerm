package com.cooksys.bootcamp.fileserver;

public class Commands {
	public static final byte END_OF_LIST = '\r';
	public static final byte LIST_FILES = 'L';
	public static final byte SEND_FILE = 'F';
	public static final byte END = '\n';
	public static final byte FILE_EXISTS = 'Y';
	public static final byte FILE_DOES_NOT_EXIST = 'N';
}
