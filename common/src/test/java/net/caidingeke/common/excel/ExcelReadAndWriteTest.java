package net.caidingeke.common.excel;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.caidingke.common.excel.DynamicBean;
import net.caidingke.common.excel.ReadableExcel;
import net.caidingke.common.excel.WritableExcel;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 测试excel读取和写入
 *
 * @author bowen
 * @create 2016-11-10 16:08
 */

public class ExcelReadAndWriteTest {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Throwable {
        writeExcel();
        //readExcel();
        //System.out.println("=======");
        //readExcel2();
        //testDynamicBean();
    }

    private static void writeExcel() throws ParseException {
        File excelFile = new File("person.xlsx");

        List<Person> list = Lists.newArrayList();
        for (int i = 1; i < 100000; i++) {
            list.add(new Person(i, "樊博文" + i, i % 2 == 0, format.parse("1991-03-0" + i)));
        }

        // 属性与 Excel 列的映射关系
        LinkedHashMap<String, String> mapper = new LinkedHashMap<String, String>();
        mapper.put("id", "编号");
        mapper.put("name", "姓名");
        mapper.put("local", "本地户口");
        mapper.put("birthday", "出生日期");

        // 创建一个可写的 Excel 对象
        WritableExcel excel = new WritableExcel(list, mapper);

        // 写出 Excel 文档文件
        boolean success = excel.write(excelFile);

        if (success) {
            System.out.println("success");
        }
    }

    //解析语法1：["id", "name", "local", "birthday"] 从左至右依次对应 Excel 表格的列数据
    private static void readExcel() {
        File excelFile = new File("person.xlsx");

        // 创建一个可读的 Excel 对象
        ReadableExcel excel = new ReadableExcel(excelFile);

        // 解析 Excel 数据为 JavaBean 对象
        List<Person> persons = excel.asList(Person.class, "id", "name", "local", "birthday");

        for (Person person : persons) {

            System.out.println(person);

        }
    }



    /**
     * 解析语法2：["0:id", "2:local", "1:name"] 只提取想要的列的数据（0 代表第 1 列）

     "2:local" 表示将 Excel 表格第 3 列的数据解析成 JavaBean 对象的 name 属性的值

     ["0:id", "2:local", "1:sex"] 等效于 ["id", "2:local", "1:name"]，其余类似，可灵活提取表格数据
     */
    private static void readExcel2() {

        File excelFile = new File("person.xlsx");

        // 创建一个可读的 Excel 对象
        ReadableExcel excel = new ReadableExcel(excelFile);

        // 解析 Excel 数据为 JavaBean 对象
        List<Person> persons = excel.asList(Person.class, "0:id", "2:local", "1:name");

        for (Person person : persons) {

            System.out.println(person);

        }

    }

    private static void testDynamicBean() {
        List<Person> persons = new ArrayList<Person>();

        // 创建动态 Bean 实例
        DynamicBean dynaBean = new DynamicBean(Person.class);

        // 创建一个 Bean 的实例
        dynaBean.newBeanInstance();
        // 设置 Bean 属性的值
        dynaBean.setFieldValue("id", 1);
        dynaBean.setFieldValue("name", "张三");
        dynaBean.setFieldValue("local",Boolean.TRUE);
        dynaBean.setFieldValue("birthday", new Date());
        // 取出 Bean 对象
        persons.add((Person) dynaBean.getBean());

        // 创建一个 Bean 的实例
        dynaBean.newBeanInstance();
        // 设置 Bean 属性的值
        dynaBean.setFieldValue("id", 2);
        dynaBean.setFieldValue("name", "李四");
        dynaBean.setFieldValue("local",Boolean.FALSE);
        dynaBean.setFieldValue("birthday", new Date());
        // 取出 Bean 对象
        persons.add((Person) dynaBean.getBean());

        for (Person person : persons) {

            System.out.println(person);
        }

    }


    @Getter
    @Setter
    public static class Person {

        private int id;

        private String name;

        private boolean local;

        private Date birthday;

        public Person() {

        }

        public Person(int id, String name, boolean local, Date birthday) {
            this.id = id;
            this.name = name;
            this.local = local;
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", local=" + local +
                    ", birthday=" + birthday +
                    '}';
        }
    }

}
