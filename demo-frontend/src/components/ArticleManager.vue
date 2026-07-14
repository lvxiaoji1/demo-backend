<template>
    <div class="container">
        <div class="form-card">
            <h3>{{ editing ? '编辑文章' : '创建文章' }}</h3>
            <div class="form-row">
                <input v-model="form.title" placeholder="标题" />
                <select v-model="form.userId" v-if="!editing">
                    <option value="" disabled>选择作者</option>
                    <option v-for="u in users" :key="u.id" :value="u.id">{{ u.name }}</option>
                </select>
                <button @click="save" :disabled="!form.title || !form.content || (!editing && !form.userId)">
                    {{ editing ? '更新' : '创建' }}
                </button>
                <button v-if="editing" @click="cancelEdit" class="cancel">取消</button>
            </div>
            <textarea v-model="form.content" placeholder="内容" rows="4"></textarea>
        </div>

        <div class="table-card">
            <h3>文章列表</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>标题</th>
                        <th>作者</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading"><td colspan="5" class="loading">加载中...</td></tr>
                    <tr v-else-if="articles.length === 0"><td colspan="5" class="empty">暂无文章</td></tr>
                    <tr v-for="a in articles" :key="a.id">
                        <td>{{ a.id }}</td>
                        <td>{{ a.title }}</td>
                        <td>{{ a.authorName }}</td>
                        <td>{{ a.createTime }}</td>
                        <td class="actions">
                            <button @click="edit(a)" class="edit">编辑</button>
                            <button @click="remove(a.id)" class="delete">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="pagination" v-if="totalPages > 1">
                <button @click="goPage(0)" :disabled="page === 0">首页</button>
                <button @click="goPage(page - 1)" :disabled="page === 0">上一页</button>
                <span>第 {{ page + 1 }} / {{ totalPages }} 页</span>
                <button @click="goPage(page + 1)" :disabled="page >= totalPages - 1">下一页</button>
                <button @click="goPage(totalPages - 1)" :disabled="page >= totalPages - 1">末页</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi, userApi } from '../api/index.js'

const articles = ref([])
const users = ref([])
const loading = ref(true)
const editing = ref(false)
const editingId = ref(null)
const form = ref({ title: '', content: '', userId: '' })
const page = ref(0)
const size = ref(10)
const totalPages = ref(0)

const fetchArticles = async () => {
    loading.value = true
    try {
        const res = await articleApi.findAll(page.value, size.value)
        articles.value = res.data.content || res.data
        totalPages.value = res.data.totalPages || 1
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
}

const fetchUsers = async () => {
    try {
        const res = await userApi.findAll()
        users.value = res.data || res
    } catch (e) {
        console.error(e)
    }
}

const goPage = (p) => {
    page.value = p
    fetchArticles()
}

const save = async () => {
    try {
        if (editing.value) {
            await articleApi.update(editingId.value, { title: form.value.title, content: form.value.content })
        } else {
            await articleApi.create({ title: form.value.title, content: form.value.content, userId: Number(form.value.userId) })
        }
        form.value = { title: '', content: '', userId: '' }
        editing.value = false
        editingId.value = null
        page.value = 0
        await fetchArticles()
    } catch (e) {
        console.error(e)
    }
}

const edit = (a) => {
    editing.value = true
    editingId.value = a.id
    form.value = { title: a.title, content: a.content, userId: '' }
}

const cancelEdit = () => {
    editing.value = false
    editingId.value = null
    form.value = { title: '', content: '', userId: '' }
}

const remove = async (id) => {
    try {
        await articleApi.delete(id)
        await fetchArticles()
    } catch (e) {
        console.error(e)
    }
}

onMounted(() => {
    fetchUsers()
    fetchArticles()
})
</script>

<style scoped>
.container { display: flex; flex-direction: column; gap: 20px; }
.form-card, .table-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
h3 { margin-bottom: 12px; font-size: 16px; color: #555; }
.form-row { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 8px; }
input, select { flex: 1; min-width: 120px; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
input:focus, select:focus { outline: none; border-color: #409eff; }
textarea { width: 100%; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; resize: vertical; }
textarea:focus { outline: none; border-color: #409eff; }
button { padding: 8px 16px; background: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
button:hover { background: #337ecc; }
button:disabled { background: #a0cfff; cursor: not-allowed; }
button.cancel { background: #909399; }
button.edit { background: #67c23a; padding: 4px 10px; font-size: 12px; }
button.delete { background: #f56c6c; padding: 4px 10px; font-size: 12px; }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
th, td { padding: 10px 8px; text-align: left; border-bottom: 1px solid #eee; }
th { background: #fafafa; font-weight: 600; color: #555; }
.actions { display: flex; gap: 4px; }
.loading, .empty { text-align: center; color: #999; padding: 30px; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 8px; margin-top: 12px; }
.pagination span { font-size: 14px; color: #666; }
</style>