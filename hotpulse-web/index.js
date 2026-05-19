/**
 * 路由配置文件
 * 管理所有页面的跳转规则
 */
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SearchView from '../views/SearchView.vue'
import FavoriteView from '../views/FavoriteView.vue'
import ProfileView from '../views/ProfileView.vue'
import AuthView from '../views/AuthView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/auth' },
    { path: '/home', component: HomeView },
    { path: '/search', component: SearchView },
    { path: '/favorite', component: FavoriteView },
    { path: '/profile', component: ProfileView },
    { path: '/auth', component: AuthView },
  ],
})

const routeLabelMap = {
  '/home': '研究概览',
  '/search': '研究助手',
  '/favorite': '知识库',
  '/profile': '工作台',
}

router.beforeEach((to) => {
  const token = localStorage.getItem('hp_token')
  const isAuthPage = to.path === '/auth'

  if (!token && !isAuthPage) {
    return { path: '/auth', query: { redirect: to.fullPath } }
  }

  if (token && isAuthPage) {
    return '/home'
  }

  if (token && routeLabelMap[to.path]) {
    const recent = JSON.parse(localStorage.getItem('hp_recent_routes') || '[]')
    const filtered = recent.filter((item) => item.path !== to.path)
    filtered.unshift({
      path: to.path,
      label: routeLabelMap[to.path],
      time: Date.now(),
    })
    localStorage.setItem('hp_recent_routes', JSON.stringify(filtered.slice(0, 5)))
  }

  return true
})

export default router
