package com.zzvc.mmps.daemon;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DaemonMessage {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	
	public static final String CMD_JOIN = "JOIN";
	public static final String CMD_QUIT = "QUIT";
	public static final String CMD_TERM = "TERM";
	public static final String CMD_APRV = "APRV";
	public static final String CMD_DENY = "DENY";
	public static final String CMD_INFO = "INFO";
	public static final String CMD_WARN = "WARN";
	public static final String CMD_ERROR = "ERROR";
	public static final String CMD_STUS = "STUS";
	public static final String CMD_CLOSE = "CLOSE";
	
	private final String command;
	private final long timestamp;
	private final String content;

	public DaemonMessage(Object message) {
		String strMessage = (String) message;
		this.command = strMessage.substring(0, 5).trim();
		long parsedTimestamp = 0;
		try {
			parsedTimestamp = format.parse(strMessage.substring(5, 25)).getTime();
		} catch (ParseException e) {
		}
		this.timestamp = parsedTimestamp;
		this.content = strMessage.substring(25);
	}

	public DaemonMessage(String command, String content) {
		this.command = command;
		this.timestamp = System.currentTimeMillis();
		this.content = content;
	}

	public String getCommand() {
		return command;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getContent() {
		return content;
	}
	
	public String createMessage() {
		return String.format("%-5s", command) + format.format(timestamp) + content;
	}
	
}
