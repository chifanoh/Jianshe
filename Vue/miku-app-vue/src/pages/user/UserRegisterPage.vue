<template>
  <div id="userRegisterPage">
    <GlobalHeader :simple-mode="true" />
    <main class="register-main">
      <n-card class="register-card" :bordered="false">
        <div class="register-title">注册</div>
        <n-form ref="formRef" :model="formState" :rules="rules" size="large" class="register-form">
          <n-form-item path="username">
            <n-input v-model:value="formState.username" placeholder="用户名" clearable />
          </n-form-item>
          <n-form-item path="email">
            <n-input v-model:value="formState.email" placeholder="QQ邮箱" clearable />
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
              注册
            </n-button>
          </n-form-item>
        </n-form>
        <div class="divider"></div>
        <div class="register-footer">
          <span>已有账号?</span>
          <router-link to="/user/login" class="login-link">立即登录</router-link>
        </div>
      </n-card>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, type FormInst, type FormRules } from 'naive-ui'
import { userRegister } from '@/api/user'
import GlobalHeader from '@/components/common/GlobalHeader.vue'
import CryptoJS from 'crypto-js'

const router = useRouter()
const message = useMessage()
const formRef = ref<FormInst | null>(null)
const loading = ref(false)

interface FormState {
  username: string
  email: string
  password: string
}

const formState = reactive<FormState>({
  username: '',
  email: '',
  password: '',
})

// 验证QQ邮箱格式
const isQQEmail = (str: string): boolean => {
  const qqEmailRegex = /^[1-9]\d{4,11}@qq\.com$/
  return qqEmailRegex.test(str)
}

// 验证用户名格式
const isValidUsername = (str: string): boolean => {
  return str.length >= 1 && str.length <= 6
}

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (!value) return true
        return isValidUsername(value.trim())
      },
      message: '用户名长度必须在1-6字符之间',
      trigger: 'blur',
    },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    {
      validator: (rule, value) => {
        if (!value) return true
        return isQQEmail(value.trim())
      },
      message: '请输入正确的QQ邮箱格式',
      trigger: 'blur',
    },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 12, message: '密码长度必须在6-12字符之间', trigger: 'blur' },
  ],
}

// 计算表单是否有效
const isFormValid = computed(() => {
  const username = formState.username.trim()
  const email = formState.email.trim()
  const password = formState.password
  return (
    username &&
    isValidUsername(username) &&
    email &&
    isQQEmail(email) &&
    password &&
    password.length >= 6 &&
    password.length <= 12
  )
})

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (errors) => {
    if (errors) return

    if (loading.value) return

    loading.value = true

    try {
      const registerParams = {
        username: formState.username.trim(),
        email: formState.email.trim(),
        password: CryptoJS.MD5(formState.password).toString().toLowerCase(),
      }

      const res = await userRegister(registerParams)

      if (res.data.code === 200) {
        message.success('注册成功')
        router.push('/user/login')
      } else {
        message.error(res.data.message || '注册失败，请稍后重试')
      }
    } catch (error: any) {
      message.error(error.response?.data?.message || '注册失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
#userRegisterPage {
  min-height: 100vh;
  background-color: #0d0d0d;
  display: flex;
  flex-direction: column;
}

.register-main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
}

.register-card {
  width: 400px;
  background-color: transparent;
}

.register-card :deep(.n-card__content) {
  padding: 0;
}

.register-title {
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  text-align: center;
  margin-bottom: 32px;
}

.register-form :deep(.n-form-item) {
  margin-bottom: 12px;
  display: block;
}

.register-form :deep(.n-form-item-feedback-wrapper) {
  min-height: 0;
  margin-top: 4px;
}

.register-form :deep(.n-input) {
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

.register-form :deep(.n-input__input-el) {
  color: #fff !important;
}

.register-form :deep(.n-input__placeholder) {
  color: #666 !important;
}

.register-form :deep(.n-input-wrapper) {
  background-color: #1a1a1a !important;
}

.register-form :deep(.n-input--focus) {
  background-color: #1a1a1a !important;
}

.register-form :deep(.n-input--hover) {
  background-color: #1a1a1a !important;
}

.register-form :deep(.n-input--active) {
  background-color: #1a1a1a !important;
}

.register-form :deep(.n-button) {
  --n-color: #1a3a5c !important;
  --n-color-hover: #2563eb !important;
  --n-color-focus: #2563eb !important;
  --n-color-pressed: #1d4ed8 !important;
  --n-text-color: #fff !important;
  --n-text-color-hover: #fff !important;
  --n-text-color-focus: #fff !important;
  --n-text-color-pressed: #fff !important;
}

.register-form :deep(.n-button:not(.n-button--disabled)) {
  --n-color: #2563eb !important;
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #333;
  margin: 16px 0;
}

.register-footer {
  text-align: center;
  color: #888;
  font-size: 14px;
}

.login-link {
  color: #2563eb;
  text-decoration: none;
  margin-left: 4px;
}

.login-link:hover {
  text-decoration: underline;
}
</style>
