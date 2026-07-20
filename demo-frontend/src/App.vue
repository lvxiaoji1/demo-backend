<template>
    <div class="app-layout" v-if="isLoggedIn">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>管理系统</h2>
            </div>
            <nav class="sidebar-nav">
                <router-link to="/dashboard" class="nav-item">📊 仪表盘</router-link>
                <router-link to="/tasks" class="nav-item">📋 任务管理</router-link>
                <router-link to="/users" class="nav-item">👤 用户管理</router-link>
                <router-link to="/articles" class="nav-item">📝 文章管理</router-link>
                <router-link to="/categories" class="nav-item">📂 分类管理</router-link>
            </nav>
            <div class="sidebar-footer">
                <span class="user-info">{{ userName }}</span>
                <button @click="logout" class="logout-btn">登出</button>
            </div>
        </aside>
        <main class="main-content">
            <router-view />
        </main>
    </div>
    <div class="app-login" v-else>
        <router-view />
    </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isLoggedIn = ref(!!localStorage.getItem('token'))
const userName = ref(JSON.parse(localStorage.getItem('user') || '{}').name || '')

watch(() => route.path, () => {
    const token = localStorage.getItem('token')
    isLoggedIn.value = !!token
    if (token) {
        userName.value = JSON.parse(localStorage.getItem('user') || '{}').name || ''
    }
})

const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    isLoggedIn.value = false
    router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #f0f2f5; color: #333; }

.app-layout { display: flex; min-height: 100vh; }

.sidebar {
    width: 220px;
    background: #1d1e2c;
    color: #ccc;
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
}
.sidebar-header {
    padding: 20px;
    border-bottom: 1px solid #2a2b3d;
}
.sidebar-header h2 {
    color: #fff;
    font-size: 18px;
    font-weight: 600;
}

.sidebar-nav {
    flex: 1;
    padding: 12px 0;
    display: flex;
    flex-direction: column;
    gap: 2px;
}
.nav-item {
    display: block;
    padding: 10px 20px;
    color: #b0b3c1;
    text-decoration: none;
    font-size: 14px;
    transition: all 0.15s;
    border-left: 3px solid transparent;
}
.nav-item:hover {
    background: #2a2b3d;
    color: #fff;
}
.nav-item.router-link-active {
    background: #2a2b3d;
    color: #409eff;
    border-left-color: #409eff;
}

.sidebar-footer {
    padding: 16px 20px;
    border-top: 1px solid #2a2b3d;
    display: flex;
    flex-direction: column;
    gap: 8px;
}
.user-info {
    font-size: 13px;
    color: #888;
}
.logout-btn {
    padding: 6px 0;
    background: transparent;
    color: #f56c6c;
    border: 1px solid #f56c6c;
    border-radius: 4px;
    cursor: pointer;
    font-size: 13px;
    transition: all 0.15s;
}
.logout-btn:hover {
    background: #f56c6c;
    color: #fff;
}

.main-content {
    flex: 1;
    padding: 24px;
    overflow-y: auto;
    max-width: 1100px;
}

.app-login {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f0f2f5;
}
</style>
