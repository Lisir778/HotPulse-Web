<template>
  <div class="trend-chart" v-show="ready">
    <div class="chart-hd">
      <h3>近 7 天热度趋势</h3>
      <span class="chart-sub">{{ topLabel }}</span>
    </div>
    <div ref="chart" class="chart-canvas"></div>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  items: { type: Array, default: () => [] },
})

const ready = ref(false)
const chart = ref(null)
const topLabel = ref('')
let instance = null

// Generate synthetic 7-day trend from current heat value
const genTrend = (now, i) => {
  const days = []
  for (let d = 6; d >= 0; d--) {
    const date = new Date()
    date.setDate(date.getDate() - d)
    const base = now * (0.55 + Math.random() * 0.3)
    const trend = d === 0 ? now : Math.round(base - (6 - d) * (now * 0.06) - Math.random() * now * 0.15)
    days.push(Math.max(0, trend))
  }
  return days
}

const dateLabels = () => {
  const labels = []
  for (let d = 6; d >= 0; d--) {
    const date = new Date()
    date.setDate(date.getDate() - d)
    labels.push(`${date.getMonth() + 1}/${date.getDate()}`)
  }
  return labels
}

const render = () => {
  if (!instance || !props.items.length) return

  const top = props.items.slice(0, 5)
  topLabel.value = top.map(i => i.title?.slice(0, 6)).join(' · ')

  const dates = dateLabels()
  const colors = ['#4f46e5', '#f59e0b', '#10b981', '#ef4444', '#7c3aed']

  const series = top.map((item, i) => ({
    name: item.title?.slice(0, 10) || `热点${i + 1}`,
    type: 'line',
    data: genTrend(item.heat || 100, i),
    smooth: true,
    symbol: 'circle',
    symbolSize: 4,
    lineStyle: { width: 2, color: colors[i] },
    itemStyle: { color: colors[i] },
    emphasis: { focus: 'series' },
  }))

  instance.setOption({
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#fff',
      borderColor: '#e2e8f0',
      textStyle: { color: '#0f172a', fontSize: 12 },
      boxShadow: '0 4px 12px rgba(0,0,0,.08)',
    },
    legend: { show: false },
    grid: {
      left: 40,
      right: 12,
      top: 12,
      bottom: 24,
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { lineStyle: { color: '#e2e8f0' } },
      axisTick: { show: false },
      axisLabel: { color: '#94a3b8', fontSize: 10 },
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#f1f5f9' } },
      axisLabel: {
        color: '#94a3b8',
        fontSize: 10,
        formatter: v => v >= 1000 ? (v / 1000).toFixed(1) + 'k' : v,
      },
    },
    series,
  }, true)

  ready.value = true
}

const resize = () => instance?.resize()

onMounted(() => {
  instance = echarts.init(chart.value)
  window.addEventListener('resize', resize)
  render()
})

watch(() => props.items, render, { deep: true })

onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  instance?.dispose()
})
</script>

<style scoped>
.trend-chart {
  margin-top: 0;
}

.chart-hd {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 2px;
  padding: 0 4px;
}

.chart-hd h3 {
  font-size: 13px;
  font-weight: 700;
}

.chart-sub {
  font-size: 10px;
  color: var(--tx-3);
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chart-canvas {
  width: 100%;
  height: 200px;
}
</style>