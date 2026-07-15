import { createRouter, createWebHashHistory } from 'vue-router'
import UserManager from '../components/UserManager.vue'
import ArticleManager from '../components/ArticleManager.vue'
import CategoryManager from '../components/CategoryManager.vue'
import Login from '../components/Login.vue'

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/', redirect: '/users' },
    { path: '/users', name: 'Users', component: UserManager, meta: { requiresAuth: true } },
    { path: '/articles', name: 'Articles', component: ArticleManager, meta: { requiresAuth: true } },
    { path: '/categories', name: 'Categories', component: CategoryManager, meta: { requiresAuth: true } }
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