import axios from 'axios'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { clearAllUserData } from '@/utils/indexedDB'
import router from '@/router'

// 创建 axios 实例
export const myAxios = axios.create({
  baseURL: '/',
  timeout: 10000,
  withCredentials: true, // 关键：允许发送和接收 HttpOnly Cookie
})

// 添加请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 处理 401 未授权错误
async function handleUnauthorized() {
  try {
    // 清空 IndexedDB 中的用户数据
    await clearAllUserData()
  } catch {
    // 静默处理
  }

  try {
    // 获取 store 并清空登录状态
    const loginUserStore = useLoginUserStore()
    loginUserStore.clearUser()
  } catch {
    // 如果 store 获取失败，至少清空 localStorage/sessionStorage
    localStorage.removeItem('loginUser')
  }

  // 如果当前不在登录页，则跳转到登录页
  if (router.currentRoute.value.path !== '/user/login') {
    router.push({
      path: '/user/login',
      query: { redirect: router.currentRoute.value.fullPath },
    })
  }
}

// 添加响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么

    // 处理 401 未授权错误（JWT 过期或无效）
    if (error.response?.status === 401) {
      handleUnauthorized()
    }

    return Promise.reject(error)
  },
)
