/**
 * 获取最佳题目评分结果
 * @param answerList
 * @param questions
 * @param question_results
 */
export function getBestQuestionResult(answerList, questions, question_results) {
  // 初始化一个字典来存储每个维度的分数
  const score = {
    'I': 0,
    'E': 0,
    'S': 0,
    'N': 0,
    'T': 0,
    'F': 0,
    'J': 0,
    'P': 0
  };

  // 遍历答案列表和问题列表来计算分数
  for (let i = 0; i < answerList.length; i++) {
    const answer = answerList[i];  // 用户的回答，"A" 或 "B"
    const question = questions[i]; // 当前的问题对象

    // 找到用户选择的选项
    const selectedOption = question.options.find(option => option.key === answer);

    // 更新相应维度的分数
    if (selectedOption) {
      score[selectedOption.result]++;
    }
  }

  // 根据得分确定最终的四个字母结果
  const finalResult = [
    score['I'] >= score['E'] ? 'I' : 'E',
    score['S'] >= score['N'] ? 'S' : 'N',
    score['T'] >= score['F'] ? 'T' : 'F',
    score['J'] >= score['P'] ? 'J' : 'P'
  ];

  // 在结果列表中找到与计算出的结果匹配的对象
  const bestResult = question_results.find(result => {
    return result.resultProp.every((prop, index) => prop === finalResult[index]);
  });

  // 返回最佳匹配的结果对象
  return bestResult || null; // 如果没有找到匹配的结果，返回 null
}

