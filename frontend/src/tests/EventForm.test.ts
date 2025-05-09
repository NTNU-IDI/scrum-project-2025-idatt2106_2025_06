import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import EventForm from '@/components/EventForm.vue'
import { createTestingPinia } from '@pinia/testing'
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
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
            stubActions: false,
          }),
        ],
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
    expect(wrapper.find('#radius').element.value).toBe('1') // default radius
  })

  it('updates v-model bindings when user types', async () => {
    const titleInput = wrapper.find('#title')
    const descriptionInput = wrapper.find('#description')
    const latitudeInput = wrapper.find('#latitude')
    const longitudeInput = wrapper.find('#longitude')
    const contentInput = wrapper.find('#content')

    await titleInput.setValue('Flom i Oslo')
    await descriptionInput.setValue('Sterk flom ved Akerselva')
    await latitudeInput.setValue('59.9139')
    await longitudeInput.setValue('10.7522')
    await contentInput.setValue('Dette er en lengre beskrivelse.')

    expect(titleInput.element.value).toBe('Flom i Oslo')
    expect(descriptionInput.element.value).toBe('Sterk flom ved Akerselva')
    expect(latitudeInput.element.value).toBe('59.9139')
    expect(longitudeInput.element.value).toBe('10.7522')
    expect(contentInput.element.value).toBe('Dette er en lengre beskrivelse.')
  })

  it('calls createNewEvent when submitting in "new" mode', async () => {
    await wrapper.find('#title').setValue('Flom i Oslo');
    await wrapper.find('#description').setValue('Sterk flom ved Akerselva');
    await wrapper.find('#latitude').setValue('59.9139');
    await wrapper.find('#longitude').setValue('10.7522');
    await wrapper.find('#content').setValue('Dette er en lengre beskrivelse.');
    await wrapper.find('#radius').setValue('5');

    wrapper.vm.selectedSeverity = 'low';
    wrapper.vm.eventType = 'natural_disaster';
    await wrapper.vm.$nextTick();

    const submitButton = wrapper.find("#submitButton")

    await submitButton.trigger('click')

    expect(eventStore.createNewEvent).toHaveBeenCalled();
    const payload = eventStore.createNewEvent.mock.calls[0][0];
    expect(payload.name).toBe('Flom i Oslo');
    expect(payload.description).toBe('Sterk flom ved Akerselva');
  });

  it('clears fields when clicking clear button', async () => {
    const titleInput = wrapper.find('#title')
    const descriptionInput = wrapper.find('#description')
    const latitudeInput = wrapper.find('#latitude')
    const longitudeInput = wrapper.find('#longitude')
    const contentInput = wrapper.find('#content')

    await titleInput.setValue('Flom i Oslo')
    await descriptionInput.setValue('Sterk flom ved Akerselva')
    await latitudeInput.setValue('59.9139')
    await longitudeInput.setValue('10.7522')
    await contentInput.setValue('Dette er en lengre beskrivelse.')


    const clearButton = wrapper.find('#clearButton');
    await clearButton.trigger('click');

    expect(titleInput.element.value).toBe('')
    expect(descriptionInput.element.value).toBe('')
    expect(latitudeInput.element.value).toBe('')
    expect(longitudeInput.element.value).toBe('')
    expect(contentInput.element.value).toBe('')
  });
})
