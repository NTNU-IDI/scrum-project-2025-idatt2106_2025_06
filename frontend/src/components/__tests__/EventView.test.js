import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import EventDetail from '@/views/EventView.vue'
import { fetchEventById } from '@/service/eventService.js'

vi.mock('@/service/eventService.js', () => ({
  fetchEventById: vi.fn(),
}))

vi.mock('vue-router', () => ({
  useRoute: () => ({
    params: { id: '123' },
  }),
}))

describe('EventDetail.vue', () => {
  const mockEvent = {
    id: '123',
    name: 'Test Event',
    description: 'Dette er en test-beskrivelse',
    content: 'Detaljert info om eventet.',
    status: 'planned',
    severity: 'high',
    createdAt: '2024-05-01T12:00:00Z',
    updatedAt: '2024-05-05T15:30:00Z',
  }

  beforeEach(() => {
    fetchEventById.mockReset()
  })

  it('gets and shows event-data', async () => {
    fetchEventById.mockResolvedValueOnce(mockEvent)

    const wrapper = mount(EventDetail)

    expect(fetchEventById).toHaveBeenCalledWith('123')
    expect(wrapper.html()).not.toContain('Test Event')

    await flushPromises()

    expect(wrapper.text()).toContain('Test Event')
    expect(wrapper.text()).toContain('Dette er en test-beskrivelse')
    expect(wrapper.text()).toContain('Detaljert info om eventet.')

    expect(wrapper.text()).toContain('Status: Planlagt')
    expect(wrapper.text()).toContain('Beredskapsnivå: Høy')

    expect(wrapper.text()).toContain('Publisert: 1. mai 2024')
    expect(wrapper.text()).toContain('Sist oppdatert: 5. mai 2024')
  })

  it('handles fetch-error', async () => {
    fetchEventById.mockRejectedValueOnce(new Error('Network error'))

    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})

    const wrapper = mount(EventDetail)

    await flushPromises()

    expect(wrapper.html()).not.toContain('Test Event')

    expect(consoleErrorSpy).toHaveBeenCalledWith(expect.stringContaining('Error fetching event:'), expect.any(Error))

    consoleErrorSpy.mockRestore()
  })
})
