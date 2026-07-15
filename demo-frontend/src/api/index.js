import axios from 'axios'

const api = axios.create({
    baseURL: '/api'
})

// 自动解包 ApiResponse
api.interceptors.response.use(
    response => {
        const body = response.data
        if (body && body.code !== undefined) {
            if (body.code === 200) {
                response.data = body.data
            } else {
                return Promise.reject(new Error(body.message))
            }
        }
        return response
    },
    error => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            window.location.hash = '#/login'
        }
        const msg = error.response?.data?.message || error.message
        return Promise.reject(new Error(msg))
    }
)

// 请求拦截器：自动带 token
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = 'Bearer ' + token
    }
    return config
})

export const authApi = {
    login: (data) => api.post('/auth/login', data),
    register: (data) => api.post('/auth/register', data)
}

export const userApi = {
    findAll: () => api.get('/users'),
    findById: (id) => api.get('/users/' + id),
    create: (data) => api.post('/users', data),
    update: (id, data) => api.put('/users/' + id, data),
    delete: (id) => api.delete('/users/' + id)
}

export const articleApi = {
    findAll: (page = 0, size = 10) => api.get('/articles?page=' + page + '&size=' + size),
    findById: (id) => api.get('/articles/' + id),
    findByUserId: (userId) => api.get('/articles?userId=' + userId),
    create: (data) => api.post('/articles', data),
    update: (id, data) => api.put('/articles/' + id, data),
    delete: (id) => api.delete('/articles/' + id),
    search: (keyword, page = 0, size = 10) => api.get('/articles/search?keyword=' + encodeURIComponent(keyword) + '&page=' + page + '&size=' + size)
}

export const categoryApi = {
    findAll: () => api.get('/categories'),
    findById: (id) => api.get('/categories/' + id),
    create: (data) => api.post('/categories', data),
    delete: (id) => api.delete('/categories/' + id)
}