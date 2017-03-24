package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.entity.user.DLUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.ReadyJSON;

public class Ready extends AbstractHandler {
    public Ready(DiscSocket socket) {
        super(socket);
    }

    @Override
    public void handle(SocketPacket packet) {
        Gson gson = new Gson();
        String d = gson.toJson(packet.d);
        ReadyJSON readyJSON = gson.fromJson(d, ReadyJSON.class);

        // set session id first just incase some screws up
        this.socket.sessionID = readyJSON.session_id;

        // setup the Loaders user object
        try {
            loader.user = new DLUser(this.loader.addUser(readyJSON.user));
            if (loader.user.bot == true) {
                this.socket.loader.token = "Bot " + this.socket.loader.token;
            }

            // load the guilds
            for (GuildJSON guild : readyJSON.guilds) {
                this.socket.loader.addGuild(guild);
            }

            // load the private channels
            for (ChannelJSON data : readyJSON.private_channels) {
                this.socket.loader.addChannel(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if the loader is ready to rock & roll
        this.socket.loader.checkReady();
    }
}