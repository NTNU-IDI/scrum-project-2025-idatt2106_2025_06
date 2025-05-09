import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import AlertsView from '../views/AlertsView.vue'
import ProfileView from '@/views/ProfileView.vue'
import MapView from '@/views/MapView.vue'
import InventoryView from '@/views/InventoryView.vue'
import LogInView from '@/views/LogInView.vue'
import SignUpView from '@/views/SignUpView.vue'
import ScenarioInfoView from '@/views/ScenarioInfoView.vue'
import InfoView from '@/views/InfoView.vue'
import BeforeView from '@/views/BeforeView.vue'
import DuringView from '@/views/DuringView.vue'
import AfterView from '@/views/AfterView.vue'
import AdminView from '@/views/AdminView.vue'
import AdminMap from '@/views/AdminMapView.vue'
import EmailVerificationView from '@/views/EmailVerificationView.vue'
import { useSessionStore } from '@/stores/session.js'
import EventView from '@/views/EventView.vue'
import ScenarioDetailView from '@/views/ScenarioDetailView.vue'
import AddRemoveAdminView from '@/views/AddRemoveAdminView.vue'


const routes = [
  { path: '/', component: HomeView, name: 'home' },
  { path: '/alerts', component: AlertsView, name: 'alerts' },
  { path: '/alerts/:id', component: EventView, name: 'event' },
  { path: '/inventory', component: InventoryView, name: 'inventory', meta: { requiresAuth: true } },
  { path: '/map', component: MapView, name: 'map' },
  { path: '/profile', component: ProfileView, name: 'profile', meta: { requiresAuth: true } },
  { path: '/login', component: LogInView, name: 'login' },
  { path: '/signup', component: SignUpView, name: 'signup' },
  { path: '/scenario', component: ScenarioInfoView, name: 'scenario' },
  { path: '/info', component: InfoView, name: 'info' },
  { path: '/before', component: BeforeView, name: 'before' },
  { path: '/during', component: DuringView, name: 'during' },
  { path: '/after', component: AfterView, name: 'after' },
  { path: '/admin/addremove', component: AddRemoveAdminView, name: 'addremove', meta: { requiresAdminAccess: true }},
  {
    path: '/admin/map',
    component: AdminMap,
    name: 'admin-map',
    meta: { requiresAdminAccess: true },
  },
  { path: '/admin', component: AdminView, name: 'admin', meta: { requiresAdminAccess: true } },
  {
    path: '/email-verification/:token',
    component: EmailVerificationView,
    meta: { requiresAuth: false },
  },
  {path: '/scenario/:id',
    component: ScenarioDetailView,
    name: 'scenario-detail',
    props: true,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const session = useSessionStore()

  if ((to.path === '/login' || to.path === '/signup') && session.isAuthenticated) {
    return next('/')
  }

  if (to.meta.requiresAuth && !session.isAuthenticated) {
    return next('/login')
  }

  if (to.meta.requiresAdminAccess) {
    const role = session.user?.role
    if (!['ROLE_ADMIN', 'ROLE_MODERATOR'].includes(role)) {
      return next('/')
    }
  }

  next()
})

export default router
