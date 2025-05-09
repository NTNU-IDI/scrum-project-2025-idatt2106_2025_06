// components/__tests__/ScenarioDialog.test.ts
import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import ScenarioDialog from '@/components/ScenarioDialog.vue'
import { createTestingPinia } from '@pinia/testing'

// Mock the UI components
vi.mock('@/components/ui/dialog', () => ({
  Dialog: {
    name: 'Dialog',
    template: '<div><slot /></div>'
  },
  DialogContent: {
    name: 'DialogContent',
    template: '<div><slot /></div>',
    props: ['description']
  },
  DialogHeader: {
    name: 'DialogHeader',
    template: '<div><slot /></div>'
  },
  DialogTitle: {
    name: 'DialogTitle',
    template: '<div><slot /></div>'
  },
  DialogFooter: {
    name: 'DialogFooter',
    template: '<div><slot /></div>'
  }
}))

vi.mock('@/components/ui/button', () => ({
  Button: {
    name: 'Button',
    template: '<button type="button"><slot /></button>'
  }
}))

vi.mock('@/components/ui/input', () => ({
  Input: {
    name: 'Input',
    template: '<input v-model="modelValue" @input="$emit(\'update:modelValue\', $event.target.value)" />',
    props: ['modelValue']
  }
}))

describe('ScenarioDialog', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(ScenarioDialog, {
      props: {
        scenario: null
      },
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
          }),
        ],
      }
    })
  })

  it('shows correct title for new scenario', () => {
    expect(wrapper.text()).toContain('Legg til nytt scenario')
  })

  it('shows correct title for editing scenario', async () => {
    await wrapper.setProps({
      scenario: {
        id: '1',
        title: 'Test Scenario',
        description: 'Test Description',
        content: 'Test Content'
      }
    })

    expect(wrapper.text()).toContain('Endre scenario')
  })

  it('emits close event when cancel button is clicked', async () => {
    const cancelButton = wrapper.find('button[variant="outline"]')
    await cancelButton.trigger('click')

    expect(wrapper.emitted('close')).toBeTruthy()
  })

  it('creates new scenario when form is submitted', async () => {
    // Fill in the form
    const inputs = wrapper.findAll('input')
    await inputs[0].setValue('New Scenario')
    await inputs[1].setValue('New Description')
    await wrapper.find('textarea').setValue('New Content')

    // Submit the form
    const saveButton = wrapper.find('button:not([variant="outline"])')
    await saveButton.trigger('click')

    // Get store and verify it was called
    const scenarioStore = wrapper.vm.scenarioStore
    expect(scenarioStore.createScenario).toHaveBeenCalledWith({
      title: 'New Scenario',
      description: 'New Description',
      content: 'New Content',
      url: ''
    })

    // Verify dialog was closed
    expect(wrapper.emitted('close')).toBeTruthy()
  })
})
