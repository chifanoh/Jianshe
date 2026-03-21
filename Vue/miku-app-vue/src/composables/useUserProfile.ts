import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useLoginUserStore } from '@/store/useLoginUserStore'

export interface MenuOption {
  label: string
  key: string
  title: string
  placeholder: string
}

/**
 * 用户个人中心组合式函数
 * 处理用户资料、菜单导航、路由参数等逻辑
 */
export function useUserProfile() {
  const route = useRoute()
  const router = useRouter()
  const loginUserStore = useLoginUserStore()

  // 菜单配置
  const menuOptions: MenuOption[] = [
    { label: '我的帖子', key: 'posts', title: '我的帖子', placeholder: '暂无帖子' },
    { label: '我的评论', key: 'comments', title: '我的评论', placeholder: '暂无评论' },
    { label: '我的收藏', key: 'stars', title: '我的收藏', placeholder: '暂无收藏' },
    { label: '我的关注', key: 'follows', title: '我的关注', placeholder: '暂无关注' },
  ]

  // 当前选中的菜单
  const menuValue = ref('posts')

  // 从路由参数初始化菜单选中状态
  const initMenuFromRoute = () => {
    const tab = route.query.tab as string
    if (tab && menuOptions.some((item) => item.key === tab)) {
      menuValue.value = tab
    }
  }

  // 监听路由参数变化
  watch(
    () => route.query.tab,
    (newTab) => {
      if (newTab && menuOptions.some((item) => item.key === newTab)) {
        menuValue.value = newTab as string
      }
    },
  )

  // 处理菜单点击
  const handleMenuClick = (key: string) => {
    menuValue.value = key
    // 更新路由参数
    router.replace({
      query: { ...route.query, tab: key },
    })
  }

  // 用户信息
  const userName = computed(() => loginUserStore.loginUser.userName || '未登录')
  const userId = computed(() => loginUserStore.loginUser.id || '-')
  const src = loginUserStore.loginUser.userAvatar
  // 处理头像地址
  const avatarSrc = computed(() => {
    if (!src) return ''
    if (src.startsWith('http')) return src
    return '/' + src.replace(/\\/g, '/')
  })

  // 当前菜单项配置
  const currentMenu = computed(() => {
    return menuOptions.find((item) => item.key === menuValue.value) || menuOptions[0]
  })

  // 处理编辑资料
  const handleEditProfile = () => {
    // TODO: 实现编辑资料功能
  }

  // 初始化
  initMenuFromRoute()

  return {
    // 用户相关
    userName,
    userId,
    avatarSrc,
    loginUserStore,

    // 菜单相关
    menuOptions,
    menuValue,
    currentMenu,
    handleMenuClick,

    // 操作
    handleEditProfile,
  }
}
