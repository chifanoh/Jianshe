<template>
  <!-- 页脚 -->
  <footer class="footer">
    <div class="footer-content">
      <span class="footer-text"></span>
      <div class="footer-links">
        <a href="#" style="text-decoration: none">小站简介</a>
        <a href="#" style="text-decoration: none">友情链接</a>
        <a href="#" style="text-decoration: none">联系我们</a>
        <a href="#" style="text-decoration: none">GitHub 仓库</a>
      </div>
    </div>
  </footer>
</template>
<script setup lang="ts"></script>
<style scoped>
.footer {
  color: #000000;
  background-color: #ffffff;
  padding: 20px;
  text-align: center;
  border-top: 1.5px solid #c5c5c580;
}

.footer-content {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-logo {
  height: 24px;
  width: 24px;
}

.footer-links a {
  color: #000000;
  margin: 0 10px;
}

.footer-links a:hover {
  text-decoration: underline;
}
</style>
