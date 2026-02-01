import { myAxios } from '@/request'
import type { Article } from '@/api/article'

/**
 * 搜索文章
 * @param keyword 搜索关键词
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
    const response = await myAxios.get('/api/search', {
      params: {
        keyword: keyword.trim(),
        page,
        pageSize,
      },
    })

    if (response.data.code === 200) {
      return response.data.data || []
    } else {
      throw new Error(response.data.message || '搜索失败')
    }
  } catch (error) {
    console.error('搜索请求失败:', error)
    throw error
  }
}

/**
 * 搜索建议
 * @param keyword 搜索关键词
 * @returns 搜索建议列表
 */
export async function getSearchSuggestions(keyword: string): Promise<string[]> {
  if (!keyword || keyword.trim().length === 0) {
    return []
  }

  if (keyword.trim().length > 20) {
    return []
  }

  try {
    const response = await myAxios.get('/api/search/suggestions', {
      params: {
        keyword: keyword.trim(),
      },
    })

    if (response.data.code === 200) {
      return response.data.data || []
    } else {
      return []
    }
  } catch (error) {
    console.error('获取搜索建议失败:', error)
    return []
  }
}
