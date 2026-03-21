import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/store/useLoginUserStore'

/**
 * 关注页面导航组合式函数
 * 处理关注按钮点击逻辑：
 * - 已登录：跳转到个人中心的关注页面
 * - 未登录：跳转到登录页面
 */
export function useFollow() {
  const router = useRouter()
  const loginUserStore = useLoginUserStore()

  /**
   * 处理关注导航
   * @param redirectPath 登录后重定向路径，默认为个人中心关注页
   */
  const handleFollowClick = (redirectPath: string = '/user/profile') => {
    // 检查用户是否已登录
    const isLoggedIn =
      loginUserStore.loginUser?.id && loginUserStore.loginUser?.userName !== '未登录'

    if (isLoggedIn) {
      // 已登录，跳转到个人中心
      router.push({
        path: redirectPath,
        query: { tab: 'follows' }, // 可选：自动切换到关注标签页
      })
    } else {
      // 未登录，跳转到登录页面，并携带重定向参数
      router.push({
        path: '/user/login',
        query: { redirect: redirectPath },
      })
    }
  }

  /**
   * 检查用户是否已登录
   */
  const isLoggedIn = () => {
    return !!(loginUserStore.loginUser?.id && loginUserStore.loginUser?.userName !== '未登录')
  }

  return {
    handleFollowClick,
    isLoggedIn,
  }
}
