<script>
import 'mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl';

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

export default {
  props: ['modelValue'],
  mounted() {
    this.cardEl = this.$refs.props;
    let selectedEl = null;

    const {lng, lat, zoom, bearing, pitch} = this.modelValue;
    const map = new mapboxgl.Map({
      container: this.$refs.mapContainer,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [lng, lat],
      bearing, pitch, zoom
    });

    const updateLocation = () =>
      this.$emit('update:modelValue', this.getLocation());
    ['moveend', 'zoomend', 'rotateend', 'pitchend']
      .forEach(evt => map.on(evt, updateLocation));

    map.on('load', () => {
      fetch('/data/tilfluktsrom-4326.json')
        .then(res => res.json())
        .then(data => {
          data.features.forEach(feature => {
            const el = document.createElement('div');
            el.className = 'marker';
            el.style.cursor = 'pointer';

            const icon = document.createElement('div');
            icon.className = 'icon';
            icon.style.backgroundImage = 'url(/VaultDoor.svg)';
            icon.style.width = '52px';
            icon.style.height = '72px';
            icon.style.scale = '0.6'
            icon.style.cursor = 'pointer';
            icon.style.transition = 'transform 0.1s ease';

            // hover
            icon.addEventListener('mouseenter', () => {
              icon.style.transform = 'scale(1.1)';
            });
            icon.addEventListener('mouseleave', () => {
              if (el !== selectedEl) {
                icon.style.transform = 'scale(1)';
              }
            });

            icon.addEventListener('click', (e) => {
              e.stopPropagation();

              if (selectedEl && selectedEl !== el) {
                selectedEl.querySelector('.icon').style.transform = 'scale(1)';
              }
              selectedEl = el;
              icon.style.transform = 'scale(1.2)';
              this.showCard(feature.properties);
            });

            el.appendChild(icon);

            new mapboxgl.Marker(el)
              .setLngLat(feature.geometry.coordinates)
              .addTo(map);
          });
        });

      // deselect
      map.on('click', () => {
        if (selectedEl) {
          selectedEl.querySelector('.icon').style.transform = 'scale(1)';
          selectedEl = null;
        }
        this.hideCard();
      });
    });

    this.map = map;
  },
  unmounted() {
    this.map.remove();
  },
  methods: {
    getLocation() {
      return {
        ...this.map.getCenter(),
        bearing: this.map.getBearing(),
        pitch: this.map.getPitch(),
        zoom: this.map.getZoom()
      };
    },
    showCard(props) {
      this.cardEl.innerHTML = `
        <div class="map-overlay-inner">
          <code>${props.adresse || props.name}</code><hr>
          ${Object.entries(props)
        .map(([k, v]) => `<li><b>${k}</b>: ${v}</li>`)
        .join('')}
        </div>`;
      this.cardEl.style.display = 'block';
    },
    hideCard() {
      this.cardEl.style.display = 'none';
    }
  }
}
</script>

<template>
  <div ref="mapContainer" class="map-container flex-1 w-full rounded-xl shadow"></div>
  <div ref="props"
       class="w-96 h-96 bg-white hidden p-4 absolute right-4 top-14
              overflow-hidden rounded-md"></div>
</template>

<style scoped>
.icon {
  background-size: contain;
  background-repeat: no-repeat;
  transition: transform 0.1s ease;
}
</style>
