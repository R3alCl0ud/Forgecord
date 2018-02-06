package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.Gateway;

/**
 * @author Perry Berman
 */
public class Resumed extends AbstractHandler {

	public Resumed(Gateway socket) {
		super(socket);
	}

	public void handle() {
		loader.socket.setRetries(0);
		loader.socket.keepAlive();
		loader.socket.lastHeartbeatAck = true; // this should fix the 1007 disconnection loop
		loader.checkReady();
	}

}
