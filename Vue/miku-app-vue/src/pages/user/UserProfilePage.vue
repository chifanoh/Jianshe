<template>
  <div class="mask"></div>
  <div id="userProfilePage">
    <div class="main-content-section">
      <div class="main-box flex">
        <div class="flex">
          <div class="container">
            <div class="info-section mb-12">
              <div class="main-info-section">
                <div class="flex mb-15">
                  <div class="icon-section">
                    <div class="mr-24">
                      <img :src="avatarSrc" class="top-avatar" alt="avatar" />
                    </div>
                  </div>
                  <div class="user-base-info">
                    <div class="flex-vertical-center">
                      <span class="top-name">{{ userName }}</span>
                    </div>
                    <div class="">
                      <span class="top-id">ID: {{ loginUserStore.loginUser.id || '-' }}</span>
                    </div>
                  </div>
                </div>
                <div class="">
                  <div class="relation-section">
                    <span class="item pointer">关注：<span class="number">0</span></span>
                    <span class="item pointer">粉丝：<span class="number">0</span></span>
                    <span class="item pointer">获赞：<span class="number">0</span></span>
                  </div>
                  <div class="info-footer mt-20">
                    <n-button color="#586370" strong secondary @click="menuValue = 'edit'"
                      >编辑资料
                    </n-button>
                  </div>
                </div>
              </div>
            </div>

            <div class="content-sections flex">
              <div class="sub-section mr-12">
                <div class="sub-item-section mb-8">
                  <div class="title text-18 mx-12 px-32 pb-20">个人中心</div>
                  <ul class="content">
                    <li
                      v-for="item in menuOptions"
                      :key="item.key"
                      :class="['menu-item', { active: menuValue === item.key }]"
                      @click="menuValue = item.key"
                    >
                      <span class="menu-label">{{ item.label }}</span>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="main-section px-24 py-20">
                <div class="person-center-content-header">
                  <div v-if="menuValue === 'posts'">
                    <h2 class="title">我的帖子</h2>
                  </div>
                  <div v-else-if="menuValue === 'comments'">
                    <h2 class="title">我的评论</h2>
                  </div>
                  <div v-else-if="menuValue === 'stars'">
                    <h2 class="title">我的收藏</h2>
                  </div>
                  <div v-else-if="menuValue === 'follows'">
                    <h2 class="title">我的关注</h2>
                  </div>
                  <div v-else>
                    <h2 class="title">编辑资料</h2>
                  </div>
                </div>

                <div class="content-section" style="min-height: 500px">
                  <div class="flex-center">
                    <div v-if="menuValue === 'posts'">
                      <span class="panel-placeholder">暂无数据</span>
                    </div>
                    <div v-else-if="menuValue === 'comments'">
                      <span class="panel-placeholder">暂无数据</span>
                    </div>
                    <div v-else-if="menuValue === 'stars'">
                      <span class="panel-placeholder">暂无数据</span>
                    </div>
                    <div v-else-if="menuValue === 'follows'">
                      <span class="panel-placeholder">暂无数据</span>
                    </div>
                    <div v-else>
                      <span class="panel-placeholder">暂不支持该功能</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed, ref } from 'vue'
import { useLoginUserStore } from '@/store/useLoginUserStore'

const loginUserStore = useLoginUserStore()

onMounted(async () => {
  if (!loginUserStore.isLogin) {
    await loginUserStore.loadUserFromCache()
  }
})
const userName = computed(() => loginUserStore.loginUser.userName || '未登录')

const avatarSrc = computed(() => {
  const src = loginUserStore.loginUser.userAvatar
  if (!src) return defaultAvatar
  if (src.startsWith('http')) return src
  return '/' + src.replace(/\\/g, '/')
})

const menuValue = ref('edit')

const menuOptions = [
  { label: '我的帖子', key: 'posts' },
  { label: '我的评论', key: 'comments' },
  { label: '我的收藏', key: 'stars' },
  { label: '我的关注', key: 'follows' },
]

const defaultAvatar =
  'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMzIiIGN5PSIzMiIgcj0iMzIiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMzIiIGN5PSIyNiIgcj0iMTAiIGZpbGw9IiNjY2MiLz4KPHBhdGggZD0iTTE4IDQ4QzE0IDQ4IDE0IDQyIDE0IDM2SDUwQzUwIDQyIDUwIDQ4IDQ2IDQ4SDE4WiIgZmlsbD0iI2NjYyIvPgo8L3N2Zz4K'
</script>

<style scoped>
#userProfilePage {
  padding: 80px 0 24px;
}

.mask {
  position: fixed;
  top: 60px;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100vw;
  height: calc(100vh - 60px);
  z-index: 0;
  background-color: #f7f8fa;
}

.flex {
  display: flex;
}

.mr-12 {
  margin-right: 12px;
}

.mb-8 {
  margin-bottom: 8px;
}

.mb-15 {
  margin-bottom: 15px;
}

.mr-24 {
  margin-right: 24px;
}

.mt-20 {
  margin-top: 20px;
}

.text-18 {
  font-size: 18px;
  line-height: 26px;
}

.mx-12 {
  margin-left: 12px;
  margin-right: 12px;
}

.px-32 {
  padding-left: 32px;
}

.container {
  display: flex;
  flex-direction: column;
}

.content-sections {
  flex-grow: 1;
}

.main-section {
  flex-grow: 1;
  overflow-y: auto;
  padding-right: 32px;
}

.pb-20 {
  padding-bottom: 20px;
}

.py-20 {
  padding-top: 20px;
  padding-bottom: 20px;
}

.px-24 {
  padding-left: 24px;
  padding-right: 24px;
}

.mb-12 {
  margin-bottom: 12px;
}

.person-center-content-header .title {
  display: inline;
  width: 100%;
  font-size: 18px;
  line-height: 26px;
  font-weight: 700;
  color: #393f4d;
  margin: 0;
}

.container {
  width: 892px;
}

.info-section {
  position: relative;
  display: flex;
  justify-content: space-between;
  padding: 40px 40px 24px 44px;
  background: #fff;
  border-radius: 8px;
  height: auto;
}

.info-footer {
  width: 96px;
  height: 40px;
  font-weight: 500;
  color: #586370;
  background: #f3f6f8;
  border-radius: 4px;
}

.relation-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  font-weight: 400;
  font-size: 16px;
  line-height: 24px;
  color: #8c95a3;
  word-break: none;
}

.number {
  font-weight: 700;
  color: #333;
}

.flex-vertical-center {
  display: flex;
}

.flex-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.top-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
}

.top-name {
  font-weight: 700;
  max-width: 292px;
  font-size: 24px;
  line-height: 32px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--kr-basic-black);
}

.top-id {
  margin-top: 4px;
  color: #999;
  font-size: 13px;
}

.main-content-section {
  height: 100%;
  width: 1208px;
  margin: 0 auto;
  position: relative;
  justify-content: center;
  z-index: 1;
}

.main-info-section {
  position: relative;
}

.main-box {
  display: flex;
  position: relative;
  justify-content: center;
}

.content {
  box-sizing: border-box;
  list-style: none;
  margin: 0;
  padding: 0;
}

.menu-item {
  margin-bottom: 8px;
  font-size: 16px;
  line-height: 24px;
  font-weight: 400;
  cursor: pointer;
  padding: 12px 44px;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  position: relative;
  border-left: 4px solid transparent;
}

.menu-item:hover {
  color: #a4a4a4;
}

.menu-item.active {
  background-color: #e6f7ff;
  border-left-color: #7991b9;
  color: #7991b9;
  font-weight: 500;
}

.main-section {
  width: calc(100% - 300px);
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 40px;
}

.person-center-content-header {
  border-bottom: 1px solid #edeff5;
  padding-bottom: 20px;
}

.panel-placeholder {
  color: #999;
}

.content-sections {
  position: relative;
  height: 100%;
  min-height: calc(100vh - 340px);
}

.content-section {
  position: relative;
  height: calc(100% - 67px);
  min-height: calc(100% - 67px);
}

.sub-section {
  position: sticky;
  top: 76px;
  min-width: 250px;
  height: 881px;
  width: 240px;
  flex: 1;
  max-height: calc(100vh - 76px);
}

.sub-item-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px 0;
}

.title {
  font-weight: 700;
  border-bottom: 1px solid #edeff5;
  margin-bottom: 20px;
  color: #393f4d;
}
</style>
