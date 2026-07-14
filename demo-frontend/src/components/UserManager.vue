<template>
    <div class="container">
        <div class="form-card">
            <h3>{{ editing ? '编辑用户' : '创建用户' }}</h3>
            <div class="form-row">
                <input v-model="form.name" placeholder="姓名" />
                <input v-model="form.email" placeholder="邮箱" />
                <input v-model.number="form.age" type="number" placeholder="年龄" />
                <button @click="save" :disabled="!form.name || !form.email">
                    {{ editing ? '更新' : '创建' }}
                </button>
                <button v-if="editing" @click="cancelEdit" class="cancel">取消</button>
            </div>
        </div>

        <div class="table-card">
            <h3>用户列表</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>姓名</th>
                        <th>邮箱</th>
                        <th>年龄</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading"><td colspan="6" class="loading">加载中...</td></tr>
                    <tr v-else-if="users.length === 0"><td colspan="6" class="empty">暂无用户</td></tr>
                    <tr v-for="user in users" :key="user.id">
                        <td>{{ user.id }}</td>
                        <td>{{ user.name }}</td>
                        <td>{{ user.email }}</td>
                        <td>{{ user.age }}</td>
                        <td>{{ user.createTime }}</td>
                        <td class="actions">
                            <button @click="edit(user)" class="edit">编辑</button>
                            <button @click="remove(user.id)" class="delete">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../api/index.js'

const users = ref([])
const loading = ref(true)
const editing = ref(false)
const editingId = ref(null)
const form = ref({ name: '', email: '', age: null })

const fetchUsers = async () => {
    try {
        const res = await userApi.findAll()
        users.value = res.data || res
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
}

const save = async () => {
    try {
        if (editing.value) {
            await userApi.update(editingId.value, form.value)
        } else {
            await userApi.create(form.value)
        }
        form.value = { name: '', email: '', age: null }
        editing.value = false
        editingId.value = null
        await fetchUsers()
    } catch (e) {
        console.error(e)
    }
}

const edit = (user) => {
    editing.value = true
    editingId.value = user.id
    form.value = { name: user.name, email: user.email, age: user.age }
}

const cancelEdit = () => {
    editing.value = false
    editingId.value = null
    form.value = { name: '', email: '', age: null }
}

const remove = async (id) => {
    try {
        await userApi.delete(id)
        await fetchUsers()
    } catch (e) {
        console.error(e)
    }
}

onMounted(fetchUsers)
</script>

<style scoped>
.container { display: flex; flex-direction: column; gap: 20px; }
.form-card, .table-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
h3 { margin-bottom: 12px; font-size: 16px; color: #555; }
.form-row { display: flex; gap: 8px; flex-wrap: wrap; }
input { flex: 1; min-width: 120px; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
input:focus { outline: none; border-color: #409eff; }
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
</style>