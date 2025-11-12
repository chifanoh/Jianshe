<template>
  <div class="server-error-container">
    <n-result status="500" title="500 服务器错误" description="服务器出错可能说明该雇更多程序员了">
    </n-result>
  </div>
</template>

<style scoped>
.server-error-container {
  display: flex;
  min-height: 60vh;
  align-items: center;
  justify-content: center;
  padding: 111px 0 111px 0;
  background-color: #f7f8fa;
}
</style>
