// 评论对象
export interface CommentApi {
  id: string | number // 评论id
  parentId: string | number | null // 回复的父id, 一级评论为null
  uid: string | number // 用户id
  content: string // 评论内容
  address?: string // 用户地址
  likes?: number // 点赞数量
  createTime: string // 创建时间
  user: UserApi // 用户对象
  reply?: ReplyApi | null // 回复数据
}

// 回复对象
export interface ReplyApi {
  total: number // 回复总数
  list: CommentApi[] // 回复列表
}

// 用户对象
export interface UserApi {
  id: string | number // 用户id
  username: string // 用户名
  avatar: string // 用户头像
  level?: number // 用户等级
  homeLink?: string // 用户个人主页链接
  likeIds?: string[] | number[] // 点赞的评论数组id
}

// 评论配置参数
export interface ConfigApi {
  user: UserApi // 当前用户
  emoji: EmojiApi // 表情包数据
  comments: CommentApi[] // 评论数据
  replyShowSize?: number // 回复页大小
  show?: ShowApi // 显示对象
  aTarget?: '_blank' | '_parent' | '_self' | '_top' // 个人主页跳转方式
  mention?: MentionApi // @提及
  upload?: (files: File[], finish: (val: string[]) => void) => void // 图片上传事件
  page?: boolean // 是否分页
  relativeTime?: boolean // 是否开启人性化时间
}

interface ShowApi {
  form?: boolean // 是否显示评论表单
  content?: boolean // 是否显示评论内容
  level?: boolean // 是否显示等级
  likes?: boolean // 是否点赞
  address?: boolean // 是否显示地址
  homeLink?: boolean // 是否跳转个人主页地址
  reply?: boolean // 是否显示回复按钮
}

// 回复分页事件
export interface CommentReplyPageApi {
  parentId: string // 父id
  pageNum: any // 页数
  pageSize: number // 页大小
  finish: (reply: ReplyApi) => void // 回调完成覆盖回复数据
}

// 提及评论事件
export interface CommentSubmitApi {
  content: string // 评论内容
  parentId: string | null // 父id
  finish: (comment?: CommentApi) => void // 回调完成添加评论数据
  reply?: CommentApi // 回复数据
  mentionList?: any[] // 提及数据
}

// 提交评论的实际数据结构
export interface CommentPayload {
  content: string
  parentId: string | number | null
  uid: string | number
  articleId: string | number
}
