import App from './App.vue'
import router from './router'
import naive from 'naive-ui'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { authChecker } from '@/utils/auth'

const pinia = createPinia()

createApp(App).use(router).use(naive).use(pinia).mount('#app')
// 启动认证状态检查器
authChecker.startChecking((isAuth) => {
  if (!isAuth) {
    console.warn('认证状态失效，需要重新登录')
    // 可以在这里触发登出操作或跳转到登录页
    // 例如：router.push('/login')
  }
})
