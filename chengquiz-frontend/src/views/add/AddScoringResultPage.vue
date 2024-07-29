<template>
  <div id="addScoringResultPage" style="text-align: center">
    <h2 style="margin-bottom: 32px">设置评分</h2>
    <a-form
      :model="form"
      :style="{ width: '480px', margin: '0 auto' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item label="应用 id">
        {{ appId }}
      </a-form-item>
      <a-form-item v-if="updateId" label="修改评分 id">
        {{ updateId }}
      </a-form-item>
      <a-form-item field="resultName" label="结果名称">
        <a-input v-model="form.resultName" placeholder="请输入结果名称" />
      </a-form-item>
      <a-form-item field="resultDesc" label="结果描述">
        <a-input v-model="form.resultDesc" placeholder="请输入结果描述" />
      </a-form-item>
      <a-form-item field="resultPicture" label="结果图标">
        <PictureUploader
          :value="form.resultPicture"
          :onChange="(value) => (form.resultPicture = value)"
          :biz="resultBiz"
        />
      </a-form-item>
      <a-form-item v-if="appType === 1" field="resultProp" label="结果集">
        <a-input-tag
          v-model="form.resultProp"
          :style="{ width: '320px' }"
          placeholder="请输出结果集，按回车确认"
          allow-clear
        />
      </a-form-item>
      <a-form-item
        v-if="appType === 0"
        field="resultScoreRange"
        label="结果得分范围"
      >
        <a-input-number
          v-model="form.resultScoreRange"
          placeholder="请输入结果得分范围"
        />
      </a-form-item>
      <a-button
        type="primary"
        html-type="submit"
        style="width: 120px; margin: 0 auto"
      >
        提交
      </a-button>
    </a-form>
    <a-divider />
    <h2 style="margin-bottom: 32px; text-align: center">评分管理</h2>
    <ScoringResultTable :appId="appId" :doUpdate="doUpdate" ref="tableRef" />
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watchEffect, withDefaults } from "vue";
import API from "@/api";
import { useRouter } from "vue-router";
import ScoringResultTable from "@/views/add/components/ScoringResultTable.vue";
import {
  addScoringResultUsingPost,
  editScoringResultUsingPost,
} from "@/api/scoringResultController";
import message from "@arco-design/web-vue/es/message";
import { getAppVoByIdUsingGet } from "@/api/appController";
import PictureUploader from "@/components/PictureUploader.vue";

interface Props {
  appId: string;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const resultBiz = "scoring_result_picture";
const tableRef = ref();
const appType = ref<number>();

// 表单参数
const form = ref({
  resultDesc: "",
  resultName: "",
  resultPicture: "",
} as API.ScoringResultAddRequest);

const updateId = ref<any>();

const doUpdate = (scoringResult: API.ScoringResultVO) => {
  updateId.value = scoringResult.id;
  form.value = scoringResult;
};
/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  const app = await getAppVoByIdUsingGet({ id: props.appId as any });
  if (app.data.code === 0) {
    appType.value = app.data.data?.appType;
  } else {
    message.error("获取应用数据失败，" + app.data.message);
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  let res: any;
  // 如果是修改
  if (updateId.value) {
    res = await editScoringResultUsingPost({
      id: updateId.value as any,
      ...form.value,
    });
  } else {
    // 创建
    res = await addScoringResultUsingPost({
      appId: props.appId as any,
      ...form.value,
    });
  }
  if (res.data.code === 0) {
    message.success("操作成功");
  } else {
    message.error("操作失败，" + res.data.message);
  }
  if (tableRef.value) {
    tableRef.value.loadData();
    updateId.value = undefined;
  }
};
</script>
