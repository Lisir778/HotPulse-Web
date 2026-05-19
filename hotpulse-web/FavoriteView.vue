<template>
  <div class="rc-page">
    <header class="pg-hd">
      <div>
        <div class="kicker">知识库</div>
        <h1>研究收藏</h1>
        <p>收藏的资料与稍后研究的内容都会在这里查看。</p>
      </div>
      <div class="pg-stat-row">
        <div class="pg-stat"><span>总数</span><strong>{{ stats.total }}</strong></div>
        <div class="pg-stat"><span>当前</span><strong>{{ activeTab === 'later' ? '稍后研究' : activeTab }}</strong></div>
      </div>
    </header>

    <div v-if="errorText" class="err">{{ errorText }}</div>

    <!-- Filter bar -->
    <div class="card filter-bar">
      <button v-for="t in tabs" :key="t" class="pill" :class="{ on: activeTab === t }" @click="activeTab = t">{{ t }}</button>
      <button class="btn btn--sec btn--sm" style="margin-left:auto" @click="clearFilter">重置</button>
    </div>

    <!-- List -->
    <div class="card">
      <!-- Loading -->
      <div v-if="loading" class="empty">
        <div class="empty-icon"><svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg></div>
        <p>加载中...</p>
      </div>

      <!-- Favorites empty -->
      <div v-else-if="activeTab !== 'later' && filtered.length === 0" class="empty">
        <div class="empty-icon"><svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg></div>
        <p>暂无收藏</p>
      </div>

      <!-- Later empty -->
      <div v-else-if="activeTab === 'later' && laterItems.length === 0" class="empty">
        <div class="empty-icon"><svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"/><polyline points="12 6 12 12 16 14"/></svg></div>
        <p>暂无稍后研究</p>
        <p style="font-size:12px;color:var(--tx-3);margin-top:4px">在首页对资料点击「稍后研究」即可加入这里</p>
      </div>

      <!-- Later items -->
      <div v-else-if="activeTab === 'later'" class="fav-list">
        <div v-for="(item, i) in laterItems" :key="item.newsId" class="fav-row">
          <div class="fav-icon" style="background:#eef2ff;color:#4f46e5">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"/><polyline points="12 6 12 12 16 14"/></svg>
          </div>
          <div class="fav-body">
            <div class="fav-title">{{ item.title }}</div>
            <div class="fav-meta">稍后研究 · {{ formatTime(item.savedAt) }}</div>
          </div>
          <button class="row-btn del-btn" @click="removeLater(i)" title="移除">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>
      </div>

      <!-- Favorites -->
      <div v-else class="fav-list">
        <div v-for="item in filtered" :key="item.id" class="fav-row">
          <div class="fav-icon" :style="{ background: item.iconBg, color: item.iconColor }">{{ item.icon }}</div>
          <div class="fav-body">
            <div class="fav-title">{{ item.title }}</div>
            <div class="fav-meta">{{ item.source }} · {{ item.savedAt }}</div>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Tools -->
    <div class="tools">
      <div class="card tool-panel">
        <div class="kicker" style="margin-bottom:12px">分布统计</div>
        <div class="tool-row"><span>资料</span><strong>{{ countByType('news') }}</strong></div>
        <div class="tool-row"><span>研究结果</span><strong>{{ countByType('research') }}</strong></div>
        <div class="tool-row"><span>稍后研究</span><strong>{{ laterItems.length }}</strong></div>
        <div class="tool-row"><span>其他</span><strong>{{ countByType('other') }}</strong></div>
      </div>
      <div class="card tool-panel">
        <div class="kicker" style="margin-bottom:12px">快速筛选</div>
        <button class="tool-btn" @click="activeTab = 'news'">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
          只看资料
        </button>
        <button class="tool-btn" @click="activeTab = 'research'">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
          只看研究结果
        </button>
        <button class="tool-btn" @click="activeTab = 'later'">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"/><polyline points="12 6 12 12 16 14"/></svg>
          稍后研究
        </button>
        <button class="tool-btn" @click="reloadFavorites">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>
          重新加载
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { getCurrentUser } from '../api/user'
import { getNewsDetail } from '../api/news'
import { listFavorites } from '../api/favorite'

const STORAGE_KEY = 'hp_save_later'

const tabs = ['全部', 'news', 'research', 'later', 'other']
const activeTab = ref('全部')
const loading = ref(false)
const errorText = ref('')
const favorites = ref([])
const laterItems = ref([])

const stats = computed(() => ({
  total: favorites.value.length + laterItems.value.length,
}))

const normalizeType = v => {
  if (!v) return 'other'
  const l = String(v).toLowerCase()
  if (l === 'news' || l === 'article') return 'news'
  if (l === 'research' || l === 'report' || l === 'note') return 'research'
  return 'other'
}

const enrichFavorites = async items => {
  const e = await Promise.all(items.map(async (item, i) => {
    const nid = item.newsId
    let news = null
    if (nid) { try { news = await getNewsDetail(nid) } catch { news = null } }
    const type = normalizeType(item.type)
    const icons = {
      news: { icon: '✦', bg: '#eef2ff', c: '#4f46e5' },
      research: { icon: '研', bg: '#ecfdf5', c: '#065f46' },
      other: { icon: '◎', bg: '#fffbeb', c: '#92400e' },
    }
    const ico = icons[type] || icons.other
    return {
      id: item.id || `${i}-${nid || 'fav'}`,
      type,
      title: news?.title || `收藏 #${nid || item.id}`,
      source: news?.source || item.type || '收藏',
      savedAt: item.createdAt ? new Date(item.createdAt).toLocaleString() : '刚刚',
      desc: news?.summary || '该收藏目前只保留了基础记录。',
      icon: ico.icon, iconBg: ico.bg, iconColor: ico.c,
    }
  }))
  favorites.value = e
}

const loadLater = () => {
  try {
    laterItems.value = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]')
  } catch { laterItems.value = [] }
}

const removeLater = (index) => {
  laterItems.value.splice(index, 1)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(laterItems.value))
}

const loadCurrentUserId = async () => {
  const cached = localStorage.getItem('hp_user')
  if (cached) { try { const u = JSON.parse(cached); if (u?.id) return u.id } catch { /* */ } }
  const user = await getCurrentUser()
  if (user?.id) { localStorage.setItem('hp_user', JSON.stringify(user)); return user.id }
  throw new Error('无法获取当前用户信息')
}

const loadFavorites = async () => {
  loading.value = true; errorText.value = ''
  try {
    const userId = await loadCurrentUserId()
    const records = await listFavorites(userId)
    await enrichFavorites(Array.isArray(records) ? records : [])
  } catch (err) { favorites.value = []; errorText.value = err?.message || '加载失败' }
  finally { loading.value = false }
}

const filtered = computed(() =>
  activeTab.value === '全部' ? favorites.value : favorites.value.filter(i => i.type === activeTab.value)
)

const countByType = t => favorites.value.filter(i => i.type === t).length
const clearFilter = () => { activeTab.value = '全部' }
const reloadFavorites = () => { loadFavorites(); loadLater() }

const formatTime = v => v ? new Date(v).toLocaleString() : ''

onMounted(() => { loadFavorites(); loadLater() })
</script>

<style scoped>
.pg-hd {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}
.pg-hd h1 { font-size: clamp(22px, 3vw, 30px); font-weight: 800; letter-spacing: -.02em; margin: 4px 0 6px; }
.pg-hd > div > p { color: var(--tx-2); font-size: 13px; }

.pg-stat-row { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; flex-shrink: 0; }
.pg-stat { padding: 12px 16px; border-radius: var(--r); background: var(--bg-active); display: grid; gap: 2px; }
.pg-stat span { font-size: 11px; color: var(--tx-3); }
.pg-stat strong { font-size: 18px; font-weight: 700; }

.filter-bar {
  display: flex;
  gap: 4px;
  padding: 6px 8px;
  align-items: center;
}
.pill {
  padding: 5px 12px;
  border-radius: var(--r-full);
  font-size: 12px;
  font-weight: 600;
  color: var(--tx-2);
  transition: all .15s var(--e);
}
.pill:hover { background: var(--bg-hover); }
.pill.on { background: var(--brand); color: #fff; }

/* List */
.fav-list { display: grid; }
.fav-row {
  display: grid;
  grid-template-columns: 40px minmax(0, 1fr) auto;
  gap: 14px;
  align-items: start;
  padding: 14px 20px;
  border-bottom: 1px solid var(--b-sub);
}
.fav-row:last-child { border-bottom: none; }
.fav-icon {
  width: 40px; height: 40px;
  border-radius: var(--r);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}
.fav-title { font-size: 14px; font-weight: 600; margin-bottom: 2px; }
.fav-meta { font-size: 11px; color: var(--tx-3); margin-bottom: 6px; }
.fav-body p { font-size: 12px; color: var(--tx-2); line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }

.row-btn {
  width: 28px; height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--tx-3);
  transition: all .15s var(--e);
}
.row-btn:hover { background: var(--bg-hover); }
.del-btn:hover { color: var(--danger); background: var(--danger-50); }

/* Empty */
.empty { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 64px 20px; color: var(--tx-3); font-size: 13px; }
.empty-icon { width: 48px; height: 48px; display: flex; align-items: center; justify-content: center; border-radius: var(--r-lg); background: var(--bg-active); color: var(--tx-3); }

/* Tools */
.tools { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.tool-panel { padding: 18px; }
.tool-row { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid var(--b-sub); font-size: 13px; }
.tool-row:last-child { border-bottom: none; }
.tool-row span { color: var(--tx-2); }
.tool-row strong { font-weight: 700; }

.tool-btn {
  width: 100%;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: var(--r);
  font-size: 13px;
  color: var(--tx-2);
  transition: all .15s var(--e);
}
.tool-btn:hover { background: var(--bg-hover); color: var(--brand); }
.tool-btn svg { flex-shrink: 0; }

.err { padding: 10px 14px; border-radius: var(--r); background: var(--danger-50); color: var(--danger); font-size: 13px; }

@media (max-width: 600px) { .pg-hd { flex-direction: column; } .tools { grid-template-columns: 1fr; } }
</style>