package com.yingzi.myLearning.service.Chapter_3_Common;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/7/23 10:50
 */
public class Lambda {
    private  final String a="Lambda";
    /**
     * 遍历时的查询建议查询所有在内存中做比对
     * 减少对数据库的请求 加快反应速度
     */
    private void  assembleResult(){

        //做list-->map(k,V)
        List<TTargetEntity> targetList=manageTargetService.getResult(null,null);
        Map<String,String> CodeNameMap =targetList.stream().collect(
                Collectors.toMap(TTargetEntity::getTargetCode, TTargetEntity::getTargetName, (v1, v2) -> v1));

        //  struct orgCode->reason map，splice the reason while find duplicate orgCode
        Map<String, String> codeReasonMap = jgclassifyList.stream().collect(
                Collectors.toMap(TJgclassifyEntity::getDepartmentCode, TJgclassifyEntity::getReasonName, (v1, v2) -> v1 + " ； " + v2));


        //做灵活的分组处理 相比与数据库下sql的groupBy更灵活更快
        Map<String, List<TJgclassifyEntity>> jgClassifyMap = jgclassifyList.stream().collect(
                groupingBy(TJgclassifyEntity::getOrgClassifg));

        //lambda 表达的其实就是匿名类的简化属于匿名类 使用值传递 所以a是final型的
        jgClassifyMap.forEach((a,list)->{
            a.equals("Lambda");
            list.size();
        });



        List<FinalResultEntity> list=manageResultService.getAllOrgSCore(dto);
        //根据总分进排名 由分数倒序排
        List<FinalResultEntity> sortByScoreList =
                list.stream()
                        .sorted(Comparator
                                .comparing(FinalResultEntity::getTotalScore)
                                .reversed())
                        .collect(Collectors.toList());



        List<TJgclassifyEntity> reList1 = jgClassifyMap.get(jglx);
        //拿到所有distinct的该机构code
        List<String> departCodeList = reList1.stream().map(
                TJgclassifyEntity::getDepartmentCode).distinct().collect(Collectors.toList());
        departCodeList.forEach(t->{
            doSomeThing();
        });

    }
}
