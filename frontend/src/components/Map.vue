<template>
  <div class="relative w-full h-full flex">
    <div ref="mapContainer" class="map-container flex-1 w-full rounded-xl shadow"></div>

    <div
      v-if="popup.visible"
      class="map-overlay-inner bg-white p-4 absolute bottom-4 left-4 mr-40 overflow-auto rounded-md"
    >
      <h3 class="font-semibold mb-2">{{ popup.item.name }}</h3>
      <p class="mb-2">{{ popup.item.description }}</p>

      <div class="flex gap-2 mt-2">
        <Button v-if="!popup.isEvent" class="flex-1" @click="routeToPopup"> Vis rute hit</Button>

        <RouterLink v-if="popup.isEvent" :to="`/alert/${popup.item.id}`" class="flex-1">
          <Button>Gå til hendelse</Button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import 'mapbox-gl/dist/mapbox-gl.css'
import mapboxgl from 'mapbox-gl'
import { createVNode, onBeforeUnmount, onMounted, reactive, ref, render, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { Button } from '@/components/ui/button'
import { getClosestMarkerId } from '@/service/markerService.js'
import {
  HeartPulse,
  Hospital,
  Info,
  MapPin,
  OctagonAlert,
  Package,
  Plus,
  Shield as ShieldIcon,
  Shield,
  TriangleAlert,
  Vault,
  Warehouse,
} from 'lucide-vue-next'

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN

const props = defineProps({
  modelValue: Object,
  settings: Object,
  markers: Array,
  storages: Array,
  events: Array,
  startSelection: [String, Number],
})
const emit = defineEmits(['update:modelValue', 'select-event', 'select-marker'])

const mapContainer = ref(null)
let map = null

const popup = reactive({
  visible: false,
  item: null,
  coords: [0, 0],
  isEvent: false,
})

let statusMessage = ref('Ikke aktivert')
let userMarker = null
let routeStart = null
let lastDest = null
let animationId = null
let dashStep = 0

let markerObjs = []
let eventMarkerObjs = []

// path animation
const dashSequence = [
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
]

const severityConfig = {
  info: { icon: Info, bg: '#3498db' },
  high: { icon: OctagonAlert, bg: '#e74c3c' },
  medium: { icon: TriangleAlert, bg: '#f1c40f' },
  low: { icon: ShieldIcon, bg: '#2ecc71' },
}
const typeConfig = {
  Shelter: { icon: Vault, bg: '#4e95f3' },
  Defibrillator: { icon: HeartPulse, bg: '#f34e4e' },
  EmergencyClinic: { icon: Hospital, bg: '#e36414' },
  DistributionPoint: { icon: Package, bg: '#14e3d3' },
  PoliceStation: { icon: Shield, bg: '#e314a1' },
  Pharmacy: { icon: Plus, bg: '#14a114' },
  General: { icon: MapPin, bg: '#a1a114' },
  storage: { icon: Warehouse, bg: '#8e4ef3' },
}

onMounted(() => {
  const { lng, lat, zoom, bearing, pitch } = props.modelValue
  map = new mapboxgl.Map({
    container: mapContainer.value,
    style: 'mapbox://styles/mapbox/streets-v12',
    center: [lng, lat],
    zoom,
    bearing,
    pitch,
  })

  getGeolocation()

  map.on('load', () => {
    updateRouteStart()
    redrawAll()
  })
  map.on('click', clearSelection)
  ;['moveend', 'zoomend', 'rotateend', 'pitchend'].forEach((evt) =>
    map.on(evt, () => emit('update:modelValue', getLocation())),
  )

  watch(() => [props.markers, props.storages, props.events, props.settings], redrawAll, {
    deep: true,
  })

  watch(
    () => props.startSelection,
    () => {
      updateRouteStart()
      if (lastDest) fetchAndAnimateRoute(routeStart, lastDest)
    },
  )
})

onBeforeUnmount(() => {
  cancelAnimationFrame(animationId)
  map?.remove()
})

async function flyToNearest(type) {
  const id = await getClosestMarkerId(routeStart, type)
  if (id == null) {
    statusMessage.value = 'Ingen nærmeste markør funnet'
    return
  }
  const m = props.markers.find((x) => x.id === id)
  if (!m) return
  const coords = [m.location.longitude, m.location.latitude]
  map.flyTo({ center: coords, zoom: 14, essential: true })
  fetchAndAnimateRoute(routeStart, coords)
}

function flyToUser() {
  if (!userMarker) {
    getGeolocation()
    return
  }
  const { lng, lat } = userMarker.getLngLat()
  map.flyTo({ center: [lng, lat], zoom: 14, essential: true })
}

defineExpose({ statusMessage, flyToUser, flyToNearest })

function getLocation() {
  const [lng, lat] = map.getCenter().toArray()
  return {
    lng,
    lat,
    bearing: map.getBearing(),
    pitch: map.getPitch(),
    zoom: map.getZoom(),
  }
}

function getGeolocation() {
  if (!navigator.geolocation) {
    statusMessage.value = 'Geolokasjon støttes ikke'
    return
  }
  statusMessage.value = 'Henter posisjon…'
  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const { latitude: lat, longitude: lng } = pos.coords
      statusMessage.value = 'Posisjon funnet'
      if (!userMarker) {
        userMarker = new mapboxgl.Marker({ color: '#007cbf' }).setLngLat([lng, lat]).addTo(map)
      } else {
        userMarker.setLngLat([lng, lat])
      }
      if (props.startSelection === 'current') routeStart = { lng, lat }
    },
    (err) => {
      statusMessage.value =
        err.code === err.PERMISSION_DENIED ? 'Tilgang nektet' : `Feil: ${err.message}`
    },
  )
}

function updateRouteStart() {
  if (props.startSelection === 'current') {
    if (userMarker) {
      const { lng, lat } = userMarker.getLngLat()
      routeStart = { lng, lat }
    } else {
      getGeolocation()
    }
  } else {
    const st = props.storages.find((s) => String(s.id) === String(props.startSelection))
    if (st?.location?.longitude != null) {
      routeStart = { lng: st.location.latitude, lat: st.location.longitude }
    }
  }
}

function generateCircle([lng, lat], radiusKm, points = 64) {
  const coords = []
  for (let i = 0; i < points; i++) {
    const angle = (i / points) * 2 * Math.PI
    const dx = radiusKm * Math.cos(angle)
    const dy = radiusKm * Math.sin(angle)
    const dLat = dy / 110.574
    const dLng = dx / (111.32 * Math.cos((lat * Math.PI) / 180))
    coords.push([lng + dLng, lat + dLat])
  }
  coords.push(coords[0])
  return { type: 'Feature', geometry: { type: 'Polygon', coordinates: [coords] } }
}

async function fetchAndAnimateRoute(start, end) {
  if (!start || !end) return
  const coords = `${start.lng},${start.lat};${end[0]},${end[1]}`
  const url =
    `https://api.mapbox.com/directions/v5/mapbox/driving/${coords}` +
    `?geometries=geojson&overview=full&access_token=${mapboxgl.accessToken}`
  const res = await fetch(url)
  const js = await res.json()
  if (!js.routes?.length) return

  const geo = { type: 'Feature', geometry: js.routes[0].geometry }
  if (map.getSource('route')) {
    ;['route-bg', 'route-dashed'].forEach((id) => {
      if (map.getLayer(id)) map.removeLayer(id)
    })
    map.removeSource('route')
  }
  map.addSource('route', { type: 'geojson', data: geo })
  map.addLayer({
    id: 'route-bg',
    type: 'line',
    source: 'route',
    paint: { 'line-color': 'yellow', 'line-width': 6, 'line-opacity': 0.4 },
  })
  map.addLayer({
    id: 'route-dashed',
    type: 'line',
    source: 'route',
    paint: {
      'line-color': 'orange',
      'line-width': 6,
      'line-dasharray': dashSequence[0],
    },
  })

  animateDash()
}

function animateDash(timestamp = 0) {
  const idx = Math.floor((timestamp / 50) % dashSequence.length)
  if (idx !== dashStep) {
    map.setPaintProperty('route-dashed', 'line-dasharray', dashSequence[idx])
    dashStep = idx
  }
  animationId = requestAnimationFrame(animateDash)
}

function redrawAll() {
  if (!map?.isStyleLoaded()) return

  const style = map.getStyle()?.sources || {}
  Object.keys(style)
    .filter((id) => id.startsWith('event-') && id.endsWith('-src'))
    .forEach((srcId) => {
      const layerId = srcId.replace('-src', '-layer')
      if (map.getLayer(layerId)) map.removeLayer(layerId)
      map.removeSource(srcId)
    })

  markerObjs.forEach((o) => o.marker.remove())
  markerObjs = []
  eventMarkerObjs.forEach((o) => o.marker.remove())
  eventMarkerObjs = []

  const s = props.settings
  const q = s.searchQuery?.toLowerCase() || ''

  // standard markers
  props.markers.forEach((m) => {
    const { type, location, name, description, capacity } = m
    if (
      (type === 'Shelter' && !s.showShelters) ||
      (type === 'Defibrillator' && !s.showDefibrillators) ||
      (type === 'EmergencyClinic' && !s.showEmergencyClinics) ||
      (type === 'DistributionPoint' && !s.showDistributionPoints) ||
      (type === 'PoliceStation' && !s.showPoliceStations) ||
      (type === 'Pharmacy' && !s.showPharmacies) ||
      (type === 'General' && !s.showGeneral)
    )
      return
    if (capacity != null && capacity < s.minCapacity) return
    if (q && !`${name} ${description}`.toLowerCase().includes(q)) return

    addHtmlMarker(m, type, [location.longitude, location.latitude])
  })

  // storages
  if (s.showStorages) {
    props.storages.forEach((st) => {
      console.log('st: ' + st)
      const { location, name } = st
      if (q && !`${name}`.toLowerCase().includes(q)) return
      props.storages.forEach((st) => {
        addHtmlMarker(st, 'storage', [location.latitude, location.longitude])
      })
    })
  }

  // event markers + circles
  if (s.showEvents) {
    props.events.forEach((ev) => {
      addHtmlMarker(
        ev,
        null,
        [ev.location.longitude, ev.location.latitude],
        severityConfig[ev.severity] || severityConfig.info,
      )
    })

    props.events.forEach((ev) => {
      const center = [ev.location.longitude, ev.location.latitude]
      ;[
        ['impactAreaRadiusKm', 'rgba(0,0,255,0.2)', 'rgba(0,0,255,0.5)'],
        ['recommendedEvacuationAreaRadiusKm', 'rgba(255,165,0,0.2)', 'rgba(255,165,0,0.5)'],
        ['mandatoryEvacuationAreaRadiusKm', 'rgba(255,0,0,0.2)', 'rgba(255,0,0,0.5)'],
      ].forEach(([f, fill, outline]) => {
        const km = ev[f]
        if (km != null) {
          const feat = generateCircle(center, km)
          const srcId = `event-${ev.id}-${f}-src`
          const layerId = `event-${ev.id}-${f}-layer`
          map.addSource(srcId, { type: 'geojson', data: feat })
          map.addLayer({
            id: layerId,
            type: 'fill',
            source: srcId,
            paint: { 'fill-color': fill, 'fill-outline-color': outline },
          })
        }
      })
    })
  }
}

function addHtmlMarker(item, type, coords, overrideCfg) {
  const cfg = overrideCfg || typeConfig[type] || typeConfig.General
  const el = document.createElement('div')
  el.className = 'marker'
  const wrap = document.createElement('div')
  Object.assign(wrap.style, {
    width: '32px',
    height: '32px',
    borderRadius: '50%',
    backgroundColor: cfg.bg,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    cursor: 'pointer',
    transition: 'transform .1s',
  })
  render(createVNode(cfg.icon, { size: 20, color: '#fff' }), wrap)
  wrap.addEventListener('mouseenter', () => (wrap.style.transform = 'scale(1.1)'))
  wrap.addEventListener('mouseleave', () => (wrap.style.transform = 'scale(1)'))
  el.appendChild(wrap)

  const marker = new mapboxgl.Marker(el).setLngLat(coords).addTo(map)
  const isEvent = !!overrideCfg
  const isStorage = type === 'storage'

  if (isEvent) {
    marker.getElement().addEventListener('click', (e) => {
      e.stopPropagation()
      popup.item = { ...item, id: item.id } // ensure .id
      popup.coords = coords
      popup.isEvent = true
      popup.visible = true
      emit('select-event', popup.item)
    })
    eventMarkerObjs.push({ marker, el })
  } else if (!isStorage) {
    marker.getElement().addEventListener('click', (e) => {
      e.stopPropagation()
      popup.item = item
      popup.coords = coords
      popup.isEvent = false
      popup.visible = true
      emit('select-marker', item)
    })
    markerObjs.push({ marker, el })
  }
}

function clearSelection() {
  popup.visible = false
}

function routeToPopup() {
  lastDest = popup.coords
  fetchAndAnimateRoute(routeStart, popup.coords)
}
</script>

<style scoped>
.marker {
}
</style>
