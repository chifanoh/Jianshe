<template>
  <div ref="turnstileContainer" class="turnstile-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'

interface Props {
  siteKey: string
  theme?: 'light' | 'dark' | 'auto'
  size?: 'normal' | 'compact' | 'flexible'
  language?: string
}

const props = withDefaults(defineProps<Props>(), {
  theme: 'auto',
  size: 'flexible',
  language: 'zh-CN',
})

const emit = defineEmits<{
  verify: [token: string]
  error: [error: Error]
  expire: []
}>()

const turnstileContainer = ref<HTMLDivElement>()
let widgetId: string | null = null

// 加载 Turnstile 脚本
const loadTurnstileScript = (): Promise<void> => {
  return new Promise((resolve, reject) => {
    // 如果脚本已加载，直接返回
    if (document.querySelector('script[src*="challenges.cloudflare.com"]')) {
      resolve()
      return
    }

    const script = document.createElement('script')
    script.src = 'https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit'
    script.async = true
    script.defer = true
    script.onload = () => resolve()
    script.onerror = () => reject(new Error('Failed to load Turnstile script'))
    document.head.appendChild(script)
  })
}

// 渲染 Turnstile
const renderTurnstile = () => {
  if (!turnstileContainer.value || !(window as any).turnstile) return

  // 如果已存在 widget，先移除
  if (widgetId) {
    ;(window as any).turnstile.remove(widgetId)
  }

  widgetId = (window as any).turnstile.render(turnstileContainer.value, {
    sitekey: props.siteKey,
    theme: props.theme,
    size: props.size,
    language: props.language,
    'flex-cf': true,
    callback: (token: string) => {
      emit('verify', token)
    },
    'error-callback': (error: Error) => {
      emit('error', error)
    },
    'expired-callback': () => {
      emit('expire')
    },
  })
}

// 重置验证码
const reset = () => {
  if (widgetId && (window as any).turnstile) {
    ;(window as any).turnstile.reset(widgetId)
  }
}

// 获取响应 token
const getResponse = (): string | null => {
  if (widgetId && (window as any).turnstile) {
    return (window as any).turnstile.getResponse(widgetId)
  }
  return null
}

// 暴露方法给父组件
defineExpose({
  reset,
  getResponse,
})

onMounted(async () => {
  try {
    await loadTurnstileScript()
    renderTurnstile()
  } catch (error) {
    emit('error', error as Error)
  }
})

onUnmounted(() => {
  if (widgetId && (window as any).turnstile) {
    ;(window as any).turnstile.remove(widgetId)
  }
})

// 监听 props 变化，重新渲染
watch(
  () => [props.theme, props.size, props.language],
  () => {
    renderTurnstile()
  },
  { deep: true },
)
</script>

<style scoped>
.turnstile-container {
  display: flex;
  justify-content: center;
  min-height: 65px;
  width: 100%;
}

.turnstile-container :deep(iframe) {
  width: 100% !important;
  max-width: 100% !important;
}

.turnstile-container :deep(.cf-turnstile) {
  width: 100% !important;
}

.turnstile-container :deep(div) {
  width: 100% !important;
}
</style>
