import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import AppView from '@/App.vue'
import { createTestingPinia } from '@pinia/testing'
import { useSessionStore } from '@/stores/session'
import Navbar from '@/components/NavBar.vue'
import NavBarAdmin from '@/components/NavBarAdmin.vue'

describe('YourComponent', () => {
  let wrapper

  beforeEach(() => {
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
        plugins: [pinia],
        stubs: {
          RouterView: true,
          RouterLink: true,
          Footer: true,
        },
      },
    })
  })

  it('renders Navbar when user is not an admin', async () => {
    const session = useSessionStore()
    session.user = { role: 'ROLE_USER' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(Navbar).exists()).toBe(true)
    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(false)
  })

  it('renders NavBarAdmin when user is an admin', async () => {
    const session = useSessionStore()
    session.user = { role: 'ROLE_ADMIN' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(true)
    expect(wrapper.findComponent(Navbar).exists()).toBe(false)
  })

  it('renders NavBarAdmin when user is a moderator', async () => {
    const session = useSessionStore()
    session.user = { role: 'ROLE_MODERATOR' }
    await wrapper.vm.$nextTick()

    expect(wrapper.findComponent(NavBarAdmin).exists()).toBe(true)
    expect(wrapper.findComponent(Navbar).exists()).toBe(false)
  })
})
