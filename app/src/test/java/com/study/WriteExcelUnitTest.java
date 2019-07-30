package com.study;

import com.study.file.entity.PeopleEntity;
import com.study.file.util.WriteExcel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * 生成Excel示例
 */
public class WriteExcelUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        List<PeopleEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PeopleEntity peopleEntity = new PeopleEntity();
            peopleEntity.id = (long) i;
            peopleEntity.userName = "张" + i;
            peopleEntity.userAge = 25 + i;
            peopleEntity.userIDCard = "34128777887887878" + i;
            peopleEntity.empNo = "00" + i;
            list.add(peopleEntity);
        }

        String[] title = {"ID", "姓名", "年龄", "身份证号", "员工编号"};
        WriteExcel.initExcel("D://temp//员工信息表.xls", "员工信息表", title);
        WriteExcel.writeObjListToExcel(getList(list), "D://temp//员工信息表.xls");

    }


    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return .
     */
    private ArrayList<ArrayList<String>> getList(List<PeopleEntity> peopleEntities) {
        ArrayList<ArrayList<String>> recordList = new ArrayList<>();
        for (int i = 0; i < peopleEntities.size(); i++) {
            PeopleEntity peopleEntity = peopleEntities.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(peopleEntity.id + "");
            beanList.add(peopleEntity.userName);
            beanList.add(peopleEntity.userAge + "");
            beanList.add(peopleEntity.userIDCard);
            beanList.add(peopleEntity.empNo);
            recordList.add(beanList);
        }
        return recordList;
    }

}