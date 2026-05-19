<!-- 左侧侧边栏导航：菜单、路由跳转 -->
<template>
  <aside class="sb">
    <div class="sb-top">
      <!-- Logo -->
      <RouterLink to="/home" class="sb-logo">
        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="var(--brand)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
        </svg>
        <span>Hotpulse</span>
      </RouterLink>

      <!-- Nav -->
      <nav class="sb-nav">
        <RouterLink v-for="item in nav" :key="item.to" :to="item.to" class="sb-link" active-class="is-on">
          <span class="sb-icon" v-html="item.icon"></span>
          <span class="sb-label">{{ item.label }}</span>
          <span class="sb-desc">{{ item.desc }}</span>
        </RouterLink>
      </nav>
    </div>

    <!-- User -->
    <div class="sb-user">
      <RouterLink to="/profile" class="sb-link sb-user-link" active-class="is-on">
        <Avatar :src="userAvatar" :name="username" size="sm" style="grid-row:1/3" />
        <span class="sb-label">{{ username }}</span>
        <span class="sb-desc">工作台</span>
      </RouterLink>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import Avatar from './Avatar.vue'

const nav = [
  {
    to: '/home', label: '研究概览', desc: 'Trends & tasks',
    icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>'
  },
  {
    to: '/search', label: '研究助手', desc: 'AI Research',
    icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>'
  },
  {
    to: '/favorite', label: '知识库', desc: 'Saved research',
    icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>'
  },
]

const username = computed(() => {
  try {
    const u = JSON.parse(localStorage.getItem('hp_user') || '{}')
    return u.username || '用户'
  } catch { return '用户' }
})

const userAvatar = computed(() => {
  try {
    const u = JSON.parse(localStorage.getItem('hp_user') || '{}')
    return u.avatar || ''
  } catch { return '' }
})
</script>

<style scoped>
.sb {
  width: 180px;
  min-height: 100vh;
  background: #0f0f1a;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px 12px;
  flex-shrink: 0;
  position: sticky;
  top: 0;
}

.sb-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  margin-bottom: 32px;
  color: #fff;
  font-size: 18px;
  font-weight: 800;
  letter-spacing: -.02em;
}

.sb-nav {
  display: grid;
  gap: 2px;
}

.sb-link {
  display: grid;
  grid-template-columns: auto 1fr;
  grid-template-rows: auto auto;
  column-gap: 12px;
  padding: 10px 12px;
  border-radius: var(--r);
  transition: all .15s var(--e);
  color: #9ca3af;
  text-decoration: none;
}

.sb-link:hover {
  background: rgba(255,255,255,.06);
  color: #e5e7eb;
}

.sb-link.is-on {
  background: rgba(79,70,229,.15);
  color: #a5b4fc;
}

.sb-icon {
  grid-row: 1 / 3;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
}

.sb-label {
  font-size: 13px;
  font-weight: 600;
  line-height: 1.3;
}

.sb-desc {
  font-size: 10px;
  opacity: .5;
  font-weight: 400;
}

.sb-link.is-on .sb-desc {
  opacity: .7;
}

/* User */
.sb-user {
  border-top: 1px solid rgba(255,255,255,.08);
  padding-top: 12px;
}


@media (max-width: 768px) {
  .sb {
    width: 68px;
    padding: 16px 8px;
  }
  .sb-logo span { display: none; }
  .sb-logo { justify-content: center; }
  .sb-label, .sb-desc { display: none; }
  .sb-link {
    grid-template-columns: 1fr;
    justify-items: center;
    padding: 10px;
  }
  .sb-icon { grid-row: auto; }
  .sb-avatar { grid-row: auto; }
  .sb-user-link { justify-items: center; }
}

@media (max-width: 480px) {
  .app:not(.app--auth) {
    flex-direction: column-reverse;
  }
  .sb {
    width: 100%;
    min-height: auto;
    position: fixed;
    bottom: 0;
    top: auto;
    z-index: 100;
    flex-direction: row;
    padding: 0;
  }
  .sb-top {
    display: flex;
    width: 100%;
  }
  .sb-logo { display: none; }
  .sb-nav {
    display: flex;
    width: 100%;
    justify-content: space-around;
  }
  .sb-link {
    grid-template-columns: 1fr;
    justify-items: center;
    padding: 8px;
    gap: 4px;
  }
  .sb-label {
    display: block;
    font-size: 10px;
  }
  .sb-desc { display: none; }
  .sb-user { display: none; }
  .main { padding-bottom: 64px; }
}
</style>