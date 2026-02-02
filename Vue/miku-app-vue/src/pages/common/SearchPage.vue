<template>
  <div class="main-section-container">
    <div class="mask"></div>
    <div class="main-content-section">
      <div class="search-page">
        <!-- 搜索结果标题 -->
        <p class="txt text-14 mx-24 pb-16 border">搜索"{{ keyword }}"，结果如下：</p>

        <!-- 内容区域 -->
        <div class="content-container">
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-container">
            <div class="loading-text">搜索中...</div>
          </div>

          <!-- 搜索结果 -->
          <div v-else-if="articles.length > 0" class="search-results">
            <ArticleCard v-for="article in articles" :key="article.id" :article="article" />
          </div>

          <!-- 无结果状态 -->
          <div v-else class="no-results">
            <p class="no-results-text">未找到相关结果</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { searchArticles } from '@/api/search'
import ArticleCard from '@/components/Article/ArticleCard.vue'
import type { Article } from '@/api/article'

const route = useRoute()
const keyword = ref('')
const articles = ref<Article[]>([])
const loading = ref(false)

const performSearch = async (searchKeyword: string) => {
  if (!searchKeyword?.trim()) {
    articles.value = []
    return
  }

  loading.value = true
  try {
    articles.value = await searchArticles(searchKeyword.trim())
  } catch (error) {
    console.error('搜索失败:', error)
    articles.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  keyword.value = (route.query.q as string) || ''
  performSearch(keyword.value)
})

watch(
  () => route.query.q,
  (newQuery) => {
    keyword.value = (newQuery as string) || ''
    performSearch(keyword.value)
  },
)
</script>

<style scoped>
.main-section-container {
  position: relative;
  margin: 0 auto;
  height: 100%;
  padding: 80px 0 24px;
  box-sizing: content-box;
}

.main-content-section {
  height: 100%;
  width: 1200px;
  margin: 0 auto;
  position: relative;
  justify-content: center;
  z-index: 1;
}

.search-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  max-width: 800px;
  margin: 0 auto;
}

.content-container {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.txt {
  font-weight: 400;
  color: #aeb6c2;
}

.border {
  border-bottom: 0.5px solid #edeff5;
}

.text-14 {
  font-size: 14px;
  line-height: 22px;
}

.mx-24 {
  margin-left: 24px;
  margin-right: 24px;
}

.pb-16 {
  padding-bottom: 16px;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  padding: 40px 0;
}

.loading-text {
  color: #666;
  font-size: 14px;
}

.search-results {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.no-results {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

.no-results-text {
  color: #999;
  font-size: 14px;
}
</style>
