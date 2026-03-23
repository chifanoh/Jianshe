declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<object, object, unknown>
  export default component
}

declare module '@wangeditor/editor-for-vue' {
  import type { DefineComponent } from 'vue'
  import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'

  interface EditorProps {
    modelValue?: string
    defaultConfig?: Partial<IEditorConfig>
    mode?: 'default' | 'simple'
    onCreated?: (editor: IDomEditor) => void
  }

  interface ToolbarProps {
    editor?: IDomEditor
    defaultConfig?: Partial<IToolbarConfig>
    mode?: 'default' | 'simple'
  }

  export const Editor: DefineComponent<EditorProps, object, unknown>
  export const Toolbar: DefineComponent<ToolbarProps, object, unknown>
}
