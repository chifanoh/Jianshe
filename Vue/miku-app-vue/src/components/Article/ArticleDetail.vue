<template>
  <div class="main-section-container">
    <!-- 骨架屏加载状态 -->
    <div v-if="loading" class="skeleton-container">
      <div class="article-content">
        <!-- 左侧统计骨架 -->
        <div class="skeleton-sidebar-left">
          <n-skeleton height="80px" width="80px" :sharp="false" />
          <n-skeleton height="80px" width="80px" :sharp="false" />
          <n-skeleton height="80px" width="80px" :sharp="false" />
        </div>

        <!-- 中间内容骨架 -->
        <div class="skeleton-content-area">
          <div class="skeleton-header">
            <n-skeleton height="40px" width="40px" circle />
            <div class="skeleton-user-info">
              <n-skeleton height="16px" width="120px" :sharp="false" />
              <n-skeleton height="14px" width="80px" :sharp="false" />
            </div>
          </div>
          <n-skeleton height="28px" width="80%" :sharp="false" />
          <div class="skeleton-content">
            <n-skeleton
              v-for="i in 8"
              :key="i"
              height="14px"
              :width="getSkeletonLineWidth(i)"
              :sharp="false"
            />
          </div>
        </div>

        <!-- 右侧用户骨架 -->
        <div class="skeleton-sidebar-right">
          <n-skeleton height="200px" width="280px" :sharp="false" />
        </div>
      </div>
    </div>

    <!-- 文章内容 -->
    <div v-else-if="article" class="article-content">
      <div class="main-box flex">
        <!-- 左侧：评论/收藏/点赞 -->
        <div class="sub-section">
          <div class="post-state-section mr-20">
            <div class="stat-item mb-20">
              <div class="icon">
                <svg width="32" height="32" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M17.74 30L16 29l4-7h6a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h9v2H6a4 4 0 0 1-4-4V8a4 4 0 0 1 4-4h20a4 4 0 0 1 4 4v12a4 4 0 0 1-4 4h-4.84z"
                    fill="currentColor"
                  ></path>
                  <path d="M8 10h16v2H8z" fill="currentColor"></path>
                  <path d="M8 16h10v2H8z" fill="currentColor"></path>
                </svg>
              </div>
              <span class="stat-number">{{ article.comments }}</span>
              <div class="stat-label">评论</div>
            </div>

            <div class="stat-item mb-20">
              <div class="icon"></div>
              <img src="@sicons/antd/StarOutlined.svg" alt="" />
              <span class="stat-number">{{ article.favorite }}</span>
              <div class="stat-label">收藏</div>
            </div>

            <div class="stat-item mb-20">
              <div class="icon"></div>
              <img src="@sicons/antd/HeartOutlined.svg" alt="" />
              <span class="stat-number">{{ article.likes }}</span>
              <div class="stat-label">点赞</div>
            </div>
          </div>
        </div>

        <!-- 中间：文章标题和正文 -->
        <div class="flex">
          <div class="mr-12 main-section">
            <div class="detail-section">
              <div class="main-content-section">
                <!-- 文章头部 -->
                <section class="header-section mb-16">
                  <div class="title-content flex-between-center mb-16">
                    <h2 class="title text-24">{{ article.title }}</h2>
                  </div>
                  <div class="sub-title-section text-14 flex">
                    <span class="mr-24">文章发布于 {{ formatTime(article.createdTime) }}</span>
                    <span></span>
                  </div>
                </section>

                <!-- 文章正文 -->
                <div class="article-body">
                  <div class="article-text" v-html="formatContent(article.content)"></div>

                  <!-- 文章图片 -->
                  <div class="article-images" v-if="article.images && article.images.length">
                    <img
                      v-for="(image, index) in article.images"
                      :key="index"
                      :src="image"
                      :alt="`文章图片${index + 1}`"
                      class="article-image"
                      @click="previewImage(image)"
                    />
                  </div>
                  <div class="content-footer text-14">
                    <div class="left-section">
                      <span class="mr-24">浏览：{{ article.likes }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 评论区 -->
              <div class="comment-section mt-12">
                <CommentSection :article-id="article.id" />
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：用户卡片 -->
        <div class="sub-box">
          <n-affix :trigger-top="50" position="absolute" :listen-to="() => containerRef">
            <div class="inside-box">
              <div class="side-section">
                <div class="user-info-section flex mb-8">
                  <router-link :to="`/user/${article.user.id}`" class="user-link" target="_blank">
                    <!-- 用户头像 -->
                    <div
                      class="user-avatar icon mr-16"
                      style="width: 48px; height: 48px; min-width: 48px"
                    >
                      <div class="user-image mr-16">
                        <img
                          :src="article.user.avatar"
                          alt=""
                          style="object-fit: cover"
                          class="img-cover"
                        />
                      </div>
                    </div>
                  </router-link>
                  <!-- 用户信息 -->
                  <div class="info-section">
                    <router-link :to="`/user/${article.user.id}`" class="user-link" target="_blank">
                      <div class="user-name text-14">
                        <span class="name">{{ article.user.name }}</span>
                      </div>
                    </router-link>
                    <p class="interact-section text-14 mb-10">
                      <span class="mr-16 pointer"
                        >关注 <span class="number ml-4">{{ article.user.follow }}</span>
                      </span>
                      <span class="mr-16 pointer"
                        >粉丝 <span class="number ml-4">{{ article.user.fans }}</span>
                      </span>
                    </p>
                  </div>
                </div>
                <button class="follow-btn btn mt-12">
                  <img
                    src="@sicons/antd/PlusOutlined.svg"
                    alt=""
                    style="width: 12px; height: 12px"
                    class="mr-4 icon-white"
                  />
                  关注
                </button>
              </div>
            </div>
          </n-affix>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <div class="error-icon">😢</div>
      <h3>文章加载失败</h3>
      <p>{{ error }}</p>
      <button @click="loadArticle" class="retry-btn">重新加载</button>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <div class="empty-icon">📄</div>
      <h3>文章不存在</h3>
      <p>该文章可能已被删除或不存在</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail } from '@/api/article'
import type { Article } from '@/types/article'

// 路由和状态
const route = useRoute()
const article = ref<Article | null>(null)
const loading = ref(true)
const error = ref<string>('')
const containerRef = ref<HTMLElement | undefined>(undefined)

// 加载文章详情
const loadArticle = async () => {
  loading.value = true
  error.value = ''

  try {
    const articleId = route.params.id
    if (!articleId) {
      error.value = '文章ID不能为空'
      return
    }

    const response = await getArticleDetail(String(articleId))
    if (response.data.code === 200) {
      article.value = response.data.data
    } else {
      error.value = response.data.msg || '获取文章失败'
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('加载文章失败:', err)
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timeString: string) => {
  const date = new Date(timeString)
  const now = new Date()
  const diff = now.getTime() - date.getTime()

  // 转换为分钟、小时、天
  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

// 格式化内容（处理换行）
const formatContent = (content: string) => {
  return content.replace(/\n/g, '<br>')
}

// 预览图片
const previewImage = (imageUrl: string) => {
  // 这里可以集成图片预览组件
  window.open(imageUrl, '_blank')
}

// 获取骨架屏行宽度
const getSkeletonLineWidth = (index: number): string => {
  if (index % 3 === 0) return '70%'
  if (index % 3 === 1) return '90%'
  return '80%'
}

// 组件挂载时加载文章
onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.main-section-container {
  position: relative;
  margin: 0 auto;
  height: 100%;
  padding: 82px 0 24px;
  box-sizing: content-box;
}

/* Flexbox三栏布局 */
.article-content {
  height: 100%;
  width: 1200px;
  margin: 0 auto;
  position: relative;
  justify-content: center;
  z-index: 1;
}

.main-box {
  position: relative;
  justify-content: center;
}

.main-section {
  z-index: 0;
  height: 100%;
}

.detail-section {
  width: 740px;
  position: relative;
  border-radius: 8px;
}

.main-content-section {
  padding: 5px 32px 25px;
  background-color: #fff;
  border-radius: 8px;
}

.comment-section {
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 48px;
}

.sub-title-section {
  color: #aeb6c2;
  font-weight: 400;
  margin-bottom: 20px;
}

.content-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #aeb6c2;
  font-weight: 400;
}

.text-14 {
  font-size: 14px;
  line-height: 22px;
}

.mb-10 {
  margin-bottom: 10px;
}

.flex {
  display: flex;
}

.mr-12 {
  margin-right: 12px;
}

.mt-12 {
  margin-top: 12px;
}

.mb-8 {
  margin-bottom: 8px;
}

.mr-16 {
  margin-right: 16px;
}

.ml-4 {
  margin-left: 4px;
}

.mr-4 {
  margin-right: 4px;
}

/* 左侧统计栏 */
.sub-section {
  position: sticky;
  height: 100%;
  left: 0;
  top: 76px;
  bottom: 0;
}

.post-state-section {
  text-align: center;
  z-index: 10;
  user-select: none;
}

.mr-20 {
  margin-right: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.stat-item:hover {
  transform: translateY(-3px);
}

.stat-number {
  font-size: 14px;
  line-height: 14px;
  font-weight: 400;
  color: #8c95a3;
}

.stat-label {
  font-size: 12px;
  color: #555555;
}

/* 点赞效果 */
.stat-item {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  padding: 8px 4px;
}

.stat-item:hover {
  background: #f5f7fa;
  transform: translateY(-2px);
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}
.header-section {
  border-bottom: 1px solid #edeff5;
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mb-16 {
  margin-bottom: 16px;
}

.mr-24 {
  margin-right: 24px;
}

.title {
  font-size: 24px;
  line-height: 32px;
  font-weight: 400;
  color: #292b2f;
  margin: 0;
}

.text-24 {
  font-size: 24px;
  line-height: 32px;
  font-weight: 600;
}

.article-topics {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  max-width: 300px;
}

.topic-tag {
  padding: 4px 10px;
  background: #f0f7ff;
  color: #1890ff;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

/* 文章正文 */
.article-body {
  margin-bottom: 22px;
}

.article-text {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 20px;
}

.article-images {
  margin: 20px 0;
}

.article-image {
  width: 100%;
  max-width: 600px;
  height: auto;
  border-radius: 8px;
  margin: 12px 0;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.article-image:hover {
  transform: scale(1.02);
}

/* 右侧用户卡片 */
.sub-box {
  height: 100%;
  position: sticky;
  width: 300px;
  z-index: 0;
  will-change: top;
}

.side-section {
  margin-bottom: 12px;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.user-info-section {
  width: 100%;
}

.info-section {
  flex: 1;
  min-width: 0;
  box-sizing: border-box;
  list-style: none;
  margin: 0;
  padding: 0;
}

.follow-btn {
  width: 100%;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #1890ff;
  color: #fff;
  transition: all 0.3s ease;
}

.follow-btn:hover {
  background-color: #3aa0ff;
  box-shadow: 0 4px 12px rgba(20, 150, 255, 0.4);
}

.btn {
  font-size: 14px;
  font-weight: 500;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  user-select: none;
  outline: none;
}

.user-name {
  display: flex;
  align-items: center;
}

.user-link {
  color: #292b2f;
  text-decoration: none;
}

.user-link:hover {
  color: #1890ff;
}

.name {
  font-weight: 700;
  max-width: 155px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.interact-section {
  font-weight: 400;
  color: #8c95a3;
  margin: 0 0 10px;
  padding: 0;
}

.number {
  font-weight: 700;
  color: #000000;
}

.user-image {
  width: 100%;
  height: 100%;
  background: #f3f3f3;
  background-size: 100% 100%;
  border-radius: 100%;
  font-size: 12px;
  position: relative;
}

.img-cover {
  border-radius: 100%;
  height: 100%;
  width: 100%;
  height: auto;
}

/* 骨架屏样式 - Naive UI */
.skeleton-container {
  padding: 20px;
  height: 100%;
  width: 1200px;
  margin: 0 auto;
  position: relative;
  justify-content: center;
  z-index: 1;
}

.skeleton-sidebar-left {
  width: 80px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.skeleton-content-area {
  flex: 1;
  max-width: 700px;
  padding: 0 20px;
}

.skeleton-sidebar-right {
  width: 280px;
}

.skeleton-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 12px;
}

.skeleton-user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.skeleton-content {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 错误和空状态 */
.error-state,
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #666;
}

.error-icon,
.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.error-state h3,
.empty-state h3 {
  font-size: 20px;
  margin: 0 0 8px 0;
  color: #333;
}

.error-state p,
.empty-state p {
  font-size: 16px;
  margin: 0 0 24px 0;
  color: #666;
}

.retry-btn {
  padding: 12px 24px;
  background: #1890ff;
  color: #fff;
  border: none;
  border-radius: 24px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: #40a9ff;
  transform: translateY(-1px);
}

.icon-white {
  filter: invert(1) grayscale(100%) brightness(200%);
}
</style>
