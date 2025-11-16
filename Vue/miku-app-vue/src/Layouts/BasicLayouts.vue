<template>
  <n-message-provider>
    <n-dialog-provider>
      <div id="basicLayouts">
        <GloubalHeader />
        <div class="content-wrap">
          <router-view v-slot="{ Component }">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </router-view>
        </div>
        <!-- <GloubalFooter /> -->
      </div>
    </n-dialog-provider>
  </n-message-provider>
</template>

<script setup lang="ts">
import { NMessageProvider, NDialogProvider } from 'naive-ui'
import GloubalHeader from '@/components/GloubalHeader.vue'
// import GloubalFooter from '@/components/GloubalFooter.vue'
</script>

<style scoped>
#basicLayouts {
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-wrap {
  flex: 1;
}
</style>
