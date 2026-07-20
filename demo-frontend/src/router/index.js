import { createRouter, createWebHashHistory } from 'vue-router'
import UserManager from '../components/UserManager.vue'
import ArticleManager from '../components/ArticleManager.vue'
import CategoryManager from '../components/CategoryManager.vue'
import Login from '../components/Login.vue'
import Dashboard from '../components/Dashboard.vue'
import TaskManager from '../components/TaskManager.vue'

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/', redirect: '/dashboard' },
    { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
    { path: '/users', name: 'Users', component: UserManager, meta: { requiresAuth: true } },
    { path: '/articles', name: 'Articles', component: ArticleManager, meta: { requiresAuth: true } },
    { path: '/categories', name: 'Categories', component: CategoryManager, meta: { requiresAuth: true } },
    { path: '/tasks', name: 'Tasks', component: TaskManager, meta: { requiresAuth: true } }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router