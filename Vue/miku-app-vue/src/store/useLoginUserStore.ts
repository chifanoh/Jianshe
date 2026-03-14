import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser, userLogout } from '@/api/user'
import { saveUserDataWithTimestamp, getLatestUserData, clearAllUserData } from '@/utils/indexedDB'

// ==================== 类型定义 ====================

export interface BaseUser {
  id?: string | number
  userName: string
  userAccount?: string
  userAvatar?: string
  userProfile?: string
  userRole?: string
  sex?: number
  phone?: string
  email?: string
  userStatus?: number
  createTime?: string
  updateTime?: string
  token?: string
}

export interface ApiUser extends BaseUser {
  name?: string
  avatar?: string
}

export type User = BaseUser

// ==================== 工具函数 ====================

function normalizeUserData(apiUser: ApiUser): User {
  return {
    id: apiUser.id,
    userName: apiUser.name || apiUser.userName || '未登录',
    userAvatar: apiUser.avatar || apiUser.userAvatar,
    sex: apiUser.sex,
    phone: apiUser.phone,
    email: apiUser.email,
  }
}

// 静默清理 IndexedDB（失败不抛出）
async function silentlyClearUserData(): Promise<void> {
  try {
    await clearAllUserData()
  } catch {
    // 清理失败静默处理，不影响主流程
  }
}

// 静默保存用户数据（失败不抛出）
async function silentlySaveUserData(user: User): Promise<void> {
  try {
    await saveUserDataWithTimestamp(user)
  } catch {
    // 保存失败静默处理，不影响主流程
  }
}

// ==================== Store 定义 ====================

export const useLoginUserStore = defineStore('loginUser', () => {
  // ---------- 状态 ----------
  const loginUser = ref<User>({ userName: '未登录' })

  const isLogin = computed(() => {
    return loginUser.value.id !== undefined && loginUser.value.id !== null
  })

  // ---------- 私有辅助方法 ----------

  // 检查用户数据是否需要刷新（信息不完整）
  function needsRefresh(): boolean {
    return !loginUser.value.userAvatar || loginUser.value.userName === '未登录'
  }

  // 验证 Cookie 并同步状态
  async function validateAndSync(): Promise<void> {
    // 直接使用 fetchLoginUser，避免重复调用 getCurrentUser
    const result = await fetchLoginUser()
    if (!result.success) {
      // Cookie 无效或获取失败，清空状态
      loginUser.value = { userName: '未登录' }
      await silentlyClearUserData()
    }
  }

  // 延迟执行（让 UI 先渲染缓存）
  function defer(fn: () => void | Promise<void>, ms = 100): void {
    setTimeout(fn, ms)
  }

  // ---------- 核心方法 ----------

  /**
   * 从服务器获取最新用户信息
   * 使用 Promise 锁防止重复请求
   */
  let fetchUserPromise: Promise<{ success: boolean; data?: User; message?: string }> | null = null

  async function fetchLoginUser(): Promise<{ success: boolean; data?: User; message?: string }> {
    if (fetchUserPromise) return fetchUserPromise

    fetchUserPromise = getCurrentUser()
      .then(async (res) => {
        if (res.data.code !== 200 || !res.data.data) {
          loginUser.value = { userName: '未登录' }
          await silentlyClearUserData()
          return {
            success: false,
            message: res.data.msg || '获取用户信息失败',
          }
        }

        const userData = normalizeUserData(res.data.data)
        loginUser.value = userData
        await silentlySaveUserData(userData)

        return { success: true, data: userData }
      })
      .catch((error) => {
        console.error('获取用户信息失败:', error)
        return { success: false, message: '网络错误，请稍后重试' }
      })
      .finally(() => {
        fetchUserPromise = null
      })

    return fetchUserPromise
  }

  /**
   * 初始化：优先从 IndexedDB 加载，后台验证/刷新
   * 流程：读缓存 → 显示 → 后台验证 → 按需刷新
   */
  async function loadUserFromCache(): Promise<boolean> {
    try {
      const cachedUser = await getLatestUserData()

      // 无缓存：直接走服务器
      if (!cachedUser) {
        const result = await fetchLoginUser()
        return result.success
      }

      // 有缓存：立即显示
      loginUser.value = normalizeUserData(cachedUser)

      // 后台处理认证验证（不阻塞 UI）
      defer(async () => {
        if (isLogin.value) {
          // 已登录态：仅信息不完整时才刷新
          if (needsRefresh()) {
            await fetchLoginUser()
          }
        } else {
          // 未登录态：验证 Cookie 有效性
          await validateAndSync()
        }
      })

      return true
    } catch {
      return false
    }
  }

  // ========== 退出登录 ==========
  async function logout() {
    try {
      await userLogout() // 不需要传参
    } catch (error) {
      console.error('退出请求失败:', error)
    } finally {
      // 无论后端成功与否，前端都清空
      loginUser.value = { userName: '未登录' }
      await clearAllUserData()
    }
  }

  /**
   * 设置用户信息（同时更新缓存）
   */
  async function setLoginUser(user: User): Promise<void> {
    loginUser.value = user
    await silentlySaveUserData(user)
  }

  // ---------- 轻量更新方法（仅修改内存，不同步缓存）----------

  function updateUserAvatar(avatarUrl: string): void {
    if (loginUser.value) {
      loginUser.value.userAvatar = avatarUrl
    }
  }

  function updateUserName(userName: string): void {
    if (loginUser.value) {
      loginUser.value.userName = userName
    }
  }

  function clearUser(): void {
    loginUser.value = { userName: '未登录' }
  }

  // ---------- 导出 ----------
  return {
    loginUser,
    isLogin,
    logout,
    loadUserFromCache,
    fetchLoginUser,
    setLoginUser,
    updateUserAvatar,
    updateUserName,
    clearUser,
  }
})
