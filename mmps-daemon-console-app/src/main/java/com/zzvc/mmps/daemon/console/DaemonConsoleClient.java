package com.zzvc.mmps.daemon.console;

import com.zzvc.mmps.console.ConsoleMessageSupport;
import com.zzvc.mmps.gui.util.PatternUtil;

public class DaemonConsoleClient extends ConsoleMessageSupport {
    private static final String LOCALHOST_NAME="localhost";

    private String host;
    private int port;
    
    private PatternUtil patternUtil;

	public DaemonConsoleClient() {
		super();
		pushBundle("DaemonConsoleResources");
	}

	public void init() {
		try {
			patternUtil = new PatternUtil(findText("console.daemon.log.message.pattern"));
		} catch (Exception e) {
			patternUtil = new PatternUtil();
		}
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public void connecting() {
		infoLocal("console.daemon.connect", host);
		statusLocal("console.daemon.connect", host);
	}
	
	public void connectFailed() {
		warnLocal("console.daemon.error.connectfailed", host, port);
		statusLocal("console.daemon.error.connectfailed", host, port);
	}
	
	public void approved(String daemonAppTitle) {
		setConsoleTitle(findText("console.daemon.log.format", host, daemonAppTitle));
		infoLocal("console.daemon.join.approved", host, daemonAppTitle);
	}

	public void denied() {
		errorLocal("console.daemon.join.denied", host);
		statusLocal("console.daemon.join.denied", host);
	}
	
	public void broken() {
		warnLocal("console.daemon.broken", host);
		statusLocal("console.daemon.broken", host);
	}
	
	public void closed() {
		warnLocal("console.daemon.closed", host);
		statusLocal("console.daemon.closed", host);
	}
	
	public void infoRemote(long timestamp, String text) {
		infoMessage("console.daemon.log.format", host, patternUtil.format(timestamp, "", text));
	}
	
	public void warnRemote(long timestamp, String text) {
		warnMessage("console.daemon.log.format", host, patternUtil.format(timestamp, "", text));
	}
	
	public void errorRemote(long timestamp, String text) {
		errorMessage("console.daemon.log.format", host, patternUtil.format(timestamp, "", text));
	}
	
	public void statusRemote(String text) {
		statusMessage("console.daemon.log.format", host, text);
	}
	
	public void infoLocal(String key, Object... args) {
		infoMessage("console.daemon.log.format", LOCALHOST_NAME, findText(key, args));
	}
	
	public void warnLocal(String key, Object... args) {
		warnMessage("console.daemon.log.format", LOCALHOST_NAME, findText(key, args));
	}
	
	public void errorLocal(String key, Object... args) {
		errorMessage("console.daemon.log.format", LOCALHOST_NAME, findText(key, args));
	}
	
	public void statusLocal(String key, Object... args) {
		statusMessage("console.daemon.log.format", LOCALHOST_NAME, findText(key, args));
	}

	@Override
	protected String getConsolePrefix() {
		return "";
	}
}
