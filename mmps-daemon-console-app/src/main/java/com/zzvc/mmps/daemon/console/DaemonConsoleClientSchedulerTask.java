package com.zzvc.mmps.daemon.console;

import javax.annotation.Resource;

import com.zzvc.mmps.scheduler.task.SchedulerTask;
import com.zzvc.mmps.task.TaskSupport;

public class DaemonConsoleClientSchedulerTask extends TaskSupport implements SchedulerTask {
	@Resource
	private DaemonConsoleClientHandler handler;

	public DaemonConsoleClientSchedulerTask() {
		super();
		pushBundle("DaemonConsoleResources");
	}

	@Override
	public void onSchedule() {
		if (handler.needConnect()) {
			handler.connect();
		}
	}

}
