import App from './App.vue'
import router from './router'
import naive from 'naive-ui'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
// import { authChecker } from '@/utils/auth'

const pinia = createPinia()

createApp(App).use(router).use(naive).use(pinia).mount('#app')
// 启动认证状态检查器(后期需要再打开)
// authChecker.startChecking((isAuth) => {
//   if (!isAuth) {
//     console.warn('认证状态失效，需要重新登录')
//     // 可以在这里触发登出等操作,例如:router.push('/login')
//   }
// })
