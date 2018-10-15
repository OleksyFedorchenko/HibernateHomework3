package com.company;


import com.company.Entity.City;
import com.company.Entity.Country;
import com.company.Entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class App
{
    static EntityManagerFactory factory;
    static EntityManager em;
    static Country country;
    static City city;
    static Person person;
    public static void main( String[] args ) throws IOException {
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
//                case "11":
//                    System.out.println("Input name of country to see all cities from it");
//                    String inc = br.readLine();
//                    int idctc = idOfCountry("WHERE name = '" + inc + "'");
//                    cityOfCountry(idctc);
//                    break;
//                case "12":
//                    System.out.println("Input some letters of person name:");
//                    String match = br.readLine();
//                    selectPerson("WHERE first_name LIKE '%" + match + "%'");
//                    break;
//                case "13":
//                    fileReadRandom();
//                    selectCountry("ORDER BY id");
//                    selectCity("ORDER BY id");
//                    selectPerson("ORDER BY id");
//                    break;
            }
        } while (!input.equals("14"));

        em.getTransaction().commit();
        em.close();
        factory.close();
    }
    public static void insertCountry(String cn){
        country=Country.builder()
                .name(cn)
                .build();
        em.persist(country);
    }
    public static void insertCity(String citn){
        city=City.builder()
                .name(citn)
                .country(country)
                .build();
        em.persist(city);
    }
    public static void insertPerson(String fn,String ln,int a){
        person=Person.builder()
                .first_name(fn)
                .last_name(ln)
                .age(a)
                .city(city)
                .build();
        em.persist(person);
    }
    public static void selectPerson(String order){
        List<Person> personList=em.createQuery("SELECT p FROM Person p "+order,Person.class).getResultList();
        personList.forEach(System.out::println);
    }
    public static void selectCity(String order){
        List<City> cityList=em.createQuery("SELECT ct FROM City ct "+order,City.class).getResultList();
        cityList.forEach(System.out::println);
    }
    public static void selectCountry(String order){
        List<Country> countryList=em.createQuery("SELECT c FROM Country c "+order,Country.class).getResultList();
        countryList.forEach(System.out::println);
    }
    public static void selectPersonFromOneCity(String cn){
    em.createQuery("SELECT p FROM Person p JOIN City ct ON (ct.name=:cityname) AND (p.city.id=ct.id)",Person.class).setParameter("cityname",cn).getResultList().forEach(System.out::println);
    }
}


