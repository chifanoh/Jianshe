<template>
  <u-comment-scroll :disable="disable" @more="more">
    <u-comment :config="config" @submit="handleSubmit" @reply-page="replyPage" @focus="handleFocus">
      <!-- 把二级回复按钮暴露出来 -->
      <template #reply-btn> </template>
      <!-- 操作栏 - 只显示在当前用户的评论上 -->
      <template #operate="scope">
        <Operate
          v-if="scope.uid === loginUserStore.loginUser.id"
          :comment="scope"
          @remove="handleRemove"
        />
      </template>
    </u-comment>
  </u-comment-scroll>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue'
import { UToast } from 'undraw-ui'
import type { CommentApi, CommentSubmitApi, ConfigApi, CommentReplyPageApi } from 'undraw-ui'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { getComments, submitComment, deleteComment, getCommentReplies } from '@/api/comment'
import Operate from './Operate.vue'

// Props定义
interface Props {
  articleId: string
}

const props = withDefaults(defineProps<Props>(), {
  articleId: 'default-article-id',
})

// 状态管理
const loginUserStore = useLoginUserStore()

// 组件状态
const currentPage = ref(1)
const pageSize = ref(10)
// 是否禁用滚动加载评论
const disable = ref(false)

// 评论配置 - 使用官方推荐的config结构
const config = reactive<ConfigApi>({
  user: {
    id: '',
    username: '',
    avatar: '',
  },
  comments: [], // 评论数据
  page: true, // 开启分页
  relativeTime: true, // 开启人性化时间
  show: {
    level: false, // 关闭等级显示
    homeLink: false, // 关闭个人主页链接跳转
    address: false, // 关闭地址信息
    likes: false, // 开启点赞按钮显示
    reply: true, // 开启回复功能
    content: true, // 显示评论内容
    form: true, // 显示评论表单
  },
  replyShowSize: 3, // 回复显示数量
})

// 监听用户登录状态变化
watch(
  () => loginUserStore.loginUser,
  (newUser) => {
    if (newUser && newUser.id) {
      config.user = {
        id: newUser.id,
        username: newUser.userName,
        avatar:
          newUser.userAvatar ||
          'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMjAiIGN5PSIxNiIgcj0iNiIgZmlsbD0iI2NjYyIvPgo8cGF0aCBkPSJNMTIgMzJDOCAzMiA4IDI4IDggMjRIMzJDMzIgMjggMzIgMzIgMjggMzJIMTJ6IiBmaWxsPSIjY2NjIi8+Cjwvc3ZnPgo=',
      }
    } else {
      config.user = {
        id: '',
        username: '游客',
        avatar:
          'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMjAiIGN5PSIxNiIgcj0iNiIgZmlsbD0iI2NjYyIvPgo8cGF0aCBkPSJNMTIgMzJDOCAzMiA4IDI4IDggMjRIMzJDMzIgMjggMzIgMzIgMjggMzJIMTJ6IiBmaWxsPSIjY2NjIi8+Cjwvc3ZnPgo=',
      }
    }
  },
  { immediate: true },
)

// 提交评论 - 使用官方推荐的finish结构
const handleSubmit = async ({ content, parentId, finish }: CommentSubmitApi) => {
  try {
    // 检查用户是否登录
    if (!loginUserStore.isLogin) {
      UToast({
        message: '请先登录后再评论',
        type: 'warn',
      })
      return
    }

    // 调用API提交评论
    const response = await submitComment(content, props.articleId, parentId)
    if (response.data.code === 200) {
      const newComment = response.data.data

      // 构建符合组件格式的评论数据
      const comment: CommentApi = {
        id: newComment.id,
        parentId: newComment.parentId || null,
        uid: loginUserStore.loginUser.id || '',
        content: content,
        likes: newComment.likes || 0,
        createTime: newComment.createTime,
        user: {
          username: loginUserStore.loginUser.userName,
          avatar: loginUserStore.loginUser.userAvatar || '/UserAvatar.svg',
        },
        reply: null,
      }
      // 使用finish回调通知组件评论提交成功
      finish(comment)
      UToast({ message: '评论成功!', type: 'info' })
    } else {
      UToast({
        message: '评论失败',
        type: 'error',
      })
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    UToast({
      message: '网络错误，请稍后重试',
      type: 'error',
    })
  }
}

// 加载评论数据
const more = async (page: number = 1) => {
  try {
    const response = await getComments(props.articleId, page, pageSize.value)

    if (response.data.code === 200) {
      const { records, total } = response.data.data

      const formattedComments: CommentApi[] = records.map((c: any) => {
        // 映射后端评论结构到 CommentApi
        const comment: CommentApi = {
          id: c.id,
          parentId: c.parentId || null,
          uid: c.userId,
          content: c.content,
          likes: c.likes || 0,
          createTime: c.createTime,
          user: { username: c.userName, avatar: c.userAvatar },
          // 直接使用后端嵌套的 reply 对象
          reply: c.reply
            ? {
                total: c.reply.total,
                list: c.reply.list.map((r: any) => ({
                  id: r.id,
                  parentId: r.parentId,
                  uid: r.userId,
                  content: r.content,
                  likes: r.likes || 0,
                  createTime: r.createTime,
                  user: { username: r.userName, avatar: r.userAvatar },
                  reply: null, // 回复的回复暂时不处理
                })),
              }
            : null, // 如果没有回复，则设置为 null
        }
        return comment
      })

      if (page === 1) {
        config.comments = formattedComments
      } else {
        config.comments = [...config.comments, ...formattedComments]
      }

      currentPage.value = page
      const hasMore = config.comments.length < total
      disable.value = !hasMore

      UToast({
        type: 'success',
        duration: 1500,
      })
    } else {
      UToast({ message: response.data.msg || '加载评论失败', type: 'error' })
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    UToast({ message: '网络错误，请稍后重试', type: 'error' })
  }
}

// 评论框获得焦点
const handleFocus = () => {
  if (!loginUserStore.isLogin) {
    UToast({
      message: '请先登录后再评论',
      type: 'warn',
    })
  }
}

// 处理删除评论
const handleRemove = async (comment: CommentApi) => {
  try {
    // 检查用户是否登录
    if (!loginUserStore.isLogin) {
      UToast({
        message: '请先登录后再操作',
        type: 'warn',
      })
      return
    }

    // 调用API删除评论
    const response = await deleteComment(String(comment.id))
    if (response.data.code === 200) {
      // 从评论列表中移除
      config.comments = config.comments.filter((c) => c.id !== comment.id)
      UToast({ message: '删除成功', type: 'info' })
    } else {
      UToast({
        message: response.data.message || '删除失败',
        type: 'error',
      })
    }
  } catch (error) {
    console.error('删除评论失败:', error)
    UToast({
      message: '网络错误，请稍后重试',
      type: 'error',
    })
  }
}

// 回复分页加载
const replyPage = async ({ parentId, current, size, finish }: CommentReplyPageApi) => {
  try {
    const response = await getCommentReplies(parentId, current, size)

    if (response.data.code === 200) {
      const { records, total } = response.data.data

      const formattedReplies = records.map((r: any) => ({
        id: r.id,
        parentId: r.parentId,
        uid: r.userId,
        content: r.content,
        likes: r.likes || 0,
        createTime: r.createTime,
        user: { username: r.userName, avatar: r.userAvatar },
        reply: null,
      }))

      finish({
        total: total,
        list: formattedReplies,
      })
    } else {
      UToast({ message: response.data.msg || '加载回复失败', type: 'error' })
      finish({ total: 0, list: [] })
    }
  } catch (error) {
    console.error('加载回复失败:', error)
    UToast({ message: '网络错误，请稍后重试', type: 'error' })
    finish({ total: 0, list: [] })
  }
}

// 组件挂载时加载评论数据
onMounted(async () => {
  await more(1)
})

// 暴露给父组件的方法
defineExpose({
  more,
  refresh: () => more(currentPage.value),
})
</script>

<style lang="scss" scoped></style>
