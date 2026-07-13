import { createRouter, createWebHashHistory } from 'vue-router'
import UserManager from '../components/UserManager.vue'
import ArticleManager from '../components/ArticleManager.vue'

const routes = [
    { path: '/', redirect: '/users' },
    { path: '/users', name: 'Users', component: UserManager },
    { path: '/articles', name: 'Articles', component: ArticleManager }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router