import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'

export function useSearch() {
  const router = useRouter()
  const message = useMessage()

  // 搜索功能
  const searchQuery = ref('')

  // 搜索输入验证
  const validateSearchInput = (value: string): boolean => {
    if (!value || value.trim().length === 0) {
      return false
    }
    return value.trim().length <= 20
  }

  // 处理搜索输入
  const handleSearchInput = (value: string) => {
    if (value && value.length === 20) {
      message.warning('搜索最多不能超过20个字符')
      return
    }
    if (value && value.length > 20) {
      searchQuery.value = value.slice(0, 20)
      return
    }
    if (!validateSearchInput(value)) {
      searchQuery.value = ''
    }
  }

  const goToSearch = () => {
    const query = searchQuery.value.trim()
    if (validateSearchInput(query)) {
      const url = router.resolve({ path: '/search', query: { q: query } }).href
      window.open(url, '_blank')
    }
  }

  return {
    searchQuery,
    validateSearchInput,
    handleSearchInput,
    goToSearch,
  }
}
