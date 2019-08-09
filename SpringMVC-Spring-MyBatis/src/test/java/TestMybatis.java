
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.cpic.center.model.SurveyEntity;
import com.cpic.center.service.CpicSurveyService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class) // = extends SpringJUnit4ClassRunner
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:spring-mybatis.xml"})
public class TestMybatis {

    private static final Logger logger = Logger.getLogger(TestMybatis.class);
    @Autowired
    private CpicSurveyService cpicSurveyService;


    @Test
    public void test1() {

        List<SurveyEntity> list = cpicSurveyService.getAll();
        logger.info(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void test2() {

        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setId("-00001");
        surveyEntity.setUsrName("hkhk");
        surveyEntity.setDepartmentName("bulibuli");
        surveyEntity.setQuestionCode(0);
        surveyEntity.setReplyCode(0);
        surveyEntity.setReplyDetail("啦啦啦啦啦");
        surveyEntity.setFinishDate(new Date());
        surveyEntity.setActDate(new Date());
        surveyEntity.setIsValid(1);
        surveyEntity.setNewColumn(new BigDecimal("0"));
        logger.info(surveyEntity.toString());
        int i = cpicSurveyService.insert(surveyEntity);
        logger.info(JSON.toJSONStringWithDateFormat("add " + i, "yyyy-MM-dd HH:mm:ss"));
    }

//	//@Test
//	public void test3() {
//
//		SurveyEntity SurveyEntity = new SurveyEntity();
//		SurveyEntity.setId("0000");
//		SurveyEntity.setName("bbbb");
//		SurveyEntity.setAge(1234);
//		SurveyEntity.setAddress("ABCD");
//		int i = cpicSurveyService.update(SurveyEntity);
//		logger.info(JSON.toJSONStringWithDateFormat("update " +i, "yyyy-MM-dd HH:mm:ss"));
//	}
//
//	//@Test
//	public void test4() {
//
//		SurveyEntity SurveyEntity = new SurveyEntity();
//		SurveyEntity.setId("0000");
//		SurveyEntity.setName("bbbb");
//		SurveyEntity.setAge(1234);
//		SurveyEntity.setAddress("ABCD");
//		int i = cpicSurveyService.delete("0000");
//		logger.info(JSON.toJSONStringWithDateFormat("delete "+i, "yyyy-MM-dd HH:mm:ss"));
//	}
//
@Test
public void tset0(){
    System.out.println("test ok"+cpicSurveyService);
}
}
