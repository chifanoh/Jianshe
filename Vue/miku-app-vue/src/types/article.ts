/**
 * 文章相关类型定义
 */

// 文章作者信息
export interface ArticleAuthor {
  id: string | number
  name: string
  avatar: string
  follow: number
  fans: number
  sex?: number
}

// 文章话题信息
export interface ArticleTopic {
  id: string | number
  name: string
}

// 文章数据结构 - 适配后端实际返回格式
export interface Article {
  id: string | number
  user: ArticleAuthor
  title: string
  content: string
  images: string[] | null
  topics: ArticleTopic[] | null
  views: number
  comments: number
  likes: number
  favorite: number
  createdTime: string
  updatedTime?: string
}

// 文章列表响应数据 - 适配后端实际返回格式
export interface ArticleListResponse {
  code: number
  msg: string | null
  data: {
    records: Article[]
    total: number
  }
}

// 文章查询参数
export interface ArticleQueryParams {
  page?: number
  pageSize?: number
  userId?: string | number
  topicId?: string | number
  keyword?: string
}

// 创建文章参数
export interface CreateArticleParams {
  title: string
  content: string
  images?: string[]
  topicIds?: string[] | number[]
}

// 更新文章参数
export interface UpdateArticleParams {
  title?: string
  content?: string
  images?: string[]
  topicIds?: string[] | number[]
}
