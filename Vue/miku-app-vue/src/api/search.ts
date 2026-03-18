import { myAxios } from '@/request'
import type { Article } from '@/api/article'

/**
 * 搜索文章
 * @param query 搜索关键词
 * @param page 页码
 * @param pageSize 每页数量
 * @returns 搜索结果列表
 */
export async function searchArticles(
  keyword: string,
  page: number = 1,
  pageSize: number = 20,
): Promise<Article[]> {
  if (!keyword || keyword.trim().length === 0) {
    throw new Error('搜索关键词不能为空')
  }

  if (keyword.trim().length > 20) {
    throw new Error('搜索关键词长度不能超过20个字符')
  }

  try {
    const formData = new FormData()
    formData.append('keyword', keyword.trim())
    formData.append('page', page.toString())
    formData.append('pageSize', pageSize.toString())

    const response = await myAxios.post('/api/search', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    if (response.data.code === 200) {
      return response.data.data || []
    } else {
      throw new Error(response.data.message || '搜索失败')
    }
  } catch (error) {
    console.error('请求失败:', error)
    throw error
  }
}
