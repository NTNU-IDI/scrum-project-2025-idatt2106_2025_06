import {createRouter, createWebHistory} from 'vue-router'

import HomeView from '../views/HomeView.vue'
import AlertsView from '../views/AlertsView.vue'
import ProfileView from "@/views/ProfileView.vue";
import MapView from "@/views/MapView.vue";
import InventoryView from "@/views/InventoryView.vue";
import LogInView from "@/views/LogInView.vue";
import SignUpView from "@/views/SignUpView.vue";
import ScenarioInfoView from '@/views/ScenarioInfoView.vue'
import InfoView from "@/views/InfoView.vue";
import ForView from "@/views/BeforeView.vue";
import UnderView from "@/views/UnderView.vue";
import EtterView from "@/views/AfterView.vue";


const routes = [
  {path: '/', component: HomeView, name: 'home'},
  {path: '/alerts', component: AlertsView, name: 'alerts'},
  {path: '/inventory', component: InventoryView, name: 'inventory'},
  {path: '/map', component: MapView, name: 'map'},
  {path: '/profile', component: ProfileView, name: 'profile'},
  {path: '/login', component: LogInView, name: 'login'},
  {path: '/signup', component: SignUpView, name: 'signup'},
  {path: '/scenario', component: ScenarioInfoView, name: 'scenario'},
  {path: '/info', component: InfoView, name: 'info'},
  {path: '/for', component: ForView, name: 'for'},
  {path: '/under', component: UnderView, name: 'under'},
  {path: '/etter', component: EtterView, name: 'etter'},
]

const index = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default index
