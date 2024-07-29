<template>
  <a-modal v-model:visible="visible" :footer="false">
    <template #title> {{ title }}</template>
    <div style="text-align: center">
      <h4>复制分享链接</h4>
      <a-typography-paragraph copyable>{{ link }}</a-typography-paragraph>
      <h4>手机扫码查看</h4>
      <img :src="codeImg" alt="" />
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import QRCode from "qrcode";
import { defineProps, ref, withDefaults, defineExpose } from "vue";
import message from "@arco-design/web-vue/es/message";

interface Props {
  title: string;
  link: string;
}

const codeImg = ref();

const props = withDefaults(defineProps<Props>(), {
  link: () => "",
  title: () => "分享",
});

QRCode.toDataURL(props.link)
  .then((url: any) => {
    //console.log(url);
    codeImg.value = url;
  })
  .catch((err: any) => {
    //console.error(err);
    message.error("生成二维码失败" + err.message);
  });

const visible = ref(false);

const openModal = () => {
  visible.value = true;
};

defineExpose({
  openModal,
});
</script>
<style scoped></style>
