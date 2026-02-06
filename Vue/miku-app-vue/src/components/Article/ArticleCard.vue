<template>
  <div class="article-card">
    <!-- 用户信息 -->
    <div class="article-header">
      <div class="article-userinfo">
        <div class="article-user">
          <router-link :to="'/user/' + (articleData.user.id || 'default')" class="avatar-link">
            <div class="article-avatar" style="width: 40px; height: 40px; min-width: 40px">
              <img :src="articleData.user.avatar" :alt="articleData.user.name" class="avatar" />
            </div>
          </router-link>
          <router-link :to="`/user/${articleData.user.id || 'default'}`" class="username-link">
            <div class="ml-12">
              <div class="username">{{ articleData.user.name }}</div>
            </div>
          </router-link>
        </div>
        <span class="divider">
          <span class="post-time">{{ formattedTime }}</span>
        </span>
        <button class="follow-btn">关注</button>
      </div>
    </div>

    <!-- 内容区域 -->
    <router-link :to="`/post/${articleData.id || 'default'}`" class="content-link" target="_blank">
      <div class="article-content">
        <div class="mb-8">
          <div class="title">
            <span class="title-text">{{ articleData.title }}</span>
          </div>
          <article class="content" v-html="firstParagraphContent"></article>
        </div>
        <div class="preview">
          <div v-if="articleData.images && articleData.images.length > 0" class="image-preview">
            <img
              v-for="(image, index) in articleData.images.slice(0, 3)"
              :key="index"
              :src="image"
              :alt="`图片${index + 1}`"
              class="preview-img"
            />
            <div v-if="articleData.images.length > 3" class="image-count">
              +{{ articleData.images.length - 3 }}
            </div>
          </div>
        </div>
      </div>
    </router-link>

    <!-- 互动数据 -->
    <div class="footer">
      <div class="interaction">
        <span class="stat">
          <svg width="20" height="20" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M17.74 30L16 29l4-7h6a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h9v2H6a4 4 0 0 1-4-4V8a4 4 0 0 1 4-4h20a4 4 0 0 1 4 4v12a4 4 0 0 1-4 4h-4.84z"
              fill="currentColor"
            ></path>
            <path d="M8 10h16v2H8z" fill="currentColor"></path>
            <path d="M8 16h10v2H8z" fill="currentColor"></path>
          </svg>
          {{ articleData.comments }}
        </span>
        <span class="stat">
          <svg
            width="20"
            height="20"
            viewBox="0 0 32 32"
            xmlns="http://www.w3.org/2000/svg"
            class="mr-2"
          >
            <path
              d="M22.45 6a5.47 5.47 0 0 1 3.91 1.64a5.7 5.7 0 0 1 0 8L16 26.13L5.64 15.64a5.7 5.7 0 0 1 0-8a5.48 5.48 0 0 1 7.82 0l2.54 2.6l2.53-2.58A5.44 5.44 0 0 1 22.45 6m0-2a7.47 7.47 0 0 0-5.34 2.24L16 7.36l-1.11-1.12a7.49 7.49 0 0 0-10.68 0a7.72 7.72 0 0 0 0 10.82L16 29l11.79-11.94a7.72 7.72 0 0 0 0-10.82A7.49 7.49 0 0 0 22.45 4z"
              fill="currentColor"
            ></path>
          </svg>
          {{ articleData.likes }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Article } from '@/types/article'

interface Props {
  article?: Article
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  article: () => ({
    id: '',
    user: {
      id: '',
      name: '',
      avatar: '',
      follow: 0,
      fans: 0,
    },
    title: '',
    content: '',
    images: [],

    comments: 0,
    likes: 0,
    favorite: 0,
    views: 0,
    createdTime: new Date().toISOString(),
  }),
  loading: false,
})

// 解构文章数据
const articleData = computed(() => props.article)

/**
 * 格式化时间
 * @param date 时间
 * @returns 格式化后的时间
 */
const formatTimeAgo = (date: string | number | Date): string => {
  const now = new Date()
  const target = new Date(date)
  const diff = now.getTime() - target.getTime()

  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  const months = Math.floor(days / 30)
  const years = Math.floor(days / 365)

  if (years > 0) return `${years}年前`
  if (months > 0) return `${months}个月前`
  if (days > 0) return `${days}天前`
  if (hours > 0) return `${hours}小时前`
  if (minutes > 0) return `${minutes}分钟前`
  return '刚刚'
}

const formattedTime = computed(() => {
  const time = articleData.value.createdTime
  return formatTimeAgo(time)
})

/**
 * 截取文章内容的第一个段落
 * @param content 文章内容
 * @returns 第一个段落的内容
 */
const firstParagraphContent = computed(() => {
  const content = articleData.value.content
  if (!content) return ''

  try {
    const parser = new DOMParser()
    const doc = parser.parseFromString(content, 'text/html')
    const body = doc.body

    for (const node of Array.from(body.childNodes)) {
      if (node.nodeType === Node.ELEMENT_NODE) {
        const element = node as HTMLElement
        if (element.textContent && element.textContent.trim() !== '') {
          return element.textContent.trim()
        }
      } else if (node.nodeType === Node.TEXT_NODE) {
        if (node.textContent && node.textContent.trim() !== '') {
          return node.textContent.trim()
        }
      }
    }
    const plainText = content.replace(/<[^>]*>/g, '')
    return plainText.substring(0, 150) + (plainText.length > 150 ? '...' : '')
  } catch (e) {
    console.error('Error parsing HTML content:', e)
    const plainText = content.replace(/<[^>]*>/g, '')
    return plainText.substring(0, 150) + (plainText.length > 150 ? '...' : '')
  }
})
</script>

<style scoped>
.article-card {
  padding: 24px 30px;
  margin-bottom: 8px;
  background-color: #fff;
  border-radius: 8px;
}

.article-header {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  margin-bottom: 8px;
  position: relative;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.article-userinfo {
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-flex: 1;
  flex-grow: 1;
}

.article-user {
  display: flex;
  align-items: center;
}

.ml-12 {
  margin-left: 12px;
}

.username {
  font-size: 14px;
  color: #393f4d;
  max-width: 180px;
  line-height: 20px;
}

.avatar-link {
  text-decoration: none;
}

.username-link {
  text-decoration: none;
}

.username-link:hover .username {
  color: #a4a4a4;
}

.content-link {
  text-decoration: none;
  color: inherit;
  display: block;
}

.content-link:hover .title-text {
  color: #a4a4a4;
}

.post-time {
  color: #ccc;
  display: flex;
  align-items: center;
  margin-left: 10px;
  font-size: 12px;
}

.follow-btn {
  padding: 4px 12px;
  border: 1px solid #fe2c55;
  background: white;
  color: #fe2c55;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  position: absolute;
  right: 0;
  top: 0;
  transition: all 0.2s ease;
}

.follow-btn:hover {
  background: #fe2c55;
  color: white;
}

.mb-8 {
  margin-bottom: 8px;
}

.title {
  font-size: 16px;
  line-height: 24px;
  margin-bottom: 4px;
  color: #292b2f;
  font-weight: 500;
  display: flex;
  align-items: center;
  position: relative;
  padding-right: 100px;
}

.content {
  color: #586370;
  width: 100%;
  word-break: break-all;
  font-size: 14px;
  line-height: 22px;
  margin-bottom: 4px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.preview {
  margin-top: 10px;
  overflow: hidden;
  display: -webkit-inline-box;
  display: -ms-inline-flexbox;
  display: inline-flex;
  vertical-align: top;
  position: relative;
}

.image-preview {
  position: relative;
  margin-top: 12px;
}

.preview-img {
  width: 100%;
  max-height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.image-count {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0;
  margin-top: 7px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.interaction {
  display: flex;
  gap: 20px;
  align-items: center;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #999;
  font-size: 14px;
}

.icon {
  font-style: normal;
}

.mr-2 {
  margin-right: 2px;
}
</style>
