<template>
  <div class="news-carousel">
    <n-carousel
      class="n-carousel-item"
      autoplay
      :interval="3000"
      effect="card"
      prev-slide-style="transform: translateX(-120%) translateZ(-800px);"
      next-slide-style="transform: translateX(20%) translateZ(-800px);"
      style="height: 240px"
      :show-dots="true"
    >
      <n-carousel-item
        v-for="news in newsList"
        :key="news.id"
        :style="{ width: '70%' }"
        @click="handleClick(news)"
      >
        <div class="news-item">
          <img
            class="carousel-img"
            :src="news.imageUrl || defaultImage"
            :alt="news.title"
            @error="handleImageError"
          />
          <div class="news-overlay">
            <div class="news-content">
              <h3 class="news-title">{{ news.title }}</h3>
              <p v-if="news.description" class="news-desc">{{ news.description }}</p>
              <div class="news-meta">
                <span v-if="news.source" class="news-source">{{ news.source }}</span>
                <span v-if="news.publishTime" class="news-time">{{ formatTime(news.publishTime) }}</span>
              </div>
            </div>
          </div>
        </div>
      </n-carousel-item>
    </n-carousel>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDailyNews, type NewsItem } from '@/api/news'

// 默认图片
const defaultImage = 'https://picsum.photos/800/400?random=0'

// 新闻列表
const newsList = ref<NewsItem[]>([])
const loading = ref(false)

// 获取新闻数据
const fetchNews = async () => {
  loading.value = true
  try {
    const news = await getDailyNews(5)
    newsList.value = news
  } catch (error) {
    console.error('获取新闻失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理图片加载失败
const handleImageError = (e: Event) => {
  const target = e.target as HTMLImageElement
  target.src = defaultImage
}

// 处理点击
const handleClick = (news: NewsItem) => {
  if (news.url && news.url !== '#') {
    window.open(news.url, '_blank')
  }
}

// 格式化时间
const formatTime = (time: string) => {
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  // 小于1小时显示分钟
  if (diff < 3600000) {
    const minutes = Math.floor(diff / 60000)
    return minutes < 1 ? '刚刚' : `${minutes}分钟前`
  }
  // 小于24小时显示小时
  if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  }
  // 否则显示日期
  return date.toLocaleDateString('zh-CN', {
    month: 'short',
    day: 'numeric',
  })
}

// 组件挂载时获取新闻
onMounted(() => {
  fetchNews()
})

// 暴露刷新方法
defineExpose({
  refresh: fetchNews,
})
</script>

<style scoped>
.news-carousel {
  width: 100%;
}

.news-item {
  position: relative;
  width: 100%;
  height: 100%;
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.news-item:hover .carousel-img {
  transform: scale(1.05);
}

.news-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
}

.news-content {
  color: #fff;
}

.news-title {
  font-size: 16px;
  font-weight: 600;
  line-height: 1.4;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-desc {
  font-size: 13px;
  line-height: 1.4;
  margin: 0 0 8px 0;
  opacity: 0.9;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
  opacity: 0.8;
}

.news-source {
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.news-time {
  color: #ddd;
}

/* 轮播图样式覆盖 */
:deep(.n-carousel__dots) {
  bottom: 10px;
}

:deep(.n-carousel__dot) {
  background: rgba(255, 255, 255, 0.4);
}

:deep(.n-carousel__dot--active) {
  background: #fff;
}
</style>
