<template>
  <div class="nc">
    <div class="nc-hd">
      <h3>通知中心</h3>
      <div class="nc-hd-r">
        <span v-if="unread" class="nc-badge">{{ unread }}</span>
        <button class="btn btn--sec btn--sm" @click="markAllRead">全部已读</button>
      </div>
    </div>

    <div v-if="list.length" class="nc-list">
      <div
        v-for="item in list"
        :key="item.id"
        class="nc-item"
        :class="{ unread: !item.read }"
        @click="markRead(item)"
      >
        <span class="nc-dot" :class="item.type"></span>
        <div class="nc-body">
          <div class="nc-title">{{ item.title }}</div>
          <div class="nc-desc">{{ item.desc }}</div>
          <div class="nc-time">{{ item.time }}</div>
        </div>
      </div>
    </div>

    <div v-else class="nc-empty">
      <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
      <p>暂无通知</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'

const STORAGE_KEY = 'hp_notifications'
const list = ref([])

const unread = computed(() => list.value.filter(i => !i.read).length)

// Seed some demo notifications if empty
const seedDefaults = () => {
  const existing = localStorage.getItem(STORAGE_KEY)
  if (existing) return
  const now = Date.now()
  const seeds = [
    { id: 1, type: 'info', title: '欢迎使用 Hotpulse', desc: 'AI 研究助手已就绪，开始你的第一次研究吧。', time: new Date(now - 3600000).toLocaleString(), read: false },
    { id: 2, type: 'success', title: '数据同步完成', desc: '研究资料已从数据库成功同步。', time: new Date(now - 86400000).toLocaleString(), read: false },
    { id: 3, type: 'warn', title: '提示：完善个人资料', desc: '设置头像和用户名可以让你的工作台更个性化。', time: new Date(now - 172800000).toLocaleString(), read: false },
  ]
  localStorage.setItem(STORAGE_KEY, JSON.stringify(seeds))
  list.value = seeds
}

const markRead = (item) => {
  item.read = true
  localStorage.setItem(STORAGE_KEY, JSON.stringify(list.value))
}

const markAllRead = () => {
  list.value.forEach(i => i.read = true)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(list.value))
}

onMounted(() => {
  try {
    const cached = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]')
    list.value = cached.length ? cached : []
    if (!cached.length) seedDefaults()
  } catch { list.value = [] }
})
</script>

<style scoped>
.nc-hd {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.nc-hd h3 { font-size: 15px; font-weight: 700; }
.nc-hd-r { display: flex; align-items: center; gap: 8px; }

.nc-badge {
  padding: 1px 8px;
  border-radius: var(--r-full);
  background: var(--danger);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
}

.nc-list { display: grid; }

.nc-item {
  display: flex;
  gap: 10px;
  padding: 12px 14px;
  border-radius: var(--r);
  cursor: pointer;
  transition: all .15s var(--e);
}
.nc-item:hover { background: var(--bg-hover); }
.nc-item.unread { background: var(--brand-50); }

.nc-dot {
  width: 6px; height: 6px;
  border-radius: 50%;
  margin-top: 7px;
  flex-shrink: 0;
  background: var(--tx-3);
}
.nc-dot.info { background: var(--brand); }
.nc-dot.success { background: var(--success); }
.nc-dot.warn { background: var(--accent); }
.nc-item.unread .nc-dot { box-shadow: 0 0 0 4px rgba(79,70,229,.12); }

.nc-body { min-width: 0; }
.nc-title { font-size: 13px; font-weight: 600; margin-bottom: 2px; }
.nc-desc { font-size: 12px; color: var(--tx-2); line-height: 1.5; margin-bottom: 4px; }
.nc-time { font-size: 10px; color: var(--tx-3); }

.nc-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 32px 0;
  color: var(--tx-3);
  font-size: 13px;
}
</style>