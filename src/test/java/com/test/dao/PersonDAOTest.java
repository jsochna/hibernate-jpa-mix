package com.test.dao;

import com.test.domain.Person;
import com.test.domain.util.GENDER;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public class PersonDAOTest {

    @Inject
    private PersonDAO personDAO;
    private static boolean dbInitialized = false;

    @Before
    public void testSetUp() {

        if (!dbInitialized) {
            Person person = new Person();
            person.setLastName("Last");
            person.setFirstName("First");
            person.setGender(GENDER.MALE);
            person.setDateOfBirth(new Date());

            personDAO.storePerson(person);

            person = new Person();
            person.setLastName("Last");
            person.setFirstName("Peter");
            person.setGender(GENDER.MALE);
            person.setDateOfBirth(new Date());

            personDAO.storePerson(person);

            person = new Person();
            person.setLastName("Last");
            person.setFirstName("Peter");
            person.setGender(GENDER.MALE);
            person.setDateOfBirth(new Date());

            personDAO.storePerson(person);

            person = new Person();
            person.setLastName("Last");
            person.setFirstName("First");
            person.setGender(GENDER.MALE);
            person.setDateOfBirth(new Date());

            personDAO.storePerson(person);

            dbInitialized = true;
        }

    }

    @Test
    public void testReadPersonByFirstNameUsingNativeQuery() {
        List<Person> personList = personDAO.readPersonByFirstNameUsingNativeQuery("Peter");

        assertCorrectNumberOfPeter(personList);

    }

    @Test
    public void testReadPersonByFirstNameUsingHQL() {
        List<Person> personList = personDAO.readPersonByFirstNameUsingHQL("Peter");

        assertCorrectNumberOfPeter(personList);

    }

    @Test
    public void testReadPersonByPositionalAndNamedArgumentJPA() {
        List<Person> personList = personDAO.readPersonPositionalAndNamedArgumentJPA("First", "Last");
        assertThat(personList.size(), CoreMatchers.is(2));
    }

    @Test
    public void testReadPersonByPositionalAndNamedArgumentHibernate() {
        List<Person> personList = personDAO.readPersonPositionalAndNamedArgumentHibernate("First", "Last");
        assertThat(personList.size(), CoreMatchers.is(2));
    }

    @Test
    public void testReadPersonByFirstNameUsingCriteria() {
        List<Person> personList = personDAO.readPersonByFirstNameUsingCriteria("Peter");

        assertCorrectNumberOfPeter(personList);

    }

    @Test
    public void testReadPersonById() {
        Person person = personDAO.readPersonById(1);
        assertNotNull(person);
    }

    private void assertCorrectNumberOfPeter(List<Person> personList) {
        int peterCount = 0;
        for (Person person : personList) {
            if (person.getFirstName().equals("Peter")) {
                peterCount++;
            }
        }

        assertEquals("Number of Peters should be 2", 2, peterCount);
    }

}