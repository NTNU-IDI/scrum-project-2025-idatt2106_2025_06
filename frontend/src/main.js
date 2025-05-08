import './assets/main.css'

import {createApp} from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from "@/router/router.js";
import { startSendingUserLocation, stopSendingUserPosition } from './service/locationService.js'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)


app.config.globalProperties.$startSendingUserLocation = () => startSendingUserLocation;
app.config.globalProperties.$stopSendingUserPosition = stopSendingUserPosition;


app.mount('#app')
