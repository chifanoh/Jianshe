import { myAxios } from '@/request'
import type { CommentApi, CommentPayload, ReplyApi } from '@/types/commentSection'

// 获取评论列表
export function getCommentList(articleId: string): Promise<CommentApi[]> {
  return myAxios.get(`/comment/list/${articleId}`)
}

// 提交评论
export function submitComment(data: CommentPayload): Promise<CommentApi> {
  return myAxios.post('/comment/submit', data)
}

// 获取评论回复列表
export function getRepliesByParentId(
  parentId: string,
  current: number,
  size: number,
): Promise<ReplyApi> {
  return myAxios.get(`/comment/replies/${parentId}`, { params: { current, size } })
}
