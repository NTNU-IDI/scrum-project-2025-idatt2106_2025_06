import { Client } from '@stomp/stompjs';

let client;

let isConnected = false;

export const setConnected = (connected) => {
  isConnected = connected;
};

export const initializeWebSocket = (jwtToken, onMessageCallback) => {
  client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    connectHeaders: {
      Authorization: `Bearer ${jwtToken}`,
    },
    onConnect: () => {
      setConnected(true);
      console.log('WebSocket connected');
      client.subscribe('/topic/public/newsAlerts', (message) => {
        const parsedMessage = JSON.parse(message.body);
        console.log('[WebSocket EVENT message]', parsedMessage);
        onMessageCallback(parsedMessage, '/topic/public/newsAlerts');
      });
      client.subscribe('/topic/public/events', (message) => {
        const parsedMessage = JSON.parse(message.body);
        console.log('[WebSocket EVENT message]', parsedMessage);
        onMessageCallback(parsedMessage, '/topic/public/events');
      });
    },
    onStompError: (frame) => {
      console.error('Broker error:', frame.headers['message']);
      console.error('Details:', frame.body);
      console.error('Error:', frame.body);
    },
  });
  client.activate();
};

export const sendMessage = (destination, body, jwtToken) => {
  console.log('[WebSocket EVENT message]', destination, body);
  if (isConnected && client) {
    client.publish({
      destination,
      body: body,
      headers: {
        Authorization: `Bearer ${jwtToken}`
      }
    });
    console.log('Message sent:', body);
  } else {
    console.error('WebSocket is not connected.');
  }
};


