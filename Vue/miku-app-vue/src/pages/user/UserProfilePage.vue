<template>
  <div id="userProfilePage">
    <div class="main-content-section">
      <div class="main-box flex">
        <div class="flex">
          <div class="container">
            <div class="info-section mb-12">
              <div class="top-row">
                <img :src="avatarSrc" class="top-avatar" alt="avatar" />
                <div class="top-info">
                  <div class="top-name">{{ userName }}</div>
                  <div class="top-id">ID: {{ loginUserStore.loginUser.id || '-' }}</div>
                  <div class="top-meta">
                    <span>关注：0</span>
                    <span>粉丝：0</span>
                    <span>获赞：0</span>
                  </div>
                  <div class="top-sign">
                    {{ loginUserStore.loginUser.userProfile || '这个人很懒，还没有签名。' }}
                  </div>
                  <button class="btn btn-primary top-edit" @click="menuValue = 'edit'">
                    编辑资料
                  </button>
                </div>
              </div>
            </div>

            <div class="content-section flex">
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
                    <h2 class="title">帖子</h2>
                  </div>
                  <div v-else-if="menuValue === 'comments'">
                    <h2 class="title">评论</h2>
                  </div>
                  <div v-else-if="menuValue === 'stars'">
                    <h2 class="title">收藏</h2>
                  </div>
                  <div v-else-if="menuValue === 'follows'">
                    <h2 class="title">关注</h2>
                  </div>
                  <div v-else>
                    <h2 class="title">编辑资料</h2>
                    <div class="edit-profile">
                      <img :src="avatarSrc" class="edit-avatar" alt="avatar" />
                      <div class="btn-group">
                        <button class="btn btn-primary" style="margin-right: 8px">修改头像</button>
                        <button class="btn">选择挂件</button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="content-section">
                  <div v-if="menuValue === 'posts'">
                    <div class="panel-placeholder">暂无数据</div>
                  </div>
                  <div v-else-if="menuValue === 'comments'">
                    <div class="panel-placeholder">暂无数据</div>
                  </div>
                  <div v-else-if="menuValue === 'stars'">
                    <div class="panel-placeholder">暂无数据</div>
                  </div>
                  <div v-else-if="menuValue === 'follows'">
                    <div class="panel-placeholder">暂无数据</div>
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
  { label: '帖子', key: 'posts' },
  { label: '评论', key: 'comments' },
  { label: '收藏', key: 'stars' },
  { label: '关注', key: 'follows' },
]

const defaultAvatar =
  'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMzIiIGN5PSIzMiIgcj0iMzIiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMzIiIGN5PSIyNiIgcj0iMTAiIGZpbGw9IiNjY2MiLz4KPHBhdGggZD0iTTE4IDQ4QzE0IDQ4IDE0IDQyIDE0IDM2SDUwQzUwIDQyIDUwIDQ4IDQ2IDQ4SDE4WiIgZmlsbD0iI2NjYyIvPgo8L3N2Zz4K'
</script>

<style scoped>
#userProfilePage {
  padding: 80px 0 24px;
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

.text-18 {
  font-size: 18px;
  line-height: 26px;
}

.mx-12 {
  margin: 0 12px;
}

.px-32 {
  padding: 0 32px;
}

.pb-20 {
  padding-top: 20px;
  padding-bottom: 20px;
}

.py-20 {
  padding: 20px 0;
}

.px-24 {
  padding-left: 24px;
  padding-right: 24px;
}

.mb-12 {
  margin-bottom: 12px;
}

.title {
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

.btn {
  height: 28px;
  line-height: 28px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 10px;
  background: #fff;
  cursor: pointer;
}
.btn-primary {
  border-color: #1890ff;
  background: #1890ff;
  color: #fff;
}
.top-row {
  display: flex;
  align-items: center;
  gap: 16px;
}
.top-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
}
.top-info {
  flex: 1;
}
.top-name {
  font-size: 20px;
  font-weight: 600;
}
.top-id {
  margin-top: 4px;
  color: #999;
  font-size: 13px;
}
.top-meta {
  display: flex;
  gap: 16px;
  margin-top: 8px;
}
.top-sign {
  margin-top: 8px;
  color: #666;
}
.top-edit {
  margin-top: 12px;
}
.main-content-section {
  height: 100%;
  width: 1208px;
  margin: 0 auto;
  position: relative;
  justify-content: center;
  z-index: 1;
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
  display: flex;
  align-items: center;
  height: 36px;
  padding: 0 12px;
  border-radius: 6px;
  cursor: pointer;
}
.menu-item:hover {
  background: #f5f5f5;
}
.menu-item.active {
  background: #e6f7ff;
  color: #1890ff;
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

.btn-group {
  display: flex;
  align-items: center;
}

.panel-placeholder {
  color: #999;
}
.edit-profile {
  display: flex;
  align-items: center;
  gap: 16px;
}
.edit-avatar {
  width: 72px;
  height: 72px;
}
.content-section {
  position: relative;
}

.sub-section {
  top: 76px;
  min-width: 240px;
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
