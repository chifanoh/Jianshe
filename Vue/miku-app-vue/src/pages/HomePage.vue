<template>
  <!-- 首页 -->
  <div id="homePage">
    <div class="main-layout">
      <!-- 左导航栏 -->
      <div class="sub-section">
        <div class="nav-sidebar py-30 mr-12">
          <div class="logo-section mb-24">
            <img class="js" src="" alt="" />
          </div>
          <div class="nav-item" data-section="follow">
            <svg
              width="24"
              height="24"
              viewBox="0 0 32 32"
              xmlns="http://www.w3.org/2000/svg"
              class="mr-8"
            >
              <path
                d="M8 9a4 4 0 1 1 4-4a4 4 0 0 1-4 4zm0-6a2 2 0 1 0 2 2a2 2 0 0 0-2-2z"
                fill="currentColor"
              ></path>
              <path
                d="M24 9a4 4 0 1 1 4-4a4 4 0 0 1-4 4zm0-6a2 2 0 1 0 2 2a2 2 0 0 0-2-2z"
                fill="currentColor"
              ></path>
              <path
                d="M26 30h-4a2 2 0 0 1-2-2v-7h2v7h4v-9h2v-6a1 1 0 0 0-1-1h-6.42L16 20l-4.58-8H5a1 1 0 0 0-1 1v6h2v9h4v-7h2v7a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-7a2 2 0 0 1-2-2v-6a3 3 0 0 1 3-3h7.58L16 16l3.42-6H27a3 3 0 0 1 3 3v6a2 2 0 0 1-2 2v7a2 2 0 0 1-2 2z"
                fill="currentColor"
              ></path>
            </svg>
            <a class="nav-link" href="/follow"><span class="nav-text">关注</span></a>
          </div>
          <div class="nav-item active" data-section="recommend">
            <svg
              width="24"
              height="24"
              viewBox="0 0 32 32"
              xmlns="http://www.w3.org/2000/svg"
              class="mr-8"
            >
              <path
                d="M30 22H16v-1h-2v1H2v6h12v1h2v-1h14zM4 26v-2h10v2zm24 0H16v-2h12z"
                fill="currentColor"
              ></path>
              <path
                d="M30 13h-6v-1h-2v1H2v6h20v1h2v-1h6zM4 17v-2h18v2zm24 0h-4v-2h4z"
                fill="currentColor"
              ></path>
              <path
                d="M30 4H10V3H8v1H2v6h6v1h2v-1h20zM4 8V6h4v2zm24 0H10V6h18z"
                fill="currentColor"
              ></path>
            </svg>
            <a class="nav-link" href="/"><span class="nav-text">推荐</span></a>
          </div>
          <div class="nav-item" data-section="teahouse">
            <svg
              width="24"
              height="24"
              viewBox="0 0 32 32"
              xmlns="http://www.w3.org/2000/svg"
              class="mr-8"
            >
              <path
                d="M28 6H8c-1.2 0-2 .8-2 2v14c0 1.2.8 2 2 2h8v-2H8V8h20v14h-7.2L16 28.8l1.6 1.2l4.2-6H28c1.2 0 2-.8 2-2V8c0-1.2-.8-2-2-2z"
                fill="currentColor"
              ></path>
              <path
                d="M4 18H2V5c0-1.7 1.3-3 3-3h13v2H5c-.6 0-1 .4-1 1v13z"
                fill="currentColor"
              ></path>
            </svg>
            <a class="nav-link" href="/teahouse"><span class="nav-text">茶馆</span></a>
          </div>
          <div class="nav-item" data-section="official">
            <svg
              width="24"
              height="24"
              viewBox="0 0 32 32"
              xmlns="http://www.w3.org/2000/svg"
              class="mr-8"
            >
              <path
                d="M28 6H8c-1.2 0-2 .8-2 2v14c0 1.2.8 2 2 2h8v-2H8V8h20v14h-7.2L16 28.8l1.6 1.2l4.2-6H28c1.2 0 2-.8 2-2V8c0-1.2-.8-2-2-2z"
                fill="currentColor"
              ></path>
              <path
                d="M4 18H2V5c0-1.7 1.3-3 3-3h13v2H5c-.6 0-1 .4-1 1v13z"
                fill="currentColor"
              ></path>
            </svg>
            <a class="nav-link" href="/official"><span class="nav-text">官方</span></a>
          </div>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="home-center">
        <!-- 轮播图 -->
        <div class="home-header">
          <n-carousel
            class="n-carousel-item"
            autoplay
            :interval="2500"
            effect="card"
            prev-slide-style="transform: translateX(-120%) translateZ(-800px);"
            next-slide-style="transform: translateX(20%) translateZ(-800px);"
            style="height: 240px"
            :show-dots="false"
          >
            <n-carousel-item :style="{ width: '70%' }">
              <img class="carousel-img" src="@/assets/image/lxq01.png" />
            </n-carousel-item>
            <n-carousel-item :style="{ width: '70%' }">
              <img class="carousel-img" src="@/assets/image/lxq02.png" />
            </n-carousel-item>
            <n-carousel-item :style="{ width: '70%' }">
              <img class="carousel-img" src="@/assets/image/lxq03.png" />
            </n-carousel-item>
            <n-carousel-item :style="{ width: '70%' }">
              <img class="carousel-img" src="@/assets/image/lxq02.png" />
            </n-carousel-item>
          </n-carousel>
        </div>

        <!-- 论坛区域 -->
        <div class="home-list mr-12">
          <div class="home-list-body">
            <ArticleList />
          </div>
        </div>
      </div>

      <!-- 右导航栏 -->
      <div class="home-sub">
        <n-date-picker panel type="date" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ArticleList from '@/components/Article/ArticleList.vue'
import { ref, onMounted } from 'vue'

// 导航状态
const activeSection = ref('recommend')

// 切换导航
const switchSection = (section: string) => {
  activeSection.value = section

  // 更新导航项激活状态
  const navItems = document.querySelectorAll('.nav-item')
  navItems.forEach((item) => {
    if (item.getAttribute('data-section') === section) {
      item.classList.add('active')
    } else {
      item.classList.remove('active')
    }
  })
}

onMounted(() => {
  // 绑定点击事件
  const navItems = document.querySelectorAll('.nav-item')
  navItems.forEach((item) => {
    item.addEventListener('click', () => {
      const section = item.getAttribute('data-section')
      if (section) {
        switchSection(section)
      }
    })
  })
})
</script>

<style scoped>
#homePage {
  padding: 82px 0 24px;
  position: relative;
}

.main-home {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-layout {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  box-sizing: content-box;
}

.sub-ul {
  display: none;
}

/* 中间内容区域 */
.home-center {
  flex: 1;
  max-width: 700px;
  margin: 0 auto;
}

.n-carousel-item {
  overflow: visible;
}

.carousel-img {
  margin: 0 auto;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.home-header {
  margin-bottom: 20px;
}

.home-list {
  border-radius: 4px;
}

.home-list-body {
  position: relative;
}

/* 右导航栏 */
.home-sub {
  flex: 0 0 280px;
  position: sticky;
  top: 76px;
  height: fit-content;
  align-self: flex-start;
}
/* * * * */

/* 左导航栏 */
.logo-section {
  height: 36px;
}

.mb-24 {
  margin-bottom: 24px;
}

.js {
  width: 58px;
  height: 38px;
}

.sub-section {
  position: sticky;
  height: 100%;
  left: 0;
  top: 76px;
  bottom: 0;
}

.nav-sidebar {
  width: 240px;
  background-color: #fff;
  text-align: center;
  border-radius: 8px;
}

.py-30 {
  padding-top: 30px;
  padding-bottom: 30px;
}

.mr-12 {
  margin-right: 12px;
}

.mr-8 {
  margin-right: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 12px 40px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  border-left: 5px solid transparent;
}

.nav-item:hover {
  color: #a4a4a4;
}

.nav-item.active {
  background-color: #e6f7ff;
  border-left-color: #1890ff;
  color: #1890ff;
  font-weight: 500;
}

.nav-icon {
  font-size: 18px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.nav-text {
  font-size: 15px;
  letter-spacing: 0.5px;
}
.nav-link {
  text-decoration: none;
  color: inherit;
}
/* * * * */
</style>
