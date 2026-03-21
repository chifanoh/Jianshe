<template>
  <div class="mask"></div>
  <div id="userProfilePage">
    <div class="main-content-section">
      <div class="main-box flex">
        <div class="flex">
          <div class="profile-container">
            <!-- 用户信息头部 -->
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
                    <div>
                      <span class="top-id">ID: {{ userId }}</span>
                    </div>
                  </div>
                </div>
                <div>
                  <div class="relation-section">
                    <span class="item pointer">关注：<span class="number">0</span></span>
                    <span class="item pointer">粉丝：<span class="number">0</span></span>
                    <span class="item pointer">获赞：<span class="number">0</span></span>
                  </div>
                  <div class="info-footer mt-20">
                    <n-button
                      style="width: 96px; height: 40px"
                      color="#586370"
                      strong
                      secondary
                      @click="handleEditProfile"
                    >
                      编辑资料
                    </n-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 内容区域 -->
            <div class="content-sections flex">
              <!-- 左侧菜单 -->
              <div class="sub-section mr-12">
                <div class="sub-item-section mb-8">
                  <div class="menu-title text-18 mx-12 px-32 pb-20">个人中心</div>
                  <ul class="content">
                    <li
                      v-for="item in menuOptions"
                      :key="item.key"
                      :class="['menu-item', { active: menuValue === item.key }]"
                      @click="handleMenuClick(item.key)"
                    >
                      <span class="menu-label">{{ item.label }}</span>
                    </li>
                  </ul>
                </div>
              </div>

              <!-- 右侧内容 -->
              <div class="main-section px-24 py-20">
                <div class="person-center-content-header">
                  <h2 class="title">{{ currentMenu.title }}</h2>
                </div>

                <div class="content-section" style="min-height: 500px">
                  <div class="flex-center">
                    <span class="panel-placeholder">{{ currentMenu.placeholder }}</span>
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
import { useUserProfile } from '@/composables/useUserProfile'

const {
  userName,
  userId,
  avatarSrc,
  menuOptions,
  menuValue,
  currentMenu,
  handleMenuClick,
  handleEditProfile,
} = useUserProfile()
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

.profile-container {
  width: 892px;
  display: flex;
  flex-direction: column;
}

/* 用户信息头部 */
.info-section {
  position: relative;
  display: flex;
  justify-content: space-between;
  padding: 40px 40px 24px 44px;
  background: #fff;
  border-radius: 8px;
  height: auto;
  margin-bottom: 12px;
}

.main-info-section {
  position: relative;
}

.icon-section {
  display: flex;
}

.top-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
}

.user-base-info {
  display: flex;
  flex-direction: column;
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

.relation-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  font-weight: 400;
  font-size: 16px;
  line-height: 24px;
  color: #8c95a3;
}

.number {
  font-weight: 700;
  color: #333;
}

.info-footer {
  width: 96px;
  height: 40px;
  font-weight: 500;
  color: #586370;
  background: #f3f6f8;
  border-radius: 4px;
  margin-top: 20px;
}

/* 内容区域 */
.content-sections {
  flex-grow: 1;
  position: relative;
  height: 100%;
  min-height: calc(100vh - 340px);
  display: flex;
}

/* 左侧菜单 */
.sub-section {
  position: sticky;
  top: 76px;
  min-width: 250px;
  height: 881px;
  width: 240px;
  flex: 1;
  max-height: calc(100vh - 76px);
  margin-right: 12px;
}

.sub-item-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px 0;
  margin-bottom: 8px;
}

.menu-title {
  font-weight: 700;
  border-bottom: 1px solid #edeff5;
  margin-bottom: 20px;
  color: #393f4d;
  font-size: 18px;
  line-height: 26px;
  margin-left: 12px;
  margin-right: 12px;
  padding-left: 32px;
  padding-right: 32px;
  padding-bottom: 20px;
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

/* 右侧内容 */
.main-section {
  flex-grow: 1;
  overflow-y: auto;
  width: calc(100% - 300px);
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 40px;
  padding-left: 24px;
  padding-right: 24px;
  padding-top: 20px;
  padding-bottom: 20px;
}

.person-center-content-header {
  border-bottom: 1px solid #edeff5;
  padding-bottom: 20px;
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

.content-section {
  position: relative;
  height: calc(100% - 67px);
  min-height: calc(100% - 67px);
}

.flex-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.panel-placeholder {
  color: #999;
}

/* 工具类 */
.flex {
  display: flex;
}

.flex-vertical-center {
  display: flex;
}

.mr-24 {
  margin-right: 24px;
}

.mb-15 {
  margin-bottom: 15px;
}

.pointer {
  cursor: pointer;
}
</style>
