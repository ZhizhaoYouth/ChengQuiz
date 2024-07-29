<template>
  <div id="userLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <img class="logo" src="../assets/logo.png" />
          <div>橙小测 AI 问卷测评平台</div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer"> 欢迎使用橙小测平台！</a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
#userLayout {
  text-align: center;
}

#userLayout .logo {
  width: 48px;
  height: 48px;
}

#userLayout .content {
  margin-bottom: 28px;
  padding: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
}

#userLayout .header {
  margin-top: 10px;
  margin-bottom: 16px;
  font-size: 21px;
}

#userLayout .footer {
  padding: 16px;
  text-align: center;
  background: #efefef;
}
</style>
