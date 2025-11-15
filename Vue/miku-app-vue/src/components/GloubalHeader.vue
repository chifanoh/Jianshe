<template>
  <!-- 导航栏 -->
  <div class="header">
    <div class="header-inner">
      <div class="header-left">
        <div class="header-logo-container">
          <router-link to="/">
            <img src="/logo.png" alt="Miku Logo" class="logo" />
          </router-link>
        </div>
        <ul class="nav-list">
          <li class="nav-item">
            <router-link to="/" class="nav-link">
              <div>首页</div>
            </router-link>
          </li>
        </ul>
      </div>

      <div class="header-right">
        <!-- 搜索框 -->
        <div class="search-container">
          <n-space vertical>
            <n-input placeholder="搜索">
              <template>
                <n-icon :component="FlashOutline" />
              </template>
            </n-input>
          </n-space>
        </div>

        <!-- 发布 -->
        <button class="submit-btn" @click="showModal = true">
          <svg
            width="20"
            height="20"
            viewBox="0 0 32 32"
            xmlns="http://www.w3.org/2000/svg"
            class="z-1"
          >
            <path d="M31 24h-4v-4h-2v4h-4v2h4v4h2v-4h4v-2z" fill="currentColor"></path>
            <path
              d="M25 5h-3V4a2.006 2.006 0 0 0-2-2h-8a2.006 2.006 0 0 0-2 2v1H7a2.006 2.006 0 0 0-2 2v21a2.006 2.006 0 0 0 2 2h10v-2H7V7h3v3h12V7h3v9h2V7a2.006 2.006 0 0 0-2-2zm-5 3h-8V4h8z"
              fill="currentColor"
            ></path>
          </svg>
          <span class="submit-btn-text">发布</span>
        </button>

        <!-- 发布模态框 -->
        <n-modal
          style="width: 900px; max-width: 90vw"
          v-model:show="showModal"
          preset="dialog"
          title="试着发布一篇文章吧"
          positive-text="确认"
          negative-text="算了"
          :positive-button-props="{
            size: 'large',
          }"
          :negative-button-props="{
            size: 'large',
          }"
          @positive-click="submitCallback"
          @negative-click="cancelCallback"
          class="publish-modal"
          transform-origin="center"
        >
          <div class="publish-modal-content">
            <div class="publish-form">
              <div class="form-item">
                <label class="form-label">文章标题</label>
                <n-input
                  v-model:value="articleTitle"
                  placeholder="请输入文章标题 (必填)"
                  maxlength="30"
                  show-count
                  size="large"
                />
              </div>
              <div class="form-item">
                <label class="form-label">文章内容</label>
                <RichEditor v-model="articleContent" />
              </div>
              <div class="form-item">
                <label class="form-label">话题标签</label>
                <n-space vertical>
                  <n-select
                    v-model:value="articleTags"
                    placeholder="选择话题标签"
                    multiple
                    :options="tagOptions"
                    clearable
                  />
                </n-space>
              </div>
            </div>
          </div>
        </n-modal>

        <!-- 用户头像 -->
        <n-dropdown
          v-if="loginUser.isLogin"
          trigger="hover"
          :options="options"
          @select="handleDropdownSelect"
        >
          <div class="avatar" style="width: 40px; height: 40px; min-width: 40px; cursor: pointer">
            <img
              :src="
                loginUser.isLogin && loginUser.loginUser.userAvatar
                  ? loginUser.loginUser.userAvatar.startsWith('http')
                    ? loginUser.loginUser.userAvatar
                    : '/' + loginUser.loginUser.userAvatar.replace(/\\/g, '/')
                  : 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMjAiIGN5PSIxNiIgcj0iNiIgZmlsbD0iI2NjYyIvPgo8cGF0aCBkPSJNMTIgMzJDOCAzMiA4IDI4IDggMjRIMzJDMzIgMjggMzIgMzIgMjggMzJIMTJ6IiBmaWxsPSIjY2NjIi8+Cjwvc3ZnPgo='
              "
              :alt="loginUser.isLogin ? loginUser.loginUser.userName : '用户头像'"
              style="object-fit: cover; width: 100%; height: 100%; border-radius: 50%"
            />
          </div>
        </n-dropdown>
        <!-- 未登录时显示登录链接 -->
        <router-link v-else to="/user/login" class="login-avatar">
          <n-dropdown
            ><div class="avatar" style="width: 40px; height: 40px; min-width: 40px">
              <img
                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNmMGYwZjAiLz4KPGNpcmNsZSBjeD0iMjAiIGN5PSIxNiIgcj0iNiIgZmlsbD0iI2NjYyIvPgo8cGF0aCBkPSJNMTIgMzJDOCAzMiA4IDI4IDggMjRIMzJDMzIgMjggMzIgMzIgMjggMzJIMTJ6IiBmaWxsPSIjY2NjIi8+Cjwvc3ZnPgo="
                alt="登录"
                style="object-fit: cover; width: 100%; height: 100%; border-radius: 50%"
              /></div
          ></n-dropdown>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FlashOutline } from '@vicons/ionicons5'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { NModal, NInput, NSpace, useMessage } from 'naive-ui'
import RichEditor from './Article/RichEditor.vue'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import { createArticle } from '@/api/article'

// 模态框状态
const showModal = ref(false)
const publishing = ref(false)
const message = useMessage()
const loginUser = useLoginUserStore()
const router = useRouter()

// 只在需要时获取用户信息
onMounted(async () => {
  // 如果已经登录且有用户信息，不需要重复获取
  if (loginUser.isLogin && loginUser.loginUser.id) {
    return
  }

  // 如果头像为空或者用户名为'未登录'，才尝试获取用户信息
  if (!loginUser.loginUser.userAvatar || loginUser.loginUser.userName === '未登录') {
    await loginUser.loadUserFromCache()
  }
})

// 表单数据
const articleTitle = ref('')
const articleContent = ref('')
const articleTags = ref<string[]>([])

// 话题标签选项
const tagOptions = [
  {
    label: '前端',
    value: '1',
  },
  {
    label: '后端',
    value: '2',
  },
  {
    label: '数据库',
    value: '3',
  },
]

// 头像下拉菜单选项
const options = [
  {
    label: '个人中心',
    key: 'profile',
  },
  {
    label: '退出登录',
    key: 'logout',
  },
]

// 处理下拉菜单选择
const handleDropdownSelect = (key: string) => {
  if (key === 'profile') {
    // 跳转到个人中心
    router.push('/user/profile')
  } else if (key === 'logout') {
    // 处理退出登录
    loginUser.logout()
    router.push('/')
  }
}

// 发布文章
function cancelCallback() {
  return
}

async function submitCallback() {
  if (!articleTitle.value.trim()) {
    message.warning('请输入文章标题')
    return false
  }
  if (!articleContent.value.trim()) {
    message.warning('请输入文章内容')
    return false
  }

  publishing.value = true

  try {
    // 调用创建文章API
    const response = await createArticle({
      title: articleTitle.value.trim(),
      content: articleContent.value.trim(),
      topicIds: articleTags.value.length > 0 ? articleTags.value : undefined,
    })

    if (response.data.code === 200) {
      message.success('文章发布成功！')
      showModal.value = false

      // 重置表单
      articleTitle.value = ''
      articleContent.value = ''
      articleTags.value = []

      // 可以在这里添加跳转到文章详情页的逻辑
      if (response.data.data && response.data.data.id) {
        router.push(`/article/${response.data.data.id}`)
      }
    } else {
      message.error(response.data.msg || '发布失败，请稍后重试')
    }
  } catch (error) {
    console.error('发布文章失败:', error)
    message.error('发布失败，请稍后重试')
  } finally {
    publishing.value = false
  }
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  width: 100vw;
  margin: 0 auto;
  min-width: 1268px;
  background-color: rgb(255, 255, 255, 0.8);
  backdrop-filter: blur(5px);
  z-index: 1000;
  border-bottom: solid 1px #e0e0e0;
}

.header-inner {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  width: 1268px;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-logo-container,
.header-right {
  display: flex;
  align-items: center;
}

.nav-list {
  display: flex;
  align-items: center;
  list-style: none;
  margin: 0;
  padding: 0;
  /* margin-right: auto; */
  font-size: 18px;
  font-weight: 600;
  line-height: 30px;
}

.nav-item {
  margin: 0;
  padding: 0;
}

.nav-link {
  color: #000000;
  text-decoration: none;
  font-size: 1em;
  cursor: pointer;
  display: block;
  width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-link:hover {
  color: #bababa;
}

.search-container {
  width: 400px;
  margin-right: 20px;
}

.logo {
  width: 110px;
  height: 65px;
  margin-right: 50px;
}

.login-link {
  padding: 8px 15px;
  color: #000000;
  text-decoration: none;
}

.register-link {
  background-color: rgb(0, 90, 190);
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
}

.register-link:hover {
  background-color: rgb(0, 80, 200);
}

.submit-btn {
  height: 40px;
  min-width: 88px;
  padding: 9px 16px;
  margin-right: 32px;
  display: flex;
  align-items: center;
  color: #fff;
  background-color: #1890ff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  user-select: none;
  position: relative;
  overflow: hidden;
  transition: all 0.5s ease;
}

.z-1 {
  z-index: 1;
}

.submit-btn:hover {
  background-color: #3aa0ff;
  box-shadow: 0 4px 12px rgba(20, 150, 255, 0.4);
}

.submit-btn-text {
  font-size: 16px;
  line-height: 24px;
  margin-left: 6px;
  position: relative;
}

.avatar {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.login-avatar:hover {
  filter: brightness(0.95);
}

/* 发布模态框样式 */
.publish-modal {
  border-radius: 12px;
  overflow: hidden;
}

.publish-modal-content {
  padding: 24px 0 16px;
}

.publish-form {
  margin-bottom: 8px;
}

.form-item {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  letter-spacing: 0.5px;
}
</style>
