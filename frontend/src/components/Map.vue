<script>
import '/node_modules/mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl'

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN

export default {
  props: ['modelValue'],
  mounted() {
    const { lng, lat, zoom, bearing, pitch } = this.modelValue;

    const map = new mapboxgl.Map({
      container: this.$refs.mapContainer,
      style: "mapbox://styles/mapbox/streets-v12",
      center: [lng, lat],
      bearing,
      pitch,
      zoom,
    });

    const updateLocation = () =>
      this.$emit('update:modelValue', this.getLocation());

    map.on('move', updateLocation);
    map.on('zoom', updateLocation);
    map.on('rotate', updateLocation);
    map.on('pitch', updateLocation);

    this.map = map;
  },
  unmounted() {
    this.map.remove();
    this.map = null;
  },
  methods: {
    getLocation() {
      return {
        ...this.map.getCenter(),
        bearing: this.map.getBearing(),
        pitch: this.map.getPitch(),
        zoom: this.map.getZoom(),
      }
    }
  },
}
</script>

<template>
  <div ref="mapContainer" class="map-container flex-1 w-full"></div>
</template>

<style scoped>

</style>
