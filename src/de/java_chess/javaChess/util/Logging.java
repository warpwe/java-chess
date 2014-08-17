package de.java_chess.javaChess.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class Logging {
	/**
	 * The Logfile
	 */
	File file;

	/**
	 * The filename for the Logfile
	 */
	private static final String LOG_FILENAME = "JCEngine.log";
	
	private static Logging loggingInstance = null;

	/**
	 * The vector for buffering the existing lines from the Logfile
	 */
	Vector<String> vector;

	private Logging() {
		createLogFile();
	}
	
	public static Logging getLogging() {
		if (!(loggingInstance == null)) {
			return loggingInstance;
		} else {
			return loggingInstance = new Logging();
		}
	}

	/**
	 * Initially create a logfile
	 */
	public void createLogFile() {
	//	System.out.println("Wird sind in createlogfile");
		File homedir = new File(System.getProperty("user.home"));
		file = new File(homedir, LOG_FILENAME);
		try {
			FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter
					.println("## Java-Chess Engine Logfile, do not edit! ##");
			printWriter.println("## For internal use only! ##");
			printWriter.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Adds a log entry into the JavaChess logfile
	 * 
	 * @param text
	 *            The text to append
	 */
	public void addLogEntry(String text) {
	//	System.out.println("Wird sind in addlogentry");
		if (file != null) {
			try {
				// First read the existing lines into vBuffer
				BufferedReader bfrIniFile = new BufferedReader(
						new FileReader(file));
				String strOrgLine = bfrIniFile.readLine();
				vector = new Vector<String>();
				String strLine;

				while (strOrgLine != null) {
					strLine = strOrgLine.trim();
					this.vector.addElement(strLine);
					strOrgLine = bfrIniFile.readLine();
				}

				// now create file new but with same filename
				FileWriter fileWriter = new FileWriter(file);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				// insert former existing text:
				String strWrite[] = new String[vector.size()];
				vector.copyInto(strWrite);

				for (int i = 0; i < vector.size(); i++) {
					printWriter.println(strWrite[i]);
				}

				// add the new text now:
				printWriter.println(text);
				printWriter.close();
				bfrIniFile.close();
			} catch (Exception e) {
			}
		}
	}
}
