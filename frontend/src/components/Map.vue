<template>
  <div class="relative w-full h-full flex">
    <div ref="mapContainer" class="map-container flex-1 w-full rounded-xl shadow"></div>
    <div
      v-if="popup.visible"
      class="map-overlay-inner bg-white p-4 absolute bottom-4 left-4 mr-40 overflow-auto rounded-md"
    >
      <h3 class="font-semibold mb-2">{{ popup.item.name }}</h3>
      <p class="mb-2">{{ popup.item.description }}</p>
      <Button
        class="mt-2"
        @click="
          () => {
            lastDest = popup.coords
            fetchAndAnimateRoute(routeStart, popup.coords)
          }
        "
      >
        Vis rute hit
      </Button>
    </div>
  </div>
</template>

<script setup>
import 'mapbox-gl/dist/mapbox-gl.css'
import mapboxgl from 'mapbox-gl'
import {
  createVNode,
  defineEmits,
  defineExpose,
  defineProps,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  render,
  watch,
} from 'vue'
import {
  Archive,
  HeartPulse,
  Hospital,
  MapPin,
  Package,
  Plus,
  Shield,
  Vault,
} from 'lucide-vue-next'
import { Button } from '@/components/ui/button/index.js'

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN

const popup = reactive({
  visible: false,
  item: null,
  coords: [0, 0],
})

const props = defineProps({
  modelValue: Object,
  settings: Object,
  markers: Array,
  storages: Array,
  startSelection: [String, Number],
})
const emit = defineEmits(['update:modelValue'])

const statusMessage = ref('Ikke aktivert')
let userMarker = null

function flyToUser() {
  if (!userMarker) {
    getGeolocation()
    return
  }
  const { lng, lat } = userMarker.getLngLat()
  map.flyTo({ center: [lng, lat], zoom: 14, essential: true })
}

defineExpose({ statusMessage, flyToUser })

const mapContainer = ref(null)
const propsOverlay = ref(null)
let map,
  animationId,
  dashStep = 0,
  routeStart = null,
  lastDest = null,
  markerObjs = []

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

const typeConfig = {
  Shelter: { icon: Vault, bg: '#4e95f3' },
  Defibrillator: { icon: HeartPulse, bg: '#f34e4e' },
  EmergencyClinic: { icon: Hospital, bg: '#e36414' },
  DistributionPoint: { icon: Package, bg: '#14e3d3' },
  PoliceStation: { icon: Shield, bg: '#e314a1' },
  Pharmacy: { icon: Plus, bg: '#14a114' },
  General: { icon: MapPin, bg: '#a1a114' },
  storage: { icon: Archive, bg: '#8e4ef3' },
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
  const updateLoc = () => emit('update:modelValue', getLocation())
  ;['moveend', 'zoomend', 'rotateend', 'pitchend'].forEach((e) => map.on(e, updateLoc))

  watch(() => [props.markers, props.storages, props.settings], redrawAll, { deep: true })
  watch(
    () => props.startSelection,
    () => {
      updateRouteStart()
      if (lastDest) fetchAndAnimateRoute(routeStart, lastDest)
    },
  )
})

onBeforeUnmount(() => {
  if (animationId) cancelAnimationFrame(animationId)
  map?.remove()
})

function getLocation() {
  const [lng, lat] = map.getCenter().toArray()
  return { lng, lat, bearing: map.getBearing(), pitch: map.getPitch(), zoom: map.getZoom() }
}

function getGeolocation() {
  if (!navigator.geolocation) {
    statusMessage.value = 'Geolokasjon støttes ikke'
    return
  }
  statusMessage.value = 'Henter posisjon...'
  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const { latitude, longitude } = pos.coords
      statusMessage.value = 'Posisjon funnet'
      if (!userMarker) {
        userMarker = new mapboxgl.Marker({ color: '#007cbf' })
          .setLngLat([longitude, latitude])
          .addTo(map)
      } else {
        userMarker.setLngLat([longitude, latitude])
      }
      if (props.startSelection === 'current') routeStart = { lng: longitude, lat: latitude }
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
    } else getGeolocation()
  } else {
    const st = props.storages.find((s) => String(s.id) === String(props.startSelection))
    if (st?.location?.longitude != null) {
      routeStart = { lng: st.location.longitude, lat: st.location.latitude }
    }
  }
}

function redrawAll() {
  if (!map?.isStyleLoaded()) return
  markerObjs.forEach((o) => o.marker.remove())
  markerObjs = []

  const s = props.settings,
    q = s.searchQuery?.toLowerCase() || ''

  // backend markers
  for (const m of props.markers) {
    const { type, location, name, description, capacity } = m
    if (
      (type === 'shelter' && !s.showShelters) ||
      (type === 'defibrillator' && !s.showDefibrillators) ||
      (type === 'emergencyClinic' && !s.showEmergencyClinics) ||
      (type === 'distributionPoint' && !s.showDistributionPoints) ||
      (type === 'policeStation' && !s.showPoliceStations) ||
      (type === 'pharmacy' && !s.showPharmacies) ||
      (type === 'general' && !s.showGeneral)
    )
      continue
    if (capacity != null && capacity < s.minCapacity) continue
    if (q && !`${name} ${description}`.toLowerCase().includes(q)) continue

    addOne(m, type, [location.longitude, location.latitude])
  }

  // storages
  if (s.showStorages) {
    for (const st of props.storages) {
      const { location, name, description } = st
      if (q && !`${name} ${description}`.toLowerCase().includes(q)) continue
      addOne(st, 'storage', [location.longitude, location.latitude])
    }
  }
}

const selectedEl = ref(null)

function addOne(item, type, coords) {
  const cfg = typeConfig[type] || typeConfig.general
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
  const vnode = createVNode(cfg.icon, { size: 20, color: '#fff' })
  render(vnode, wrap)

  wrap.addEventListener('mouseenter', () => (wrap.style.transform = 'scale(1.1)'))
  wrap.addEventListener('mouseleave', () => {
    if (el !== selectedEl.value) wrap.style.transform = 'scale(1)'
  })
  wrap.addEventListener('click', (e) => {
    e.stopPropagation()
    if (selectedEl.value && selectedEl.value !== el) {
      selectedEl.value.querySelector('div').style.transform = 'scale(1)'
    }
    wrap.style.transform = 'scale(1.2)'
    selectedEl.value = el
    showCard(item, coords)
  })

  el.appendChild(wrap)
  const marker = new mapboxgl.Marker(el).setLngLat(coords).addTo(map)
  markerObjs.push({ marker: marker, el })
}

function clearSelection() {
  if (selectedEl.value) {
    selectedEl.value.querySelector('div').style.transform = 'scale(1)'
    selectedEl.value = null
  }
  hideCard()
  clearRoute()
}

function showCard(item, coords) {
  popup.item = item
  popup.coords = coords
  popup.visible = true
}

function hideCard() {
  popup.visible = false
}

async function fetchAndAnimateRoute(start, end) {
  if (!start || !end) return
  const p = `${start.lng},${start.lat};${end[0]},${end[1]}`
  const url =
    `https://api.mapbox.com/directions/v5/mapbox/driving/${p}` +
    `?geometries=geojson&overview=full&access_token=${mapboxgl.accessToken}`
  const res = await fetch(url),
    js = await res.json()
  if (!js.routes?.length) return
  const geo = { type: 'Feature', geometry: js.routes[0].geometry }
  if (map.getSource('route')) {
    ;['route-bg', 'route-dashed'].forEach((id) => map.getLayer(id) && map.removeLayer(id))
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
    paint: { 'line-color': 'orange', 'line-width': 6, 'line-dasharray': dashSequence[0] },
  })
  animateDash()
}

function animateDash(t = 0) {
  const next = parseInt((t / 50) % dashSequence.length)
  if (next !== dashStep) {
    map.setPaintProperty('route-dashed', 'line-dasharray', dashSequence[next])
    dashStep = next
  }
  animationId = requestAnimationFrame(animateDash)
}

function clearRoute() {
  if (animationId) cancelAnimationFrame(animationId)
  if (map.getSource('route')) {
    ;['route-bg', 'route-dashed'].forEach((id) => map.getLayer(id) && map.removeLayer(id))
    map.removeSource('route')
  }
  dashStep = 0
}
</script>

<style scoped>
.marker {
}
</style>
