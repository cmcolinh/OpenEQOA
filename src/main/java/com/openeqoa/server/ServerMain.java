package com.openeqoa.server;

import static com.openeqoa.server.util.Log.println;

import java.util.HashMap;
import java.util.Map;

import com.openeqoa.server.game.GameLoop;
import com.openeqoa.server.game.state.GameState;
import com.openeqoa.server.network.client.TCPClientManager;
import com.openeqoa.server.network.client.UDPClientManager;
import com.openeqoa.server.network.tcp.TCPConnection;
import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.UDPConnection;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ServerMain {

    private static ServerMain instance = null;

    private final short loginServerId = (short) 0x1061; // "login"
    private final short serverId = (short) 0x0AE0; // "EQOA", if we need an architecture with different endpoints, this
                                                   // will need to be loadable from configuration
    private final Map<String, Short> otherServers = new HashMap<>(); // one server setup at present
    private final GameState gameState = new GameState();
    private final TCPClientManager clientManager = new TCPClientManager();
    private final UDPClientManager udpClients = new UDPClientManager.SingleServerImplementation();
    private final TCPConnection tcpConnection = new TCPConnection();
    private final UDPConnection udpConnection = new UDPConnection();
    private final GameLoop gameLoop = new GameLoop();
    private final CalculateCRC calculateCRC = CalculateCRC.getInstance();

    /**
     * Used to handle closing down all threads associated with the server. Volatile
     * allows the variable to exist between threads.
     */
    @Setter
    private volatile boolean running = true;

    public static void main(String[] args) {
        ServerMain.getInstance().startServer();
    }

    /**
     * Gets the main instance of this class.
     *
     * @return A singleton instance of this class.
     */
    public static ServerMain getInstance() {
        if (instance == null)
            instance = new ServerMain();
        return instance;
    }

    /**
     * Initializes the game server.
     */
    private void startServer() {
        println(getClass(), "Initializing network...");
        tcpConnection.openServer();
        udpConnection.openServer();
        gameLoop.start();
    }
}
