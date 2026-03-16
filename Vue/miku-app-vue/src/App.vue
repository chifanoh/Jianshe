<template>
  <n-config-provider :locale="zhCN" :date-locale="dateZhCN" :theme-overrides="themeOverrides">
    <n-dialog-provider>
      <n-message-provider>
        <!-- 初始化完成显示布局，否则显示加载中 -->
        <template v-if="initialized">
          <BasicLayouts />
        </template>
      </n-message-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>
<style></style>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { zhCN, dateZhCN } from 'naive-ui'
import BasicLayouts from '@/Layouts/BasicLayouts.vue'
import themeOverrides from '@/utils/theme.ts'
import { useLoginUserStore } from '@/store/useLoginUserStore'

const initialized = ref(false)
const loginUserStore = useLoginUserStore()
onMounted(async () => {
  await loginUserStore.loadUserFromCache()
  initialized.value = true
})
</script>
