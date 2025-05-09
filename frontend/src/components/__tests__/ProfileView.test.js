import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import ProfileView from '@/views/ProfileView.vue'

vi.mock('@/stores/session', () => ({
  useSessionStore: () => ({
    token: 'fake-token',
    isAuthenticated: true,
    user: { id: 1, name: 'Testbruker', email: 'test@example.com', trackingEnabled: false },
    updateProfile: vi.fn(() => Promise.resolve(true)),
    updatePassword: vi.fn(() => Promise.resolve(true)),
    updateLocationTracking: vi.fn(() => Promise.resolve(true)),
    updateDeletedLocationHistory: vi.fn(() => Promise.resolve(true)),
    logout: vi.fn()
  })
}))

vi.mock('@/stores/storage', () => ({
  useStorageStore: () => ({
    storages: [],
    membersByStorageId: {},
    fetchAll: vi.fn(() => Promise.resolve()),
    create: vi.fn(() => Promise.resolve({ id: 1 })),
    join: vi.fn(() => Promise.resolve(true)),
    removeMember: vi.fn(() => Promise.resolve(true))
  })
}))

describe('ProfileView.vue', () => {
  let wrapper

  beforeEach(async () => {
    wrapper = mount(ProfileView, {
      attachTo: document.body,
      global: {
        stubs: ['router-link']
      }
    })
    await flushPromises()
  })

  it('viser overskriften "Min side"', () => {
    expect(wrapper.text()).toContain('Min side')
  })

  it('viser seksjonen "Personalia"', () => {
    expect(wrapper.text()).toContain('Personalia:')
  })

  it('viser seksjonen "Husstander"', () => {
    expect(wrapper.text()).toContain('Husstander:')
  })

  it('viser seksjonen "Personvern"', () => {
    expect(wrapper.text()).toContain('Personvern:')
  })

  it('initielt viser ikke feilmelding for passord', () => {
    expect(wrapper.text()).not.toContain('Kunne ikke endre passord.')
  })

  it('kan klikke på Endre passord uten feil', async () => {
    const button = wrapper.find('[data-testid="change-passwd-btn"]')
    await button.trigger('click')
    expect(button.exists()).toBe(true) // sikrer at knapp fortsatt finnes
  })

  it('kan klikke på Rediger personalia uten feil', async () => {
    const button = wrapper.find('[data-testid="edit-profile-btn"]')
    await button.trigger('click')
    expect(button.exists()).toBe(true)
  })

  it('kan klikke på Opprett ny husstand uten feil', async () => {
    const button = wrapper.find('[data-testid="create-storage-btn"]')
    await button.trigger('click')
    expect(button.exists()).toBe(true)
  })

  it('viser brukerens navn og e-post', () => {
    expect(wrapper.text()).toContain('Brukernavn: Testbruker')
    expect(wrapper.text()).toContain('Epostadresse: test@example.com')
  })

  it('viser Rediger personalia knapp', async () => {
    const button = wrapper.find('[data-testid="edit-profile-btn"]')
    expect(button.exists()).toBe(true)
  })

  it('viser Endre passord knapp', async () => {
    const button = wrapper.find('[data-testid="change-passwd-btn"]')
    expect(button.exists()).toBe(true)
  })

  it('viser Opprett husstand knapp', async () => {
    const button = wrapper.find('[data-testid="create-storage-btn"]')
    expect(button.exists()).toBe(true)
  })

  it('viser Bli med i husstand knapp', async () => {
    const button = wrapper.find('[data-testid="join-storage-btn"]')
    expect(button.exists()).toBe(true)
  })

  it('viser Slett lokasjonshistorikk knapp', async () => {
    const button = wrapper.find('[data-testid="delete-location-history-btn"]')
    expect(button.exists()).toBe(true)
  })
})
