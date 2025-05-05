import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import HomeView from '@/views/HomeView.vue'

const stubs = {
  BentoGrid: true,
  BentoCard: true,
  BentoCardCustom: true,
  AlertCard: true,
}

describe('HomeView.vue', () => {
  it('renders layout and child components', () => {
    const wrapper = mount(HomeView, {
      global: { stubs },
    })

    expect(wrapper.exists()).toBe(true)

    expect(wrapper.findComponent({ name: 'BentoGrid' }).exists()).toBe(true)

    expect(wrapper.findAllComponents({ name: 'BentoCard' }).length).toBeGreaterThan(0)

    expect(wrapper.findComponent({ name: 'BentoCardCustom' }).exists()).toBe(true)

    expect(wrapper.findComponent({ name: 'AlertCard' }).exists()).toBe(true)
  })
})
