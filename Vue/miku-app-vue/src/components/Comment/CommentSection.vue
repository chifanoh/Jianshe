<template>
  <u-comment :config="config" @submit="submit" @reply-page="replyPage">
    <u-comment-nav v-model="latest" @sorted="sorted"></u-comment-nav>
  </u-comment>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watchEffect } from 'vue'
import { UToast } from 'undraw-ui'
import type { CommentReplyPageApi, CommentSubmitApi, ConfigApi } from 'undraw-ui'
import { getCommentList, submitComment, getRepliesByParentId } from '@/api/comment'
import { useLoginUserStore } from '@/store/useLoginUserStore'

const props = defineProps({
  articleId: {
    type: String,
    required: true,
  },
})

const loginUserStore = useLoginUserStore()

const config = reactive<ConfigApi>({
  user: {
    id: undefined,
    username: '未登录',
    avatar: '',
  } as any, // 当前用户信息
  comments: [], // 评论数据
  page: true, // 开启分页
  relativeTime: true, // 开启人性化时间
  show: {
    level: false, // 关闭等级显示
    homeLink: false, // 关闭个人主页链接跳转
    address: false, // 关闭地址信息
    likes: false, // 关闭点赞按钮显示
  },
})

// 监听登录用户状态变化，更新评论配置中的用户信息
watchEffect(() => {
  if (loginUserStore.isLogin) {
    config.user.id = loginUserStore.loginUser.id || ''
    config.user.username = loginUserStore.loginUser.userName
    config.user.avatar = loginUserStore.loginUser.userAvatar || ''
  } else {
    config.user.id = ''
    config.user.username = '未登录'
    config.user.avatar = ''
  }
})

// 模拟请求接口获取评论数据
onMounted(async () => {
  try {
    const res = await getCommentList(props.articleId)
    config.comments = res
  } catch (error) {
    console.error('获取评论失败:', error)
    UToast({ message: '获取评论失败', type: 'error' })
  }
})

// 评论提交事件
const submit = async ({ content, parentId, finish }: CommentSubmitApi) => {
  if (!loginUserStore.isLogin) {
    UToast({ message: '请先登录再发表评论', type: 'warn' })
    return
  }

  if (!loginUserStore.loginUser.id) {
    UToast({ message: '用户信息获取失败，请重新登录', type: 'error' })
    return
  }

  try {
    const newComment = await submitComment({
      content,
      parentId,
      uid: loginUserStore.loginUser.id,
      articleId: props.articleId,
    })
    finish(newComment)
    UToast({ message: '评论成功!', type: 'info' })
  } catch (error) {
    console.error('提交评论失败:', error)
    UToast({ message: '提交评论失败', type: 'error' })
  }
}

//回复分页
const replyPage = async ({ parentId, current, size, finish }: CommentReplyPageApi) => {
  try {
    const res = await getRepliesByParentId(parentId, current, size)
    finish(res)
  } catch (error) {
    console.error('获取回复失败:', error)
    UToast({ message: '获取回复失败', type: 'error' })
  }
}

//排序
const latest = ref(true)
const sorted = (latest: boolean) => {
  console.log(latest)
}
</script>

<style lang="scss" scoped></style>
