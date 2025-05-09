import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import ScenarioCard from '@/components/ScenarioCard.vue'
import { createTestingPinia } from '@pinia/testing'
import { useScenarioStore } from '@/stores/scenario'

describe('ScenarioCard', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(ScenarioCard, {
      props: {
        scenarioId: '123',
        title: 'Test Scenario',
        description: 'This is a test scenario.',
        createdAt: '2024-05-01T12:00:00Z',
        updatedAt: '2024-05-01T12:00:00Z',
      },
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
          }),
        ],
      },
    })
  })

  it('')
})
