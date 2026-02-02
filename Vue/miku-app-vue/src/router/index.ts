import HomePage from '@/pages/HomePage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserProfilePage from '@/pages/user/UserProfilePage.vue'
import ArticleDetail from '@/components/Article/ArticleDetail.vue'
import SearchPage from '@/pages/common/SearchPage.vue'
import NotFoundPage from '@/Result/NotFoundPage.vue'
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  { path: '', name: 'home', component: HomePage },
  { path: '/search', name: 'search', component: SearchPage },
  { path: '/user/login', name: 'userLogin', component: UserLoginPage },
  { path: '/user/register', name: 'userRegister', component: UserRegisterPage },
  { path: '/user/profile', name: 'userProfile', component: UserProfilePage },
  { path: '/post/:id', name: 'articleDetail', component: ArticleDetail },
  // 404页面，必须放在最后
  { path: '/:pathMatch(.*)*', name: 'notFound', component: NotFoundPage },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
