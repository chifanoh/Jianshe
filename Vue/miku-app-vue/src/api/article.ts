import { myAxios } from '@/request'

/**
 * 文章数据类型定义
 */
export interface ArticleUser {
  id: string | number
  name: string
  avatar: string
  follow: number
  fans: number
  sex?: number
}

export interface Article {
  id: string | number
  user: ArticleUser
  title: string
  content: string
  images: string[] | null
  views: number
  comments: number
  likes: number
  createdTime: string
  localDateTime?: string
  updatedTime?: string
}

export interface ArticleListResponse {
  code: number
  msg: string | null
  data: {
    records: Article[]
    total: number
  }
}

export interface CreateArticleResponse {
  code: number
  msg: string | null
  data: Article
}

/**
 * 获取文章列表
 * @param params 查询参数
 * @returns 文章列表数据
 */
export const getArticleList = async (params?: {
  page?: number
  pageSize?: number
  userId?: string | number
}) => {
  return myAxios.request<ArticleListResponse>({
    url: '/api/article/list',
    method: 'GET',
    params,
  })
}

/**
 * 获取单个文章详情
 * @param id 文章ID
 * @returns 文章详情数据
 */
export const getArticleDetail = async (id: string | number) => {
  return myAxios.request({
    url: `/api/article/${id}`,
    method: 'GET',
  })
}

/**
 * 创建新文章
 * @param data 文章数据
 * @returns 创建结果
 */
export const createArticle = async (data: {
  title: string
  content: string
  images?: string[]
}) => {
  return myAxios.request<CreateArticleResponse>({
    url: '/api/article/create',
    method: 'POST',
    data,
  })
}

/**
 * 更新文章
 * @param id 文章ID
 * @param data 更新数据
 * @returns 更新结果
 */
export const updateArticle = async (
  id: string | number,
  data: {
    title?: string
    content?: string
    images?: string[]
  },
) => {
  return myAxios.request({
    url: `/api/article/update/${id}`,
    method: 'POST',
    data,
  })
}

/**
 * 删除文章
 * @param id 文章ID
 * @returns 删除结果
 */
export const deleteArticle = async (id: string | number) => {
  return myAxios.request({
    url: `/api/article/delete/${id}`,
    method: 'POST',
  })
}

/**
 * 点赞文章
 * @param id 文章ID
 * @returns 点赞结果
 */
export const toggleLikeApi = async (id: string | number) => {
  return myAxios.request({
    url: `/api/article/like/${id}`,
    method: 'POST',
  })
}

/**
 * 获取用户发布的文章列表
 * @param userId 用户ID
 * @param params 分页参数
 * @returns 用户文章列表
 */
export const getUserArticles = async (
  userId: string | number,
  params?: {
    page?: number
    pageSize?: number
  },
) => {
  return myAxios.request<ArticleListResponse>({
    url: `/api/article/user/${userId}`,
    method: 'GET',
    params,
  })
}

/**
 * 获取文章点赞状态
 * @param articleId 文章ID
 * @returns 点赞状态（true/false）
 */
export const getLikeStatus = async (articleId: string | number) => {
  return myAxios.request<{
    code: number
    msg: string | null
    data: boolean
  }>({
    url: `/api/article/status/${articleId}`,
    method: 'GET',
  })
}
