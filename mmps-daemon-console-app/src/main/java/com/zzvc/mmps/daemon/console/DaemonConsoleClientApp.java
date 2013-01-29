package com.zzvc.mmps.daemon.console;

import javax.annotation.Resource;

import com.zzvc.mmps.app.AppSupport;

public class DaemonConsoleClientApp extends AppSupport {
	@Resource
	private DaemonConsoleClientHandler handler;

	public DaemonConsoleClientApp() {
		super();
		pushBundle("DaemonConsoleResources");
	}

	@Override
	public void afterStartup() {
		handler.init();
		handler.connect();
	}

	@Override
	public void beforeShutdown() {
		handler.quit();
	}
}
