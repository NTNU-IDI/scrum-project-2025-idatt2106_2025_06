<script>
import 'mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl';

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

export default {
  props: ['modelValue'],
  mounted() {
    this.cardEl = this.$refs.props;

    const {lng, lat, zoom, bearing, pitch} = this.modelValue;
    const map = new mapboxgl.Map({
      container: this.$refs.mapContainer,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [lng, lat],
      bearing, pitch, zoom
    });

    const updateLocation = () =>
      this.$emit('update:modelValue', this.getLocation());
    map.on('moveend', updateLocation);
    map.on('zoomend', updateLocation);
    map.on('rotateend', updateLocation);
    map.on('pitchend', updateLocation);

    map.on('load', () => {
      map.addSource('tilfluktsrom', {
        type: 'geojson',
        data: '/data/tilfluktsrom-4326.json',
        generateId: true
      });

      map.addLayer({
        id: 'tilfluktsrom',
        type: 'circle',
        source: 'tilfluktsrom',
        paint: {
          'circle-color': [
            'case',
            ['boolean', ['feature-state', 'selected'], false], '#f00',
            '#4264fb'
          ],
          'circle-radius': [
            'case',
            ['boolean', ['feature-state', 'selected'], false], 12,
            ['boolean', ['feature-state', 'highlight'], false], 12,
            8
          ],
          'circle-stroke-width': 4,
          'circle-stroke-color': '#fff'
        }
      });

      let selectedId = null;
      let hoveredId = null;

      // click on a feature to select
      map.on('click', 'tilfluktsrom', (e) => {
        const feature = e.features[0];
        if (selectedId !== null) {
          map.setFeatureState(
            {source: 'tilfluktsrom', id: selectedId},
            {selected: false}
          );
        }
        selectedId = feature.id;
        map.setFeatureState(
          {source: 'tilfluktsrom', id: selectedId},
          {selected: true}
        );
        this.showCard(feature.properties);
      });

      // click on blank map to clear
      map.on('click', (e) => {
        const hits = map.queryRenderedFeatures(e.point, {
          layers: ['tilfluktsrom']
        });
        if (!hits.length && selectedId !== null) {
          map.setFeatureState(
            {source: 'tilfluktsrom', id: selectedId},
            {selected: false}
          );
          selectedId = null;
          this.hideCard();
        }
      });

      // hover
      map.on('mousemove', 'tilfluktsrom', (e) => {
        map.getCanvas().style.cursor = 'pointer';
        const featureId = e.features[0].id;

        if (hoveredId !== null && hoveredId !== featureId) {
          map.setFeatureState(
            {source: 'tilfluktsrom', id: hoveredId},
            {highlight: false}
          );
        }

        hoveredId = featureId;
        map.setFeatureState(
          {source: 'tilfluktsrom', id: hoveredId},
          {highlight: true}
        );
      });

      map.on('mouseleave', 'tilfluktsrom', () => {
        map.getCanvas().style.cursor = '';
        if (hoveredId !== null) {
          map.setFeatureState(
            {source: 'tilfluktsrom', id: hoveredId},
            {highlight: false}
          );
          hoveredId = null;
        }
      });
    });

    this.map = map;
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
};
</script>

<template>
  <div ref="mapContainer"
       class="map-container flex-1 w-full rounded-xl shadow"></div>
  <div ref="props"
       class="w-96 h-96 bg-white hidden p-4 absolute right-4 top-14
              overflow-hidden rounded-md"></div>
</template>
