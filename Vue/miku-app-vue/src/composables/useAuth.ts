import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/store/useLoginUserStore'

export function useAuth() {
  const loginUser = useLoginUserStore()
  const router = useRouter()

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
