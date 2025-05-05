import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import HomeView from '@/views/HomeView.vue'

describe('HomeView.vue', () => {
  it('renders layout and child components', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: ['BentoGrid', 'BentoCard', 'BentoCardCustom', 'AlertCard'],
      },
    })

    expect(wrapper.exists()).toBe(true)

    expect(wrapper.find('bento-grid-stub').exists()).toBe(true)
    expect(wrapper.find('bento-card-custom-stub').exists()).toBe(true)
    expect(wrapper.find('alert-card-stub').exists()).toBe(true)
  })
})
