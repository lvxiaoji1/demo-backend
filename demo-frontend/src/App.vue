<template>
    <div class="app">
        <div class="nav" v-if="isLoggedIn">
            <div class="nav-left">
                <router-link to="/users" class="nav-link">用户管理</router-link>
                <router-link to="/articles" class="nav-link">文章管理</router-link>
                <router-link to="/categories" class="nav-link">分类管理</router-link>
            </div>
            <div class="nav-right">
                <span class="user-info">{{ userName }}</span>
                <button @click="logout" class="logout-btn">登出</button>
            </div>
        </div>
        <router-view />
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoggedIn = ref(!!localStorage.getItem('token'))
const userName = ref(JSON.parse(localStorage.getItem('user') || '{}').name || '')

const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    isLoggedIn.value = false
    router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #f5f5f5; color: #333; }
.app { max-width: 900px; margin: 0 auto; padding: 20px; }
.nav { display: flex; justify-content: space-between; align-items: center; gap: 4px; margin-bottom: 24px; background: white; border-radius: 8px; padding: 8px 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.nav-left { display: flex; gap: 4px; }
.nav-right { display: flex; align-items: center; gap: 12px; }
.nav-link { padding: 8px 20px; text-decoration: none; color: #555; font-size: 14px; border-radius: 4px; font-weight: 500; }
.nav-link:hover { background: #f0f0f0; }
.nav-link.router-link-active { background: #409eff; color: white; }
.user-info { font-size: 14px; color: #666; }
.logout-btn { padding: 6px 14px; background: #f56c6c; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px; }
.logout-btn:hover { background: #e05050; }
</style>