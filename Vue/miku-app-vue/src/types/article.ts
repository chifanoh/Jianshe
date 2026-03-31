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

// 文章数据结构 - 适配后端实际返回格式
export interface Article {
  id: string | number
  user: ArticleAuthor
  title: string
  content: string
  summary?: string // 文章摘要，用于列表展示
  images: string[] | null
  views: number
  comments: number
  likesCount: number
  createdTime: string
  updatedTime?: string
}

// 文章列表响应数据 - 适配后端实际返回格式（游标分页）
export interface ArticleListResponse {
  code: number
  msg: string | null
  data: {
    records: Article[]
    total: number
    cursor: number | null // 游标，用于下一页查询
  }
}

// 文章查询参数（游标分页）
export interface ArticleQueryParams {
  pageSize?: number
  cursor?: number | null
  userId?: string | number
  keyword?: string
}

// 创建文章参数
export interface CreateArticleParams {
  title: string
  content: string
  images?: string[]
}

// 更新文章参数
export interface UpdateArticleParams {
  title?: string
  content?: string
  images?: string[]
}
