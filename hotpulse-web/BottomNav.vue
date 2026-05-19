<!-- 底部导航栏：移动端快捷操作 -->
<template>
  <header class="nav">
    <div class="nav-inner">
      <RouterLink to="/home" class="nav-brand">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="var(--brand)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
        </svg>
        <span>Hotpulse</span>
      </RouterLink>

      <nav class="nav-links">
        <RouterLink v-for="l in links" :key="l.to" :to="l.to" class="nav-link" active-class="is-active">
          <span class="nav-icon" v-html="l.icon"></span>
          <span>{{ l.label }}</span>
        </RouterLink>
      </nav>

      <div class="nav-end">
        <RouterLink to="/profile" class="avatar-dot" title="工作台">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </RouterLink>
      </div>
    </div>
  </header>
</template>

<script setup>
const links = [
  { to: '/home', label: '控制台', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>' },
  { to: '/search', label: '研究台', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>' },
  { to: '/favorite', label: '知识库', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>' },
]
</script>

<style scoped>
.nav {
  position: sticky;
  top: 0;
  z-index: 200;
  border-bottom: 1px solid var(--border);
  background: rgba(255,255,255,.82);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

.nav-inner {
  max-width: 1100px;
  margin: 0 auto;
  height: 56px;
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 0 24px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 800;
  letter-spacing: -.01em;
  color: var(--text);
  flex-shrink: 0;
}

.nav-links {
  display: flex;
  gap: 2px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: var(--r-md);
  font-size: 13px;
  font-weight: 600;
  color: var(--text-2);
  transition: all var(--fast) var(--ease);
}

.nav-link:hover { background: var(--surface-hover); color: var(--text); }

.nav-link.is-active {
  background: var(--brand-50);
  color: var(--brand);
}

.nav-icon {
  display: flex;
  align-items: center;
}

.nav-end {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.avatar-dot {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-3);
  transition: all var(--fast) var(--ease);
}

.avatar-dot:hover {
  background: var(--surface-hover);
  color: var(--text-2);
}

@media (max-width: 640px) {
  .nav-inner { padding: 0 16px; }

  .nav-link span:last-child { display: none; }
  .nav-link { padding: 8px; }
}
</style>