import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import MarkerForm from '@/components/MarkerForm.vue'
import * as markerService from '@/service/markerService'
import { createPinia } from 'pinia'

describe('MarkerForm', () => {
  let wrapper
  let createMarkerSpy
  let updateMarkerSpy

  beforeEach(() => {
    wrapper = mount(MarkerForm, {
      props: {
        mode: 'new',
      },
      global: {
        plugins: [createPinia()],
      },
    })
    createMarkerSpy = vi.spyOn(markerService, 'createMarker').mockResolvedValue({})
    updateMarkerSpy = vi.spyOn(markerService, 'updateMarker').mockResolvedValue({})
  })

  it('renders the form with default values', () => {
    expect(wrapper.find('#title').element.value).toBe('')
    expect(wrapper.find('#description').element.value).toBe('')
    expect(wrapper.find('#latitude').element.value).toBe('')
    expect(wrapper.find('#longitude').element.value).toBe('')
    expect(wrapper.find('#contact-name').element.value).toBe('')
    expect(wrapper.find('#contact-email').element.value).toBe('')
    expect(wrapper.find('#contact-phone').element.value).toBe('')
  })

  it('updates v-model bindings when user types', async () => {
    await wrapper.find('#title').setValue('Legevakt')
    await wrapper.find('#description').setValue('Åpen hele døgnet')
    await wrapper.find('#latitude').setValue('59.911')
    await wrapper.find('#longitude').setValue('10.752')
    await wrapper.find('#contact-name').setValue('Ola Nordmann')
    await wrapper.find('#contact-email').setValue('ola@nordmann.no')
    await wrapper.find('#contact-phone').setValue('12345678')

    expect(wrapper.find('#title').element.value).toBe('Legevakt')
    expect(wrapper.find('#description').element.value).toBe('Åpen hele døgnet')
    expect(wrapper.find('#latitude').element.value).toBe('59.911')
    expect(wrapper.find('#longitude').element.value).toBe('10.752')
    expect(wrapper.find('#contact-name').element.value).toBe('Ola Nordmann')
    expect(wrapper.find('#contact-email').element.value).toBe('ola@nordmann.no')
    expect(wrapper.find('#contact-phone').element.value).toBe('12345678')
  })

  it('calls createMarker when submitting in "new" mode', async () => {
    await wrapper.find('#title').setValue('Legevakt')
    await wrapper.find('#description').setValue('Åpen hele døgnet')
    await wrapper.find('#latitude').setValue('59.911')
    await wrapper.find('#longitude').setValue('10.752')
    await wrapper.find('#contact-name').setValue('Ola Nordmann')
    await wrapper.find('#contact-email').setValue('ola@nordmann.no')
    await wrapper.find('#contact-phone').setValue('12345678')

    await wrapper.findAllComponents({ name: 'Button' })[0].trigger('click')

    expect(createMarkerSpy).toHaveBeenCalled()
    const payload = createMarkerSpy.mock.calls[0][0]
    expect(payload.name).toBe('Legevakt')
    expect(payload.description).toBe('Åpen hele døgnet')
    expect(payload.location.latitude).toBe(59.911)
    expect(payload.location.longitude).toBe(10.752)
  })

  it('calls updateMarker when submitting in "edit-marker" mode', async () => {
    const markerData = {
      id: 1,
      name: 'Apotek',
      description: 'Åpent 09-18',
      type: 'Pharmacy',
      location: { latitude: 59.91, longitude: 10.75 },
      openingHours: {
        monday: '09:00-18:00',
        tuesday: '09:00-18:00',
      },
      contactInfo: { name: 'Kari', email: 'kari@apotek.no', phone: '98765432' },
    }
    wrapper = mount(MarkerForm, {
      props: {
        mode: 'edit-marker',
        markerData,
      },
      global: {
        plugins: [createPinia()],
      },
    })
    await wrapper.find('#title').setValue('Nytt Apoteknavn')
    await wrapper.findAllComponents({ name: 'Button' })[0].trigger('click')

    expect(updateMarkerSpy).toHaveBeenCalled()
    const payload = updateMarkerSpy.mock.calls[0][0]
    expect(payload.id).toBe(1)
    expect(payload.name).toBe('Nytt Apoteknavn')
  })

  it('clears fields when clicking clear button', async () => {
    await wrapper.find('#title').setValue('Legevakt')
    await wrapper.find('#description').setValue('Åpen hele døgnet')
    await wrapper.find('#latitude').setValue('59.911')
    await wrapper.find('#longitude').setValue('10.752')

    await wrapper.findAllComponents({ name: 'Button' })[1].trigger('click')

    expect(wrapper.find('#title').element.value).toBe('')
    expect(wrapper.find('#description').element.value).toBe('')
    expect(wrapper.find('#latitude').element.value).toBe('')
    expect(wrapper.find('#longitude').element.value).toBe('')
  })
})
