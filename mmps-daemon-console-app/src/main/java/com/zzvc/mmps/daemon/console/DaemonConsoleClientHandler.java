package com.zzvc.mmps.daemon.console;

import javax.annotation.Resource;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.zzvc.mmps.daemon.DaemonMessage;

public class DaemonConsoleClientHandler extends IoHandlerAdapter {
	@Resource
	private DaemonConsoleClient client;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		client.broken();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		DaemonMessage consoleMessage = new DaemonMessage(message);
		String messageCommand = consoleMessage.getCommand();
		long messageTimestamp = consoleMessage.getTimestamp();
		String messageContent = consoleMessage.getContent();
		
		if (DaemonMessage.CMD_APRV.equals(messageCommand)) {
			client.approved(messageContent);
		} else if (DaemonMessage.CMD_DENY.equals(messageCommand)) {
			client.denied();
		} else if (DaemonMessage.CMD_INFO.equals(messageCommand)) {
			client.infoRemote(messageTimestamp, messageContent);
		} else if (DaemonMessage.CMD_WARN.equals(messageCommand)) {
			client.warnRemote(messageTimestamp, messageContent);
		} else if (DaemonMessage.CMD_ERROR.equals(messageCommand)) {
			client.errorRemote(messageTimestamp, messageContent);
		} else if (DaemonMessage.CMD_STUS.equals(messageCommand)) {
			client.statusRemote(messageContent);
		} else if (DaemonMessage.CMD_CLOSE.equals(messageCommand)) {
			client.closed();
		}
	}
}
