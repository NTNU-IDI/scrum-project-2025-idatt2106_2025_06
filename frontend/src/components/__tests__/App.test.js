import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import AppView from '@/App.vue'
import { createTestingPinia } from '@pinia/testing'
import { useSessionStore } from '@/stores/session'
import Navbar from '@/components/NavBar.vue'
import NavBarAdmin from '@/components/NavBarAdmin.vue'
import { createRouter, createWebHistory } from 'vue-router'

describe('AppView', () => {
  let wrapper
  let session
  let router

  beforeEach(() => {
    // Create a mock router instance
    router = createRouter({
      history: createWebHistory(),
      routes: [] // You can add actual routes if needed, but it's not required for this test
    })

    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        session: {
          user: null,
        },
      },
    })

    wrapper = mount(AppView, {
      global: {
        plugins: [pinia, router], // Inject the router here
        stubs: {
          RouterView: true,
          RouterLink: true,
          Footer: true,
        },
      },
    })

    session = useSessionStore()  // Access the store here
  })

  it('renders Navbar when user is not an admin', async () => {
    session.user = { role: 'ROLE_USER' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(Navbar).exists()).toBe(true)
    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(false)
  })

  it('renders NavBarAdmin when user is an admin', async () => {
    session.user = { role: 'ROLE_ADMIN' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(true)
    expect(wrapper.findComponent(Navbar).exists()).toBe(false)
  })

  it('renders NavBarAdmin when user is a moderator', async () => {
    session.user = { role: 'ROLE_MODERATOR' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(true)
    expect(wrapper.findComponent(Navbar).exists()).toBe(false)
  })
})
