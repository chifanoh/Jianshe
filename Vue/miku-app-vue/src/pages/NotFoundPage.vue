<template>
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
// import { useRouter } from 'vue-router'
// const router = useRouter()

// router.push('https://game.haiyong.site')
const game = () => {
  window.location.href = 'https://game.haiyong.site'
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 20px;
}
</style>
