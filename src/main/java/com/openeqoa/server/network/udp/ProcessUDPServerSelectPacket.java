package com.openeqoa.server.network.udp;

/**
 * This class will process the bytes of a packet seeking to establish a new
 * connection, parsing the packe to extract the client messages and the message
 * handler. It will register the IP address and the username in the UDP
 * connection registry and send a response establishing the connection
 * 
 * @author colin
 *
 */
/*
 * public class ProcessUDPServerSelectPacket implements Runnable { private final
 * short loginServerId = ServerMain.getInstance().getLoginServerId();
 * 
 * private final byte[] packetBytes;
 * 
 * private final InetAddress ipAddress;
 * 
 * public static Map<Byte, MessageHandler> createMessageFromRoutingCode =
 * Map.ofEntries(Map.entry((byte) 0x0E, new
 * SessionInitiatorRoutineMessageHandler(ServerMain.getInstance().
 * getUdpConnection(), ServerMain.getInstance().getUdpClients(),
 * CalculateCRC.getInstance(), ServerMain.getInstance().getLoginServerId())));
 * 
 * public void run() { List<ClientMessage> messages = findMessages(packetBytes,
 * ipAddress); }
 * 
 * private MessageHandler getHandler(byte[] packetBytes) { return
 * packetBytes.length > 5 ? createMessageFromRoutingCode.get((byte)
 * (packetBytes[5] >> 4 & 0x0F)) : null; }
 * 
 * private List<ClientMessage> findMessages(byte[] packetBytes, InetAddress
 * ipAddress) { // TODO lots of lazy hard coding here. I'm sure that not all
 * packets are // following these rules.
 * 
 * int index = findFirstHeaderBreakIndex();
 * 
 * println(getClass(), "0x" +
 * Integer.toHexString(packetBytes[index]).substring(6, 8));
 * 
 * return messageStreamFrom(packetBytes, ipAddress, index).filter(message ->
 * message != null) .collect(Collectors.collectingAndThen(Collectors.toList(),
 * Collections::unmodifiableList)); }
 * 
 * private int findFirstHeaderBreakIndex() { return 14; }
 * 
 * private Stream<ClientMessage> messageStreamFrom(byte[] packetBytes,
 * InetAddress ipAddress, int startIndex) { Iterable<ClientMessage>
 * messageIterable = () -> new PacketByteMessageIterator(packetBytes, ipAddress,
 * startIndex); return StreamSupport.stream(messageIterable.spliterator(),
 * false); } }
 */
