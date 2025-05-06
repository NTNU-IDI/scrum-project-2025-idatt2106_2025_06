<template>
  <div>
    <h1>WebSocket Test</h1>
    <button @click="connectWebSocket">Connect WebSocket</button>
    <div>
      <input v-model="inputMessage" placeholder="Type a message" />
      <button @click="sendMessage">Send Message</button>
    </div>
    <ul>
      <li v-for="(message, index) in messages" :key="index">{{ message }}</li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Client } from '@stomp/stompjs';

const messages = ref([]);
const inputMessage = ref('');
const jwtToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiODA3NWM0OS00OWUxLTRlMTktYjEwZS1iMTEzOTJmMGZhOGYiLCJlbWFpbCI6Im1hc2lkYXNtZEBtYWlobC5jb20iLCJyb2xlIjpbIlJPTEVfVVNFUiJdLCJwZXJtaXNzaW9ucyI6WyJMT0dJTl9VU0VSIiwiQ1JFQVRFX1NUT1JBR0UiXSwiaWF0IjoxNzQ2NDM1NDA4LCJleHAiOjE3NDcwMzU0MDh9.r-BMSOCR1wFukfCJ6xOPt58Mc5oV0XIQs0dKyD9yF0A'; // Replace with your actual token

const client = new Client({
  brokerURL: 'ws://localhost:8080/ws',
  connectHeaders: {
    Authorization: `Bearer ${jwtToken}`
  },
  onConnect: () => {
    client.subscribe('/topic/notifications', message => {
      messages.value.push(`Received: ${message.body}`);
    });
  },
  onStompError: (frame) => {
    console.error('Broker reported error:', frame.headers['message']);
    console.error('Additional details:', frame.body);
  }
});

client.activate();

const connectWebSocket = () => {
  messages.value.push('STOMP client activated.');
};

const sendMessage = () => {
  if (client.connected) {
    client.publish({
      destination: '/app/notification',
      body: inputMessage.value
    });
    messages.value.push(`Sent: ${inputMessage.value}`);
    inputMessage.value = '';
  } else {
    messages.value.push('STOMP client is not connected.');
  }
};
</script>
