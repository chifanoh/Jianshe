import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { toggleLikeApi, getLikeStatus } from '@/api/article'

/**
 * 文章点赞功能 Composable
 * 提供点赞状态的初始化、点赞/取消点赞等完整功能
 */
export const useArticleLike = () => {
  const router = useRouter()
  const loginUserStore = useLoginUserStore()
  const isLiked = ref(false)
  const isLoading = ref(false)
  const isSubmitting = ref(false)

  /**
   * 获取点赞状态
   * @param articleId 文章ID
   * @param isUserLoggedIn 用户是否已登录
   */
  const initLikeStatus = async (articleId: string | number) => {
    if (!loginUserStore.isLogin) {
      return
    }

    try {
      const response = await getLikeStatus(articleId)
      isLiked.value = response.data
    } catch (error) {
      console.error('获取点赞状态失败:', error)
      isLiked.value = false
    }
  }

  /**
   * 点赞/取消点赞（乐观更新）
   * @param articleId 文章ID
   * @param currentLikes 当前点赞数
   * @param onArticleUpdate 回调函数，用于更新文章点赞数
   * @returns 点赞状态
   */
  const toggleLike = async (
    articleId: string | number,
    currentLikes: number,
    onArticleUpdate: (updatedLikes: number) => void,
  ): Promise<boolean> => {
    console.error(loginUserStore.isLogin)
    // 1. 边界检查：未登录用户不能点赞
    if (!loginUserStore.isLogin) {
      // 未登录，跳转登录页
      console.error(loginUserStore.isLogin)
      router.push('/login')
      return isLiked.value
    }

    // 2. 边界检查：防止重复提交
    if (isSubmitting.value) {
      console.error('点赞操作正在进行中，请勿重复点击')
      return isLiked.value
    }
    // 3. 乐观更新：立即更新本地状态
    const newLiked = !isLiked.value
    isLiked.value = newLiked
    isLoading.value = true

    // 更新文章点赞数（立即响应，提升用户体验）
    onArticleUpdate(currentLikes + (newLiked ? 1 : -1))

    try {
      // 4. 调用后端接口（后端自动完成点赞/取消的切换逻辑）
      await toggleLikeApi(articleId)
      return newLiked
    } catch (error) {
      // 5. 接口失败：回滚本地状态
      console.error('点赞操作失败:', error)
      isLiked.value = !newLiked
      onArticleUpdate(currentLikes) // 回滚点赞数
      return isLiked.value
    } finally {
      isLoading.value = false
    }
  }

  /**
   * 手动设置点赞状态（用于文章详情加载时）
   * @param liked 点赞状态
   */
  const setLikedStatus = (liked: boolean) => {
    isLiked.value = liked
  }

  return {
    isLiked,
    isLoading,
    isSubmitting,
    initLikeStatus,
    toggleLike,
    setLikedStatus,
  }
}
