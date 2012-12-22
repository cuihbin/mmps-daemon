package com.zzvc.mmps.daemon.console;

import com.zzvc.mmps.console.ConsoleObserver;
import com.zzvc.mmps.daemon.DaemonMessage;

public class DaemonConsole implements ConsoleObserver {
	private DaemonConsoleServerHandler serverHandler;

	@Override
	public void trace(String text) {
	}

	@Override
	public void info(String text) {
		serverHandler.broadcastLog(new DaemonMessage(DaemonMessage.CMD_INFO, text));
	}

	@Override
	public void warn(String text) {
		serverHandler.broadcastLog(new DaemonMessage(DaemonMessage.CMD_WARN, text));
	}

	@Override
	public void error(String text) {
		serverHandler.broadcastLog(new DaemonMessage(DaemonMessage.CMD_ERROR, text));
	}

	@Override
	public void status(String text) {
		serverHandler.broadcastStatus(new DaemonMessage(DaemonMessage.CMD_STUS, text));
	}

	@Override
	public void setConsoleTitle(String consoleTitle) {
		serverHandler.setConsoleTitle(consoleTitle);
	}

	@Override
	public void init() {
		serverHandler = new DaemonConsoleServerHandler();
		serverHandler.init();
		serverHandler.bind();
	}

	@Override
	public void destroy() {
		serverHandler.close();
	}
}
