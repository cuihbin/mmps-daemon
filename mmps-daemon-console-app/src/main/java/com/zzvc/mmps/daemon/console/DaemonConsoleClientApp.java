package com.zzvc.mmps.daemon.console;

import javax.annotation.Resource;

import com.zzvc.mmps.app.AppSupport;

public class DaemonConsoleClientApp extends AppSupport {
	@Resource
	private DaemonConsoleClient client;

	public DaemonConsoleClientApp() {
		super();
		pushBundle("DaemonConsoleResources");
	}

	@Override
	public void afterStartup() {
		client.init();
		client.connect();
	}

	@Override
	public void beforeShutdown() {
		client.quit();
	}
}
