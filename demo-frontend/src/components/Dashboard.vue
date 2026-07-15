<template>
    <div class="dashboard">
        <h2>数据概览</h2>
        <div class="cards">
            <div class="card">
                <div class="number">{{ stats.userCount }}</div>
                <div class="label">用户数</div>
            </div>
            <div class="card">
                <div class="number">{{ stats.articleCount }}</div>
                <div class="label">文章数</div>
            </div>
            <div class="card">
                <div class="number">{{ stats.categoryCount }}</div>
                <div class="label">分类数</div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = 'Bearer ' + token
    return config
})
api.interceptors.response.use(r => {
    if (r.data && r.data.code === 200) { r.data = r.data.data }
    return r
})

const stats = ref({ userCount: 0, articleCount: 0, categoryCount: 0 })

onMounted(async () => {
    try {
        const res = await api.get('/dashboard/stats')
        stats.value = res.data || res
    } catch (e) {
        console.error(e)
    }
})
</script>

<style scoped>
.dashboard { }
h2 { font-size: 20px; margin-bottom: 20px; color: #333; }
.cards { display: flex; gap: 20px; }
.card { flex: 1; background: white; border-radius: 8px; padding: 30px; text-align: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.number { font-size: 36px; font-weight: 700; color: #409eff; }
.label { font-size: 14px; color: #666; margin-top: 8px; }
</style>