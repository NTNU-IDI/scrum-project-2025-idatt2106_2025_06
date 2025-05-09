import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import HomeView from '@/views/HomeView.vue'
import { createPinia } from 'pinia'

describe('HomeView.vue', () => {
  it('renders layout and child components', () => {
    const wrapper = mount(HomeView, {
      global: {
        plugins: [createPinia()],
        stubs: ['BentoGrid', 'BentoCard', 'BentoCardCustom', 'AlertCard'],
      },
    })

    expect(wrapper.exists()).toBe(true)

    expect(wrapper.find('bento-grid-stub').exists()).toBe(true)
  })
})
