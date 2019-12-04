package ru.Test.Bookstore.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Main {

    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Employee> employees = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
	// write your code here
        initData();

        //
        String booksInfo = String.format("Общее количество проданных книг %d на сумму %f", getCountOfSoulBooks(), getAllPriceOfSoldBooks());
        System.out.println(booksInfo);

        //
        for (Employee employee : employees){
            System.out.println(employee.getName() + " продал(а) " +getProfitByEmployee(employee.getId()).toString());
        }


        //
        ArrayList<BookAdditional> soldBooksCount = getCountOfSoldBooksByGenre();
        HashMap<BookGenge, Double> soldBookPrices = getPriceOFSoldBooksByGenre();

        String soldBookStr = "По жанру: %s продано %d книг общей стоимостью %f";

        for (BookAdditional bookAdditional : soldBooksCount){
            double price = soldBookPrices.get(bookAdditional.getGenge());
            System.out.println(String.format(
                                String.format(
                                        soldBookStr,
                                        bookAdditional.getGenge().name(),bookAdditional.getCount(),price
                                )
            ));
        }

        //
        int age = 30;
        String analyseGenreStr = "Покупатели до %d лет выбирают жанр %s";
        System.out.println(String.format(analyseGenreStr,30, getMostPopularGenreLessThenAge (30)));


        String analyseGenreStr2 = "Покупатели старше %d лет выбирают жанр %s";
        System.out.println(String.format(analyseGenreStr2,30, getMostPopularGenreLessThenAge (30)));




    }
        // получение наиболее популярного жанра
    public static BookGenge getMostPopularGenreLessThenAge (int age){
        ArrayList<Long> custimersIds = new ArrayList<>();

        for ( Customer customer : customers){
            if (customer.getAge() < age){
                custimersIds.add(customer.getId());
            }
        }
        return getMostPopularBookGenre(custimersIds);

    }

    public static BookGenge getMostPopularGenreMoreThenAge (int age){
        ArrayList<Long> custimersIds = new ArrayList<>();

        for ( Customer customer : customers){
            if (customer.getAge() > age){
                custimersIds.add(customer.getId());
            }
        }
        return getMostPopularBookGenre(custimersIds);

    }



    private static BookGenge getMostPopularBookGenre(ArrayList<Long> custimersIds) {
        int countArt = 0, countPr = 0, countPs = 0;

        for (Order order: orders){
            if (custimersIds.contains(order.getCustomerId())){
                countArt +=getCountOfSoldBooksByGanre(order, BookGenge.Art);
                countPr +=getCountOfSoldBooksByGanre(order, BookGenge.Programming);
                countPs +=getCountOfSoldBooksByGanre(order, BookGenge.Psyhology);
            }
        }
        ArrayList<BookAdditional> result = new ArrayList<>();
        result.add(new BookAdditional(BookGenge.Art, countArt));
        result.add(new BookAdditional(BookGenge.Programming, countPr));
        result.add(new BookAdditional(BookGenge.Psyhology, countPs));

        result.sort(new Comparator<BookAdditional>() {
            @Override
            public int compare(BookAdditional left, BookAdditional right) {
                return right.getCount() - left.getCount();
            }
        });

        return result.get(0).getGenge();
    }


    // получение количества книг по жанрам
    public static ArrayList<BookAdditional> getCountOfSoldBooksByGenre(){
        ArrayList<BookAdditional> result = new ArrayList<>();
        int countArt = 0, countPr =0, countPs=0;
        for (Order order : orders){
            countArt += getCountOfSoldBooksByGanre(order, BookGenge.Art);
            countPr += getCountOfSoldBooksByGanre(order, BookGenge.Programming);
            countPs += getCountOfSoldBooksByGanre(order, BookGenge.Psyhology);
        }
        result.add(new BookAdditional(BookGenge.Art, countArt));
        result.add(new BookAdditional(BookGenge.Programming, countPr));
        result.add(new BookAdditional(BookGenge.Psyhology, countPs));

        return result;
    }

    // получить стоимость проданных книг по жанрам
    public static HashMap<BookGenge,Double> getPriceOFSoldBooksByGenre(){
        HashMap<BookGenge,Double> result = new HashMap<>();
        double priceArt=0, pricePr=0 , pricePs=0;
        for (Order order : orders){
            priceArt += getPriceOfSoldBooksByGanre(order, BookGenge.Art);
            pricePr += getPriceOfSoldBooksByGanre(order, BookGenge.Programming);
            pricePs += getPriceOfSoldBooksByGanre(order, BookGenge.Psyhology);
        }
        result.put(BookGenge.Art, priceArt);
        result.put(BookGenge.Programming, pricePr);
        result.put(BookGenge.Psyhology, pricePs);
        return result;
    }

         /*
        @param номер
        количество книг по одному жанру
        */
        public static int getCountOfSoldBooksByGanre (Order order, BookGenge genge){
            int count = 0;
            for (long bookId : order.getBooks()){
                Book book = getBookById(bookId);
                if ( book!= null && book.getGenre() == genge)
                    count ++;
            }
            return count;
        }



    /*
    @param номер
    стоимость книг по одному жанру
     */
    public static double getPriceOfSoldBooksByGanre (Order order, BookGenge genge){
        double price = 0;
        for (long bookId : order.getBooks()){
            Book book = getBookById(bookId);
            if ( book!= null && book.getGenre() == genge)
                price += book.getPrice();
        }
        return price;
    }



    // количество и стоимость проданных одним продавцом
    public static Profit getProfitByEmployee (long employeeId){
        int count = 0; double price = 0;
        for (Order order : orders){
            if (order.getEmployeeId() == employeeId){
                price += getPriceOfSoldBooksInOrder(order);
                count += order.getBooks().length;

            }
        }
        return new Profit (count, price) ;
    }


    // получение общей суммы проданных книг
    public static double getAllPriceOfSoldBooks(){
        double price = 0;
        for (Order order : orders){
            price+=getPriceOfSoldBooksInOrder(order);
        }
        return price;
            }

    // сумма книг в одном заказе
    public static double getPriceOfSoldBooksInOrder (Order order) {
        double price = 0;
        for (long bookId : order.getBooks()){
            Book book = getBookById(bookId);
            if ( book!= null)
            price += book.getPrice();
        }
        return price;

    }


    // получение общего количества проданных книг
    public static int getCountOfSoulBooks(){
        int count=0;
        for (Order order : orders){
            count+=order.getBooks().length;
            }
        return count;
    }

    // получение книги но id
    public static Book getBookById(long id){
        Book current = null;
        for (Book book : books){
            if (book.getId()==id){
                current = book;
                break;
            }
        }

            return current;
    }


    public static void initData() {
        //Продавцы
        employees.add(new Employee(1,"Иванов", 32));
        employees.add(new Employee(2,"Петров", 30));
        employees.add(new Employee(3,"Сидорова", 26));

        //Покупатели
        customers.add(new Customer(1, "Сидоров", 25));
        customers.add(new Customer(2, "Романов", 25));
        customers.add(new Customer(3, "Симонов", 25));
        customers.add(new Customer(4, "Кириенко", 25));
        customers.add(new Customer(5, "Попова", 25));

        //Книги
        books.add(new Book(1, "Война и Мир", "Лев Толстой", 1600, BookGenge.Art));
        books.add(new Book(2, "Преступление и наказание", "Федор Достоевский", 600, BookGenge.Art));
        books.add(new Book(3, "Война или любовь", "Лея Бриджер", 2600, BookGenge.Art));
        books.add(new Book(4, "Мертвые души", "Николай Гоголь", 500, BookGenge.Art));

        books.add(new Book(5, "Введение в психоанализ", "Зигмунд Фрейд", 1050, BookGenge.Psyhology));
        books.add(new Book(6, "Психология влияния", "Роберт Чалдини", 550, BookGenge.Psyhology));
        books.add(new Book(7, "Как перестать беспокоиться и начать жить", "Дейл Карнеги", 1000, BookGenge.Psyhology));

        books.add(new Book(8, "Gang of Four", "Гамма Эрих", 900, BookGenge.Programming));
        books.add(new Book(9, "Совершенный код", "Стив Макконел", 1200, BookGenge.Programming));
        books.add(new Book(10, "Рефакторинг", "Мартин Фаулер", 850, BookGenge.Programming));
        books.add(new Book(11, "Алгоритмы", "Кармен Томас Х.", 450, BookGenge.Programming));

        //
        orders.add(new Order(1,1,1,new long[]{8,9,10,11}));
        orders.add(new Order(2,1,2,new long[]{1}));
        orders.add(new Order(3,2,3,new long[]{5,6,7}));
        orders.add(new Order(4,2,4,new long[]{1,2,3,4}));
        orders.add(new Order(5,3,5,new long[]{2,5,9}));


    }
}
