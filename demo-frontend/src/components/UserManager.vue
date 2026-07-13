<template>
    <div class="container">
        <div class="form-card">
            <h3>{{ editing ? 'Edit User' : 'Create User' }}</h3>
            <div class="form-row">
                <input v-model="form.name" placeholder="Name" />
                <input v-model="form.email" placeholder="Email" />
                <input v-model.number="form.age" type="number" placeholder="Age" />
                <button @click="save" :disabled="!form.name || !form.email">
                    {{ editing ? 'Update' : 'Create' }}
                </button>
                <button v-if="editing" @click="cancelEdit" class="cancel">Cancel</button>
            </div>
        </div>

        <div class="table-card">
            <h3>User List</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Age</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="loading"><td colspan="6" class="loading">Loading...</td></tr>
                    <tr v-else-if="users.length === 0"><td colspan="6" class="empty">No users yet</td></tr>
                    <tr v-for="user in users" :key="user.id">
                        <td>{{ user.id }}</td>
                        <td>{{ user.name }}</td>
                        <td>{{ user.email }}</td>
                        <td>{{ user.age }}</td>
                        <td>{{ user.createTime }}</td>
                        <td class="actions">
                            <button @click="edit(user)" class="edit">Edit</button>
                            <button @click="remove(user.id)" class="delete">Delete</button>
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
        users.value = res.data
    } catch (e) {
        console.error('Failed to fetch users:', e)
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
        console.error('Save failed:', e)
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
        console.error('Delete failed:', e)
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