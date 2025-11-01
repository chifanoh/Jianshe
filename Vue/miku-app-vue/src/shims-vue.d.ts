declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
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
  
  export const Editor: DefineComponent<EditorProps, {}, any>
  export const Toolbar: DefineComponent<ToolbarProps, {}, any>
}
