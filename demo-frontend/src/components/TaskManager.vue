<template>
    <div class="container">
        <h2>任务管理</h2>

        <div class="form-card">
            <h3>{{ editing ? '编辑任务' : '创建任务' }}</h3>
            <div class="form-row">
                <input v-model="form.title" placeholder="任务标题" />
                <input v-model="form.description" placeholder="描述（选填）" />
                <button @click="save" :disabled="!form.title">{{ editing ? '保存' : '创建' }}</button>
                <button v-if="editing" @click="cancelEdit" class="cancel">取消</button>
            </div>
        </div>

        <div class="table-card">
            <h3>任务列表</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>标题</th>
                        <th>描述</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading"><td colspan="5" class="loading">加载中...</td></tr>
                    <tr v-else-if="tasks.length === 0"><td colspan="5" class="empty">暂无任务，创建一个吧</td></tr>
                    <tr v-for="t in tasks" :key="t.id" :class="{ done: t.done }">
                        <td>{{ t.id }}</td>
                        <td class="task-title">{{ t.title }}</td>
                        <td>{{ t.description || '-' }}</td>
                        <td>
                            <span :class="['status', t.done ? 'done-badge' : 'undone-badge']">
                                {{ t.done ? '已完成' : '未完成' }}
                            </span>
                        </td>
                        <td class="actions">
                            <button @click="toggleDone(t)" :class="t.done ? 'undo' : 'done-btn'">
                                {{ t.done ? '撤销' : '完成' }}
                            </button>
                            <button @click="edit(t)">编辑</button>
                            <button @click="remove(t.id)" class="delete">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { taskApi } from '../api/index.js'

const tasks = ref([])
const loading = ref(true)
const editing = ref(false)
const editingId = ref(null)
const form = ref({ title: '', description: '' })

const fetch = async () => {
    try {
        const res = await taskApi.findAll()
        tasks.value = res.data || res
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
}

const save = async () => {
    try {
        if (editing.value) {
            await taskApi.update(editingId.value, { title: form.value.title, description: form.value.description })
        } else {
            await taskApi.create({ title: form.value.title, description: form.value.description })
        }
        form.value = { title: '', description: '' }
        editing.value = false
        editingId.value = null
        await fetch()
    } catch (e) {
        console.error(e)
    }
}

const edit = (task) => {
    editing.value = true
    editingId.value = task.id
    form.value = { title: task.title, description: task.description || '' }
}

const cancelEdit = () => {
    editing.value = false
    editingId.value = null
    form.value = { title: '', description: '' }
}

const toggleDone = async (task) => {
    try {
        await taskApi.toggle(task.id)
        await fetch()
    } catch (e) {
        console.error(e)
    }
}

const remove = async (id) => {
    try {
        await taskApi.delete(id)
        await fetch()
    } catch (e) {
        console.error(e)
    }
}

onMounted(fetch)
</script>

<style scoped>
.container { display: flex; flex-direction: column; gap: 20px; }
h2 { font-size: 20px; color: #333; }
.form-card, .table-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
h3 { margin-bottom: 12px; font-size: 16px; color: #555; }
.form-row { display: flex; gap: 8px; flex-wrap: wrap; }
input { flex: 1; min-width: 120px; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
input:focus { outline: none; border-color: #409eff; }
button { padding: 8px 16px; background: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
button:hover { background: #337ecc; }
button:disabled { background: #a0cfff; cursor: not-allowed; }
button.cancel { background: #999; }
button.cancel:hover { background: #777; }
button.delete { background: #f56c6c; padding: 4px 10px; font-size: 12px; }
button.done-btn { background: #67c23a; padding: 4px 10px; font-size: 12px; }
button.done-btn:hover { background: #529b2e; }
button.undo { background: #e6a23c; padding: 4px 10px; font-size: 12px; }
button.undo:hover { background: #cf9236; }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
th, td { padding: 10px 8px; text-align: left; border-bottom: 1px solid #eee; }
th { background: #fafafa; font-weight: 600; color: #555; }
tr.done .task-title { text-decoration: line-through; color: #aaa; }
.actions { display: flex; gap: 4px; }
.status { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.done-badge { background: #e1f3d8; color: #67c23a; }
.undone-badge { background: #fef0f0; color: #f56c6c; }
.loading, .empty { text-align: center; color: #999; padding: 30px; }
</style>
