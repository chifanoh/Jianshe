import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser, userLogin, userLogout, userRegister } from '@/api/user'
import { saveUserDataWithTimestamp, getLatestUserData, clearAllUserData } from '@/utils/indexedDB'
import { isAuthenticated, clearAuthCredentials } from '@/utils/auth'

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
        setTimeout(async () => {
          const isAuth = await isAuthenticated()
          if (!isAuth) {
            loginUser.value = { userName: '未登录' }
            await clearAllUserData()
          } else if (!loginUser.value.userAvatar || loginUser.value.userName === '未登录') {
            // 只有在缓存数据不完整时才刷新
            await fetchLoginUser()
          }
        }, 100)

        return true
      }

      // 如果IndexedDB中没有数据，检查Cookie认证状态
      const isAuth = await isAuthenticated()
      if (!isAuth) {
        // 如果Cookie中无有效认证，清空用户数据
        loginUser.value = { userName: '未登录' }
        return false
      }

      // Cookie认证有效但IndexedDB无数据，从服务器获取用户信息
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
        const res = await getCurrentUser()
        if (res.data.code === 200 && res.data.data) {
          const payload = res.data.data
          // 使用统一的映射函数处理API返回数据
          const userData = normalizeUserData(payload)
          loginUser.value = userData

          // 保存到IndexedDB缓存（秒级存储）
          try {
            await saveUserDataWithTimestamp(userData)
          } catch (cacheError) {
            console.warn(cacheError)
          }

          return { success: true, data: userData }
        } else {
          loginUser.value = { userName: '未登录' }

          // 清除IndexedDB缓存
          try {
            await clearAllUserData()
          } catch (cacheError) {
            console.warn('清除IndexedDB缓存失败:', cacheError)
          }

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

  // 用户登录
  async function login(userAccount: string, userPassword: string) {
    try {
      const res = await userLogin({ userAccount, userPassword })
      if (res.data.code === 200 && res.data.data) {
        // 使用统一的映射函数处理登录返回数据
        const userData = normalizeUserData(res.data.data)
        // 保存用户数据到状态管理和IndexedDB
        await setLoginUser(userData)
        return { success: true, data: userData, message: '登录成功' }
      } else {
        return { success: false, message: res.data.msg || '登录失败' }
      }
    } catch {
      return { success: false, message: '网络错误，请稍后重试' }
    }
  }

  // 用户注册
  async function register(userAccount: string, userPassword: string, checkPassword: string) {
    try {
      const res = await userRegister({ userAccount, userPassword, checkPassword })
      if (res.data.code === 200 && res.data.data) {
        // 使用统一的映射函数处理注册返回数据
        const userData = normalizeUserData(res.data.data)
        // 保存用户数据到状态管理和IndexedDB
        await setLoginUser(userData)
        return { success: true, data: userData, message: '注册成功' }
      } else {
        return { success: false, message: res.data.msg || '注册失败' }
      }
    } catch {
      return { success: false, message: '网络错误，请稍后重试' }
    }
  }

  // 用户登出
  async function logout() {
    try {
      const res = await userLogout({})
      if (res.data.code === 200) {
        // 清空状态管理中的用户数据
        loginUser.value = { userName: '未登录' }
        // 同时清空IndexedDB缓存
        await clearAllUserData()
        return { success: true, message: '登出成功' }
      } else {
        return { success: false, message: res.data.msg || '登出失败' }
      }
    } catch {
      return { success: false, message: '网络错误，请稍后重试' }
    }
  }

  // 单独设置用户信息
  async function setLoginUser(user: User) {
    loginUser.value = user

    // 同时保存到IndexedDB缓存（秒级存储）
    try {
      await saveUserDataWithTimestamp(user)
    } catch (cacheError) {
      console.warn(cacheError)
    }
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

    // 同时清除HttpOnly Cookie和IndexedDB缓存
    clearAuthCredentials().catch((error) => console.error('清除Cookie失败:', error))
    clearAllUserData().catch((error) => console.error('清除缓存失败:', error))
  }

  return {
    loginUser,
    isLogin,
    loadUserFromCache,
    fetchLoginUser,
    login,
    register,
    logout,
    setLoginUser,
    updateUserAvatar,
    updateUserName,
    clearUser,
  }
})
