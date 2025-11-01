import { myAxios } from '@/request'
import type { CommentApi, CommentPayload } from '@/types/commentSection'

// 获取评论列表
export function getCommentList(articleId: string): Promise<CommentApi[]> {
  return myAxios.get(`/comment/list/${articleId}`)
}

// 提交评论
export function submitComment(data: CommentPayload): Promise<CommentApi> {
  return myAxios.post('/comment/submit', data)
}
