import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import EventForm from '@/components/EventForm.vue'
import { createPinia } from 'pinia'
import { useEventStore } from '@/stores/event'

describe('EventForm', () => {
  let wrapper
  let eventStore

  beforeEach(() => {
    wrapper = mount(EventForm, {
      props: {
        token: 'dummy-token',
        mode: 'new',
      },
      global: {
        plugins: [createPinia()],
      },
    })
    eventStore = useEventStore()
    vi.spyOn(eventStore, 'createNewEvent')
    vi.spyOn(eventStore, 'updateExistingEvent')
  })

  it('renders the form with default values', () => {
    expect(wrapper.find('#title').element.value).toBe('')
    expect(wrapper.find('#description').element.value).toBe('')
    expect(wrapper.find('#latitude').element.value).toBe('')
    expect(wrapper.find('#longitude').element.value).toBe('')
    expect(wrapper.find('#content').element.value).toBe('')
  })

  it('updates v-model bindings when user types', async () => {
    await wrapper.find('#title').setValue('Flom i Oslo')
    await wrapper.find('#description').setValue('Sterk flom ved Akerselva')
    await wrapper.find('#latitude').setValue('59.9139')
    await wrapper.find('#longitude').setValue('10.7522')
    await wrapper.find('#content').setValue('Dette er en lengre beskrivelse.')

    expect(wrapper.find('#title').element.value).toBe('Flom i Oslo')
    expect(wrapper.find('#description').element.value).toBe('Sterk flom ved Akerselva')
    expect(wrapper.find('#latitude').element.value).toBe('59.9139')
    expect(wrapper.find('#longitude').element.value).toBe('10.7522')
    expect(wrapper.find('#content').element.value).toBe('Dette er en lengre beskrivelse.')
  })

  it('calls createNewEvent when submitting in "new" mode', async () => {
    await wrapper.find('#title').setValue('Flom i Oslo')
    await wrapper.find('#description').setValue('Sterk flom ved Akerselva')
    await wrapper.find('#latitude').setValue('59.9139')
    await wrapper.find('#longitude').setValue('10.7522')
    await wrapper.find('#content').setValue('Dette er en lengre beskrivelse.')
    await wrapper.find('#impactAreaRadius').setValue('5')
    await wrapper.find('#mandatoryEvacuationRadius').setValue('3')
    await wrapper.find('#recommendedEvacuationRadius').setValue('2')

    wrapper.vm.selectedSeverity = 'low'
    wrapper.vm.eventType = 'natural_disaster'
    await wrapper.vm.$nextTick()

    await wrapper.find('#submitButton').trigger('click')

    expect(eventStore.createNewEvent).toHaveBeenCalled()
    const payload = eventStore.createNewEvent.mock.calls[0][0]
    expect(payload.name).toBe('Flom i Oslo')
    expect(payload.description).toBe('Sterk flom ved Akerselva')
  })

  it('clears fields when clicking clear button', async () => {
    await wrapper.find('#title').setValue('Flom i Oslo')
    await wrapper.find('#description').setValue('Sterk flom ved Akerselva')
    await wrapper.find('#latitude').setValue('59.9139')
    await wrapper.find('#longitude').setValue('10.7522')
    await wrapper.find('#content').setValue('Dette er en lengre beskrivelse.')

    await wrapper.find('#clearButton').trigger('click')

    expect(wrapper.find('#title').element.value).toBe('')
    expect(wrapper.find('#description').element.value).toBe('')
    expect(wrapper.find('#latitude').element.value).toBe('')
    expect(wrapper.find('#longitude').element.value).toBe('')
    expect(wrapper.find('#content').element.value).toBe('')
  })
})
