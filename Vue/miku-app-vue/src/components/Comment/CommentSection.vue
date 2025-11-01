<template>
  <u-comment :config="config" @submit="submit">
    <u-comment-nav v-model="latest" @sorted="sorted"></u-comment-nav>
  </u-comment>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { UToast, Time, CommentApi, CommentSubmitApi, ConfigApi } from 'undraw-ui'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { getCommentList, submitComment } from '@/api/comment'

const props = defineProps({
  articleId: {
    type: String,
    required: true,
  },
})

const loginUserStore = useLoginUserStore()

const config = reactive<ConfigApi>({
  user: {
    id: loginUserStore.loginUser.id,
    username: loginUserStore.loginUser.userName,
    avatar: loginUserStore.loginUser.userAvatar,
  } as any, // 当前用户信息
  comments: [], // 评论数据
  relativeTime: true, // 开启人性化时间
  show: {
    level: false, // 关闭等级显示
    homeLink: false, // 关闭个人主页链接跳转
    address: false, // 关闭地址信息
    likes: false, // 关闭点赞按钮显示
  },
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

//排序
const latest = ref(true)
const sorted = (latest: boolean) => {
  console.log(latest)
}
</script>

<style lang="scss" scoped></style>
