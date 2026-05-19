<!-- 登录/注册页面：用户身份验证 -->
<template>
  <div class="auth-page">
    <!-- Left: brand -->
    <div class="auth-brand">
      <div class="brand-bg">
        <div class="glow g1"></div>
        <div class="glow g2"></div>
        <div class="grid-pattern"></div>
      </div>
      <div class="brand-inner">
        <div class="brand-logo">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
          </svg>
        </div>
        <h1>Hotpulse</h1>
        <p class="brand-tagline">AI 驱动的热点研究助手</p>
        <p class="brand-desc">从海量数据中提炼洞察，用 AI 拆解研究路径，保存你的每一次思考。</p>
        <div class="brand-features">
          <div class="bf">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
            智能检索与分析
          </div>
          <div class="bf">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
            结构化研究报告
          </div>
          <div class="bf">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
            个人知识库管理
          </div>
        </div>
      </div>
    </div>

    <!-- Right: form -->
    <div class="auth-form">
      <div class="form-card">
        <div class="form-tabs">
          <button :class="{ on: mode === 'login' }" @click="mode = 'login'">登录</button>
          <button :class="{ on: mode === 'register' }" @click="mode = 'register'">注册</button>
        </div>

        <form @submit.prevent="submit">
          <label class="field">
            <span>用户名</span>
            <input v-model.trim="form.username" autocomplete="username" placeholder="你的用户名" />
          </label>
          <label class="field">
            <span>密码</span>
            <div class="pwd">
              <input v-model="form.password" :type="showPw ? 'text' : 'password'" autocomplete="current-password" placeholder="你的密码" />
              <button type="button" class="pwd-btn" @click="showPw = !showPw">{{ showPw ? '隐藏' : '显示' }}</button>
            </div>
          </label>
          <button class="submit-btn" :disabled="loading" type="submit">
            {{ loading ? '处理中...' : mode === 'login' ? '登录' : '注册新账号' }}
          </button>
        </form>

        <p class="switch">
          {{ mode === 'login' ? '还没有账号？' : '已有账号？' }}
          <button @click="mode = mode === 'login' ? 'register' : 'login'">
            {{ mode === 'login' ? '立即注册' : '去登录' }}
          </button>
        </p>

        <div v-if="message" class="msg" :class="messageType">{{ message }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loginUser, registerUser } from '../api/user'

const router = useRouter()
const route = useRoute()

const mode = ref('login')
const loading = ref(false)
const showPw = ref(false)
const message = ref('')
const messageType = ref('info')
const form = reactive({ username: '', password: '' })

const showMsg = (text, type = 'info') => { message.value = text; messageType.value = type }

const nextPath = () => {
  const r = route.query.redirect
  return typeof r === 'string' && r.startsWith('/') ? r : '/home'
}

const submit = async () => {
  if (!form.username || !form.password) { showMsg('请输入用户名和密码', 'error'); return }
  loading.value = true
  try {
    if (mode.value === 'login') {
      const data = await loginUser(form)
      localStorage.setItem('hp_token', data.token)
      localStorage.setItem('hp_user', JSON.stringify(data))
      window.dispatchEvent(new Event('hp-auth-changed'))
      await router.replace(nextPath())
      return
    }
    await registerUser(form)
    showMsg('注册成功，请登录', 'success')
    mode.value = 'login'
  } catch (err) { showMsg(err?.message || '请求失败', 'error') }
  finally { loading.value = false }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1.1fr 1fr;
}

/* ── Brand ── */
.auth-brand {
  background: #0a0a1a;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.brand-bg { position: absolute; inset: 0; }
.glow { position: absolute; border-radius: 50%; filter: blur(80px); opacity: .25; }
.g1 { width: 400px; height: 400px; top: -120px; right: -80px; background: var(--brand); }
.g2 { width: 300px; height: 300px; bottom: -60px; left: -60px; background: var(--accent); }

.grid-pattern {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(ellipse at center, black 30%, transparent 70%);
}

.brand-inner {
  position: relative;
  z-index: 1;
  max-width: 400px;
  padding: 48px;
  color: #fff;
}

.brand-logo {
  width: 56px;
  height: 56px;
  border-radius: var(--r-lg);
  background: rgba(255,255,255,.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 32px;
}

.brand-inner h1 {
  font-size: 36px;
  font-weight: 800;
  letter-spacing: -.03em;
  margin-bottom: 4px;
}

.brand-tagline {
  font-size: 15px;
  opacity: .65;
  margin-bottom: 32px;
}

.brand-desc {
  font-size: 13px;
  line-height: 1.8;
  opacity: .5;
  margin-bottom: 40px;
}

.brand-features { display: grid; gap: 12px; }

.bf {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  opacity: .6;
}

.bf svg { opacity: .5; flex-shrink: 0; }

/* ── Form ── */
.auth-form {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
  background: var(--bg-page);
}

.form-card { width: 100%; max-width: 380px; }

.form-tabs {
  display: flex;
  gap: 4px;
  padding: 4px;
  border-radius: var(--r);
  background: var(--bg-active);
  margin-bottom: 28px;
}

.form-tabs button {
  flex: 1;
  padding: 8px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  color: var(--tx-2);
  transition: all .15s var(--e);
}

.form-tabs button.on {
  background: var(--bg-root);
  color: var(--brand);
  box-shadow: var(--sh-xs);
}

.field {
  display: grid;
  gap: 6px;
  margin-bottom: 16px;
}

.field span {
  font-size: 12px;
  font-weight: 600;
  color: var(--tx-2);
}

.field input {
  width: 100%;
  padding: 10px 14px;
  border-radius: var(--r);
  border: 1px solid var(--b);
  font-size: 14px;
  background: var(--bg-root);
  transition: all .15s var(--e);
}

.field input:focus {
  border-color: var(--brand);
  box-shadow: 0 0 0 3px var(--brand-50);
}

.field input::placeholder { color: var(--tx-4); }

.pwd {
  display: flex;
  align-items: center;
  border-radius: var(--r);
  border: 1px solid var(--b);
  background: var(--bg-root);
  padding-right: 8px;
  transition: all .15s var(--e);
}
.pwd:focus-within {
  border-color: var(--brand);
  box-shadow: 0 0 0 3px var(--brand-50);
}
.pwd input {
  border: none;
  background: transparent;
  box-shadow: none;
}
.pwd input:focus { box-shadow: none; }

.pwd-btn {
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 600;
  color: var(--brand);
  white-space: nowrap;
}

.submit-btn {
  width: 100%;
  padding: 11px;
  border-radius: var(--r);
  background: var(--brand);
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  margin-top: 6px;
  transition: all .15s var(--e);
}
.submit-btn:hover:not(:disabled) { background: var(--brand-600); }
.submit-btn:disabled { opacity: .6; cursor: not-allowed; }

.switch {
  text-align: center;
  font-size: 13px;
  color: var(--tx-3);
  margin-top: 20px;
}
.switch button {
  color: var(--brand);
  font-weight: 600;
}
.switch button:hover { text-decoration: underline; }

.msg {
  margin-top: 16px;
  padding: 10px 14px;
  border-radius: var(--r);
  font-size: 12px;
}
.msg.success { background: var(--success-50); color: #065f46; }
.msg.error { background: var(--danger-50); color: #991b1b; }
.msg.info { background: var(--brand-50); color: var(--brand-700); }

@media (max-width: 768px) {
  .auth-page { grid-template-columns: 1fr; }
  .auth-brand { display: none; }
  .auth-form { padding: 32px 24px; }
}
</style>