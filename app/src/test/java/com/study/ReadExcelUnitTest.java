package com.study;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 作者: TangRen on 2019/7/30
 * 包名：com.study
 * 邮箱：996489865@qq.com
 * TODO:读取Excel示例
 */
public class ReadExcelUnitTest {
    @Test
    public void test() {
        // 字符列表
        List<String> list = new ArrayList<String>();
        // 文件路径
        String filePath = "D://temp//员工信息表.xls";
        // 输入流
        InputStream is = null;
        // Excel工作簿
        Workbook workbook = null;

        try {
            // 加载Excel文件
            is = new FileInputStream(filePath);
            // 获取workbook
            workbook = Workbook.getWorkbook(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取sheet, 如果你的workbook里有多个sheet可以利用workbook.getSheets()方法来得到所有的
        // 这里只取得第一个sheet的值，默认从0开始
        assert workbook != null;
        Sheet sheet = workbook.getSheet(0);
        // 查看sheet的列
        System.out.println(sheet.getColumns());
        // 查看sheet的行
        System.out.println(sheet.getRows());

        Cell cell = null;// 单个单元格
        // 开始循环，取得cell里的内容，按具体类型来取
        // 这里只取String类型
        for (int j = 0; j < sheet.getColumns(); j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sheet.getRows(); i++) {
                // 列,行
                cell = sheet.getCell(j, i);
                // 获取单元格内容
                sb.append(cell.getContents());
                // 将单元格的每行内容用逗号隔开
                sb.append(",");
            }
            //将每行的字符串用一个String类型的集合保存。
            list.add(sb.toString());
        }

        workbook.close();// 关闭工作簿

        // 迭代集合查看每行的数据
        for (String ss : list) {
            System.out.println(ss);
        }
    }
}
