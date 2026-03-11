<template>
  <!-- 登录表单 -->
  <div id="userRegisterPage">
    <main class="register-main">
      <div class="register-container">
        <div class="form-section">
          <div class="left-nav">
            <h2>注册</h2>
          </div>
          <form @submit.prevent="register" class="register-form">
            <div class="input-group">
              <input
                type="text"
                placeholder="用户名"
                v-model="formState.username"
                required
                class="register-input"
                :class="{ error: fieldErrors.username }"
                @blur="validateField('username')"
                @input="clearFieldError('username')"
              />
              <span v-if="fieldErrors.username" class="field-error">{{
                fieldErrors.username
              }}</span>
            </div>
            <div class="input-group">
              <input
                type="text"
                placeholder="邮箱"
                v-model="formState.email"
                required
                class="register-input"
                :class="{ error: fieldErrors.email }"
                @blur="validateField('email')"
                @input="clearFieldError('email')"
              />
              <span v-if="fieldErrors.email" class="field-error">{{ fieldErrors.email }}</span>
            </div>

            <div class="input-group">
              <input
                type="password"
                placeholder="密码"
                v-model="formState.password"
                required
                class="register-input"
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
              class="register-button"
              :disabled="loading || !isFormValid"
              :class="{ loading: loading }"
            >
              {{ loading ? '注册中...' : '注册' }}
            </button>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
// import { useRouter } from 'vue-router' // 暂时注释，后续注册功能实现时使用

interface FormState {
  username: string
  email: string
  password: string
}

interface FieldErrors {
  username: string
  email: string
  password: string
}

const formState = reactive<FormState>({
  username: '',
  email: '',
  password: '',
})

const fieldErrors = reactive<FieldErrors>({
  username: '',
  email: '',
  password: '',
})

const loading = ref(false)
// const router = useRouter() // 暂时注释，后续注册功能实现时使用

// 验证QQ邮箱格式（只允许QQ邮箱）
const isQQEmail = (str: string): boolean => {
  const qqEmailRegex = /^[1-9]\d{4,11}@qq\.com$/
  return qqEmailRegex.test(str)
}

// 验证用户名格式（1-6字符）
const isValidUsername = (str: string): boolean => {
  return str.length >= 1 && str.length <= 6
}

// 验证单个字段
const validateField = (field: keyof FieldErrors): boolean => {
  fieldErrors[field] = ''

  if (field === 'username') {
    if (!formState.username.trim()) {
      fieldErrors.username = '请输入用户名'
      return false
    }
    if (!isValidUsername(formState.username.trim())) {
      fieldErrors.username = '用户名长度必须在1-6字符之间'
      return false
    }
  }

  if (field === 'email') {
    if (!formState.email.trim()) {
      fieldErrors.email = '请输入邮箱'
      return false
    }
    if (!isQQEmail(formState.email.trim())) {
      fieldErrors.email = '请输入正确的QQ邮箱格式'
      return false
    }
  }

  if (field === 'password') {
    if (!formState.password.trim()) {
      fieldErrors.password = '请输入密码'
      return false
    }
    if (formState.password.length < 6 || formState.password.length > 12) {
      fieldErrors.password = '密码长度必须在6-12字符之间'
      return false
    }
  }

  return true
}

// 清除字段错误
const clearFieldError = (field: keyof FieldErrors) => {
  fieldErrors[field] = ''
}

// 表单整体验证
const validateForm = (): boolean => {
  const usernameValid = validateField('username')
  const emailValid = validateField('email')
  const passwordValid = validateField('password')
  return usernameValid && emailValid && passwordValid
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

const register = () => {
  if (!validateForm()) {
    return
  }

  // 防止重复提交
  if (loading.value) {
    return
  }

  loading.value = true

  try {
    // 这里添加实际的注册逻辑
    alert('注册功能开发中...')
    // router.push('/user/login')
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
#userRegisterPage {
  overflow: hidden;
  color: #fff;
  min-height: 50vh;
  display: flex;
  flex-direction: column;
  padding-top: 60px;
}

.register-main {
  flex: 1;
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-container {
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

.left-nav h2 {
  font-size: 30px;
}

.form-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.register-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-group {
  width: 90%;
  margin-bottom: 20px;
}

.register-input {
  width: 100%;
  padding: 15px;
  border: 1px solid #333;
  background-color: #1a1a1d;
  color: #fff;
  border-radius: 10px;
  box-sizing: border-box;
}

.register-button {
  width: 90%;
  padding: 10px;
  background-color: rgb(0, 90, 200);
  color: #fff;
  border: none;
  border-radius: 10px;
  cursor: pointer;
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

.field-error {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 4px;
  display: block;
}

.register-input.error {
  border-color: #ff4d4f;
}

.register-button:disabled {
  background-color: #666;
  cursor: not-allowed;
}

.register-button.loading {
  position: relative;
}

.register-button.loading::after {
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
</style>
