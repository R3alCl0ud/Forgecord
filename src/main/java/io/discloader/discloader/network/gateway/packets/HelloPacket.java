package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.Hello;

public class HelloPacket extends DiscPacket {

	public HelloPacket(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Hello hello = this.socket.gson.fromJson(this.socket.gson.toJson(packet.d), Hello.class);
		this.socket.keepAlive(hello.heartbeat_interval);
	}

}
