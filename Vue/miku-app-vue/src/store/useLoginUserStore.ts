import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser } from '@/api/user'
import { saveUserDataWithTimestamp, getLatestUserData, clearAllUserData } from '@/utils/indexedDB'

// 用户类型定义 - 规范:使用接口继承和可选字段
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

// 后端返回的用户数据类型
export interface ApiUser extends BaseUser {
  name?: string
  avatar?: string
}

// 前端使用的用户类型
export type User = BaseUser

// 用户数据映射工具 - 规范:单一映射函数
function normalizeUserData(apiUser: ApiUser): User {
  return {
    id: apiUser.id,
    userName: apiUser.name || apiUser.userName || '未登录',
    userAvatar: apiUser.avatar || apiUser.userAvatar,
    userAccount: apiUser.userAccount,
    userProfile: apiUser.userProfile,
    userRole: apiUser.userRole,
    sex: apiUser.sex,
    phone: apiUser.phone,
    email: apiUser.email,
    userStatus: apiUser.userStatus,
    createTime: apiUser.createTime,
    updateTime: apiUser.updateTime,
    token: apiUser.token,
  }
}

export const useLoginUserStore = defineStore('loginUser', () => {
  // 用户信息状态
  const loginUser = ref<User>({
    userName: '未登录',
  })

  // 登录状态
  const isLogin = computed(() => {
    return loginUser.value.id !== undefined && loginUser.value.id !== null
  })

  // 从IndexedDB加载用户数据 - 优先显示缓存头像与基本信息
  async function loadUserFromCache() {
    try {
      // 首先尝试从IndexedDB获取缓存的用户数据（秒级恢复）
      const cachedUser = await getLatestUserData()
      if (cachedUser) {
        // 使用统一的映射函数处理缓存数据
        loginUser.value = normalizeUserData(cachedUser)
        // 异步验证Cookie认证状态，如果无效清空；有效则后台刷新最新数据
        // 优化：如果缓存数据已存在且用户已登录，则不再立即调用 getCurrentUser
        if (isLogin.value) {
          setTimeout(async () => {
            // 只有在缓存数据不完整时才刷新
            if (!loginUser.value.userAvatar || loginUser.value.userName === '未登录') {
              await fetchLoginUser()
            }
          }, 100)
        } else {
          // 如果缓存存在但用户未登录，则需要验证Cookie
          setTimeout(async () => {
            const isAuth = await getCurrentUser()
            if (!isAuth) {
              loginUser.value = { userName: '未登录' }
              await clearAllUserData()
            } else {
              await fetchLoginUser()
            }
          }, 100)
        }

        return true
      }

      // 如果IndexedDB中没有数据，直接从服务器获取用户信息
      const userRes = await fetchLoginUser()
      return userRes.success
    } catch {
      return false
    }
  }

  // 远程获取登录用户信息
  let fetchUserPromise: Promise<{ success: boolean; data?: User; message?: string }> | null = null

  async function fetchLoginUser() {
    if (fetchUserPromise) {
      return fetchUserPromise
    }

    // 创建新的请求Promise
    fetchUserPromise = (async () => {
      try {
        //TODO: 优化：如果缓存数据已存在且用户已登录，则不再立即调用 getCurrentUser
        const res = await getCurrentUser()
        if (res.data.code === 200 && res.data.data) {
          const payload = res.data.data
          // 使用统一的映射函数处理API返回数据
          const userData = normalizeUserData(payload)
          loginUser.value = userData

          // 保存到IndexedDB缓存（秒级存储）
          try {
            await saveUserDataWithTimestamp(userData)
          } catch {}

          return { success: true, data: userData }
        } else {
          loginUser.value = { userName: '未登录' }

          // 清除IndexedDB缓存
          try {
            await clearAllUserData()
          } catch {}

          return { success: false, message: res.data.msg || '获取用户信息失败' }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        return { success: false, message: '网络错误，请稍后重试' }
      } finally {
        fetchUserPromise = null
      }
    })()

    return fetchUserPromise
  }

  // 单独设置用户信息
  async function setLoginUser(user: User) {
    loginUser.value = user

    // 同时保存到IndexedDB缓存（秒级存储）
    try {
      await saveUserDataWithTimestamp(user)
    } catch {}
  }

  // 更新用户头像
  function updateUserAvatar(avatarUrl: string) {
    if (loginUser.value) {
      loginUser.value.userAvatar = avatarUrl
    }
  }

  // 更新用户名
  function updateUserName(userName: string) {
    if (loginUser.value) {
      loginUser.value.userName = userName
    }
  }

  // 清除用户信息（用于登出或会话过期）
  function clearUser() {
    loginUser.value = { userName: '未登录' }
  }

  return {
    loginUser,
    isLogin,
    loadUserFromCache,
    fetchLoginUser,
    setLoginUser,
    updateUserAvatar,
    updateUserName,
    clearUser,
  }
})
