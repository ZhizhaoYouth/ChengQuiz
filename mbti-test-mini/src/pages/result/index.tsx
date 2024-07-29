import { View, Image } from "@tarojs/components";
import { AtButton } from "taro-ui";
import Taro from "@tarojs/taro";
import "./index.scss";
import headerBg from "../../assets/headerBg.jpeg";
import GlobalFooter from "../../components/GlobalFooter";
import questionResults from "../../data/question_results.json";
import questions from "../../data/questions.json";
import { getBestQuestionResult } from "../../utils/bizUtils";
/**
 * 测试结果页面
 */
export default () => {
  // 获取测试结果
  const answerList=Taro.getStorageSync("answerList");
  if(!answerList||answerList.length<1){
    Taro.showToast({
      title: "答案为空",
      icon: "error",
      duration: 3000,
    });
  }
  const result=getBestQuestionResult(answerList,questions,questionResults);

  return (
    <View className="resultPage">
      <View className="at-article__h1 title">{result.resultName}</View>
      <View className="at-article__h2 subTitle">{result.resultDesc}</View>
      <AtButton
        type="primary"
        circle
        className="enterBtn"
        onClick={() => {
          Taro.reLaunch({
            url: "/pages/index/index",
          });
        }}
      >
        返回主页
      </AtButton>
      <Image className="headerBg" src={headerBg} />
      <GlobalFooter />
    </View>
  );
};
