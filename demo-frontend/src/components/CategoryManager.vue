<template>
    <div class="container">
        <div class="form-card">
            <h3>Create Category</h3>
            <div class="form-row">
                <input v-model="form.name" placeholder="Category name" />
                <input v-model="form.description" placeholder="Description (optional)" />
                <button @click="save" :disabled="!form.name">Create</button>
            </div>
        </div>

        <div class="table-card">
            <h3>Category List</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading"><td colspan="4" class="loading">Loading...</td></tr>
                    <tr v-else-if="categories.length === 0"><td colspan="4" class="empty">No categories yet</td></tr>
                    <tr v-for="c in categories" :key="c.id">
                        <td>{{ c.id }}</td>
                        <td>{{ c.name }}</td>
                        <td>{{ c.description || '-' }}</td>
                        <td class="actions">
                            <button @click="remove(c.id)" class="delete">Delete</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { categoryApi } from '../api/index.js'

const categories = ref([])
const loading = ref(true)
const form = ref({ name: '', description: '' })

const fetch = async () => {
    try {
        const res = await categoryApi.findAll()
        categories.value = res.data || res
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
}

const save = async () => {
    try {
        await categoryApi.create({ name: form.value.name, description: form.value.description })
        form.value = { name: '', description: '' }
        await fetch()
    } catch (e) {
        console.error(e)
    }
}

const remove = async (id) => {
    try {
        await categoryApi.delete(id)
        await fetch()
    } catch (e) {
        console.error(e)
    }
}

onMounted(fetch)
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
button.delete { background: #f56c6c; padding: 4px 10px; font-size: 12px; }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
th, td { padding: 10px 8px; text-align: left; border-bottom: 1px solid #eee; }
th { background: #fafafa; font-weight: 600; color: #555; }
.actions { display: flex; gap: 4px; }
.loading, .empty { text-align: center; color: #999; padding: 30px; }
</style>