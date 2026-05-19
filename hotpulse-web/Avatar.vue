<template>
  <div
    class="ava"
    :class="[size]"
    :style="{ background: bg }"
    :title="name"
  >
    <img
      v-if="src && !imgErr"
      :src="src"
      :alt="name"
      class="ava-img"
      @error="imgErr = true"
    />
    <span v-else class="ava-text">{{ initial }}</span>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  src: { type: String, default: '' },
  name: { type: String, default: '?' },
  size: { type: String, default: 'md' },
})

const imgErr = ref(false)

const initial = computed(() => {
  const n = (props.name || '?').trim()
  return n.slice(0, 1).toUpperCase()
})

// Consistent color per name
const colors = [
  'linear-gradient(135deg, #4f46e5, #7c3aed)',
  'linear-gradient(135deg, #f59e0b, #ef4444)',
  'linear-gradient(135deg, #10b981, #059669)',
  'linear-gradient(135deg, #ef4444, #ec4899)',
  'linear-gradient(135deg, #7c3aed, #a855f7)',
  'linear-gradient(135deg, #2563eb, #06b6d4)',
  'linear-gradient(135deg, #ea580c, #f59e0b)',
  'linear-gradient(135deg, #0891b2, #10b981)',
]

const bg = computed(() => {
  if (props.src && !imgErr.value) return 'transparent'
  let hash = 0
  for (let i = 0; i < (props.name || '').length; i++) {
    hash = props.name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
})
</script>

<style scoped>
.ava {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--r);
  color: #fff;
  font-weight: 700;
  flex-shrink: 0;
  overflow: hidden;
  user-select: none;
}

.ava.sm { width: 24px; height: 24px; font-size: 10px; border-radius: 6px; }
.ava.md { width: 32px; height: 32px; font-size: 13px; }
.ava.lg { width: 52px; height: 52px; font-size: 22px; border-radius: var(--r-lg); }
.ava.xl { width: 72px; height: 72px; font-size: 28px; border-radius: var(--r-lg); }

.ava-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.ava-text {
  line-height: 1;
}
</style>