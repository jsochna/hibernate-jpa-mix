package com.test.dao;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.domain.Person;
import com.test.domain.Person_;
import com.test.domain.QPerson;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PersonDAO {

    @PersistenceContext(name = "test")
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional(readOnly = true)
    public List<Person> readPersonByFirstNameUsingNativeQuery(String firstName) {

        Query q = entityManager.createNativeQuery("SELECT * FROM person WHERE firstname = :firstname", Person.class);
        q.setParameter("firstname", firstName);

        List<Person> result = q.getResultList();
        return result;
    }

    public List<Person> readPersonByFirstNameUsingHQL(String firstName) {

        Query q = entityManager.createQuery("from Person p where p.firstName = :firstName", Person.class);
        q.setParameter("firstName", firstName);

        List<Person> result = q.getResultList();
        return result;

    }

    public List<Person> readPersonByFirstNameUsingCriteria(String firstName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        cq.where(cb.equal(root.get(Person_.firstName), firstName));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void storePerson(Person person) {
        entityManager.persist(person);
    }

    public Person readPersonById(int i) {

        return entityManager.find(Person.class, i);

    }

    @Transactional(readOnly = true)
    public List<Person> readPersonPositionalAndNamedArgumentJPA(String first, String last) {
        QPerson person = QPerson.person;
        JPQLQuery<Person> query = new JPAQueryFactory(entityManager)
                .selectFrom(person)
                .where(person.firstName.eq(first)
                        .and(person.lastName.eq(last)));
        return query.fetch();
    }

    @Transactional(readOnly = true)
    public List<Person> readPersonPositionalAndNamedArgumentHibernate(String first, String last) {
        QPerson person = QPerson.person;
        JPQLQuery<Person> query = new HibernateQuery<Person>(sessionFactory.getCurrentSession())
                .from(person)
                .where(person.firstName.eq(first)
                        .and(person.lastName.eq(last)));
        return query.fetch();
    }
}
