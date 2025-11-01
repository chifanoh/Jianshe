<template>
  <n-message-provider>
    <n-dialog-provider>
      <div id="basicLayouts">
        <GloubalHeader />
        <div class="content-wrap">
          <!-- 路由出口-缓存页面,减少不必要的性能开销 -->
          <router-view v-slot="{ Component }">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </router-view>
        </div>
        <GloubalFooter />
      </div>
    </n-dialog-provider>
  </n-message-provider>
</template>

<script setup lang="ts">
import { NMessageProvider, NDialogProvider } from 'naive-ui'
import GloubalHeader from '@/components/GloubalHeader.vue'
import GloubalFooter from '@/components/GloubalFooter.vue'
</script>

<style scoped>
#basicLayouts {
  /* width: 1200px; */
  margin: 0 auto;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-wrap {
  flex: 1;
}
</style>
