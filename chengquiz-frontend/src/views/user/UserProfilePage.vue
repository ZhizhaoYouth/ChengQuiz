<template>
  <div id="userProfilePage" style="text-align: center">
    <h2 style="margin-bottom: 32px">修改个人资料</h2>
    <a-form
      :model="form"
      :style="{ width: '480px', margin: '0 auto' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item field="id" label="用户ID">
        {{ form.id }}
      </a-form-item>
      <a-form-item field="userName" label="昵称">
        <a-input
          v-model="form.userName"
          placeholder="请输入昵称"
          max-length="15"
          :word-length="countChineseCharacters"
          :show-word-limit="true"
        />
      </a-form-item>
      <a-form-item field="userAvatar" label="上传头像">
        <PictureUploader
          :value="form.userAvatar"
          :onChange="(value) => (form.userAvatar = value)"
          :biz="userBiz"
        />
      </a-form-item>
      <a-form-item field="userProfile" label="用户简介">
        <a-input
          v-model="form.userProfile"
          placeholder="请输入用户简介"
          max-length="80"
          :show-word-limit="true"
        />
      </a-form-item>
      <a-form-item field="userRole" label="用户角色">
        {{ USER_ROLE_MAP[form.userRole] }}
      </a-form-item>
      <a-button
        type="primary"
        html-type="submit"
        :style="{ width: '120px', margin: '0 auto' }"
      >
        提交
      </a-button>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, ref, watchEffect, withDefaults } from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import PictureUploader from "@/components/PictureUploader.vue";
import {
  getLoginUserUsingGet,
  updateMyUserUsingPost,
  updateUserUsingPost,
} from "@/api/userController";
import { USER_ROLE_MAP } from "@/constant/app";
import MDEditor from "@/components/MDEditor.vue";
import MDViewer from "@/components/MDViewer.vue";

const router = useRouter();

const form = ref({
  id: 0,
  userAvatar: "",
  userName: "",
  userProfile: "",
  userRole: "",
} as API.LoginUserVO);

const oldUserProfile = ref<API.LoginUserVO>();

const userBiz = "user_avatar";

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await getLoginUserUsingGet();
  if (res.data.code === 0 && res.data.data) {
    oldUserProfile.value = res.data.data;
    form.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

function countChineseCharacters(value) {
  let count = 0;
  for (let i = 0; i < value.length; i++) {
    const char = value[i];
    if (char.match(/[\u4e00-\u9fa5]/)) {
      count += 2;
    } else {
      count += 1;
    }
  }
  return count;
}

/**
 * 提交
 */
const handleSubmit = async () => {
  const res = await updateMyUserUsingPost({
    ...form.value,
  });
  if (res.data.code === 0) {
    message.success("修改成功，即将回到主页");
    setTimeout(() => {
      router.push(`/`);
    }, 3000);
  } else {
    message.error("操作失败，" + res.data.message);
  }
};
</script>
