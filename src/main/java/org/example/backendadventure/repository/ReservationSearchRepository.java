package org.example.backendadventure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.backendadventure.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class ReservationSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reservation> search(Map<String, String> searchCriteria) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> query = builder.createQuery(Reservation.class);
        Root<Reservation> entity = query.from(Reservation.class);

        Predicate[] predicates = new Predicate[searchCriteria.size()];
        Set<String> fetch = new HashSet<>();
        int i = 0;
        for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("date")) {
                predicates[i++] = builder.equal(entity.get(key), LocalDate.parse(value));
            } else if (key.contains(".")) {
                int index = key.indexOf('.');
                String child = key.substring(0, index);
                fetch.add(child);
                String childKey = key.substring(index + 1);
                predicates[i++] = builder.equal(entity.get(child).get(childKey), value);
            } else {
                predicates[i++] = builder.equal(entity.get(key), value);
            }
        }
        fetch.forEach(child -> entity.fetch(child, JoinType.LEFT));

        query.select(entity).where(builder.and(predicates));
        return entityManager.createQuery(query).getResultList();
    }

}
