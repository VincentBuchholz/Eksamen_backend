package facades;

import dtos.HouseDTO;
import entities.House;

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

    public HouseDTO createHouse(HouseDTO houseDTO) {
        EntityManager em = getEntityManager();
        House house = new House(houseDTO.getAddress(), houseDTO.getCity(), houseDTO.getRooms(), houseDTO.getImg());
        try{
            em.getTransaction().begin();
            em.persist(house);
            em.getTransaction().commit();
            return new HouseDTO(house);
        } finally {
            em.close();
        }
    }

    public HouseDTO getHouseByID(int houseID) {
        EntityManager em = getEntityManager();
        try{
            return new HouseDTO(em.find(House.class,houseID));
        } finally {
            em.close();
        }
    }
}
