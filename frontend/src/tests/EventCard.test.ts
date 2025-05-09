import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import EventCard from '@/components/EventCard.vue'
import { createTestingPinia } from '@pinia/testing'
import { useEventStore } from '@/stores/event'

describe('EventCard', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(EventCard, {
      props: {
        variant: 'admin',
        eventId: '123',
        name: 'Test Event',
        description: 'This is a test event.',
        type: 'info',
        severity: 'info',
        status: 'active',
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

  it('renders event data correctly', () => {
    expect(wrapper.text()).toContain('Test Event')
    expect(wrapper.text()).toContain('This is a test event.')
  })

  it('emits edit event when edit button is clicked', async () => {
    const buttons = wrapper.findAll('button')
    const editButton =buttons[0]

    await editButton.trigger('click')

    expect(wrapper.emitted('edit')).toBeTruthy()

    const emittedPayload = wrapper.emitted('edit')[0][0]
    expect(emittedPayload.eventId).toBe('123')
    expect(emittedPayload.name).toBe('Test Event')
  })

  it('calls deleteEventById when delete button is clicked', async () => {
    const eventStore = useEventStore()

    const buttons = wrapper.findAll('button')
    const deleteButton = buttons[1]

    await deleteButton.trigger('click')

    expect(eventStore.deleteEventById).toHaveBeenCalled()
  })

  it('shows time only when updatedAt is today', async () => {
    const now = new Date().toISOString()
    await wrapper.setProps({ updatedAt: now })

    const timeText = wrapper.find('p.text-neutral-500').text()
    expect(timeText).toMatch(/\d{2}:\d{2}/)
  })

})
