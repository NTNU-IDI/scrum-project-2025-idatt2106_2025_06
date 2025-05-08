import './assets/main.css'

import {createApp} from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from "@/router/router.js";
import { useSessionStore } from '@/stores/session.js'
import { initializeWebSocket} from '@/lib/websocket.js'
import { useWebSocketStore } from '@/stores/websocket.js'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

const session = useSessionStore()
const webSocketStore = useWebSocketStore()
webSocketStore.initialize(session.token)

app.mount('#app')
