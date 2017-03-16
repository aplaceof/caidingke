package net.caidingke.common.algorithm;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.primitives.Ints;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author bowen
 * @create 2017-01-20 10:37
 */

public class NullTest extends TestCase {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class Person implements Comparable<Person> {

        private String firstName;

        private String lastName;

        private int age;

        @Override
        public int compareTo(Person o) {
            return ComparisonChain.start()
                    .compare(this.firstName, o.firstName)
                    .compare(this.age, o.age)
                    .result();
        }
    }

    @Test
    public void testMapNull() {

        Map<String, Object> map = Maps.newHashMap();

        map.put("key", null);

        System.out.println(map.get("key"));
        System.out.println(map.get("value"));

        System.out.println(Optional.of(map));
        System.out.println(Optional.of(map).get().get("key"));
        if (Optional.of(map) instanceof Map) {
            System.out.println("dd");
        }

    }

    @Test
    public void testCompare() {

        Person person = new Person();
        person.setAge(11);
        person.setFirstName("bowen");
        Person person1 = new Person();
        person1.setAge(21);
        person1.setFirstName("bowen");

        Person person2 = new Person();
        person2.setAge(10);

        System.out.println(person.compareTo(person1));

        Ordering.natural().nullsLast();

        List<Person> ss = new ArrayList<>();
        ss.add(person);
        ss.add(person1);
        ss.add(person2);
        Collections.sort(ss);
        for (Person pp : ss) {
            System.out.println(pp.age);
        }
    }

    @Test
    public void testOrdering() {

        Ordering<String> byLengthOrdering = new Ordering<String>() {
            public int compare(String left, String right) {
                return Ints.compare(left.length(), right.length());
            }
        };
        System.out.println(byLengthOrdering.compare("s", "DDD"));

        //Ordering<Person> personOrdering = new Ordering<Person>() {
        //    @Override
        //    public int compare(Person person, Person t1) {
        //        return ComparisonChain.start()
        //                .compare(person.getAge(), t1.getAge())
        //                .compare(person.getFirstName(), t1.getFirstName())
        //                .compare(person.getLastName(), t1.getLastName())
        //                .result();
        //    }
        //};

        Person p1 = new Person("fan", "bowen", 25);
        Person p2 = new Person("fan", "xiaofan", 25);
        Person p3 = new Person("azhang", "san", 25);
        Person p4 = new Person(null, "bowen", 25);

        List<Person> persons = Lists.newArrayList();

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p4);


        //Ordering<Person> ordering = Ordering.natural().nullsLast().onResultOf(new Function<Person, String>() {
        //    public String apply(Person foo) {
        //        return foo.lastName;
        //    }
        //});

        Ordering<Person> ordering = Ordering.natural().nullsLast().onResultOf(new Function<Person, Comparable>() {
            @Override
            public Comparable apply(Person person) {
                return person.getFirstName();
            }
        });

        Collections.sort(persons, ordering);

        //Collections.sort(persons);
        //System.out.println(personOrdering.nullsFirst().compare(p1, p2));
        persons.forEach(person -> System.out.println(person));

        int[] ints = {2, 3, 4};

        System.out.println(Ints.contains(ints, 5));


    }

    @Test
    public void testForeachMap() {
        Map<String, Integer> items = new HashMap<>();
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);

        items.forEach((k, v) -> System.out.print(k));

        items.forEach((k, v) -> System.out.println("Item : " + k + " Count : " + v));

        items.forEach((k, v) -> {
            System.out.println("Item : " + k + " Count : " + v);
            if ("E".equals(k)) {
                System.out.println("Hello E");
            }
        });

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
        }

        items.forEach((k, v) -> {
            if ("A".equals(k)) {
                System.out.println(v);
            }
        });
    }

    @Test
    public void testForeachList() {
        List<String> items = Lists.newArrayList();
        items.add("a");
        items.add("b");
        items.add("c");
        items.add("d");

        for (String item : items) {
            System.out.println(item);

        }

        items.forEach(item -> System.out.println(item));

        items.forEach(item -> {

            if ("a".equals(item)) {
                System.out.println("Yes");
            }
        });

        items.forEach(System.out::println);

        items.stream()
                .filter(s -> s.contains("b"))
                .forEach(System.out::println);
    }

    @Test
    public void testMultiMap() {

        Multimap setMap = HashMultimap.create();
        setMap.put("a", 2);
        setMap.put("a", 3);
        setMap.put("a", 3);

        System.out.println(setMap.containsKey("a"));
        System.out.println(setMap.containsValue(2));
        Collection collection = setMap.get("a");

        setMap.get("a").add("33");
        collection.forEach(item -> System.out.println(item));

        Map<String, Integer> nameToId = Maps.newHashMap();
        Map<Integer, String> idToName = Maps.newHashMap();
        nameToId.put("Bob", 42);
        idToName.put(42, "Bob");
        nameToId.put("Bob", 45);
        idToName.put(45, "Bob");

        System.out.println(nameToId);
        System.out.println(idToName);



    }

    @Test
    public void testMultiSet() {

        Multiset multiset = HashMultiset.create();

        multiset.add("a");
        multiset.add("a");
        multiset.add("a", 10);
        System.out.println(multiset.count("a"));

    }

}
