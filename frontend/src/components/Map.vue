<template>
  <div class="relative w-full h-full flex">
    <div ref="mapContainer" class="map-container flex-1 w-full rounded-xl shadow"></div>
    <div
      ref="props"
      class="w-full h-64 bg-white hidden p-4 absolute bottom-4 left-4 mr-40 overflow-hidden rounded-md"
    ></div>
  </div>
</template>

<script>
import 'mapbox-gl/dist/mapbox-gl.css'
import mapboxgl from 'mapbox-gl'

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN

export default {
  props: {
    modelValue: { type: Object, required: true },
    settings: { type: Object, required: true },
  },
  data() {
    return {
      selectedEl: null,
      latitude: null,
      longitude: null,
      errorMessage: null,
      isLoading: false,
    }
  },
  emits: ['update:modelValue'],
  mounted() {
    const { lng, lat, zoom, bearing, pitch } = this.modelValue
    const map = new mapboxgl.Map({
      container: this.$refs.mapContainer,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [lng, lat],
      zoom,
      bearing,
      pitch,
    })
    this.map = map

    this.markerObjs = []

    this.getGeolocation()
    map.on('load', () => {
      fetch('/data/tilfluktsrom-4326.json')
        .then((res) => res.json())
        .then((data) => {
          data.features.forEach((feature) => {
            const props = feature.properties
            const el = document.createElement('div')
            el.className = 'marker'

            const icon = document.createElement('div')
            icon.className = 'icon'
            icon.style.backgroundImage = 'url(/VaultDoor.svg)'
            icon.style.width = '52px'
            icon.style.height = '72px'
            icon.style.scale = '0.6'
            icon.style.cursor = 'pointer'
            icon.style.transition = 'transform 0.1s ease'

            // hover effects
            icon.addEventListener('mouseenter', () => {
              icon.style.transform = 'scale(1.1)'
            })
            icon.addEventListener('mouseleave', () => {
              if (el !== this.selectedEl) {
                icon.style.transform = 'scale(1)'
              }
            })

            // click behavior
            icon.addEventListener('click', (e) => {
              e.stopPropagation()
              if (this.selectedEl && this.selectedEl !== el) {
                this.selectedEl.querySelector('.icon').style.transform = 'scale(1)'
              }
              this.selectedEl = el
              icon.style.transform = 'scale(1.2)'
              this.showCard(props)
            })

            el.appendChild(icon)

            const marker = new mapboxgl.Marker(el)
              .setLngLat(feature.geometry.coordinates)
              .addTo(map)

            this.markerObjs.push({ marker, el, props })
          })

          this.applySettings(this.settings)
        })

      map.on('click', () => {
        if (this.selectedEl) {
          this.selectedEl.querySelector('.icon').style.transform = 'scale(1)'
          this.selectedEl = null
        }
        this.hideCard()
      })
    })

    const updateLocation = () => this.$emit('update:modelValue', this.getLocation())
    ;['moveend', 'zoomend', 'rotateend', 'pitchend'].forEach((evt) => map.on(evt, updateLocation))

    this.$watch(
      () => this.settings,
      (newSettings) => this.applySettings(newSettings),
      { deep: true },
    )
  },

  unmounted() {
    if (this.map) this.map.remove()
  },

  methods: {
    getLocation() {
      const [lng, lat] = this.map.getCenter().toArray()
      return {
        lng,
        lat,
        bearing: this.map.getBearing(),
        pitch: this.map.getPitch(),
        zoom: this.map.getZoom(),
      }
    },
    getGeolocation() {
      if ('geolocation' in navigator) {
        this.isLoading = true

        navigator.geolocation.getCurrentPosition(
          (position) => {
            this.latitude = position.coords.latitude
            this.longitude = position.coords.longitude
            this.isLoading = false
            console.log(
              'Client location: ' + position.coords.latitude + ', ' + position.coords.longitude,
            )
            this.userMarker = new mapboxgl.Marker({
              color: '#007cbf',
            })
              .setLngLat([this.longitude, this.latitude])
              .addTo(this.map)
          },
          (error) => {
            this.isLoading = false
            this.errorMessage = `Error: ${error.message}`
          },
        )
      } else {
        this.errorMessage = 'Geolocation is not supported by your browser.'
      }
    },
    flyToUser() {
      if (this.latitude != null && this.longitude != null && this.map) {
        this.map.flyTo({
          center: [this.longitude, this.latitude],
          zoom: 14,
          essential: true,
        })
      } else {
        console.warn('User location not available yet.')
      }
    },
    applySettings(settings) {
      this.markerObjs.forEach(({ el, props }) => {
        let visible = true
        if (!settings.showShelters) visible = false
        if (props.capacity < settings.minCapacity) visible = false
        if (settings.searchQuery) {
          const q = settings.searchQuery.toLowerCase()
          const text = ((props.adresse || props.name) + '').toLowerCase()
          if (!text.includes(q)) visible = false
        }
        el.style.display = visible ? 'block' : 'none'
      })
    },

    showCard(props) {
      const container = this.$refs.props
      container.innerHTML = `
        <div class="map-overlay-inner">
          <code>${props.adresse || props.name}</code><hr>
          ${Object.entries(props)
            .map(([k, v]) => `<li><b>${k}</b>: ${v}</li>`)
            .join('')}
        </div>`
      container.style.display = 'block'
    },

    hideCard() {
      this.$refs.props.style.display = 'none'
    },
  },
}
</script>

<style scoped>
.icon {
  background-size: contain;
  background-repeat: no-repeat;
  transition: transform 0.1s ease;
}
</style>
