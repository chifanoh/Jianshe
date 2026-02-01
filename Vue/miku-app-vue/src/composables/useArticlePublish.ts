import { ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useRouter } from 'vue-router'
import { createArticle } from '@/api/article'

export function useArticlePublish() {
  // 模态框状态
  const showModal = ref(false)
  const publishing = ref(false)
  const message = useMessage()
  const router = useRouter()

  // 表单数据
  const articleTitle = ref('')
  const articleContent = ref('')
  const articleTags = ref<string[]>([])

  // 发布文章
  function cancelCallback() {
    return
  }

  async function submitCallback() {
    if (!articleTitle.value.trim()) {
      message.warning('请输入文章标题')
      return false
    }
    if (!articleContent.value.trim()) {
      message.warning('请输入文章内容')
      return false
    }

    publishing.value = true

    try {
      // 调用创建文章API
      const response = await createArticle({
        title: articleTitle.value.trim(),
        content: articleContent.value.trim(),
        topicIds: articleTags.value.length > 0 ? articleTags.value : undefined,
      })

      if (response.data.code === 200) {
        message.success('文章发布成功！')
        showModal.value = false

        // 重置表单
        articleTitle.value = ''
        articleContent.value = ''
        articleTags.value = []

        // 可以在这里添加跳转到文章详情页的逻辑
        if (response.data.data && response.data.data.id) {
          router.push(`/article/${response.data.data.id}`)
        }
      } else {
        message.error(response.data.msg || '发布失败，请稍后重试')
      }
    } catch (error) {
      console.error('发布文章失败:', error)
      message.error('发布失败，请稍后重试')
    } finally {
      publishing.value = false
    }
  }

  return {
    showModal,
    publishing,
    articleTitle,
    articleContent,
    articleTags,
    cancelCallback,
    submitCallback,
  }
}
