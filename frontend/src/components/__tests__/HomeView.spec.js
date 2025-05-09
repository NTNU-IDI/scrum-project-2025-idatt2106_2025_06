import { mount } from '@vue/test-utils'
import { describe, it } from 'vitest'
import HomeView from '@/views/HomeView.vue'

describe('HomeView.vue', () => {
  it('renders layout and child components', () => {
    const wrapper = mount(HomeView, {
      global: {
        stubs: ['BentoGrid', 'BentoCard', 'BentoCardCustom', 'AlertCard'],
      },
    })
  })
})
