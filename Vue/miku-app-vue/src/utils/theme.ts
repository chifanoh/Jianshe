// theme.ts
import type { GlobalThemeOverrides } from 'naive-ui'
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#cccccc', // 主色
    primaryColorHover: '#cccccc', // 悬停色
    primaryColorPressed: '#cccccc', // 按下色
  },
  Button: {
    textColor: '#000000', // 按钮文字颜色
  },
}
export default themeOverrides
