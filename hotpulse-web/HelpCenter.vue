<template>
  <div class="hc">
    <div class="hc-hd">
      <h3>帮助中心</h3>
    </div>

    <div class="hc-list">
      <div v-for="(item, i) in faq" :key="i" class="hc-item" :class="{ open: openIdx === i }">
        <button class="hc-q" @click="openIdx = openIdx === i ? -1 : i">
          <span>{{ item.q }}</span>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline :points="openIdx === i ? '18 15 12 9 6 15' : '6 9 12 15 18 9'"/></svg>
        </button>
        <div v-show="openIdx === i" class="hc-a">
          <p>{{ item.a }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const openIdx = ref(-1)

const faq = [
  {
    q: '如何使用小热研究助手？',
    a: '在研究助手页面输入你想探索的问题，小热会自动拆解研究路径、分析相关资料、给出研究结论和下一步建议。你也可以从研究概览选择一个热点话题直接开始。',
  },
  {
    q: '如何收藏和管理研究资料？',
    a: '在研究概览的最新资料中，每条资料右侧有「收藏」和「稍后研究」按钮。点击收藏后，资料会保存到知识库；点击稍后研究，可以在知识库的「稍后研究」标签中查看。',
  },
  {
    q: '如何更换头像和修改个人信息？',
    a: '在工作台页面，点击头像即可上传新头像。你还可以在同一页面修改用户名和密码。所有修改会立即生效。',
  },
  {
    q: '数据从哪里来？',
    a: '研究资料来自后端数据库。管理员通过后端接口录入新闻、热榜等数据，前端实时读取并展示。AI 研究助手会优先使用这些真实数据进行研究分析。',
  },
]
</script>

<style scoped>
.hc-hd { margin-bottom: 12px; }
.hc-hd h3 { font-size: 15px; font-weight: 700; }

.hc-list { display: grid; gap: 2px; }

.hc-item { border-radius: var(--r); overflow: hidden; }

.hc-q {
  width: 100%;
  text-align: left;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  font-size: 13px;
  font-weight: 600;
  color: var(--tx);
  transition: all .15s var(--e);
}
.hc-q:hover { background: var(--bg-hover); }
.hc-q svg { flex-shrink: 0; color: var(--tx-3); }

.hc-item.open .hc-q { color: var(--brand); background: var(--brand-50); }

.hc-a {
  padding: 0 14px 14px;
  font-size: 12px;
  line-height: 1.8;
  color: var(--tx-2);
}
</style>