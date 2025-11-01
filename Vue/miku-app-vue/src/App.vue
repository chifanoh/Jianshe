<template>
  <n-config-provider :locale="zhCN" :date-locale="dateZhCN">
    <BasicLayouts />
  </n-config-provider>
</template>
<style></style>
<script setup lang="ts">
import { zhCN, dateZhCN } from 'naive-ui'
import BasicLayouts from '@/Layouts/BasicLayouts.vue'
</script>
