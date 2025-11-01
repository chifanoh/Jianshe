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
                v-model="username"
                required
                class="register-input"
              />
            </div>
            <div class="input-group">
              <input
                type="text"
                placeholder="邮箱"
                v-model="email"
                required
                class="register-input"
              />
            </div>
            <div class="input-group">
              <input
                type="text"
                placeholder="验证码"
                v-model="code"
                required
                class="register-input"
              />
            </div>
            <div class="input-group">
              <input
                type="password"
                placeholder="密码"
                v-model="password"
                required
                class="register-input"
              />
            </div>
            <button type="submit" class="register-button">注册</button>
          </form>
          <div class="divider-section">
            <hr class="divider-line" />
            <span class="divider-text">或</span>
            <hr class="divider-line" />
          </div>
          <button class="forgot-password-button">忘记密码</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const router = useRouter()

const register = () => {
  // 简单的登录逻辑，实际应用中应与后端交互
  if (username.value === 'test' && password.value === '123456') {
    router.push('/') // 登录成功后直接跳转到主页面
  } else {
    alert('用户名或密码错误！')
  }
}
</script>

<style scoped>
#userRegisterPage {
  overflow: hidden;
  background-color: rgb(245, 245, 245);
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
  height: 600px;
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
</style>
