<template>
  <n-dropdown trigger="click" :options="options" @select="onSelect">
    <div class="operation-warp">
      <u-icon>
        <svg viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M586.624 234.624a74.624 74.624 0 1 1-149.184 0 74.624 74.624 0 0 1 149.12 0z m0 554.624a74.624 74.624 0 1 1-149.248 0 74.624 74.624 0 0 1 149.248 0zM512 586.624a74.624 74.624 0 1 0 0-149.248 74.624 74.624 0 0 0 0 149.248z"
            fill="currentColor"
          ></path>
        </svg>
      </u-icon>
    </div>
  </n-dropdown>
</template>

<script setup lang="ts">
import { useClipboard } from '@vueuse/core'
import { UToast } from 'undraw-ui'
import type { CommentApi } from 'undraw-ui'
import type { DropdownOption } from 'naive-ui'

defineOptions({
  name: 'CommentOperate',
})

interface Props {
  comment: CommentApi
}

const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'remove', comment: CommentApi): void
}>()

const { copy } = useClipboard()

const options: DropdownOption[] = [
  {
    label: '删除',
    key: 'remove',
  },
  {
    type: 'divider',
    key: 'd1',
  },
  {
    label: '复制',
    key: 'copy',
  },
]

const onSelect = (key: string) => {
  switch (key) {
    case 'remove':
      emit('remove', props.comment)
      break
    case 'copy':
      copy(props.comment.content)
      UToast({ type: 'info', message: '复制成功' })
      break
  }
}
</script>

<style scoped>
.operation-warp {
  font-size: 16px;
  color: #9499a0;
  cursor: pointer;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.operation-warp:hover {
  color: #00aeec;
}
</style>
