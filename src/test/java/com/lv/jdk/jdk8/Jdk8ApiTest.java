package com.lv.jdk.jdk8;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@DisplayName("jdk8.0特性测试")
public class Jdk8ApiTest {

    /**
     * org.itstack.demo.AFormula
     * 抽象类的使用
     * 1、abstract方式定义抽象类
     * 2、在抽象类中提供抽象方法
     * 3、提供默认的方法
     */
    @Test
    @DisplayName("匿名函数实现抽象方法")
    public void test_00() {
        AFormula aFormula = new AFormula() {
            @Override
            double calculate(int a) {
                return a * a;
            }
        };

        System.out.println(aFormula.calculate(2)); //求平方：4
        System.out.println(aFormula.sqrt(2));     //求开方：1.4142135623730951
    }

    /**
     * org.itstack.demo.IFormula
     * jdk1.8接口的使用方式
     * 1、也可以在接口中定义方法
     */
    @Test
    @DisplayName("接口匿名函数实现抽象方法")
    public void test_01() {
        IFormula formula = new IFormula() {
            @Override
            public double calculate(int a) {
                return a * a;
            }
        };

        System.out.println(formula.calculate(2));
        System.out.println(formula.sqrt(2));
    }

    /**
     * 使用上还可以更简单
     */
    @Test
    @DisplayName("lambda表达式")
    public void test_02() {
        // 入参a 和 实现
        IFormula formula = a -> a * a;
        System.out.println(formula.calculate(2));
        System.out.println(formula.sqrt(2));
    }

    @Test
    @DisplayName("数组排序")
    public void test_03() {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        //通过匿名类实现
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });
        //lambda 实现
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });
        //lambda精简
        names.sort((a, b) -> b.compareTo(a));
        //排序倒转
        names.sort(Comparator.reverseOrder());

        System.out.println(JSON.toJSONString(names));
    }

    @DisplayName("函数式接口")
    @Test
    public void test04() {
        //函数式接口将字符串转换为数值类型
        IConverter<String, Integer> converter01 = new IConverter<String, Integer>() {
            @Override
            public Integer convert(String from) {
                return Integer.valueOf(from);
            }
        };

        IConverter<String, Integer> converter02 = (from) -> {
            return Integer.valueOf(from);
        };

        IConverter<String, Integer> converter03 = from -> Integer.valueOf(from);
        Integer converted03 = converter03.convert("123");
        System.out.println(converted03);

        IConverter<Integer, String> converter04 = String::valueOf;
        String converted04 = converter04.convert(11);
        System.out.println(converted04);

    }
    @DisplayName("函数式接口")
    @Test
    public void test05() {
        Something something = new Something();
        //方法内部的lambda 实现s -> String.valueOf(s.charAt(0)
        IConverter<String, String> converter01 = s -> String.valueOf(s.charAt(0)); //参照物，直接把逻辑放到这调用
        IConverter<String, String> converter02 = something::startsWith;            //方法引用
        System.out.println(converter01.convert("Java"));
        System.out.println(converter02.convert("Java"));
    }

    @DisplayName("方法引用，双冒号")
    @Test
    public void test06() {
        //lambad实现
        IPersonFactory<Person> personIPersonFactory = (firstName, lastName) -> new Person(firstName, lastName); //[参照物]，Lambda
        /**
         * 双冒号的使用条件
         * 使用双冒号有两个条件：
         *
         * 条件1
         * 条件1为必要条件，必须要满足这个条件才能使用双冒号。
         * Lambda表达式内部只有一条表达式（第一种Lambda表达式），并且这个表达式只是调用已经存在的方法，不做其他的操作。
         *
         * 条件2
         * 由于双冒号是为了省略item ->这一部分，所以条件2是需要满足不需要写参数item也知道如何使用item的情况。
         * 有两种情况可以满足这个要求，这就是我将双冒号的使用分为2类的依据。
         *
         *
         * 静态方法引用（static method）语法：classname::methodname 例如：Person::getAge
         * 对象的实例方法引用语法：instancename::methodname 例如：System.out::println
         * 对象的超类方法引用语法： super::methodname
         * 类构造器引用语法： classname::new 例如：ArrayList::new
         * 数组构造器引用语法： typename[]::new 例如： String[]:new
         */
        IPersonFactory<Person> personFactory = Person::new;
        Person person = personIPersonFactory.create("Peter", "Parker");
        System.out.println(person);
    }
    @DisplayName("lambda表达式")
    @Test
    public void test07() {
        int num = 1;
        IConverter<Integer, String> stringConverter = from -> String.valueOf(from + num);
        String convert = stringConverter.convert(2);
        System.out.println(convert); // 3
    }


    /**
     * 断言，判断参数是否符合条件
     */
    @DisplayName("Predicate")
    @Test
    public void test11() {
        //设置 一个表达式，参数的长度大于0
        Predicate<String> predicate = (s) -> s.length() > 0;

        boolean foo0 = predicate.test("foo");           // true

        //否定取反
        boolean foo1 = predicate.negate().test("foo");  // negate否定相当于!true

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(predicate.and(isEmpty).test("1")); //两个断言为true才为true
    }

    /**
     * 接收一个参数，返回一个泛型<T,R> R的结果
     */
    @DisplayName("Function")
    @Test
    public void test12() {
        Function<String, Integer> toInteger = (a)->Integer.valueOf(a)*2;                                     //1、转Integer
        Function<String, String> backToString = toInteger.andThen(String::valueOf);                     //2、转String
        Function<String, String> afterToStartsWith = backToString.andThen(new Something()::startsWith); //3、截取第一位

        String apply = afterToStartsWith.apply("123");// "123"
        System.out.println(apply);
    }

    /**
     * 供应商 不接受参数，返回一个执行方法的结果
     */
    @DisplayName("Supplier")
    @Test
    public void test13() {
        Supplier<Person> personSupplier0 = Person::new;
        personSupplier0.get();   // new Person

        Supplier<String> personSupplier1 = Something::test01;  //这个test方法是静态的，且无入参
        String s = personSupplier1.get();// hi
        System.out.println(s);

        Supplier<String> personSupplier2 = new Something()::test02; //hi
    }

    /**
     * 消费者：接受参数，执行参数结果，不返回值
     */
    @DisplayName("Consumer")
    @Test
    public void test14() {
        // 参照物，方便知道下面的Lamdba表达式写法
        //定义一个消费者的实现
        Consumer<Person> greeter01 = new Consumer<Person>() {
            @Override
            public void accept(Person p) {
                System.out.println("Hello, " + p.firstName);
            }
        };
        greeter01.accept(new Person("张狗子", "Skywalker"));
        //Lamdba表达式写法
        Consumer<Person> greeter02 = (p) -> System.out.println("Hello, " + p.firstName);
        greeter02.accept(new Person("Luke", "Skywalker"));  //Hello, Luke
        //方法引用
        Consumer<Person> greeter03 = new MyConsumer<Person>()::accept;    // 也可以通过定义类和方法的方式去调用，这样才是实际开发的姿势
        greeter03.accept(new Person("李四", "Skywalker"));  //Hello, Luke
    }

    /**
     * 比较两个类
     */
    @DisplayName("Comparator")
    @Test
    public void test15() {
        //比较两个值，大于为1 ，等于为0  小于为-1
        Comparator<Person> comparator01 = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        //传入一个function参数，则会执行这个方法后再进行对比
        Comparator<Person> comparator02 = Comparator.comparing(p -> p.firstName);           //等同于上面的方式

        Person p1 = new Person("1", "Doe");
        Person p2 = new Person("2", "Wonderland");

        int compare = comparator01.compare(p1, p2);//  -1
        System.out.println(compare);
        int compare1 = comparator02.reversed().compare(p1, p2);//  1
        System.out.println(compare1);
    }

    @DisplayName("Optional")
    @Test
    public void test16() {
        Optional<String> optional = Optional.of("bam");

          //值不为null,等于true，值为空等于false
        System.out.println( optional.isPresent());// true
        //当有值的时候则获取值，无值的时候抛出异常
        System.out.println(optional.get());// "bam"
        //如果存在该值，返回值， 否则返回 other。
        optional.orElse("fallback");    // "bam"
        //如果值存在则使用该值调用 consumer , 否则不做任何事情。
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
        //如果有值，则对其执行调用映射函数得到返回值。如果返回值不为 null，则创建包含映射返回值的Optional作为map方法返回值，否则返回空Optional。
        Optional<String> s1 = optional.map((s) -> s + "11");

        Optional<Person> optionalPerson = Optional.of(new Person());
        optionalPerson.ifPresent(s -> System.out.println(s.firstName));
        //of 参数非空，ofNullable 参数可以为空
        Integer integer = Optional.ofNullable(2).map((a) -> (Integer) a * 1).orElse(1);
        System.out.println("result="+integer);
    }

    List<String> stringCollection = new ArrayList<>();

    @BeforeEach
    public  void init_list() {
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");
    }

    @Test
    @DisplayName("流过滤")
    public void test17() {
        stringCollection
                .stream()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);
    }

    @Test
    public void test18() {
        stringCollection
                .stream()
                .sorted()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("流排序后过滤")
    public void test19() {
        stringCollection
                .stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())  //等同于(a, b) -> b.compareTo(a)
                .forEach(System.out::println);
    }

    @Test
    public void test20() {
        // anyMatch：验证 list 中 string 是否有以 a 开头的, 匹配到第一个，即返回 true
        boolean anyStartsWithA =
                stringCollection
                        .stream()
                        .anyMatch((s) -> s.startsWith("a"));

        System.out.println(anyStartsWithA);      // true

        // allMatch：验证 list 中 string 是否都是以 a 开头的
        boolean allStartsWithA =
                stringCollection
                        .stream()
                        .allMatch((s) -> s.startsWith("a"));

        System.out.println(allStartsWithA);      // false

        // noneMatch：验证 list 中 string 是否都不是以 z 开头的
        boolean noneStartsWithZ =
                stringCollection
                        .stream()
                        .noneMatch((s) -> s.startsWith("z"));

        System.out.println(noneStartsWithZ);      // true
    }

    @Test
    public void test21() {
        // count：先对 list 中字符串开头为 b 进行过滤，让后统计数量
        long startsWithB =
                stringCollection
                        .stream()
                        .filter((s) -> s.startsWith("b"))
                        .count();

        System.out.println(startsWithB);    // 3
    }

    /**
     * reduce 聚合函数，把stream中的元素进行消费
     * 如果有两个参数，第一参数是初始值，第二个参数为计算函数 ，返回 初始值类型的结果
     * 如果只有一个参数，则传入的是计算函数，返回Optional对象，需要通过Optional对象判断值是否存在
     */
    @Test
    public void test22() {
        Optional<String> reduced =
                stringCollection
                        .stream()
                        .sorted()
                        .reduce((s1, s2) -> s1 + "#" + s2);

        reduced.ifPresent(System.out::println); //如果存在值，则进行消费，否则不进行任何处理
        // aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2
    }

    @Test
    public void test23() {
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // 纳秒
        long t0 = System.nanoTime();

        long count = values.stream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        // 纳秒转微秒
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("顺序流排序耗时: %d ms", millis));

        //顺序流排序耗时: 712 ms
    }

    @Test
    public void test24() {
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        long t0 = System.nanoTime();

        long count = values.parallelStream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis));
        //parallel sort took: 385 ms
    }

    @Test
    public void test25() {
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value , 否则 put, 再返回 老value
            System.out.println(map.putIfAbsent(i, "val" + i));
            System.out.println(map.get(i));
        }

        // forEach 可以很方便地对 map 进行遍历操作
        map.forEach((key, value) -> System.out.println(value));

    }

    @Test
    public void test26() {
        Map<Integer, BeanA> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value, 否则 put, 再返回 老value
            map.putIfAbsent(i, new BeanA(i, "明明" + i, i + 20, "89021839021830912809" + i));
        }

        Stream<BeanB> beanBStream00 = map.values().stream().map(new Function<BeanA, BeanB>() {
            @Override
            public BeanB apply(BeanA beanA) {
                return new BeanB(beanA.getName(), beanA.getAge());
            }
        });

        Stream<BeanB> beanBStream01 = map.values().stream().map(beanA -> new BeanB(beanA.getName(), beanA.getAge()));

        beanBStream01.forEach(System.out::println);
    }

    @Test
    public void test27() {
        // 我们还可以很方便地对某个 key 的值做相关操作：
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value, 否则 put, 再返回 老value
            map.putIfAbsent(i, "val" + i);
        }
        // computeIfPresent(), 当 key 存在时，才会做相关处理
        // 如下：对 key 为 3 的值，内部会先判断值是否存在，存在，则做 value + key 的拼接操作
        map.computeIfPresent(3, (num, val) -> val + num);
        map.get(3);             // val33

        // 先判断 key 为 9 的元素是否存在，存在，则做删除操作
        map.computeIfPresent(9, (num, val) -> null);
        map.containsKey(9);     // false

        // computeIfAbsent(), 当 key 不存在时，才会做相关处理
        // 如下：先判断 key 为 23 的元素是否存在，不存在，则添加
        map.computeIfAbsent(23, num -> "val" + num);
        map.containsKey(23);    // true

        // 先判断 key 为 3 的元素是否存在，存在，则不做任何处理
        map.computeIfAbsent(3, num -> "bam");
        map.get(3);             // val33
    }

    @Test
    public void test28() {
        // 我们还可以很方便地对某个 key 的值做相关操作：
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value, 否则 put, 再返回 老value
            map.putIfAbsent(i, "val" + i);
        }
        //关于删除操作，JDK 8 中提供了能够新的 remove() API:
        map.remove(3, "val3"); //判断值key -value 是否匹配，匹配则删除
        map.get(3);             // val33

        map.remove(3, "val33");
        map.get(3);             // null
    }

    @Test
    public void test29() {
        // 我们还可以很方便地对某个 key 的值做相关操作：
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value, 否则 put, 再返回 老value
            map.putIfAbsent(i, "val" + i);
        }
        //关于添加方法，JDK 8 中提供了带有默认值的 getOrDefault() 方法：

        // 若 key 42 不存在，则返回 not found
        map.getOrDefault(42, "not found");  // not found
    }


    @Test
    public void test30() {
        // 我们还可以很方便地对某个 key 的值做相关操作：
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            // 会判断 key 是否已经存在，存在则直接返回 value, 否则 put, 再返回 老value
            map.putIfAbsent(i, "val" + i);
        }
        //对于 value 的合并操作也变得更加简单

        // merge 方法，会先判断进行合并的 key 是否存在，不存在，则会添加元素
        map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
        map.get(9);             // val9

        // 若 key 的元素存在，则对 value 执行拼接操作
        map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
        map.get(9);             // val9concat
    }

    @Test
    public void test31() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();

        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);   // 老版本 java.util.Date
    }

    @Test
    public void test32() {
        System.out.println(ZoneId.getAvailableZoneIds());
        // prints all available timezone ids

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        //[Asia/Aden, America/Cuiaba, Etc/GMT+9, Etc/GMT+8, Africa/Nairobi, America/Marigot, Asia/Aqtau, Pacific/Kwajalein, America/El_Salvador, Asia/Pontianak, Africa/Cairo, Pacific/Pago_Pago, Africa/Mbabane, Asia/Kuching, Pacific/Honolulu, Pacific/Rarotonga, America/Guatemala, Australia/Hobart, Europe/London, America/Belize, America/Panama, Asia/Chungking, America/Managua, America/Indiana/Petersburg, Asia/Yerevan, Europe/Brussels, GMT, Europe/Warsaw, America/Chicago, Asia/Kashgar, Chile/Continental, Pacific/Yap, CET, Etc/GMT-1, Etc/GMT-0, Europe/Jersey, America/Tegucigalpa, Etc/GMT-5, Europe/Istanbul, America/Eirunepe, Etc/GMT-4, America/Miquelon, Etc/GMT-3, Europe/Luxembourg, Etc/GMT-2, Etc/GMT-9, America/Argentina/Catamarca, Etc/GMT-8, Etc/GMT-7, Etc/GMT-6, Europe/Zaporozhye, Canada/Yukon, Canada/Atlantic, Atlantic/St_Helena, Australia/Tasmania, Libya, Europe/Guernsey, America/Grand_Turk, US/Pacific-New, Asia/Samarkand, America/Argentina/Cordoba, Asia/Phnom_Penh, Africa/Kigali, Asia/Almaty, US/Alaska, Asia/Dubai, Europe/Isle_of_Man, America/Araguaina, Cuba, Asia/Novosibirsk, America/Argentina/Salta, Etc/GMT+3, Africa/Tunis, Etc/GMT+2, Etc/GMT+1, Pacific/Fakaofo, Africa/Tripoli, Etc/GMT+0, Israel, Africa/Banjul, Etc/GMT+7, Indian/Comoro, Etc/GMT+6, Etc/GMT+5, Etc/GMT+4, Pacific/Port_Moresby, US/Arizona, Antarctica/Syowa, Indian/Reunion, Pacific/Palau, Europe/Kaliningrad, America/Montevideo, Africa/Windhoek, Asia/Karachi, Africa/Mogadishu, Australia/Perth, Brazil/East, Etc/GMT, Asia/Chita, Pacific/Easter, Antarctica/Davis, Antarctica/McMurdo, Asia/Macao, America/Manaus, Africa/Freetown, Europe/Bucharest, America/Argentina/Mendoza, Asia/Macau, Europe/Malta, Mexico/BajaSur, Pacific/Tahiti, Africa/Asmera, Europe/Busingen, America/Argentina/Rio_Gallegos, Africa/Malabo, Europe/Skopje, America/Catamarca, America/Godthab, Europe/Sarajevo, Australia/ACT, GB-Eire, Africa/Lagos, America/Cordoba, Europe/Rome, Asia/Dacca, Indian/Mauritius, Pacific/Samoa, America/Regina, America/Fort_Wayne, America/Dawson_Creek, Africa/Algiers, Europe/Mariehamn, America/St_Johns, America/St_Thomas, Europe/Zurich, America/Anguilla, Asia/Dili, America/Denver, Africa/Bamako, GB, Mexico/General, Pacific/Wallis, Europe/Gibraltar, Africa/Conakry, Africa/Lubumbashi, Asia/Istanbul, America/Havana, NZ-CHAT, Asia/Choibalsan, America/Porto_Acre, Asia/Omsk, Europe/Vaduz, US/Michigan, Asia/Dhaka, America/Barbados, Europe/Tiraspol, Atlantic/Cape_Verde, Asia/Yekaterinburg, America/Louisville, Pacific/Johnston, Pacific/Chatham, Europe/Ljubljana, America/Sao_Paulo, Asia/Jayapura, America/Curacao, Asia/Dushanbe, America/Guyana, America/Guayaquil, America/Martinique, Portugal, Europe/Berlin, Europe/Moscow, Europe/Chisinau, America/Puerto_Rico, America/Rankin_Inlet, Pacific/Ponape, Europe/Stockholm, Europe/Budapest, America/Argentina/Jujuy, Australia/Eucla, Asia/Shanghai, Universal, Europe/Zagreb, America/Port_of_Spain, Europe/Helsinki, Asia/Beirut, Asia/Tel_Aviv, Pacific/Bougainville, US/Central, Africa/Sao_Tome, Indian/Chagos, America/Cayenne, Asia/Yakutsk, Pacific/Galapagos, Australia/North, Europe/Paris, Africa/Ndjamena, Pacific/Fiji, America/Rainy_River, Indian/Maldives, Australia/Yancowinna, SystemV/AST4, Asia/Oral, America/Yellowknife, Pacific/Enderbury, America/Juneau, Australia/Victoria, America/Indiana/Vevay, Asia/Tashkent, Asia/Jakarta, Africa/Ceuta, America/Recife, America/Buenos_Aires, America/Noronha, America/Swift_Current, Australia/Adelaide, America/Metlakatla, Africa/Djibouti, America/Paramaribo, Europe/Simferopol, Europe/Sofia, Africa/Nouakchott, Europe/Prague, America/Indiana/Vincennes, Antarctica/Mawson, America/Kralendijk, Antarctica/Troll, Europe/Samara, Indian/Christmas, America/Antigua, Pacific/Gambier, America/Indianapolis, America/Inuvik, America/Iqaluit, Pacific/Funafuti, UTC, Antarctica/Macquarie, Canada/Pacific, America/Moncton, Africa/Gaborone, Pacific/Chuuk, Asia/Pyongyang, America/St_Vincent, Asia/Gaza, Etc/Universal, PST8PDT, Atlantic/Faeroe, Asia/Qyzylorda, Canada/Newfoundland, America/Kentucky/Louisville, America/Yakutat, Asia/Ho_Chi_Minh, Antarctica/Casey, Europe/Copenhagen, Africa/Asmara, Atlantic/Azores, Europe/Vienna, ROK, Pacific/Pitcairn, America/Mazatlan, Australia/Queensland, Pacific/Nauru, Europe/Tirane, Asia/Kolkata, SystemV/MST7, Australia/Canberra, MET, Australia/Broken_Hill, Europe/Riga, America/Dominica, Africa/Abidjan, America/Mendoza, America/Santarem, Kwajalein, America/Asuncion, Asia/Ulan_Bator, NZ, America/Boise, Australia/Currie, EST5EDT, Pacific/Guam, Pacific/Wake, Atlantic/Bermuda, America/Costa_Rica, America/Dawson, Asia/Chongqing, Eire, Europe/Amsterdam, America/Indiana/Knox, America/North_Dakota/Beulah, Africa/Accra, Atlantic/Faroe, Mexico/BajaNorte, America/Maceio, Etc/UCT, Pacific/Apia, GMT0, America/Atka, Pacific/Niue, Canada/East-Saskatchewan, Australia/Lord_Howe, Europe/Dublin, Pacific/Truk, MST7MDT, America/Monterrey, America/Nassau, America/Jamaica, Asia/Bishkek, America/Atikokan, Atlantic/Stanley, Australia/NSW, US/Hawaii, SystemV/CST6, Indian/Mahe, Asia/Aqtobe, America/Sitka, Asia/Vladivostok, Africa/Libreville, Africa/Maputo, Zulu, America/Kentucky/Monticello, Africa/El_Aaiun, Africa/Ouagadougou, America/Coral_Harbour, Pacific/Marquesas, Brazil/West, America/Aruba, America/North_Dakota/Center, America/Cayman, Asia/Ulaanbaatar, Asia/Baghdad, Europe/San_Marino, America/Indiana/Tell_City, America/Tijuana, Pacific/Saipan, SystemV/YST9, Africa/Douala, America/Chihuahua, America/Ojinaga, Asia/Hovd, America/Anchorage, Chile/EasterIsland, America/Halifax, Antarctica/Rothera, America/Indiana/Indianapolis, US/Mountain, Asia/Damascus, America/Argentina/San_Luis, America/Santiago, Asia/Baku, America/Argentina/Ushuaia, Atlantic/Reykjavik, Africa/Brazzaville, Africa/Porto-Novo, America/La_Paz, Antarctica/DumontDUrville, Asia/Taipei, Antarctica/South_Pole, Asia/Manila, Asia/Bangkok, Africa/Dar_es_Salaam, Poland, Atlantic/Madeira, Antarctica/Palmer, America/Thunder_Bay, Africa/Addis_Ababa, Europe/Uzhgorod, Brazil/DeNoronha, Asia/Ashkhabad, Etc/Zulu, America/Indiana/Marengo, America/Creston, America/Mexico_City, Antarctica/Vostok, Asia/Jerusalem, Europe/Andorra, US/Samoa, PRC, Asia/Vientiane, Pacific/Kiritimati, America/Matamoros, America/Blanc-Sablon, Asia/Riyadh, Iceland, Pacific/Pohnpei, Asia/Ujung_Pandang, Atlantic/South_Georgia, Europe/Lisbon, Asia/Harbin, Europe/Oslo, Asia/Novokuznetsk, CST6CDT, Atlantic/Canary, America/Knox_IN, Asia/Kuwait, SystemV/HST10, Pacific/Efate, Africa/Lome, America/Bogota, America/Menominee, America/Adak, Pacific/Norfolk, America/Resolute, Pacific/Tarawa, Africa/Kampala, Asia/Krasnoyarsk, Greenwich, SystemV/EST5, America/Edmonton, Europe/Podgorica, Australia/South, Canada/Central, Africa/Bujumbura, America/Santo_Domingo, US/Eastern, Europe/Minsk, Pacific/Auckland, Africa/Casablanca, America/Glace_Bay, Canada/Eastern, Asia/Qatar, Europe/Kiev, Singapore, Asia/Magadan, SystemV/PST8, America/Port-au-Prince, Europe/Belfast, America/St_Barthelemy, Asia/Ashgabat, Africa/Luanda, America/Nipigon, Atlantic/Jan_Mayen, Brazil/Acre, Asia/Muscat, Asia/Bahrain, Europe/Vilnius, America/Fortaleza, Etc/GMT0, US/East-Indiana, America/Hermosillo, America/Cancun, Africa/Maseru, Pacific/Kosrae, Africa/Kinshasa, Asia/Kathmandu, Asia/Seoul, Australia/Sydney, America/Lima, Australia/LHI, America/St_Lucia, Europe/Madrid, America/Bahia_Banderas, America/Montserrat, Asia/Brunei, America/Santa_Isabel, Canada/Mountain, America/Cambridge_Bay, Asia/Colombo, Australia/West, Indian/Antananarivo, Australia/Brisbane, Indian/Mayotte, US/Indiana-Starke, Asia/Urumqi, US/Aleutian, Europe/Volgograd, America/Lower_Princes, America/Vancouver, Africa/Blantyre, America/Rio_Branco, America/Danmarkshavn, America/Detroit, America/Thule, Africa/Lusaka, Asia/Hong_Kong, Iran, America/Argentina/La_Rioja, Africa/Dakar, SystemV/CST6CDT, America/Tortola, America/Porto_Velho, Asia/Sakhalin, Etc/GMT+10, America/Scoresbysund, Asia/Kamchatka, Asia/Thimbu, Africa/Harare, Etc/GMT+12, Etc/GMT+11, Navajo, America/Nome, Europe/Tallinn, Turkey, Africa/Khartoum, Africa/Johannesburg, Africa/Bangui, Europe/Belgrade, Jamaica, Africa/Bissau, Asia/Tehran, WET, Africa/Juba, America/Campo_Grande, America/Belem, Etc/Greenwich, Asia/Saigon, America/Ensenada, Pacific/Midway, America/Jujuy, Africa/Timbuktu, America/Bahia, America/Goose_Bay, America/Virgin, America/Pangnirtung, Asia/Katmandu, America/Phoenix, Africa/Niamey, America/Whitehorse, Pacific/Noumea, Asia/Tbilisi, America/Montreal, Asia/Makassar, America/Argentina/San_Juan, Hongkong, UCT, Asia/Nicosia, America/Indiana/Winamac, SystemV/MST7MDT, America/Argentina/ComodRivadavia, America/Boa_Vista, America/Grenada, Australia/Darwin, Asia/Khandyga, Asia/Kuala_Lumpur, Asia/Thimphu, Asia/Rangoon, Europe/Bratislava, Asia/Calcutta, America/Argentina/Tucuman, Asia/Kabul, Indian/Cocos, Japan, Pacific/Tongatapu, America/New_York, Etc/GMT-12, Etc/GMT-11, Etc/GMT-10, SystemV/YST9YDT, Etc/GMT-14, Etc/GMT-13, W-SU, America/Merida, EET, America/Rosario, Canada/Saskatchewan, America/St_Kitts, Arctic/Longyearbyen, America/Caracas, America/Guadeloupe, Asia/Hebron, Indian/Kerguelen, SystemV/PST8PDT, Africa/Monrovia, Asia/Ust-Nera, Egypt, Asia/Srednekolymsk, America/North_Dakota/New_Salem, Asia/Anadyr, Australia/Melbourne, Asia/Irkutsk, America/Shiprock, America/Winnipeg, Europe/Vatican, Asia/Amman, Etc/UTC, SystemV/AST4ADT, Asia/Tokyo, America/Toronto, Asia/Singapore, Australia/Lindeman, America/Los_Angeles, SystemV/EST5EDT, Pacific/Majuro, America/Argentina/Buenos_Aires, Europe/Nicosia, Pacific/Guadalcanal, Europe/Athens, US/Pacific, Europe/Monaco]
        // ZoneRules[currentStandardOffset=+01:00]
        // ZoneRules[currentStandardOffset=-03:00]
    }

    @Test
    public void test33() {

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);

        System.out.println(now1.isBefore(now2));  // false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        System.out.println(hoursBetween);       // -3
        System.out.println(minutesBetween);     // -239
    }

    @Test
    public void test34() {
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);       // 23:59:59

        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMAN);

        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println(leetTime);   // 13:37
    }

    @Test
    public void test35() {
        LocalDate today = LocalDate.now();
        // 今天加一天
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        // 明天减两天
        LocalDate yesterday = tomorrow.minusDays(2);

        // 2014 年七月的第四天
        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println(dayOfWeek);    // 星期五
    }

    @Test
    public void test36() {
        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
        System.out.println(xmas);   // 2014-12-24
    }

    @Test
    public void test37() {
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

        DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
        System.out.println(dayOfWeek);      // 星期三

        Month month = sylvester.getMonth();
        System.out.println(month);          // 十二月

        // 获取改时间是该天中的第几分钟
        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteOfDay);    // 1439
    }

    @Test
    public void test38() {
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
        Instant instant = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
    }

    @Test
    public void test39() {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("MMM dd, yyyy - HH:mm");

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
        String string = formatter.format(parsed);
        System.out.println(string);     // Nov 03, 2014 - 07:13
    }

    @Test
    public void test40() {
        @Hints({@Hint("hint1"), @Hint("hint2")})
        class Person {
        }
    }

    @Test
    public void test41() {
        @Hint("hint1")
        @Hint("hint2")
        class Person {
        }

        Hint hint = Person.class.getAnnotation(Hint.class);
        System.out.println(hint);                   // null

        Hints hints1 = Person.class.getAnnotation(Hints.class);
        System.out.println(hints1.value().length);  // 2

        Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
        System.out.println(hints2.length);          // 2
    }

}


