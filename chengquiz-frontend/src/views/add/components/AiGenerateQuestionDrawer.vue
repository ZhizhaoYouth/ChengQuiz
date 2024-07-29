<template>
  <a-button type="outline" @click="handleClick">使用AI生成题目</a-button>
  <a-drawer
    :width="340"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title>使用 AI 生成题目</template>
    <div>
      <a-form
        :model="form"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
      >
        <a-form-item label="应用 id">
          {{ appId }}
        </a-form-item>
        <a-form-item field="questionCount" label="题目数量">
          <a-input-number
            v-model="form.questionCount"
            min="0"
            max="20"
            placeholder="请输入题目数量"
          />
        </a-form-item>
        <a-form-item field="optionCount" label="选项数量">
          <a-input-number
            v-model="form.optionCount"
            min="0"
            max="5"
            placeholder="请输入选项数量"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button
              :loading="submitting"
              type="primary"
              html-type="submit"
              style="width: 120px"
            >
              生成题目
            </a-button>
            <a-button
              :loading="submitting"
              style="width: 120px"
              @click="handleSubmitSSE"
            >
              实时生成
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults } from "vue";
import API from "@/api";
import { aiGenerateQuestionUsingPost } from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";

interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
  onSSESuccess?: (result: API.QuestionContentDTO) => void;
  onSSEStart?: (event: any) => void;
  onSSEClose?: (event: any) => void;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const visible = ref(false);
const submitting = ref(false);

const form = reactive({
  questionCount: 10,
  optionCount: 2,
} as API.AiGenerateQuestionRequest);

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  const res = await aiGenerateQuestionUsingPost({
    appId: props.appId as any,
    ...form,
  });
  if (res.data.code === 0 && res.data.data.length > 0) {
    if (props.onSuccess) {
      props.onSuccess(res.data.data);
    } else {
      message.success("生成成功");
    }
    handleCancel();
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;
};

/**
 * 提交(流式返回)
 */
const handleSubmitSSE = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  const eventSource = new EventSource(
    "http://localhost:8101/api/question/ai_generate/sse" +
      `?appId=${props.appId}&optionCount=${form.optionCount}&questionCount=${form.questionCount}`
  );
  let first = true;
  //接受消息
  eventSource.onmessage = function (event) {
    if (first) {
      props.onSSEStart?.(event);
      handleCancel();
      first = !first;
    }
    props.onSSESuccess?.(JSON.parse(event.data));
  };
  //
  eventSource.onerror = function (event) {
    if (event.eventPhase === EventSource.CLOSED) {
      eventSource.close();
      props.onSSEClose?.(event);
    }
  };
  submitting.value = false;
};
</script>
