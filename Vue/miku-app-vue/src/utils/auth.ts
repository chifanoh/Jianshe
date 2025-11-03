import { myAxios } from '@/request'

/**
 * 认证工具类 - HttpOnly Cookie 实现
 * 替代 IndexedDB，提供更安全的认证机制
 * 包含请求节流机制，避免重复请求
 */

// 请求节流缓存
const requestCache = new Map<string, Promise<any>>()
const CACHE_TTL = 5000 // 5秒缓存

/**
 * 带缓存的请求封装
 * 避免短时间内重复请求相同的接口
 */
async function cachedRequest<T>(
  key: string,
  requestFn: () => Promise<T>,
  ttl = CACHE_TTL,
): Promise<T> {
  const cached = requestCache.get(key)
  if (cached) {
    return cached
  }
  const promise = requestFn()
  requestCache.set(key, promise)
  // 清理过期缓存
  setTimeout(() => {
    requestCache.delete(key)
  }, ttl)
  return promise
}

/**
 * 检查当前认证状态
 * 通过调用后端接口验证 HttpOnly Cookie 是否有效
 * 使用共享Promise避免重复请求
 */
let authCheckPromise: Promise<boolean> | null = null

export async function isAuthenticated(): Promise<boolean> {
  if (authCheckPromise) {
    return authCheckPromise
  }
  authCheckPromise = cachedRequest('isAuthenticated', async () => {
    try {
      const response = await myAxios.get('/api/user/current')
      return response.data?.code === 200 && response.data?.data != null
    } catch (error) {
      console.warn('检查认证状态失败:', error)
      return false
    }
  })
  authCheckPromise.finally(() => {
    authCheckPromise = null
  })
  return authCheckPromise
}

/**
 * 获取当前用户信息（通过 HttpOnly Cookie）
 * 优先使用 Cookie 认证，无需手动传递 token
 * 共享Promise避免重复请求
 */
export async function getCurrentUserByCookie() {
  try {
    const isAuth = await isAuthenticated()
    if (!isAuth) {
      return {
        success: false,
        message: '用户未认证',
      }
    }
    // 如果认证通过，直接返回用户信息
    const response = await myAxios.get('/api/user/current')
    return {
      success: true,
      data: response.data.data,
      message: '获取用户信息成功',
    }
  } catch (error: any) {
    return {
      success: false,
      message: error.response?.data?.message || '网络错误，请稍后重试',
    }
  }
}

/**
 * 清除 HttpOnly Cookie 中的认证信息
 * 通过调用后端登出接口实现
 */
export async function clearAuthCredentials(): Promise<boolean> {
  try {
    // 调用后端登出接口，后端会清除 HttpOnly Cookie
    const response = await myAxios.post('/api/user/logout', {})
    return response.data?.code === 200
  } catch (error) {
    console.error('清除认证信息失败:', error)
    return false
  }
}

/**
 * 获取 CSRF Token（如果需要的话）
 * 某些后端框架需要 CSRF Token 进行安全验证
 */
export async function getCSRFToken(): Promise<string | null> {
  try {
    const response = await myAxios.get('/api/csrf-token')
    return response.data?.data?.token || null
  } catch (error) {
    console.warn('获取 CSRF Token 失败:', error)
    return null
  }
}

/**
 * 刷新认证 Token（如果需要的话）
 * 用于处理 Token 过期的情况
 */
export async function refreshAuthToken(): Promise<boolean> {
  try {
    const response = await myAxios.post('/api/user/refresh-token', {})
    return response.data?.code === 200
  } catch (error) {
    console.error('刷新认证 Token 失败:', error)
    return false
  }
}

/**
 * 认证状态检查器
 * 定期检查认证状态，处理自动登出等场景
 */
export class AuthStatusChecker {
  private intervalId: number | null = null
  private checkInterval = 5 * 60 * 1000 // 5分钟检查一次
  private lastCheckTime = 0
  private minCheckInterval = 30 * 1000 // 最少30秒检查一次

  /**
   * 开始定期检查
   */
  startChecking(callback?: (isAuth: boolean) => void) {
    this.stopChecking()
    const checkAuth = async () => {
      const now = Date.now()
      // 避免过于频繁的检查
      if (now - this.lastCheckTime < this.minCheckInterval) {
        return
      }
      this.lastCheckTime = now
      const isAuth = await isAuthenticated()
      if (callback) {
        callback(isAuth)
      }
      // 如果未认证，可以触发登出操作
      if (!isAuth) {
        console.warn('认证状态失效，需要重新登录')
        // 可以在这里触发登出操作或跳转到登录页
      }
    }
    // 延迟5秒后执行第一次检查，避免页面初始化时的并发请求
    setTimeout(() => {
      checkAuth()
      // 设置定期检查
      this.intervalId = window.setInterval(checkAuth, this.checkInterval)
    }, 5000)
  }

  /**
   * 停止定期检查
   */
  stopChecking() {
    if (this.intervalId) {
      clearInterval(this.intervalId)
      this.intervalId = null
    }
  }
}

// 导出单例实例
export const authChecker = new AuthStatusChecker()
