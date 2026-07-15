<template>
    <div class="login-container">
        <div class="login-card">
            <h2>{{ isLogin ? '登录' : '注册' }}</h2>
            <div class="toggle">
                <button :class="{ active: isLogin }" @click="isLogin = true">登录</button>
                <button :class="{ active: !isLogin }" @click="isLogin = false">注册</button>
            </div>

            <div class="form">
                <input v-if="!isLogin" v-model="form.name" placeholder="昵称" />
                <input v-model="form.email" placeholder="邮箱" />
                <input v-model="form.password" type="password" placeholder="密码" />
                <p v-if="error" class="error">{{ error }}</p>
                <button @click="submit" :disabled="loading">
                    {{ loading ? '请稍候...' : (isLogin ? '登录' : '注册') }}
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api/index.js'

const router = useRouter()
const isLogin = ref(true)
const loading = ref(false)
const error = ref('')
const form = ref({ name: '', email: '', password: '' })

const submit = async () => {
    error.value = ''
    loading.value = true
    try {
        let data
        if (isLogin.value) {
            const res = await authApi.login({
                email: form.value.email,
                password: form.value.password
            })
            data = res.data || res
        } else {
            const res = await authApi.register({
                name: form.value.name,
                email: form.value.email,
                password: form.value.password
            })
            data = res.data || res
        }
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify({ id: data.userId, name: data.name, email: data.email }))
        router.push('/users')
    } catch (e) {
        error.value = e.message
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.login-container { display: flex; justify-content: center; align-items: center; min-height: 60vh; }
.login-card { background: white; border-radius: 8px; padding: 32px; width: 360px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
h2 { text-align: center; margin-bottom: 16px; font-size: 20px; color: #333; }
.toggle { display: flex; gap: 0; margin-bottom: 20px; border-radius: 4px; overflow: hidden; border: 1px solid #ddd; }
.toggle button { flex: 1; padding: 8px; border: none; background: white; cursor: pointer; font-size: 14px; color: #666; }
.toggle button.active { background: #409eff; color: white; }
.form { display: flex; flex-direction: column; gap: 12px; }
input { padding: 10px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
input:focus { outline: none; border-color: #409eff; }
button { padding: 10px; background: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
button:hover { background: #337ecc; }
button:disabled { background: #a0cfff; cursor: not-allowed; }
.error { color: #f56c6c; font-size: 13px; text-align: center; }
</style>