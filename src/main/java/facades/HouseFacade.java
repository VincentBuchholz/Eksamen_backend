package facades;

import dtos.HouseDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class HouseFacade {
    private static HouseFacade instance;
    private static EntityManagerFactory emf;


    public static HouseFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HouseFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<HouseDTO> getAllHouses() {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<HouseDTO> query = em.createQuery("SELECT new dtos.HouseDTO(h) FROM House h", HouseDTO.class);
            List<HouseDTO> houseDTOs = query.getResultList();
            return houseDTOs;
        } finally {
            em.close();
        }
    }
}
