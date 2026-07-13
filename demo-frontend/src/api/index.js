import axios from 'axios'

const api = axios.create({
    baseURL: '/api'
})

// 响应拦截器：自动解包 ApiResponse 的 data 字段
// 后端返回 { code: 200, message: "success", data: [...] }
// 拦截后前台拿到的直接是 data 的值 ([...])
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
        const msg = error.response?.data?.message || error.message
        return Promise.reject(new Error(msg))
    }
)

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
    delete: (id) => api.delete('/articles/' + id)
}