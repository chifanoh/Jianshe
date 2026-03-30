<template>
  <div class="post-list">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div v-for="i in 3" :key="i" class="article-card skeleton">
        <div class="article-header">
          <div class="article-userinfo">
            <div class="article-user">
              <div class="avatar skeleton-avatar"></div>
              <div class="username skeleton-text"></div>
            </div>
            <div class="post-time skeleton-text"></div>
          </div>
        </div>
        <div class="article-content">
          <div class="title">
            <div class="title-text skeleton-title"></div>
          </div>
          <div class="content skeleton-content"></div>
        </div>
      </div>
    </div>

    <!-- 文章列表 -->
    <div v-else-if="articles.length > 0" class="articles">
      <ArticleCard v-for="article in articles" :key="article.id" :article="article" />
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="refresh" class="retry-btn">重新加载</button>
    </div>

    <!-- 空状态 -->
    <div v-else-if="articles.length === 0" class="empty">
      <p>暂无文章</p>
    </div>

    <!-- 加载更多 -->
    <div v-if="!loading" class="load-more-container">
      <div v-if="hasMore" class="load-more">
        <button @click="loadMore" :disabled="loadingMore" class="load-more-btn">
          <span v-if="loadingMore" class="loading-spinner"></span>
          {{ loadingMore ? '加载中...' : '加载更多' }}
        </button>
      </div>
      <div v-else-if="articles.length > 0" class="no-more">
        <p>已经到底啦～</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import ArticleCard from './ArticleCard.vue'
import { getArticleList } from '@/api/article'
import type { Article, ArticleListResponse } from '@/types/article'

// 响应式数据
const articles = ref<Article[]>([])
const loading = ref(false)
const loadingMore = ref(false)
const error = ref<string | null>(null)
const page = ref(1)
const pageSize = ref(20) // 初始加载20篇
const total = ref(0)
const hasMore = ref(true)
const isInitialLoad = ref(true) // 标记是否为初始加载

// 计算总页数
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 获取文章列表
const fetchArticles = async (isLoadMore = false) => {
  // 重置错误状态
  error.value = null

  if (!isLoadMore) {
    loading.value = true
    isInitialLoad.value = true
  } else {
    loadingMore.value = true
    isInitialLoad.value = false
  }

  try {
    const { data: response }: { data: ArticleListResponse } = await getArticleList({
      page: page.value,
      pageSize: pageSize.value,
    })

    if (response.code === 200) {
      const newArticles = response.data.records

      // 使用 nextTick 确保 DOM 更新流畅
      await nextTick()

      if (isLoadMore) {
        // 加载更多时平滑追加
        articles.value.push(...newArticles)

        // 滚动到新加载内容的起始位置，提供视觉反馈
        const newItems = document.querySelectorAll('.article-card')
        const lastOldIndex = articles.value.length - newArticles.length - 1
        if (lastOldIndex >= 0 && newItems[lastOldIndex]) {
          setTimeout(() => {
            newItems[lastOldIndex].scrollIntoView({ behavior: 'smooth', block: 'nearest' })
          }, 100)
        }
      } else {
        // 初始加载或刷新
        articles.value = newArticles
        if (articles.value.length > 0) {
          window.scrollTo({ top: 0, behavior: 'smooth' })
        }
      }

      total.value = response.data.total
      // 支持后端用 total: -1 表示还有更多数据
      if (total.value === -1) {
        hasMore.value = newArticles.length === pageSize.value
      } else {
        hasMore.value = page.value < totalPages.value
      }
    } else {
      error.value = response.msg || '获取文章列表失败'
    }
  } catch (err) {
    error.value = '网络错误，请检查网络连接'
    console.error('获取文章列表出错:', err)
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 防抖函数
const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number,
): ((...args: Parameters<T>) => void) => {
  let timeout: ReturnType<typeof setTimeout>
  return function executedFunction(...args: any[]) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 加载更多 - 添加防抖
const loadMore = debounce(() => {
  if (hasMore.value && !loadingMore.value && !loading.value) {
    page.value++
    fetchArticles(true)
  }
}, 300)

// 刷新数据
const refresh = () => {
  page.value = 1
  hasMore.value = true
  error.value = null
  fetchArticles()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchArticles()
})

// 暴露方法给父组件
defineExpose({
  refresh,
  loadMore,
})
</script>

<style scoped>
.post-list {
  width: 100%;
}

.loading {
  padding: 20px 0;
}

.skeleton {
  padding: 24px 30px;
  border-bottom: 1px solid #ebebeb;
}

.skeleton-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f0f0f0;
  margin-right: 12px;
}

.skeleton-text {
  height: 16px;
  background: #f0f0f0;
  border-radius: 4px;
  margin: 4px 0;
}

.skeleton-title {
  width: 200px;
  height: 20px;
  background: #f0f0f0;
  border-radius: 4px;
  margin-bottom: 8px;
}

.skeleton-content {
  width: 100%;
  height: 18px;
  background: #f0f0f0;
  border-radius: 4px;
  margin: 8px 0;
}

.empty {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.empty p {
  margin: 0;
  font-size: 16px;
}

.error {
  text-align: center;
  padding: 40px 20px;
  color: #ff4d4f;
}

.error p {
  margin: 0 0 16px 0;
  font-size: 16px;
}

.retry-btn {
  padding: 8px 24px;
  border: 1px solid #ff4d4f;
  background: white;
  color: #ff4d4f;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: #ff4d4f;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(255, 77, 79, 0.3);
}

.load-more-container {
  text-align: center;
  padding: 20px;
}

.load-more {
  margin: 20px 0;
}

.load-more-btn {
  padding: 10px 30px;
  border: 1px solid #1890ff;
  background: white;
  color: #1890ff;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-width: 140px;
  justify-content: center;
}

.load-more-btn:hover:not(:disabled) {
  background: #1890ff;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.3);
}

.load-more-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  border: 2px solid #1890ff;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.no-more {
  color: #999;
  font-size: 14px;
  margin: 20px 0;
}

.no-more p {
  margin: 0;
}

.articles {
  width: 100%;
  transition: all 0.3s ease;
}
</style>
