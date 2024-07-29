import {
  NavigationGuardNext,
  RouteLocationNormalized,
  RouteRecordRaw,
} from "vue-router";
import HomePage from "@/views/HomePage.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import ACCESS_ENUM from "@/access/accessEnum";
import NoAuthPage from "@/views/NoAuthPage.vue";
import UserLoginPage from "@/views/user/UserLoginPage.vue";
import UserRegisterPage from "@/views/user/UserRegisterPage.vue";
import AdminUserPage from "@/views/admin/AdminUserPage.vue";
import AdminAppPage from "@/views/admin/AdminAppPage.vue";
import AdminQuestionPage from "@/views/admin/AdminQuestionPage.vue";
import AdminScoringResultPage from "@/views/admin/AdminScoringResultPage.vue";
import AdminUserAnswerPage from "@/views/admin/AdminUserAnswerPage.vue";
import AppDetailPage from "@/views/app/AppDetailPage.vue";
import AddAppPage from "@/views/add/AddAppPage.vue";
import AddQuestionPage from "@/views/add/AddQuestionPage.vue";
import AddScoringResultPage from "@/views/add/AddScoringResultPage.vue";
import DoAnswerPage from "@/views/answer/DoAnswerPage.vue";
import AnswerResultPage from "@/views/answer/AnswerResultPage.vue";
import MyAnswerPage from "@/views/answer/MyAnswerPage.vue";
import { useLoginUserStore } from "@/store/userStore";
import { computed, ref } from "vue";
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { getUserAnswerVoByIdUsingGet } from "@/api/userAnswerController";
import AppStatisticPage from "@/views/statistic/AppStatisticPage.vue";
import MyAppPage from "@/views/app/MyAppPage.vue";
import UserProfilePage from "@/views/user/UserProfilePage.vue";

const isMyApp = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const loginUserStore = useLoginUserStore();
  const userId = loginUserStore.loginUser?.id;
  const data = ref<API.AppVO>({});
  if (!userId) {
    // 用户未登录，跳转
    next({ path: "/user/login" });
  } else {
    // 检查用户是否有权限访问该App
    const appId = to.params.id;
    const res = await getAppVoByIdUsingGet({
      id: appId as any,
    });
    if (res.data.code === 0) {
      data.value = res.data.data as any;
    } else {
      message.error("获取数据失败，" + res.data.message);
    }
    const isMy = computed(() => {
      return userId && userId === data.value.userId;
    });
    if (isMy.value) {
      next(); // 有权限，继续访问
    } else {
      next({ path: "/noAuth" }); // 无权限，跳转到无权限页面
    }
  }
};

const isMyAppBeforeAdd = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const loginUserStore = useLoginUserStore();
  const userId = loginUserStore.loginUser?.id;
  const data = ref<API.AppVO>({});
  if (!userId) {
    // 用户未登录，跳转
    next({ path: "/user/login" });
  } else {
    // 检查用户是否有权限访问该App
    const appId = to.params.appId;
    const res = await getAppVoByIdUsingGet({
      id: appId as any,
    });
    if (res.data.code === 0) {
      data.value = res.data.data as any;
    } else {
      message.error("获取数据失败，" + res.data.message);
    }
    const isMy = computed(() => {
      return userId && userId === data.value.userId;
    });
    if (isMy.value) {
      next(); // 有权限，继续访问
    } else {
      next({ path: "/noAuth" }); // 无权限，跳转到无权限页面
    }
  }
};

const isMyResult = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const loginUserStore = useLoginUserStore();
  const userId = loginUserStore.loginUser?.id;
  const data = ref<API.ScoringResultVO>({});
  if (!userId) {
    // 用户未登录，跳转
    next({ path: "/user/login" });
  } else {
    // 检查用户是否有权限访问该Result
    const resultId = to.params.id;
    const res = await getUserAnswerVoByIdUsingGet({
      id: resultId as any,
    });
    if (res.data.code === 0) {
      data.value = res.data.data as any;
    } else {
      message.error("获取数据失败，" + res.data.message);
    }
    const isMy = computed(() => {
      return userId && userId === data.value.userId;
    });
    if (isMy.value) {
      next(); // 有权限，继续访问
    } else {
      next({ path: "/noAuth" }); // 无权限，跳转到无权限页面
    }
  }
};

const isPassedOrIsMy = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const loginUserStore = useLoginUserStore();
  const userId = loginUserStore.loginUser?.id;
  const data = ref<API.AppVO>({});
  if (!userId) {
    // 用户未登录,但也能查看应用详情
    next();
  } else {
    // 检查用户是否有权限访问该App
    const appId = to.params.id;
    const res = await getAppVoByIdUsingGet({
      id: appId as any,
    });
    if (res.data.code === 0) {
      data.value = res.data.data as any;
    } else {
      message.error("获取数据失败，" + res.data.message);
    }
    const isMy = computed(() => {
      return userId && userId === data.value.userId;
    });
    if (data.value.reviewStatus === 1 || isMy.value) {
      next();
    } else {
      next({ path: "/noAuth" }); // 未过审，跳转到无权限页面
    }
  }
};

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "主页",
    component: HomePage,
  },
  {
    path: "/edit/profile",
    name: "用户资料",
    component: UserProfilePage,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/add/app",
    name: "创建应用",
    component: AddAppPage,
  },
  {
    path: "/edit/app/:id",
    name: "修改应用",
    props: true,
    component: AddAppPage,
    beforeEnter: isMyApp,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/add/question/:appId",
    name: "创建题目",
    component: AddQuestionPage,
    props: true,
    beforeEnter: isMyAppBeforeAdd,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/add/scoring_result/:appId",
    name: "创建评分",
    component: AddScoringResultPage,
    props: true,
    beforeEnter: isMyAppBeforeAdd,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/app/detail/:id",
    name: "应用详情页",
    props: true,
    component: AppDetailPage,
    beforeEnter: isPassedOrIsMy,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/answer/do/:appId",
    name: "答题",
    component: DoAnswerPage,
    props: true,
    meta: {
      hideInMenu: true,
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/answer/result/:id",
    name: "答题结果",
    component: AnswerResultPage,
    props: true,
    beforeEnter: isMyResult,
    meta: {
      hideInMenu: true,
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/app/my",
    name: "我的应用",
    component: MyAppPage,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/answer/my",
    name: "我的答题",
    component: MyAnswerPage,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/admin/user",
    name: "用户管理",
    component: AdminUserPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/admin/app",
    name: "应用管理",
    component: AdminAppPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/admin/question",
    name: "题目管理",
    component: AdminQuestionPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/admin/scoring_result",
    name: "评分管理",
    component: AdminScoringResultPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/admin/user_answer",
    name: "回答管理",
    component: AdminUserAnswerPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/app_statistic",
    name: "应用统计",
    component: AppStatisticPage,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuthPage,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: UserLoginPage,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterPage,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
];
