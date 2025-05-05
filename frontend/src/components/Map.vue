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

  emits: ['update:modelValue'],

  data() {
    return {
      selectedEl: null,
      latitude: null,
      longitude: null,
      errorMessage: null,
      isLoading: false,
      statusMessage: 'Ikke aktivert',
      geoDenied: false,

      // for path animation - https://docs.mapbox.com/mapbox-gl-js/example/animate-ant-path/
      dashSequence: [
        [0, 4, 3],
        [0.5, 4, 2.5],
        [1, 4, 2],
        [1.5, 4, 1.5],
        [2, 4, 1],
        [2.5, 4, 0.5],
        [3, 4, 0],
        [0, 0.5, 3, 3.5],
        [0, 1, 3, 3],
        [0, 1.5, 3, 2.5],
        [0, 2, 3, 2],
        [0, 2.5, 3, 1.5],
        [0, 3, 3, 1],
        [0, 3.5, 3, 0.5],
      ],
      dashStep: 0,
      animationId: null,
    }
  },

  mounted() {
    const { lng, lat, zoom, bearing, pitch } = this.modelValue
    this.map = new mapboxgl.Map({
      container: this.$refs.mapContainer,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [lng, lat],
      zoom,
      bearing,
      pitch,
    })

    this.markerObjs = []
    this.getGeolocation()

    this.map.on('load', () => {
      fetch('/data/tilfluktsrom-4326.json')
        .then((res) => res.json())
        .then((data) => {
          data.features.forEach((feature) => {
            const props = feature.properties
            const coords = feature.geometry.coordinates

            const el = document.createElement('div')
            el.className = 'marker'

            const icon = document.createElement('div')
            icon.className = 'icon'
            Object.assign(icon.style, {
              backgroundImage: 'url(/VaultDoor.svg)',
              width: '52px',
              height: '72px',
              scale: '0.6',
              cursor: 'pointer',
              transition: 'transform 0.1s ease',
            })

            icon.addEventListener('mouseenter', () => (icon.style.transform = 'scale(1.1)'))
            icon.addEventListener('mouseleave', () => {
              if (el !== this.selectedEl) icon.style.transform = 'scale(1)'
            })

            icon.addEventListener('click', (e) => {
              e.stopPropagation()
              if (this.selectedEl && this.selectedEl !== el) {
                this.selectedEl.querySelector('.icon').style.transform = 'scale(1)'
              }
              this.map.flyTo({
                center: coords,
                zoom: 14,
                essential: true,
              })
              this.selectedEl = el
              icon.style.transform = 'scale(1.2)'
              this.showCard(props)
              this.fetchAndAnimateRoute(coords)
            })

            el.appendChild(icon)

            const marker = new mapboxgl.Marker(el).setLngLat(coords).addTo(this.map)

            this.markerObjs.push({ marker, el, props })
          })

          this.applySettings(this.settings)
        })

      this.map.on('click', () => {
        if (this.selectedEl) {
          this.selectedEl.querySelector('.icon').style.transform = 'scale(1)'
          this.selectedEl = null
        }
        this.clearRoute()
        this.hideCard()
      })
    })

    const updateLocation = () => this.$emit('update:modelValue', this.getLocation())
    ;['moveend', 'zoomend', 'rotateend', 'pitchend'].forEach((evt) =>
      this.map.on(evt, updateLocation),
    )

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
        this.statusMessage = 'Henter posisjon...'

        navigator.geolocation.getCurrentPosition(
          (position) => {
            this.latitude = position.coords.latitude
            this.longitude = position.coords.longitude
            this.statusMessage = 'Posisjon funnet'
            this.geoDenied = false
            this.isLoading = false

            // Add marker if not already present
            if (!this.userMarker) {
              this.userMarker = new mapboxgl.Marker({ color: '#007cbf' })
                .setLngLat([this.longitude, this.latitude])
                .addTo(this.map)
            } else {
              this.userMarker.setLngLat([this.longitude, this.latitude])
            }
          },
          (error) => {
            this.isLoading = false
            if (error.code === error.PERMISSION_DENIED) {
              this.statusMessage = 'Tilgang nektet'
              this.geoDenied = true
            } else {
              this.statusMessage = `Feil: ${error.message}`
            }
          },
        )
      } else {
        this.statusMessage = 'Geolokasjon støttes ikke'
      }
    },

    flyToUser() {
      if (this.geoDenied || this.latitude == null || this.longitude == null) {
        this.getGeolocation()
        return
      }

      this.map.flyTo({
        center: [this.longitude, this.latitude],
        zoom: 14,
        essential: true,
      })
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
      const c = this.$refs.props
      c.innerHTML = `
        <div class="map-overlay-inner">
          <code>${props.adresse || props.name}</code><hr>
          ${Object.entries(props)
            .map(([k, v]) => `<li><b>${k}</b>: ${v}</li>`)
            .join('')}
        </div>`
      c.style.display = 'block'
    },
    hideCard() {
      this.$refs.props.style.display = 'none'
    },

    async fetchAndAnimateRoute(coords) {
      if (!this.latitude || !this.longitude) return

      const coordsParam = `${this.longitude},${this.latitude};${coords[0]},${coords[1]}`
      const url =
        `https://api.mapbox.com/directions/v5/mapbox/driving/${coordsParam}` +
        `?geometries=geojson&overview=full&access_token=${mapboxgl.accessToken}`

      const res = await fetch(url)
      const json = await res.json()
      if (!json.routes?.length) return

      const routeGeoJSON = { type: 'Feature', geometry: json.routes[0].geometry }

      if (this.map.getSource('route')) {
        ;['route-dashed', 'route-bg'].forEach((id) => {
          if (this.map.getLayer(id)) this.map.removeLayer(id)
        })
        this.map.removeSource('route')
      }

      this.map.addSource('route', { type: 'geojson', data: routeGeoJSON })
      this.map.addLayer({
        id: 'route-bg',
        type: 'line',
        source: 'route',
        paint: {
          'line-color': 'yellow',
          'line-width': 6,
          'line-opacity': 0.4,
        },
      })
      this.map.addLayer({
        id: 'route-dashed',
        type: 'line',
        source: 'route',
        paint: {
          'line-color': 'orange',
          'line-width': 6,
          'line-dasharray': this.dashSequence[0],
        },
      })

      this.animateDash()
    },

    animateDash(timestamp = 0) {
      const seq = this.dashSequence
      const next = parseInt((timestamp / 50) % seq.length)

      if (next !== this.dashStep) {
        this.map.setPaintProperty('route-dashed', 'line-dasharray', seq[next])
        this.dashStep = next
      }
      this.animationId = requestAnimationFrame(this.animateDash.bind(this))
    },

    clearRoute() {
      if (this.animationId) {
        cancelAnimationFrame(this.animationId)
        this.animationId = null
      }
      if (this.map.getSource('route')) {
        ;['route-dashed', 'route-bg'].forEach((id) => {
          if (this.map.getLayer(id)) this.map.removeLayer(id)
        })
        this.map.removeSource('route')
      }
      this.dashStep = 0
    },
  },
}
</script>

<style scoped>
.icon {
  background-size: contain;
  background-repeat: no-repeat;
}
</style>
