<template>
  <div id="userLoginPage">
    <GlobalHeader :simple-mode="true" />
    <main class="login-main">
      <n-card class="login-card" :bordered="false">
        <div class="login-title">登录</div>
        <n-form ref="formRef" :model="formState" :rules="rules" size="large" class="login-form">
          <n-form-item path="emailOrName">
            <n-input v-model:value="formState.emailOrName" placeholder="用户名或邮箱" clearable />
          </n-form-item>
          <n-form-item path="password">
            <n-input
              v-model:value="formState.password"
              type="password"
              placeholder="密码"
              clearable
              show-password-on="mousedown"
              @keyup.enter="handleSubmit"
            />
          </n-form-item>
          <n-form-item>
            <n-button
              type="primary"
              size="large"
              :loading="loading"
              :disabled="!isFormValid"
              block
              @click="handleSubmit"
            >
              登录
            </n-button>
          </n-form-item>
        </n-form>
        <div class="divider"></div>
        <div class="login-footer">
          <span>没有账号?</span>
          <router-link to="/user/register" class="register-link">注册账号</router-link>
        </div>
      </n-card>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, type FormInst, type FormRules } from 'naive-ui'
import { userLogin } from '@/api/user'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import GlobalHeader from '@/components/common/GlobalHeader.vue'
import CryptoJS from 'crypto-js'

const router = useRouter()
const message = useMessage()
const loginUserStore = useLoginUserStore()
const formRef = ref<FormInst | null>(null)
const loading = ref(false)

interface FormState {
  emailOrName: string
  password: string
}

const formState = reactive<FormState>({
  emailOrName: '',
  password: '',
})

// 验证邮箱格式
const isEmail = (str: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(str)
}

// 验证用户名格式
const isValidUsername = (str: string): boolean => {
  const usernameRegex = /^[a-zA-Z0-9_]{3,16}$/
  return usernameRegex.test(str)
}

const rules: FormRules = {
  emailOrName: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (!value) return true
        const input = value.trim()
        return isEmail(input) || isValidUsername(input)
      },
      message: '请输入有效的用户名或邮箱',
      trigger: 'blur',
    },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 12, message: '密码长度必须在6-12位之间', trigger: 'blur' },
  ],
}

// 计算表单是否有效
const isFormValid = computed(() => {
  const emailOrName = formState.emailOrName.trim()
  const password = formState.password
  return (
    emailOrName &&
    password &&
    password.length >= 6 &&
    password.length <= 12 &&
    (isEmail(emailOrName) || isValidUsername(emailOrName))
  )
})

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (errors) => {
    if (errors) return

    if (loading.value) return

    loading.value = true

    try {
      const loginParams = {
        account: formState.emailOrName.trim(),
        password: CryptoJS.MD5(formState.password).toString().toLowerCase(),
      }

      const res = await userLogin(loginParams)

      if (res.data.code === 200 && res.data.data) {
        const userData = {
          id: res.data.data.id,
          userName: res.data.data.name,
          userAvatar: res.data.data.avatar,
          email: res.data.data.email,
          token: res.data.data.token,
        }

        await loginUserStore.setLoginUser(userData)

        try {
          const { saveUserDataWithTimestamp } = await import('@/utils/indexedDB')
          await saveUserDataWithTimestamp(userData)
        } catch (error) {
          console.error('保存用户数据到IndexedDB失败:', error)
        }

        message.success('登录成功')
        router.push({ path: '/', replace: true })
      } else {
        message.error(res.data.message || '登录失败，账号或密码错误')
      }
    } catch (error: any) {
      message.error(error.response?.data?.message || '登录失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
#userLoginPage {
  min-height: 100vh;
  background-color: #0d0d0d;
  display: flex;
  flex-direction: column;
}

.login-main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
}

.login-card {
  width: 400px;
  background-color: transparent;
}

.login-card :deep(.n-card__content) {
  padding: 0;
}

.login-title {
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  text-align: center;
  margin-bottom: 32px;
}

.login-form :deep(.n-form-item) {
  margin-bottom: 12px;
  display: block;
}

.login-form :deep(.n-form-item-feedback-wrapper) {
  min-height: 0;
  margin-top: 4px;
}

.login-form :deep(.n-input) {
  --n-color: #1a1a1a !important;
  --n-color-focus: #1a1a1a !important;
  --n-color-hover: #1a1a1a !important;
  --n-color-active: #1a1a1a !important;
  --n-color-disabled: #1a1a1a !important;
  --n-border: none !important;
  --n-border-focus: none !important;
  --n-border-hover: none !important;
  --n-border-active: none !important;
  --n-border-disabled: none !important;
  --n-placeholder-color: #666 !important;
  --n-text-color: #fff !important;
  --n-height: 48px !important;
}

.login-form :deep(.n-input__input-el) {
  color: #fff !important;
}

.login-form :deep(.n-input__placeholder) {
  color: #666 !important;
}

.login-form :deep(.n-input-wrapper) {
  background-color: #1a1a1a !important;
}

.login-form :deep(.n-input--focus) {
  background-color: #1a1a1a !important;
}

.login-form :deep(.n-input--hover) {
  background-color: #1a1a1a !important;
}

.login-form :deep(.n-input--active) {
  background-color: #1a1a1a !important;
}

.login-form :deep(.n-button) {
  --n-color: #1a3a5c !important;
  --n-color-hover: #2563eb !important;
  --n-color-focus: #2563eb !important;
  --n-color-pressed: #1d4ed8 !important;
  --n-text-color: #fff !important;
  --n-text-color-hover: #fff !important;
  --n-text-color-focus: #fff !important;
  --n-text-color-pressed: #fff !important;
}

.login-form :deep(.n-button:not(.n-button--disabled)) {
  --n-color: #2563eb !important;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #333;
  margin: 16px 0;
}

.login-footer {
  text-align: center;
  color: #888;
  font-size: 14px;
}

.register-link {
  color: #2563eb;
  text-decoration: none;
  margin-left: 4px;
}

.register-link:hover {
  text-decoration: underline;
}
</style>
