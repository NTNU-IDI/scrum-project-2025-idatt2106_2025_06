// ScenarioCard.test.ts
import { mount } from '@vue/test-utils'
import ScenarioCard from '@/components/ScenarioCard.vue'
import { createTestingPinia } from '@pinia/testing'
import { describe, it, expect, vi } from 'vitest'

// Mock the external components
vi.mock('@/components/ui/button', () => ({
  Button: {
    name: 'Button',
    template: '<button><slot /></button>'
  }
}))

vi.mock('lucide-vue-next', () => ({
  PencilIcon: {
    template: '<div>PencilIcon</div>'
  },
  PlusIcon: {
    template: '<div>PlusIcon</div>'
  },
  TrashIcon: {
    template: '<div>TrashIcon</div>'
  }
}))

vi.mock('@/components/ScenarioDialog.vue', () => ({
  default: {
    name: 'ScenarioDialog',
    template: '<div>ScenarioDialog</div>',
    props: ['scenario']
  }
}))

describe('ScenarioCard', () => {
  it('viser alle scenarioer i listen', () => {
    const wrapper = mount(ScenarioCard, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
            initialState: {
              scenario: {
                scenarios: [
                  { id: '1', title: 'Brann', description: 'Brann i bolig' },
                  { id: '2', title: 'Flom', description: 'Flom i Oslo' },
                ]
              },
              session: {
                token: 'abc'
              }
            }
          })
        ]
      }
    })

    expect(wrapper.text()).toContain('Brann')
    expect(wrapper.text()).toContain('Flom')
  })

  it('åpner dialog når "Legg til nytt scenario" klikkes', async () => {
    const wrapper = mount(ScenarioCard, {
      global: {
        plugins: [createTestingPinia({
          createSpy: vi.fn
        })]
      }
    })

    // Initially, the dialog should not be visible
    expect(wrapper.findComponent({ name: 'ScenarioDialog' }).exists()).toBe(false)

    // Click the "Legg til nytt scenario" button
    const button = wrapper.find('button')
    await button.trigger('click')

    // After clicking, the dialog should be visible
    expect(wrapper.findComponent({ name: 'ScenarioDialog' }).exists()).toBe(true)
  })
})
