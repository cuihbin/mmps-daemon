package com.zzvc.mmps.daemon.shutdown;

import static com.zzvc.mmps.daemon.DaemonConstants.DEFAULT_GROUP;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zzvc.mmps.daemon.DaemonMessage;

public class DaemonShutdownHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(DaemonShutdownHandler.class);
	
	private static final long DAEMON_SHUTDOWN_TIMEOUT = 30000; 
	
	private NioSocketConnector connector;
	
	private String group = DEFAULT_GROUP;
	private String host = "localhost";
	private int port;
	
	public DaemonShutdownHandler() {
		ResourceBundle bundle = ResourceBundle.getBundle("daemon");
		try {
			group = bundle.getString("console.daemon.group");
		} catch (Exception e) {
		}
		try {
			port = Integer.parseInt(bundle.getString("console.daemon.port"));
		} catch (Exception e) {
			logger.error("Cannot read daemon listener port. Daemon will not be terminated", e);
			return;
		}

		connector = new NioSocketConnector();
		connector.setHandler(this);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

		ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
        future.awaitUninterruptibly();
        if (!future.isConnected()) {
        	logger.error("Cannot connect to daemon listener. Daemon will not be terminated.");
            connector.dispose();
            
            return;
        }
        
        IoSession session = future.getSession();
        session.write(new DaemonMessage(DaemonMessage.CMD_TERM, group).createMessage());
        
		synchronized (this) {
			try {
				this.wait(DAEMON_SHUTDOWN_TIMEOUT);
			} catch (InterruptedException e) {
			}
		}
        
        session.close(true);
        connector.dispose();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		DaemonMessage consoleMessage = new DaemonMessage(message);
		String messageCommand = consoleMessage.getCommand();
		
		if (DaemonMessage.CMD_CLOSE.equals(messageCommand)) {
	        synchronized (this) {
	        	this.notifyAll();
	        }
		}
	}
}
