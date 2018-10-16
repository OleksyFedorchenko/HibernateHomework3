package com.company;


import com.company.Entity.City;
import com.company.Entity.Country;
import com.company.Entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class App {
    static EntityManagerFactory factory;
    static EntityManager em;
    static Country country;
    static City city;
    static Person person;

    public static void main(String[] args) throws IOException {
        factory = Persistence.createEntityManagerFactory("jdbctoorm");
        em = factory.createEntityManager();
        em.getTransaction().begin();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        do {
            System.out.println("1-Add country \n"
                    + "2-Add city\n"
                    + "3-Add person\n"
                    + "4-Show list of person\n"
                    + "5-Show list of cities\n"
                    + "6-Show list of countries\n"
                    + "7-Show person information by id\n"
                    + "8-Show city information by id\n"
                    + "9-Show country information by id\n"
                    + "10-Show info about people from one city\n"
                    + "11-Show info about cities which are from one country\n"
                    + "12-Find person\n"
                    + "13-Random adding tables\n"
                    + "14-Exit");


            input = br.readLine();
            switch (input) {
                case "1":
                    System.out.println("Input country name:");
                    String cn = br.readLine();
                    insertCountry(cn);
                    break;
                case "2":
                    System.out.println("Input city name:");
                    String citn = br.readLine();
                    insertCity(citn);
                    break;
                case "3":
                    System.out.println("Input first name:");
                    String fn = br.readLine();
                    System.out.println("Input last name:");
                    String ln = br.readLine();
                    System.out.println("Input your age:");
                    int a = Integer.parseInt(br.readLine());
                    insertPerson(fn, ln, a);
                    break;
                case "4":
                    selectPerson("ORDER BY first_name");
                    break;
                case "5":
                    selectCity("ORDER BY name DESC");
                    break;
                case "6":
                    selectCountry("ORDER BY id");
                    break;
                case "7":
                    System.out.println("Input id of person:");
                    int id = Integer.parseInt(br.readLine());
                    selectPerson("WHERE id =" + id);
                    break;
                case "8":
                    System.out.println("Input id of city:");
                    id = Integer.parseInt(br.readLine());
                    selectCity("WHERE id =" + id);
                    break;
                case "9":
                    System.out.println("Input id of country:");
                    id = Integer.parseInt(br.readLine());
                    selectCountry("WHERE id =" + id);
                    break;
                case "10":
                    System.out.println("Input name of city to see all from this place");
                    String in = br.readLine();
                    selectPersonFromOneCity(in);
                    break;
                case "11":
                    System.out.println("Input name of country to see all cities from it");
                    String inc = br.readLine();
                    cityOfCountry(inc);
                    break;
                case "12":
                    System.out.println("Input some letters of person name:");
                    String match = br.readLine();
                    selectPerson("WHERE p.first_name LIKE '%" + match + "%'");
                    break;
                case "13":
                    fileReadRandom();
                    selectCountry("ORDER BY id");
                    selectCity("ORDER BY id");
                    selectPerson("ORDER BY id");
                    break;
            }
        } while (!input.equals("14"));

        em.getTransaction().commit();
        em.close();
        factory.close();
    }

    public static void insertCountry(String cn) {
        country = Country.builder()
                .name(cn)
                .build();
        em.persist(country);
    }

    public static void insertCity(String citn) {
        city = City.builder()
                .name(citn)
                .country(country)
                .build();
        em.persist(city);
    }

    public static void insertPerson(String fn, String ln, int a) {
        person = Person.builder()
                .first_name(fn)
                .last_name(ln)
                .age(a)
                .city(city)
                .build();
        em.persist(person);
    }

    public static void selectPerson(String order) {
        List<Person> personList = em.createQuery("SELECT p FROM Person p " + order, Person.class).getResultList();
        personList.forEach(System.out::println);
    }

    public static void selectCity(String order) {
        List<City> cityList = em.createQuery("SELECT ct FROM City ct " + order, City.class).getResultList();
        cityList.forEach(System.out::println);
    }

    public static void selectCountry(String order) {
        List<Country> countryList = em.createQuery("SELECT c FROM Country c " + order, Country.class).getResultList();
        countryList.forEach(System.out::println);
    }

    public static void selectPersonFromOneCity(String cn) {
        em.createQuery
                ("SELECT p FROM Person p JOIN p.city ct WHERE ct.name=:cityname", Person.class)
                .setParameter("cityname", cn)
                .getResultList()
                .forEach(System.out::println);
    }

    public static void cityOfCountry(String in) {
        em.createQuery
                ("SELECT ct FROM City ct JOIN ct.country c WHERE c.name=:countryname", City.class)
                .setParameter("countryname", in)
                .getResultList()
                .forEach(System.out::println);
    }

    public static void fileReadRandom() throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("countries.txt"));
        for (String line : lines) {
            insertCountry(line);
        }

        Random rand = new Random();
        List<Country> countryList = em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
        lines = Files.readAllLines(Paths.get("cityes.txt"));
        for (String line : lines) {
            int cn = 1 + rand.nextInt(countryList.size() - 1);
            city = City.builder()
                    .name(line)
                    .country(countryList.get(cn))
                    .build();
            em.persist(city);
        }


        FileReader fr = new FileReader("names.txt");
        BufferedReader br = new BufferedReader(fr);

        String line;
        String strs[];
        List<City> cityList = em.createQuery("SELECT ct FROM City ct", City.class).getResultList();
        while ((line = br.readLine()) != null) {
            strs = line.split(" ");
            for (int i = 0; i < strs.length; i += 2) {
                int cnt = 1 + rand.nextInt(cityList.size() - 1);
                person = Person.builder()
                        .first_name(strs[i])
                        .last_name(strs[i + 1])
                        .age(1 + rand.nextInt(89))
                        .city(cityList.get(cnt))
                        .build();
                em.persist(person);

            }
        }
        br.close();
        fr.close();
    }
}


