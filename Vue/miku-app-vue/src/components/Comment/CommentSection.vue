<template>
  <u-comment-scroll :disable="disable" @more="more">
    <u-comment
      :config="config"
      @submit="handleSubmit"
      @like="handleLike"
      @reply-page="handleReplyPage"
      @focus="handleFocus"
      @cancel="handleCancel"
    >
    </u-comment>
  </u-comment-scroll>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue'
import { UToast } from 'undraw-ui'
import type { CommentApi, CommentSubmitApi, ConfigApi, CommentReplyPageApi } from 'undraw-ui'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { getComments, submitComment, likeComment, getCommentReplies } from '@/api/comment'

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
        avatar: newUser.userAvatar || '/UserAvatar.svg',
      }
    } else {
      config.user = {
        id: '',
        username: '游客',
        avatar: '/UserAvatar.svg',
      }
    }
  },
  { immediate: true },
)

// 加载评论数据
const more = async (page: number = 1) => {
  try {
    const response = await getComments(props.articleId, page, pageSize.value)

    if (response.data.code === 200) {
      const { records } = response.data.data

      // 转换后端数据格式为组件需要的格式
      const formattedComments = records.map((comment: any) => ({
        id: comment.id,
        parentId: comment.parentId || null,
        uid: comment.userId,
        content: comment.content,
        likes: comment.likes || 0,
        createTime: comment.createTime,
        user: {
          username: comment.userName,
          avatar: comment.userAvatar,
        },
        reply:
          comment.replyCount > 0
            ? {
                total: comment.replyCount,
                list: [], // 初始化为空，通过reply-page事件加载
              }
            : null,
      }))

      config.comments = formattedComments
      currentPage.value = page

      UToast({
        message: `成功加载 ${formattedComments.length} 条评论`,
        type: 'success',
        duration: 1500,
      })
    } else {
      UToast({
        message: response.data.msg || '加载评论失败',
        type: 'error',
      })
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    UToast({
      message: '网络错误，请稍后重试',
      type: 'error',
    })
  }
}

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
        content: newComment.content,
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

// 点赞评论
const handleLike = async (id: string) => {
  try {
    const response = await likeComment(id)

    if (response.data.code === 200) {
      UToast({
        message: '点赞成功！',
        type: 'success',
        duration: 1500,
      })
    } else {
      UToast({
        message: response.data.msg || '点赞失败',
        type: 'error',
      })
    }
  } catch (error) {
    console.error('点赞失败:', error)
    UToast({
      message: '网络错误，请稍后重试',
      type: 'error',
    })
  }
}

// 加载评论回复 - 使用官方推荐的finish结构
const handleReplyPage = async ({ parentId, current, size, finish }: CommentReplyPageApi) => {
  try {
    const response = await getCommentReplies(parentId, current, size)

    if (response.data.code === 200) {
      const { records, total } = response.data.data

      // 转换回复数据格式
      const replyList = records.map((reply: any) => ({
        id: reply.id,
        parentId: reply.parentId,
        uid: reply.userId,
        content: reply.content,
        likes: reply.likes || 0,
        createTime: reply.createTime,
        user: {
          username: reply.userName,
          avatar: reply.userAvatar || '/UserAvatar.svg',
        },
      }))

      // 使用finish回调返回回复数据
      finish({
        total,
        list: replyList,
      })
    } else {
      UToast({
        message: response.data.msg || '加载回复失败',
        type: 'error',
      })
    }
  } catch (error) {
    console.error('加载回复失败:', error)
    UToast({
      message: '网络错误，请稍后重试',
      type: 'error',
    })
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

// 取消评论
const handleCancel = () => {
  // 可以在这里添加取消评论的逻辑
  console.log('取消评论')
}

// 组件挂载时加载评论数据
onMounted(async () => {
  // 确保用户信息已加载
  if (!loginUserStore.isLogin && loginUserStore.loginUser.userName === '未登录') {
    await loginUserStore.loadUserFromCache()
  }

  // 加载评论数据
  await more()
})

// 暴露给父组件的方法
defineExpose({
  more,
  refresh: () => more(currentPage.value),
})
</script>

<style lang="scss" scoped></style>
