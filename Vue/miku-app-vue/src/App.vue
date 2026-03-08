<template>
  <n-config-provider :locale="zhCN" :date-locale="dateZhCN" :theme-overrides="themeOverrides">
    <n-dialog-provider>
      <n-message-provider>
        <BasicLayouts />
      </n-message-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>
<style></style>
<script setup lang="ts">
import { zhCN, dateZhCN } from 'naive-ui'
import BasicLayouts from '@/Layouts/BasicLayouts.vue'
import themeOverrides from './utils/theme'
</script>
