/**
 * 项目入口文件
 * 挂载Vue、路由、全局配置
 */
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)

app.mount('#app')
