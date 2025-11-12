<template>
  <div class="mask"></div>
  <div class="not-found-container">
    <n-result status="404" title="404 资源不存在" description="生活总归带点荒谬">
      <template #footer>
        <n-button @click="game">找点乐子吧</n-button>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
import { NResult, NButton } from 'naive-ui'
const game = () => {
  window.location.href = 'https://game.haiyong.site'
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  min-height: 60vh;
  align-items: center;
  justify-content: center;
  padding: 111px 0 111px 0;
  background-color: #f7f8fa;
}
</style>
