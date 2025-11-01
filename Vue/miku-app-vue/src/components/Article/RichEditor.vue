<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      style="height: 200px; overflow-y: hidden"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { onBeforeUnmount, ref, shallowRef, onMounted, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig } from '@wangeditor/editor'

const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const toolbarConfig = {
  toolbarKeys: [
    'header1',
    'header2',
    'bold',
    'italic',
    'underline',
    'divider',
    'insertLink',
    'color',
    'bgColor',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
  ],
}
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入内容...',
  readOnly: false,
}

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef<IDomEditor>()

// 内容 HTML
const valueHtml = ref(props.modelValue || '<p></p>')

// 监听外部值变化
watch(
  () => props.modelValue,
  (newValue) => {
    if (newValue !== valueHtml.value && editorRef.value) {
      valueHtml.value = newValue
      editorRef.value.setHtml(newValue)
    }
  },
)

// 监听编辑器内容变化
watch(valueHtml, (newValue) => {
  emit('update:modelValue', newValue)
})

// 模拟 ajax 异步获取内容
onMounted(() => {
  setTimeout(() => {
    if (!valueHtml.value || valueHtml.value === '<p></p>') {
      valueHtml.value = '<p></p>'
    }
  }, 500)
})

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

// 暴露给模板的响应式数据
const mode = 'default' // 或 'simple'
</script>
