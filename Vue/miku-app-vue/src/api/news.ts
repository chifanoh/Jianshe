import { myAxios } from '@/request'

/**
 * 新闻数据类型
 */
export interface NewsItem {
  id: string
  title: string
  description?: string
  url: string
  imageUrl?: string
  source?: string
  publishTime?: string
}

export interface NewsResponse {
  code: number
  msg: string | null
  data: {
    records: NewsItem[]
  }
}

/**
 * 获取每日新闻（从后端获取，后端已做缓存）
 * 如果后端返回空数据或失败，使用前端模拟数据
 */
export const getDailyNews = async (count: number = 5): Promise<NewsItem[]> => {
  try {
    const { data: response } = await myAxios.request<NewsResponse>({
      url: '/api/news/list',
      method: 'GET',
      params: {
        pageSize: count,
      },
    })

    // 后端返回成功且有数据
    if (response.code === 200 && response.data?.records && response.data.records.length > 0) {
      return response.data.records
    }

    // 后端返回空数据，使用模拟数据
    return getMockNews(count)
  } catch (error) {
    console.error('获取新闻失败:', error)
    // 后端请求失败，使用模拟数据
    return getMockNews(count)
  }
}

/**
 * 模拟数据（作为备用）
 */
const getMockNews = (count: number = 5): NewsItem[] => {
  const mockNews: NewsItem[] = [
    {
      id: '1',
      title: '科技前沿：AI 技术持续突破，引领行业变革',
      description: '人工智能技术在各领域不断取得新突破...',
      url: '#',
      imageUrl: 'https://picsum.photos/800/400?random=1',
      source: '科技日报',
      publishTime: new Date().toISOString(),
    },
    {
      id: '2',
      title: '绿色发展：可再生能源投资创新高',
      description: '全球可再生能源投资持续增长...',
      url: '#',
      imageUrl: 'https://picsum.photos/800/400?random=2',
      source: '财经新闻',
      publishTime: new Date().toISOString(),
    },
    {
      id: '3',
      title: '文化传承：传统手工艺焕发新生机',
      description: '传统手工艺在现代设计中焕发新的生命力...',
      url: '#',
      imageUrl: 'https://picsum.photos/800/400?random=3',
      source: '文化周刊',
      publishTime: new Date().toISOString(),
    },
    {
      id: '4',
      title: '健康生活：科学运动成为新时尚',
      description: '越来越多的人开始关注科学运动...',
      url: '#',
      imageUrl: 'https://picsum.photos/800/400?random=4',
      source: '健康时报',
      publishTime: new Date().toISOString(),
    },
    {
      id: '5',
      title: '数字经济：电商直播带动新消费',
      description: '直播电商成为消费增长新引擎...',
      url: '#',
      imageUrl: 'https://picsum.photos/800/400?random=5',
      source: '商业周刊',
      publishTime: new Date().toISOString(),
    },
  ]
  return mockNews.slice(0, count)
}

/**
 * 获取今日头条热点
 * 使用 NewsAPI 的头条新闻
 */
export const getHotNews = async (): Promise<NewsItem[]> => {
  return getDailyNews(5)
}
