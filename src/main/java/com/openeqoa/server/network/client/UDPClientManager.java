package com.openeqoa.server.network.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Map: SessionId (2byte short) -> UDPClientHandler
 * <p>
 * It is possible that if we use multiple server endpoints, that we will need to
 * keep a centralized list of client endpoints using something like Redis cache
 */
public interface UDPClientManager {

	/** Registers a client to the client manager */
	void addClient(short clientId, UDPClientHandler clientHandler);

	/** Deregisters a client from the client manager */
	void removeClient(short clientId);

	/** Query whether there is a handler for a given clientId */
	boolean isRegistered(short clientId);

	/** Acquire the handler associated with a given id */
	UDPClientHandler getClient(short clientId);

	/** Get the number of active connections */
	int clientsOnline();

	public static class SingleServerImplementation implements UDPClientManager {
		private final Map<Short, UDPClientHandler> udpClientRegistry = new HashMap<>();

		@Override
		public void addClient(short clientId, UDPClientHandler clientHandler) {
			synchronized (udpClientRegistry) {
				if (!udpClientRegistry.containsKey(clientId)) {
					udpClientRegistry.put(clientId, clientHandler);
				}
			}
		}

		@Override
		public void removeClient(short clientId) {
			synchronized (udpClientRegistry) {
				udpClientRegistry.remove(clientId);
			}
		}

		@Override
		public boolean isRegistered(short clientId) {
			return udpClientRegistry.containsKey(clientId);
		}

		@Override
		public UDPClientHandler getClient(short clientId) {
			synchronized (udpClientRegistry) {
				return udpClientRegistry.get(clientId);
			}
		}

		@Override
		public int clientsOnline() {
			return udpClientRegistry.size();
		}
	}
}