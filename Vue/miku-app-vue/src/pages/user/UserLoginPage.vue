<template>
  <!-- 登录表单 -->
  <div id="userLoginPage">
    <main class="login-main">
      <div class="login-container">
        <div class="form-section">
          <div class="left-nav">
            <h2>登录</h2>
          </div>
          <form @submit.prevent="login" class="login-form">
            <div class="input-group">
              <input
                type="text"
                placeholder="用户名"
                v-model="formState.emailOrName"
                required
                class="login-input"
                :class="{ error: fieldErrors.emailOrName }"
                @blur="validateField('emailOrName')"
                @input="clearFieldError('emailOrName')"
              />
              <span v-if="fieldErrors.emailOrName" class="field-error">{{
                fieldErrors.emailOrName
              }}</span>
            </div>
            <div class="input-group">
              <input
                type="password"
                placeholder="密码"
                v-model="formState.password"
                required
                class="login-input"
                :class="{ error: fieldErrors.password }"
                @blur="validateField('password')"
                @input="clearFieldError('password')"
              />
              <span v-if="fieldErrors.password" class="field-error">{{
                fieldErrors.password
              }}</span>
            </div>
            <button
              type="submit"
              class="login-button"
              :disabled="loading || !isFormValid"
              :class="{ loading: loading }"
            >
              {{ loading ? '登录中...' : '登录' }}
            </button>
            <div v-if="errorMessage" class="error-message">
              {{ errorMessage }}
            </div>
          </form>
          <div class="divider-section">
            <hr class="divider-line" />
            <span class="divider-text">或</span>
            <hr class="divider-line" />
          </div>
          <button class="forgot-password-button" @click="handleForgotPassword">忘记密码</button>
          <div class="register-section">
            没有账号? <a :href="'/user/register'" class="register-link">注册账号</a>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { userLogin } from '@/api/user'
import { useRouter } from 'vue-router'
import { reactive, ref, computed } from 'vue'
import { useLoginUserStore } from '@/store/useLoginUserStore'
import CryptoJS from 'crypto-js'

interface FormState {
  emailOrName: string
  password: string
}

interface FieldErrors {
  emailOrName: string
  password: string
}

const formState = reactive<FormState>({
  emailOrName: '',
  password: '',
})

const fieldErrors = reactive<FieldErrors>({
  emailOrName: '',
  password: '',
})

const loading = ref(false)
const errorMessage = ref('')
const router = useRouter()
const loginUserStore = useLoginUserStore()

// 验证邮箱格式
const isEmail = (str: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(str)
}

// 验证用户名格式（只允许字母、数字、下划线，3-16位）
const isValidUsername = (str: string): boolean => {
  const usernameRegex = /^[a-zA-Z0-9_]{3,16}$/
  return usernameRegex.test(str)
}

// 验证单个字段
const validateField = (field: keyof FieldErrors): boolean => {
  fieldErrors[field] = ''

  if (field === 'emailOrName') {
    if (!formState.emailOrName.trim()) {
      fieldErrors.emailOrName = '请输入用户名'
      return false
    }

    const input = formState.emailOrName.trim()
    if (!isEmail(input) && !isValidUsername(input)) {
      fieldErrors.emailOrName = '请输入有效的用户名或邮箱'
      return false
    }
  }

  if (field === 'password') {
    if (!formState.password.trim()) {
      fieldErrors.password = '请输入密码'
      return false
    }

    if (formState.password.length < 6 || formState.password.length > 12) {
      fieldErrors.password = '密码长度必须在6-12位之间'
      return false
    }
  }

  return true
}

// 清除字段错误
const clearFieldError = (field: keyof FieldErrors) => {
  fieldErrors[field] = ''
  errorMessage.value = ''
}

// 表单整体验证
const validateForm = (): boolean => {
  const emailOrNameValid = validateField('emailOrName')
  const passwordValid = validateField('password')
  return emailOrNameValid && passwordValid
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

// 处理忘记密码
const handleForgotPassword = () => {
  // 可以跳转到忘记密码页面或显示找回密码表单
  alert('忘记密码功能开发中...')
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!validateForm()) {
    return
  }

  // 防止重复提交
  if (loading.value) {
    return
  }

  loading.value = true
  errorMessage.value = ''

  try {
    // 构建请求参数，使用统一的account字段匹配后端UserLoginDTO
    const loginParams = {
      account: formState.emailOrName.trim(),
      password: CryptoJS.MD5(formState.password).toString(),
    }

    const res = await userLogin(loginParams)

    // 登录成功处理
    if (res.data.code === 200 && res.data.data) {
      // 将接口数据转换为前端需要的格式
      const userData = {
        id: res.data.data.id,
        userName: res.data.data.name,
        userAvatar: res.data.data.avatar,
        email: res.data.data.email,
        token: res.data.data.token,
      }

      // 保存用户数据到状态管理和IndexedDB
      await loginUserStore.setLoginUser(userData)

      // 保存到IndexedDB缓存
      try {
        const { saveUserDataWithTimestamp } = await import('@/utils/indexedDB')
        await saveUserDataWithTimestamp(userData)
      } catch (error) {
        console.warn('保存用户数据到IndexedDB失败:', error)
      }

      // 跳转到首页或重定向页面
      router.push({
        path: '/',
        replace: true,
      })
    } else {
      errorMessage.value = res.data.message || '登录失败，账号或密码错误'
    }
  } catch (error: any) {
    errorMessage.value = error.response?.data?.message || '登录失败，请检查网络连接'
  } finally {
    loading.value = false
  }
}

const login = () => {
  handleSubmit()
}
</script>

<style scoped>
#userLoginPage {
  overflow: hidden;
  color: #fff;
  min-height: 50vh;
  display: flex;
  flex-direction: column;
  padding-top: 60px;
}

.login-main {
  flex: 1;
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  width: 320px;
  height: 450px;
  background-color: rgb(24, 24, 27);
  padding: 20px;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.left-nav {
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  white-space: nowrap;
}

.left-nav h2 {
  font-size: 30px;
}

.form-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-group {
  width: 90%;
  margin-bottom: 20px;
}

.login-input {
  width: 100%;
  padding: 15px;
  border: 1px solid #333;
  background-color: #1a1a1d;
  color: #fff;
  border-radius: 10px;
  box-sizing: border-box;
}

.login-button {
  width: 90%;
  padding: 10px;
  background-color: rgb(0, 90, 200);
  color: #fff;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.login-button:disabled {
  background-color: #666;
  cursor: not-allowed;
}

.login-button.loading {
  position: relative;
}

.login-button.loading::after {
  content: '';
  position: absolute;
  width: 16px;
  height: 16px;
  margin: auto;
  border: 2px solid transparent;
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.error-message {
  color: #ff4d4f;
  font-size: 14px;
  margin-top: 10px;
  text-align: center;
}

.login-input.error {
  border-color: #ff4d4f;
}

.field-error {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 4px;
  display: block;
}

.divider-section {
  width: 90%;
  text-align: center;
  margin-top: 20px;
  color: darkgray;
  display: flex;
  align-items: center;
}

.divider-line {
  flex: 1;
  border: none;
  border-top: 1px solid #333;
}

.divider-text {
  margin: 0 10px;
}

.forgot-password-button {
  width: 90%;
  padding: 10px;
  background-color: transparent;
  color: dodgerblue;
  border: 1px solid dodgerblue;
  border-radius: 10px;
  margin-top: 20px;
  cursor: pointer;
}

.register-section {
  margin-top: 20px;
  color: darkgray;
}

.register-link {
  color: dodgerblue;
  text-decoration: none;
}
</style>
