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
        onMessageCallback(parsedMessage, '/topic/public/newsAlerts');
      });
      client.subscribe('/topic/public/events', (message) => {
        const parsedMessage = JSON.parse(message.body);
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

export const sendMessage = (destination, body) => {
  if (isConnected && client) {
    client.publish({
      destination,
      body: JSON.stringify(body),
    });
  } else {
    console.error('WebSocket is not connected.');
  }
};
