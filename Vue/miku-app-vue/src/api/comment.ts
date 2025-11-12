import { myAxios } from '@/request'

/**
 * 评论相关的API接口
 */

/**
 * 获取评论列表
 * @param articleId 文章ID
 * @param page 页码
 * @param size 每页大小
 * @returns 评论列表数据
 */
export const getComments = async (articleId: string, page: number = 1, pageSize: number = 10) => {
  return myAxios.request({
    url: `/api/comment/list/${articleId}`,
    method: 'GET',
    params: {
      page,
      pageSize,
    },
  })
}

/**
 * 提交评论
 * @param content 评论内容
 * @param articleId 文章ID
 * @param parentId 父评论ID（可选）
 * @returns 提交的评论数据
 */
export const submitComment = async (
  content: string,
  articleId: string,
  parentId: string | null,
) => {
  return myAxios.request({
    url: '/api/comment/submit',
    method: 'POST',
    data: {
      content,
      articleId,
      parentId,
    },
  })
}

/**
 * 删除评论
 * @param commentId 评论ID
 * @returns 删除结果
 */
export const deleteComment = async (commentId: string) => {
  return myAxios.request({
    url: `/api/comment/delete/${commentId}`,
    method: 'DELETE',
  })
}

/**
 * 获取评论回复列表
 * @param parentId 父评论ID
 * @param page 页码
 * @param size 每页大小
 * @returns 回复列表数据
 */
export const getCommentReplies = async (parentId: string, page: number = 1, size: number = 5) => {
  return myAxios.request({
    url: '/api/comment/replies',
    method: 'GET',
    params: {
      parentId,
      page,
      size,
    },
  })
}
