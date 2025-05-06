import { Client } from '@stomp/stompjs';

let client;

let isConnected = false;

// Define a function to set the connection state
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
      setConnected(true); // Set the connection state to true
      console.log('WebSocket connected');
      client.subscribe('/topic/public/newsAlerts', (message) => {
        const parsedMessage = JSON.parse(message.body);
        onMessageCallback(parsedMessage);
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
  if (isConnected && client) { // Now checks the isConnected variable
    client.publish({
      destination,
      body: JSON.stringify(body),
    });
  } else {
    console.error('WebSocket is not connected.');
  }
};
