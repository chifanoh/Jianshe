import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/store/useLoginUserStore'

export function useAuth() {
  const loginUser = useLoginUserStore()
  const router = useRouter()

  // 只在需要时获取用户信息
  onMounted(async () => {
    // 如果已经登录且有用户信息，不需要重复获取
    if (loginUser.isLogin && loginUser.loginUser.id) {
      return
    }

    // 如果用户名为'未登录'，才尝试获取用户信息
    if (loginUser.loginUser.userName === '未登录') {
      await loginUser.loadUserFromCache()
    }
  })

  // 头像下拉菜单选项
  const options = [
    {
      label: '个人中心',
      key: 'profile',
    },
    {
      label: '退出登录',
      key: 'logout',
    },
  ]

  // 处理下拉菜单选择
  const handleDropdownSelect = (key: string) => {
    if (key === 'profile') {
      // 跳转到个人中心
      router.push('/user/profile')
    }

    if (key === 'logout') {
      // 处理退出登录
      loginUser.logout()
      router.push('/')
    }
  }

  return {
    loginUser,
    options,
    handleDropdownSelect,
  }
}
