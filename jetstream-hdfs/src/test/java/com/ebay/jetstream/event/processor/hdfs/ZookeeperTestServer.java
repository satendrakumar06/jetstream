package com.ebay.jetstream.event.processor.hdfs;

import java.io.File;
import java.net.InetSocketAddress;

import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import com.google.common.io.Files;

public class ZookeeperTestServer {

	private final int tickTime;
	private final int clientPort;
	private final int numConnections;

	private ServerCnxnFactory standaloneServerFactory;
	private ZooKeeperServer server;

	private File locate;

	public ZookeeperTestServer(int tickTime, int clientPort, int numConnections) {
		this.tickTime = tickTime;
		this.clientPort = clientPort;
		this.numConnections = numConnections;
	}

	public void startup() throws Exception {
		locate = Files.createTempDir();
		File dir = new File(locate.getPath(), "zookeeper").getAbsoluteFile();

		server = new ZooKeeperServer(dir, dir, tickTime);
		standaloneServerFactory = new NIOServerCnxnFactory();
		standaloneServerFactory.configure(new InetSocketAddress(clientPort),
				numConnections);

		standaloneServerFactory.startup(server);
	}

	public void serverShutdown() {
		server.shutdown();
	}

	public void serverStartup() {
		server.startup();
	}

	public void shutdown() throws Exception {
		server.shutdown();
		standaloneServerFactory.shutdown();
		// FileUtils.deleteDirectory(locate);
	}

}
