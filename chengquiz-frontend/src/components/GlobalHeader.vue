<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="titleBar">
            <img class="logo" src="../assets/logo.png" />
            <div class="title">橙小测</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path"
          >{{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="260px" :style="{ marginRight: '20px' }">
      <div v-if="loginUserStore.loginUser.id">
        <a-avatar
          :size="48"
          :image-url="loginUserStore.loginUser.userAvatar"
          :style="{ marginRight: '10px' }"
          shape="square"
          @click="handleAvatarClick"
        />
        {{ loginUserStore.loginUser.userName ?? "无名" }}
        <a-button
          type="primary"
          size="small"
          @click="handleLogout"
          style="margin-left: 10px"
          >退出
        </a-button>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref, watchEffect } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { userLogoutUsingPost } from "@/api/userController";
import message from "@arco-design/web-vue/es/message";
import API from "@/api";

const loginUserStore = useLoginUserStore();

const router = useRouter();
//当前选中的菜单项
const selectedKeys = ref(["/"]);
//路由跳转时自动更新选中菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});
//展示在菜单中的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});
//点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const handleAvatarClick = async () => {
  router.push("/edit/profile");
};

const handleLogout = async () => {
  const res = await userLogoutUsingPost({});
  await loginUserStore.fetchLoginUser();
  if (res.data.code === 0) {
    message.success("退出登录成功");
    //跳转到登录页面
    router.push("/user/login");
  } else {
    message.error("退出登录失败");
  }
};
</script>

<style scoped>
#globalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.title {
  margin-left: 16px;
  color: black;
  font-weight: 600;
  font-size: 15px;
}

.logo {
  height: 48px;
}
</style>
